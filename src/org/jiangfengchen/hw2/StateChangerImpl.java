package org.jiangfengchen.hw2;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.shared.chess.*;


public class StateChangerImpl implements StateChanger{
	 public void makeMove(State state, Move move) throws IllegalMove {

		    if (state.getGameResult() != null) {
		      // Game already ended!
		      throw new IllegalMove();
		    }
		    Position from = move.getFrom();
		    Piece piece = state.getPiece(from);

		    if (piece == null) {
		      // Nothing to move!
		      throw new IllegalMove();
		    }
		    Color color = piece.getColor();
		    if (color != state.getTurn()) {
		      // Wrong player moves!
		      throw new IllegalMove();
		    }
		    
		    if (isPromotion(move)){
		    	// Impossible promote move
				 if(color==Color.WHITE&&move.getTo().getRow()!=7) throw new IllegalMove();
				 if(color==Color.BLACK&&move.getTo().getRow()!=0) throw new IllegalMove();
			 }
		    
		    if (isPromotion(move) && (move.getPromoteToPiece()==PieceKind.PAWN ||move.getPromoteToPiece()==PieceKind.KING)){
		    	throw new IllegalMove();  // Impossible promote 
		    }
		    
		    if (isPromotion(move) && piece.getKind()!=PieceKind.PAWN) { // Impossible piecekind to promote
		      throw new IllegalMove();
		    }
		    if (!isLegalMove(move,state)) {		//Illegal Move
		      throw new IllegalMove();
		    }
		    else {
		    	 Position to = move.getTo();
		    	 Piece get = state.getPiece(to);
		    	 if(isCastle(move,state)){ //Deal with castle
					 if(to.getCol()==6){						 
						 state.setPiece(from, null);
						 state.setPiece(from.getRow(),7, null);
						 state.setPiece(to, piece);
						 state.setPiece(from.getRow(), 5, new Piece(color,PieceKind.ROOK));					 							
						 
				      }
				 
					 if(to.getCol()==2){					
						 state.setPiece(from, null);
						 state.setPiece(from.getRow(),0, null);
						 state.setPiece(to, piece);
						 state.setPiece(from.getRow(), 3, new Piece(color,PieceKind.ROOK));							
						 
					  }
						 state.setEnpassantPosition(null);
					     state.setTurn(color==Color.BLACK?Color.WHITE:Color.BLACK);
				}
		    
		    	 
		    	if(piece.getKind().equals(PieceKind.PAWN)){  
		    		if(isEnpassant(move,state)){  //Deal with Enpassant
		    			state.setPiece(from, null);
		    			state.setPiece(state.getEnpassantPosition(), null);
		    			state.setPiece(to, piece);
		    			state.setEnpassantPosition(null);
		    			state.setNumberOfMovesWithoutCaptureNorPawnMoved(0);
		    			state.setTurn(color==Color.BLACK?Color.WHITE:Color.BLACK);		    			
		    		}
		    		else{
		    			state.setPiece(from, null);
		    			if(isPromotion(move)) state.setPiece(to,new Piece(color, move.getPromoteToPiece()));
		    			else state.setPiece(to, piece);
		    			state.setNumberOfMovesWithoutCaptureNorPawnMoved(0);
		    			if(Math.abs(to.getRow()-from.getRow())==2) state.setEnpassantPosition(to); //Move to Enpassant Position
		    			else state.setEnpassantPosition(null); 
		    			state.setTurn(color==Color.BLACK?Color.WHITE:Color.BLACK);
		    			if(get!=null&&get.getKind()==PieceKind.ROOK){  //If eat a rook, set flags
			    			if (to.equals(new Position(0,0))) state.setCanCastleQueenSide(Color.WHITE, false);
			    			if (to.equals(new Position(0,7))) state.setCanCastleKingSide(Color.WHITE, false);
			    			if (to.equals(new Position(7,0))) state.setCanCastleQueenSide(Color.BLACK, false);
			    			if (to.equals(new Position(7,7))) state.setCanCastleKingSide(Color.BLACK, false);
			    		}
		    		}
		    	}
		    	
		    	if(!piece.getKind().equals(PieceKind.PAWN)){
		    		if(isCapture(move,state))state.setNumberOfMovesWithoutCaptureNorPawnMoved(0);
		    		else state.setNumberOfMovesWithoutCaptureNorPawnMoved(state.getNumberOfMovesWithoutCaptureNorPawnMoved()+1);
		    		state.setPiece(from, null);
		    		state.setPiece(to, piece);
		    		state.setEnpassantPosition(null);
		    		state.setTurn(color==Color.BLACK?Color.WHITE:Color.BLACK);
		    		if(get!=null&&get.getKind()==PieceKind.ROOK){ //If eat a rook, set flags
		    			if (to.equals(new Position(0,0))) state.setCanCastleQueenSide(Color.WHITE, false);
		    			if (to.equals(new Position(0,7))) state.setCanCastleKingSide(Color.WHITE, false);
		    			if (to.equals(new Position(7,0))) state.setCanCastleQueenSide(Color.BLACK, false);
		    			if (to.equals(new Position(7,7))) state.setCanCastleKingSide(Color.BLACK, false);
		    		}
		    	}
		   
		    	
		    	if(piece.getKind().equals(PieceKind.ROOK)){ //If rook moves, set flags
		    		if(from.getCol()==7&&from.getRow()==0&&color==Color.WHITE) state.setCanCastleKingSide(color, false);
		    		if(from.getCol()==7&&from.getRow()==7&&color==Color.BLACK) state.setCanCastleKingSide(color, false);
		    		if(from.getCol()==0&&from.getRow()==0&&color==Color.WHITE) state.setCanCastleQueenSide(color, false);
		    		if(from.getCol()==0&&from.getRow()==7&&color==Color.BLACK) state.setCanCastleQueenSide(color, false);
		    	}
		    	
		    	if(piece.getKind().equals(PieceKind.KING)){ //If king moves, set flags
		    		state.setCanCastleKingSide(color, false);
		    		state.setCanCastleQueenSide(color, false);
		    	}
		    	
		    	
		    	if(isColorStalemated(state.getTurn(),state)) state.setGameResult(new GameResult(null, GameResultReason.STALEMATE));	    	
		    	else if(state.getNumberOfMovesWithoutCaptureNorPawnMoved()>=100) state.setGameResult(new GameResult(null, GameResultReason.FIFTY_MOVE_RULE));
		    	else if(isColorCheckmated(state.getTurn(),state)) state.setGameResult(new GameResult(color, GameResultReason.CHECKMATE));
		    	
		    	
					 
			}
		        
	}
		  
	 
	 //Check if Position is on board
	 
