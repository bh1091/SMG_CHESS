package org.bohuang.hw9;

import static org.shared.chess.Color.BLACK;
import static org.shared.chess.Color.WHITE;
import static org.shared.chess.PieceKind.KING;
import static org.shared.chess.PieceKind.PAWN;

import java.awt.Color;

import org.shared.chess.Move;
import org.shared.chess.Piece;
import org.shared.chess.PieceKind;
import org.shared.chess.State;

public class Test {
	
	
	public static void main(String[] args){
		State state = new State();
		/*for(int i = 0 ; i < 8 ; i++){
			state.setPiece(7, i, null);
			state.setPiece(6, i, null);
			state.setPiece(0, i, null);
			state.setPiece(1, i, null);
		}
		
		state.setPiece(7, 4, new Piece(BLACK, KING));
		state.setPiece(0, 4, null);
		state.setPiece(3, 4, new Piece(WHITE, KING));
		state.setPiece(6, 3, null);
		state.setPiece(4, 3, new Piece(BLACK, PAWN));
		state.setCanCastleKingSide(WHITE, false);
		state.setCanCastleQueenSide(WHITE, false);
		state.setCanCastleKingSide(BLACK, false);
		state.setCanCastleQueenSide(BLACK, false);*/
		//state.setTurn(BLACK);
		Heuristic heuristic = new Heuristic();
		AlphaBetaPruning pruning = new AlphaBetaPruning(heuristic);
		Timer timer = new Timer(5000);
		//System.out.print(state.getPiece(0, 0).getColor().toString());
		Move move = pruning.findBestMove(state, 5, timer);
		System.out.println(move.getFrom().getRow()+" " + move.getFrom().getCol() + " to " + move.getTo().getRow()+ " " + move.getTo().getCol());
	}
	
	
	
	

}
