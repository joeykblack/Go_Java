/**
 * 
 */
package jkb.go.resources.test;


import static org.junit.Assert.*;
import jkb.go.resources.BitBoardConst;

import org.junit.Before;
import org.junit.Test;

/**
 * @author joey
 *
 */
public class BitBoardConstTest
{

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
	}

	@Test
	public void testFiles()
	{
		assertTrue(BitBoardConst.FILE_A.get(0,0));
		assertTrue(BitBoardConst.FILE_A.get(18,0));
		assertTrue(!BitBoardConst.FILE_A.get(0,1));

		assertTrue(BitBoardConst.FILE_T.get(0,18));
		assertTrue(BitBoardConst.FILE_T.get(18,18));
		assertTrue(!BitBoardConst.FILE_T.get(0,17));
	}
	
}
