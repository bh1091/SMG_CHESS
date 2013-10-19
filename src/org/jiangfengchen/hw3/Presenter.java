package org.jiangfengchen.hw3;


import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.shared.chess.Color;
import org.shared.chess.GameResult;
import org.shared.chess.GameResultReason;
import org.shared.chess.Move;
import org.shared.chess.Piece;
import org.shared.chess.PieceKind;
import org.shared.chess.Position;
import org.shared.chess.State;
import org.jiangfengchen.hw2_5.StateExplorerImpl;
import org.jiangfengchen.hw2.StateChangerImpl;
import org.jiangfengchen.hw6.client.LoginService;
import org.jiangfengchen.hw6.client.LoginServiceAsync;
import org.jiangfengchen.hw6.client.MyMessages;
import org.jiangfengchen.hw7.Match;
import org.jiangfengchen.hw9.AlphaBetaPruning;
import org.jiangfengchen.hw9.DateTimer;
import org.jiangfengchen.hw9.Heuristic;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.AudioElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.media.client.Audio;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.googlecode.gwtphonegap.client.util.PhonegapUtil;


public class Presenter {
  public interface View {
    /**
     * Renders the piece at this position.
     * If piece is null then the position is empty.
     */
    void setPiece(int row, int col, Piece piece);
    /**
     * Turns the highlighting on or off at this cell.
     * Cells that can be clicked should be highlighted.
     */
    void setHighlighted(int row, int col, boolean highlighted);
    /**
     * Indicate whose turn it is.
     */
    void setWhoseTurn(Color color);
    /**
     * Indicate whether the game is in progress or over.
     */
    void setGameResult(GameResult gameResult);
    
    void setPresenter(Presenter presenter);
    
    void setStart(int row, int col, boolean highlighted);
    
    void setGameStatus(String text);
    
    ImageResource getResource(int i);

  }
  
  private View view;
  private State state;
  private Set<Position> HLPos=new HashSet<Position>();
  private StateExplorerImpl Explorer=new StateExplorerImpl();
  private boolean IsClicked=false;
  private Position from =null;
  private Position ss=null;
  private Set<Position> starts=new HashSet<Position>();
  private StateChangerImpl sc= new StateChangerImpl();
  private String email="";
  private String white="";
  private String black="";
  private long id;
  AlphaBetaPruning abp = new AlphaBetaPruning(new Heuristic());
  LoginServiceAsync loginService = GWT.create(LoginService.class);
  final MyMessages myMessage = GWT.create(MyMessages.class);
  private Storage store=Storage.getLocalStorageIfSupported();
  Presenter(){
	  ((ServiceDefTarget) loginService).setServiceEntryPoint("http://kanppa.appspot.com/jiangfengchen/login");
	  PhonegapUtil.prepareService((ServiceDefTarget) loginService, "http://kanppa.appspot.com/jiangfengchen/", "login");
	  this.state=new State();
	  if(state.getGameResult()==null) starts=Explorer.getPossibleStartPositions(state);
  }
  
  public void setView(View view) {
    this.view = view;
    view.setPresenter(this); 
  }
  
  public void setMail(String mail){
	  this.email=mail;
  }
  
  public void setState(State state) {
	this.state=state;
	if(state.getGameResult()==null) starts=Explorer.getPossibleStartPositions(state);
	this.showState();
  }
  public void MouseOn(int row,int col){
	  if (state.getGameResult()!=null) return;
	  if (IsClicked) return;
	  clearStart();
	  clearHighLights();
	  if (!starts.contains(new Position(7-row,col))) return;
	  Set<Position> possiblepos=getTarget(row,col);
	  view.setStart(row, col, true);
	  ss=new Position(7-row,col);
	  for (Position p:possiblepos){
		  HLPos.add(p);
		  view.setHighlighted(7-p.getRow(), p.getCol(), true);
		  }
  }
  	public boolean Judge(Position from,Position to){
  	    if(state.getTurn()==Color.WHITE&&email!=white) return false;
  	    if(state.getTurn()==Color.BLACK&&email!=black) return false;
  		if (state.getGameResult()!=null) return false;
  		if (!starts.contains(from)) return false;
  		if(!getTarget(7-from.getRow(),from.getCol()).contains(to)) return false;
  		return true;
  	}
  
