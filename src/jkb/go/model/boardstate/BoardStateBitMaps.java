/**
 * 
 */
package jkb.go.model.boardstate;

import jkb.go.helper.BitBoardGameAction;
import jkb.go.helper.Converter;
import jkb.go.helper.Validator;
import jkb.go.model.BitBoard;
import jkb.go.model.Type;

/**
 * @author joey
 *
 */
public class BoardStateBitMaps implements BoardState 
{
	private BitBoard black;
	private BitBoard white;
	private BitBoard lastDead;
	private int capturedBlackStones;
	private int capturedWhiteStones;
	
	
	public BoardStateBitMaps() 
	{
		black = new BitBoard();
		white = new BitBoard();
		lastDead = new BitBoard();
		capturedBlackStones = 0;
		capturedWhiteStones = 0;
	}

	/**
	 * @param originalbs
	 */
	public BoardStateBitMaps(BoardStateBitMaps originalbs)
	{
		black = new BitBoard(originalbs.getBlack());
		white = new BitBoard(originalbs.getWhite());
		lastDead = new BitBoard(originalbs.getLastDead());
		capturedBlackStones = originalbs.getCapturedBlackStones();
		capturedWhiteStones = originalbs.getCapturedWhiteStones();
	}

	/* (non-Javadoc)
	 * @see jkb.go.model.boardstate.BoardState#play(int, char, jkb.go.model.Type)
	 */
	@Override
	public boolean play(int rank, char file, Type type) 
	{
		int row = Converter.rankToRow(rank);
		int col = Converter.fileToCol(file);
		
		return playRowCol(row, col, type);
	}
	

	public boolean playRowCol(int row, int col, Type type) 
	{
		boolean isWhite = (type==Type.WHITE);

		// Get players boards
		BitBoard player = (isWhite) ? white : black;
		BitBoard defender = (isWhite) ? black : white;
		
		// Initial validation
		boolean valid = Validator.validate(row, col, player, defender, this.lastDead);
		
		if (valid)
		{
			// Place stone
			player.set(row, col, true);
			
			// Remove dead
			BitBoard defenderResult = BitBoardGameAction.removeDead(player, defender);
			
			// Test if suicide 
			valid = !Validator.isSuicide(player, defenderResult, row, col);
			
			if (valid)
			{
				// Store for ko
				lastDead = defenderResult.xor(defender);
				
				// Update dead
				if (isWhite) 
				{
					capturedBlackStones += lastDead.count();
					black = defenderResult;
				}
				else
				{
					capturedWhiteStones += lastDead.count();
					white = defenderResult;
				}
			}
			else
			{
				// Restore
				player.set(row, col, false);
			}
		}
		
		return valid;
	}
	
	
	
	/*
	 * Getters and Setters
	 */

	public BitBoard getBlack()
	{
		return black;
	}

	public void setBlack(BitBoard black)
	{
		this.black = black;
	}

	public BitBoard getWhite()
	{
		return white;
	}

	public void setWhite(BitBoard white)
	{
		this.white = white;
	}

	public BitBoard getLastDead()
	{
		return lastDead;
	}

	public void setLastDead(BitBoard lastDead)
	{
		this.lastDead = lastDead;
	}

	public int getCapturedBlackStones()
	{
		return capturedBlackStones;
	}

	public void setCapturedBlackStones(int capturedBlackStones)
	{
		this.capturedBlackStones = capturedBlackStones;
	}

	/**
	 * @return the capturedWhiteStones
	 */
	public int getCapturedWhiteStones()
	{
		return capturedWhiteStones;
	}

	/**
	 * @param capturedWhiteStones the capturedWhiteStones to set
	 */
	public void setCapturedWhiteStones(int capturedWhiteStones)
	{
		this.capturedWhiteStones = capturedWhiteStones;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		int boardsize = 19;
		
		for (int row = boardsize-1; row >= 0; row--)
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
				sb.append(cellToChar(row, col) + " ");
			}
			sb.append('\n');
		}
		sb.append("   A B C D E F G H J K L M N O P Q R S T");
		
		return sb.toString();
	}

	/**
	 * @param cell
	 * @return
	 */
	private String cellToChar(int row, int col)
	{
		String s;
		if (black.get(row, col))
		{
			s = "X";
		}
		else if (white.get(row, col))
		{
			s = "O";
		}
		else
		{
			s = ".";
		}
		return s;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		boolean equal = false;
		if (obj instanceof BoardStateBitMaps)
		{
			BoardStateBitMaps bs = (BoardStateBitMaps)obj;
			equal = black.equals(bs.getBlack()) 
				 && white.equals(bs.getWhite());			
		}
		
		return equal;
	}
}