	 private boolean isonboard(int row, int col) {
		 return (row >=0 && row <=7 && col >=0 && col <=7);
	 }
	 
	 //Get the Set of Positions a piece can reach (not necessarily legal)
	 
	 private Set<Position> getMoveRange(Position from,State state){
		    Piece pc = state.getPiece(from);
		    PieceKind type = pc.getKind();
		    Color color = pc.getColor();
	        Set<Position> result = new HashSet<Position>();
	        int row = from.getRow();
	        int col = from.getCol();
	        
	        if (type == PieceKind.PAWN){
	        	if (color == Color.WHITE) {
	        		if (row == 1){
	        			result.add(new Position(3,col));
	        		}
	        		if (row != 7){
	        			result.add(new Position(row+1 ,col));
	        		}
	        		
	        		if (col == 0){
	        			result.add(new Position(row+1 ,col+1));
	        		}
	        		
	        		if (col == 7){
	        			result.add(new Position(row+1 ,col-1));
	        		}
	        		
	        		if (col!=0 && col!=7){
	        			result.add(new Position(row+1 ,col-1));
	        			result.add(new Position(row+1 ,col+1));
	        		}
	        	}
	        	
	        	if (color == Color.BLACK) {
	        		if (row == 6) {
	        			result.add(new Position(4,col));
	        		}
	        		if (row != 0) {
	        			result.add(new Position(row-1 ,col));
	        		}
	        		
	        		if (col == 0) {
	        			result.add(new Position(row-1 ,col+1));
	        		}
	        		
	        		if (col == 7) {
	        			result.add(new Position(row-1 ,col-1));
	        		}
	        		
	        		if (col!=0 && col!=7){
	        			result.add(new Position(row-1 ,col-1));
	        			result.add(new Position(row-1 ,col+1));
	        		}	        			        		
	        	}

	        }
	        
	        if (type == PieceKind.BISHOP){
	        	int x = 0;
	        	int y1,y2;
	        	while ( 0 <= x && x <= 7){
	        		y1 = x-row+col;
	        		y2 = row+col-x;
	        		
	        		if(0 <= y1 && y1 <= 7){
	        			result.add(new Position(x,y1));
	        		}
	        		
	        		if(0 <= y2 && y2 <= 7){
	        			result.add(new Position(x,y2));
	        		}
	        		x++;	        		
	        	}
	        }
	        
	        if (type == PieceKind.KNIGHT){
	        	if(isonboard(row+1,col+2)) result.add(new Position(row+1,col+2));
	        	if(isonboard(row+1,col-2)) result.add(new Position(row+1,col-2));
	        	if(isonboard(row+2,col+1)) result.add(new Position(row+2,col+1));
	        	if(isonboard(row+2,col-1)) result.add(new Position(row+2,col-1));
	        	if(isonboard(row-1,col+2)) result.add(new Position(row-1,col+2));
	        	if(isonboard(row-1,col-2)) result.add(new Position(row-1,col-2));
	        	if(isonboard(row-2,col+1)) result.add(new Position(row-2,col+1));
	        	if(isonboard(row-2,col-1)) result.add(new Position(row-2,col-1));	        	
	        }
	        
	        if (type == PieceKind.KING){
	        	if(isonboard(row+1,col+1)) result.add(new Position(row+1,col+1));
	        	if(isonboard(row+1,col-1)) result.add(new Position(row+1,col-1));
	        	if(isonboard(row+1,col))   result.add(new Position(row+1,col));
	        	if(isonboard(row,col-1))   result.add(new Position(row,col-1));
	        	if(isonboard(row,col+1))   result.add(new Position(row,col+1));
	        	if(isonboard(row-1,col))   result.add(new Position(row-1,col));
	        	if(isonboard(row-1,col+1)) result.add(new Position(row-1,col+1));
	        	if(isonboard(row-1,col-1)) result.add(new Position(row-1,col-1));
	        	
	        
	        	if (color == Color.WHITE && row == 0 && col == 4){
	        		
	        		if(state.isCanCastleKingSide(Color.WHITE))result.add(new Position(0,6));
	        		if(state.isCanCastleQueenSide(Color.WHITE))result.add(new Position(0,2));
	        	}
	        	
	        	if (color == Color.BLACK && row == 7 && col == 4){
	        		if(state.isCanCastleKingSide(Color.BLACK))result.add(new Position(7,6));
	        		if(state.isCanCastleQueenSide(Color.BLACK))result.add(new Position(7,2));
	        	}
	    
	        }
	        
	        if (type == PieceKind.ROOK){
	        	int x=0;
	        	int y=0;
	        	while ( 0 <= x && x <= 7){
	        		result.add(new Position(x,col));
	        	    x++;
	        	}
	            while ( 0 <= y && y <= 7){
	                result.add(new Position(row,y));
	                y++;	             
	            }	        
            }
	        
	        if (type == PieceKind.QUEEN){
	        	int x = 0;
	        	int y1,y2;
	        	while ( 0 <= x && x <= 7){
	        		y1 = x-row+col;
	        		y2 = row+col-x;
	        		
	        		if(0 <= y1 && y1 <= 7){
	        			result.add(new Position(x,y1));
	        		}
	        		
	        		if(0 <= y2 && y2 <= 7){
	        			result.add(new Position(x,y2));
	        		}
	        		x++;	        		
	        	}
	        	x = 0;
	        	y1 = 0;
	        	while ( 0 <= x && x <= 7){
	        		result.add(new Position(x,col));
	        	    x++;
	        	}
	            while ( 0 <= y1 && y1 <= 7){
	                result.add(new Position(row,y1));
	                y1++;	             
	            }	
	        	
	        	
	        }	   

	        return result;	 
	 }
	 
