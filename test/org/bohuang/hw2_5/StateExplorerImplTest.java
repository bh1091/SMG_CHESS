package org.bohuang.hw2_5;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.junit.Test;

import org.shared.chess.*;

import com.google.common.collect.Sets;

public abstract class StateExplorerImplTest extends AbstractStateExplorerTest{

	@Override
	public StateExplorer getStateExplorer() {
		// TODO Auto-generated method stub
		return new StateExplorerImpl();
	}
	
	public void clearPiece(State state) {
		
		for (int i = 0 ; i <= 7 ; i ++){
			for (int j = 0 ; j <= 7 ; j++){
				state.setPiece(i , j, null);
			}
		}
		
		state.setCanCastleKingSide(Color.WHITE, false);
		state.setCanCastleKingSide(Color.BLACK, false);
		state.setCanCastleQueenSide(Color.WHITE, false);
		state.setCanCastleQueenSide(Color.BLACK, false);
		
	}
	
	/*
	 * start test by bo huang
	 * */
	
	@Test
	public void testGetPossibleMoves_InitStateBlack() {
		Set<Move> expectedMoves = Sets.newHashSet();
		for (int i = 0 ; i <= 7 ; i++){
			expectedMoves.add(new Move(new Position(6,i),new Position(5,i),null));
			expectedMoves.add(new Move(new Position(6,i),new Position(4,i),null));
		}
		expectedMoves.add(new Move(new Position(7, 1), new Position(5, 0), null));
	    expectedMoves.add(new Move(new Position(7, 1), new Position(5, 2), null));
	    expectedMoves.add(new Move(new Position(7, 6), new Position(5, 5), null));
	    expectedMoves.add(new Move(new Position(7, 6), new Position(5, 7), null));
	    
	    State state = start.copy();
	    state.setTurn(Color.BLACK);
	    
	    assertEquals(expectedMoves,
		        stateExplorer.getPossibleMoves(state));
	}
	
	@Test
	public void testGetPossibleMoves_PawnCannotMoveWhite() {
		Set<Move> expectedMoves = Sets.newHashSet();
		State state = start.copy();
	    clearPiece(state);
	    
	    state.setPiece(0, 0, new Piece (Color.WHITE,PieceKind.KING));
	    state.setPiece(7, 7, new Piece(Color.BLACK,PieceKind.KING));
	    state.setPiece(3, 3, new Piece(Color.WHITE,PieceKind.PAWN));
	    state.setPiece(4, 3, new Piece(Color.BLACK,PieceKind.PAWN));
	    
	    expectedMoves.add(new Move(new Position(0,0),new Position(0,1),null));
	    expectedMoves.add(new Move(new Position(0,0),new Position(1,1),null));
	    expectedMoves.add(new Move(new Position(0,0),new Position(1,0),null));
	    
	    assertEquals(expectedMoves,
		        stateExplorer.getPossibleMoves(state));
	}
	
	@Test
	public void testGetPossibleMoves_PawnCaptureEnpassWhite() {
		Set<Move> expectedMoves = Sets.newHashSet();
		State state = start.copy();
	    clearPiece(state);
	    
	    state.setPiece(0, 0, new Piece (Color.WHITE,PieceKind.KING));
	    state.setPiece(7, 7, new Piece(Color.BLACK,PieceKind.KING));
	    state.setPiece(4, 3, new Piece(Color.WHITE,PieceKind.PAWN));
	    state.setPiece(4, 4, new Piece(Color.BLACK,PieceKind.PAWN));
	    state.setEnpassantPosition(new Position(4,4));
	    
	    expectedMoves.add(new Move(new Position(0,0),new Position(0,1),null));
	    expectedMoves.add(new Move(new Position(0,0),new Position(1,1),null));
	    expectedMoves.add(new Move(new Position(0,0),new Position(1,0),null));
	    expectedMoves.add(new Move(new Position(4,3),new Position(5,4),null));
	    expectedMoves.add(new Move(new Position(4,3),new Position(5,3),null));
	    
	    assertEquals(expectedMoves,
		        stateExplorer.getPossibleMoves(state));
	}
	

