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
public class RandGameApproxMonteCarloAgent implements GoAgent
{
	private Type myType;
	private final int numApproxRounds = 3;
	private final int numApproxPieces = 120;
	private int numGames = 300;

	/**
	 * @param type
	 */
	public RandGameApproxMonteCarloAgent(Type type)
	{
		super();
		this.myType = type;
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
					int score = MoveEvaluator.randGameApproxAveValMCScore(bs, row, col, this.myType, numGames, numApproxRounds, numApproxPieces, System.currentTimeMillis());
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
