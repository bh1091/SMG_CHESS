package org.bohuang.hw6.server;




import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bohuang.hw3.StateSerializer;

import org.bohuang.hw6.client.ChessService;
import org.bohuang.hw6.client.LoginInfo;
import org.bohuang.hw7.Match;
import org.bohuang.hw7.Player;
import org.bohuang.hw8.RankCalculator;
import org.shared.chess.Color;
import org.shared.chess.State;

import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

public class ChessServiceImpl extends RemoteServiceServlet implements
ChessService {
	/**
	 * 
	 */
	 public static void addHeadersForCORS(HttpServletRequest req, HttpServletResponse resp) {
		    resp.setHeader("Access-Control-Allow-Methods", "POST"); // "GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS");
		    resp.setHeader("Access-Control-Allow-Origin", "*");
		    resp.setHeader("Access-Control-Allow-Headers", "X-GWT-Module-Base, X-GWT-Permutation, Content-Type"); 
		  }
	 
	 @Override
	  protected void doOptions(HttpServletRequest req, HttpServletResponse resp) {
	    addHeadersForCORS(req, resp);
	  }

	  @Override
	  protected void onAfterResponseSerialized(String serializedResponse) {
	    super.onAfterResponseSerialized(serializedResponse);
	    addHeadersForCORS(getThreadLocalRequest(), getThreadLocalResponse());
	  }
	private static final long serialVersionUID = 1L;
	ChannelService channelService = ChannelServiceFactory.getChannelService();
	static{
		try{
			ObjectifyService.register(Match.class);
			ObjectifyService.register(Player.class);
		} catch (Throwable t){
			t.printStackTrace();
		}
		
	}
	
	Queue<String> players = new LinkedList<String>();
	StateSerializer serializer = new StateSerializer();



public LoginInfo login(String requestUri) {
UserService userService = UserServiceFactory.getUserService();
User user = userService.getCurrentUser();
LoginInfo loginInfo = new LoginInfo();
ChannelService channelService = ChannelServiceFactory.getChannelService();
Objectify ofy = ObjectifyService.ofy().transaction();

if (user != null) {
  loginInfo.setLoggedIn(true);
  loginInfo.setEmailAddress(user.getEmail());
  loginInfo.setNickname(user.getNickname());
  loginInfo.setLogoutUrl(userService.createLogoutURL(requestUri));
  String token =  channelService.createChannel(loginInfo.getEmailAddress());
  loginInfo.setToken(token);
  if(!players.contains(loginInfo.getEmailAddress())){
		players.add(loginInfo.getEmailAddress());
	}
  if(ofy.load().type(Player.class).id(loginInfo.getEmailAddress()).get()==null){
		Player player;
		player = new Player(loginInfo.getEmailAddress(),1500);
		try{
			ofy.save().entity(player).now();
			ofy.getTxn().commit();
		}finally{
			if(ofy.getTxn().isActive()) {
				ofy.getTxn().rollback();
			}
		}
		
	}
} else {
  loginInfo.setLoggedIn(false);
  loginInfo.setLoginUrl(userService.createLoginURL(requestUri));
}


return loginInfo;
}

@Override
public LinkedList<Match> LoadMatch(String UserID) {
	// TODO Auto-generated method stub
	Objectify ofy = ObjectifyService.ofy();
	
	LinkedList<Match> result= new LinkedList<Match>();
	Iterable<Match> stored = ofy.load().type(Match.class);
	Iterator<Match> iter = stored.iterator();
	
	while(iter.hasNext()){
		Match temp = iter.next();
		if(temp.hasId()){
		if((temp.getWhiteId().equals(UserID)&&
				!temp.isDeleteByWhite())||
				temp.getBlackId().equals(UserID)&&
				!temp.isDeleteByBlack()){
					result.add(temp);
					//Log.info("match "+temp.getId()+" found");
				}else{
					//Log.info("not this match " +temp.getId());
				}
		}
	}
	return result;
}



@Override
public Match LoadMatchById(Long id) {
	// TODO Auto-generated method stub
	Objectify ofy = ObjectifyService.ofy().transaction();
	Match match = ofy.load().type(Match.class).id(id).get();
	//Log.info("match load: "+match.getId());
	return match;
}

@Override
public Match SearchGame(String userId) {
	// TODO Auto-generated method stub
	Match temp = null ;
	Objectify ofy = ObjectifyService.ofy().transaction();
	if(players.isEmpty()){
		players.add(userId);
	}
	if(players.peek().equals(userId)){
		players.add(players.poll());
		//Log.info(userId+" add to queue");
	}
	if(players.peek().equals(userId)){
		//Log.info("No Other Players");
	}else{
		//Log.info(players.peek()+"found");
		String opponent = players.poll();
		Random rand = new Random();
		rand.setSeed(System.currentTimeMillis());
		Long tempId = (Long)rand.nextLong();
		temp = new Match(userId,opponent,tempId);
		Player oppo = ofy.load().type(Player.class).id(opponent).get();
		Player self = ofy.load().type(Player.class).id(userId).get();
		int oppoRank = (int) oppo.getRank();
		int selfRank = (int) self.getRank();
		channelService.sendMessage(new ChannelMessage(opponent, "start,"+userId+","+temp.getId()+","+selfRank+","+oppoRank));
		ofy.save().entity(temp).now();
		ofy.getTxn().commit();
	}
	
	return temp;
}

@Override
public Match PlayWith(String userId, String opponent) {
	// TODO Auto-generated method stub
	Match temp;
	Objectify ofy = ObjectifyService.ofy().transaction();
	Random rand = new Random();
	rand.setSeed(System.currentTimeMillis());
	Long tempId = (Long)rand.nextLong();
	temp = new Match(userId,opponent,tempId);
	Player oppo = ofy.load().type(Player.class).id(opponent).get();
	Player self = ofy.load().type(Player.class).id(userId).get();
	int oppoRank = (int) oppo.getRank();
	int selfRank = (int) self.getRank();
	channelService.sendMessage(new ChannelMessage(opponent, "start,"+userId+","+temp.getId()+","+selfRank+","+oppoRank));
	ofy.save().entity(temp).now();
	ofy.getTxn().commit();
	return temp;
}

@Override
public Match MakeMove(String stateString, String userId, Long gameId) {
	// TODO Auto-generated method stub
	Match temp = null;
	String winnerId;
	double winnerPara = 1;
	String loserId;
	double loserPara = 0;
	Objectify ofy = ObjectifyService.ofy().transaction();
	try{
		temp = ofy.load().type(Match.class).id(gameId).get();
		if(temp!=null){			 
			temp.setState(stateString);
			State state = serializer.stringToState(stateString);
			if(state.getTurn().equals(Color.WHITE)){
				channelService.sendMessage(new ChannelMessage(temp.getWhiteId(), "move,"+stateString));
			}else{
				channelService.sendMessage(new ChannelMessage(temp.getBlackId(), "move,"+stateString));
			}
			if(state.getGameResult()!=null){
				if(state.getGameResult().getWinner().isWhite()){
					winnerId = temp.getWhiteId();
					loserId = temp.getBlackId();
				}else if(state.getGameResult().getWinner().isBlack()){
					loserId = temp.getWhiteId();
					winnerId = temp.getBlackId();
				}else{
					winnerId = temp.getWhiteId();
					loserId = temp.getBlackId();
					winnerPara = 0.5;
					loserPara = 0.5;
				}
				Player winner = ofy.load().type(Player.class).id(winnerId).get();
				Player loser  = ofy.load().type(Player.class).id(loserId).get();
				RankCalculator calculator = new RankCalculator();
				Date now = new Date();
				int winnerdays = (int)((winner.getLastPlayed().getTime()-now.getTime())/(1000*60*60*24));
				int loserdays = (int)((loser.getLastPlayed().getTime()-now.getTime())/(1000*60*60*24));
				winner.setRank(calculator.Calculate(winner.getRank(),loser.getRank(),winner.getRD(),winnerdays, winnerPara));
				loser.setRank(calculator.Calculate(loser.getRank(),winner.getRank(),loser.getRD(),loserdays, loserPara));
				winner.setLastPlayed(now);
				loser.setLastPlayed(now);
				winner.setRD(calculator.calculateRD(winnerdays, winner.getRD()));
				loser.setRD(calculator.calculateRD(loserdays, loser.getRD()));
				ofy.save().entity(winner).now();
				ofy.save().entity(loser).now();
			}
			ofy.save().entity(temp).now();
			ofy.getTxn().commit();
		}
	}finally{
		if(ofy.getTxn().isActive()) {
			ofy.getTxn().rollback();
		}
	}
	
	return temp;
}

@Override
public String Delete(String userId, Long gameId) {
	// TODO Auto-generated method stub
	Objectify ofy = ObjectifyService.ofy();
	Match temp = null;
	if(ofy.load().type(Match.class).id(gameId).get()!=null){
		temp = ofy.load().type(Match.class).id(gameId).get();
		if(temp.getWhiteId().equals(userId)){
			temp.setDeleteByWhite(true);
		}
		if(temp.getBlackId().equals(userId)){
			temp.setDeleteByBlack(true);
		}
		if(temp.isDeleteByBlack()&&temp.isDeleteByWhite()){
			ofy.delete().entity(temp).now();
		}
	}else{
		
	}
	return null;
}

@Override
public Double getRank(String userId) {
	Objectify ofy = ObjectifyService.ofy();
	Player player = ofy.load().type(Player.class).id(userId).get();
	
	return player.getRank();
}

}
