/**
 * 
 */
package jkb.go.model.test;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import jkb.go.factory.BitBoardFactory;
import jkb.go.helper.BitBoardGameAction;
import jkb.go.helper.Converter;
import jkb.go.helper.evaluator.AreaEvaluator;
import jkb.go.model.BitBoard;
import jkb.go.model.Type;
import jkb.go.model.boardstate.BoardState;
import jkb.go.model.boardstate.BoardStateBitMaps;

/**
 * @author joey
 *
 */
public class BoardStateBitMapPerformanceTest
{
	
	/*
BoardStateCells Performance Test
Time to play 300: 245 mills
Memory used by one BoardStateBit: 33911

BoardStateBitGroup Performance Test
Time to play 300: 225 mills
Memory used by one BoardStateBit: 1770

BoardStateBitGroup Performance Test
Time to play 100000: 33428 mills
Memory used by one BoardStateBit: 774

BoardStateBitMap Performance Test
Time to play 300: 204 mills
Memory used by one BoardStateBit: 73

BoardStateBitMap Performance Test
Time to play 100000: 10931 mills
Memory used by one BoardStateBit: 62
	 */

	public static void main(String[] args)
	{
		System.out.println("BoardStateBitMaps Performance Test");

		int numGames;
		long start = 0;
		long end = 0;

//		System.out.println();
//		System.out.println("Speed Test With Score");
//		numGames = 300;
//		start = System.currentTimeMillis();
//		testPlaySpeedWithScore(numGames);
//		end = System.currentTimeMillis();
//		System.out.println("Time to play " + numGames + ": " + (end-start) + " mills");
	
//		System.out.println();
//		System.out.println("Speed Test");
//		numGames = 300;
//		start = System.currentTimeMillis();
//		testPlaySpeed(numGames);
//		end = System.currentTimeMillis();
//		System.out.println("Time to play " + numGames + ": " + (end-start) + " mills");
	
//		System.out.println();
//		System.out.println("Speed Test for approximated random games");
//		numGames = 100000;
//		start = System.currentTimeMillis();
//		testApproximationPlaySpeed(numGames);
//		end = System.currentTimeMillis();
//		System.out.println("Time to play " + numGames + ": " + (end-start) + " mills");
	
		System.out.println();
		System.out.println("Speed Test for threaded approximated random games");
		numGames = 10000;
		start = System.currentTimeMillis();
		testThreadedApproximationPlaySpeed(numGames);
		end = System.currentTimeMillis();
		System.out.println("Time to play " + numGames + ": " + (end-start) + " mills");

//		System.out.println();
//		System.out.println("Memory used by one BoardStateBit: " + testMem());
	} 

	
	/*
	 * Array impl:
	 * 361^2 = 0.143 sec
	 * 361^3 = 20 sec
	 * 
	 * Bitboard impl
	 * 361^2 = 0.150 sec
	 * 361^3 = 20.938 sec
	 */
	/**
	 * Test method for {@link jkb.go.model.BoardStateCells#play(int, char, jkb.go.model.Type)}.
	 * @param numGames 
	 */
	public static void testPlaySpeed(int numGames)
	{
		Random r = new Random(0);
		for (int game = 0; game < numGames; game++)
		{
			BoardState bs = new BoardStateBitMaps();
			Type type = Type.BLACK;
			for (int i = 0; i < 361; i++)
			{
				int row = r.nextInt(19);
				int col = r.nextInt(19);
				bs.play(Converter.rowToRank(row), Converter.colToFile(col), type);
				type = type.inverse();
			}
		}
	}
	

