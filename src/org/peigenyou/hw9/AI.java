package org.peigenyou.hw9;

import org.shared.chess.Move;
import org.shared.chess.State;

public class AI {
	private int timeOutMilliSec=2000,maxDepth=5;
	public Move getMove(State state) {
				AlphaBetaPruning a = new AlphaBetaPruning(new HeuristicImpl());
				return a.findBestMove(state, maxDepth, new DateTimer(timeOutMilliSec));
	}
	
}
