package org.bohuang.hw3;

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LoginPanel extends PopupPanel {
	  private VerticalPanel loginPanel = new VerticalPanel();
	  private Label login = new Label("Please Sign in");
	  private Anchor signIn = new Anchor("Sign In");	  
	  public LoginPanel(String url){
	    signIn.setHref(url);
	    loginPanel.add(login);
	    loginPanel.add(signIn);
	    this.setGlassEnabled(true);
	    
	    this.setWidget(loginPanel);
	  }
}
