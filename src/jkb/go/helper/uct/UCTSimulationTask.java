/**
 * 
 */
package jkb.go.helper.uct;

import jkb.go.helper.evaluator.MoveEvaluator;
import jkb.go.model.BitBoard;
import jkb.go.model.Type;
import jkb.go.model.boardstate.BoardStateBitMaps;
import jkb.go.model.uct.Node;

/**
 * @author joey
 *
 */
public class UCTSimulationTask implements Runnable
{
	public static final double UCTK = 0.5;
	private Node root;
	private BoardStateBitMaps bs;
	private Type type;
	

	/**
	 * @param root
	 * @param bs
	 * @param type
	 */
	public UCTSimulationTask(Node root, BoardStateBitMaps bs, Type type)
	{
		super();
		this.root = root;
		this.bs = bs;
		this.type = type;
	}




	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run()
	{
		playSimulation(root, bs, type);
	}
	
	

	
	private int playSimulation(Node root, BoardStateBitMaps bs, Type type)
	{
		int win = 0;
		if (root.visits<5 && root.row!=-1)
		{
			win = randomGame(root, bs, type);
		}
		else
		{
			synchronized (root)
			{
				if (root.child==null)
				{
					populateChildren(root, bs, type);
				}
			}
			
			Node next = UCTSelector(root, bs, type);

			BoardStateBitMaps nextState = new BoardStateBitMaps(bs);
			nextState.playRowCol(next.row, next.col, type);
			
			win = 1 - playSimulation(next, nextState, type.inverse());
		}
		
		synchronized (root)
		{
			root.visits++;
			root.wins += win;
		
			// Set root.best to child with most wins if children exist
			Node next = root.child;
			while (next!=null)
			{
				if (root.best==null || root.best.wins<next.wins)
				{
					root.best = next;
				}
				next = next.sybling;
			}	
		}
		
		return win;
	}
	
	private Node UCTSelector(Node root, BoardStateBitMaps bs, Type type)
	{
		Node result = null;
		double bestUct = 0;
		Node next = root.child;
		
		while (next!=null)
		{
			double uct = 0;
			if (next.visits > 0)
			{
				double winrate = next.wins/next.visits;
				uct = UCTK*Math.sqrt(Math.log(root.visits)/(5*next.visits)) + winrate;
			}
			else
			{
				uct = 10000 + 1000*Math.random();
			}
			
			if (uct > bestUct)
			{
				bestUct = uct;
				result = next;
			}
			
			next = next.sybling;
		}
		
		return result;
	}

	/**
	 * @param root
	 * @param bs
	 * @param type
	 */
	private void populateChildren(Node root, BoardStateBitMaps bs, Type type)
	{
		BitBoard empty = bs.getBlack().nor(bs.getWhite());
		Node child = null;
		
		for (int row = 0; row < 19; row++)
		{
			for (int col = 0; col < 19; col++)
			{
				if (empty.get(row, col))
				{
					if (root.child==null)
					{
						root.child = new Node(row, col);
						child = root.child;
					}
					else
					{
						child.sybling = new Node(row, col);
						child = child.sybling;
					}
				}
			}
		}
	}

	/**
	 * @param root
	 * @param bs
	 * @param type
	 * @return
	 */
	private int randomGame(Node root, BoardStateBitMaps bs, Type type)
	{
		return MoveEvaluator.randGameApprox(bs, root.row, root.col, type, 3, 20, System.currentTimeMillis());
	}

}
