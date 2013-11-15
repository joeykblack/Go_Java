/**
 * 
 */
package jkb.go.model;

import jkb.go.resources.BitBoardConst;



/**
 * @author joey
 *
 */
public class BitBoard implements Comparable<BitBoard>
{
	public long board0;
	public long board1;
	public long board2;
	public long board3;
	public long board4;
	public long board5;
	
	/**
	 * Constructor
	 */
	public BitBoard()
	{
		board0 = 0l;
		board1 = 0l;
		board2 = 0l;
		board3 = 0l;
		board4 = 0l;
		board5 = 0l;
	}
	
	/**
	 * Copy Constructor
	 * @param original
	 */
	public BitBoard(BitBoard original)
	{
		init(original);
	}
	
	public void init(BitBoard original)
	{
		this.board0 = original.board0;
		this.board1 = original.board1;
		this.board2 = original.board2;
		this.board3 = original.board3;
		this.board4 = original.board4;
		this.board5 = original.board5;
	}
	
	
	/*
	 * Getter and Setter
	 */
	
	public boolean get (int row, int col)
	{
		return get((row*19)+col);
	}
	
	public boolean get (int index)
	{
		long boardSection = getSection(index/64);
		return (boardSection & (1l << (index%64))) != 0;
	}
	
	public void set (int row, int col, boolean value)
	{
		set((row*19)+col, value);
	}
	
	public void set (int index, boolean value)
	{
		int i = index/64;
		long boardSection = getSection(i);
		if (value)
		{
			boardSection |= (1l << (index%64));
		}
		else
		{
			boardSection &= ~(1l << (index%64));
		}
		setSection(i, boardSection);
	}
	
	
	
	/*
	 * Shifting
	 */
	
	public BitBoard shiftForward(int amount)
	{
		BitBoard bb = new BitBoard();

		int revAmount = 64-amount;
		bb.board0 = board0 << amount;
		bb.board1 = (board1 << amount) | (board0 >>> revAmount);
		bb.board2 = (board2 << amount) | (board1 >>> revAmount);
		bb.board3 = (board3 << amount) | (board2 >>> revAmount);
		bb.board4 = (board4 << amount) | (board3 >>> revAmount);
		bb.board5 = (board5 << amount) | (board4 >>> revAmount)&BitBoardConst.USED_BITS;
		
		return bb;
	}

	public BitBoard shiftBack(int amount)
	{
		BitBoard bb = new BitBoard();
		
		int revAmount = 64-amount;
		bb.board0 = board0 >>> amount | (board1 << revAmount);
		bb.board1 = (board1 >>> amount) | (board2 << revAmount);
		bb.board2 = (board2 >>> amount) | (board3 << revAmount);
		bb.board3 = (board3 >>> amount) | (board4 << revAmount);
		bb.board4 = (board4 >>> amount) | (board5 << revAmount);
		bb.board5 = (board5 >>> amount);
		
		return bb;
	}
	
	public BitBoard shiftUp (int amount)
	{
		return shiftForward(amount*19);
	}
	
	public BitBoard shiftDown (int amount)
	{
		return shiftBack(amount*19);
	}
	
	public BitBoard shiftLeftOne ()
	{
		BitBoard bb = shiftBack(1);
		bb.board0 &= BitBoardConst.FILE_T_NEGATED.board0;
		bb.board1 &= BitBoardConst.FILE_T_NEGATED.board1;
		bb.board2 &= BitBoardConst.FILE_T_NEGATED.board2;
		bb.board3 &= BitBoardConst.FILE_T_NEGATED.board3;
		bb.board4 &= BitBoardConst.FILE_T_NEGATED.board4;
		bb.board5 &= BitBoardConst.FILE_T_NEGATED.board5;
		return bb;
	}

	public BitBoard shiftRightOne ()
	{
		BitBoard bb = shiftForward(1);
		bb.board0 &= BitBoardConst.FILE_A_NEGATED.board0;
		bb.board1 &= BitBoardConst.FILE_A_NEGATED.board1;
		bb.board2 &= BitBoardConst.FILE_A_NEGATED.board2;
		bb.board3 &= BitBoardConst.FILE_A_NEGATED.board3;
		bb.board4 &= BitBoardConst.FILE_A_NEGATED.board4;
		bb.board5 &= BitBoardConst.FILE_A_NEGATED.board5;
		return bb;
	}
	
	/**
	 * Any bits shifted into the unused bits area should be errased before
	 * doing any further shifts
	 */
//	private void fixBoard5()
//	{
//		this.board5 = this.board5 & BitBoardConst.USED_BITS;
//	}
	
	/*
	 * Bitwise
	 */
	
	public BitBoard and (BitBoard bb)
	{
		BitBoard newbb = new BitBoard();
		newbb.board0 = bb.board0&this.board0;
		newbb.board1 = bb.board1&this.board1;
		newbb.board2 = bb.board2&this.board2;
		newbb.board3 = bb.board3&this.board3;
		newbb.board4 = bb.board4&this.board4;
		newbb.board5 = bb.board5&this.board5;
		return newbb;
	}
	
	public BitBoard andNot (BitBoard bb)
	{
		BitBoard newbb = new BitBoard();
		newbb.board0 =~bb.board0&this.board0;
		newbb.board1 =~bb.board1&this.board1;
		newbb.board2 =~bb.board2&this.board2;
		newbb.board3 =~bb.board3&this.board3;
		newbb.board4 =~bb.board4&this.board4;
		newbb.board5 =~bb.board5&this.board5&BitBoardConst.USED_BITS;
		return newbb;	
	}
	
