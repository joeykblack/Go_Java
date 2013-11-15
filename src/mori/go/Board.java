//
// Board.java
//
// by Hiroki Mori (mori@sainet.or.jp)
// 
// Version 0.9	96/9/11
// Version 1.0a 96/9/14 redo and pass in free play not implemented.
// Version 1.0.1a 97/1/8 misc. changes
// Version 1.0.2a 97/1/11 delete NextMove(), draw message
// Version 1.0.3a 97/1/23 implements Pass() and toString()
// Version 1.0.31a 97/1/29 changes name: ClearBoard->Clear
// Version 1.0.4a 97/2/22 put this class in the package mori.go.
// Version 1.1b 97/3/16 added comments for javadoc and now opened to the public.
// Version 1.2b 97/4/15 naming convention for methods are now complied with JDK
//						 handling of offscreen graphics is improved
// Version 1.2.1b 97/4/17 misc. changes. a bug when redoing fixed. drawCapturedStones()
// Version 1.2.2b 97/4/20 bugs in handicapped game corrected. still bug in redo.
// Version 1.3b 97/5/16 grid size is now resizable. and many small changes
// Version 1.3.1b 97/5/19 method name changed: enableClick -> setClickable
// Version 1.3.5b 97/6/3 more efficient removeStones algorithm
// Version 1.4 97/7/21 draws coordinates
// Version 1.4.1 97/7/27 shorten message strings; now redo correctly
// Version 1.4.2 97/9/22 draws hoshi for various board sizes
// Version 1.5 97/11/23 setLanguage() method added and messages strings can be multi-ligual.
// Version 1.5.2 98/1/25 fixed bugs in ko and passing.
// Version 1.6 98/3/15 JDK1.1 event model can be chosen to handle mouse event.
// Version 1.7 98/3/16 posts an event when legal move is clicked on the board.


/* If you don't like this class being in a package,
   remove the following package declaration.
   Then this class will be in the default package.
   In that case, be careful for name collision!
*/
package mori.go; 

import java.awt.*;
import java.util.*;
//import java.awt.event.*; // JDK1.1 only

public class Board extends Canvas{

	///////////////////////////////
	//////    constants      //////
	///////////////////////////////

	static final int none = 101;
	static final int black = 1;
	static final int white = 2;
	static final int outside = 3;
	
	static final int max_move = 400;
	static final int pass = 100;


	///////////////////////////////
	////// member variables  //////
	///////////////////////////////
	
	// board states
	protected int boardSize;
	protected int next;
	protected int[][] stone;
	protected int blackDead, whiteDead, numMoves;
	protected Position ko;
	protected Position lastMove;
	protected Position[] moveSequence;

	protected boolean handicapped = false;
	protected int[][] initStone = null; // remembering initial positions for handicap games. needed for undoing.
	protected	int firstMove = black; // for handicapped games, set to white.
	protected boolean firstMoveMade = false;
	
	// others
	protected boolean clickable = false;
	protected int	fixedGridSize = 0; // if 0, grid size is determined based on the canvas size
	protected int	gridSize; // internal use only
	protected boolean drawDeadStones = true;
	protected boolean drawCoordinates = true;
	private	String	message;
	protected Graphics offscreenG; // offscreen graphics
	protected Image buffer;
	protected boolean undoing = false, redoing = false;

	// strings
	protected String illegalMessage = "Illegal";
	protected String koMessage = "Illegal(Ko)";
	protected String deadStoneString = "dead";


	///////////////////////////////
	/////////  methods   //////////
	///////////////////////////////
	
