package org.ashishmanral.hw7;


import org.ashishmanral.hw11.PopulateContacts;
import org.ashishmanral.hw6.client.LoginService;
import org.ashishmanral.hw6.client.LoginServiceAsync;
import org.ashishmanral.hw6.client.UserDetails;
import org.ashishmanral.hw7.MultiplayerChessService;
import org.ashishmanral.hw7.MultiplayerChessServiceAsync;
import org.ashishmanral.hw8.GameMessages;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * ChessEntryPoint class
 * @author Ashish
 * EntryPoint for GWT
 */
public class ChessEntryPoint implements EntryPoint{
  
  private UserDetails userDetails = null;
  GameMessages messages = (GameMessages)GWT.create(GameMessages.class);

  
  /**
   * First method that gets fired
   */
  public void onModuleLoad() {
    LoginServiceAsync ashishLoginService = GWT.create(LoginService.class);
    ashishLoginService.login(Window.Location.getHref(), new AsyncCallback<UserDetails>() {
      public void onFailure(Throwable error) {
        Window.alert(messages.loginOnFailure() + error.getMessage());
      }
      
      public void onSuccess(UserDetails details) {
        userDetails = details;
        if(userDetails.isLoggedIn()) {
        	MultiplayerChessServiceAsync chessService = GWT.create(MultiplayerChessService.class);
            chessService.initializePlayer(userDetails.getEmailId(), new AsyncCallback<String>() {

              @Override
              public void onFailure(Throwable caught) {
                Window.alert(messages.initializePlayerOnFailure() + caught.getMessage());
              }

              @Override
              public void onSuccess(String token) {
            	  Graphics graphics;
                  if(History.getToken().isEmpty()){
                    Storage storage=Storage.getLocalStorageIfSupported();
                    if(storage!=null) storage.clear();
                      History.newItem("WR$WG$WB$WQ$WK$WB$WG$WR$WP$WP$WP$WP$WP$WP$WP$WP$N$N$N$N$N$N$N$N$N$N$N$N$N$N$N$N$N$N$N$N$N$N$N$N$N$N$N$N$N$N$N$N$BP$BP$BP$BP$BP$BP$BP$BP$BR$BG$BB$BQ$BK$BB$BG$BR$W$T$T$T$T$0$88$N");
                      graphics=new Graphics(token, userDetails.getEmailId(), messages);
                    }
                    else{
                      graphics=new Graphics(token, userDetails.getEmailId(), messages);
                      graphics.restoreState(History.getToken());
                    }
                    RootPanel.get().add(graphics);
                    final Graphics finalGraphics=graphics;
                    History.addValueChangeHandler(new ValueChangeHandler<String>() {
                      public void onValueChange(ValueChangeEvent<String> event) {
                        String historyToken = event.getValue();
                        finalGraphics.restoreState(historyToken);
                      }
                    });}
              
            });
          } 
          else {
        	Anchor signIn = new Anchor(messages.signInAnchor());
        	signIn.setHref(userDetails.getLoginUrl());
        	VerticalPanel verticalPanel = new VerticalPanel();
        	verticalPanel.add(new Label(messages.signInRequest()));
        	verticalPanel.add(signIn);
            RootPanel.get().add(verticalPanel);
          }
      }
    });
  } 
}

