package org.alexanderoskotsky.hw2;

import org.shared.chess.Color;
import org.shared.chess.Move;
import org.shared.chess.Piece;
import org.shared.chess.PieceKind;
import org.shared.chess.Position;
import org.shared.chess.State;

/**
 * The StateManipulator updates the state of the game for a move such as
 * castling flags, enpassant positions, promotions and piece movement. The move
 * should be validated before using this class or else an invalid state will be
 * produced
 * 
 * 
 */
public class StateManipulator {
	/**
	 * Execute a move by updating the board's state. This should only be called
	 * once the move has been validated
	 * 
	 * @param state
	 * @param move
	 */
	public void doMove(State state, Move move) {
		Piece toPiece = state.getPiece(move.getTo());
		Piece piece = state.getPiece(move.getFrom());
		Color color = piece.getColor();

		state.setTurn(color.getOpposite());

		// stalemate turn counter
		if (toPiece == null && !piece.getKind().equals(PieceKind.PAWN)) {
			state.setNumberOfMovesWithoutCaptureNorPawnMoved(state.getNumberOfMovesWithoutCaptureNorPawnMoved() + 1);
		} else {
			state.setNumberOfMovesWithoutCaptureNorPawnMoved(0);
		}

		// can't castle once king moves
		if (piece.getKind().equals(PieceKind.KING)) {
			state.setCanCastleKingSide(color, false);
			state.setCanCastleQueenSide(color, false);
		}

		int backRow = color.equals(Color.WHITE) ? 0 : 7;

		// king side castle
		if (piece.getKind().equals(PieceKind.KING) && move.getFrom().getRow() == backRow && move.getFrom().getCol() == 4
				&& move.getTo().getCol() == 6) {

			state.setPiece(backRow, 7, null);
			state.setPiece(backRow, 5, new Piece(color, PieceKind.ROOK));

		}

		// queen side castle
		if (piece.getKind().equals(PieceKind.KING) && move.getFrom().getRow() == backRow && move.getFrom().getCol() == 4
				&& move.getTo().getCol() == 2) {

			state.setPiece(backRow, 0, null);
			state.setPiece(backRow, 3, new Piece(color, PieceKind.ROOK));
		}

		if (piece.getKind().equals(PieceKind.PAWN) && state.getEnpassantPosition() != null) {
			int increment = color.equals(Color.WHITE) ? 1 : -1;
			Position enpassant = new Position(state.getEnpassantPosition().getRow() + increment, state.getEnpassantPosition().getCol());
			if (move.getTo().equals(enpassant)) {
				state.setPiece(state.getEnpassantPosition(), null);
			}
		}

		updateEnpassant(state, move);

		updateCastleFlags(state, move);

		// move the piece
		state.setPiece(move.getTo(), piece);
		state.setPiece(move.getFrom(), null);

		if (piece.getKind().equals(PieceKind.PAWN) && color.equals(Color.WHITE) && move.getTo().getRow() == 7) {
			state.setPiece(move.getTo(), new Piece(Color.WHITE, move.getPromoteToPiece()));
		}

		if (piece.getKind().equals(PieceKind.PAWN) && color.equals(Color.BLACK) && move.getTo().getRow() == 0) {
			state.setPiece(move.getTo(), new Piece(Color.BLACK, move.getPromoteToPiece()));
		}

	}

	private void updateEnpassant(State state, Move move) {
		Piece piece = state.getPiece(move.getFrom());
		if (piece.getKind().equals(PieceKind.PAWN)) {
			if (piece.getColor().equals(Color.WHITE)) {

				if (move.getFrom().getRow() == 1 && move.getTo().getRow() == 3) {
					state.setEnpassantPosition(new Position(3, move.getFrom().getCol()));
					return;
				}

			} else {
				if (move.getFrom().getRow() == 6 && move.getTo().getRow() == 4) {
					state.setEnpassantPosition(new Position(4, move.getFrom().getCol()));
					return;
				}
			}
		}

		state.setEnpassantPosition(null);
	}

	private void updateCastleFlags(State state, Move move) {
		// white can't castle on queen's side once queen's rook has moved
		if (move.getFrom().equals(new Position(0, 0)) || move.getTo().equals(new Position(0, 0))) {
			state.setCanCastleQueenSide(Color.WHITE, false);
		}

		// white can't castle on king's side once king's rook has moved
		else if (move.getFrom().equals(new Position(0, 7)) || move.getTo().equals(new Position(0, 7))) {
			state.setCanCastleKingSide(Color.WHITE, false);
		}

		// black can't castle on queen's side once rook is moved
		else if (move.getFrom().equals(new Position(7, 0)) || move.getTo().equals(new Position(7, 0))) {
			state.setCanCastleQueenSide(Color.BLACK, false);
		}

		// black can't castle on king's side once rook is moved
		else if (move.getFrom().equals(new Position(7, 7)) || move.getTo().equals(new Position(7, 7))) {
			state.setCanCastleKingSide(Color.BLACK, false);
		}
	}
}
