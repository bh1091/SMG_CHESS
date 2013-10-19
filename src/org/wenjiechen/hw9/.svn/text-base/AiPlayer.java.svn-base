package org.wenjiechen.hw9;

import org.shared.chess.Move;
import org.shared.chess.State;

public class AiPlayer {
	private int millisecondsPerMove = 1500;
	private int maxDepth = 4;

	public Move chooseMove(State state) {
		AlphaBetaPruning aiExplorer = new AlphaBetaPruning(new HeuristicImpl());
		return aiExplorer.findBestMove(state, maxDepth, new DateTimer(millisecondsPerMove));
	}

}