package org.vorasahil.hw6.server;

import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.vorasahil.hw6.client.Message;
import org.vorasahil.hw6.client.MessageService;

public class MessageServiceImpl extends RemoteServiceServlet 
   implements MessageService{

	private ChannelService channelService = ChannelServiceFactory.getChannelService();
 
   private static final long serialVersionUID = 1L;

   static int gameId=0;
   static int player=0;
   
   /**
    * Registers a user to a game, and creates/begins the game.
    */
   public Message getMessage(String input) {
	   int gameID=gameId;
	   int playerId=player;
      player++;
      if(player==2){
    	  beginGame(gameId,input);
    	  gameId++;
    	  player%=2;
      }
      else{
    	  createGame(gameId,input);
      }
      
      String token = channelService.createChannel(input);
      Message message = new Message();
      message.setToken(token);
      message.setGameId(gameID);
      message.setPlayerId(playerId);
      return message;
   }   
   
   private void beginGame(int gameId,String player2){
	   Game game=GameManager.getGame(gameId);
	   game.setPlayer2(player2);
	   channelService.sendMessage(new ChannelMessage(game.getPlayer(0),new String("begin")));
  }
   
   private void createGame(int gameId,String player1){
	   Game game=new Game(gameId);
	   game.setPlayer1(player1);
	   GameManager.addGame(game);
   }
}