	 /*
	  * 
	  * Get the King position of a certain color
	  * 
	  * */
	 private Position getKingPosition(State state,Color color){ 
		 Piece pc;
		 for (int x=0; x<=7; x++){
			 for (int y=0; y<=7; y++){
				 pc = state.getPiece(x,y);
				 if (pc!=null&&pc.getKind() == PieceKind.KING && pc.getColor() == color) return new Position(x,y);
			 }
		 }
		 return null;
	 }
	 
	 /*
	  * 
	  * Get all pieces of a color on the board
	  * 
	  * */
	 
	 public Set<Position> getPieces(State state,Color color){
		 Set<Position> result =  new HashSet<Position>();
		 for(int x=0; x <= 7; x++){
			 for(int y=0; y <= 7; y++){
				 Piece p =state.getPiece(x,y);
				 if(p!=null && p.getColor()==color) result.add(new Position(x,y));
			 }
		 }		 		 		 
		 return result;
	 }
	 
	 /*
	  * 
	  * check if a move is within the move range of a piece(Not necessarily legal)
	  * 
	  * */
	 public boolean isReachableMove(State state, Move move){
		 Position from = move.getFrom();
		 Piece fpiece = state.getPiece(from);
		 Color fcolor = fpiece.getColor();
		 PieceKind fkind = fpiece.getKind();
		 Position to = move.getTo();
		 //same color piece occupied
		 if (state.getPiece(to)!=null && state.getPiece(to).getColor()== fcolor ) return false;
		 //castling
		 if (fkind == PieceKind.KING&& (Math.abs(from.getCol()-to.getCol()) > 1) && (to.getCol() == 2 ||to.getCol() == 6) ) {
			 if (from.getCol()!=4||from.getRow()!=to.getRow()) return false;
             
			 if (fcolor == Color.WHITE && to.getCol() == 6 && from.getCol() == 4) {
				 if (state.getPiece(0,5)==null && state.getPiece(to)==null && state.isCanCastleKingSide(Color.WHITE)) {
					 return true;
				 }
				 return false;
			 }
			 if (fcolor == Color.WHITE && to.getCol() == 2 && from.getCol() == 4) {
				 if (state.getPiece(0,1)==null&&state.getPiece(0, 3)==null && state.getPiece(to)==null && state.isCanCastleQueenSide(Color.WHITE)) {
					 return true;
				 }
				 return false;
			 }
			 if (fcolor == Color.BLACK && to.getCol() == 6 && from.getCol() == 4) {
				 if (state.getPiece(7,5)==null && state.getPiece(to)==null && state.isCanCastleKingSide(Color.BLACK)) {
					 return true;
				 }
				 return false;
			 }
			 if (fcolor == Color.BLACK && to.getCol() == 2 && from.getCol() == 4) {
				 if (state.getPiece(7,1)==null&&state.getPiece(7, 3)==null && state.getPiece(to)==null && state.isCanCastleQueenSide(Color.BLACK)) {
					 return true;
				 }
				 return false;
			 }
			 
			 return false;
		 }
		 
		 		 

		 
		 Set<Position> range = getMoveRange(from,state);
		

		 
		 if (fkind == PieceKind.KNIGHT){
			 for (Position m : range){
				 if(to.getRow() == m.getRow() && to.getCol() == m.getCol()) return true;
			 }
			 return false;
		 }
		 
		 if (fkind == PieceKind.BISHOP){
			 for (Position m : range){
				 if(to.getRow() == m.getRow() && to.getCol() == m.getCol()){
					 Set<Position> bt = getPostionBetween(from,to);
					 if(bt.size()==0) return true;
					 for (Position n : bt){
						 if(state.getPiece(n)!=null) return false;
					 }
					 return true;
				 }				 
			 }
			 return false;
		 }
		 
		 if (fkind == PieceKind.ROOK){
			 for (Position m : range){
				 if(to.getRow() == m.getRow() && to.getCol() == m.getCol()){
					 Set<Position> bt = getPostionBetween(from,to);
					 if(bt == null) return true;
					 for (Position n : bt){
						 if(state.getPiece(n)!=null) return false;
					 }
					 return true;
				 }
			 }
			 return false;
		 }
		 
		 if (fkind == PieceKind.PAWN){
			 
			 for (Position m : range){
				 if(to.getRow() == m.getRow() && to.getCol() == m.getCol()){
				     if(Math.abs(from.getRow()-to.getRow())==2){
				    	 if(state.getPiece(new Position((from.getRow()+to.getRow())/2,from.getCol()))!=null) return false;
				     }
					 if(to.getCol() == from.getCol()){	
						 if(state.getPiece(to)==null) return true;
						 else return false;
					 }
					 
				 
				     if(Math.abs(to.getCol() - from.getCol())==1){
				    	 Position enpassant = state.getEnpassantPosition();
				    	 if (enpassant!=null){
				    		 if (Math.abs(enpassant.getCol()-from.getCol())==1 && enpassant.getRow()==from.getRow() &&enpassant.getCol()==to.getCol()) {
				    			 if (to.getCol() == enpassant.getCol()&&Math.abs(to.getRow()-from.getRow())==1) return true;
				    		 } else {
				    			 if (state.getPiece(to)==null) return false;
				    			 if (state.getPiece(to)!=null&&state.getPiece(to).getColor()!=fcolor) return true;
				    		 }
				    	 }
				    	 if (enpassant==null&&state.getPiece(to)==null) return false;
				    	 if (enpassant==null&&state.getPiece(to)!=null&&state.getPiece(to).getColor()!=fcolor) return true;
				    	 return false;
					 
				    }
				}
			 }
			 return false;
		 }
		 
		 
		 if (fkind == PieceKind.QUEEN){
			 for (Position m : range){
				 if(to.getRow() == m.getRow() && to.getCol() == m.getCol()){
					 Set<Position> bt = getPostionBetween(from,to);
					 if(bt == null) return true;
					 for (Position n : bt){
						 if(state.getPiece(n)!=null) return false;
					 }
					 return true;					 
				 }
			 }
			 return false;
		 }
		 
		 if (fkind == PieceKind.KING){
			 for (Position m : range){
				 
				 if(to.getRow() == m.getRow() && to.getCol() == m.getCol()){
					 return true;
				 }
			 }
			 return false;
		 }
		 
		 return false;
		 
	 }
	 
