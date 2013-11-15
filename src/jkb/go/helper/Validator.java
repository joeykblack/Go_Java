/**
 * 
 */
package jkb.go.helper;

import jkb.go.model.BitBoard;
import jkb.go.model.Group;
import jkb.go.model.boardstate.BoardStateBitGroups;


/**
 * @author joey
 *
 */
public class Validator
{


	/*
	 * BoardStateBitGroups methods
	 */
	/**
	 * @param row
	 * @param col
	 * @param isWhite
	 * @return
	 */
	public static boolean validate(BoardStateBitGroups bs, Group newGroup, boolean isWhite)
	{
		return !isOccupied(newGroup.getPositions(), bs.getBlackOccupied(), bs.getWhiteOccupied()) &&
			   !isKo(newGroup.getPositions(), bs.getLastCapturedGroup());
	}	/**
	 * @param lastCapturedGroup 
	 * @param row
	 * @param col
	 * @param isWhite
	 * @return
	 */
	private static boolean isKo(BitBoard move, BitBoard lastCapturedGroup)
	{
		return (lastCapturedGroup!=null) && lastCapturedGroup.equals(move);
	}

	/**
	 * @param whiteOccupied 
	 * @param blackOccupied 
	 * @param row
	 * @param col
	 * @param isWhite
	 * @return
	 */
	private static boolean isOccupied(BitBoard bb, BitBoard blackOccupied, BitBoard whiteOccupied)
	{
		return blackOccupied.or(whiteOccupied).overlaps(bb);
	}
	
	/**
	 * @param row
	 * @param col
	 * @param isWhite
	 * @return
	 */
	public static boolean isSuicide(BoardStateBitGroups bs, Group newGroup, boolean isWhite)
	{
		return newGroup.getAdjacent().disjoint( bs.getOccupied(!isWhite).not() );
	}
	
	
	
	
	
	
	/*
	 * BoardStateBitMaps methods 
	 */
	
	public static boolean validate(int row, int col, BitBoard player, BitBoard defender, BitBoard lastdead)
	{
		return !player.or(defender).get(row, col) // is not occupied
			&& !(lastdead.get(row, col) && lastdead.count()==1); 
			// TODO: absolute ko
	}
	
	/**
	 * @param boardStateBitMaps
	 * @param row
	 * @param col
	 * @return
	 */
	public static boolean isSuicide(BitBoard player, BitBoard defender, int row, int col)
	{
		boolean suicide = false;
		
		BitBoard last = new BitBoard();
		BitBoard newGroup = new BitBoard();
		newGroup.set(row, col, true);
		
		BitBoard empty = player.nor(defender);
		
		// expand until we hit an empty space or we cannot expand further
		do
		{
			last.init(newGroup);
			newGroup = BitBoardComplexOpps.expandBlocking(newGroup, defender);
		} while ( (suicide=newGroup.disjoint(empty)) && !last.equals(newGroup) );
		
		return suicide;
	}

}
