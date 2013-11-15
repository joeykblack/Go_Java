/**
 * 
 */
package jkb.go.model.boardstate;

import jkb.go.helper.Converter;
import jkb.go.helper.Validator;
import jkb.go.model.BitBoard;
import jkb.go.model.Group;
import jkb.go.model.Type;


/**
 * @author joey
 *
 */
public class BoardStateBitGroups implements BoardState
{
	private final Group[] groups;
	private int groupIndexLimit; // max number of groups created so far
	
	private BitBoard blackOccupied;
	private BitBoard whiteOccupied;
	
	// Temporary variables (reset every play)
	
	private int newGroupIndex;
	private boolean doesNewGroupNeedSpace;
	
	private final Group[] mergeGroups;
	private int mergeGroupsLimit;
	
	private final Group[] deadGroups;
	private int deadGroupsLimit;

	private BitBoard lastCapturedGroup; // for ko test (reset after validation at begging of play)
	
	private int blackArea;
	private int whiteArea;
	
	/**
	 * Constructor
	 */
	public BoardStateBitGroups()
	{
		groups = new Group[181];
		groupIndexLimit = 0;
		mergeGroups = new Group[4];
		deadGroups = new Group[4];
		reset();
		
		lastCapturedGroup = null;
		blackOccupied = new BitBoard();
		whiteOccupied = new BitBoard();
	}
	


	/* (non-Javadoc)
	 * @see jkb.go.model.BoardState#play(int, char, jkb.go.model.Type)
	 */
	@Override
	public boolean play(int rank, char file, Type type)
	{
		int row = Converter.rankToRow(rank);
		int col = Converter.fileToCol(file);
		boolean isWhite = (type==Type.WHITE);
		Group newGroup = new Group(row, col, isWhite);
		
		boolean isValid = Validator.validate(this, newGroup, isWhite);
		
		// Place piece and update my groups
		if (isValid)
		{
			lastCapturedGroup = null; // clear after ko validation
			
			// Update occupied map
			updateOccupied(row, col, isWhite);
			
			// Merge new group and remove dead in array
			updateGroups(newGroup, isWhite);
			
			// If this is suicide
			if (Validator.isSuicide(this, newGroup, isWhite))
			{
				// Roll back invalid move
				isValid=false;
				restore(row, col, isWhite);
			}
			else
			{
				// Store last capture for ko test
				lastCapturedGroup = (deadGroupsLimit==1) ? deadGroups[0].getPositions() : null;
			}
			
			// Reset temp arrays for next turn
			reset();
		}
		
		return isValid;
	}



	/**
	 * @param newGroup
	 * @param isWhite
	 */
	private void updateGroups(Group newGroup, boolean isWhite)
	{
		// Get my occupied places for dead test
		BitBoard myUnOccupied = getOccupied(isWhite).not();
		BitBoard oppOccupied = getOccupied(!isWhite);
		
		// For each group
		for (int i = 0; i < groupIndexLimit; i++)
		{
			Group current = groups[i];
			
			// if found an empty slot and new group still needs a place
			if (current==null)
			{
				if (doesNewGroupNeedSpace)
				{
					putNewGroupAt(i, newGroup);
				}
			}
			else
			{
				// Merge if touching same type group
				if (current.isWhite()==isWhite && newGroup.getAdjacent().overlaps(current.getPositions()))
				{
					newGroup.setPositions(newGroup.getPositions().or(current.getPositions()));
					mergeGroups[mergeGroupsLimit] = current;
					mergeGroupsLimit++;
					vacateIndex(newGroup, i);
				}
				// Remove dead
				else if ( current.getAdjacent().disjoint(myUnOccupied) )
				{
					// Remove dead from array
					deadGroups[deadGroupsLimit] = current;
					deadGroupsLimit++;
					vacateIndex(newGroup, i);

					// Remove dead from map
					oppOccupied.removeBits(current.getPositions());
				}
			}
		}
		// Did not find an empty slot for new group, put at end.
		// Only happens if all slots are full. Not possible to have
		// more than 181 groups, so invalid index exception should
		// be impossible.
		if (doesNewGroupNeedSpace)
		{
			putNewGroupAt(groupIndexLimit, newGroup);
			groupIndexLimit++;
		}
		newGroup.recalcAdjacent();
	}

