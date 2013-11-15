// SimpleGoApplet11.java

// Sample applet source for showing how to use mori.go.Board class
// For JDK1.1 only!!!

package mori.go;

import java.awt.*;
import java.applet.*;

public class SimpleGoApplet11 extends Applet
{	
	Board board;

	public void init(){
		setLayout(new BorderLayout());				
		add("Center", board = new Board( 19)); // construct 19x19 board
		board.setClickable( true); // accept mouse click
		
		Panel buttons = new Panel();
		buttons.add( new Button("Pass"));
		buttons.add( new Button("Undo"));
		buttons.add( new Button("Redo"));

		add( "South", buttons);

		// putting handicap stones.
		board.putStone( new Position( 3,3), Board.black);
		board.putStone( new Position( 15,15), Board.black);
		board.putStone( new Position( 3,15), Board.black);
		board.putStone( new Position( 15,3), Board.black);
		board.setFirstMove( Board.white); // white makes the first move for handicap games.

		// making actual moves.
		board.makeMove( new Position( 13, 2)); // white
		board.makeMove( new Position( 16, 5)); // black
		
//		board.addActionListener( new java.awt.event.ActionListener(){
//			public void actionPerformed( java.awt.event.ActionEvent ae){
//				System.out.println("action performed " + ae.getActionCommand());
//			}
//		});
	}
	
	public boolean action( Event evt, Object arg){
		if ( arg.equals("Pass")){
			board.pass(); 
			return true;
		}else if ( arg.equals("Undo")){
			board.undoMove();
			board.repaint();
			return true;
		}else if ( arg.equals("Redo")){
			board.redoMove();
			board.repaint();
			return true;
		}else
			return false;
	}
	
}


