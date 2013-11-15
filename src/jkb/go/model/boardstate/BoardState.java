/**
 * 
 */
package jkb.go.model.boardstate;

import jkb.go.model.Type;


/**
 * @author joey
 *
 */
public interface BoardState
{
	public boolean play(int rank, char file, Type type);
}
