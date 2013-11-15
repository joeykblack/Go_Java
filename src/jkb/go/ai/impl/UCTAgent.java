/**
 * 
 */
package jkb.go.ai.impl;

import jkb.go.ai.GoAgent;
import jkb.go.helper.Converter;
import jkb.go.helper.uct.UCTUtil;
import jkb.go.helper.uct.UCTUtilV2;
import jkb.go.model.Type;
import jkb.go.model.boardstate.BoardState;
import jkb.go.model.boardstate.BoardStateBitMaps;
import jkb.go.model.uct.Node;

/**
 * @author joey
 *
 */
public class UCTAgent implements GoAgent
{
	private Type type;
	private UCTUtilV2 util;
	

	/**
	 * @param type
	 */
	public UCTAgent(Type type)
	{
		super();
		this.type = type;
		util = new UCTUtilV2(type);
	}

	/* (non-Javadoc)
	 * @see jkb.go.ai.GoAgent#play(jkb.go.model.boardstate.BoardState)
	 */
	@Override
	public String play(BoardState bs)
	{
		return (bs instanceof BoardStateBitMaps) ? playBoardStateBitMaps((BoardStateBitMaps)bs) : null;
	}

	/**
	 * @param bs
	 * @return
	 */
	private String playBoardStateBitMaps(BoardStateBitMaps bs)
	{
//		Node best = util.UCTSearch(bs, 2000);
		Node best = UCTUtil.UCTSearch(bs, type, 10000);
		
		return Converter.rowColToString(best.row, best.col);
	}

}