	public BitBoard or (BitBoard bb)
	{
		BitBoard newbb = new BitBoard();
		newbb.board0 = bb.board0|this.board0;
		newbb.board1 = bb.board1|this.board1;
		newbb.board2 = bb.board2|this.board2;
		newbb.board3 = bb.board3|this.board3;
		newbb.board4 = bb.board4|this.board4;
		newbb.board5 = bb.board5|this.board5;	
		return newbb;	
	}
	

	
	public long getSection (int index)
	{
		long boardSection;
		
		switch (index)
		{
		case 0:
			boardSection = board0;
			break;
		case 1:
			boardSection = board1;
			break;
		case 2:
			boardSection = board2;
			break;
		case 3:
			boardSection = board3;
			break;
		case 4:
			boardSection = board4;
			break;
		default:
			boardSection = board5;
		}
		
		return boardSection;
	}
	
	public void setSection (int index, long newSection)
	{
		switch (index)
		{
		case 0:
			board0 = newSection;
			break;
		case 1:
			board1 = newSection;
			break;
		case 2:
			board2 = newSection;
			break;
		case 3:
			board3 = newSection;
			break;
		case 4:
			board4 = newSection;
			break;
		default:
			board5 = newSection;
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		int boardsize = 19;
		StringBuilder sb = new StringBuilder();
		
		for (int row = boardsize; row >= 0; row--)
		{
			if (row+1>9)
			{
				sb.append((row+1) + " ");
			}
			else
			{
				sb.append(" " + (row+1) + " ");	
			}
			for (int col = 0; col < boardsize; col++)
			{
				sb.append(((get(row, col)) ? "@" : ".") + " ");
			}
			sb.append('\n');
		}
		sb.append("   A B C D E F G H J K L M N O P Q R S T");
		
		return sb.toString();
	}

	
	public boolean equals(BitBoard bb)
	{
		return (((this.board0^bb.board0) 
			   | (this.board1^bb.board1)
			   | (this.board2^bb.board2)
			   | (this.board3^bb.board3)
			   | (this.board4^bb.board4)
			   | (this.board5^bb.board5) ) == 0l);
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(BitBoard bb)
	{
		return (equals(bb) ? 0 : -1);
	}

//	/**
//	 * @return
//	 */
//	public boolean isZero()
//	{
//		return (board0&board1&board2&board3&board4&board5)==0l;
//	}
	
	public boolean overlaps (BitBoard bb)
	{
		return ( ( bb.board0&this.board0|
				   bb.board1&this.board1|
				   bb.board2&this.board2|
				   bb.board3&this.board3|
				   bb.board4&this.board4|
				   bb.board5&this.board5 ) != 0l );
	 }
	
	public boolean disjoint (BitBoard bb)
	{
		return ( ( bb.board0&this.board0|
				   bb.board1&this.board1|
				   bb.board2&this.board2|
				   bb.board3&this.board3|
				   bb.board4&this.board4|
				   bb.board5&this.board5 ) == 0l );
	 }

	/**
	 * @return
	 */
	public BitBoard not()
	{
		BitBoard newbb = new BitBoard();
		newbb.board0 = ~this.board0;
		newbb.board1 = ~this.board1;
		newbb.board2 = ~this.board2;
		newbb.board3 = ~this.board3;
		newbb.board4 = ~this.board4;
		newbb.board5 = ~this.board5&BitBoardConst.USED_BITS;
		return newbb;
	}
	
	public void removeBits(BitBoard bb)
	{
		this.board0 &=~bb.board0;
		this.board1 &=~bb.board1;
		this.board2 &=~bb.board2;
		this.board3 &=~bb.board3;
		this.board4 &=~bb.board4;
		this.board5 &=~bb.board5&BitBoardConst.USED_BITS;
	}
	
	public void addBits(BitBoard bb)
	{
		this.board0 |= bb.board0;
		this.board1 |= bb.board1;
		this.board2 |= bb.board2;
		this.board3 |= bb.board3;
		this.board4 |= bb.board4;
		this.board5 |= bb.board5;
	}

	public BitBoard xor(BitBoard bb)
	{
		BitBoard newbb = new BitBoard();
		newbb.board0 = bb.board0^this.board0;
		newbb.board1 = bb.board1^this.board1;
		newbb.board2 = bb.board2^this.board2;
		newbb.board3 = bb.board3^this.board3;
		newbb.board4 = bb.board4^this.board4;
		newbb.board5 = bb.board5^this.board5;
		return newbb;
	}

	public int count()
	{
		return Long.bitCount(this.board0)
			+ Long.bitCount(this.board1)
			+ Long.bitCount(this.board2)
			+ Long.bitCount(this.board3)
			+ Long.bitCount(this.board4)
			+ Long.bitCount(this.board5);
	}

	/**
	 * @param defender
	 * @return
	 */
	public BitBoard nor(BitBoard bb)
	{
		BitBoard newbb = new BitBoard();
		newbb.board0 =~(bb.board0|this.board0);
		newbb.board1 =~(bb.board1|this.board1);
		newbb.board2 =~(bb.board2|this.board2);
		newbb.board3 =~(bb.board3|this.board3);
		newbb.board4 =~(bb.board4|this.board4);
		newbb.board5 =~(bb.board5|this.board5)&BitBoardConst.USED_BITS;
		return newbb;	
	}
}