	@Test
	public void testGetPossibleMoves_CheckMateWhite() {
		Set<Position> expectedPositions = Sets.newHashSet();
	    	    
	    State state = start.copy();
	    clearPiece(state);
	    
	    state.setPiece(0, 0, new Piece (Color.WHITE,PieceKind.KING));
	    state.setPiece(0, 2, new Piece(Color.BLACK,PieceKind.ROOK));
	    state.setPiece(1, 2, new Piece(Color.BLACK,PieceKind.ROOK));
	    state.setPiece(7, 7, new Piece(Color.BLACK,PieceKind.KING));
	    
	    
	    assertEquals(expectedPositions,
	        stateExplorer.getPossibleMoves(state));
	}
	
	@Test
	public void testGetPossibleMoves_CheckMateBlack() {
		Set<Position> expectedPositions = Sets.newHashSet();
	    	    
	    State state = start.copy();
	    clearPiece(state);
	    
	    state.setPiece(0, 0, new Piece (Color.WHITE,PieceKind.KING));
	    state.setPiece(7, 2, new Piece(Color.WHITE,PieceKind.ROOK));
	    state.setPiece(6, 2, new Piece(Color.WHITE,PieceKind.ROOK));
	    state.setPiece(7, 7, new Piece(Color.BLACK,PieceKind.KING));
	    state.setTurn(Color.BLACK);
	    
	    assertEquals(expectedPositions,
	        stateExplorer.getPossibleMoves(state));
	}
	
	@Test
	public void testGetPossibleMoves_StaleMateWhite() {
		Set<Position> expectedPositions = Sets.newHashSet();
	    	    
	    State state = start.copy();
	    clearPiece(state);
	    
	    state.setPiece(0, 0, new Piece (Color.WHITE,PieceKind.KING));
	    state.setPiece(2, 1, new Piece(Color.BLACK,PieceKind.ROOK));
	    state.setPiece(1, 2, new Piece(Color.BLACK,PieceKind.ROOK));
	    state.setPiece(7, 7, new Piece(Color.BLACK,PieceKind.KING));
	    	    
	    assertEquals(expectedPositions,
	        stateExplorer.getPossibleMoves(state));
	}
	
	@Test
	public void testGetPossibleMoves_StaleMateBlack() {
		Set<Position> expectedPositions = Sets.newHashSet();
	    	    
	    State state = start.copy();
	    clearPiece(state);
	    
	    state.setPiece(0, 0, new Piece (Color.WHITE,PieceKind.KING));
	    state.setPiece(5, 6, new Piece(Color.WHITE,PieceKind.ROOK));
	    state.setPiece(6, 5, new Piece(Color.WHITE,PieceKind.ROOK));
	    state.setPiece(7, 7, new Piece(Color.BLACK,PieceKind.KING));
	    state.setTurn(Color.BLACK);
	    
	    assertEquals(expectedPositions,
	        stateExplorer.getPossibleMoves(state));
	}
	
	
	@Test
	public void testGetPossibleStartPositions_InitStateBlack() {
		Set<Position> expectedPositions = Sets.newHashSet();
	    // pawn positions
	    for (int c = 0; c < 8; c++)
	      expectedPositions.add(new Position(6, c));
	    // knight positions
	    expectedPositions.add(new Position(7, 1));
	    expectedPositions.add(new Position(7, 6));
	    
	    State state = start.copy();
	    state.setTurn(Color.BLACK);
	    
	    assertEquals(expectedPositions,
	        stateExplorer.getPossibleStartPositions(state));
	}
	
