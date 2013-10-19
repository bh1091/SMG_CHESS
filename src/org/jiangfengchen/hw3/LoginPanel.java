package org.jiangfengchen.hw3;

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.googlecode.mgwt.ui.client.widget.RoundPanel;



public class LoginPanel extends PopupPanel {
	  private RoundPanel loginpanel = new RoundPanel();
	  private Label loginLabel = new Label("Please sign in to your Google Account");
	  private Anchor signInLink = new Anchor("Sign In");
	
	  public LoginPanel(String url){
	    signInLink.setHref(url);
	    signInLink.setText("Click Here to Sign In");
	    loginLabel.setStyleName("black");
	    loginpanel.add(loginLabel);
	    loginpanel.add(signInLink);
	    this.setWidth("300px");
	    this.setGlassEnabled(true);
	    this.setAnimationEnabled(true);
	    this.setWidget(loginpanel);
	  }
}