  public void OnClick(int row,int col){
	  if(state.getTurn()==Color.WHITE&&email!=white) {
		  IsClicked=false;
		  return;
	  }
	  if(state.getTurn()==Color.BLACK&&email!=black) {
		  IsClicked=false;
		  return;
	  }
	  if (state.getGameResult()!=null) {
		  IsClicked=false;
		  return; 
	  }
	 
	  if(!IsClicked){
	  if (!starts.contains(new Position(7-row,col))) return;
	  from = new Position(7-row,col);
	  IsClicked=true;
	  }else{
	  Position to =new Position(7-row,col);
	  if(!getTarget(7-from.getRow(),from.getCol()).contains(to)){
		  IsClicked=false;	
		  clearStart();
		  clearHighLights();  
		  return;
	  }
	  Piece piece=state.getPiece(from);
	  if(piece.getKind()==PieceKind.PAWN&&(row==7||row==0)){
	  MyPopup pop =new MyPopup(from.getRow(),from.getCol(),to.getRow(),to.getCol());
	  pop.center();
	  }else{
	  final State back=state.copy();
	  sc.makeMove(state, new Move(from,new Position(7-row,col),null));
	  if(id==0){
		  
		  Move mv= abp.findBestMove(state, 2, new DateTimer(800));
		  if(mv!=null){
			  sc.makeMove(state, mv);
			  this.showState();
		  }else{
			  this.showState();
			  view.setGameStatus("Congratulation, you win");
		  }
		  
	  }
	  String result="";
      if(state.getGameResult()==null){
    	 
    	  starts=Explorer.getPossibleStartPositions(state);
      }else{
    	  if(state.getGameResult().isDraw()){
    		  result="D";
    	  }else if(state.getGameResult().getWinner()==Color.WHITE){
    		  result="W";
    	  }else {
    		  result="B";
    	  }
      }
	  IsClicked=false;
	  if(id!=0){
	  AsyncCallback<Match> callback = new AsyncCallback<Match>() {
		    public void onSuccess(Match result) {
		     if(result==null) {
		    	 store.removeItem("Move,"+id);	
		    	 Presenter.this.setState(back);
		    	 return;
		     }
		     store.removeItem("Move,"+result.getMatchid());	
		     State st= Presenter.Deserialize(result.getState());
		  	 Presenter.this.setState(st);
		  	 if(st.getGameResult()==null){
		  		 if (result.isWhiteTurn()) Presenter.this.view.setGameStatus(result.getWhite()+myMessage.wturn());
		  		 else Presenter.this.view.setGameStatus(result.getBlack()+myMessage.bturn());
		  	 }
		    }

		    public void onFailure(Throwable caught) {
		    	Presenter.this.setState(back);
			  Window.alert("Lose connection, will resend moves in every 10sec");

	    	 Timer timer = new Timer(){

				@Override
				public void run() {
					 recall();
					
				}	    	 
	    	 };
	    	 timer.schedule(10000);
	    	
		    };
		    
		};
		 if(store!=null){
		    	store.setItem("Move,"+id,Presenter.Seralize(state)+" "+email+" "+id+" "+result);
		 }
		loginService.MakeMove(Presenter.Seralize(state), email, id,result, callback);
		
		
	
//	loginService.MakeMove(Presenter.Seralize(state), email, id,result, callback);
	  }
//	  view.setPiece(7-from.getRow(), from.getCol(), null);
//	  view.setPiece(row, col, piece);
//	  view.setGameResult(state.getGameResult());
//	  view.setWhoseTurn(state.getTurn());
/*	  if(state.getGameResult()!=null){
		 HorizontalPanel hp=new HorizontalPanel();
		 final DialogBox db=new DialogBox();
		 db.setText("Game's over,want another game?");
		 Button yes = new Button("yes");
		 Button no = new Button("no");
		 yes.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				State s =new State();
				db.hide();
				Presenter.this.showState();
			}		 
		 });
		 no.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				db.hide();	
			}
			 
		 });
		 hp.add(yes);
		 hp.add(no);
		 db.setWidget(hp);
		 db.center();
	 }
	  Presenter.this.setState(state);*/
	  }
	  }
  }
  
 
  public void showState(){
	 if(state.getTurn()==Color.WHITE) view.setGameStatus(white+" "+myMessage.wturn());
	 else  if(state.getTurn()==Color.BLACK) view.setGameStatus(black+" "+myMessage.bturn());
	 view.setGameResult(state.getGameResult());
	 clearStart();
	 clearHighLights();
	 for (int r = 0; r < 8; r++) {
	      for (int c = 0; c < 8; c++) {
	        view.setPiece(r, c, state.getPiece(r, c));
	      }
	 }
	
  }
  public Position convert(int row,int col){
	  if(row>7||row<0||col>7||col<0) return null;
	  return new Position(7-row,col);
  }
  