	/**
	 * Construct a board with the specified board size.
	 */
	public  Board( int board_size){ 
		boardSize = board_size;
		//resize( boardSize * gridSize + gridSize, boardSize * gridSize + gridSize + 30);

		stone = new int[ boardSize][ boardSize];
		initStone = new int[ boardSize][ boardSize];
		resetSequence();		
		clear();
		repaint();

		// The following method is for JDK1.1 event handling
		// If you enable this, you have to disable mouseDown method.
		/*
		addMouseListener( new MouseAdapter(){
			public void mousePressed( MouseEvent e){
				if ( !clickable)
					return;
					
    			int xp = ( e.getX() - gridSize/2)/gridSize;
    			int yp = ( e.getY() - gridSize/2)/gridSize;
       			
    			// if the click is inside the board.
    			if ( xp >= 0 && xp < boardSize && yp >= 0 && yp < boardSize){
 		   			Position pos = new Position( xp, yp);			
	
					// if there is a legal move clicked on the board,
					// send an event containing Position to the parent component.
					// The parent component can be notified it by implementing Component#action.
					if ( makeMove( pos)){
						dispatchEvent( new ActionEvent( this, AWTEvent.RESERVED_ID_MAX + 1, pos.toString()));
					}
					repaint();
    			}
			}
		});
		*/
		// end of JDK1.1 part
	}

	// This is also for JDK1.1. It is used to send events outside of the Board.
	/*
	transient java.awt.event.ActionListener actionListener;
	public synchronized void addActionListener( ActionListener l) {
		actionListener = AWTEventMulticaster.add(actionListener, l);
	}
	protected void processEvent( AWTEvent e){
		if ( e instanceof ActionEvent){
			if ( actionListener != null)
				actionListener.actionPerformed( (ActionEvent)e);
		}else
			super.processEvent( e);
	}
    public synchronized void removeActionListener(ActionListener l) {
	actionListener = AWTEventMulticaster.remove(actionListener, l);
    }
	*/
	
	/**
	 * mouseDown event.
	 * Call setClickable to accept mouse click on the board.<p>
	 * If you, the Board user, want to know when a legal move is made 
	 * on the board, implement action method in the parent component.<p>
	 * @see #setClickable
	 */
    public boolean mouseDown(Event e, int xp, int yp) {
		if ( !clickable)
			return false;
			
    	xp = ( xp - gridSize/2)/gridSize;
    	yp = ( yp - gridSize/2)/gridSize;
       	
    	// if the click is inside the board.
    	if ( xp >= 0 && xp < boardSize && yp >= 0 && yp < boardSize){
 		   	Position pos = new Position( xp, yp);
			
			// if there is a legal move clicked on the board,
			// send an event containing Position to the parent component.
			// The parent component can be notified it by implementing Component#action.
			if ( makeMove( pos))
				postEvent( new Event( this, Event.ACTION_EVENT, pos));

			repaint();
    	}  	     		
		return true;
    }
	

    /**
     * Converts to a human-readable form.
	 * This method is still under construction.
     * @return	the textual representation.
     */
	public String toString(){

		StringBuffer sb = new StringBuffer();
		int turn = black;

		sb.append( "SIZE[");
		sb.append( boardSize);
		sb.append( "]\n");
		for ( int i = 0; i < numMoves; i++){
			if ( turn == black){
				sb.append("B[");
				turn = white;
			}else{
				sb.append("W[");
				turn = black;
			}
			
			Position pos = moveSequence[ i];
			sb.append( pos.x);
			sb.append(", ");
			sb.append( pos.y);
			sb.append("]\n");
		}

		return sb.toString();
	}

	/**
	 * Undo the last move.
	 */
	public void undoMove(){
		if ( numMoves < 1)
			return;
		
		int moves = numMoves;
		
		clear();

		if ( handicapped) 
			for ( int x = 0; x < boardSize; x++)
				for ( int y = 0; y < boardSize; y++){
					stone[ x][ y] = initStone[ x][ y];
				}
		
		undoing = true;
		for ( int i = 0; i < moves-1; i++){
			makeMove( moveSequence[ i]);
		}
		undoing = false;
	}
	
	/**
	 * Redo the move that was undone.<p>
	 */
	public void redoMove(){
		redoing = true;
		makeMove( moveSequence[ numMoves]);
		redoing = false;
	}		
	
