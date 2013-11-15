/**
 * 
 */
package jkb.go.helper;

import jkb.go.model.BitBoard;

/**
 * @author joey
 *
 */
public class BitBoardGameAction
{
	/**
	 * @param attacker
	 * @param defender
	 * @return updates defender with dead removed
	 */
	public static BitBoard removeDead (BitBoard attacker, BitBoard defender)
	{
		// Find liberties
		BitBoard liberties = BitBoardComplexOpps.explodeBlocking(defender, attacker);
		
		// Find stones adjacent to liberties
		BitBoard stonesAdjacentLiberties = BitBoardComplexOpps.explodeContaining(liberties, defender);
		
		// Expand liberated stones into their groups
		BitBoard last = new BitBoard();
		do
		{
			last.init(stonesAdjacentLiberties);
			stonesAdjacentLiberties = BitBoardComplexOpps.expandContaining(stonesAdjacentLiberties, defender);
		}
		while ( !last.equals(stonesAdjacentLiberties) ); // Continue as long as we are making progress
		
		return stonesAdjacentLiberties;
	}
}