  private void clearHighLights(){
	  
	  for (Position p:HLPos){
		  view.setHighlighted(7-p.getRow(), p.getCol(), false);  
	  }
	  HLPos.clear();
  }
  
  private void clearStart(){
	  if(ss!=null) view.setStart(7-ss.getRow(), ss.getCol(), false);
  }
  
  
  public Set<Position> getTarget(int row,int col){
	  Set<Position> result=new HashSet<Position>();
	  Position from = convert(row,col);
	  Set<Move> possimoves=Explorer.getPossibleMovesFromPosition(state, from);
	  for(Move mv:possimoves){
		  result.add(mv.getTo());
	  }
	  return result;
  }
  private  class MyPopup extends PopupPanel {

	    public MyPopup(final int fx,final int fy,final int tx,final int ty) {
	      
	      super(true);
	      VerticalPanel vp=new VerticalPanel();
	      vp.setSpacing(8);
	      Image queen = new Image();
	      queen.setResource(view.getResource(0));
	      Image bishop = new Image();
	      bishop.setResource(view.getResource(1));
	      Image rook = new Image();
	      rook.setResource(view.getResource(2));
	      Image knight = new Image();
	      knight.setResource(view.getResource(3));
	      Label l =new Label("Promote To?");
	      queen.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				MyPopup.this.hide();
				String result=null;
				final State back=state.copy();
				sc.makeMove(state, new Move(new Position(fx,fy),new Position(tx,ty),PieceKind.QUEEN));
				 if(id==0){
					
					
					 Move mv= abp.findBestMove(state, 2, new DateTimer(800));
					  if(mv!=null){
						  sc.makeMove(state, mv);
						  Presenter.this.showState();
					  }else{
						  Presenter.this.showState();
						  view.setGameStatus("Congratulation, you win");
					  }
					  
				  }
				if(state.getGameResult()==null)starts=Explorer.getPossibleStartPositions(state);
				else{
					 if(state.getGameResult().isDraw()){
			    		  result="D";
			    	  }else if(state.getGameResult().getWinner()==Color.WHITE){
			    		  result="W";
			    	  }else {
			    		  result="B";
			    	  }
				}
				IsClicked=false;
		if (id!=0) {
	
			AsyncCallback<Match> callback = new AsyncCallback<Match>() {
				public void onSuccess(Match result) {
					store.removeItem("Move,"+id);	
					if (result == null) {		
						Presenter.this.setState(back);
						return;
					}
					State st = Presenter.Deserialize(result.getState());
					Presenter.this.setState(st);
					if (st.getGameResult() == null) {
						if (result.isWhiteTurn())
							Presenter.this.view.setGameStatus(result.getWhite()
									+ myMessage.wturn());
						else
							Presenter.this.view.setGameStatus(result.getBlack()
									+ myMessage.bturn());
					}
				}

			    public void onFailure(Throwable caught) {
			    	Presenter.this.setState(back);
						  Window.alert("Lose connection, will resend moves in every 10sec");
						
	
			    	 Timer timer = new Timer(){

						@Override
						public void run() {
							 recall();
							
						}	    	 
			    	 };
			    	 timer.schedule(10000);
			    	
				    };
				    
				};
				 if(store!=null){
				    	store.setItem("Move,"+id,Presenter.Seralize(state)+" "+email+" "+id+" "+result);
				 }
				loginService.MakeMove(Presenter.Seralize(state), email, id,result, callback);
		}
			}
	      });
	      bishop.addClickHandler(new ClickHandler(){
				@Override
				public void onClick(ClickEvent event) {
					MyPopup.this.hide();
					String result=null;
					final State back = state.copy();
					sc.makeMove(state, new Move(new Position(fx,fy),new Position(tx,ty),PieceKind.BISHOP));
					 if(id==0){
					
						 Move mv= abp.findBestMove(state, 2, new DateTimer(800));
						  if(mv!=null){
							  sc.makeMove(state, mv);
							  Presenter.this.showState();
						  }else{
							  Presenter.this.showState();
							  view.setGameStatus("Congratulation, you win");
						  }
						  
					}
					if(state.getGameResult()==null)starts=Explorer.getPossibleStartPositions(state);
					else {
						 if(state.getGameResult().isDraw()){
				    		  result="D";
				    	  }else if(state.getGameResult().getWinner()==Color.WHITE){
				    		  result="W";
				    	  }else {
				    		  result="B";
				    	  }
					}
					IsClicked=false;
	if (id!=0) {
		//				view.setPiece(7-fx, fy, null);
		//				view.setPiece(7-tx, ty, new Piece(state.getTurn()==Color.BLACK?Color.WHITE:Color.BLACK,PieceKind.BISHOP));
		//				view.setGameResult(state.getGameResult());
		//				view.setWhoseTurn(state.getTurn());
		//			Presenter.this.setState(state);
		AsyncCallback<Match> callback = new AsyncCallback<Match>() {
			public void onSuccess(Match result) {
				store.removeItem("Move,"+id);	
				if (result == null) {
					Presenter.this.setState(back);
					return;
				}
				State st = Presenter.Deserialize(result.getState());
				Presenter.this.setState(st);
				if (st.getGameResult() == null) {
					if (result.isWhiteTurn())
						Presenter.this.view.setGameStatus(result.getWhite()
								+ myMessage.wturn());
					else
						Presenter.this.view.setGameStatus(result.getBlack()
								+ myMessage.bturn());
				}
			}

		    public void onFailure(Throwable caught) {
		    	Presenter.this.setState(back);
					  Window.alert("Lose connection, will resend moves in every 10sec");
					
		    	 Timer timer = new Timer(){

					@Override
					public void run() {
						 recall();
						
					}	    	 
		    	 };
		    	 timer.schedule(10000);
		    	
			    };
			    
			};
			 if(store!=null){
			    	store.setItem("Move,"+id,Presenter.Seralize(state)+" "+email+" "+id+" "+result);
			 }
			loginService.MakeMove(Presenter.Seralize(state), email, id,result, callback);
	}
				}
		      });
	      rook.addClickHandler(new ClickHandler(){
				@Override
				public void onClick(ClickEvent event) {
					MyPopup.this.hide();
					String result=null;
					final State back = state.copy();
					sc.makeMove(state, new Move(new Position(fx,fy),new Position(tx,ty),PieceKind.ROOK));
					 if(id==0){
						 
						 Move mv= abp.findBestMove(state, 2, new DateTimer(800));
						  if(mv!=null){
							  sc.makeMove(state, mv);
							  Presenter.this.showState();
						  }else{
							  Presenter.this.showState();
							  view.setGameStatus("Congratulation, you win");
						  }
						  
					}
					if(state.getGameResult()==null)starts=Explorer.getPossibleStartPositions(state);
					else{
						 if(state.getGameResult().isDraw()){
				    		  result="D";
				    	  }else if(state.getGameResult().getWinner()==Color.WHITE){
				    		  result="W";
				    	  }else {
				    		  result="B";
				    	  }
					}
					IsClicked=false;	
	if (id!=0) {
		//				view.setPiece(7-fx, fy, null);
		//				view.setPiece(7-tx, ty, new Piece(state.getTurn()==Color.BLACK?Color.WHITE:Color.BLACK,PieceKind.ROOK));
		//				view.setGameResult(state.getGameResult());
		//			view.setWhoseTurn(state.getTurn());	
		//			Presenter.this.setState(state);
		AsyncCallback<Match> callback = new AsyncCallback<Match>() {
			public void onSuccess(Match result) {
				store.removeItem("Move,"+id);	
				if (result == null) {
					Presenter.this.setState(back);
					return;
				}
				State st = Presenter.Deserialize(result.getState());
				Presenter.this.setState(st);
				if (st.getGameResult() == null) {
					if (result.isWhiteTurn())
						Presenter.this.view.setGameStatus(result.getWhite()
								+ myMessage.wturn());
					else
						Presenter.this.view.setGameStatus(result.getBlack()
								+ myMessage.bturn());
				}
			}

		    public void onFailure(Throwable caught) {
		    	Presenter.this.setState(back);
					  Window.alert("Lose connection, will resend moves in every 10sec");
					
		    	 Timer timer = new Timer(){
		    		 
					@Override
					public void run() {
						 recall();
						
					}	    	 
		    	 };
		    	 timer.schedule(10000);
		    	
			    };
			    
			};
			 if(store!=null){
			    	store.setItem("Move,"+id,Presenter.Seralize(state)+" "+email+" "+id+" "+result);
			 }
			loginService.MakeMove(Presenter.Seralize(state), email, id,result, callback);
	}
				}
		      });
	      knight.addClickHandler(new ClickHandler(){
				@Override
				public void onClick(ClickEvent event) {
					MyPopup.this.hide();
					String result=null;
					final State back = state.copy();
					sc.makeMove(state, new Move(new Position(fx,fy),new Position(tx,ty),PieceKind.KNIGHT));
					if(state.getGameResult()==null)starts=Explorer.getPossibleStartPositions(state);
					 if(id==0){
					
						 Move mv= abp.findBestMove(state, 2, new DateTimer(800));
						  if(mv!=null){
							  sc.makeMove(state, mv);
							  Presenter.this.showState();
						  }else{
							  Presenter.this.showState();
							  view.setGameStatus("Congratulation, you win");
						  }
						  
					}
					else{
						 if(state.getGameResult().isDraw()){
				    		  result="D";
				    	  }else if(state.getGameResult().getWinner()==Color.WHITE){
				    		  result="W";
				    	  }else {
				    		  result="B";
				    	  }
					}
					IsClicked=false;
	if (id!=0) {
		//				view.setPiece(7-fx, fy, null);
		//				view.setPiece(7-tx, ty, new Piece(state.getTurn()==Color.BLACK?Color.WHITE:Color.BLACK,PieceKind.KNIGHT));
		//				view.setGameResult(state.getGameResult());
		//				view.setWhoseTurn(state.getTurn());
		//		Presenter.this.setState(state);
		AsyncCallback<Match> callback = new AsyncCallback<Match>() {
			public void onSuccess(Match result) {
				store.removeItem("Move,"+id);	
				if (result == null) {
					Presenter.this.setState(back);
					return;
				}
				State st = Presenter.Deserialize(result.getState());
				Presenter.this.setState(st);
				if (st.getGameResult() == null) {
					if (result.isWhiteTurn())
						Presenter.this.view.setGameStatus(result.getWhite()
								+ myMessage.wturn());
					else
						Presenter.this.view.setGameStatus(result.getBlack()
								+ myMessage.bturn());
				}
			}

		    public void onFailure(Throwable caught) {
		    	Presenter.this.setState(back);
					  Window.alert("Lose connection, will resend moves in every 10sec");
					
		    	 Timer timer = new Timer(){

					@Override
					public void run() {
						 recall();
						
					}	    	 
		    	 };
		    	 timer.schedule(10000);
		    	
			    };
			    
			};
			 if(store!=null){
			    	store.setItem("Move,"+id,Presenter.Seralize(state)+" "+email+" "+id+" "+result);
			 }
			loginService.MakeMove(Presenter.Seralize(state), email, id,result, callback);
	}
				}
		      });
	      vp.add(l);
	      vp.add(queen);
	      vp.add(bishop);
	      vp.add(rook);
	      vp.add(knight);
	      this.setGlassEnabled(true);
	      this.setAnimationEnabled(true);
	      this.setWidget(vp);

	    }
	  }
  


