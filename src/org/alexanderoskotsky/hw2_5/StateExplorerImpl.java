package org.alexanderoskotsky.hw2_5;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.alexanderoskotsky.hw2.StateManipulator;
import org.shared.chess.Color;
import org.shared.chess.Move;
import org.shared.chess.Piece;
import org.shared.chess.PieceKind;
import org.shared.chess.Position;
import org.shared.chess.State;
import org.shared.chess.StateExplorer;

public class StateExplorerImpl implements StateExplorer {

	@Override
	public Set<Move> getPossibleMoves(State state) {
		Set<Move> moves = new HashSet<Move>();

		Color color = state.getTurn();

		for (int i = 0; i < State.ROWS; i++) {
			for (int j = 0; j < State.COLS; j++) {
				Position start = new Position(i, j);

				Piece piece = state.getPiece(start.getRow(), start.getCol());

				if (piece != null && piece.getColor().equals(color)) {
					moves.addAll(getPossibleMovesFromPosition(state, start));
				}
			}

		}
		return moves;
	}

	@Override
	public Set<Move> getPossibleMovesFromPosition(State state, Position start) {
		Piece piece = state.getPiece(start);

		if(piece == null) {
			return new HashSet<Move>();
		}
		
		Set<Move> moves = getMovesFromPosition(state, start);

		if (piece.getKind().equals(PieceKind.KING)) {
			moves.addAll(getCastleMoves(state, start));
		}

		Iterator<Move> iter = moves.iterator();
		while (iter.hasNext()) {
			Move move = iter.next();
			State afterState = state.copy();

			StateManipulator sm = new StateManipulator();

			sm.doMove(afterState, move);

			// if move exposes king then its not allowed
			if (isPositionUnderAttack(afterState, findKing(afterState, piece.getColor()), piece.getColor())) {
				iter.remove();
			}
		}

		return moves;
	}

	private Set<Move> getMovesFromPosition(State state, Position start) {
		Piece piece = state.getPiece(start);

		if(piece == null) {
			return new HashSet<Move>();
		}
		
		Set<Move> moves = new HashSet<Move>();

		switch (piece.getKind()) {
		case BISHOP:
			moves = getPossibleMovesForBishop(state, start);
			break;
		case KING:
			moves = getPossibleMovesForKing(state, start);
			break;
		case KNIGHT:
			moves = getPossibleMovesForKnight(state, start);
			break;
		case PAWN:
			moves = getPossibleMovesForPawn(state, start);
			break;
		case QUEEN:
			moves = getPossibleMovesForQueen(state, start);
			break;
		case ROOK:
			moves = getPossibleMovesForRook(state, start);
			break;
		}

		return moves;
	}

	private Set<Move> getPossibleMovesForPawn(State state, Position start) {
		Piece piece = state.getPiece(start);
		Set<Move> moves = new HashSet<Move>();

		int row = start.getRow();
		int col = start.getCol();

		int increment = piece.getColor().equals(Color.WHITE) ? 1 : -1;

		int backRow = piece.getColor().equals(Color.WHITE) ? 7 : 0;

		int startRow = piece.getColor().equals(Color.WHITE) ? 1 : 6;

		PieceKind[] promoteTypes = { PieceKind.BISHOP, PieceKind.KNIGHT, PieceKind.QUEEN, PieceKind.ROOK };

		Position oneAhead = new Position(row + increment, col);
		Position attackLeft = new Position(row + increment, col - 1);
		Position attackRight = new Position(row + increment, col + 1);

		Position enpassant = null;
		if (state.getEnpassantPosition() != null) {
			// get the position where out pawn will end up if doing enpassant
			// the enpassant position variable in the state is only the position
			// of the pawn we are killing
			enpassant = new Position(state.getEnpassantPosition().getRow() + increment, state.getEnpassantPosition().getCol());
		}

		if (withinBoard(oneAhead)) {
			Piece otherPiece = state.getPiece(oneAhead);
			if (otherPiece == null) {
				if (oneAhead.getRow() != backRow) {
					moves.add(new Move(start, oneAhead, null));
				}

				if (oneAhead.getRow() == backRow) {
					for (PieceKind kind : promoteTypes) {
						moves.add(new Move(start, oneAhead, kind));
					}
				}
			}
		}

		if (withinBoard(attackLeft)) {
			Piece otherPiece = state.getPiece(attackLeft);

			if ((otherPiece != null && !otherPiece.getColor().equals(piece.getColor())) || attackLeft.equals(enpassant)) {

				if (attackLeft.getRow() != backRow) {
					moves.add(new Move(start, attackLeft, null));
				}

				if (attackLeft.getRow() == backRow) {
					for (PieceKind kind : promoteTypes) {
						moves.add(new Move(start, attackLeft, kind));
					}
				}
			}

		}

		if (withinBoard(attackRight)) {
			Piece otherPiece = state.getPiece(attackRight);
			if (otherPiece != null && !otherPiece.getColor().equals(piece.getColor()) || attackRight.equals(enpassant)) {

				if (attackRight.getRow() != backRow) {
					moves.add(new Move(start, attackRight, null));
				}

				if (attackRight.getRow() == backRow) {
					for (PieceKind kind : promoteTypes) {
						moves.add(new Move(start, attackRight, kind));
					}
				}
			}

		}

		if (start.getRow() == startRow) {
			Piece oneAheadPiece = state.getPiece(start.getRow() + increment, start.getCol());
			Piece twoAheadPiece = state.getPiece(start.getRow() + (increment * 2), start.getCol());

			if (oneAheadPiece == null && twoAheadPiece == null) {
				moves.add(new Move(start, new Position(start.getRow() + (increment * 2), start.getCol()), null));
			}

		}
		return moves;
	}