	 public Set<Position> getReachablePosition(Position from,State state){ //get Move range of a piece from a position
		 Piece piece = state.getPiece(from);
	
		 Set<Position> result = new HashSet<Position>();
		 Set<Position> range = getMoveRange(from,state); 
		 for (Position m : range){
			 Move mv = new Move(from,m,null);
			 if(isReachableMove(state,mv)) result.add(m);

		 }	
		 return result;
	 }
	 
	 private boolean isColorChecked(State state,Color defend){ //check if a color's king is under attack
		 Position Kingposition= getKingPosition(state,defend);
		 if (Kingposition == null) return false;
		 Color attack;
		 if (defend == Color.WHITE) attack = Color.BLACK;
		 else attack = Color.WHITE;
		 Set<Position> attackpieces = getPieces(state,attack);
		 for (Position ap : attackpieces){
			 Set<Position> targets = getReachablePosition(ap,state);
			 for (Position target : targets){
				 if (Kingposition.getCol() == target.getCol() && Kingposition.getRow() == target.getRow()) return true;
			 }
		 }
		 return false;				 
	 }
	 
	 public Set<Position> getLegalMoves(Position from,State state){ //get legal position a piece can move to
		 Set<Position> result = new HashSet<Position>();
		 Set<Position> reachs = getReachablePosition(from,state);
		 for (Position r : reachs) {
			 Move m = new Move(from,r,null);
			 if(isCastle(m,state)){
				 Move m2 = new Move(from,new Position(from.getRow(),(from.getCol()+r.getCol())/2),null);
				 if(!DoesMoveGetChecked(m2,state)&&!DoesMoveGetChecked(m,state)&&!isColorChecked(state,state.getPiece(from).getColor())) result.add(r);
			 }
			 else if(!DoesMoveGetChecked(m,state)) result.add(r);
		 }
		 return result;
	       
	 }
	 
