package jkb.go.game.test;

import java.util.ArrayList;
import java.util.List;
/**
 * 
 */

import jkb.go.model.boardstate.BoardStateBitMaps;

/**
 * @author joey
 *
 */
public class Temp
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		
		List<BoardStateBitMaps> boards = new ArrayList<BoardStateBitMaps>();
		int c = 0;
		while(true)
		{
			System.out.println(c++);
			boards.add(new BoardStateBitMaps());
		}

	}

}
