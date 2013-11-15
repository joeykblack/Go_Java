/**
 * 
 */
package jkb.go.gui;

import java.awt.BorderLayout;
import java.awt.Event;

import javax.swing.JFrame;
import javax.swing.JPanel;

import jkb.go.ai.GoAgent;
import jkb.go.ai.impl.UCTAgent;
import jkb.go.helper.Converter;
import jkb.go.model.Type;
import jkb.go.model.boardstate.BoardState;
import jkb.go.model.boardstate.BoardStateBitMaps;
import mori.go.Board;
import mori.go.Position;

/**
 * @author joey
 *
 */
public class GoFrame extends JFrame implements Runnable
{
	private static final long serialVersionUID = 1L;
	
	private Board board;
	private BoardState boardState;
	
	private GoAgent agent;
	
	
	/**
	 * 
	 */
	public GoFrame()
	{
		agent = new UCTAgent(Type.WHITE);
		boardState = new BoardStateBitMaps();
		
		JPanel main = new JPanel(new BorderLayout());
		main.setSize(500, 500);
		
		board = new Board(19);
		board.setClickable(true);
        
        main.add(board, BorderLayout.CENTER);
        
		this.setSize(500, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(main);                    // Here we add it to the frame
        this.setVisible(true);
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Component#action(java.awt.Event, java.lang.Object)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public boolean action(Event evt, Object what)
	{
		if (what instanceof Position)
		{
			Position p = (Position)what;
			boardState.play((19-p.y), Converter.colToFile(p.x), Type.BLACK);
			
			// Ai move
			board.setClickable(false);
			new Thread(this).start();
			
		}
		return super.action(evt, what);
	}


	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run()
	{
		String aimove = agent.play(boardState);
		int rank = Converter.getRank(aimove);
		char file = Converter.getFile(aimove);
		boardState.play(rank, file, Type.WHITE);
		Position aip = new Position(Converter.fileToCol(file), 18-Converter.rankToRow(rank));
		board.makeMove(aip);
		board.repaint();
		board.setClickable(true);
	}
	/**
	 * @return the board
	 */
	public Board getBoard()
	{
		return board;
	}

	/**
	 * @param board the board to set
	 */
	public void setBoard(Board board)
	{
		this.board = board;
	}
	

}