public static String Seralize(State st) {

	StringBuilder s = new StringBuilder();
	int mv = st.getNumberOfMovesWithoutCaptureNorPawnMoved();
	s.append(st.getTurn().isBlack()?"B":"W");
	s.append(st.isCanCastleKingSide(Color.BLACK)?"t":"f");
	s.append(st.isCanCastleKingSide(Color.WHITE)?"t":"f");
	s.append(st.isCanCastleQueenSide(Color.BLACK)?"t":"f");
	s.append(st.isCanCastleQueenSide(Color.WHITE)?"t":"f");	
	if(st.getEnpassantPosition()==null){
		s.append("$");
		s.append("$");
	}
	else{
		s.append(String.valueOf(st.getEnpassantPosition().getRow()));
		s.append(String.valueOf(st.getEnpassantPosition().getCol()));
	}
	
	if(st.getGameResult()!=null){
		if(st.getGameResult().getWinner().isBlack()){
			s.append("B");
		}else if(st.getGameResult().getWinner().isWhite()){
			s.append("W");
		}else{
			s.append("D");
		}
		
		if(st.getGameResult().getGameResultReason()==(GameResultReason.CHECKMATE)){
			s.append("c");
		}else if(st.getGameResult().getGameResultReason()==(GameResultReason.FIFTY_MOVE_RULE)){
			s.append("f");
		}else if(st.getGameResult().getGameResultReason()==(GameResultReason.STALEMATE)){
			s.append("s");
		}else if(st.getGameResult().getGameResultReason()==GameResultReason.THREEFOLD_REPETITION_RULE){
			s.append("t");
		}
	}else{
		s.append("$");
		s.append("$");
	}

		
	for(int i = 0 ; i < 8 ; i++){
		for(int j = 0 ; j < 8 ; j++){
			if(st.getPiece(i, j)==null){
				s.append("$");
			}
			else{
				if(st.getPiece(i, j).getColor().isBlack()){
					switch(st.getPiece(i, j).getKind()){
					case PAWN:{
						s.append("p");
						break;
					}
					case ROOK:{
						s.append("r");
						break;
					}
					case KNIGHT:{
						s.append("n");
						break;
					}
					case BISHOP:{
						s.append("b");
						break;
					}
					case QUEEN:{
						s.append("q");
						break;
					}
					case KING:{
						s.append("k");
						break;
					}
					}
				}
				else{
					switch(st.getPiece(i, j).getKind()){
					case PAWN:{
						s.append("P");
						break;
					}
					case ROOK:{
						s.append("R");
						break;
					}
					case KNIGHT:{
						s.append("N");
						break;
					}
					case BISHOP:{
						s.append("B");
						break;
					}
					case QUEEN:{
						s.append("Q");
						break;
					}
					case KING:{
						s.append("K");
						break;
					}
					}
				}
			}
		}
	}
	s.append(String.valueOf(mv));
	
	return s.toString();
}

