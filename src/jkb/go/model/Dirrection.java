package jkb.go.model;
public enum Dirrection 
{
		UP(0, 1, 0), RIGTH(1, 0, 1), DOWN(2, -1, 0), LEFT(3, 0, -1);
		
		private final int index;
		private final int rowOffset;
		private final int colOffset;
		
		private Dirrection(int index, int rowOffset, int colOffset)
		{
			this.index = index;
			this.rowOffset = rowOffset;
			this.colOffset = colOffset;
		}

		/**
		 * @return the index
		 */
		public int getIndex()
		{
			return index;
		}

		/**
		 * @return the rowOffset
		 */
		public int getRowOffset()
		{
			return rowOffset;
		}

		/**
		 * @return the colOffset
		 */
		public int getColOffset()
		{
			return colOffset;
		}
}