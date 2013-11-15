/**
 * 
 */
package jkb.go.resources;

import jkb.go.model.BitBoard;

/**
 * @author joey
 *
 */
public class BitBoardConst
{
	
	public static final BitBoard FILE_A = makeFile(0);
	public static final BitBoard FILE_A_NEGATED = makeFile(0).not();

	public static final BitBoard FILE_T = makeFile(18);
	public static final BitBoard FILE_T_NEGATED = makeFile(18).not();
	
	public static final long USED_BITS = ~( (1l << 63) | (1l << 62) | (1l << 61) | (1l << 60) | (1l << 59) | (1l << 58) | (1l << 57) | (1l << 56) | (1l << 55) | (1l << 54)
										  | (1l << 53) | (1l << 52) | (1l << 51) | (1l << 50) | (1l << 49) | (1l << 48) | (1l << 47) | (1l << 46) | (1l << 45) | (1l << 44)
										  | (1l << 43) | (1l << 42) | (1l << 41) );

	/**
	 * @param col
	 * @return
	 */
	private static BitBoard makeFile(int col)
	{
		BitBoard bb = new BitBoard();
		
		for (int index = 0; index < 361; index++)
		{
			if ((index-col) % 19 == 0)
			{
				bb.set(index, true);
			}
		}
		
		return bb;
	}

}
