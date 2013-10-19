package org.vorasahil.hw2_5;

import org.vorasahil.hw2.*;

import static org.shared.chess.Color.BLACK;
import static org.shared.chess.Color.WHITE;
import static org.shared.chess.PieceKind.BISHOP;
import static org.shared.chess.PieceKind.KNIGHT;
import static org.shared.chess.PieceKind.PAWN;
import static org.shared.chess.PieceKind.QUEEN;
import static org.shared.chess.PieceKind.ROOK;

import java.util.*;

import org.shared.chess.*;

/**
 * 
 * @author Sahil Vora
 * 
 */
public class StateExplorerImpl implements StateExplorer {

	StateChangerImpl stateChanger = new StateChangerImpl();

	@Override
	public Set<Move> getPossibleMoves(State state) {
		State copy = state.copy();
		Color turn = copy.getTurn();
		Set<Move> moves = new HashSet<Move>();
		for (int i = 0; i <= 7; i++) {
			for (int j = 0; j <= 7; j++) {
				copy = state.copy();
				Position p = new Position(i, j);
				Piece piece = copy.getPiece(p);
				if (piece != null && piece.getColor().equals(turn))
					moves.addAll(getPossibleMovesFromPosition(copy, p));
			}
		}
		return moves;
	}

	@Override
	public Set<Move> getPossibleMovesFromPosition(State state, Position start) {
		State copy = state.copy();
		Color turn = state.getTurn();
		Piece piece = state.getPiece(start);

		int row = start.getRow();

		boolean isPawn;
		if (piece != null)
			isPawn = piece.getKind().equals(PAWN);
		else
			isPawn = false;
		int toRow = 0;
		boolean isPromotionPossible = false;
		if (isPawn && (turn.equals(BLACK) && row == 1)) {
			toRow = 0;
			isPromotionPossible = true;
		}
		if (isPawn && turn.equals(WHITE) && row == 6) {
			toRow = 7;
			isPromotionPossible = true;
		}
		Set<Move> moves = new HashSet<Move>();
		List<Position> position = getToPosition(start, state);
		for (Position pos : position) {
			Move move = new Move(start, pos, null);
			boolean valid = true;
			try {
				State copy2 = copy.copy();
				stateChanger.tryMove(copy2, move);
			} catch (Exception e) {
				valid = false;
			}
			if (valid) {
				if (isPromotionPossible && pos.getRow() == toRow) {
					moves.add(new Move(start, pos, QUEEN));
					moves.add(new Move(start, pos, ROOK));
					moves.add(new Move(start, pos, BISHOP));
					moves.add(new Move(start, pos, KNIGHT));
				} else {
					moves.add(move);
				}
			}
		}

		return moves;
	}

	@Override
	public Set<Position> getPossibleStartPositions(State state) {
		State copy = state.copy();
		Color turn = copy.getTurn();
		Set<Position> positions = new HashSet<Position>();
		for (int i = 0; i <= 7; i++) {
			for (int j = 0; j <= 7; j++) {
				copy = state.copy();
				Position p = new Position(i, j);
				Piece piece = copy.getPiece(p);
				if (piece != null && piece.getColor().equals(turn))
					if (IsMovePossibleFromPosition(copy, p))
						positions.add(p);
			}
		}
		return positions;
	}

	/**
	 * Finds out of a move(any) is possible from the position in question.
	 * 
	 * @param state
	 * @param start
	 * @return True if move is possible, false otherwise.
	 */
	private boolean IsMovePossibleFromPosition(State state, Position start) {
		// TODO
		State copy2;
		for (Position pos : getToPosition(start, state)) {
			Move move = new Move(start, pos, null);
			boolean valid = true;
			try {
				copy2 = state.copy();
				stateChanger.tryMove(copy2, move);
			} catch (Exception e) {
				valid = false;
			}
			if (valid) {
				return true;
			}
		}
		return false;
	}

