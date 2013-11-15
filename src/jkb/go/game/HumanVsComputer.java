/**
 * 
 */
package jkb.go.game;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collection;

import jkb.go.ai.GoAgent;
import jkb.go.ai.impl.ThreadedRandGameApproxMonteCarloAgent;
import jkb.go.helper.Converter;
import jkb.go.model.Type;
import jkb.go.model.boardstate.BoardState;
import jkb.go.model.boardstate.BoardStateBitMaps;

/**
 * @author joey
 *
 */
public class HumanVsComputer
{

	private static Collection<String> exit = Arrays.asList("exit", "quit", "q");


	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		boolean play = true;
		String command;
		BoardState boardState = new BoardStateBitMaps();
		GoAgent agent = new ThreadedRandGameApproxMonteCarloAgent(Type.WHITE);

		try
		{
		
			exit:
			while (play)
			{
				// Print board
				System.out.println();
				System.out.println(boardState.toString());
				
				// Human move
				boolean valid = false;
				do
				{
					// get move
					System.out.print("Your move: ");
					command = br.readLine();
					if (testExit(command)) break exit;
					int rank = Converter.getRank(command);
					char file = Converter.getFile(command);
					
					// move if valid
					valid = boardState.play(rank, file, Type.BLACK);
					if (!valid)
					{
						System.out.println(command + " is not a valid move");
					}
					
				} while (!valid);

				// Print board
				System.out.println();
				System.out.println(boardState.toString());
				
				// is game over
				
				// AI move
				System.out.println();
				System.out.println("...Thinking...");
				long start = System.currentTimeMillis();
				String aimove = agent.play(boardState);
				long end = System.currentTimeMillis();
				System.out.println("Time: " + (end-start));
				int rank = Converter.getRank(aimove);
				char file = Converter.getFile(aimove);
				boardState.play(rank, file, Type.WHITE);
				
				// is game over
				
			}

			// Print board
			System.out.println();
			System.out.println(boardState.toString());
			// print results
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
		System.out.println();
		System.out.println("Exiting");
	} // end main

	/**
	 * @param command
	 * @return
	 */
	private static boolean testExit(String command)
	{
		return exit.contains(command);
	}

}
