/**
 * 
 */
package jkb.go.helper;

import jkb.go.model.BitBoard;
import jkb.go.resources.BitBoardConst;

/**
 * @author joey
 *
 */
public class BitBoardComplexOpps
{
	

	
	public static BitBoard explode(BitBoard bb)
	{
		BitBoard newBB = new BitBoard();

		newBB.board0 = (( (bb.board0 >>> 1 | (bb.board1 << 63)) & BitBoardConst.FILE_T_NEGATED.board0 )
					| ( (bb.board0 << 1) & BitBoardConst.FILE_A_NEGATED.board0 )
					| ( bb.board0 << 19 )
					| ( (bb.board0 >>> 19) | (bb.board1 << 45) ))
					& ~bb.board0;
		
		newBB.board1 = (( ((bb.board1 >>> 1) | (bb.board2 << 63)) & BitBoardConst.FILE_T_NEGATED.board1 )
					| ( ((bb.board1 << 1) | (bb.board0 >>> 63)) & BitBoardConst.FILE_A_NEGATED.board1 )
					| ( (bb.board1 << 19) | (bb.board0 >>> 45) )
					| ( (bb.board1 >>> 19) | (bb.board2 << 45) ))
					& ~bb.board1;
		
		newBB.board2 = (( ((bb.board2 >>> 1) | (bb.board3 << 63)) & BitBoardConst.FILE_T_NEGATED.board2 )
					| ( ((bb.board2 << 1) | (bb.board1 >>> 63)) & BitBoardConst.FILE_A_NEGATED.board2 )
					| ( (bb.board2 << 19) | (bb.board1 >>> 45) )
					| ( (bb.board2 >>> 19) | (bb.board3 << 45) ))
					& ~bb.board2;
		
		newBB.board3 = (( ((bb.board3 >>> 1) | (bb.board4 << 63)) & BitBoardConst.FILE_T_NEGATED.board3 )
					| ( ((bb.board3 << 1) | (bb.board2 >>> 63)) & BitBoardConst.FILE_A_NEGATED.board3 )
					| ( (bb.board3 << 19) | (bb.board2 >>> 45) )
					| ( (bb.board3 >>> 19) | (bb.board4 << 45) ))
					& ~bb.board3;
		
		newBB.board4 = (( ((bb.board4 >>> 1) | (bb.board5 << 63)) & BitBoardConst.FILE_T_NEGATED.board4 )
					| ( ((bb.board4 << 1) | (bb.board3 >>> 63)) & BitBoardConst.FILE_A_NEGATED.board4 )
					| ( (bb.board4 << 19) | (bb.board3 >>> 45) )
					| ( (bb.board4 >>> 19) | (bb.board5 << 45) ))
					& ~bb.board4;
		
		newBB.board5 = (( (bb.board5 >>> 1) & BitBoardConst.FILE_T_NEGATED.board5 )
					 | ( ((bb.board5 << 1) | (bb.board4 >>> 63)) & BitBoardConst.FILE_A_NEGATED.board5 )
					 | ( (bb.board5 << 19) | (bb.board4 >>> 45) )
					 | ( (bb.board5 >>> 19) ))
					& ~bb.board5
					& BitBoardConst.USED_BITS;
		
		return newBB;
	}
	
