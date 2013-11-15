/**
 * 
 */
package jkb.go.helper.evaluator;

import java.util.Random;

import jkb.go.factory.BitBoardFactory;
import jkb.go.helper.BitBoardGameAction;
import jkb.go.helper.Converter;
import jkb.go.model.BitBoard;
import jkb.go.model.Type;
import jkb.go.model.boardstate.BoardStateBitMaps;

/**
 * @author joey
 *
 */
public class MoveEvaluator
{

	
	/**
	 * @param bs
	 * @param move
	 * @return
	 */
	public static int bruteForceMCAveScore(BoardStateBitMaps originalbs, int testRow, int tesCol, Type myType, int numGames, long seed)
	{
		int moveScore = -1;
		BoardStateBitMaps bsWithMove = new BoardStateBitMaps(originalbs);
		if ( bsWithMove.play(Converter.rowToRank(testRow), Converter.colToFile(tesCol), myType) )
		{
			Random r = new Random(seed);
			int total = 0;
			int scoreIndex = (myType==Type.BLACK) ? 0 : 1;
			int oppScoreIndex = (myType==Type.BLACK) ? 1 : 0;
			for (int game = 0; game < numGames; game++)
			{
				BoardStateBitMaps temp = new BoardStateBitMaps(bsWithMove);
				Type type = myType.inverse();
				for (int i = 0; i < 361; i++)
				{
					int row = r.nextInt(19);
					int col = r.nextInt(19);
					while (!temp.play(Converter.rowToRank(row), Converter.colToFile(col), type))
					{
						row = r.nextInt(19);
						col = r.nextInt(19);
					}
					type = type.inverse();
					
					// board is mostly full?
					if ( (i % 10 == 0) && AreaEvaluator.evaluateArea((BoardStateBitMaps) temp)[2]<=10)
					{
						break;
					}
				}
				if (AreaEvaluator.evaluateArea((BoardStateBitMaps) temp)[scoreIndex]<AreaEvaluator.evaluateArea((BoardStateBitMaps) temp)[oppScoreIndex])
				{
					total++;
				}
			}
			moveScore = total;
		}
		return moveScore;
	}


	/**
	 * @param bs
	 * @param move
	 * @return average socore of games
	 */
	public static int randGameApproxAveValMCScore(BoardStateBitMaps originalbs, int testRow, int tesCol, Type myType, 
			int numGames, int numApproxRounds, int numApproxPieces, long seed)
	{
		int moveScore = -1;
		BoardStateBitMaps bs = new BoardStateBitMaps(originalbs);
		if (bs.play(Converter.rowToRank(testRow), Converter.colToFile(tesCol), myType))
		{
			int total = 0;
			Random r = new Random(seed);
			int scoreIndex = (myType==Type.BLACK) ? 0 : 1;
			
			BitBoard baseEmpty = bs.getBlack().nor(bs.getWhite());
			
			for (int game = 0; game < numGames; game++)
			{
				BoardStateBitMaps temp = new BoardStateBitMaps(bs);
				BitBoard empty = new BitBoard(baseEmpty);
				
				for (int i = 0; i < numApproxRounds; i++)
				{
					playApproxRound(myType, temp, empty, numApproxPieces, r);
				}
				total += AreaEvaluator.evaluateArea((BoardStateBitMaps) temp)[scoreIndex];
			}
			
			moveScore = total/numGames; 
		}
		return moveScore;
	}
	/**
	 * @param bs
	 * @param move
	 * @return average socore of games
	 */
	public static int randGameApproxMCMinLossScore(BoardStateBitMaps originalbs, int testRow, int testCol, Type myType, 
			int numGames, int numApproxRounds, int numApproxPieces, long seed)
	{
		int minScore = -1;
		BoardStateBitMaps bs = new BoardStateBitMaps(originalbs);
		if (bs.play(Converter.rowToRank(testRow), Converter.colToFile(testCol), myType))
		{
			minScore = 1000;
			Random r = new Random(seed);
			int scoreIndex = (myType==Type.BLACK) ? 0 : 1;

			BitBoard baseEmpty = bs.getBlack().nor(bs.getWhite());
			
			for (int game = 0; game < numGames; game++)
			{
				BoardStateBitMaps temp = new BoardStateBitMaps(bs);
				BitBoard empty = new BitBoard(baseEmpty);
				
				for (int i = 0; i < numApproxRounds; i++)
				{
					playApproxRound(myType, temp, empty, numApproxPieces, r);	
				}
				int score = AreaEvaluator.evaluateArea((BoardStateBitMaps) temp)[scoreIndex];
				if (minScore>score)
				{
					minScore = score;
				}
			}
		}
		
		return minScore;
	}
	
	
	/**
	 * @param bs
	 * @param move
	 * @return average socore of games
	 */
	public static int randGameApproxMCWinRateScore(BoardStateBitMaps originalbs, int testRow, int testCol, Type myType, 
			int numGames, int numApproxRounds, int numApproxPieces, long seed)
	{
		int total = -1;
		BoardStateBitMaps bs = new BoardStateBitMaps(originalbs);
		if (bs.playRowCol(testRow, testCol, myType))
		{
			total = 0;
			Random r = new Random(seed);
			int scoreIndex = (myType==Type.BLACK) ? 0 : 1;
			int oppScoreIndex = (myType.inverse()==Type.BLACK) ? 0 : 1;

			BitBoard baseEmpty = bs.getBlack().nor(bs.getWhite());
			
			for (int game = 0; game < numGames; game++)
			{
				BoardStateBitMaps temp = new BoardStateBitMaps(bs);
				BitBoard empty = new BitBoard(baseEmpty);
				
				for (int i = 0; i < numApproxRounds; i++)
				{
					playApproxRound(myType, temp, empty, numApproxPieces, r);	
				}
				int[] score = AreaEvaluator.evaluateArea((BoardStateBitMaps) temp);
				if (score[scoreIndex] - score[oppScoreIndex] > 0)
				{
					total++;
				}
			}
		}
		
		return total;
	}
	
