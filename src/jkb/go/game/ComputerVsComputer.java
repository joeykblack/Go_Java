/**
 * 
 */
package jkb.go.game;

import jkb.go.ai.GoAgent;
import jkb.go.ai.impl.GoRandomAgent;
import jkb.go.ai.impl.ThreadedRandGameApproxMonteCarloAgent;
import jkb.go.helper.Converter;
import jkb.go.helper.evaluator.AreaEvaluator;
import jkb.go.model.Type;
import jkb.go.model.boardstate.BoardState;
import jkb.go.model.boardstate.BoardStateBitMaps;

/**
 * @author joey
 *
 */
public class ComputerVsComputer
{

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		System.out.println("Computer vs Computer");
		
		boolean play = true;
		BoardState boardState = new BoardStateBitMaps();
		GoAgent agent1 = new GoRandomAgent(Type.BLACK);
		long totalTime1 = 0;
		GoAgent agent2 = new ThreadedRandGameApproxMonteCarloAgent(Type.WHITE);
		long totalTime2 = 0;
		
		int numRounds = 0;
		
		long start, end;
		int rank;
		char file;

		try
		{
		
			exit:
			while (play)
			{
				// AI 1 move
				start = System.currentTimeMillis();
				String ai1move = agent1.play(boardState);
				end = System.currentTimeMillis();
				totalTime1 += end-start;
				rank = Converter.getRank(ai1move);
				file = Converter.getFile(ai1move);
				boardState.play(rank, file, Type.BLACK);
				System.out.println("Agent 1 total time: " + totalTime1);
				
				// AI 2 move
				start = System.currentTimeMillis();
				String ai2move = agent2.play(boardState);
				end = System.currentTimeMillis();
				totalTime2 += end-start;
				rank = Converter.getRank(ai2move);
				file = Converter.getFile(ai2move);
				boardState.play(rank, file, Type.WHITE);
				System.out.println("Agent 2 total time: " + totalTime2);
				
				numRounds++;
				
				// is game over
				if (numRounds>150) break exit;
			}

			// Print board
			System.out.println();
			System.out.println(boardState.toString());
			
			// print results
			int[] score = AreaEvaluator.evaluateArea((BoardStateBitMaps) boardState);
			long blackAverageTime = totalTime1/numRounds;
			long whiteAverageTime = totalTime2/numRounds;
			System.out.println();
			System.out.println("Black: score="+score[0] + " average time="+blackAverageTime + " type=" + agent1.getClass().getName());
			System.out.println("White: score="+score[1] + " average time="+whiteAverageTime + " type=" + agent2.getClass().getName());
			System.out.println("Rounds="+numRounds);
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
		System.out.println();
		System.out.println("Exiting");
	} // end main
	
	
	
	/*

Black (dumb): score=58 average time=10537
White (aprox): score=47 average time=411
rounds=50
Note: almost all plays at bottom of board. Black played
	a few in the oppen.


Black: score=49 average time=0 type=jkb.go.ai.impl.GoRandomAgent
White: score=77 average time=4121 type=jkb.go.ai.impl.ThreadedBruteForceMonteCarloAgent (300)
rounds=50
Note: ThreadedBruteForceMonteCarloAgentplayed mostly at bottom by building dimonds

Black: score=52 average time=3438 type=jkb.go.ai.impl.ThreadedRandGameApproxMonteCarloAgent (1000,120,3)
White: score=45 average time=4558 type=jkb.go.ai.impl.ThreadedBruteForceMonteCarloAgent (300)
rounds=50
Note: Black dominated bottom left and center. White dominated bottom right. A few scattered 
	moves by both around board.
	
Black: score=39 average time=4637 type=jkb.go.ai.impl.ThreadedBruteForceMonteCarloAgent (300)
White: score=42 average time=3518 type=jkb.go.ai.impl.ThreadedRandGameApproxMonteCarloAgent (1000,120,3)
Rounds=51
	
Black: score=66 average time=3768 type=jkb.go.ai.impl.ThreadedRandGameApproxMonteCarloAgent (1000,120,3)
White: score=47 average time=0 type=jkb.go.ai.impl.GoRandomAgent
Rounds=51
Note: Black dominated bottom.

Black: score=48 average time=0 type=jkb.go.ai.impl.GoRandomAgent
White: score=71 average time=3731 type=jkb.go.ai.impl.ThreadedRandGameApproxMonteCarloAgent (1000,120,3)
Rounds=51

Black: score=66 average time=3785 type=jkb.go.ai.impl.ThreadedRandGameApproxMonteCarloAgent (1000,120,3)
White: score=48 average time=3742 type=jkb.go.ai.impl.ThreadedRandGameApproxMonteCarloMiniMaxAgent (1000,120,3)
Rounds=51

Black: score=48 average time=0 type=jkb.go.ai.impl.GoRandomAgent
White: score=70 average time=1078 type=jkb.go.ai.impl.ThreadedRandGameApproxMonteCarloAgent (100, 20, 4)
Rounds=51
Black: score=48 average time=0 type=jkb.go.ai.impl.GoRandomAgent
White: score=68 average time=1079 type=jkb.go.ai.impl.ThreadedRandGameApproxMonteCarloAgent
Rounds=51
Black: score=48 average time=0 type=jkb.go.ai.impl.GoRandomAgent
White: score=68 average time=1072 type=jkb.go.ai.impl.ThreadedRandGameApproxMonteCarloAgent
Rounds=51
Black: score=46 average time=0 type=jkb.go.ai.impl.GoRandomAgent
White: score=69 average time=1094 type=jkb.go.ai.impl.ThreadedRandGameApproxMonteCarloAgent
Rounds=51
Black: score=49 average time=0 type=jkb.go.ai.impl.GoRandomAgent
White: score=71 average time=1062 type=jkb.go.ai.impl.ThreadedRandGameApproxMonteCarloAgent
Rounds=51
Black: score=48 average time=0 type=jkb.go.ai.impl.GoRandomAgent
White: score=69 average time=1102 type=jkb.go.ai.impl.ThreadedRandGameApproxMonteCarloAgent
Rounds=51
Black: score=49 average time=0 type=jkb.go.ai.impl.GoRandomAgent
White: score=70 average time=1075 type=jkb.go.ai.impl.ThreadedRandGameApproxMonteCarloAgent
Rounds=51

	 */
	
	

}