	/**
	 * Clear the sequence of moves.
	 * You don't have to call this usually.
	 */
	protected void resetSequence(){

		firstMoveMade = false;
		handicapped = false;

		// clear the move sequence
		Position pos = new Position( none, none);
		moveSequence = new Position[ max_move];
		for ( int i = 0; i < max_move; i++)
			moveSequence[ i] = pos;
	}

	/**
	 * Clear all moves on the board.
	 * However, move sequence is retained. That is, still redoable.
	 */
	public void clear(){
		next = firstMove;

		whiteDead = blackDead = 0;
		ko = new Position( none, none);
		lastMove = new Position( none, none);
		numMoves = 0;
		message = null;

		Position pos = new Position();
    	for ( int x = 0; x < boardSize; x++)
			for ( int y = 0; y < boardSize; y++){
				pos.x = x; pos.y = y;
				putStone( pos, none);
			}
	}

	/**
	 * Set the board to enable or disable to accept mouse click on it.
	 * By default, it is disabled.
	 */
	public void	setClickable( boolean b){
		clickable = b;
	}
	
	/**
	 * Set whether the number of captured stones below the board is drawn.
	 * By default, it is enabled.
	 */
	public void	setDrawDeadStones( boolean b){
		drawDeadStones = b;
	}

	/**
	 * Set whether the coordinates on the board is drawn.
	 * By default, it is enabled.
	 */
	public void	setDrawCoordinates( boolean b){
		drawCoordinates = b;
	}

	/**
	 * Put a stone on a board with the specified stone(B or W) and the position.
	 * Legality of the move is not checked.
	 * Mainly used to put handicap stones. Don't call this after you call makeMove().
	 * However, you can call this again after you call resetSequence()
	 * @see #makeMove
	 */
    public void putStone( Position pos, int stone2){
		if ( pos.x >= 0 && pos.x < boardSize && pos.y >= 0 && pos.y < boardSize)
    		stone[ pos.x][ pos.y] = stone2;
    }
    
    /**
	 * Internal use only.
	 */
	protected int checkAt( Position p){
    	if ( p.x < 0 || p.x >= boardSize || p.y < 0 || p.y >= boardSize)
    		return outside;
    	else
    		return stone[ p.x][ p.y];
    }
	
    /**
	 * Internal use only.
	 */
	protected int removeDeadStones( Position p, boolean checkonly){
   		Vector toCheck = new Vector();
    	Vector checked = new Vector();
		int c = 0;
    	
		int my, opt, tmp;
    	Position ps[] = new Position[ 4];
    	for ( int i = 0; i < 4; i++)
    		ps[ i] = new Position();
		   	
    	if ( checkAt( p) == black){
    		opt = white;
    		my = black;
    	}else{
    		opt = black;
    		my = white;
    	}

		toCheck.addElement( new Position( p));
    	
    	Position check;

    	while ( toCheck.size() > 0 ){
			check = (Position)toCheck.firstElement();

	 		ps[0].x = check.x;
	 		ps[0].y = check.y + 1;
			ps[1].x = check.x; 
			ps[1].y = check.y - 1;
			ps[2].x = check.x + 1; 
			ps[2].y = check.y;
			ps[3].x = check.x - 1; 
			ps[3].y = check.y;
			
			boolean found = false;
			for ( int i = 0; i < 4; i++){
				tmp = checkAt( ps[i]);
				if ( tmp == my ){
					found = false;
					for ( int j = 0; j < checked.size(); j++){
						Position tmp2 = (Position)checked.elementAt( j);
						if ( tmp2.x == ps[i].x && tmp2.y == ps[i].y){
							found = true;
							break;
						}
					}
					if ( !found){
						for ( int j = 0; j < toCheck.size(); j++){
							Position tmp2 = (Position)toCheck.elementAt( j);
							if ( tmp2.x == ps[i].x && tmp2.y == ps[i].y){
								found = true;
								break;
							}
						}
						if ( !found){
							toCheck.addElement( new Position( ps[i]));
						}
					}
				}
				if ( tmp == none)
					return 0; // nothing dead !!
			}
			checked.addElement( new Position( check));
			toCheck.removeElement( check);
    	}	

		if ( checkonly){
			putStone( p, none);
			return 1;
		}
		
 	  	for ( int i = 0; i < checked.size(); i++){
			check = new Position( (Position)checked.elementAt( i));
			putStone( check, none);
			c++;
			if ( my == black)
				blackDead++;
			else
				whiteDead++;
		}
					
		return c;
	}

