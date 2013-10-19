package org.yuanjia.hw6.client;

import java.util.List;

import org.yuanjia.hw7.Match;
import org.yuanjia.hw7.Player;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ChessServicesAsync {
	
	void displayState(String stateStr, String clientId, String matchId, AsyncCallback<Void> callback);
	
	void login(String requestUri,  AsyncCallback<LoginInfo> callback);

	void autoMatch(AsyncCallback<Boolean> callback);

	void invite(String text, AsyncCallback<String> callback);

	void removeMatch(String matchId, AsyncCallback<Void> callback);

	void loadMatch(String clientId, String string, AsyncCallback<Void> callback);

	void getMatch(String matchId, AsyncCallback<Match> callback);

	void setGameResult(String matchId, String winnewColor,
			AsyncCallback<String> callback);

	void getPlayer(String playerEmail,
			AsyncCallback<Player> callback);

	void getMatchList(String playerEmail, AsyncCallback<List<Match>> callback);
}