	 private boolean DoesMoveGetChecked(Move move,State state){ //check if a wthin range move is legal
		 Position from = move.getFrom();
		 Position to = move.getTo();		 
		 State expected = state.copy();
		 Piece pfrom = state.getPiece(from);
		 Color color = pfrom.getColor();
		 Position ept = state.getEnpassantPosition();
		 if(pfrom.getKind()==PieceKind.KING && Math.abs(to.getCol()-from.getCol())==2){
			 if(color==Color.WHITE && from.getCol()==4){
				 if(to.getCol()==6&&to.getRow()==0) {
					 expected.setPiece(from, null);
					 expected.setPiece(new Position(0,7),null);
					 expected.setPiece(to, new Piece(color,PieceKind.KING));
					 expected.setPiece(new Position(0,5), new Piece(color,PieceKind.ROOK));
					 boolean ck =  isColorChecked(expected,color);
					 return ck;					 
				 }
				 if(to.getCol()==2&&to.getRow()==0) {
					 expected.setPiece(from, null);
					 expected.setPiece(new Position(0,0),null);
					 expected.setPiece(to, new Piece(color,PieceKind.KING));
					 expected.setPiece(new Position(0,3), new Piece(color,PieceKind.ROOK));
					 boolean ck =  isColorChecked(expected,color);
					 return ck;					 
				 }
			 }
			 if(color==Color.BLACK && from.getCol()==4){
				 if(to.getCol()==6&&to.getRow()==7) {
					 expected.setPiece(from, null);
					 expected.setPiece(new Position(7,7),null);
					 expected.setPiece(to, new Piece(color,PieceKind.KING));
					 expected.setPiece(new Position(7,5), new Piece(color,PieceKind.ROOK));
					 boolean ck =  isColorChecked(expected,color);
					 return ck;					 
				 }
				 if(to.getCol()==2&&to.getRow()==7) {
					 expected.setPiece(from, null);
					 expected.setPiece(new Position(7,0),null);
					 expected.setPiece(to, new Piece(color,PieceKind.KING));
					 expected.setPiece(new Position(7,3), new Piece(color,PieceKind.ROOK));
					 boolean ck =  isColorChecked(expected,color);
					 return ck;					 
				 }
			 }
		 }
		 if(pfrom.getKind()==PieceKind.PAWN && ept!=null && from.getRow()==ept.getRow() && to.getCol()==ept.getCol()){
			 expected.setPiece(from, null);
			 expected.setPiece(ept, null);
			 expected.setEnpassantPosition(null);
			 expected.setPiece(to, pfrom);
			 boolean ck =  isColorChecked(expected,color);
			 return ck;	
		 }
		 expected.setPiece(from,null);
		 expected.setPiece(to, pfrom);
		 boolean ck =  isColorChecked(expected,color);
		 return ck;			 		 		 
	 }
	/* get all position of pieces betweetn two positions
	 * can be vertical,Horizontal
	 * or diagonal
	 * 
	 */
	 private Set<Position> getPostionBetween(Position from,Position to){  
		 Set<Position> result = new HashSet<Position>();
		 if (to.getCol()-from.getCol() == to.getRow()-from.getRow()){
			 if (to.getCol() > from.getCol()) {
				 int y = from.getCol()+1;
				 int x;
				 while ( y > from.getCol() && y < to.getCol()){
					 x = from.getRow() + y - from.getCol();
					 result.add(new Position(x,y));
					 y++;
				 }
			 }
			 
			 if (to.getCol() < from.getCol()) {
				 int y = to.getCol()+1;
				 int x;
				 while ( y > to.getCol() && y < from.getCol()){
					 x = from.getRow() + y - from.getCol();
					 result.add(new Position(x,y));
					 y++;
				 }
			 }
		 }
		 
		 if (from.getCol()-to.getCol() == to.getRow()-from.getRow()){
			 if (to.getCol() > from.getCol()) {
				 int y = from.getCol()+1;
				 int x;
				 while ( y > from.getCol() && y < to.getCol()){
					 x = from.getRow() - y + from.getCol();
					 result.add(new Position(x,y));
					 y++;
				 }
			 }
			 
			 if (to.getCol() < from.getCol()) {
				 int y = to.getCol()+1;
				 int x;
				 while ( y > to.getCol() && y < from.getCol()){
					 x = from.getRow() - y + from.getCol();
					 result.add(new Position(x,y));
					 y++;
				 }
			 }
			 
		 }
		 
		 if (from.getCol() == to.getCol()){
			 if (from.getRow() > to.getRow()){
				 for(int x = to.getRow()+1 ; x < from.getRow(); x++) result.add(new Position(x,from.getCol()));
			 }
			 if (from.getRow() < to.getRow()){
				 for(int x = from.getRow()+1 ; x < to.getRow(); x++) result.add(new Position(x,from.getCol()));
			 }
		 }
		 
		 if (from.getRow() == to.getRow()){
			 if (from.getCol() > to.getCol()){
				 for(int y = to.getCol()+1 ; y < from.getCol(); y++) result.add(new Position(from.getRow(),y));
			 }
			 if (from.getCol() < to.getCol()){
				 for(int y = from.getCol()+1 ; y < to.getCol(); y++) result.add(new Position(from.getRow(),y));
			 }
		 }
		 	 
		 return result;
		 
	 }
	 
	 
	 public boolean isLegalMove(Move move,State state){ //check if a move is legal
		 Position to = move.getTo();
		 Position from = move.getFrom();
		 Set<Position> set = getLegalMoves(from,state);
		 for (Position p : set){
			 if (to.getCol()==p.getCol()&&to.getRow()==p.getRow()) return true;
		 }
		 return false;
	 }
	 
	 
	 public boolean isColorCheckmated(Color color,State state){ //check if a color is checkmated
		 boolean noMoves = true;
		 Set<Move> moves = new HashSet<Move>();
		 Set<Position> pset ;
		 if(isColorChecked(state,color))
		 {
			 for (Position ps : getPieces(state,color)){
				 pset = getLegalMoves(ps,state);
				 for(Position p : pset){
					 Move movetoadd = new Move(ps,p,null);
					 moves.add(movetoadd);
				 }
			 }
			 
			 for (Move m : moves){
				 if(!DoesMoveGetChecked(m,state)) noMoves=false;
			 }
			 if(noMoves) return true;
		 }
		 return false;
	 }
	 
	 
	 public boolean isMoveCheckMate(Move move,State state){ //check if the move checkmate
		 Position from = move.getFrom();
		 Position to = move.getTo();
		 State expected = state.copy();
		 Color attack = state.getPiece(from).getColor();
		 Color defend = (attack==Color.WHITE)? Color.BLACK:Color.WHITE;
		 
		 if(isEnpassant(move,state)){
			 expected.setPiece(from, null);
			 expected.setPiece(expected.getEnpassantPosition(), null);
			 expected.setEnpassantPosition(null);
			 expected.setPiece(to, state.getPiece(from));
			 expected.setTurn(defend);
			 if(isColorCheckmated(defend,expected)) return true;
			 return false;
		 }
		 
		 if(isCastle(move,state)){
			 if(to.getCol()==6){
				 expected.setPiece(from, null);
				 expected.setPiece(from.getRow(),7, null);
				 expected.setPiece(to, state.getPiece(from));
				 expected.setPiece(from.getRow(), 5, new Piece(attack,PieceKind.ROOK));
				 expected.setCanCastleKingSide(attack, false);
				 expected.setCanCastleQueenSide(attack, false);
				 if(isColorCheckmated(defend,expected)) return true;
				 return false;
			 }
			 if(to.getCol()==2){
				 expected.setPiece(from, null);
				 expected.setPiece(from.getRow(),0, null);
				 expected.setPiece(to, state.getPiece(from));
				 expected.setPiece(from.getRow(), 3, new Piece(attack,PieceKind.ROOK));
				 expected.setCanCastleQueenSide(attack, false);
				 expected.setCanCastleKingSide(attack, false);
				 if(isColorCheckmated(defend,expected)) return true;
				 return false;
			 }
			 
		 }
		 
		 expected.setPiece(from, null);
		 expected.setPiece(to, state.getPiece(from));
		 if(isColorCheckmated(defend,expected)) return true;
		 return false;		 
	 }
	 
	 
	 
