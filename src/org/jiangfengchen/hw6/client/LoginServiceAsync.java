package org.jiangfengchen.hw6.client;

import java.util.LinkedList;

import org.jiangfengchen.hw7.Match;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LoginServiceAsync {

	void login(String requestUri, AsyncCallback<LoginInfo> callback);

	void LoadGame(String userId, AsyncCallback<LinkedList<Match>> callback);

	void SearchGame(String userId, AsyncCallback<Match> callback);

	void MakeMove(String state, String userId, long gameId, String result,
			AsyncCallback<Match> callback);

	void GameWith(String userId, String opponent, AsyncCallback<Match> callback);

	void Delete(String userId, long gameId, AsyncCallback<String> callback);

	void LoadById(long id, AsyncCallback<Match> callback);

	void SaveContact(long id,String emails,
			AsyncCallback<Void> callback);

	void LoadContact(long id, AsyncCallback<String> callback);

}
