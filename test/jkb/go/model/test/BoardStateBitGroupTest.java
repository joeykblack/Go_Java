/**
 * 
 */
package jkb.go.model.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import jkb.go.model.Type;
import jkb.go.model.boardstate.BoardState;
import jkb.go.model.boardstate.BoardStateBitGroups;

import org.junit.Before;
import org.junit.Test;

/**
 * @author joey
 *
 */
public class BoardStateBitGroupTest
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
		BoardState bs = new BoardStateBitGroups();
		BoardState base = new BoardStateBitGroups();
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
		BoardState bs = new BoardStateBitGroups();
		BoardState base = new BoardStateBitGroups();
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
		BoardState bs = new BoardStateBitGroups();
		BoardState base = new BoardStateBitGroups();
		assertTrue(bs.play(19, 'A', Type.BLACK));
		assertTrue(bs.play(18, 'A', Type.WHITE));
		assertTrue(bs.play(19, 'B', Type.WHITE));
		assertTrue(base.play(18, 'A', Type.WHITE));
		assertTrue(base.play(19, 'B', Type.WHITE));
		assertTrue(bs.equals(base));
	}

	@Test
	public void testPlayCorner()
	{
		BoardState bs = new BoardStateBitGroups();
		BoardState base = new BoardStateBitGroups();
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
		BoardState bs = new BoardStateBitGroups();
		assertTrue(bs.play(2, 'A', Type.WHITE));
		assertTrue(bs.play(1, 'B', Type.WHITE));
		assertFalse(bs.play(1, 'A', Type.BLACK));
		assertTrue(bs.play(1, 'C', Type.BLACK));
	}
	
	@Test
	public void testPlaySuicideGroup()
	{
		BoardState bs = new BoardStateBitGroups();
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
		BoardState bs = new BoardStateBitGroups();
		assertTrue(bs.play(2, 'A', Type.WHITE));
		assertTrue(bs.play(2, 'B', Type.WHITE));
		assertTrue(bs.play(2, 'C', Type.WHITE));
		assertTrue(bs.play(1, 'B', Type.BLACK));
		assertTrue(bs.play(1, 'C', Type.BLACK));
		assertTrue(bs.play(1, 'A', Type.BLACK));
	}

	@Test
	public void testPlayKo()
	{
		BoardState bs = new BoardStateBitGroups();
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
		BoardState bs = new BoardStateBitGroups();
		assertTrue(bs.play(1, 'A', Type.BLACK));
		assertFalse(bs.play(1, 'A', Type.BLACK));
		assertFalse(bs.play(1, 'A', Type.WHITE));
	}
}
