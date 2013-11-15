/**
 * 
 */
package jkb.go.helper.evaluator.test;

import jkb.go.helper.evaluator.MoveEvaluator;
import jkb.go.model.Type;
import jkb.go.model.boardstate.BoardStateBitMaps;

/**
 * @author joey
 *
 */
public class MoveEvaluatorComparison
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		BoardStateBitMaps bs = new BoardStateBitMaps();
		bs.play(1, 'A', Type.WHITE);
		bs.play(2, 'A', Type.WHITE);
		bs.play(1, 'B', Type.BLACK);
		bs.play(2, 'B', Type.BLACK);
		
		
		int numGames = 1000;
		int numApproxPieces = 20;
		int numApproxRounds = 4;
		int testRow = 2;
		int testCol = 0;
		Type type = Type.BLACK;
		
		int bruteForceScore = MoveEvaluator.bruteForceMCAveScore(bs, testRow, testCol, type, numGames, 0);
		int randGameAprox = MoveEvaluator.randGameApproxAveValMCScore(bs, testRow, testCol, type, numGames, numApproxRounds, numApproxPieces, 0);
		int randGameAproxMinLoss = MoveEvaluator.randGameApproxMCMinLossScore(bs, testRow, testCol, type, numGames, numApproxRounds, numApproxPieces, 0);
		int randGameAproxWinRate = MoveEvaluator.randGameApproxMCWinRateScore(bs, testRow, testCol, type, numGames, numApproxRounds, numApproxPieces, 0);
		
		System.out.println("Score capture");
		System.out.println("Brute force score: " + bruteForceScore);
		System.out.println("Rand game approx score: " + randGameAprox);
		System.out.println("Rand game approx min loss score: " + randGameAproxMinLoss);
		System.out.println("Rand game approx win rate score: " + randGameAproxWinRate);

		
		testRow = 0;
		testCol = 18;
		type = Type.BLACK;

		bruteForceScore = MoveEvaluator.bruteForceMCAveScore(bs, testRow, testCol, type, numGames, 0);
		randGameAprox = MoveEvaluator.randGameApproxAveValMCScore(bs, testRow, testCol, type, numGames, numApproxRounds, numApproxPieces, 0);
		randGameAproxMinLoss = MoveEvaluator.randGameApproxMCMinLossScore(bs, testRow, testCol, type, numGames, numApproxRounds, numApproxPieces, 0);
		randGameAproxWinRate = MoveEvaluator.randGameApproxMCWinRateScore(bs, testRow, testCol, type, numGames, numApproxRounds, numApproxPieces, 0);
		
		System.out.println();
		System.out.println("Score corner (useless move)");
		System.out.println("Brute force score: " + bruteForceScore);
		System.out.println("Rand game approx score: " + randGameAprox);
		System.out.println("Rand game approx min loss score: " + randGameAproxMinLoss);
		System.out.println("Rand game approx win rate score: " + randGameAproxWinRate);

		
		testRow = 2;
		testCol = 0;
		type = Type.WHITE;

		bruteForceScore = MoveEvaluator.bruteForceMCAveScore(bs, testRow, testCol, type, numGames, 0);
		randGameAprox = MoveEvaluator.randGameApproxAveValMCScore(bs, testRow, testCol, type, numGames, numApproxRounds, numApproxPieces, 0);
		randGameAproxMinLoss = MoveEvaluator.randGameApproxMCMinLossScore(bs, testRow, testCol, type, numGames, numApproxRounds, numApproxPieces, 0);
		randGameAproxWinRate = MoveEvaluator.randGameApproxMCWinRateScore(bs, testRow, testCol, type, numGames, numApproxRounds, numApproxPieces, 0);
		
		System.out.println();
		System.out.println("Score corner escape capture");
		System.out.println("Brute force score: " + bruteForceScore);
		System.out.println("Rand game approx score: " + randGameAprox);
		System.out.println("Rand game approx min loss score: " + randGameAproxMinLoss);
		System.out.println("Rand game approx win rate score: " + randGameAproxWinRate);

		
		testRow = 0;
		testCol = 18;
		type = Type.WHITE;

		bruteForceScore = MoveEvaluator.bruteForceMCAveScore(bs, testRow, testCol, type, numGames, 0);
		randGameAprox = MoveEvaluator.randGameApproxAveValMCScore(bs, testRow, testCol, type, numGames, numApproxRounds, numApproxPieces, 0);
		randGameAproxMinLoss = MoveEvaluator.randGameApproxMCMinLossScore(bs, testRow, testCol, type, numGames, numApproxRounds, numApproxPieces, 0);
		randGameAproxWinRate = MoveEvaluator.randGameApproxMCWinRateScore(bs, testRow, testCol, type, numGames, numApproxRounds, numApproxPieces, 0);
		
		System.out.println();
		System.out.println("Score corner (useless move)");
		System.out.println("Brute force score: " + bruteForceScore);
		System.out.println("Rand game approx score: " + randGameAprox);
		System.out.println("Rand game approx min loss score: " + randGameAproxMinLoss);
		System.out.println("Rand game approx win rate score: " + randGameAproxWinRate);
	}
	
	
	/*


Brute force score: 118
Rand game approx score: 196
Rand game approx min loss score: 162

Brute force score: 117
Rand game approx score: 195
Rand game approx min loss score: 163


	 */
	
	
	
	

}
