package org.ashishmanral.hw7;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ashishmanral.hw7.Player;

import com.google.appengine.api.channel.ChannelPresence;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.googlecode.objectify.Key;

public class PresenceServlet extends HttpServlet {
  
  private static final long serialVersionUID = 1L;
  ChannelService channelService = ChannelServiceFactory.getChannelService();
  
  @Override
  public void doPost(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
    ChannelPresence channelPresence;
    try {
      channelPresence = channelService.parsePresence(httpRequest);
      String userEmail = channelPresence.clientId();
      
      Key<Player> currentPlayerKey = Key.create(Player.class, userEmail);
      Player currentPlayer = ofy().load().key(currentPlayerKey).get();
            
    } catch (IOException exception) {
      System.err.println("Exception in PresenceServlet : " + exception.getMessage());
    }
  }
}