	public static void testPlaySpeedWithScore(int numGames)
	{
		Random r = new Random(0);
		for (int game = 0; game < numGames; game++)
		{
			BoardState bs = new BoardStateBitMaps();
			Type type = Type.BLACK;
			for (int i = 0; i < 361; i++)
			{
				int row = r.nextInt(19);
				int col = r.nextInt(19);
				bs.play(Converter.rowToRank(row), Converter.colToFile(col), type);
				type = type.inverse();
				AreaEvaluator.evaluateArea((BoardStateBitMaps) bs); // calculate just so we can time it
			}
		}
	}
	
	
	/**
	 * Approximation of random games
	 * 
	 * @param numGames
	 */
	public static void testApproximationPlaySpeed(int numGames)
	{
		int loops = 3;
		for (int game = 0; game < numGames; game++)
		{
			BoardStateBitMaps bs = new BoardStateBitMaps();
			BitBoard empty = bs.getBlack().nor(bs.getWhite());
			BitBoard randBoard;
			
			for (int i = 0; i < loops; i++)
			{
				int piecesToPlay = 180;//empty.count()/2;
				
				// Scatter black pieces
				randBoard = BitBoardFactory.createRandomBoard(piecesToPlay, i).and(empty);
				bs.setBlack( bs.getBlack().or(randBoard) );
				
				// Update empty spaces
				empty.removeBits(randBoard);
				
				// Scatter white pieces
				randBoard = BitBoardFactory.createRandomBoard(piecesToPlay, i+loops).and(empty);
				bs.setWhite( bs.getWhite().or(randBoard) );

				// Remove black's dead
				bs.setBlack( BitBoardGameAction.removeDead(bs.getWhite(), bs.getBlack()) );
				
				// Remove white's dead
				bs.setWhite( BitBoardGameAction.removeDead(bs.getBlack(), bs.getWhite()) );
				
				// Update empty spaces (for next loop)
				empty.removeBits(randBoard);	
			}
		}
	}
	
	
	/**
	 * Threaded Approximation of random games
	 * 
	 * @param numGames
	 */
	public static void testThreadedApproximationPlaySpeed(int numGames)
	{
		ExecutorService pool = Executors.newFixedThreadPool(8);
		for (int game = 0; game < numGames; game++)
		{
			
			pool.execute( new Runnable() {

				@Override
				public void run()
				{
					int loops = 3;
					BoardStateBitMaps bs = new BoardStateBitMaps();
					BitBoard empty = bs.getBlack().nor(bs.getWhite());
					BitBoard randBoard;
					
					for (int i = 0; i < loops; i++)
					{
						int piecesToPlay = 180;//empty.count()/2;
						
						// Scatter black pieces
						randBoard = BitBoardFactory.createRandomBoard(piecesToPlay, i).and(empty);
						bs.setBlack( bs.getBlack().or(randBoard) );
						
						// Update empty spaces
						empty.removeBits(randBoard);
						
						// Scatter white pieces
						randBoard = BitBoardFactory.createRandomBoard(piecesToPlay, i+loops).and(empty);
						bs.setWhite( bs.getWhite().or(randBoard) );

						// Remove black's dead
						bs.setBlack( BitBoardGameAction.removeDead(bs.getWhite(), bs.getBlack()) );
						
						// Remove white's dead
						bs.setWhite( BitBoardGameAction.removeDead(bs.getBlack(), bs.getWhite()) );
						
						// Update empty spaces (for next loop)
						empty.removeBits(randBoard);	
					}
					return;
				} // end run
					
			}); // end execute
		} // end for games
		
		try
		{
			pool.shutdown();
			if (!pool.awaitTermination(20, TimeUnit.SECONDS))
			{
				System.out.println("Threads did not end after 20 seconds. Aborting...");
				pool.shutdownNow();
			}
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}


	/*
	 * Array board mem:  36740
	 * Bitboard size: 1571
	 */
	public static long testMem()
	{
		int size = 100000;
		long memStart = getMemoryUsed();
		BoardStateBitMaps[] bs = new BoardStateBitMaps[size];
		for (int i = 0; i < bs.length; i++)
		{
			bs[i] = new BoardStateBitMaps();
		}
		long endMem = getMemoryUsed();
		return (endMem-memStart)/size;
	}


	/**
	 * @return
	 */
	private static long getMemoryUsed()
	{
		return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
	}

}
