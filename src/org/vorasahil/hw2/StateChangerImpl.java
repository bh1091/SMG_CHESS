/**
 * 
 */
package org.vorasahil.hw2;

import org.shared.chess.*;

import java.util.*;
/**
 * @author Sahil Vora
 *
 */
public class StateChangerImpl implements StateChanger {

	/* (non-Javadoc)
	 * @see org.shared.chess.StateChanger#makeMove(org.shared.chess.State, org.shared.chess.Move)
	 */
	@Override
	public void makeMove(State state, Move move) throws IllegalMove {
		State copy=state.copy(); // Holds a copy of the state for backtracking.
		Position fromPosition= move.getFrom();
		Position toPosition= move.getTo();
		checkBoundaries(fromPosition);
		checkBoundaries(toPosition);
		boolean wasEnPassantMove=false;
		Color turn=state.getTurn();
		Piece fromPiece= state.getPiece(fromPosition);
		Piece toPiece= state.getPiece(toPosition);
		PieceKind promo=move.getPromoteToPiece(); // holds the promotion information
		boolean isCaptureMove=checkTurn(state,fromPiece,toPiece);
		boolean isEnPassantMove= checkIsEnPassantCaptureMove(state,fromPiece,toPiece,move);
		

		if(promo!=null  && !(fromPiece.getKind().equals(PieceKind.PAWN)))
			throw new IllegalMove();
		if(fromPosition.equals(toPosition))
			throw new IllegalMove();
		
		switch(fromPiece.getKind()){
			case BISHOP:
				checkBishopMovement(state, move, isCaptureMove);
				break;
			case KING:
				checkKingMovement(state, move, isCaptureMove);
				break;
			case KNIGHT:
				checkKnightMovement(state, move, isCaptureMove);
				break;
			case PAWN:
				wasEnPassantMove=checkPawnMovement(state, move, isCaptureMove,isEnPassantMove,promo);
				state.setNumberOfMovesWithoutCaptureNorPawnMoved(0);
				break;
			case QUEEN:
				checkQueenMovement(state, move, isCaptureMove);
				break;
			case ROOK:
				checkRookMovement(state, move, isCaptureMove);
				break;
			default:
				throw new IllegalMove();				
		}
		
		
		/*
		 * All Tests pass till here, therefore time to make the changes to the state..
		 */
		if(isCaptureMove){
			checkRookCapture(state,move,turn);
			state.setNumberOfMovesWithoutCaptureNorPawnMoved(0);
		}
		else{
			if(state.getPiece(fromPosition).getKind().equals(PieceKind.PAWN)){
				state.setNumberOfMovesWithoutCaptureNorPawnMoved(0);
			}
			else{
				int num=state.getNumberOfMovesWithoutCaptureNorPawnMoved();
				state.setNumberOfMovesWithoutCaptureNorPawnMoved(num+1);
				if(num+1==100){
					state.setGameResult(new GameResult(null,
							GameResultReason.FIFTY_MOVE_RULE));
				}
			}
		}
		if(wasEnPassantMove){
			state.setEnpassantPosition(toPosition);
		}
		else{
			state.setEnpassantPosition(null);
		}
		if(promo==null){
			state.setPiece(fromPosition, null);
			state.setPiece(toPosition, fromPiece);
		}
		if(promo!=null){
			state.setPiece(fromPosition, null);
			state.setPiece(toPosition, new Piece(turn,promo));	
		}
		if(turn==Color.WHITE){
			state.setTurn(Color.BLACK);
		}
		else{
			state.setTurn(Color.WHITE);
		}
		
		
		/*
		 * Check if king is under check,(after making all the changes to state)
		 * If yes, then backtrack.	
		 */
		Position kingPosition=getKingPosition(state,turn);
		if(checkPositionForCheck(kingPosition,turn,state)){
			state=copy.copy();//trackback
			throw new IllegalMove();
		}
		
		//Check for End of game.
		if(state.getGameResult()==null)
			checkEndOfGame(state);
			
	}
	
