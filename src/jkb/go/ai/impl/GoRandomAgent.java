/**
 * 
 */
package jkb.go.ai.impl;

import java.util.Random;

import jkb.go.ai.GoAgent;
import jkb.go.helper.Converter;
import jkb.go.model.Type;
import jkb.go.model.boardstate.BoardState;
import jkb.go.model.boardstate.BoardStateBitMaps;

/**
 * @author joey
 *
 */
public class GoRandomAgent implements GoAgent
{
	private Type type;
	
	/**
	 * 
	 */
	public GoRandomAgent(Type type)
	{
		this.type = type;
	}
	
	private  Random r = new Random(System.currentTimeMillis());


	/* (non-Javadoc)
	 * @see jkb.go.ai.GoAgent#play(jkb.go.model.BoardState)
	 */
	@Override
	public String play(BoardState bs)
	{
		return (bs instanceof BoardStateBitMaps) ? playBoardStateBitMaps((BoardStateBitMaps)bs) : null;
	}
	
	public String playBoardStateBitMaps(BoardStateBitMaps boardState)
	{
		BoardState tempbs = new BoardStateBitMaps(boardState);
		int row, col;
		boolean valid = false;
		int count = 0;
		do
		{
			row = r.nextInt(19);
			col = r.nextInt(19);
			valid = tempbs.play(Converter.rowToRank(row), Converter.colToFile(col), type);
			if (++count>100) break;
		} while (!valid);
		
		return Converter.rowColToString(row, col);
	}

}