	/**
	 * Pass a move.
	 */
	public void	pass(){
		makeMove( new Position( pass, pass));
	}
	
	/**
	 * Make a move.
	 * This method takes care of everything including legality check and counting dead stones.<br>
	 * Position can be generated with  "new Position( x, y)" where 0 <= x,y < board_size-1
	 */
	public boolean makeMove( Position pos){
		int my, opt;
    	Position ps[] = new Position[4];
		message = null;

		if ( pos.x == none || pos.y == none){
			//System.out.println("not redoable");
			return false;
		}

		if ( !firstMoveMade){
			firstMoveMade = true;
			recordInitialStones();
		}

		if ( pos.x != pass && pos.y != pass)// if it is a pass when undoing
		{			    	    	
	    	for ( int i = 0; i < 4; i++)
	    		ps[ i] = new Position();
			int[] dead  = new int[4];

			if ( checkAt( pos) != none){ // if a stone is already there.
				message = illegalMessage;
				return false;
			}

			if ( pos.x == ko.x && pos.y == ko.y ){ // if ko
				message = koMessage;
				return false;
			}

			putStone( pos, next);
			
			// remove dead stones
			if ( next == black){
				my = black;
				opt = white;
			}else{
				my = white;
				opt = black;
			}

			ps[0].x = pos.x; ps[0].y = pos.y + 1;
			ps[1].x = pos.x; ps[1].y = pos.y - 1;
			ps[2].x = pos.x + 1; ps[2].y = pos.y;
			ps[3].x = pos.x - 1; ps[3].y = pos.y;
			
			for ( int i = 0; i < 4; i++){
				if ( checkAt( ps[i]) == opt){
					dead[ i] = removeDeadStones( ps[i], false);
				}else
					dead[ i] = 0;
			}
			    	
			if ( removeDeadStones( pos, true) != 0){ // no liberty?
				message = illegalMessage;
				return false;
			}

			// ko implementation	
			ko.x = ko.y = 1000;
			int dame = 0;
			boolean isKo = true;
			for ( int i = 0; i < 4; i++){
				if ( checkAt(ps[ i]) == my)
					isKo = false;
				if ( checkAt(ps[ i]) == none)
					dame++;
			}
			if ( dame >= 2)
				isKo = false;
			
			if ( isKo)
				for ( int i = 0; i < 4; i++)
					if ( dead[ i] == 1)
						ko = ps[ i];
			
		}else{ // pass
			ko.x = ko.y = 1000;
		}
		
		// if legal
		next = ( next == black) ? white : black;
		lastMove = new Position( pos);
		moveSequence[ numMoves] = new Position( pos);
		numMoves++;	
		message = null;

		if ( !undoing && !redoing) //  quick & dirty :)
			moveSequence[ numMoves] = new Position( none, none); 
			
		return true;
	}

	/**
	 * Internal use only - used to record stones put before the first move is made.
	 * Mainly used for handicap games.
	 */
	private void recordInitialStones(){
		// check if it is a handicap game. if so, copy to initStone
    	for ( int x = 0; x < boardSize; x++)
			for ( int y = 0; y < boardSize; y++){
				int s = checkAt( new Position( x, y));
				if ( s != none){
					handicapped = true;
					next = firstMove;
				}
				initStone[ x][ y] = s;
			}
	}

