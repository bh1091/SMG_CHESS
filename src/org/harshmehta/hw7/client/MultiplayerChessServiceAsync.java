package org.harshmehta.hw7.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MultiplayerChessServiceAsync {
  void connectPlayer(AsyncCallback<String> callback);
  void loadMatches(AsyncCallback<String[]> callback);
  void automatch(AsyncCallback<Void> callback);
  void newEmailGame(String email, AsyncCallback<Boolean> callback);
  void deleteMatch(Long matchId, AsyncCallback<Void> callback);
  void changeMatch(Long matchId, AsyncCallback<String> callback);
  void makeMove(Long matchId, String moveString, String stateString, AsyncCallback<Void> callback);
  void registerAImatch(Boolean aiWhite, AsyncCallback<String> callback);
  void makeAImove(Long matchId, String moveString, String stateString, AsyncCallback<Void> callback); 
}
