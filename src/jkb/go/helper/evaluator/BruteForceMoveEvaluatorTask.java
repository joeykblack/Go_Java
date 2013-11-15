/**
 * 
 */
package jkb.go.helper.evaluator;

import jkb.go.ai.ThreadUpdatableAgent;
import jkb.go.model.Type;
import jkb.go.model.boardstate.BoardStateBitMaps;

/**
 * @author joey
 *
 */
public class BruteForceMoveEvaluatorTask implements Runnable
{
	
	private ThreadUpdatableAgent updator;
	private BoardStateBitMaps originalbs; 
	private int testRow;
	private int testCol;
	private Type myType; 
	private int numGames;
	
	

	/**
	 * @param updator
	 * @param originalbs
	 * @param testRow
	 * @param testCol
	 * @param myType
	 * @param numGames
	 * @param numApproxRounds
	 * @param numApproxPieces
	 */
	public BruteForceMoveEvaluatorTask(ThreadUpdatableAgent updator,
			BoardStateBitMaps originalbs, int testRow, int testCol, Type myType,
			int numGames)
	{
		super();
		this.updator = updator;
		this.originalbs = originalbs;
		this.testRow = testRow;
		this.testCol = testCol;
		this.myType = myType;
		this.numGames = numGames;
	}



	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run()
	{
		updator.updateBestMove( MoveEvaluator.bruteForceMCAveScore(originalbs, testRow, testCol, myType, numGames, System.currentTimeMillis()),
				testRow, testCol );
	}

}