	/**
	 * Draws everything
	 */
	public void paint( Graphics g ) {
		Position pos = new Position();
		
		if ( buffer == null)
			buffer = createImage( size().width, size().height);

		if ( offscreenG == null)
			offscreenG = buffer.getGraphics();

		// determine the grid size
		if ( fixedGridSize != 0)
			gridSize = fixedGridSize;
		else{
			int xgrid = (size().width - 20) / ( boardSize + 1);
			int ygrid = (size().height - 50) / ( boardSize + 1);
			gridSize = ( xgrid > ygrid )? ygrid : xgrid;
		}

		//// begin off-screen drawing ////
		offscreenG.setColor( Color.orange);
		offscreenG.fillRect( 0, 0, size().width, size().height);
		offscreenG.setColor( Color.black);
				
		drawLines( offscreenG);

		if ( drawCoordinates)
			drawCoordinates( offscreenG);

		for ( pos.x = 0; pos.x < boardSize; pos.x++)
			for ( pos.y = 0; pos.y < boardSize; pos.y++)
				drawStone( offscreenG, pos);
		
		drawLastStoneMark( offscreenG);

		if ( drawDeadStones)
			drawDeadStones( offscreenG);
		
		if ( message != null)
			drawMessage( offscreenG, Color.black);

		//// end off-screen drawing ////		
		//offscreenG.dispose();
		g.drawImage( buffer, 0, 0, null);
	}
	
	/**
	 * 
	 */
	public void update( Graphics g){
		paint (g);
	}
	
	/**
	 * Internal use only.
	 */
	protected void drawStone( Graphics g, Position pos){
		if ( checkAt( pos) == black )
			drawBlackStone( g, pos);
		else if ( checkAt( pos) == white)
			drawWhiteStone( g, pos);
	}
	
	/**
	 * Internal use only.
	 */
	protected void drawBlackStone( Graphics g, Position pos){
		int x, y;
		x = pos.x; y = pos.y;

		g.setColor( Color.black);
		g.fillOval( (x+1) * gridSize -gridSize*9/20, (y+1) * gridSize -gridSize*9/20, gridSize*9/10, gridSize*9/10);
		g.setColor( Color.gray);
		g.fillOval( (x+1) * gridSize -gridSize*5/20, (y+1) * gridSize -gridSize*5/20, gridSize/10, gridSize/10);
		
	}
	
	/**
	 * Internal use only.
	 */
	protected void drawWhiteStone( Graphics g, Position pos){
		int x, y;
		x = pos.x; y = pos.y;

		g.setColor( Color.white);
		g.fillOval( (x+1) * gridSize -gridSize*9/20, (y+1) * gridSize -gridSize*9/20, gridSize*9/10, gridSize*9/10);
		g.setColor( Color.gray);
		g.drawOval( (x+1) * gridSize -gridSize*9/20, (y+1) * gridSize -gridSize*9/20, gridSize*9/10, gridSize*9/10);
	}

	/**
	 * Internal use only.
	 */
	protected void drawLastStoneMark( Graphics g){
		int x, y;
		x = lastMove.x; y = lastMove.y;
		
		if ( checkAt( lastMove) == black ){
			g.setColor( Color.white);
			g.fillOval( x * gridSize + gridSize * 7/8, y * gridSize + gridSize * 7/8, gridSize/3, gridSize/3);
		}else if ( checkAt( lastMove) == white){
			g.setColor( Color.black);
			g.fillOval( x * gridSize + gridSize * 7/8, y * gridSize + gridSize * 7/8, gridSize/3, gridSize/3);
		}
	}

