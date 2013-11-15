/**
 * 
 */
package jkb.go.helper.uct;

import java.util.Random;

import jkb.go.helper.evaluator.AreaEvaluator;
import jkb.go.model.BitBoard;
import jkb.go.model.Type;
import jkb.go.model.boardstate.BoardStateBitMaps;
import jkb.go.model.uct.Node;

/**
 * @author joey
 *
 */
public class UCTUtilV2
{
	public final double UCTK = 0.5;
	private Random rnd;
	private int scoreIndex;
	private int oppScoreIndex;
	private Type type;
	
	/**
	 * 
	 */
	public UCTUtilV2(Type type)
	{
		this.rnd = new Random(System.currentTimeMillis());
		this.scoreIndex = (type==Type.BLACK) ? 0 : 1;
		this.oppScoreIndex = (type.inverse()==Type.BLACK) ? 0 : 1;
		this.type = type;
	}
	
	public Node UCTSearch(BoardStateBitMaps bs, int depth)
	{
		Node root = new Node(-1, -1);
		root.visits=1;
		
		for (int i = 0; i < depth; i++)
		{
			playSimulation(root, bs);
		}
		
		// Set best to child with most visits
		Node best = root.children[0];
		for (Node next : root.children)
		{
			if (best.visits<next.visits)
			{
				root.best = next;
			}
		}
		
		return best;
	}
	
	private int playSimulation(Node root, BoardStateBitMaps bs)
	{
		int win = 0;
		if (root.visits<5 && root.row!=-1)
		{
			win = randomGame(root, bs);
		}
		else
		{
			if (root.children==null)
			{
				populateChildren(root, bs);
			}
			
			Node next = UCTSelector(root, bs);

			BoardStateBitMaps nextState = new BoardStateBitMaps(bs);
			nextState.playRowCol(next.row, next.col, type);
			
			win = 1 - playSimulation(next, nextState);
		}
		
		root.visits++;
		root.wins += win;
		
		return win;
	}
	
	private Node UCTSelector(Node root, BoardStateBitMaps bs)
	{
		Node result = null;
		double bestUct = -1;
		double uct;
		
		for (Node next : root.children)
		{
			uct = UCTK*Math.sqrt(Math.log(root.visits)/(5*next.visits)) + (next.wins/next.visits);
			
			if (uct > bestUct)
			{
				bestUct = uct;
				result = next;
			}
		}
		
		return result;
	}

	/**
	 * @param root
	 * @param bs
	 * @param type
	 */
	private void populateChildren(Node root, BoardStateBitMaps bs)
	{
		BitBoard empty = bs.getBlack().nor(bs.getWhite());
		root.children = new Node[empty.count()];
		int i = 0;
		
		for (int row = 0; row < 19; row++)
		{
			for (int col = 0; col < 19; col++)
			{
				if (empty.get(row, col))
				{
					Node next = new Node(row, col);
					next.visits = 1;
					next.wins = randomGame(next, bs);
					root.children[i] = next;
					i++;
				}
			}
		}
	}


	/**
	 * @param root
	 * @param temp
	 * @param type
	 * @return
	 */
	private int randomGame(Node root, BoardStateBitMaps bs)
	{
		BoardStateBitMaps temp = new BoardStateBitMaps(bs);
		temp.playRowCol(root.row, root.col, type);
		Type current = type.inverse();
		while (temp.getBlack().count()+temp.getWhite().count() < 300)
		{
			while (!temp.playRowCol(rnd.nextInt(19), rnd.nextInt(19), current)) {} 
			current = current.inverse();
		}
		
		int[] score = AreaEvaluator.evaluateArea(temp);
		
		int result = 0;
		if ( score[scoreIndex]>score[oppScoreIndex] )
		{
			result = 1;
		}
		
		return result;
	}

}