	 private boolean isEnpassant(Move move, State state){ //check if the move is Enpassant
		 Position en = state.getEnpassantPosition();
		 if (en==null) return false;
		 Position from = move.getFrom();
		 Position to = move.getTo();
		 if (state.getPiece(from)==null) return false;
		 if (state.getPiece(from).getKind()==PieceKind.PAWN&&from.getRow()==en.getRow()&&to.getCol()==en.getCol()) return true;
		 return false;
	 }
	 
	 private boolean isCastle(Move move,State state){ //check if the move is castle
		 Position from = move.getFrom();
		 Position to = move.getTo();
		 Piece p = state.getPiece(from);
		 if(p.getKind()==PieceKind.KING&&Math.abs(to.getCol()-from.getCol())==2) return true;
		 return false;
	 }
	 
	 private boolean isPromotion(Move move){ //check if the move is promote
		 if (move.getPromoteToPiece()!=null) return true;
		 return false;
	 }
	 
	 private boolean isCapture(Move move,State state){  //check if the move is a capture move
		 if(isEnpassant(move,state)) return true;
		 Piece from = state.getPiece(move.getFrom());
		 Piece to = state.getPiece(move.getTo());
		 if(from!=null&&to!=null&&from.getColor()!=to.getColor()) return true;
		 return false;
	 }
	 
	 public boolean isColorStalemated(Color color,State state){ //check if a certain color cannot make any moves
		 boolean noMoves = true;
		 Set<Move> moves = new HashSet<Move>();
		 Set<Position> pset ;
		 if(!isColorChecked(state,color))
		 {
			 Set<Position> gp = getPieces(state,color);
			 if (gp.size()==0) return false;
			 for (Position ps : gp){				
				 pset = getLegalMoves(ps,state);
				 for(Position p : pset){
					 Move movetoadd = new Move(ps,p,null);
					 moves.add(movetoadd);
				 }
			 }
			 
			 for (Move m : moves){
				 if(!DoesMoveGetChecked(m,state)) noMoves=false;
			 }
			 if(noMoves) return true;
		 }
		 return false;
	 }
	 
	
	 
}