	/**
	 * Internal use only
	 */
	protected void drawLines( Graphics g){
		// draw lines
		for ( int x = 0; x < boardSize; x++)
			g.drawLine( x * gridSize + gridSize, gridSize, 
				x * gridSize + gridSize, gridSize * boardSize);
			
		for ( int y = 0; y < boardSize; y++)
			g.drawLine( gridSize, y * gridSize + gridSize,
				gridSize * boardSize, y * gridSize + gridSize);

		// draw hoshi(stars)
		g.setColor( Color.black);
		if ( boardSize > 9 && boardSize % 2 == 1){
			for ( int x = 3; x < boardSize-1; x+= (boardSize-7)/2)
				for ( int y = 3; y < boardSize-1; y+=(boardSize-7)/2)
					g.fillOval( x * gridSize + gridSize-2, y * gridSize + gridSize-2, 5, 5);
		}else if ( boardSize == 9){
			for ( int x = 2; x < boardSize; x+=4)
				for ( int y = 2; y < boardSize; y+=4)
					g.fillOval( x * gridSize + gridSize-2, y * gridSize + gridSize-2, 5, 5);
		}
	}

	/**
	 * Internal use only
	 */
	protected void drawCoordinates( Graphics g){
		g.setColor( Color.black);
		g.setFont( new Font( "Helvetica", Font.PLAIN, 9));
		char c = 'A';
		for ( int i = 0; i < boardSize; i++){
			if ( c == 'I')
				c++; // skip 'I'
			Character ch = new Character( c++);
			g.drawString( ch.toString(), (i+1) * gridSize - 3, (boardSize + 1) * gridSize + 5);
		}

		for ( int i = 1; i <= boardSize; i++){
			Integer in = new Integer( i);
			g.drawString( in.toString(), (boardSize + 1) * gridSize - 3, (boardSize - i + 1) * gridSize + 3);
		}
	}

	/**
	 * Internal use only
	 */
	protected void drawDeadStones( Graphics g){
		g.setColor( Color.black);
		g.fillOval( size().width-80, (boardSize + 1) * gridSize + 13, 15, 15);
		g.setFont( new Font( "Helvetica", Font.PLAIN, 11));
		g.drawString( deadStoneString, size().width - 125, (boardSize + 1) * gridSize + 25);
		g.drawString(  (new Integer(blackDead)).toString(), size().width-60, (boardSize + 1) * gridSize + 25); 
		g.drawString(  (new Integer(whiteDead)).toString(), size().width-20, (boardSize + 1) * gridSize + 25); 
		g.setColor( Color.white);
		g.fillOval( size().width-40, (boardSize + 1) * gridSize + 13, 15, 15);
		//g.drawString(  (new Integer(numMoves)).toString(), 131, boardSize * 20 + 35); 
	}

	/**
	 * Internal use only
	 */
	protected void drawMessage( Graphics g, Color col){
		g.setColor( col);
		g.setFont( new Font( "Helvetica", Font.BOLD, 12));
		g.drawString( message, 10, (boardSize + 1) * gridSize + 40);
	}

	/**
	 * Set the side which makes the first move. Default is black. Can be used for handicapped games
	 */
	public void setFirstMove( int stone){
		if ( stone == white)
			firstMove = stone;
	}

	/**
	 * Use this if you want to set the grid size. Otherwise, it is automatically determined based on the canvas size.
	 */
	public void setGridSize( int size){
		fixedGridSize = size;
	}

	/**
	 * Changes message strings based on the language specified.
	 * Currently, you can choose English(default) or Japanese.<p>
	 * Yes, I know this is a bad way of implementation. We should use resources in these cases.
	 * But I can't resist this quick and dirty way...
	 */
	public void setLanguage( String lang){
		if ( lang.equals("Japanese")){
			illegalMessage = "着手禁止";
			koMessage = "着手禁止（コウ）";
			deadStoneString = "アゲハマ";
		}
	}

	/**
	 * Draws the specified message below the board.
	 */
	public void setMessage( String message){
		this.message = message;
	}

	/**
	 * Get the current message to be drawn
	 */
	public String getMessage(){
		return message;
	}

}

