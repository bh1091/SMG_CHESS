package org.wenjiechen.hw7.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.wenjiechen.hw7.client.Player;

import com.google.appengine.api.channel.ChannelPresence;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.googlecode.objectify.Key;

import static com.googlecode.objectify.ObjectifyService.ofy;


public class ConnectedServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6486172071934195531L;

	@Override
	/**
	 * Track Client Connections and Disconnections to add and remove ChannelAPI tokens from the Player entity.
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse resp){
		try {

			ChannelService channelService = ChannelServiceFactory.getChannelService();
			ChannelPresence presence = channelService.parsePresence(req);
			
			// presence's clientId is the id which use to create a channel.
			// if you use a user.email create channel, clientId = user.email
			String channelId = presence.clientId();
//			System.out.println("****clientId = " + channelId);
			String playerEmail = channelId.substring(0, channelId.indexOf("_"));
//			System.out.println("ConnectedServlet(): playerEmail = " + playerEmail);
			Player player = ofy().load().key(Key.create(Player.class, playerEmail)).get();

			//add or remove Channel from the Player entity.
			if (presence.isConnected()) {
				System.out.println("connectedServelt(): connected ");
				player.addchannel(channelId);
			} else {
				System.out.println("connectedServelt(): disconnected");
				player.removechannel(channelId);
			}

			ofy().save().entity(player).now();
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
}
