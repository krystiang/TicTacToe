package tictactoe;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class GamestateTest {

	@Test
	public void testNew() throws Exception {
		Gamestate position = new Gamestate();
		assertEquals("         ", position.toString());
		assertEquals('x', position.turn);
	}

	@Test
	public void testMove() throws Exception {
		Gamestate position = new Gamestate().move(1);
		assertEquals(" x       ", position.toString());
		assertEquals('o', position.turn);
	}

	@Test
	public void testPossibleMoves() throws Exception {
		Gamestate position = new Gamestate().move(1).move(3).move(4);
		List<Integer> list = new ArrayList<Integer>();
		list.add(0);
		list.add(2);
		list.add(5);
		list.add(6);
		list.add(7);
		list.add(8);
		assertEquals(list, position.possibleMoves());
	}

	@Test
	public void testWin() throws Exception {
		assertFalse(new Gamestate().win('x'));
		assertTrue(new Gamestate("xxx      ").win('x'));
		assertTrue(new Gamestate("   ooo   ").win('o'));
		assertTrue(new Gamestate("x  x  x  ").win('x'));
		assertTrue(new Gamestate("  x x x  ").win('x'));
		assertTrue(new Gamestate("x   x   x").win('x'));
	}


	@Test
	public void testBestMove() throws Exception {
		assertEquals(1, new Gamestate(
				  "o o"
				+ "xx "
				+ "   ", 'o').bestMove());
		assertEquals(1, new Gamestate(
				  "o o"
				+ "xx "
				+ "x  ", 'o').bestMove());
		assertEquals(1, new Gamestate(
		         "x x"
		       + "o  "
		       + "xo ", 'o').bestMove());
	}

	@Test
	public void testGameENd() throws Exception {
		assertFalse(new Gamestate().gameEnd());
		assertTrue(new Gamestate("xxx      ").gameEnd());
	}

}