	public static BitBoard explodeBlocking(BitBoard bb, BitBoard blocker)
	{
		BitBoard newBB = new BitBoard();

		newBB.board0 = (( (bb.board0 >>> 1 | (bb.board1 << 63)) & BitBoardConst.FILE_T_NEGATED.board0 )
					| ( (bb.board0 << 1) & BitBoardConst.FILE_A_NEGATED.board0 )
					| ( bb.board0 << 19 )
					| ( (bb.board0 >>> 19) | (bb.board1 << 45) ))
					& ~bb.board0
					& ~blocker.board0;
		
		newBB.board1 = (( ((bb.board1 >>> 1) | (bb.board2 << 63)) & BitBoardConst.FILE_T_NEGATED.board1 )
					| ( ((bb.board1 << 1) | (bb.board0 >>> 63)) & BitBoardConst.FILE_A_NEGATED.board1 )
					| ( (bb.board1 << 19) | (bb.board0 >>> 45) )
					| ( (bb.board1 >>> 19) | (bb.board2 << 45) ))
					& ~bb.board1
					& ~blocker.board1;
		
		newBB.board2 = (( ((bb.board2 >>> 1) | (bb.board3 << 63)) & BitBoardConst.FILE_T_NEGATED.board2 )
					| ( ((bb.board2 << 1) | (bb.board1 >>> 63)) & BitBoardConst.FILE_A_NEGATED.board2 )
					| ( (bb.board2 << 19) | (bb.board1 >>> 45) )
					| ( (bb.board2 >>> 19) | (bb.board3 << 45) ))
					& ~bb.board2
					& ~blocker.board2;
		
		newBB.board3 = (( ((bb.board3 >>> 1) | (bb.board4 << 63)) & BitBoardConst.FILE_T_NEGATED.board3 )
					| ( ((bb.board3 << 1) | (bb.board2 >>> 63)) & BitBoardConst.FILE_A_NEGATED.board3 )
					| ( (bb.board3 << 19) | (bb.board2 >>> 45) )
					| ( (bb.board3 >>> 19) | (bb.board4 << 45) ))
					& ~bb.board3
					& ~blocker.board3;
		
		newBB.board4 = (( ((bb.board4 >>> 1) | (bb.board5 << 63)) & BitBoardConst.FILE_T_NEGATED.board4 )
					| ( ((bb.board4 << 1) | (bb.board3 >>> 63)) & BitBoardConst.FILE_A_NEGATED.board4 )
					| ( (bb.board4 << 19) | (bb.board3 >>> 45) )
					| ( (bb.board4 >>> 19) | (bb.board5 << 45) ))
					& ~bb.board4
					& ~blocker.board4;
		
		newBB.board5 = (( (bb.board5 >>> 1) & BitBoardConst.FILE_T_NEGATED.board5 )
					 | ( ((bb.board5 << 1) | (bb.board4 >>> 63)) & BitBoardConst.FILE_A_NEGATED.board5 )
					 | ( (bb.board5 << 19) | (bb.board4 >>> 45) )
					 | ( (bb.board5 >>> 19) ))
					& ~bb.board5
					& ~blocker.board5
					& BitBoardConst.USED_BITS;
		
		return newBB;
	}
	
	public static BitBoard explodeContaining(BitBoard bb, BitBoard container)
	{
		BitBoard newBB = new BitBoard();

		newBB.board0 = (( (bb.board0 >>> 1 | (bb.board1 << 63)) & BitBoardConst.FILE_T_NEGATED.board0 )
					| ( (bb.board0 << 1) & BitBoardConst.FILE_A_NEGATED.board0 )
					| ( bb.board0 << 19 )
					| ( (bb.board0 >>> 19) | (bb.board1 << 45) ))
					& ~bb.board0
					& container.board0;
		
		newBB.board1 = (( ((bb.board1 >>> 1) | (bb.board2 << 63)) & BitBoardConst.FILE_T_NEGATED.board1 )
					| ( ((bb.board1 << 1) | (bb.board0 >>> 63)) & BitBoardConst.FILE_A_NEGATED.board1 )
					| ( (bb.board1 << 19) | (bb.board0 >>> 45) )
					| ( (bb.board1 >>> 19) | (bb.board2 << 45) ))
					& ~bb.board1
					& container.board1;
		
		newBB.board2 = (( ((bb.board2 >>> 1) | (bb.board3 << 63)) & BitBoardConst.FILE_T_NEGATED.board2 )
					| ( ((bb.board2 << 1) | (bb.board1 >>> 63)) & BitBoardConst.FILE_A_NEGATED.board2 )
					| ( (bb.board2 << 19) | (bb.board1 >>> 45) )
					| ( (bb.board2 >>> 19) | (bb.board3 << 45) ))
					& ~bb.board2
					& container.board2;
		
		newBB.board3 = (( ((bb.board3 >>> 1) | (bb.board4 << 63)) & BitBoardConst.FILE_T_NEGATED.board3 )
					| ( ((bb.board3 << 1) | (bb.board2 >>> 63)) & BitBoardConst.FILE_A_NEGATED.board3 )
					| ( (bb.board3 << 19) | (bb.board2 >>> 45) )
					| ( (bb.board3 >>> 19) | (bb.board4 << 45) ))
					& ~bb.board3
					& container.board3;
		
		newBB.board4 = (( ((bb.board4 >>> 1) | (bb.board5 << 63)) & BitBoardConst.FILE_T_NEGATED.board4 )
					| ( ((bb.board4 << 1) | (bb.board3 >>> 63)) & BitBoardConst.FILE_A_NEGATED.board4 )
					| ( (bb.board4 << 19) | (bb.board3 >>> 45) )
					| ( (bb.board4 >>> 19) | (bb.board5 << 45) ))
					& ~bb.board4
					& container.board4;
		
		newBB.board5 = (( (bb.board5 >>> 1) & BitBoardConst.FILE_T_NEGATED.board5 )
					 | ( ((bb.board5 << 1) | (bb.board4 >>> 63)) & BitBoardConst.FILE_A_NEGATED.board5 )
					 | ( (bb.board5 << 19) | (bb.board4 >>> 45) )
					 | ( (bb.board5 >>> 19) ))
					& ~bb.board5
					& container.board5
					& BitBoardConst.USED_BITS;
		
		return newBB;
	}
	
