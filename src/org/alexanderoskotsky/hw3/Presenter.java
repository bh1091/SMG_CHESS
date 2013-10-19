package org.alexanderoskotsky.hw3;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.alexanderoskotsky.hw10.NullMatchInfo;
import org.alexanderoskotsky.hw10.SimpleAsyncCallback;
import org.alexanderoskotsky.hw10.StorageHelper;
import org.alexanderoskotsky.hw2.StateChangerImpl;
import org.alexanderoskotsky.hw2_5.StateExplorerImpl;
import org.alexanderoskotsky.hw5.Callback;
import org.alexanderoskotsky.hw6.ChessServiceAsync;
import org.alexanderoskotsky.hw6.PlayerInfo;
import org.alexanderoskotsky.hw7.MatchInfo;
import org.alexanderoskotsky.hw8.GameMessages;
import org.alexanderoskotsky.hw9.AlphaBetaPruning;
import org.alexanderoskotsky.hw9.ChessHeuristic;
import org.alexanderoskotsky.hw9.Timer;
import org.shared.chess.Color;
import org.shared.chess.GameResult;
import org.shared.chess.GameResultReason;
import org.shared.chess.IllegalMove;
import org.shared.chess.Move;
import org.shared.chess.Piece;
import org.shared.chess.PieceKind;
import org.shared.chess.Position;
import org.shared.chess.State;
import org.shared.chess.StateChanger;
import org.shared.chess.StateExplorer;

import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.DragHandlerAdapter;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.google.gwt.appengine.channel.client.ChannelError;
import com.google.gwt.appengine.channel.client.SocketListener;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;

public class Presenter {
	public interface View {
		/**
		 * Renders the piece at this position. If piece is null then the
		 * position is empty.
		 */
		void setPiece(int row, int col, Piece piece);

		/**
		 * Turns the highlighting on or off at this cell. Cells that can be
		 * clicked should be highlighted.
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

		SourcesTableEvents getGrid();

		SourcesTableEvents getWhitePromotionGrid();

		SourcesTableEvents getBlackPromotionGrid();

		void toggleWhitePromotion(boolean show);

		void toggleBlackPromotion(boolean show);

		void animateMove(Move move, Callback callback);

		void playMoveSound();

		HasClickHandlers getNewGameButton();

		HasClickHandlers getDeleteGameButton();

		void setMatchList(Map<String, String> items, String selectedMatchId);

		String getSelectedMatch();

		/**
		 * Open a channel to server
		 * 
		 * @param listener
		 */
		void openChannel(String channel, SocketListener listener);

		/**
		 * Set the label that displays the player's name
		 * 
		 * @param name of the player
		 */
		void setNameLabel(String name, int rank, String email);

		HasClickHandlers getMatchButton();

		HasClickHandlers getAutoMatchButton();

		HasClickHandlers getCancelAutoMatchButton();

		HasText getMatchBox();

		HasChangeHandlers getMatchList();

		void toggleAutoMatch(boolean show);

		HasClickHandlers getAiButton();

		void makeDraggableFromTo(int fromRow, int fromCol, int toRow, int toCol, Callback callback);

		void clearDragDrop();

		void addDragHandler(int row, int col, DragHandler dh);
	}

	private GameMessages messages = new GameMessages();

	private View view;

	private final StateExplorer explorer = new StateExplorerImpl();

	private StateChanger stateChanger = new StateChangerImpl();

	private Position activePosition;

	private Move pendingPromotionMove;

	private boolean animationEnabled = true;

	private Logger logger = Logger.getLogger(Presenter.class.toString());

	private String myEmail;

	private ChessServiceAsync service;

	/**
	 * info for the currently active match
	 */
	private MatchInfo matchInfo = NullMatchInfo.INSTANCE;

	private List<MatchInfo> matches = new ArrayList<MatchInfo>();

	private StorageHelper storage = new StorageHelper();

	private Map<String, String> pendingMoveMap = new LinkedHashMap<String, String>();

	public Presenter(ChessServiceAsync service, View view) {
		this.service = service;
		this.view = view;

		myEmail = storage.getFromStorage("email");
	}

