/**
 * 
 */
package jkb.go.model;

/**
 * @author joey
 *
 */
public enum Type 
{
	WHITE, BLACK;
	
	public Type inverse()
	{
		return (this==WHITE) ? BLACK : WHITE;
	}
}
