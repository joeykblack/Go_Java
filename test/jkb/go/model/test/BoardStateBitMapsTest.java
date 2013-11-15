/**
 * 
 */
package jkb.go.model.test;

import static org.junit.Assert.*;
import jkb.go.model.Type;
import jkb.go.model.boardstate.BoardState;
import jkb.go.model.boardstate.BoardStateBitMaps;

import org.junit.Before;
import org.junit.Test;

/**
 * @author joey
 *
 */
public class BoardStateBitMapsTest
{

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
	}

	/**
	 * Test method for {@link jkb.go.model.boardstate.BoardState#play(int, int, jkb.go.model.Type)}.
	 */
	@Test
	public void testPlay()
	{
		BoardState bs = new BoardStateBitMaps();
		BoardState base = new BoardStateBitMaps();
		assertTrue(bs.play(5, (char)('A'+5), Type.BLACK));
		assertTrue(bs.play(6, (char)('A'+5), Type.WHITE));
		assertTrue(bs.play(5, (char)('A'+4), Type.WHITE));
		assertTrue(bs.play(4, (char)('A'+5), Type.WHITE));
		assertTrue(bs.play(5, (char)('A'+6), Type.WHITE));
		assertTrue(base.play(6, (char)('A'+5), Type.WHITE));
		assertTrue(base.play(5, (char)('A'+4), Type.WHITE));
		assertTrue(base.play(4, (char)('A'+5), Type.WHITE));
		assertTrue(base.play(5, (char)('A'+6), Type.WHITE));
		assertTrue(bs.equals(base));
	}

	@Test
	public void testPlaySide()
	{
		BoardState bs = new BoardStateBitMaps();
		BoardState base = new BoardStateBitMaps();
		assertTrue(bs.play(1, 'B', Type.BLACK));
		assertTrue(bs.play(1, 'A', Type.WHITE));
		assertTrue(bs.play(2, 'B', Type.WHITE));
		assertTrue(bs.play(1, 'C', Type.WHITE));
		assertTrue(base.play(1, 'A', Type.WHITE));
		assertTrue(base.play(2, 'B', Type.WHITE));
		assertTrue(base.play(1, 'C', Type.WHITE));
		assertTrue(bs.equals(base));
	}

	@Test
	public void testPlayTop()
	{
		BoardState bs = new BoardStateBitMaps();
		BoardState base = new BoardStateBitMaps();
		assertTrue(bs.play(19, 'A', Type.BLACK));
		assertTrue(bs.play(18, 'A', Type.WHITE));
		assertTrue(bs.play(19, 'B', Type.WHITE));
		assertTrue(base.play(18, 'A', Type.WHITE));
		assertTrue(base.play(19, 'B', Type.WHITE));
		assertTrue(bs.equals(base));
	}

	@Test
	public void testPlayTopGroup()
	{
		BoardState bs = new BoardStateBitMaps();
		BoardState base = new BoardStateBitMaps();
		assertTrue(bs.play(19, 'A', Type.BLACK));
		assertTrue(bs.play(19, 'B', Type.BLACK));
		assertTrue(bs.play(19, 'C', Type.BLACK));
		assertTrue(bs.play(18, 'A', Type.WHITE));
		assertTrue(bs.play(18, 'B', Type.WHITE));
		assertTrue(bs.play(18, 'C', Type.WHITE));
		assertTrue(bs.play(19, 'D', Type.WHITE));
		assertTrue(base.play(18, 'A', Type.WHITE));
		assertTrue(base.play(18, 'B', Type.WHITE));
		assertTrue(base.play(18, 'C', Type.WHITE));
		assertTrue(base.play(19, 'D', Type.WHITE));
		assertTrue(bs.equals(base));
	}

	@Test
	public void testPlayCorner()
	{
		BoardState bs = new BoardStateBitMaps();
		BoardState base = new BoardStateBitMaps();
		assertTrue(bs.play(1, 'A', Type.BLACK));
		assertTrue(bs.play(2, 'A', Type.WHITE));
		assertTrue(bs.play(1, 'B', Type.WHITE));
		assertTrue(base.play(2, 'A', Type.WHITE));
		assertTrue(base.play(1, 'B', Type.WHITE));
		assertTrue(bs.equals(base));
	}
	
	@Test
	public void testPlaySuicide()
	{
		BoardState bs = new BoardStateBitMaps();
		assertTrue(bs.play(2, 'A', Type.WHITE));
		assertTrue(bs.play(1, 'B', Type.WHITE));
		assertFalse(bs.play(1, 'A', Type.BLACK));
		assertTrue(bs.play(1, 'C', Type.BLACK));
	}
	
	@Test
	public void testPlaySuicideGroup()
	{
		BoardState bs = new BoardStateBitMaps();
		assertTrue(bs.play(2, 'A', Type.WHITE));
		assertTrue(bs.play(2, 'B', Type.WHITE));
		assertTrue(bs.play(1, 'C', Type.WHITE));
		assertTrue(bs.play(1, 'D', Type.BLACK));
		assertTrue(bs.play(1, 'B', Type.BLACK));
		assertFalse(bs.play(1, 'A', Type.BLACK));
	}
	
	@Test
	public void testPlayNotSuicide()
	{
		BoardState bs = new BoardStateBitMaps();
		assertTrue(bs.play(2, 'A', Type.WHITE));
		assertTrue(bs.play(2, 'B', Type.WHITE));
		assertTrue(bs.play(2, 'C', Type.WHITE));
		assertTrue(bs.play(1, 'B', Type.BLACK));
		assertTrue(bs.play(1, 'C', Type.BLACK));
		assertTrue(bs.play(1, 'A', Type.BLACK));
	}

	@Test
	public void testPlayTopSuicideGroup()
	{
		BoardState bs = new BoardStateBitMaps();
		assertTrue(bs.play(18, 'A', Type.WHITE));
		assertTrue(bs.play(18, 'B', Type.WHITE));
		assertTrue(bs.play(18, 'C', Type.WHITE));
		assertTrue(bs.play(19, 'D', Type.WHITE));
		assertTrue(bs.play(19, 'A', Type.BLACK));
		assertTrue(bs.play(19, 'B', Type.BLACK));
		assertFalse(bs.play(19, 'C', Type.BLACK));
	}

	@Test
	public void testPlayKo()
	{
		BoardState bs = new BoardStateBitMaps();
		assertTrue(bs.play(2, 'A', Type.WHITE));
		assertTrue(bs.play(1, 'B', Type.WHITE));
		
		assertTrue(bs.play(2, 'B', Type.BLACK));
		assertTrue(bs.play(1, 'C', Type.BLACK));
		assertTrue(bs.play(1, 'A', Type.BLACK)); // capture
		
		assertFalse(bs.play(1, 'B', Type.WHITE)); // ko
	}
	
	@Test
	public void testPlayOccuppied()
	{
		BoardState bs = new BoardStateBitMaps();
		assertTrue(bs.play(1, 'A', Type.BLACK));
		assertFalse(bs.play(1, 'A', Type.BLACK));
		assertFalse(bs.play(1, 'A', Type.WHITE));
	}
	
	@Test
	public void testCaptureThreeGroupsLeftSide()
	{
		BoardStateBitMaps bs = new BoardStateBitMaps();
		assertTrue(bs.play(15, 'A', Type.WHITE));
		assertTrue(bs.play(14, 'A', Type.WHITE));
		assertTrue(bs.play(13, 'B', Type.WHITE));
		assertTrue(bs.play(12, 'A', Type.WHITE));
		assertTrue(bs.play(11, 'A', Type.WHITE));
		
		assertTrue(bs.play(16, 'A', Type.BLACK));
		assertTrue(bs.play(15, 'B', Type.BLACK));
		assertTrue(bs.play(14, 'B', Type.BLACK));
		assertTrue(bs.play(13, 'C', Type.BLACK));
		assertTrue(bs.play(12, 'B', Type.BLACK));
		assertTrue(bs.play(11, 'B', Type.BLACK));
		assertTrue(bs.play(10, 'A', Type.BLACK));
		assertTrue(bs.play(13, 'A', Type.BLACK));

		assertTrue(bs.getWhite().count()==0);
	}
}
