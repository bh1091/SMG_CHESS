package org.yuanjia.hw9;

import java.util.Collections;
import java.util.List;

import org.shared.chess.Color;
import org.shared.chess.Move;
import org.shared.chess.State;
import org.shared.chess.StateChanger;
import org.yuanjia.hw2.StateChangerImpl;

import com.google.common.collect.Lists;

public class AlphaBetaPruning {
	private StateChanger stateChanger = new StateChangerImpl();

	static class TimeoutException extends RuntimeException {

		private static final long serialVersionUID = 1L;
	}

	static class MoveScore implements Comparable<MoveScore> {
		Move move;
		int score;

		@Override
		public int compareTo(MoveScore o) {
			return o.score - score; // sort DESC (best score first)
		}
	}

	private Heuristic heuristic;

	public AlphaBetaPruning(Heuristic heuristic) {
		this.heuristic = heuristic;
	}

	public Move findBestMove(State state, int depth, Timer timer) {
		boolean isWhite = state.getTurn().isWhite();

		// Do iterative deepening (A*), and slow get better heuristic values for
		// the states.
		List<MoveScore> scores = Lists.newArrayList();
		{
			Iterable<Move> possibleMoves = heuristic.getOrderedMoves(state);
			for (Move move : possibleMoves) {
				MoveScore score = new MoveScore();
				score.move = move;
				score.score = Integer.MIN_VALUE;
				scores.add(score);
			}
		}

		try {
			for (int i = 0; i < depth; i++) {
				for (MoveScore moveScore : scores) {
					Move move = moveScore.move;
					int score = 0;
					State state_copy = state.copy();
					stateChanger.makeMove(state_copy, move);
					score = findMoveScore(state_copy, i, Integer.MIN_VALUE,
							Integer.MAX_VALUE, timer);

					if (!isWhite) {
						// the scores are from the point of view of the white,
						// so for black we need to switch.
						score = -score;
					}
					moveScore.score = score;
				}
				Collections.sort(scores); // This will give better pruning on
											// the next iteration.
			}
		} catch (TimeoutException e) {
			// OK, it should happen
		}

		Collections.sort(scores);
		return scores.get(0).move;
	}

	/**
	 * If we get a timeout, then the score is invalid.
	 */
	private int findMoveScore(State state, int depth, int alpha, int beta,
			Timer timer) throws TimeoutException {

		if (timer.didTimeout()) {
			throw new TimeoutException();
		}
		// GameOver over = state.getGameOver();
		if (depth == 0 || state.getGameResult() != null) {
			return heuristic.getStateValue(state);
		}
		Color color = state.getTurn();
		int scoreSum = 0;
		int count = 0;
		Iterable<Move> possibleMoves = null;

		possibleMoves = heuristic.getOrderedMoves(state);

		for (Move move : possibleMoves) {
			count++;
			State state_copy = state.copy();
			stateChanger.makeMove(state_copy, move);
			int childScore = findMoveScore(state_copy, depth - 1, alpha, beta,
					timer);
			if (color == null) {
				scoreSum += childScore;
			} else if (color.isWhite()) {
				alpha = Math.max(alpha, childScore);
				if (beta <= alpha) {
					break;
				}
			} else {
				beta = Math.min(beta, childScore);
				if (beta <= alpha) {
					break;
				}
			}
		}
		return color == null ? scoreSum / count : color.isWhite() ? alpha
				: beta;
	}

}