	void setMatchInfo(MatchInfo info) {
		this.matchInfo = info;
		drawState();
	}

	public void setEmail(String email) {
		myEmail = email;
	}

	private void loadMatchList() {
		String savedMatchesString = storage.getFromStorage("matchList");

		if (savedMatchesString != null) {
			List<MatchInfo> savedMatches = new ArrayList<MatchInfo>();
			String[] data = savedMatchesString.split(" ");
			for (String savedMatch : data) {
				savedMatches.add(MatchInfo.deserialize(savedMatch));
			}
			processMatchList(savedMatches);
		}

		service.getMatches(myEmail, new SimpleAsyncCallback<List<MatchInfo>>() {

			@Override
			public void onSuccess(List<MatchInfo> result) {
				logger.info(result.toString());

				processMatchList(result);
				saveMatchesToStorage();

			}
		});
	}

	private void saveMatchesToStorage() {
		if (matches.isEmpty()) {
			storage.remove("matchList");
			return;
		}
		String matchString = "";
		for (MatchInfo info : matches) {
			matchString += info.serialize() + " ";
		}

		matchString = matchString.substring(0, matchString.length() - 1);

		storage.saveInStorage("matchList", matchString);
	}

	private void savePendingMovesToStorage() {
		if (pendingMoveMap.isEmpty()) {
			storage.remove("pendingMoves");
			return;
		}
		String s = "";
		for (Map.Entry<String, String> entry : pendingMoveMap.entrySet()) {
			s += entry.getKey() + "#" + entry.getValue() + " ";
		}
		s = s.substring(0, s.length() - 1);
		storage.saveInStorage("pendingMoves", s);
	}

	private Map<String, String> loadPendingMovesFromStorage() {
		String s = storage.getFromStorage("pendingMoves");
		Map<String, String> moveMap = new LinkedHashMap<String, String>();
		if (s == null || s.isEmpty()) {
			return moveMap;
		}

		try {
			String[] moves = s.split(" ");
			for (String move : moves) {
				String[] data = move.split("#");
				moveMap.put(data[0], data[1]);
			}
		} catch (Exception e) {
			// do nothing
		}

		return moveMap;
	}

	private void processMatchList(List<MatchInfo> matches) {
		this.matches = matches;

		// a map of matchIds to match descriptions to give to the view
		Map<String, String> matchMap = new LinkedHashMap<String, String>();

		for (MatchInfo info : matches) {
			matchMap.put(info.getMatchId(), getMatchDescription(info));
		}

		view.setMatchList(matchMap, matchInfo.getMatchId());
	}

	/**
	 * Get a string representation of a match
	 * 
	 * @param info
	 * @return
	 */
	private String getMatchDescription(MatchInfo info) {
		String opponentEmail = info.getOpponentEmail();
		int opponentRank = info.getOpponentRank();

		String string = opponentEmail + "[" + opponentRank + "]" + " - " + messages.match() + "#"
				+ info.getMatchId();

		if (info.getReason() != null) {
			if (info.getReason().equals(GameResultReason.CHECKMATE)) {
				String winner = info.getWinner().equals(info.getMyColor()) ? myEmail : info
						.getOpponentEmail();

				string += ", " + messages.won(winner);

			} else {
				switch (info.getReason()) {
				case FIFTY_MOVE_RULE:
					string += ", " + messages.fiftyMoveRule();
					break;
				case STALEMATE:
					string += ", " + messages.stalemate();
					break;
				case THREEFOLD_REPETITION_RULE:
					string += ", " + messages.threeFoldRepetition();
					break;
				default:
					// do nothing. we handled checkmate above
					break;
				}
			}

		} else {
			String turn = info.getTurn().equals(info.getMyColor()) ? myEmail : info
					.getOpponentEmail();

			string += ", " + messages.turnOf(turn);
		}

		string += " "
				+ DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_SHORT).format(
						info.getStartDate());