	@Test
	public void testGetPossibleStartPositions_CheckMateWhite() {
		Set<Position> expectedPositions = Sets.newHashSet();
	    	    
	    State state = start.copy();
	    clearPiece(state);
	    
	    state.setPiece(0, 0, new Piece (Color.WHITE,PieceKind.KING));
	    state.setPiece(0, 2, new Piece(Color.BLACK,PieceKind.ROOK));
	    state.setPiece(1, 2, new Piece(Color.BLACK,PieceKind.ROOK));
	    state.setPiece(7, 7, new Piece(Color.BLACK,PieceKind.KING));
	    
	    
	    assertEquals(expectedPositions,
	        stateExplorer.getPossibleStartPositions(state));
	}
	
	@Test
	public void testGetPossibleStartPositions_CheckMateBlack() {
		Set<Position> expectedPositions = Sets.newHashSet();
	    	    
	    State state = start.copy();
	    clearPiece(state);
	    
	    state.setPiece(0, 0, new Piece (Color.WHITE,PieceKind.KING));
	    state.setPiece(7, 2, new Piece(Color.WHITE,PieceKind.ROOK));
	    state.setPiece(6, 2, new Piece(Color.WHITE,PieceKind.ROOK));
	    state.setPiece(7, 7, new Piece(Color.BLACK,PieceKind.KING));
	    state.setTurn(Color.BLACK);
	    
	    assertEquals(expectedPositions,
	        stateExplorer.getPossibleStartPositions(state));
	}
	
	@Test
	public void testGetPossibleStartPositions_StaleMateWhite() {
		Set<Position> expectedPositions = Sets.newHashSet();
	    	    
	    State state = start.copy();
	    clearPiece(state);
	    
	    state.setPiece(0, 0, new Piece (Color.WHITE,PieceKind.KING));
	    state.setPiece(2, 1, new Piece(Color.BLACK,PieceKind.ROOK));
	    state.setPiece(1, 2, new Piece(Color.BLACK,PieceKind.ROOK));
	    state.setPiece(7, 7, new Piece(Color.BLACK,PieceKind.KING));
	    	    
	    assertEquals(expectedPositions,
	        stateExplorer.getPossibleStartPositions(state));
	}
	
	@Test
	public void testGetPossibleStartPositions_StaleMateBlack() {
		Set<Position> expectedPositions = Sets.newHashSet();
	    	    
	    State state = start.copy();
	    clearPiece(state);
	    
	    state.setPiece(0, 0, new Piece (Color.WHITE,PieceKind.KING));
	    state.setPiece(5, 6, new Piece(Color.WHITE,PieceKind.ROOK));
	    state.setPiece(6, 5, new Piece(Color.WHITE,PieceKind.ROOK));
	    state.setPiece(7, 7, new Piece(Color.BLACK,PieceKind.KING));
	    state.setTurn(Color.BLACK);
	    
	    assertEquals(expectedPositions,
	        stateExplorer.getPossibleStartPositions(state));
	}
	
	@Test
	  public void testGetPossibleMovesFromPosition_Rook() {
		Set<Move> expectedMoves = Sets.newHashSet();
	    
	    State state = start.copy();
	    clearPiece(state);
	    
	    state.setPiece(0, 4, new Piece (Color.WHITE,PieceKind.KING));
	    state.setPiece(6, 0, new Piece(Color.WHITE,PieceKind.ROOK));
	    state.setPiece(6, 2, new Piece(Color.WHITE,PieceKind.ROOK));
	    state.setPiece(4, 0, new Piece(Color.BLACK,PieceKind.ROOK));
	    state.setPiece(7, 7, new Piece(Color.BLACK,PieceKind.KING));
	    
	    expectedMoves.add(new Move(new Position(6, 0), new Position(7, 0), null));
	    expectedMoves.add(new Move(new Position(6, 0), new Position(5, 0), null));
	    expectedMoves.add(new Move(new Position(6, 0), new Position(4, 0), null));
	    expectedMoves.add(new Move(new Position(6, 0), new Position(6, 1), null));
	    
	    assertEquals(expectedMoves,
	        stateExplorer.getPossibleMovesFromPosition(state, new Position(6, 0)));
	  }
	