	private boolean withinBoard(Position pos) {
		return withinBoard(pos.getRow(), pos.getCol());
	}

	private Set<Move> getPossibleMovesForKing(State state, Position start) {
		Piece piece = state.getPiece(start);
		Set<Move> moves = new HashSet<Move>();

		int[][] increments = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 }, { 1, 1 }, { 1, -1 }, { -1, 1 }, { -1, -1 } };

		int row = start.getRow();
		int col = start.getCol();

		for (int[] increment : increments) {
			int newRow = row + increment[0];
			int newCol = col + increment[1];

			if (withinBoard(newRow, newCol)) {

				Piece otherPiece = state.getPiece(newRow, newCol);

				if (otherPiece != null) {
					if (otherPiece.getColor().equals(piece.getColor())) {
					} else {
						moves.add(new Move(start, new Position(newRow, newCol), null));
					}
				} else {
					moves.add(new Move(start, new Position(newRow, newCol), null));
				}
			}
		}

		return moves;
	}

	private Set<Move> getCastleMoves(State state, Position start) {
		Piece piece = state.getPiece(start);
		Set<Move> moves = new HashSet<Move>();
		if (!isPositionUnderAttack(state, start, piece.getColor())) {
			if (state.isCanCastleKingSide(piece.getColor())) {
				int[] castleCols = { 5, 6 };

				boolean canCastle = true;
				for (int castleCol : castleCols) {
					Piece otherPiece = state.getPiece(start.getRow(), castleCol);

					if (otherPiece != null) {
						canCastle = false;
						break;
					}

					if (isPositionUnderAttack(state, new Position(start.getRow(), castleCol), piece.getColor())) {
						canCastle = false;
						break;
					}

				}

				if (canCastle) {
					moves.add(new Move(start, new Position(start.getRow(), 6), null));
				}

			}

			if (state.isCanCastleQueenSide(piece.getColor())) {
				// first check that the rook can move to the next square
				Piece otherPiece = state.getPiece(start.getRow(), 1);

				if (otherPiece == null) {
					// now check the rest of the squares are empty and not
					// under attack
					int[] castleCols = { 2, 3 };

					boolean canCastle = true;
					for (int castleCol : castleCols) {
						otherPiece = state.getPiece(start.getRow(), castleCol);

						if (otherPiece != null) {
							canCastle = false;
							break;
						}

						if (isPositionUnderAttack(state, new Position(start.getRow(), castleCol), piece.getColor())) {
							canCastle = false;
							break;
						}
					}

					if (canCastle) {
						moves.add(new Move(start, new Position(start.getRow(), 2), null));
					}
				}
			}
		}

		return moves;
	}

	private Set<Move> getPossibleMovesForKnight(State state, Position start) {
		Piece piece = state.getPiece(start);
		Set<Move> moves = new HashSet<Move>();

		int[][] increments = { { 2, 1 }, { 2, -1 }, { -2, 1 }, { -2, -1 }, { 1, 2 }, { -1, 2 }, { 1, -2 }, { -1, -2 } };

		int row = start.getRow();
		int col = start.getCol();

		for (int[] increment : increments) {
			int newRow = row + increment[0];
			int newCol = col + increment[1];

			if (withinBoard(newRow, newCol)) {

				Piece otherPiece = state.getPiece(newRow, newCol);

				if (otherPiece != null) {
					if (otherPiece.getColor().equals(piece.getColor())) {
					} else {
						moves.add(new Move(start, new Position(newRow, newCol), null));
					}
				} else {
					moves.add(new Move(start, new Position(newRow, newCol), null));
				}
			}
		}

		return moves;
	}

	private Set<Move> getPossibleMovesForQueen(State state, Position start) {
		Set<Move> moves = new HashSet<Move>();

		moves.addAll(getPossibleMovesForRook(state, start));
		moves.addAll(getPossibleMovesForBishop(state, start));

		return moves;
	}

	private Set<Move> getPossibleMovesForRook(State state, Position start) {
		Piece piece = state.getPiece(start);
		Set<Move> moves = new HashSet<Move>();

		int[][] increments = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };

		int row = start.getRow();
		int col = start.getCol();

		for (int[] increment : increments) {
			int newRow = row + increment[0];
			int newCol = col + increment[1];

			while (withinBoard(newRow, newCol)) {
				Piece otherPiece = state.getPiece(newRow, newCol);

				if (otherPiece != null) {
					if (otherPiece.getColor().equals(piece.getColor())) {
						break;
					} else {
						moves.add(new Move(start, new Position(newRow, newCol), null));
						break;
					}
				}

				moves.add(new Move(start, new Position(newRow, newCol), null));
				newRow += increment[0];
				newCol += increment[1];
			}
		}

		return moves;
	}

	private Set<Move> getPossibleMovesForBishop(State state, Position start) {
		Piece piece = state.getPiece(start);
		Set<Move> moves = new HashSet<Move>();

		int[][] increments = { { 1, 1 }, { 1, -1 }, { -1, 1 }, { -1, -1 } };

		int row = start.getRow();
		int col = start.getCol();

		for (int[] increment : increments) {
			int newRow = row + increment[0];
			int newCol = col + increment[1];

			while (withinBoard(newRow, newCol)) {
				Piece otherPiece = state.getPiece(newRow, newCol);

				if (otherPiece != null) {
					if (otherPiece.getColor().equals(piece.getColor())) {
						break;
					} else {
						moves.add(new Move(start, new Position(newRow, newCol), null));
						break;
					}
				}

				moves.add(new Move(start, new Position(newRow, newCol), null));
				newRow += increment[0];
				newCol += increment[1];
			}
		}

		return moves;
	}

	/**
	 * Test to see if a position is under attack
	 * 
	 * @param state
	 *            the state of the board
	 * @param pos
	 *            the position we are testing
	 * @param color
	 *            the color of the player who owns the piece at pos. The
	 *            opposite color will be used to find attacking pieces
	 * @return true if the position is under attack by the opposite color, false
	 *         otherwise
	 */
	public boolean isPositionUnderAttack(State state, Position pos, Color color) {
		PieceKind[] attackTypes = { PieceKind.PAWN, PieceKind.BISHOP, PieceKind.KNIGHT, PieceKind.QUEEN, PieceKind.ROOK, PieceKind.KING };

		boolean underAttack = false;
		for (PieceKind kind : attackTypes) {
			State copy = state.copy();
			copy.setPiece(pos, new Piece(color, kind));

			Set<Move> moves = getMovesFromPosition(copy, pos);
			Piece piece = new Piece(color.getOpposite(), kind);
			for (Move move : moves) {
				Piece otherPiece = copy.getPiece(move.getTo());

				if (piece.equals(otherPiece)) {
					underAttack = true;
					break;
				}
			}
		}

		return underAttack;
	}

	/**
	 * Return the position of a player's king
	 * 
	 * @param state
	 *            state of the board
	 * @param color
	 *            the player's color
	 * @return
	 */
	public Position findKing(State state, Color color) {
		Piece king = new Piece(color, PieceKind.KING);
		for (int i = 0; i < State.ROWS; i++) {
			for (int j = 0; j < State.COLS; j++) {
				Piece piece = state.getPiece(i, j);

				if (king.equals(piece)) {
					return new Position(i, j);
				}
			}
		}
		// couldn't find the king
		throw new RuntimeException("No King!!");
	}

	@Override
	public Set<Position> getPossibleStartPositions(State state) {
		Set<Move> moves = getPossibleMoves(state);
		
		
		Set<Position> positions = new HashSet<Position>();
		for(Move move : moves) {
			positions.add(move.getFrom());
		}
		
		return positions;
	}

	private boolean withinBoard(int row, int col) {
		return row >= 0 && row <= 7 && col >= 0 && col <= 7;
	}
}
