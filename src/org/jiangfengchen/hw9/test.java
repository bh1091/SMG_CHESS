package org.jiangfengchen.hw9;


import org.jiangfengchen.hw2.StateChangerImpl;
import org.shared.chess.Move;
import org.shared.chess.State;

public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		StateChangerImpl SCI = new StateChangerImpl();
		AlphaBetaPruning ab = new AlphaBetaPruning(new Heuristic());
		State s = new State();
		
		while (s.getGameResult()==null){
			long t1=System.currentTimeMillis();
			Move m = ab.findBestMove(s, 3, new DateTimer(3000));
			long t2=System.currentTimeMillis();
			System.out.print(m+" "+s.getPiece(m.getFrom())+" "+s.getPiece(m.getTo())+" ");
			System.out.println(t2-t1);
			SCI.makeMove(s, m);
		}
	
		System.out.println(s.getGameResult().getGameResultReason());
	
	}

}
