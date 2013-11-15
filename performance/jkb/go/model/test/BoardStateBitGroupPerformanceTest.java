/**
 * 
 */
package jkb.go.model.test;

import java.util.Random;

import jkb.go.helper.Converter;
import jkb.go.model.Type;
import jkb.go.model.boardstate.BoardState;
import jkb.go.model.boardstate.BoardStateBitGroups;

/**
 * @author joey
 *
 */
public class BoardStateBitGroupPerformanceTest
{
	
	/*
BoardStateCells Performance Test
Time to play 300: 245 mills
Memory used by one BoardStateBit: 33911

BoardStateBit Performance Test
Time to play 300: 225 mills
Memory used by one BoardStateBit: 1770

BoardStateBit Performance Test
Time to play 100000: 33428 mills
Memory used by one BoardStateBit: 774

BoardStateBitMap Performance Test
Time to play 300: 204 mills
Memory used by one BoardStateBit: 73
	 */

	public static void main(String[] args)
	{
		System.out.println("BoardStateBitGroups Performance Test");
	
		int numGames = 300;
		long start = System.currentTimeMillis();
		testPlaySpeed(numGames);
		long end = System.currentTimeMillis();
		System.out.println("Time to play " + numGames + ": " + (end-start) + " mills");
		System.out.println("Memory used by one BoardStateBit: " + testMem());
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
			BoardState bs = new BoardStateBitGroups();
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


	/*
	 * Array board mem:  36740
	 * Bitboard size: 1571
	 */
	public static long testMem()
	{
		int size = 100000;
		long memStart = getMemoryUsed();
		BoardStateBitGroups[] bs = new BoardStateBitGroups[size];
		for (int i = 0; i < bs.length; i++)
		{
			bs[i] = new BoardStateBitGroups();
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
