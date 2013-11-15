/**
 * 
 */
package jkb.go.model.iterator;

import jkb.go.model.BitBoard;

/**
 * @author joey
 *
 */
public class BitBoardIterator
{
	
	private final BitBoard[] boards;
	private int index;
	/**
	 * @param boards
	 */
	public BitBoardIterator(BitBoard[] boards)
	{
		super();
		this.boards = boards;
		index = -1;
	}

	public BitBoard nextBoard()
	{
		index++;
		if (index>=boards.length) index=0;
		return boards[index];
	}
}