	/**
	 * Clear index or store newGroup if still looking for home
	 * 
	 * @param newGroup
	 * @param i
	 */
	private void vacateIndex(Group newGroup, int i)
	{
		if (doesNewGroupNeedSpace)
		{
			putNewGroupAt(i, newGroup);
		}
		else
		{
			groups[i] = null;
		}
	}

	/**
	 * Store newGroup
	 * 
	 * @param i
	 * @param newGroup
	 */
	private void putNewGroupAt(int i, Group newGroup)
	{
		newGroupIndex = i;
		groups[i] = newGroup;	
		doesNewGroupNeedSpace = false;
	}




	/**
	 * Restore if play turns out to be invalid after merger/dead removal
	 * 
	 * @param row
	 * @param col
	 * @param isWhite
	 */
	private void restore(int row, int col, boolean isWhite)
	{
		// Unset occupied
		(isWhite ? whiteOccupied : blackOccupied).set(row, col, false);
		
		// Remove new group
		groups[newGroupIndex] = null;
		
		// Put merged groups back
		for (int i = 0; i < mergeGroupsLimit; i++)
		{
			slowAddGroup(mergeGroups[i]);
		}
		
		// Put dead groups back
		BitBoard oppOccupied = getOccupied(!isWhite);
		for (int i = 0; i < deadGroupsLimit; i++)
		{
			// put dead groups in map
			oppOccupied.addBits(deadGroups[i].getPositions());
			
			// put groups back in array
			slowAddGroup(deadGroups[i]);
		}
	}

	/**
	 * Find an empty spot and stick group back in.
	 * Only used to roll back changes on suicide
	 * 
	 * @param group
	 */
	private void slowAddGroup(Group group)
	{
		for (int i = 0; i < groupIndexLimit; i++)
		{
			if ( groups[i] == null )
			{
				groups[i] = group;
				break;
			}
		}
	}



	/**
	 * Reset at end of play
	 */
	private void reset()
	{
		newGroupIndex = -1;
		doesNewGroupNeedSpace = true;
		
		mergeGroupsLimit = 0;
		mergeGroups[0] = null;
		mergeGroups[1] = null;
		mergeGroups[2] = null;
		mergeGroups[3] = null;
		
		deadGroupsLimit = 0;
		deadGroups[0] = null;
		deadGroups[1] = null;
		deadGroups[2] = null;
		deadGroups[3] = null;
	}



	
	/*
	 * Getters and Setters
	 */


	/**
	 * @param row
	 * @param col
	 * @param isWhite
	 */
	private void updateOccupied(int row, int col, boolean isWhite)
	{
		((isWhite) ? whiteOccupied : blackOccupied).set(row, col, true);
	}

	/**
	 * @param isWhite
	 * @return
	 */
	public BitBoard getOccupied(boolean isWhite)
	{
		return (isWhite) ? whiteOccupied : blackOccupied;
	}

	/**
	 * @return the lastCapturedGroup
	 */
	public BitBoard getLastCapturedGroup()
	{
		return lastCapturedGroup;
	}

	/**
	 * @param lastCapturedGroup the lastCapturedGroup to set
	 */
	public void setLastCapturedGroup(BitBoard lastCapturedGroup)
	{
		this.lastCapturedGroup = lastCapturedGroup;
	}

	/**
	 * @return the blackOccupied
	 */
	public BitBoard getBlackOccupied()
	{
		return blackOccupied;
	}

	/**
	 * @param blackOccupied the blackOccupied to set
	 */
	public void setBlackOccupied(BitBoard blackOccupied)
	{
		this.blackOccupied = blackOccupied;
	}