	public static BitBoard expand(BitBoard bb)
	{
		BitBoard newBB = new BitBoard();

		newBB.board0 = ( ((bb.board0 >>> 1) | (bb.board1 << 63)) & BitBoardConst.FILE_T_NEGATED.board0 )
					| ( (bb.board0 << 1) & BitBoardConst.FILE_A_NEGATED.board0 )
					| ( bb.board0 << 19 )
					| ( (bb.board0 >>> 19) | (bb.board1 << 45) )
					| bb.board0;
		
		newBB.board1 = ( ((bb.board1 >>> 1) | (bb.board2 << 63)) & BitBoardConst.FILE_T_NEGATED.board1 )
					| ( ((bb.board1 << 1) | (bb.board0 >>> 63)) & BitBoardConst.FILE_A_NEGATED.board1 )
					| ( (bb.board1 << 19) | (bb.board0 >>> 45) )
					| ( (bb.board1 >>> 19) | (bb.board2 << 45) )
					| bb.board1;
		
		newBB.board2 = ( ((bb.board2 >>> 1) | (bb.board3 << 63)) & BitBoardConst.FILE_T_NEGATED.board2 )
					| ( ((bb.board2 << 1) | (bb.board1 >>> 63)) & BitBoardConst.FILE_A_NEGATED.board2 )
					| ( (bb.board2 << 19) | (bb.board1 >>> 45) )
					| ( (bb.board2 >>> 19) | (bb.board3 << 45) )
					| bb.board2;
		
		newBB.board3 = ( ((bb.board3 >>> 1) | (bb.board4 << 63)) & BitBoardConst.FILE_T_NEGATED.board3 )
					| ( ((bb.board3 << 1) | (bb.board2 >>> 63)) & BitBoardConst.FILE_A_NEGATED.board3 )
					| ( (bb.board3 << 19) | (bb.board2 >>> 45) )
					| ( (bb.board3 >>> 19) | (bb.board4 << 45) )
					| bb.board3;
		
		newBB.board4 = ( ((bb.board4 >>> 1) | (bb.board5 << 63)) & BitBoardConst.FILE_T_NEGATED.board4 )
					| ( ((bb.board4 << 1) | (bb.board3 >>> 63)) & BitBoardConst.FILE_A_NEGATED.board4 )
					| ( (bb.board4 << 19) | (bb.board3 >>> 45) )
					| ( (bb.board4 >>> 19) | (bb.board5 << 45) )
					| bb.board4;
		
		newBB.board5 = (( (bb.board5 >>> 1) & BitBoardConst.FILE_T_NEGATED.board5 )
					 | ( ((bb.board5 << 1) | (bb.board4 >>> 63)) & BitBoardConst.FILE_A_NEGATED.board5 )
					 | ( (bb.board5 << 19) | (bb.board4 >>> 45) )
					 | ( (bb.board5 >>> 19) )
					 | bb.board5 )
					& BitBoardConst.USED_BITS;
		
		return newBB;
	}
	
	public static BitBoard expandBlocking(BitBoard bb, BitBoard blocker)
	{
		BitBoard newBB = new BitBoard();

		newBB.board0 = (( (bb.board0 >>> 1 | (bb.board1 << 63)) & BitBoardConst.FILE_T_NEGATED.board0 )
					| ( (bb.board0 << 1) & BitBoardConst.FILE_A_NEGATED.board0 )
					| ( bb.board0 << 19 )
					| ( (bb.board0 >>> 19) | (bb.board1 << 45) )
					| bb.board0)
					& ~blocker.board0;
		
		newBB.board1 = (( ((bb.board1 >>> 1) | (bb.board2 << 63)) & BitBoardConst.FILE_T_NEGATED.board1 )
					| ( ((bb.board1 << 1) | (bb.board0 >>> 63)) & BitBoardConst.FILE_A_NEGATED.board1 )
					| ( (bb.board1 << 19) | (bb.board0 >>> 45) )
					| ( (bb.board1 >>> 19) | (bb.board2 << 45) )
					| bb.board1)
					& ~blocker.board1;
		
		newBB.board2 = (( ((bb.board2 >>> 1) | (bb.board3 << 63)) & BitBoardConst.FILE_T_NEGATED.board2 )
					| ( ((bb.board2 << 1) | (bb.board1 >>> 63)) & BitBoardConst.FILE_A_NEGATED.board2 )
					| ( (bb.board2 << 19) | (bb.board1 >>> 45) )
					| ( (bb.board2 >>> 19) | (bb.board3 << 45) )
					| bb.board2)
					& ~blocker.board2;
		
		newBB.board3 = (( ((bb.board3 >>> 1) | (bb.board4 << 63)) & BitBoardConst.FILE_T_NEGATED.board3 )
					| ( ((bb.board3 << 1) | (bb.board2 >>> 63)) & BitBoardConst.FILE_A_NEGATED.board3 )
					| ( (bb.board3 << 19) | (bb.board2 >>> 45) )
					| ( (bb.board3 >>> 19) | (bb.board4 << 45) )
					| bb.board3)
					& ~blocker.board3;
		
		newBB.board4 = (( ((bb.board4 >>> 1) | (bb.board5 << 63)) & BitBoardConst.FILE_T_NEGATED.board4 )
					| ( ((bb.board4 << 1) | (bb.board3 >>> 63)) & BitBoardConst.FILE_A_NEGATED.board4 )
					| ( (bb.board4 << 19) | (bb.board3 >>> 45) )
					| ( (bb.board4 >>> 19) | (bb.board5 << 45) )
					| bb.board4)
					& ~blocker.board4;
		
		newBB.board5 = (( (bb.board5 >>> 1) & BitBoardConst.FILE_T_NEGATED.board5 )
					 | ( ((bb.board5 << 1) | (bb.board4 >>> 63)) & BitBoardConst.FILE_A_NEGATED.board5 )
					 | ( (bb.board5 << 19) | (bb.board4 >>> 45) )
					 | ( (bb.board5 >>> 19) )
					 | bb.board5 )
					& ~blocker.board5
					& BitBoardConst.USED_BITS;
		
		return newBB;
	}
	
