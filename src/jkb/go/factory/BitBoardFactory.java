/**
 * 
 */
package jkb.go.factory;

import java.util.Random;

import jkb.go.model.BitBoard;

/**
 * @author joey
 *
 */
public class BitBoardFactory
{
	
	public static BitBoard createRandomBoard (int num, long seed)
	{
		Random r = new Random(seed);
		
		return createRandomBoard(num, r);
	}
	
	public static BitBoard createRandomBoard (int num, Random r)
	{
		BitBoard bb = new BitBoard();
		
		int row;
		int col;
		for (int i = 0; i < num; i++)
		{
			row = r.nextInt(19);
			col = r.nextInt(19);
			bb.set(row, col, true);
		}
		
		return bb;
	}

}