	@Test
	  public void testGetPossibleMovesFromPosition_Knight() {
		Set<Move> expectedMoves = Sets.newHashSet();
	    
	    State state = start.copy();
	    clearPiece(state);
	    
	    state.setPiece(0, 4, new Piece (Color.WHITE,PieceKind.KING));
	    state.setPiece(3, 3, new Piece(Color.WHITE,PieceKind.KNIGHT));
	    state.setPiece(1, 2, new Piece(Color.WHITE,PieceKind.PAWN));
	    state.setPiece(2, 1, new Piece(Color.WHITE,PieceKind.PAWN));
	    state.setPiece(1, 4, new Piece(Color.WHITE,PieceKind.PAWN));
	    state.setPiece(2, 5, new Piece(Color.WHITE,PieceKind.PAWN));
	    state.setPiece(4, 0, new Piece(Color.BLACK,PieceKind.ROOK));
	    state.setPiece(7, 7, new Piece(Color.BLACK,PieceKind.KING));
	    
	    expectedMoves.add(new Move(new Position(3, 3), new Position(5, 2), null));
	    expectedMoves.add(new Move(new Position(3, 3), new Position(4, 1), null));
	    expectedMoves.add(new Move(new Position(3, 3), new Position(4, 5), null));
	    expectedMoves.add(new Move(new Position(3, 3), new Position(5, 4), null));
	    
	    assertEquals(expectedMoves,
	        stateExplorer.getPossibleMovesFromPosition(state, new Position(3, 3)));
	  }
	
	@Test
	  public void testGetPossibleMovesFromPosition_Bishop() {
		Set<Move> expectedMoves = Sets.newHashSet();
	    
	    State state = start.copy();
	    clearPiece(state);
	    
	    state.setPiece(0, 4, new Piece (Color.WHITE,PieceKind.KING));
	    state.setPiece(3, 3, new Piece(Color.WHITE,PieceKind.BISHOP));
	    state.setPiece(5, 5, new Piece(Color.WHITE,PieceKind.PAWN));
	    state.setPiece(2, 2, new Piece(Color.WHITE,PieceKind.PAWN));
	    state.setPiece(5, 1, new Piece(Color.BLACK,PieceKind.PAWN));
	    state.setPiece(2, 4, new Piece(Color.BLACK,PieceKind.PAWN));
	    state.setPiece(7, 7, new Piece(Color.BLACK,PieceKind.KING));
	    
	    expectedMoves.add(new Move(new Position(3, 3), new Position(4, 4), null));
	    expectedMoves.add(new Move(new Position(3, 3), new Position(4, 2), null));
	    expectedMoves.add(new Move(new Position(3, 3), new Position(5, 1), null));
	    expectedMoves.add(new Move(new Position(3, 3), new Position(2, 4), null));
	    
	    assertEquals(expectedMoves,
	        stateExplorer.getPossibleMovesFromPosition(state, new Position(3, 3)));
	  }
	
	@Test
	  public void testGetPossibleMovesFromPosition_Queen() {
		Set<Move> expectedMoves = Sets.newHashSet();
	    
	    State state = start.copy();
	    clearPiece(state);
	    
	    state.setPiece(0, 4, new Piece (Color.WHITE,PieceKind.KING));
	    state.setPiece(3, 3, new Piece(Color.WHITE,PieceKind.QUEEN));
	    state.setPiece(5, 5, new Piece(Color.WHITE,PieceKind.PAWN));
	    state.setPiece(5, 3, new Piece(Color.WHITE,PieceKind.PAWN));
	    state.setPiece(3, 2, new Piece(Color.WHITE,PieceKind.PAWN));
	    state.setPiece(3, 4, new Piece(Color.WHITE,PieceKind.PAWN));
	    state.setPiece(2, 3, new Piece(Color.WHITE,PieceKind.PAWN));
	    state.setPiece(2, 2, new Piece(Color.WHITE,PieceKind.PAWN));
	    state.setPiece(5, 1, new Piece(Color.BLACK,PieceKind.PAWN));
	    state.setPiece(2, 4, new Piece(Color.BLACK,PieceKind.PAWN));
	    state.setPiece(7, 7, new Piece(Color.BLACK,PieceKind.KING));
	    
	    expectedMoves.add(new Move(new Position(3, 3), new Position(4, 4), null));
	    expectedMoves.add(new Move(new Position(3, 3), new Position(4, 2), null));
	    expectedMoves.add(new Move(new Position(3, 3), new Position(5, 1), null));
	    expectedMoves.add(new Move(new Position(3, 3), new Position(2, 4), null));
	    expectedMoves.add(new Move(new Position(3, 3), new Position(4, 3), null));
	    
	    assertEquals(expectedMoves,
	        stateExplorer.getPossibleMovesFromPosition(state, new Position(3, 3)));
	  }
	
