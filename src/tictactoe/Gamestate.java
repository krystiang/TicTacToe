package tictactoe;

import java.util.ArrayList;
import java.util.List;

// Constructors

public class Gamestate {
	public char[] board;
	public char turn;
	public int dim = 3;

	public Gamestate() {
		this.board = "         ".toCharArray();
		this.turn = 'x';
	}

	public Gamestate(char[] board, char turn) {
		this.board = board;
		this.turn = turn;
	}

	public Gamestate(String str) {
		this.board = str.toCharArray();
		this.turn = 'x';
	}

	public Gamestate(String str, char turn) {
		this.board = str.toCharArray();
		this.turn = turn;
	}

	// ----------------------

	@Override
	public String toString() {
		return new String(board);
	}

	/**
	 * @param idx
	 * @return Gamestate nachdem ein Zug ausgeführt wurde. Ein Zug besteht aus
	 *         dem Index innerhalb des Feldes auf dem der Spieler der dran ist
	 *         sein Symbol hinterlässt.
	 */
	public Gamestate move(int idx) {
		char[] newBoard = board.clone();
		newBoard[idx] = turn;
		return new Gamestate(newBoard, turn == 'x' ? 'o' : 'x');
	}

	/**
	 * @return Eine Liste aller noch möglichen Züge.
	 */
	public List<Integer> possibleMoves() {
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < board.length; i++) {
			if (board[i] == ' ') {
				list.add(i);
			}
		}
		return list;
	}

	/**
	 * @return Den bestmöglichen übrigen Zug.
	 */
	public int bestMove() {
		GamestateNode tree = buildGamestateTree(this, new GamestateNode(this),
				2);
		maxAB(tree, Integer.MIN_VALUE, Integer.MAX_VALUE);
		int max = Integer.MIN_VALUE;
		int best = -1;
		for (GamestateNode child : tree.childs) {
			if (child.val > max) {
				max = child.val;
				best = child.move;
			}
		}
		return best;
	}

	/**
	 * @param state
	 * @return Bewertet wie gut der Gamestate für den KI Spieler ist.
	 */
	public int eval(Gamestate state) {


		if (state.win('x')) {
			return -100;
		}
		if (state.win('o')) {
			return 100;
		}
		if (state.possibleMoves().size() == 0) {
			return 0;
		}
		
		int winX = state.winCount('x');
		int winO = state.winCount('o');
		System.out.println(state.board);
		System.out.println(winO - winX);
		return winO - winX;
	}

	/**
	 * @param turn
	 * @return Die noch mögliche Anzahl an Gewinnkombinationen.
	 */
	public int winCount(char turn) {
		int count = 0;
		for (int i = 0; i < dim; i++) {
			if (possible_win_line(turn, i * dim, 1)) {
				count++;
			}
			if (possible_win_line(turn, i, dim)) {
				count++;
			}
		}

		if (possible_win_line(turn, dim - 1, dim - 1)) {
			count++;
		}
		if (possible_win_line(turn, 0, dim + 1)) {
			count++;
		}
		return count;
	}

	/**
	 * @param turn
	 * @param start
	 * @param step
	 * @return Ob die entsprechende Reihe noch zu einem Gewinn führen kann.
	 */
	public boolean possible_win_line(char turn, int start, int step) {
		int count = 0;
		for (int i = 0; i < 3; i++) {
			if (board[start + step * i] == turn
					|| board[start + step * i] == ' ') {
				count++;
			}
		}
		return (count == 3);
	}

	/**
	 * @param turn
	 * @param start
	 * @param step
	 * @return Ob die Reihe die Gewinnbedingungen für den Spieler turn erfüllt.
	 */
	public boolean win_line(char turn, int start, int step) {
		for (int i = 0; i < 3; i++) {
			if (board[start + step * i] != turn) {
				return false;
			}

		}
		return true;
	}

	/**
	 * @param turn
	 * @return Ob Spieler turn gewonnen hat.
	 */
	public boolean win(char turn) {
		for (int i = 0; i < dim; i++) {
			if (win_line(turn, i * dim, 1) || win_line(turn, i, dim)) {
				return true;
			}
		}

		if (win_line(turn, dim - 1, dim - 1) || win_line(turn, 0, dim + 1)) {
			return true;
		}
		return false;
	}

	/**
	 * @param state
	 * @param tree
	 * @param tiefe
	 * @return Baut bis zu einer bestimmten Tiefe einen Baum aus allen möglichen
	 *         Gamestates.
	 */
	private GamestateNode buildGamestateTree(Gamestate state,
			GamestateNode tree, int tiefe) {
		GamestateNode child;
		for (int i : state.possibleMoves()) {
			Gamestate newState = state.move(i);
			child = tree.addChild(newState);
			child.setMove(i);
			if (tiefe > 0 && !newState.gameEnd()) {
				buildGamestateTree(newState, child, tiefe - 1);
			}
		}
		return tree;
	}

	/**
	 * @param node
	 * @param a
	 * @param b
	 * @return maxAB Algorithmus zur Bewertung des bestmöglichen Zugs.
	 */
	public int maxAB(GamestateNode node, int a, int b) {
		if (node.childs.isEmpty()) {
			node.setVal(eval(node.gamestate));
			return eval(node.gamestate);
		}
		int best = Integer.MIN_VALUE;
		for (GamestateNode child : node.childs) {
			if (best > a) {
				a = best;
			}
			int val = minAB(child, a, b);
			if (val > best) {
				best = val;
				if (a >= b) {
					break;
				}
			}
		}
		node.setVal(best);
		return best;
	}

	/**
	 * @param node
	 * @param a
	 * @param b
	 * @return minAB Algorithmus zur Bewertung des bestmöglichen Zugs.
	 */
	public int minAB(GamestateNode node, int a, int b) {
		if (node.childs.isEmpty()) {
			node.setVal(eval(node.gamestate));
			return eval(node.gamestate);
		}
		int best = Integer.MAX_VALUE;
		for (GamestateNode child : node.childs) {
			if (best < b) {
				b = best;
			}
			int val = maxAB(child, a, b);
			if (val < best) {
				best = val;
				if (a >= b) {
					break;
				}
			}

		}
		node.setVal(best);
		return best;
	}

	public boolean gameEnd() {
		return win('x') || win('o') || possibleMoves().size() == 0;
	}
}