public static State Deserialize(String str){
	State result = new State();
	int count=0;
	char k=str.charAt(count);
	if(k=='B') result.setTurn(Color.BLACK);
	else result.setTurn(Color.WHITE);
	count++;
	k=str.charAt(count);
	if(k=='t') result.setCanCastleKingSide(Color.BLACK, true);
	else result.setCanCastleKingSide(Color.BLACK, false);
	count++;
	k=str.charAt(count);
	if(k=='t') result.setCanCastleKingSide(Color.WHITE, true);
	else result.setCanCastleKingSide(Color.WHITE, false);
	count++;
	k=str.charAt(count);
	if(k=='t') result.setCanCastleQueenSide(Color.BLACK, true);
	else result.setCanCastleQueenSide(Color.BLACK, false);
	count++;
	k=str.charAt(count);
	if(k=='t') result.setCanCastleQueenSide(Color.WHITE, true);
	else result.setCanCastleQueenSide(Color.WHITE, false);
	count++;
	k=str.charAt(count);
	if(k!='$'){
		int row=Integer.parseInt(String.valueOf(k));
		count++;
		k=str.charAt(count);
		int col=Integer.parseInt(String.valueOf(k));
		Position en=new Position(row,col);
		result.setEnpassantPosition(en);
		count++;
		k=str.charAt(count);
	}else{
		count++;
		count++;
		k=str.charAt(count);
	}
	if(k!='$'){	
	    if(k=='B') {
			result.setGameResult(new GameResult(Color.BLACK,GameResultReason.CHECKMATE));
			count+=2;
			k=str.charAt(count);
		}
		else if(k=='W') {
			result.setGameResult(new GameResult(Color.WHITE,GameResultReason.CHECKMATE));
			count+=2;
			k=str.charAt(count);
		}else{
			count++;
			k=str.charAt(count);
			if(k=='f')result.setGameResult(new GameResult(null,GameResultReason.FIFTY_MOVE_RULE));
			if(k=='s')result.setGameResult(new GameResult(null,GameResultReason.STALEMATE));
			if(k=='t')result.setGameResult(new GameResult(null,GameResultReason.THREEFOLD_REPETITION_RULE));
			count++;
			k=str.charAt(count);
		}
	}else{
		count++;
		count++;
		k=str.charAt(count);
	}
	
	for(int i=0;i<8;i++){
		for(int j=0;j<8;j++){
			switch(k){
			case 'p':{
				result.setPiece(i, j, new Piece(Color.BLACK, PieceKind.PAWN));	
				break;
			}
			case 'P':{
				result.setPiece(i, j, new Piece(Color.WHITE, PieceKind.PAWN));
				break;
			}
			case 'r':{
				result.setPiece(i, j, new Piece(Color.BLACK, PieceKind.ROOK));
				break;
			}
			case 'R':{
				result.setPiece(i, j, new Piece(Color.WHITE, PieceKind.ROOK));
				break;
			}
			case 'n':{
				result.setPiece(i, j, new Piece(Color.BLACK, PieceKind.KNIGHT));
				break;
			}
			case 'N':{
				result.setPiece(i, j, new Piece(Color.WHITE, PieceKind.KNIGHT));
				break;
			}
			case 'b':{
				result.setPiece(i, j, new Piece(Color.BLACK, PieceKind.BISHOP));
				break;
			}
			case 'B':{
				result.setPiece(i, j, new Piece(Color.WHITE, PieceKind.BISHOP));
				break;
			}
			case 'q':{
				result.setPiece(i, j, new Piece(Color.BLACK, PieceKind.QUEEN));
				break;
			}
			case 'Q':{
				result.setPiece(i, j, new Piece(Color.WHITE, PieceKind.QUEEN));
				break;
			}
			case 'k':{
				result.setPiece(i, j, new Piece(Color.BLACK, PieceKind.KING));
				break;
			}
			case 'K':{
				result.setPiece(i, j, new Piece(Color.WHITE, PieceKind.KING));
				break;
			}
			default:{
				result.setPiece(i, j, null);
				break;
			}
		
			}
			count++;
			k=str.charAt(count);		
		}
	}
	
	String mvs= str.substring(count);
	int mv = Integer.valueOf(mvs);
	result.setNumberOfMovesWithoutCaptureNorPawnMoved(mv);
	return result;
	
}