	private List<Position> getToPosition(Position start, State state) {
		
		Piece piece = state.getPiece(start);
		int row = start.getRow();
		int col = start.getCol();
		List<Position> position = new ArrayList<Position>();
		if (piece != null) {
			Color color = piece.getColor();
			switch (piece.getKind()) {
			case BISHOP:
				for (int i = 1; i < 8; i++) {
					if (checkWithinBoundaries(row + i)
							&& checkWithinBoundaries(col + i))
						position.add(new Position(row + i, col + i));
					if (checkWithinBoundaries(row + i)
							&& checkWithinBoundaries(col - i))
						position.add(new Position(row + i, col - i));
					if (checkWithinBoundaries(row - i)
							&& checkWithinBoundaries(col + i))
						position.add(new Position(row - i, col + i));
					if (checkWithinBoundaries(row - i)
							&& checkWithinBoundaries(col - i))
						position.add(new Position(row - i, col - i));
				}
				break;

			case KING:
				if (checkWithinBoundaries(row + 1)
						&& checkWithinBoundaries(col + 1))
					position.add(new Position(row + 1, col + 1));
				if (checkWithinBoundaries(row + 1)
						&& checkWithinBoundaries(col - 1))
					position.add(new Position(row + 1, col - 1));
				if (checkWithinBoundaries(row - 1)
						&& checkWithinBoundaries(col + 1))
					position.add(new Position(row - 1, col + 1));
				if (checkWithinBoundaries(row - 1)
						&& checkWithinBoundaries(col - 1))
					position.add(new Position(row - 1, col - 1));
				if (checkWithinBoundaries(row + 1)
						&& checkWithinBoundaries(col))
					position.add(new Position(row + 1, col));
				if (checkWithinBoundaries(row - 1)
						&& checkWithinBoundaries(col))
					position.add(new Position(row - 1, col));
				if (checkWithinBoundaries(row)
						&& checkWithinBoundaries(col + 1))
					position.add(new Position(row, col + 1));
				if (checkWithinBoundaries(row)
						&& checkWithinBoundaries(col - 1))
					position.add(new Position(row, col - 1));
				if (checkWithinBoundaries(row)
						&& checkWithinBoundaries(col - 2))
					position.add(new Position(row, col - 2));
				if (checkWithinBoundaries(row)
						&& checkWithinBoundaries(col + 2))
					position.add(new Position(row, col + 2));

				break;
			case KNIGHT:
				int[][] k = { { 2, 1 }, { 2, -1 }, { 1, 2 }, { 1, -2 },
						{ -2, 1 }, { -2, -1 }, { -1, 2 }, { -1, -2 } };
				for (int i = 0; i < 8; i++) {
					if (checkWithinBoundaries(row + k[i][0])
							&& checkWithinBoundaries(col + k[i][1]))
						position.add(new Position(row + k[i][0], col + k[i][1]));
				}
				break;
			case PAWN:
				int pawnInc = (color.equals(Color.WHITE)) ? 1 : -1;
				if (checkWithinBoundaries(row + pawnInc)) {
					position.add(new Position(row + pawnInc, col));
					if (checkWithinBoundaries(col + 1))
						position.add(new Position(row + pawnInc, col + 1));
					if (checkWithinBoundaries(col - 1))
						position.add(new Position(row + pawnInc, col - 1));
					if (checkWithinBoundaries(row + (2 * pawnInc)))
						position.add(new Position(row + (2 * pawnInc), col));
				}
				break;
			case QUEEN:
				for (int i = 1; i < 8; i++) {
					if (checkWithinBoundaries(row + i)
							&& checkWithinBoundaries(col + i))
						position.add(new Position(row + i, col + i));
					if (checkWithinBoundaries(row + i)
							&& checkWithinBoundaries(col - i))
						position.add(new Position(row + i, col - i));
					if (checkWithinBoundaries(row - i)
							&& checkWithinBoundaries(col + i))
						position.add(new Position(row - i, col + i));
					if (checkWithinBoundaries(row - i)
							&& checkWithinBoundaries(col - i))
						position.add(new Position(row - i, col - i));

					if (checkWithinBoundaries(row + i))
						position.add(new Position(row + i, col));
					if (checkWithinBoundaries(col - i))
						position.add(new Position(row, col - i));
					if (checkWithinBoundaries(col + i))
						position.add(new Position(row, col + i));
					if (checkWithinBoundaries(row - i))
						position.add(new Position(row - i, col));
				}

				break;
			case ROOK:
				for (int i = 1; i < 8; i++) {
					if (checkWithinBoundaries(row + i))
						position.add(new Position(row + i, col));
					if (checkWithinBoundaries(col - i))
						position.add(new Position(row, col - i));
					if (checkWithinBoundaries(col + i))
						position.add(new Position(row, col + i));
					if (checkWithinBoundaries(row - i))
						position.add(new Position(row - i, col));
				}

				break;
			default:
				break;

			}
		}
		return position;
	}

	private boolean checkWithinBoundaries(int x) {
		if (x >= 0 && x <= 7)
			return true;
		else
			return false;
	}
}