	/**
	 * @return the whiteOccupied
	 */
	public BitBoard getWhiteOccupied()
	{
		return whiteOccupied;
	}

	/**
	 * @param whiteOccupied the whiteOccupied to set
	 */
	public void setWhiteOccupied(BitBoard whiteOccupied)
	{
		this.whiteOccupied = whiteOccupied;
	}
	
	

	
	/**
	 * @return the groupIndexLimit
	 */
	public int getGroupIndexLimit()
	{
		return groupIndexLimit;
	}



	/**
	 * @param groupIndexLimit the groupIndexLimit to set
	 */
	public void setGroupIndexLimit(int groupIndexLimit)
	{
		this.groupIndexLimit = groupIndexLimit;
	}



	/**
	 * @return the newGroupIndex
	 */
	public int getNewGroupIndex()
	{
		return newGroupIndex;
	}



	/**
	 * @param newGroupIndex the newGroupIndex to set
	 */
	public void setNewGroupIndex(int newGroupIndex)
	{
		this.newGroupIndex = newGroupIndex;
	}



	/**
	 * @return the mergeGroupsLimit
	 */
	public int getMergeGroupsLimit()
	{
		return mergeGroupsLimit;
	}



	/**
	 * @param mergeGroupsLimit the mergeGroupsLimit to set
	 */
	public void setMergeGroupsLimit(int mergeGroupsLimit)
	{
		this.mergeGroupsLimit = mergeGroupsLimit;
	}



	/**
	 * @return the deadGroupsLimit
	 */
	public int getDeadGroupsLimit()
	{
		return deadGroupsLimit;
	}



	/**
	 * @param deadGroupsLimit the deadGroupsLimit to set
	 */
	public void setDeadGroupsLimit(int deadGroupsLimit)
	{
		this.deadGroupsLimit = deadGroupsLimit;
	}



	/**
	 * @return the groups
	 */
	public Group[] getGroups()
	{
		return groups;
	}



	/**
	 * @return the mergeGroups
	 */
	public Group[] getMergeGroups()
	{
		return mergeGroups;
	}



	/**
	 * @return the deadGroups
	 */
	public Group[] getDeadGroups()
	{
		return deadGroups;
	}


	
	
	




	
	
	/*
	 * 
	 * To string and equals
	 * 
	 */

	/**
	 * @return the doesNewGroupNeedSpace
	 */
	public boolean isDoesNewGroupNeedSpace()
	{
		return doesNewGroupNeedSpace;
	}



	/**
	 * @param doesNewGroupNeedSpace the doesNewGroupNeedSpace to set
	 */
	public void setDoesNewGroupNeedSpace(boolean doesNewGroupNeedSpace)
	{
		this.doesNewGroupNeedSpace = doesNewGroupNeedSpace;
	}



	/**
	 * @return the blackArea
	 */
	public int getBlackArea()
	{
		return blackArea;
	}



	/**
	 * @param blackArea the blackArea to set
	 */
	public void setBlackArea(int blackArea)
	{
		this.blackArea = blackArea;
	}



	/**
	 * @return the whiteArea
	 */
	public int getWhiteArea()
	{
		return whiteArea;
	}



	/**
	 * @param whiteArea the whiteArea to set
	 */
	public void setWhiteArea(int whiteArea)
	{
		this.whiteArea = whiteArea;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		int boardsize = 19;
		
		for (int row = boardsize; row >= 0; row--)
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
		if (blackOccupied.get(row, col))
		{
			s = "X";
		}
		else if (whiteOccupied.get(row, col))
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
		if (obj instanceof BoardStateBitGroups)
		{
			BoardStateBitGroups bs = (BoardStateBitGroups)obj;
			equal = blackOccupied.equals(bs.getBlackOccupied()) 
				 && whiteOccupied.equals(bs.getWhiteOccupied())
				 //&& lastCapturedGroup.equals(bs.getLastCapturedGroup()) 
				 && (groupIndexLimit==bs.getGroupIndexLimit());			
		}
		
		return equal;
	}
	
}