	public static BitBoard expandContaining(BitBoard bb, BitBoard container)
	{
		BitBoard newBB = new BitBoard();

		newBB.board0 = (( ((bb.board0 >>> 1) | (bb.board1 << 63)) & BitBoardConst.FILE_T_NEGATED.board0 )
					| ( (bb.board0 << 1) & BitBoardConst.FILE_A_NEGATED.board0 )
					| ( bb.board0 << 19 )
					| ( (bb.board0 >>> 19) | (bb.board1 << 45) )
					| bb.board0)
					& container.board0;
		
		newBB.board1 = (( ((bb.board1 >>> 1) | (bb.board2 << 63)) & BitBoardConst.FILE_T_NEGATED.board1 )
					| ( ((bb.board1 << 1) | (bb.board0 >>> 63)) & BitBoardConst.FILE_A_NEGATED.board1 )
					| ( (bb.board1 << 19) | (bb.board0 >>> 45) )
					| ( (bb.board1 >>> 19) | (bb.board2 << 45) )
					| bb.board1)
					& container.board1;
		
		newBB.board2 = (( ((bb.board2 >>> 1) | (bb.board3 << 63)) & BitBoardConst.FILE_T_NEGATED.board2 )
					| ( ((bb.board2 << 1) | (bb.board1 >>> 63)) & BitBoardConst.FILE_A_NEGATED.board2 )
					| ( (bb.board2 << 19) | (bb.board1 >>> 45) )
					| ( (bb.board2 >>> 19) | (bb.board3 << 45) )
					| bb.board2)
					& container.board2;
		
		newBB.board3 = (( ((bb.board3 >>> 1) | (bb.board4 << 63)) & BitBoardConst.FILE_T_NEGATED.board3 )
					| ( ((bb.board3 << 1) | (bb.board2 >>> 63)) & BitBoardConst.FILE_A_NEGATED.board3 )
					| ( (bb.board3 << 19) | (bb.board2 >>> 45) )
					| ( (bb.board3 >>> 19) | (bb.board4 << 45) )
					| bb.board3)
					& container.board3;
		
		newBB.board4 = (( ((bb.board4 >>> 1) | (bb.board5 << 63)) & BitBoardConst.FILE_T_NEGATED.board4 )
					| ( ((bb.board4 << 1) | (bb.board3 >>> 63)) & BitBoardConst.FILE_A_NEGATED.board4 )
					| ( (bb.board4 << 19) | (bb.board3 >>> 45) )
					| ( (bb.board4 >>> 19) | (bb.board5 << 45) )
					| bb.board4)
					& container.board4;
		
		newBB.board5 = (( (bb.board5 >>> 1) & BitBoardConst.FILE_T_NEGATED.board5 )
					 | ( ((bb.board5 << 1) | (bb.board4 >>> 63)) & BitBoardConst.FILE_A_NEGATED.board5 )
					 | ( (bb.board5 << 19) | (bb.board4 >>> 45) )
					 | ( (bb.board5 >>> 19) )
					 | bb.board5 )
					& container.board5
					& BitBoardConst.USED_BITS;
		
		return newBB;
	}

	/**
	 * @param black
	 * @param white
	 * @return
	 */
	public static BitBoard fullExpandBlocking(BitBoard player, BitBoard blocker)
	{
		BitBoard last = new BitBoard(player);
		BitBoard bb = expandBlocking(player, blocker);
		while ( !last.equals(bb) )
		{
			last.init(bb);
			bb = expandBlocking(bb, blocker);
		}
		
		return bb;
	}

}
