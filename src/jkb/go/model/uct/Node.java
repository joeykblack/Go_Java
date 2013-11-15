/**
 * 
 */
package jkb.go.model.uct;

/**
 * @author joey
 *
 */
public class Node
{
	// v1
	public Node child;
	public Node sybling;
	public Node best;
	
	// v2
	public Node[] children;
	
	public int wins;
	public int visits;
	
	public int row;
	public int col;
	
	
	/**
	 * @param row
	 * @param col
	 */
	public Node(int row, int col)
	{
		super();
		this.row = row;
		this.col = col;
		wins = 0;
		visits = 0;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "("+row+","+col+") wins="+wins+" visits="+visits;
	}

}