	public static int randGameApprox(BoardStateBitMaps originalbs, int testRow, int testCol, Type myType, 
			int numApproxRounds, int numApproxPieces, long seed)
	{
		int win = 0;
		
		BoardStateBitMaps temp = new BoardStateBitMaps(originalbs);
		BitBoard empty = originalbs.getBlack().nor(originalbs.getWhite());
		Random r = new Random(seed);

		int scoreIndex = (myType==Type.BLACK) ? 0 : 1;
		int oppScoreIndex = (myType.inverse()==Type.BLACK) ? 0 : 1;
		
		for (int i = 0; i < numApproxRounds; i++)
		{
			playApproxRound(myType, temp, empty, numApproxPieces, r);	
		}
		int[] score = AreaEvaluator.evaluateArea(temp);
		if (score[scoreIndex] - score[oppScoreIndex] > 0)
		{
			win = 1;;
		}
		
		return win;
	}


	/**
	 * @param myType
	 * @param bs
	 * @param empty
	 * @param numApproxPieces
	 * @param rand
	 */
	private static void playApproxRound(Type myType, BoardStateBitMaps bs,
			BitBoard empty, int numApproxPieces, Random rand)
	{
		// Scatter opponent pieces
		scatterPieces(myType.inverse(), bs, empty, rand, numApproxPieces);

		// Remove my dead
		removeDead(myType, bs);
		
		// Scatter my pieces
		scatterPieces(myType, bs, empty, rand, numApproxPieces);
		
		// Remove opponent dead
		removeDead(myType.inverse(), bs);
	}


	/**
	 * @param type 
	 * @param bs
	 */
	private static void removeDead(Type type, BoardStateBitMaps bs)
	{
		if (type == Type.BLACK)
		{
			bs.setBlack( BitBoardGameAction.removeDead(bs.getWhite(), bs.getBlack()) );
		}
		else
		{
			bs.setWhite( BitBoardGameAction.removeDead(bs.getBlack(), bs.getWhite()) );
		}
	}


	/**
	 * @param bs
	 * @param empty
	 * @param rand
	 * @param numApproxPieces
	 * @return
	 */
	private static void scatterPieces(Type type, BoardStateBitMaps bs, BitBoard empty,
			Random rand, int numApproxPieces)
	{
		BitBoard randBoard = BitBoardFactory.createRandomBoard(numApproxPieces, rand).and(empty);
		empty.removeBits(randBoard);
		if (type==Type.BLACK)
		{
			bs.setBlack( bs.getBlack().or(randBoard) );
		}
		else
		{
			bs.setWhite( bs.getWhite().or(randBoard) );	
		}
	}

}