		return string;
	}

	/**
	 * Create a new match with a player by their email address
	 * 
	 * @param email the email address of the player to play with
	 */
	private void createMatch(final String email) {
		service.createMatch(myEmail, email, new SimpleAsyncCallback<MatchInfo>() {

			@Override
			public void onSuccess(MatchInfo info) {
				logger.info("new match created with " + email);
				loadMatch(info.getMatchId());
				loadMatchList();
			}
		});

	}

	private void loadMatch(String matchId) {
		// first see if we have that match cached in our list
		// if so then load it
		for (MatchInfo match : matches) {
			if (match.getMatchId().equals(matchId)) {
				matchInfo = match;

				drawState();

				break;
			}
		}

		// send a request for latest state in case ours was stale
		service.getMatchState(myEmail, matchId, new SimpleAsyncCallback<MatchInfo>() {
			@Override
			public void onSuccess(MatchInfo result) {
				logger.info(result.toString());

				matchInfo = result;

				drawState();
			}
		});
	}

	/**
	 * Tell the presenter attach event handlers to the view
	 */
	public void bindEvents() {
		myEmail = Window.prompt("Enter your email address", "");

		view.getCancelAutoMatchButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				view.toggleAutoMatch(false);

			}
		});

		view.getAutoMatchButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				view.toggleAutoMatch(true);
				service.autoMatch(myEmail, new SimpleAsyncCallback<Void>() {

					@Override
					public void onSuccess(Void result) {
						logger.info("auto matching");
					}
				});
			}
		});

		view.getDeleteGameButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				service.deleteMatch(myEmail, matchInfo.getMatchId(),
						new SimpleAsyncCallback<Void>() {

							@Override
							public void onSuccess(Void result) {
								clearGame();
								matchInfo = NullMatchInfo.INSTANCE;
								loadMatchList();
							}
						});

			}
		});

		view.getMatchList().addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				if (view.getSelectedMatch().isEmpty()) {
					clearGame();
				} else {
					loadMatch(view.getSelectedMatch());
				}
			}
		});

		view.getMatchButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				final String email = view.getMatchBox().getText();
				createMatch(email);

			}

		});

		service.connect(myEmail, new SimpleAsyncCallback<PlayerInfo>() {

			@Override
			public void onSuccess(PlayerInfo result) {
				loadMatchList();
				
				view.setNameLabel(result.getUsername(), result.getRank(), result.getEmail());

				myEmail = result.getEmail();

				storage.saveInStorage("email", myEmail);

				view.openChannel(result.getChannelId(), new SocketListener() {

					@Override
					public void onOpen() {
						logger.log(Level.INFO, "open");
					}

					@Override
					public void onMessage(String message) {
						logger.log(Level.SEVERE, message);

						if (message.contains("NewMatch")) {
							logger.info("new game");

							view.toggleAutoMatch(false);

							String matchId = message.substring(9, message.length() - 1);

							loadMatchList();

							loadMatch(matchId);
						} else {
							String[] parts = message.split("#");

							String id = parts[0];
							String stateString = parts[1];

							if (id.equals(matchInfo.getMatchId())) {
								matchInfo.setStateString(stateString);
								drawState();
							}

							loadMatchList();
						}
					}

					@Override
					public void onError(ChannelError error) {
						logger.log(Level.INFO, error.toString());
					}

					@Override
					public void onClose() {
						logger.log(Level.INFO, "close");

					}
				});
				logger.log(Level.INFO, result.toString());
			}
		});

		view.getGrid().addTableListener(new TableListener() {

			@Override
			public void onCellClicked(SourcesTableEvents sender, int row, int cell) {
				handleBoardClick(row, cell);
			}
		});

		view.getWhitePromotionGrid().addTableListener(new TableListener() {

			@Override
			public void onCellClicked(SourcesTableEvents sender, int row, int cell) {
				handlePromotion(cell);
			}
		});

		view.getBlackPromotionGrid().addTableListener(new TableListener() {

			@Override
			public void onCellClicked(SourcesTableEvents sender, int row, int cell) {
				handlePromotion(cell);
			}
		});

		view.getNewGameButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				createMatch(matchInfo.getOpponentEmail());
			}
		});

		view.getAiButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				createMatch("AI");
			}

		});

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				final int x = i;
				final int y = j;

				view.addDragHandler(i, j, new DragHandlerAdapter() {
					@Override
					public void onDragEnd(DragEndEvent event) {
						clearHighlighting();
						activePosition = null;
					}

					@Override
					public void onDragStart(DragStartEvent event) {
						handleBoardClick(x, y);
					}
				});
			}
		}

		com.google.gwt.user.client.Timer t = new com.google.gwt.user.client.Timer() {
			public void run() {
				if (!pendingMoveMap.isEmpty()) {
					Map.Entry<String, String> entry = pendingMoveMap.entrySet().iterator().next();

					updateServerState(entry.getKey(), entry.getValue(), false);
				}
			}
		};

		t.scheduleRepeating(10000);

		pendingMoveMap = loadPendingMovesFromStorage();
		
		loadMatchList();
	}

	private void handlePromotion(int cell) {
		Move newMove = new Move(pendingPromotionMove.getFrom(), pendingPromotionMove.getTo(),
				getKindFromPromotionCol(cell));

		doMove(newMove);
		clearPromotion();
	}

	private void clearPromotion() {
		pendingPromotionMove = null;
		view.toggleBlackPromotion(false);
		view.toggleWhitePromotion(false);
	}

	private PieceKind getKindFromPromotionCol(int col) {
		if (col == 0) {
			return PieceKind.KNIGHT;
		} else if (col == 1) {
			return PieceKind.BISHOP;
		} else if (col == 2) {
			return PieceKind.ROOK;
		} else {
			return PieceKind.QUEEN;
		}
	}

	private void clearGame() {
		logger.info("clearing game");
		matchInfo = NullMatchInfo.INSTANCE;
		drawState();
	}

	private void drawState() {
		if (!isGameActive()) {
			view.setWhoseTurn(null);
			view.setGameResult(null);

			for (int r = 0; r < 8; r++) {
				for (int c = 0; c < 8; c++) {
					view.setPiece(r, c, null);
				}
			}
			return;
		}

		State state = matchInfo.getState();

		view.setWhoseTurn(state.getTurn());
		view.setGameResult(state.getGameResult());

		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				view.setPiece(7 - r, c, state.getPiece(r, c));
			}
		}

		view.clearDragDrop();
		if (state.getTurn().equals(matchInfo.getMyColor())) {
			Set<Move> moves = explorer.getPossibleMoves(state);
			for (Move move : moves) {
				final Position start = move.getFrom();
				final Position target = move.getTo();
				view.makeDraggableFromTo(7 - start.getRow(), start.getCol(), 7 - target.getRow(),
						target.getCol(), new Callback() {

							@Override
							public void execute() {
								handleBoardClick(7 - target.getRow(), target.getCol());
							}
						});
			}
		}

	}

	private void clearHighlighting() {
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				view.setHighlighted(r, c, false);
			}
		}
	}

	/**
	 * Handler for click events on the board
	 * 
	 * Package private visibility to help with testing
	 * 
	 * @param row using the view's coordinates so row 7 will be on bottom
	 * @param cell the column number
	 */
	void handleBoardClick(int row, int cell) {
		if (!isGameActive()) {
			return;
		}

		State state = matchInfo.getState();
		if (!state.getTurn().equals(matchInfo.getMyColor())) {
			return;
		}

		if (pendingPromotionMove != null) {
			return;
		}

		if (activePosition == null) {
			Set<Position> starts = explorer.getPossibleStartPositions(state);

			Position fromPos = new Position(7 - row, cell);

			if (starts.contains(fromPos)) {
				clearHighlighting();
				activePosition = fromPos;
				view.setHighlighted(row, cell, true);

				Set<Move> moves = explorer.getPossibleMovesFromPosition(state, fromPos);

				for (Move move : moves) {
					Position toPos = move.getTo();
					view.setHighlighted(7 - toPos.getRow(), toPos.getCol(), true);
				}
			} else {
				drawState();
			}
		} else {
			Position toPosition = new Position(7 - row, cell);

			Move move = new Move(activePosition, toPosition, null);

			Set<Move> moves = explorer.getPossibleMovesFromPosition(state, activePosition);

			if (isValidMove(moves, move)) {

				Piece fromPiece = state.getPiece(move.getFrom());
				int backRow = state.getTurn().equals(Color.WHITE) ? 7 : 0;

				if (fromPiece.getKind().equals(PieceKind.PAWN) && move.getTo().getRow() == backRow) {
					if (state.getTurn().equals(Color.WHITE)) {

						view.toggleWhitePromotion(true);
					} else {
						view.toggleBlackPromotion(true);
					}

					pendingPromotionMove = move;

				} else {
					doMove(move);
				}
			} else {
				activePosition = null;
				clearHighlighting();
				drawState();
			}
		}
	}

	/**
	 * Return whether or not we are currently playing a game
	 * 
	 * @return true if there is an active game, false otherwise
	 */
	private boolean isGameActive() {
		return !(matchInfo instanceof NullMatchInfo);
	}

	private void afterMove(State state) {
		drawState();
		sendState();
	}

	private void doMove(Move move) {
		final State state = matchInfo.getState();
		try {
			stateChanger.makeMove(state, move);

			matchInfo.setState(state);
			matchInfo.setTurnNumber(matchInfo.getTurnNumber() + 1);
			matchInfo.setTurn(state.getTurn());
			// TODO also update winner/reason

			for (int i = 0; i < matches.size(); i++) {
				MatchInfo info = matches.get(i);
				if (info.getMatchId().equals(matchInfo.getMatchId())) {
					matches.set(i, matchInfo);
				}
			}

			saveMatchesToStorage();

			if (animationEnabled) {
				view.animateMove(move, new Callback() {
					@Override
					public void execute() {
						afterMove(state);
					}
				});
			} else {
				afterMove(state);
			}

			activePosition = null;
			clearHighlighting();

			view.playMoveSound();

			if (state.getGameResult() != null) {
				service.getPlayerInfo(myEmail, new SimpleAsyncCallback<PlayerInfo>() {

					@Override
					public void onSuccess(PlayerInfo result) {
						view.setNameLabel(result.getUsername(), result.getRank(), result.getEmail());
					}
				});
			}

			logger.log(Level.INFO, "Move: " + move.toString());

		} catch (IllegalMove e) {
			logger.log(Level.SEVERE, "Error when moving: " + move.toString(), e);
		}
	}

	private void sendState() {
		final State state = matchInfo.getState();
		String token = matchInfo.getStateString();
		logger.info("sending state " + token);

		final String moveId = matchInfo.getMatchId() + "_" + matchInfo.getTurnNumber();

		pendingMoveMap.put(moveId, token);
		savePendingMovesToStorage();

		logger.info(pendingMoveMap.toString());

		updateServerState(moveId, token, true);

		// move AI
		if (!state.getTurn().equals(matchInfo.getMyColor())
				&& matchInfo.getOpponentEmail().equals("AI")) {
			AlphaBetaPruning abp = new AlphaBetaPruning(new ChessHeuristic(), stateChanger);
			Move move = abp.findBestMove(state, 10, new Timer(5000));

			logger.info("AI move " + move.toString());

			doMove(move);
		}
	}

	private void updateServerState(final String moveId, String token, final boolean showAlert) {
		logger.log(Level.INFO, "sending sent " + moveId + " " + token);
		String[] data = moveId.split("_");
		String matchId = data[0];
		int turn = Integer.parseInt(data[1]);
		service.sendState(myEmail, matchId, token, turn, new SimpleAsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				if (showAlert) {
					Window.alert("You are not online. Move will be submitted to server when you reconnect");
				}
			}

			@Override
			public void onSuccess(Void result) {
				loadMatchList();
				pendingMoveMap.remove(moveId);
				savePendingMovesToStorage();
			}
		});
	}

	private boolean isValidMove(Set<Move> moves, Move move) {
		for (Move m : moves) {
			if (m.getFrom().equals(move.getFrom()) && m.getTo().equals(move.getTo())) {
				return true;
			}
		}

		return false;
	}

	public boolean isAnimationEnabled() {
		return animationEnabled;
	}

	public void setAnimationEnabled(boolean animationEnabled) {
		this.animationEnabled = animationEnabled;
	}
}
