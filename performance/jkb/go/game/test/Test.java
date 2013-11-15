/**
 * 
 */
package jkb.go.game.test;

import jkb.go.ai.GoAgent;
import jkb.go.ai.impl.GoRandomAgent;
import jkb.go.model.Type;
import jkb.go.model.boardstate.BoardState;
import jkb.go.model.boardstate.BoardStateBitGroups;

/**
 * @author joey
 *
 */
public class Test
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		BoardState boardState = new BoardStateBitGroups();
		GoAgent black = new GoRandomAgent(Type.BLACK);
		GoAgent white = new GoRandomAgent(Type.WHITE);
		for (int i = 0; i < 200; i++)
		{
			black.play(boardState);
			white.play(boardState);
		}
		System.out.println(boardState);
	}

}
