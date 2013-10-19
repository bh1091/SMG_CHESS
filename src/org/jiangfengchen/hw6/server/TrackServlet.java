package org.jiangfengchen.hw6.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jiangfengchen.hw7.Player;

import com.google.appengine.api.channel.ChannelPresence;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

public class TrackServlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ChannelService channelService = ChannelServiceFactory.getChannelService();
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		ChannelPresence presence = channelService.parsePresence(req);
		Objectify ofy = ObjectifyService.ofy();
		ofy.clear();
		String mail= presence.clientId();		
		Player plr=ofy.load().type(Player.class).id(mail).get();
		plr.setOnline(false);
		LoginServiceImpl.kickoff(plr);
		ofy.save().entity(plr).now();
	}


	  

}