public Audio createClick() {
	 Audio audio = Audio.createIfSupported();
	 if (audio == null) {
	      return null;
	 }
	 audio.addSource("http://kanppa.appspot.com/jiangfengchen_audio/Click.mp3", AudioElement.TYPE_MP3);
	 audio.addSource("http://kanppa.appspot.com/jiangfengchen_audio/Click.wav", AudioElement.TYPE_WAV);
	 // Show audio controls.
	 audio.setControls(false);
	 return audio;
}

public Audio createDrag() {
	 Audio audio = Audio.createIfSupported();
	 if (audio == null) {
	      return null;
	 }
	 audio.addSource("http://kanppa.appspot.com/jiangfengchen_audio/Jump.mp3", AudioElement.TYPE_MP3);
	 audio.addSource("http://kanppa.appspot.com/jiangfengchen_audio/Jump.wav", AudioElement.TYPE_WAV);
	 // Show audio controls.
	 audio.setControls(false);
	 return audio;
}

public State getState(){
	return this.state;
}


public void DoAnimation(Image img,int a,int b,int x,int y,Image source){
	if(IsClicked) {
		ImageAnimation ia =new ImageAnimation(img,a,b,x,y,source);
		ia.run(1000);
	}
}

public boolean isClicked(){
	return IsClicked;
}
public boolean containStart(Position p){
	return ss.equals(p);
}
public boolean containTo(Position p){
	return HLPos.contains(p);
}

