/**
 * 
 */
package jkb.go.ai.impl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import jkb.go.ai.GoAgent;
import jkb.go.ai.ThreadUpdatableAgent;
import jkb.go.helper.Converter;
import jkb.go.helper.evaluator.BruteForceMoveEvaluatorTask;
import jkb.go.model.BitBoard;
import jkb.go.model.Type;
import jkb.go.model.boardstate.BoardState;
import jkb.go.model.boardstate.BoardStateBitMaps;

/**
 * @author joey
 *
 */
public class ThreadedBruteForceMonteCarloAgent implements GoAgent, ThreadUpdatableAgent
{
	private Type myType;
	private int numGames = 400;
	private ExecutorService pool;
	
	private int bestRow;
	private int bestCol;
	private int bestScore;
	private int[][] scores;
	

	/**
	 * @param type
	 */
	public ThreadedBruteForceMonteCarloAgent(Type type)
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
		pool = Executors.newFixedThreadPool(8);
		bestRow = 0;
		bestCol = 0;
		bestScore = 0;
		scores = new int[19][19];
		
		BitBoard empty = bs.getBlack().nor(bs.getWhite());
		
		for (int row = 0; row < 19; row++)
		{
			for (int col = 0; col < 19; col++)
			{
				if (empty.get(row, col))
				{
					pool.execute( new BruteForceMoveEvaluatorTask(this, bs, row, col, this.myType, numGames) );
				}
			}
		}
		
		try
		{
			pool.shutdown();
			if (!pool.awaitTermination(20, TimeUnit.SECONDS))
			{
				System.out.println("Threads did not end after 20 seconds. Forcing move...");
				pool.shutdownNow();
			}
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		System.out.println();
		for (int i = scores.length-1; i >= 0 ; i--)
		{
			for (int j = 0; j < scores[i].length; j++)
			{
				System.out.print(scores[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
		
		return Converter.rowColToString(bestRow, bestCol);
	}
	
	public synchronized void updateBestMove(int score, int row, int col)
	{
		scores[row][col] = score;
		if (score>bestScore)
		{
			bestScore = score;
			bestRow = row;
			bestCol = col;
		}
	}

}
