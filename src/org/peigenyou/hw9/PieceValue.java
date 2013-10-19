package org.peigenyou.hw9;

import org.shared.chess.*;

public class PieceValue {
	private static short[] PawnTable = new short[]
			{
  		     0,  0,  0,  0,  0,  0,  0,  0,
  		    50, 50, 50, 50, 50, 50, 50, 50,
  		    10, 10, 20, 30, 30, 20, 10, 10,
  		     5,  5, 10, 27, 27, 10,  5,  5,
  		     0,  0,  0, 25, 25,  0,  0,  0,
  		     5, -5,-10,  0,  0,-10, -5,  5,
  		     5, 10, 10,-25,-25, 10, 10,  5,
  		     0,  0,  0,  0,  0,  0,  0,  0
			};
	private static short[] KnightTable = new short[]
			{
			    -50,-40,-30,-30,-30,-30,-40,-50,
			    -40,-20,  0,  0,  0,  0,-20,-40,
			    -30,  0, 10, 15, 15, 10,  0,-30,
			    -30,  5, 15, 20, 20, 15,  5,-30,
			    -30,  0, 15, 20, 20, 15,  0,-30,
			    -30,  5, 10, 15, 15, 10,  5,-30,
			    -40,-20,  0,  5,  5,  0,-20,-40,
			    -50,-40,-20,-30,-30,-20,-40,-50,
			};
	private static short[] BishopTable = new short[]
			{
			    -20,-10,-10,-10,-10,-10,-10,-20,
			    -10,  0,  0,  0,  0,  0,  0,-10,
			    -10,  0,  5, 10, 10,  5,  0,-10,
			    -10,  5,  5, 10, 10,  5,  5,-10,
			    -10,  0, 10, 10, 10, 10,  0,-10,
			    -10, 10, 10, 10, 10, 10, 10,-10,
			    -10,  5,  0,  0,  0,  0,  5,-10,
			    -20,-10,-40,-10,-10,-40,-10,-20,
			};
	private static short[] KingTable = new short[]
			{
			  -30, -40, -40, -50, -50, -40, -40, -30,
			  -30, -40, -40, -50, -50, -40, -40, -30,
			  -30, -40, -40, -50, -50, -40, -40, -30,
			  -30, -40, -40, -50, -50, -40, -40, -30,
			  -20, -30, -30, -40, -40, -30, -30, -20,
			  -10, -20, -20, -20, -20, -20, -20, -10, 
			   20,  20,   0,   0,   0,   0,  20,  20,
			   20,  30,  10,   0,   0,  10,  30,  20
			};
	private static short[] KingTableEndGame = new short[]
			{
			    -50,-40,-30,-20,-20,-30,-40,-50,
			    -30,-20,-10,  0,  0,-10,-20,-30,
			    -30,-10, 20, 30, 30, 20,-10,-30,
			    -30,-10, 30, 40, 40, 30,-10,-30,
			    -30,-10, 30, 40, 40, 30,-10,-30,
			    -30,-10, 20, 30, 30, 20,-10,-30,
			    -30,-30,  0,  0,  0,  0,-30,-30,
			    -50,-30,-30,-30,-30,-30,-30,-50
			};
	public static int getPieceValue(Piece piece,int row,int col){
		int adjustment=0;
		if(piece==null) return 0;
		switch (piece.getKind())
    {
        case PAWN:
            {
                adjustment=PawnTable[(7-row)*8+col]; 
            }
        case KNIGHT:
            {
            	adjustment=KnightTable[(7-row)*8+col];
            }
        case BISHOP:
            {
            	adjustment=BishopTable[(7-row)*8+col];
            }
        case KING:
            {
            	adjustment=KingTable[(7-row)*8+col];
            }
        default:
            {
                adjustment= 0;
            }
    }
		int pieceValue=getPieceValue(piece)+adjustment;
		pieceValue=piece.getColor()==Color.WHITE?pieceValue:-pieceValue;
		return pieceValue;
	}
	public static int getPieceValue(Piece piece)
	{
		if(piece==null) return 0;
	    switch (piece.getKind())
	    {
	        case PAWN:return 100;
	        case KNIGHT:return 320;
	        case BISHOP:return 325;
	        case ROOK:return 500;
	        case QUEEN:return 975;
	        case KING:return 32767;
	        default:return 0;
	    }
	}
}