public class ImageAnimation extends Animation{
	private  Image wg;
	private  Image source;
	PopupPanel pp = new PopupPanel();
	private int startX;
	private int startY;
	private int finalX;
	private int finalY;
	Piece piece ;
	int rowI;
	int colI;
	
	public ImageAnimation(Image wg,int sx,int sy,int fx,int fy,Image ogn){
		VerticalPanel vp = new VerticalPanel();
		this.wg=wg;
		vp.add(wg);
	    this.pp.setWidget(vp);
		this.startX=sx;
		this.startY=sy;
		pp.setPopupPosition(startX, startY);
		pp.show();
		this.finalX=fx;
		this.finalY=fy;
		source=ogn;
		source.setVisible(false);
	}
	
	@Override
	protected void onUpdate(double progress) {
	
		double positionX = startX + (progress*(this.finalX-this.startX));
		double positionY = startY + (progress*(this.finalY-this.startY));
		pp.setPopupPosition((int)positionX, (int)positionY);

	}
	@Override
	protected void onComplete(){
		super.onComplete();
		pp.hide();
		source.setVisible(true);
		
	}
	

}
public void recall(){
	LinkedList<String> remainder=GetMove();	
	 for(int i=0;i<remainder.size();i++){
		 final int count =i;
		 final String[] info = remainder.get(i).split(" ");
		 final State back = state.copy();
		 AsyncCallback<Match> callback = new AsyncCallback<Match>() {
			    public void onSuccess(Match result) {	
				     if(result==null) {
				    	 Presenter.this.setState(back);
				    	 store.removeItem("Move,"+info[2]);	
				    	 return;
				     }
				     store.removeItem("Move,"+result.getMatchid());	
				     State st= Presenter.Deserialize(result.getState());
				  	 Presenter.this.setState(st);
				  	 if(st.getGameResult()==null){
				  		 if (result.isWhiteTurn()) Presenter.this.view.setGameStatus(result.getWhite()+myMessage.wturn());
				  		 else Presenter.this.view.setGameStatus(result.getBlack()+myMessage.bturn());
				  	 }
				    }

				    public void onFailure(Throwable caught) {
				    	 Timer timer = new Timer(){
								@Override
								public void run() {
									recall();	
								}	    	 
					    	 };
					    	 timer.schedule(10000*(count+1));
				  
				    }
				};
		 loginService.MakeMove(info[0],info[1],Long.parseLong(info[2]),info.length==4?info[3]:null,callback);
	 }
}

