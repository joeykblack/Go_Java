/**
 * 
 */
package jkb.go.model;

import jkb.go.helper.BitBoardComplexOpps;

/**
 * @author joey
 *
 */
public class Group implements Comparable<Group>
{
	
	private BitBoard positions;
	private BitBoard adjacent;
	private boolean isWhite;
	
	/**
	 * 
	 */
	public Group()
	{
		positions = new BitBoard();
		adjacent = new BitBoard();
	}

	/**
	 * @param type 
	 * @param positions
	 */
	public Group(int row, int col, boolean isWhite)
	{
		super();
		this.isWhite = isWhite;
		this.positions = new BitBoard();
		this.positions.set(row, col, true);
		
		// quicker way to get adjacent for single point
		this.adjacent = new BitBoard();
		if (row<18)
		{
			this.adjacent.set(row+1, col, true);	
		}
		if (row>0)
		{
			this.adjacent.set(row-1, col, true);	
		}
		if (col<18)
		{
			this.adjacent.set(row, col+1, true);	
		}
		if (col>0)
		{
			this.adjacent.set(row, col-1, true);	
		}
	}
	
	public Group (Group original)
	{
		positions = new BitBoard(original.getPositions());
		adjacent = new BitBoard(original.getAdjacent());
	}
	
	// this is costing about 0.08sec per 360^2 plays
	public void recalcAdjacent()
	{
		adjacent = BitBoardComplexOpps.explode(positions);
//		adjacent=positions.shiftLeftOne(). // 0.02
//			or(positions.shiftRightOne()). // 0.02
//			or(positions.shiftUp(1)). // 0.02
//			or(positions.shiftDown(1)). // 0.02
//			andNot(positions);
	}

	/**
	 * @return the position
	 */
	public BitBoard getPositions()
	{
		return positions;
	}

	/**
	 * @param position the group to set
	 */
	public void setPositions(BitBoard position)
	{
		this.positions = position;
	}

	/**
	 * @return the liberties
	 */
	public BitBoard getAdjacent()
	{
		return adjacent;
	}

	/**
	 * @param adjacent the liberties to set
	 */
	public void setAdjacent(BitBoard adjacent)
	{
		this.adjacent = adjacent;
	}
	
	

	

	/**
	 * @return the isWhite
	 */
	public boolean isWhite()
	{
		return isWhite;
	}

	/**
	 * @param isWhite the isWhite to set
	 */
	public void setWhite(boolean isWhite)
	{
		this.isWhite = isWhite;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Group g)
	{
		return adjacent.compareTo(g.getAdjacent()) + positions.compareTo(g.getPositions());
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		return (obj instanceof Group) && this.compareTo((Group)obj)==0;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return positions.toString();
	}

}