	/**
	 * Checks if the move made is valid.
	 * And the game has not already ended.
	 * @param state
	 * @param fromPiece
	 * @param toPiece
	 * @return True if move is a capture or False if not.
	 * @throws IllegalMove
	 */
	private boolean checkTurn(State state, Piece fromPiece, Piece toPiece) throws IllegalMove{
		if (state.getGameResult() != null) {
		      // Game already ended!
		      throw new IllegalMove();
		    }
		
		if(fromPiece==null){
			throw new IllegalMove();
		}
		Color turn= state.getTurn();
		Color fromTurn= fromPiece.getColor();
		
		if(turn!=fromTurn){
			throw new IllegalMove();
		}
		if(toPiece!=null){
			Color toTurn= toPiece.getColor();
			if(toTurn==fromTurn){
				throw new IllegalMove();
			}
			else{
				return true;
			}
		}
		return false;
	}
	

	/**
	 * Checks if the capture move captures a rook at its starting position.
	 * If yes it sets the appropriate canCastle variables false.
	 * @param state
	 * @param move
	 * @param turn
	 */
	private void checkRookCapture(State state, Move move, Color turn) {
		
		Position toPosition= move.getTo();
		Piece p=state.getPiece(toPosition);
		if(p!=null && p.getKind().equals(PieceKind.ROOK) && !(p.getColor().equals(turn))){
			int toRow=toPosition.getRow();
			int toCol=toPosition.getCol();
			
			if(turn.equals(Color.WHITE)){
				if(toRow==7&&toCol==7)
					state.setCanCastleKingSide(Color.BLACK, false);
				if(toRow==7&&toCol==0)
					state.setCanCastleQueenSide(Color.BLACK, false);
			}
			if(turn.equals(Color.BLACK)){
				if(toRow==0&&toCol==7)
					state.setCanCastleKingSide(Color.WHITE, false);
				if(toRow==0&&toCol==0)
					state.setCanCastleQueenSide(Color.WHITE, false);
			}
		}
	}

	/**
	 * Returns the Position of the King for the turn requested.
	 * @param state
	 * @param turn
	 * @return
	 */
	private Position getKingPosition(State state,Color turn) {
		for(int i=0;i<=7;i++){
			for(int j=0;j<=7;j++){
				Piece p=state.getPiece(new Position(i,j));
				if(p!=null&& p.getColor().equals(turn)&&p.getKind().equals(PieceKind.KING))
					return new Position(i,j);
			}
		}
		//KING NOT FOUND! 
		throw new IllegalMove();
	}

