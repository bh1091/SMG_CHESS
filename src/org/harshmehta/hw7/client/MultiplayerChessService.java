package org.harshmehta.hw7.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("multiplayer")
public interface MultiplayerChessService extends RemoteService {
  String connectPlayer();
  String[] loadMatches();
  void automatch();
  Boolean newEmailGame(String email);
  void deleteMatch(Long matchId);
  String changeMatch(Long matchId);
  void makeMove(Long matchId, String moveString, String stateString);
  String registerAImatch(Boolean aiWhite);
  void makeAImove(Long matchId, String moveString, String stateString);
}
