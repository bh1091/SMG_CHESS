package org.yuanjia.hw6.client;

import java.util.List;

import org.yuanjia.hw7.Match;
import org.yuanjia.hw7.Player;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("service")
public interface ChessServices extends RemoteService{
	
	public void displayState(String stateStr, String clientId, String matchId);
	
	public LoginInfo login(String requestUri);

	public boolean autoMatch();
	
	public String invite(String OpEmail);
	
	public void removeMatch(String matchId);
	
	public void loadMatch(String matchId, String clientId);
	
	public Match getMatch(String matchId);
	
	public String setGameResult(String matchId, String winnerColor);
	
	public Player getPlayer(String playerEmail);
	
	public List<Match> getMatchList(String playerEmail);
}