	/**
	 * Checks if the move is an EnPassantCapture Move.
	 * @param state 
	 * @param fromPiece
	 * @param toPiece
	 * @param move
	 * @return true is EnPassant, false if not EnPassant.
	 */
	private boolean checkIsEnPassantCaptureMove(State state, Piece fromPiece,
			Piece toPiece, Move move) {
		Position fromPosition= move.getFrom();
		Position toPosition= move.getTo();
		int fromCol=fromPosition.getCol();
		int fromRow=fromPosition.getRow();
		int toCol=toPosition.getCol();
		int toRow=toPosition.getRow();
		Position en=state.getEnpassantPosition();
		Color turn=state.getTurn();
		if(state.getPiece(fromPosition).getKind().equals(PieceKind.PAWN) && en!=null){
			if(turn==Color.BLACK && fromRow==3 && toRow==2){
				if(Math.abs(fromCol-toCol)==1){
					if(en.getCol()==toCol && en.getRow()==3){
						return true;
					}
				}
			}
			if(turn==Color.WHITE && fromRow==4 && toRow==5){
				if(Math.abs(fromCol-toCol)==1){
					if(en.getCol()==toCol && en.getRow()==4){
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Makes sure the boundaries are maintained
	 * @param position
	 */
	private void checkBoundaries(Position position) {
		if(position.getCol()>7||position.getCol()<0||position.getRow()>7||position.getRow()<0)
		 throw new IllegalMove();
	}

	/**
	 * Checks if any move that is being made is without any obstruction.
	 * There are 3 types of movements- verical, horizontal and diagonal.. 
	 * @param state
	 * @param fromPosition
	 * @param toPosition
	 */
	private void checkClearPath(State state, Position fromPosition,
			Position toPosition) {
		int fromCol=fromPosition.getCol();
		int fromRow=fromPosition.getRow();
		int toCol=toPosition.getCol();
		int toRow=toPosition.getRow();
		if(toRow>7||toRow<0||toCol<0||toCol>7){
			throw new IllegalMove();
		}
		if(fromCol!=toCol&&fromRow!=toRow){
			//DIAGONALS.
			int i=fromRow;
			int j=fromCol;
			int rowInc=0;
			int colInc=0;
			if(fromCol<toCol){
				//Forward(down to up)
				colInc=1;
			}
			else{
				colInc=-1;
			}
			if(fromRow<toRow){
				//Left
				rowInc=1;
				}
			else{
				//Right
				rowInc=-1;
			}
			i+=rowInc;
			j+=colInc;
			while(i!=toRow){
				if(state.getPiece(new Position(i,j))!=null){
					//obstruction
					throw new IllegalMove();
				}
				i+=rowInc;
				j+=colInc;
			}
			if(j!=toCol){
				//Not on diagonal.
				throw new IllegalMove();
			}
		}
		
		if(fromCol==toCol&&fromRow!=toRow){
			//Vertical!
			int rowInc=0;
			if(fromRow<toRow){
				rowInc=1;
			}
			else{
				rowInc=-1;
			}
			int i=fromRow+rowInc;
			while(i!=toRow){
				if(state.getPiece(new Position(i,fromCol))!=null){
					//obstruction
					throw new IllegalMove();
				}
				i+=rowInc;
			}
		}
		
		if(fromCol!=toCol&&fromRow==toRow){
			//Horizontal!
			int colInc=0;
			if(fromCol<toCol){
				colInc=1;
			}
			else{
				colInc=-1;
			}
			int j=fromCol+colInc;
			while(j!=toCol){
				if(state.getPiece(new Position(fromRow,j))!=null){
					//obstruction
					throw new IllegalMove();
				}
				j+=colInc;
			}
		}
		
	}
	
	/**
	 * Checks the Rooks movement is valid.
	 * @param state
	 * @param move
	 * @param isCaptureMove
	 */
	private void checkRookMovement(State state, Move move, boolean isCaptureMove) {
		Position fromPosition= move.getFrom();
		Position toPosition= move.getTo();
		Color turn = state.getTurn();
		int fromCol=fromPosition.getCol();
		int fromRow=fromPosition.getRow();
		int toCol=toPosition.getCol();
		int toRow=toPosition.getRow();
		if(fromCol!=toCol&&fromRow!=toRow){
			throw new IllegalMove();
		}
		//updating canCastle flags..
		checkClearPath(state, fromPosition, toPosition);
		if(fromCol==0 && fromRow==0 && turn == Color.WHITE && state.isCanCastleQueenSide(turn)){
			state.setCanCastleQueenSide(turn, false);
		}
		if(fromCol==7 && fromRow==0 && turn == Color.WHITE && state.isCanCastleKingSide(turn)){
			state.setCanCastleKingSide(turn, false);
		}
		if(fromCol==0 && fromRow==7 && turn == Color.BLACK && state.isCanCastleQueenSide(turn)){
			state.setCanCastleQueenSide(turn, false);
		}
		if(fromCol==7 && fromRow==7 && turn == Color.BLACK && state.isCanCastleKingSide(turn)){
			state.setCanCastleKingSide(turn, false);
		}
	}

	
	/**
	 * Checks queen movements..
	 * @param state
	 * @param move
	 * @param isCaptureMove
	 */
	private void checkQueenMovement(State state, Move move,
			boolean isCaptureMove) {
		Position fromPosition= move.getFrom();
		Position toPosition= move.getTo();
		
		checkClearPath(state, fromPosition, toPosition);
		
	}
	
	/**
	 * Checks pawn movements!
	 * @param state
	 * @param move
	 * @param isCaptureMove
	 * @param isEnPassantMove
	 * @param promo 
	 * @return true if the Enpassant position has to be updated.
	 */
	private boolean checkPawnMovement(State state, Move move, boolean isCaptureMove,
			boolean isEnPassantMove, PieceKind promo) {
		Position fromPosition= move.getFrom();
		Position toPosition= move.getTo();
		Color turn=state.getTurn();
		int fromCol=fromPosition.getCol();
		int fromRow=fromPosition.getRow();
		int toCol=toPosition.getCol();
		int toRow=toPosition.getRow();
		
		if(promo!=null){
			if(turn == Color.BLACK){
				if(toRow!=0)
					throw new IllegalMove();
			}
			else{
				if(toRow!=7)
					throw new IllegalMove();
			}
			if(promo.equals(PieceKind.KING)||promo.equals(PieceKind.PAWN))
				throw new IllegalMove();
		}
		
		if(!isCaptureMove && fromCol!=toCol && !isEnPassantMove){
			throw new IllegalMove();
		}
		if(isCaptureMove && (Math.abs(fromCol-toCol)!=1 || Math.abs(fromRow-toRow)!=1)){
			throw new IllegalMove();
		}
		
		if(turn==Color.WHITE && fromRow>=toRow ){
			throw new IllegalMove();
		}
		
		if(turn==Color.BLACK && fromRow<=toRow ){
			throw new IllegalMove();
		}
		
		
		if(isEnPassantMove)
		{
			if(!isCaptureMove){
				state.setPiece(new Position(fromRow,toCol), null);
			}
		}
		
		if(Math.abs(fromRow-toRow)==2 && turn == Color.BLACK ){
			if(fromRow!=6 || fromCol!=toCol)
				throw new IllegalMove();
			if(state.getPiece(new Position(5,fromCol))!=null||
					state.getPiece(new Position(4,fromCol))!=null){
				throw new IllegalMove();
			}
			return true;
		}
		if(Math.abs(fromRow-toRow)==2 && turn == Color.WHITE ){
			if(fromRow!=1 || fromCol!=toCol)
				throw new IllegalMove();
			if(state.getPiece(new Position(2,fromCol))!=null||
					state.getPiece(new Position(3,fromCol))!=null){
				throw new IllegalMove();
			}
			return true;
		}
		

		if(Math.abs(fromRow-toRow)!=1){
			throw new IllegalMove();
		}
			
		return false;
	}

	/**
	 * Checks knight movements
	 * @param state
	 * @param move
	 * @param isCaptureMove
	 */
	private void checkKnightMovement(State state, Move move,
			boolean isCaptureMove) {
		Position fromPosition= move.getFrom();
		Position toPosition= move.getTo();
		int fromCol=fromPosition.getCol();
		int fromRow=fromPosition.getRow();
		int toCol=toPosition.getCol();
		int toRow=toPosition.getRow();
		if(Math.abs(fromCol-toCol)+Math.abs(fromRow-toRow)!=3)
			throw new IllegalMove();
		if(fromCol==toCol||fromRow==toRow)
			throw new IllegalMove();
	}

	/**
	 * Checks king's movements
	 * @param state
	 * @param move
	 * @param isCaptureMove
	 */
	private void checkKingMovement(State state, Move move, boolean isCaptureMove) {
		Position fromPosition= move.getFrom();
		Position toPosition= move.getTo();
		Color turn=state.getTurn();
		int fromCol=fromPosition.getCol();
		int fromRow=fromPosition.getRow();
		int toCol=toPosition.getCol();
		int toRow=toPosition.getRow();
		boolean isCastling=false;
		if(Math.abs(fromRow-toRow)>1){
			throw new IllegalMove();
		}
		
		if(Math.abs(fromCol-toCol)==2){
			if(turn==Color.BLACK && (fromRow!=toRow || fromRow!=7))
				throw new IllegalMove();
			else if(turn==Color.WHITE && (fromRow!=toRow || fromRow!=0))
				throw new IllegalMove();
			else
				isCastling=checkCastling(state,move,turn);
		}
		
		if(!isCastling && (Math.abs(fromCol-toCol)>1||Math.abs(fromRow-toRow)>1)){
			throw new IllegalMove();
		}
		
		checkCheck(toPosition,turn,state);
		state.setCanCastleKingSide(turn, false);
		state.setCanCastleQueenSide(turn, false);
		
	}

	/**
	 * Checks if the position is under attack from any direction
	 * Throws an IllegalMove Exception if the position is under check.
	 * @param position The position on the board under question
	 */
	private void checkCheck(Position position, Color turn,State state) {
		
		int col=position.getCol();
		int row=position.getRow();
		boolean right=checkStraight(row,col,turn,0,1,state);
		boolean left=checkStraight(row,col,turn,0,-1,state);
		boolean up=checkStraight(row,col,turn,1,0,state);
		boolean down=checkStraight(row,col,turn,-1,0,state);
		boolean diagUpRight=checkDiag(row,col,turn,1,1,state);
		boolean diagDownRight=checkDiag(row,col,turn,1,-1,state);
		boolean diagUpLeft=checkDiag(row,col,turn,-1,1,state);
		boolean diagDownLeft=checkDiag(row,col,turn,-1,-1,state);
		boolean knight=checkKnight(row,col,state,turn);
		boolean pawn=checkPawn(row,col,state,turn);
		boolean straight=up||down||left||right;
		boolean diag=diagUpRight||diagDownRight||diagUpLeft||diagDownLeft;
		if(straight||diag||knight||pawn){
			throw new IllegalMove();
		}

	}
	
	/**
	 * Checks if the position is under attack from any direction
	 * @param position The position on the board under question
	 * @return true if under attack, false if not under attack.
	 */
	public boolean checkPositionForCheck(Position position, Color turn,State state) {
		
		int col=position.getCol();
		int row=position.getRow();
		boolean right=checkStraight(row,col,turn,0,1,state);
		boolean left=checkStraight(row,col,turn,0,-1,state);
		boolean up=checkStraight(row,col,turn,1,0,state);
		boolean down=checkStraight(row,col,turn,-1,0,state);
		boolean diagUpRight=checkDiag(row,col,turn,1,1,state);
		boolean diagDownRight=checkDiag(row,col,turn,1,-1,state);
		boolean diagUpLeft=checkDiag(row,col,turn,-1,1,state);
		boolean diagDownLeft=checkDiag(row,col,turn,-1,-1,state);
		boolean knight=checkKnight(row,col,state,turn);
		boolean pawn=checkPawn(row,col,state,turn);
		boolean straight=up||down||left||right;
		boolean diag=diagUpRight||diagDownRight||diagUpLeft||diagDownLeft;
		if(straight||diag||knight||pawn){
			return true;
		}
		return false;
	}

	

	private boolean checkPawn(int row, int col, State state, Color turn) {
		int r=1;
		if(turn.equals(Color.BLACK)){
			r=-1;
				
		}
		else{
			r=1;
		}
		r+=row;
		int c=col+1;
		if(checkWithinBoundaries(r)&&checkWithinBoundaries(c)){
			Piece p=state.getPiece(new Position(r,c));
			if(p!=null && !(p.getColor().equals(turn)) && (p.getKind().equals(PieceKind.PAWN)))
				return true;
		}
		c=col-1;
		if(checkWithinBoundaries(r)&&checkWithinBoundaries(c)){
			Piece p=state.getPiece(new Position(r,c));
			if(p!=null && !(p.getColor().equals(turn)) && (p.getKind().equals(PieceKind.PAWN)))
				return true;
		}
		return false;
	}

	/**
	 * Checks if the position is under check from any Knight
	 * @param row
	 * @param col
	 * @param state
	 * @param turn
	 * @return true if under attack, false if not under attack.
	 */
	private boolean checkKnight(int row, int col, State state,Color turn) {
		boolean test = checkForAttackingKnight(row+2,col+1,turn,state) || 
				checkForAttackingKnight(row+2,col-1,turn,state) ||
				checkForAttackingKnight(row+1,col+2,turn,state) ||
				checkForAttackingKnight(row+1,col-2,turn,state) ||
				checkForAttackingKnight(row-2,col+1,turn,state) ||
				checkForAttackingKnight(row-2,col-1,turn,state) ||
				checkForAttackingKnight(row-1,col+2,turn,state) ||
				checkForAttackingKnight(row-1,col-2,turn,state) ;
		return test;
	}
	
	/**
	 * Helper method for checkKnight
	 * @param r
	 * @param c
	 * @param turn
	 * @param state
	 * @return
	 */
	private boolean checkForAttackingKnight(int r, int c, Color turn, State state){
		if(checkWithinBoundaries(r)&&checkWithinBoundaries(c)){
			Piece p=state.getPiece(new Position(r,c));
			if(p!=null && !(p.getColor().equals(turn)) && (p.getKind().equals(PieceKind.KNIGHT)))
				return true;
		}
		return false;
	}
	
	/**
	 * check if x is within 0 and 7(inclusive).
	 * @param x
	 * @return
	 */
	private boolean checkWithinBoundaries(int x){
		if(x>=0&&x<=7)
			return true;
		else
			return false;
	}

	/**
	 * Help method for checkCheck
	 * Checks if there is any attack from the diagonals.
	 * @param row
	 * @param col
	 * @param turn
	 * @param i
	 * @param j
	 * @param state
	 * @return
	 */
	private boolean checkDiag(int row, int col, Color turn, int i, int j, State state) {
		int r=row+i;
		int c=col+j;
		int n=0;
		while(r>=0&&r<=7&&c>=0&&c<=7){
			Piece p=state.getPiece(new Position(r,c));
			if(p!=null){
				if(p.getColor().equals(turn)){
					return false;
				}
				else{
					if(n==0 && p.getKind().equals(PieceKind.KING)){
						return true;
					}
					if(p.getKind().equals(PieceKind.BISHOP)||p.getKind().equals(PieceKind.QUEEN))
						return true;
					//If the piece encountered is some other piece.
					return false;
				}
			}
			
			r+=i;
			c+=j;
			n++;
		}
		
		return false;
	}

	/**
	 * Helper method for checkCheck
	 * Checks if there is any attack from Horizontal or Vertical Lines.
	 * @param row
	 * @param col
	 * @param turn
	 * @param i
	 * @param j
	 * @param state
	 * @return
	 */
	private boolean checkStraight(int row, int col, Color turn, int i, int j, State state) {
		int r=row+i;
		int c=col+j;
		int n=0;
		while(r>=0&&r<=7&&c>=0&&c<=7){
			Piece p=state.getPiece(new Position(r,c));
			if(p!=null){
				if(p.getColor().equals(turn)){
					return false;
				}
				else{
					if(n==0 && p.getKind().equals(PieceKind.KING)){
						return true;
					}
					if(p.getKind().equals(PieceKind.ROOK)||p.getKind().equals(PieceKind.QUEEN))
						return true;
					//If the piece encountered is some other piece.
					return false;
				}
			}

			r+=i;
			c+=j;
			n++;
		}
		//Clear till the end of board!
		return false;
	}

	

	/**
	 * validates a castling move.
	 * @param state
	 * @param move
	 * @param turn
	 * @return
	 */
	private boolean checkCastling(State state, Move move, Color turn) {
		
		Position fromPosition= move.getFrom();
		Position toPosition= move.getTo();
		int fromCol=fromPosition.getCol();
		int fromRow=fromPosition.getRow();
		int toCol=toPosition.getCol();
		int toRow=toPosition.getRow();
		int row=0;
		if(turn==Color.BLACK){
			row=7;
		}
		else{
			row=0;
		}
		if(fromRow!=row||toRow!=row||fromCol!=4||(toCol!=6&&toCol!=2))
			throw new IllegalMove();
		
		if(toCol==6){
			if(state.getPiece(new Position(row,5))!=null || 
					state.getPiece(new Position(row,6))!=null)
				throw new IllegalMove();
			
			if(!state.isCanCastleKingSide(turn))
				throw new IllegalMove();
		}
		
		if(toCol==2){
			if(state.getPiece(new Position(row,3))!=null || 
					state.getPiece(new Position(row,2))!=null ||
					state.getPiece(new Position(row,1))!=null)
				throw new IllegalMove();
			
			if(!state.isCanCastleQueenSide(turn))
				throw new IllegalMove();
		}
		checkCheck(new Position(row,fromCol),turn,state);
		checkCheck(new Position(row,toCol),turn,state);
		checkCheck(new Position(row,(fromCol+toCol)/2),turn,state);
		//IF castling is possible, update rook position..
		
		if(toCol==6){
			state.setPiece(new Position(row,7),null);
			state.setPiece(new Position(row,5),new Piece(turn,PieceKind.ROOK));			
		}
		if(toCol==2){
			state.setPiece(new Position(row,0),null);
			state.setPiece(new Position(row,3),new Piece(turn,PieceKind.ROOK));				
		}
		return true;
		
	}


	/**
	 * validates Bishops movement
	 * @param state
	 * @param move
	 * @param isCaptureMove
	 */
	private void checkBishopMovement(State state, Move move,
			boolean isCaptureMove) {
		Position fromPosition= move.getFrom();
		Position toPosition= move.getTo();
		
		int fromCol=fromPosition.getCol();
		int fromRow=fromPosition.getRow();
		int toCol=toPosition.getCol();
		int toRow=toPosition.getRow();
		if(fromCol==toCol||fromRow==toRow){
			throw new IllegalMove();
		}
		checkClearPath(state, fromPosition, toPosition);
	}

	
	
	/**
	 * Tests if there is any move left and sets the gameResult parameter of the state accordingly.
	 * It tests if there are any valid moves possible. If no valid moves are possible(for the 
	 * current turn), then the game has ended. If the king of the turn is under check it is a 
	 * checkMate else a staleMate.
	 * @param state
	 */
	private void checkEndOfGame(State state) {
		// TODO Auto-generated method stub
		State copy=state.copy();
		Color turn = copy.getTurn();
		Color other= turn.equals(Color.WHITE)?Color.BLACK:Color.WHITE;
		ArrayList<Position> pieces= new ArrayList<Position>();
		for(int i=0;i<=7;i++){
			for(int j=0;j<=7;j++){
				Piece p=copy.getPiece(new Position(i,j));
				if(p!=null && p.getColor().equals(turn)){
					pieces.add(new Position(i,j));
				}
			}
		}
		for(Position p:pieces){
			if(testValidMove(p,copy)){
				return;
			}
		}
		Position king=getKingPosition(copy,turn);
		if(checkPositionForCheck(king,turn,copy)){
			state.setGameResult(new GameResult(other,
					GameResultReason.CHECKMATE));
		}
		else{
			state.setGameResult(new GameResult(null,
					GameResultReason.STALEMATE));
		}
	}

	/**
	 * Tests if there is any valid move from position p
	 * @param p Position to be checked
	 * @param copy State for which valid moves are to be tested
	 * @return true if there is a valid move, else false
	 */
	private boolean testValidMove(Position p, State copy) {
		int col=p.getCol();
		int row=p.getRow();
		List<Position> position=new ArrayList<Position>();
		Piece piece=copy.getPiece(p);
		Color color=piece.getColor();
		switch(piece.getKind()){	
		case BISHOP:
			for(int i=1;i<8;i++){
				if(checkWithinBoundaries(row+i)&&checkWithinBoundaries(col+i))
				position.add(new Position(row+i,col+i));
				if(checkWithinBoundaries(row+i)&&checkWithinBoundaries(col-i))
				position.add(new Position(row+i,col-i));
				if(checkWithinBoundaries(row-i)&&checkWithinBoundaries(col+i))
				position.add(new Position(row-i,col+i));
				if(checkWithinBoundaries(row-i)&&checkWithinBoundaries(col-i))
				position.add(new Position(row-i,col-i));
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
			if (checkWithinBoundaries(row - 1 )
					&& checkWithinBoundaries(col ))
				position.add(new Position(row - 1, col));
			if (checkWithinBoundaries(row)
					&& checkWithinBoundaries(col + 1))
				position.add(new Position(row , col + 1));
			if (checkWithinBoundaries(row)
					&& checkWithinBoundaries(col - 1))
				position.add(new Position(row , col - 1));
			if (checkWithinBoundaries(row)
					&& checkWithinBoundaries(col - 2))
				position.add(new Position(row , col - 2));
			if (checkWithinBoundaries(row)
						&& checkWithinBoundaries(col + 2))
					position.add(new Position(row , col + 2));
			
			break;
		case KNIGHT:
			int [][]k={{2,1},{2,-1},{1,2},{1,-2},{-2,1},{-2,-1},{-1,2},{-1,-2}};
			for(int i=0;i<8;i++){
				if (checkWithinBoundaries(row+k[i][0])
						&& checkWithinBoundaries(col+ k[i][1]))
					position.add(new Position(row+k[i][0] , col + k[i][1]));	
			}
			break;
		case PAWN:
			int pawnInc=(color.equals(Color.WHITE))?1:-1;
			if(checkWithinBoundaries(row+pawnInc)){
			position.add(new Position(row+pawnInc,col));
				if(checkWithinBoundaries(col+1))
					position.add(new Position(row+pawnInc,col+1));
				if(checkWithinBoundaries(col-1))
					position.add(new Position(row+pawnInc,col-1));
				if(checkWithinBoundaries(row+(2*pawnInc)))
					position.add(new Position(row+(2*pawnInc),col));
			}
			break;
		case QUEEN:
			for(int i=1;i<8;i++){
				if(checkWithinBoundaries(row+i)&&checkWithinBoundaries(col+i))
				position.add(new Position(row+i,col+i));
				if(checkWithinBoundaries(row+i)&&checkWithinBoundaries(col-i))
				position.add(new Position(row+i,col-i));
				if(checkWithinBoundaries(row-i)&&checkWithinBoundaries(col+i))
				position.add(new Position(row-i,col+i));
				if(checkWithinBoundaries(row-i)&&checkWithinBoundaries(col-i))
				position.add(new Position(row-i,col-i));

				if(checkWithinBoundaries(row+i))
				position.add(new Position(row+i,col));
				if(checkWithinBoundaries(col-i))
				position.add(new Position(row,col-i));
				if(checkWithinBoundaries(col+i))
				position.add(new Position(row,col+i));
				if(checkWithinBoundaries(row-i))
				position.add(new Position(row-i,col));
			}
			
			break;
		case ROOK:
			for(int i=1;i<8;i++){
				if(checkWithinBoundaries(row+i))
				position.add(new Position(row+i,col));
				if(checkWithinBoundaries(col-i))
				position.add(new Position(row,col-i));
				if(checkWithinBoundaries(col+i))
				position.add(new Position(row,col+i));
				if(checkWithinBoundaries(row-i))
				position.add(new Position(row-i,col));
			}
			
			break;
		default:
			break;
			
		}
		for(Position pos:position)
		{
			Move move=new Move(p,pos,null);
				boolean valid=true;
				try{
					State copy2=copy.copy();
					tryMove(copy2,move);
				}
				catch(Exception e){
					valid=false;
				}
				if(valid){
					return true;
				}
		}
		return false;
	}

	/**
	 * Try move is same as the makeMove method, except it doesn't check for the end of game.
	 * This method is called by the checkEndOfGame method.
	 * @param state
	 * @param move
	 * @throws IllegalMove
	 */
	public void tryMove(State state, Move move) throws IllegalMove {
		State copy=state.copy();
		Position fromPosition= move.getFrom();
		Position toPosition= move.getTo();
		checkBoundaries(fromPosition);
		checkBoundaries(toPosition);
		boolean wasEnPassantMove=false;
		Color turn=state.getTurn();
		Piece fromPiece= state.getPiece(fromPosition);
		Piece toPiece= state.getPiece(toPosition);
		PieceKind promo=move.getPromoteToPiece();
		boolean isCaptureMove=checkTurn(state,fromPiece,toPiece);
		boolean isEnPassantMove= checkIsEnPassantCaptureMove(state,fromPiece,toPiece,move);
		
		if(promo!=null  && !(fromPiece.getKind().equals(PieceKind.PAWN)))
			throw new IllegalMove();
		if(fromPosition.equals(toPosition))
			throw new IllegalMove();
		
		switch(fromPiece.getKind()){
			case BISHOP:
				checkBishopMovement(state, move, isCaptureMove);
				break;
			case KING:
				checkKingMovement(state, move, isCaptureMove);
				break;
			case KNIGHT:
				checkKnightMovement(state, move, isCaptureMove);
				break;
			case PAWN:
				wasEnPassantMove=checkPawnMovement(state, move, isCaptureMove,isEnPassantMove,promo);
				state.setNumberOfMovesWithoutCaptureNorPawnMoved(0);
				break;
			case QUEEN:
				checkQueenMovement(state, move, isCaptureMove);
				break;
			case ROOK:
				checkRookMovement(state, move, isCaptureMove);
				break;
			default:
				throw new IllegalMove();				
		}
		
		
		/*
		 * All Tests pass till here, therefore time to make the changes to the state..
		 */
		if(isCaptureMove){
			checkRookCapture(state,move,turn);
			state.setNumberOfMovesWithoutCaptureNorPawnMoved(0);
		}
		else{
			if(state.getPiece(fromPosition).getKind().equals(PieceKind.PAWN)){
				state.setNumberOfMovesWithoutCaptureNorPawnMoved(0);
			}
			else{
				int num=state.getNumberOfMovesWithoutCaptureNorPawnMoved();
				state.setNumberOfMovesWithoutCaptureNorPawnMoved(num+1);
				if(num+1==100){
					state.setGameResult(new GameResult(null,
							GameResultReason.FIFTY_MOVE_RULE));
				}
			}
		}
		if(wasEnPassantMove){
			state.setEnpassantPosition(toPosition);
		}
		else{
			state.setEnpassantPosition(null);
		}
		if(promo==null){
			state.setPiece(fromPosition, null);
			state.setPiece(toPosition, fromPiece);
		}
		if(promo!=null){
			state.setPiece(fromPosition, null);
			state.setPiece(toPosition, new Piece(turn,promo));	
		}
		if(turn==Color.WHITE){
			state.setTurn(Color.BLACK);
		}
		else{
			state.setTurn(Color.WHITE);
		}
		
		
		
		Position kingPosition=getKingPosition(state,turn);
		if(checkPositionForCheck(kingPosition,turn,state)){
			state=copy.copy();//trackback
			throw new IllegalMove();
		}
	
	}
	
}