public LinkedList<String> GetMove(){
	 LinkedList<String> result = new LinkedList<String>();
	 if(store!=null){
	 for(int i=0;i<store.getLength();i++){
		 String key = store.key(i);
		 String type = key.split(",")[0];
		 if(type.equals("Move")) result.add(store.getItem(key)); 
	 }
	 }
	 return result;
}

public LinkedList<String> GetMatch(){
	 LinkedList<String> result = new LinkedList<String>();
	 if(store!=null){
	 for(int i=0;i<store.getLength();i++){
		 String key = store.key(i);
		 String type = key.split(",")[0];
		 if(type.equals("Match")) result.add(store.getItem(key)); 
	 }
	 }
	 return result;
}
public void addMatch(long id,String toAdd){
	store.setItem("Match,"+id, toAdd);
}
public void ClearMatch(){
	 for(int i=0;i<store.getLength();i++){
		 String key = store.key(i);
		 String type = key.split(",")[0];
		 if(type.equals("Match")) store.removeItem(key); 
	 }
}



public void ChangeState(long id,String st){
	 for(int i=0;i<store.getLength();i++){
		 String key = store.key(i);
		 String[] type = key.split(",");
		 if(type[0].equals("Match")&&type[1].equals(id+"")) {
			 String[] items = store.getItem(key).split(" ");
			 store.setItem(key, items[0]+" "+items[1]+" "+st+" "+items[3]+" "+items[4]);			 
		 }
	 }
}
public void DeleteById(String id){
	 for(int i=0;i<store.getLength();i++){
		 String key = store.key(i);
		 String[] type = key.split(",");
		 if(type[0].equals("Match")&&type[1].equals(id)) store.removeItem(key); 
	 }
}

public long getId() {
	return id;
}

public void setId(long id) {
	this.id = id;
}

public String getWhite() {
	return white;
}

public void setWhite(String white) {
	this.white = white;
}

public String getBlack() {
	return black;
}

public void setBlack(String black) {
	this.black = black;
}

public Storage getStore() {
	return store;
}

public void setStore(Storage store) {
	this.store = store;
}



}
