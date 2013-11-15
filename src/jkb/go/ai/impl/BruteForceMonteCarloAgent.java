/**
 * 
 */
package jkb.go.ai.impl;

import jkb.go.ai.GoAgent;
import jkb.go.helper.Converter;
import jkb.go.helper.evaluator.MoveEvaluator;
import jkb.go.model.BitBoard;
import jkb.go.model.Type;
import jkb.go.model.boardstate.BoardState;
import jkb.go.model.boardstate.BoardStateBitMaps;

/**
 * @author joey
 *
 */
public class BruteForceMonteCarloAgent implements GoAgent
{
	private Type myType;
	private int numGamesPerMove;

	/**
	 * @param type
	 */
	public BruteForceMonteCarloAgent(Type type)
	{
		super();
		this.myType = type;
		this.numGamesPerMove = 300;
	}

	/* (non-Javadoc)
	 * @see jkb.go.ai.GoAgent#play(jkb.go.model.boardstate.BoardState)
	 */
	@Override
	public String play(BoardState bs)
	{
		return (bs instanceof BoardStateBitMaps) ? playBoardStateBitMaps((BoardStateBitMaps)bs) : null;
	}

	/**
	 * @param bs
	 * @return
	 */
	private String playBoardStateBitMaps(BoardStateBitMaps bs)
	{
		BitBoard empty = bs.getBlack().nor(bs.getWhite());
		int bestRow = 0;
		int bestCol = 0;
		int bestScore = 0;
		
		for (int row = 0; row < 19; row++)
		{
			for (int col = 0; col < 19; col++)
			{
				if (empty.get(row, col))
				{
					int score = MoveEvaluator.bruteForceMCAveScore(bs, row, col, this.myType, numGamesPerMove, System.currentTimeMillis());
					if (score>bestScore)
					{
						bestScore = score;
						bestRow = row;
						bestCol = col;
					}
				}
			}
		}
		
		return Converter.rowColToString(bestRow, bestCol);
	}

}
