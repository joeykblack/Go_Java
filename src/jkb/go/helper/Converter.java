/**
 * 
 */
package jkb.go.helper;

/**
 * @author joey
 *
 */
public class Converter
{
	
	public static String rowColToString(int row, int col)
	{
		char file = (char) ('A'+col);
		if (file>='I')
		{
			file = (char) (file+1);
		}
		int rank = row+1;
		
		return file+String.valueOf(rank);
	}
	
	public static int fileToCol(char file)
	{
		char f = Character.toUpperCase(file);
		int col = f-'A';
		if (f>'I')
		{
			col--;
		}
		return col;
	}
	
	public static int rankToRow(int rank)
	{
		return rank-1;
	}

	/**
	 * @param command
	 * @return
	 */
	public static int getRank(String command)
	{
		return Integer.parseInt(command.substring(1));
	}

	/**
	 * @param command
	 * @return
	 */
	public static char getFile(String command)
	{
		return command.charAt(0);
	}

	/**
	 * @param col
	 * @return
	 */
	public static char colToFile(int col)
	{
		char file = (char) ('A'+col);
		if (file>='I')
		{
			file = (char) (file+1);
		}
		return file;
	}

	/**
	 * @param row
	 * @return
	 */
	public static int rowToRank(int row)
	{
		return row+1;
	}
	
}
