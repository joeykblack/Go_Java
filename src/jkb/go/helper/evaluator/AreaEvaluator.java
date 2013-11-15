/**
 * 
 */
package jkb.go.helper.evaluator;

import jkb.go.helper.BitBoardComplexOpps;
import jkb.go.model.BitBoard;
import jkb.go.model.boardstate.BoardStateBitMaps;

/**
 * @author joey
 *
 */
public class AreaEvaluator
{

	/*
	 *  Expand black and white area negating the others new
	 *  positions until all positions are taken.
	 */
	public static int[] evaluateArea(BoardStateBitMaps bs)
	{
		BitBoard blackArea = BitBoardComplexOpps.fullExpandBlocking(bs.getBlack(), bs.getWhite());
		BitBoard whiteArea = BitBoardComplexOpps.fullExpandBlocking(bs.getWhite(), bs.getBlack());
		BitBoard neutral = blackArea.and(whiteArea);
		
		blackArea.removeBits(neutral);
		blackArea.removeBits(bs.getBlack());
		whiteArea.removeBits(neutral);
		whiteArea.removeBits(bs.getWhite());
		
		return new int[]{blackArea.count()+bs.getCapturedWhiteStones(), 
				whiteArea.count() + bs.getCapturedBlackStones(), 
				neutral.count()};
	}

}