	@Test
	  public void testGetPossibleMovesFromPosition_KingCanCastling() {
		Set<Move> expectedMoves = Sets.newHashSet();
	    
	    State state = start.copy();	    
	    
	    state.setPiece(0, 1, null);
	    state.setPiece(0, 2, null);
	    state.setPiece(0, 3, null);
	    state.setPiece(0, 5, null);
	    state.setPiece(0, 6, null);
	    
	    
	    expectedMoves.add(new Move(new Position(0, 4), new Position(0, 3), null));
	    expectedMoves.add(new Move(new Position(0, 4), new Position(0, 2), null));
	    expectedMoves.add(new Move(new Position(0, 4), new Position(0, 5), null));
	    expectedMoves.add(new Move(new Position(0, 4), new Position(0, 6), null));
	    
	    assertEquals(expectedMoves,
	        stateExplorer.getPossibleMovesFromPosition(state, new Position(0, 4)));
	  }
	
	@Test
	  public void testGetPossibleMovesFromPosition_KingCannotCastling() {
		Set<Move> expectedMoves = Sets.newHashSet();
	    
	    State state = start.copy();	 
	    state.setCanCastleKingSide(Color.WHITE, false);
	    state.setCanCastleQueenSide(Color.WHITE, false);
	    
	    state.setPiece(0, 1, null);
	    state.setPiece(0, 2, null);
	    state.setPiece(0, 3, null);
	    state.setPiece(0, 5, null);
	    state.setPiece(0, 6, null);
	    
	    
	    expectedMoves.add(new Move(new Position(0, 4), new Position(0, 3), null));
	    expectedMoves.add(new Move(new Position(0, 4), new Position(0, 5), null));
	    
	    assertEquals(expectedMoves,
	        stateExplorer.getPossibleMovesFromPosition(state, new Position(0, 4)));
	  }
	
	@Test
	  public void testGetPossibleMovesFromPosition_PawnCanEnp() {
		Set<Move> expectedMoves = Sets.newHashSet();
	    
	    State state = start.copy();
	    clearPiece(state);
	    
	    state.setPiece(0, 4, new Piece (Color.WHITE,PieceKind.KING));
	    state.setPiece(4, 4, new Piece(Color.WHITE,PieceKind.PAWN));
	    state.setPiece(4, 3, new Piece(Color.BLACK,PieceKind.PAWN));
	    state.setPiece(5, 5, new Piece(Color.BLACK,PieceKind.PAWN));
	    state.setPiece(7, 7, new Piece(Color.BLACK,PieceKind.KING));
	    state.setEnpassantPosition(new Position(4,3));
	    
	    expectedMoves.add(new Move(new Position(4, 4), new Position(5, 4), null));
	    expectedMoves.add(new Move(new Position(4, 4), new Position(5, 3), null));
	    expectedMoves.add(new Move(new Position(4, 4), new Position(5, 5), null));
	    
	    
	    assertEquals(expectedMoves,
	        stateExplorer.getPossibleMovesFromPosition(state, new Position(4, 4)));
	  }
	
