package org.sanjana.hw9;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.sanjana.hw5.StateSerializer;
import org.sanjana.hw2.StateChangerImpl;
import org.sanjana.hw9.Heuristic;
import org.sanjana.hw9.DateTimer;
import org.shared.chess.Color;
import org.shared.chess.GameResult;
import org.shared.chess.Move;
import org.shared.chess.State;
import org.shared.chess.StateChanger;

import com.google.common.collect.Lists;
import com.google.gwt.user.client.Window;

/**
 * http://en.wikipedia.org/wiki/Alpha-beta_pruning<br>
 * This algorithm performs both A* and alpha-beta pruning.<br>
 * The set of possible moves is maintained ordered by the current heuristic value of each move. We
 * first use depth=1, and update the heuristic value of each move, then use depth=2, and so on until
 * we get a timeout or reach maximum depth. <br>
 * If a state has {@link TurnBasedState#whoseTurn} null (which happens in backgammon when we should
 * roll the dice), then I treat all the possible moves with equal probabilities. <br>
 * 
 * @author yzibin@google.com (Yoav Zibin)
 */
public class AlphaBetaPruning{

	private StateChanger stateChanger=new StateChangerImpl();
	static class TimeoutException extends RuntimeException {

		private static final long serialVersionUID = 1L;
	}

	static class MoveScore implements Comparable<MoveScore> 
	{
		Move move;
		int score;

		@Override
		public int compareTo(MoveScore o) {
			return o.score - score;
		}
	}

	private Heuristic heuristic;

	public AlphaBetaPruning(Heuristic heuristic) {
		this.heuristic = heuristic;
	}

	public Move findBestMove(State state, int depth, DateTimer timer) 
	{
		boolean isWhite = state.getTurn().isWhite();
//		Window.alert(StateSerializer.serialize(state));
		// Do iterative deepening (A*), and slow get better heuristic values for the
		// states.
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
			for (int i = 0; i < depth; i++) 
			{
				for (MoveScore moveScore : scores) 
				{
					Move move = moveScore.move;
					State newState=tryMove(state, move);
					int score = findMoveScore(newState, i, Integer.MIN_VALUE,Integer.MAX_VALUE, timer);
					if (!isWhite) {
						// the scores are from the point of view of the white, so for black
						// we need to switch.
						score = -score;
					}
					moveScore.score = score;
				}
				Collections.sort(scores); // This will give better pruning on the next
				// iteration.
			}
		}
		catch (TimeoutException e) {

//			Window.alert("timeout");
			Collections.sort(scores);
			Random generator = new Random(); 
			int i = generator.nextInt(scores.size()/5) + 1;
			return scores.get(i).move;
		}

		Collections.sort(scores);
		return scores.get(0).move;
	}

	/**
	 * If we get a timeout, then the score is invalid.
	 */
	private int findMoveScore(State state, int depth, int alpha, int beta, DateTimer timer)
			throws TimeoutException {

		if (timer.didTimeout()) { 
			throw new TimeoutException(); 
		}

		GameResult gameresult = state.getGameResult();

		if (depth == 0 || gameresult != null) { 
			return heuristic.getStateValue(state); 
		}
		
		int scoreSum = 0;
		int count = 0;
		
		Iterable<Move> possibleMoves = heuristic.getOrderedMoves(state);
		Color color=state.getTurn();
		for (Move move : possibleMoves) 
		{
			count++;
			State newState=tryMove(state, move);
			int childScore = findMoveScore(newState, depth - 1, alpha,beta, timer);
			
			if (color == null) {
				scoreSum += childScore;
			}
			else if (color.isWhite()) {
				alpha = Math.max(alpha, childScore);
				if (beta <= alpha) {
					break;
				}
			}
			else {
				beta = Math.min(beta, childScore);
				if (beta <= alpha) {
					break;
				}
			}
		}
		return color == null ? scoreSum / count : color.isWhite() ? alpha : beta;
	}

	public State tryMove(State state,Move move){
		State newState=state.copy();
		stateChanger.makeMove(newState, move);
		return newState;
	}
	
	public void getMove(){
		int sample_score=-20000;
		MoveScore m=new MoveScore();
		
	}
}