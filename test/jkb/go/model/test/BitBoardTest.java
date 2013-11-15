/**
 * 
 */
package jkb.go.model.test;

import static org.junit.Assert.*;
import jkb.go.model.BitBoard;

import org.junit.Before;
import org.junit.Test;

/**
 * @author joey
 *
 */
public class BitBoardTest
{
	private BitBoard bb;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		bb = new BitBoard();
	}
	
	@Test
	public void testShiftUp()
	{
		bb.set(5, true);
		assertTrue(bb.get(5));
		bb=bb.shiftForward(10);
		assertTrue(!bb.get(16));
		assertTrue(bb.get(15));
		assertTrue(!bb.get(14));
		assertTrue(!bb.get(5));
	}
	
	@Test
	public void testShiftDown()
	{
		bb.set(15, true);
		assertTrue(bb.get(15));
		bb=bb.shiftBack(10);
		assertTrue(!bb.get(15));
		assertTrue(!bb.get(6));
		assertTrue(bb.get(5));
		assertTrue(!bb.get(4));
	}

	@Test
	public void testAnd()
	{
		bb.set(15, true);
		BitBoard bb2 = new BitBoard();
		bb2.set(16, true);
		bb=bb.and(bb2);
		assertTrue(!bb.get(15));
		assertTrue(!bb.get(16));
	}

	@Test
	public void testAndNot()
	{
		bb.set(15, true);
		bb.set(16, true);
		BitBoard bb2 = new BitBoard();
		bb2.set(16, true);
		bb=bb.andNot(bb2);
		assertTrue(!bb.get(14));
		assertTrue(bb.get(15));
		assertTrue(!bb.get(16));
	}

	@Test
	public void testOr()
	{
		bb.set(15, true);
		bb.set(16, true);
		BitBoard bb2 = new BitBoard();
		bb2.set(16, true);
		bb2.set(17, true);
		bb=bb.or(bb2);
		assertTrue(!bb.get(14));
		assertTrue(bb.get(15));
		assertTrue(bb.get(16));
		assertTrue(bb.get(17));
		assertTrue(!bb.get(18));
	}

}