	@Test
	  public void testGetPossibleMovesFromPosition_PawnCannotEnp() {
		Set<Move> expectedMoves = Sets.newHashSet();
	    
	    State state = start.copy();
	    clearPiece(state);
	    
	    state.setPiece(0, 4, new Piece (Color.WHITE,PieceKind.KING));
	    state.setPiece(4, 4, new Piece(Color.WHITE,PieceKind.PAWN));
	    state.setPiece(4, 3, new Piece(Color.BLACK,PieceKind.PAWN));
	    state.setPiece(5, 5, new Piece(Color.BLACK,PieceKind.PAWN));
	    state.setPiece(7, 7, new Piece(Color.BLACK,PieceKind.KING));
	    
	    
	    expectedMoves.add(new Move(new Position(4, 4), new Position(5, 4), null));
	    expectedMoves.add(new Move(new Position(4, 4), new Position(5, 5), null));
	    
	    
	    assertEquals(expectedMoves,
	        stateExplorer.getPossibleMovesFromPosition(state, new Position(4, 4)));
	  }

	/*
	 * end test by bo huang
	 * */
	
	@Test
	  public void testGetPossibleStartPositions_InitState() {
	    Set<Position> expectedPositions = Sets.newHashSet();
	    // pawn positions
	    for (int c = 0; c < 8; c++)
	      expectedPositions.add(new Position(1, c));
	    // knight positions
	    expectedPositions.add(new Position(0, 1));
	    expectedPositions.add(new Position(0, 6));
	    assertEquals(expectedPositions,
	        stateExplorer.getPossibleStartPositions(start));
	  }
	  @Test
	  public void testGetPossibleMoves_InitState() {
	    Set<Move> expectedMoves = Sets.newHashSet();
	    // pawn moves
	    for (int c = 0; c < 8; c++) {
	      expectedMoves.add(new Move(new Position(1, c), new Position(2, c), null));
	      expectedMoves.add(new Move(new Position(1, c), new Position(3, c), null));
	    }
	    // knight moves
	    expectedMoves.add(new Move(new Position(0, 1), new Position(2, 0), null));
	    expectedMoves.add(new Move(new Position(0, 1), new Position(2, 2), null));
	    expectedMoves.add(new Move(new Position(0, 6), new Position(2, 5), null));
	    expectedMoves.add(new Move(new Position(0, 6), new Position(2, 7), null));
	    assertEquals(expectedMoves,
	        stateExplorer.getPossibleMoves(start));
	  }
	  
	  @Test
	  public void testGetPossibleMovesFromPosition_InitStateForLeftKnight() {
	    Set<Move> expectedMoves = Sets.newHashSet();
	    // knight moves
	    expectedMoves.add(new Move(new Position(0, 1), new Position(2, 0), null));
	    expectedMoves.add(new Move(new Position(0, 1), new Position(2, 2), null));
	    assertEquals(expectedMoves,
	        stateExplorer.getPossibleMovesFromPosition(start, new Position(0, 1)));
	  }

	  @Test
	  public void testGetPossibleMovesFromPosition_Promotion() {
	    start.setPiece(new Position(1, 0), null);
	    start.setPiece(new Position(6, 0), new Piece(Color.WHITE, PieceKind.PAWN));
	    
	    Set<Move> expectedMoves = Sets.newHashSet();
	    // promotion moves
	    expectedMoves.add(new Move(new Position(6, 0), new Position(7, 1), PieceKind.BISHOP));
	    expectedMoves.add(new Move(new Position(6, 0), new Position(7, 1), PieceKind.KNIGHT));
	    expectedMoves.add(new Move(new Position(6, 0), new Position(7, 1), PieceKind.ROOK));
	    expectedMoves.add(new Move(new Position(6, 0), new Position(7, 1), PieceKind.QUEEN));
	    assertEquals(expectedMoves,
	        stateExplorer.getPossibleMovesFromPosition(start, new Position(6, 0)));
	  }

}
