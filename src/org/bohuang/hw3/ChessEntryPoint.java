package org.bohuang.hw3;

//add comment to commit

import java.util.LinkedList;

import org.bohuang.hw6.client.ChessConstant;
import org.bohuang.hw6.client.ChessMessages;
import org.bohuang.hw6.client.ChessService;
import org.bohuang.hw6.client.ChessServiceAsync;
import org.bohuang.hw6.client.LoginInfo;
import org.bohuang.hw7.Match;
import org.shared.chess.Color;
import org.shared.chess.State;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.appengine.channel.client.ChannelError;
import com.google.gwt.appengine.channel.client.ChannelFactoryImpl;
import com.google.gwt.appengine.channel.client.Socket;
import com.google.gwt.appengine.channel.client.SocketListener;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.googlecode.gwtphonegap.client.PhoneGap;
import com.googlecode.gwtphonegap.client.PhoneGapAvailableEvent;
import com.googlecode.gwtphonegap.client.PhoneGapAvailableHandler;
import com.googlecode.gwtphonegap.client.PhoneGapTimeoutEvent;
import com.googlecode.gwtphonegap.client.PhoneGapTimeoutHandler;
import com.googlecode.gwtphonegap.client.contacts.Contact;
import com.googlecode.gwtphonegap.client.contacts.ContactError;
import com.googlecode.gwtphonegap.client.contacts.ContactFindCallback;
import com.googlecode.gwtphonegap.client.contacts.ContactFindOptions;
import com.googlecode.gwtphonegap.client.util.PhonegapUtil;
import com.googlecode.gwtphonegap.collection.shared.CollectionFactory;
import com.googlecode.gwtphonegap.collection.shared.LightArray;

public class ChessEntryPoint implements EntryPoint {
	
	 private LoginInfo loginInfo = null;
	 StateSerializer serializer = new StateSerializer();
	 
	 
	
  @Override  
  public void onModuleLoad() {
	  
	  
      final PhoneGap phoneGap = (PhoneGap) GWT.create(PhoneGap.class);
      phoneGap.addHandler(new PhoneGapAvailableHandler(){

			@Override
			public void onPhoneGapAvailable(PhoneGapAvailableEvent event) {
				// TODO Auto-generated method stub
				 Window.alert("success");
				 final ChessConstant constants = GWT.create(ChessConstant.class);
				  final ChessMessages messages = GWT.create(ChessMessages.class);
				  
				  final ChessServiceAsync ChessService = GWT.create(ChessService.class);
				  ((ServiceDefTarget) ChessService).setServiceEntryPoint("http://bohuang1213chess.appspot.com/bohuang/chess");
				  PhonegapUtil.prepareService(((ServiceDefTarget) ChessService), "http://bohuang1213chess.appspot.com/bohuang/", "chess");
				  final Graphics graphics = new Graphics();
			      final Presenter presenter = new Presenter();    
				 HorizontalPanel matchUpPanel = new HorizontalPanel();
			      
			      VerticalPanel savedGamePanel = new VerticalPanel();
			      Label title = new Label(messages.saved());
			      final ListBox savedList = new ListBox();
			      savedList.setWidth("100px");
			      savedGamePanel.add(title);
			      savedGamePanel.add(savedList);
			      
			      
			      VerticalPanel loadDeletePanel = new VerticalPanel();
			      Button load = new Button(messages.load());
			      Button delete = new Button(messages.delete());
			      load.setWidth("50px");
			      delete.setWidth("50px");
			      loadDeletePanel.add(load);
			      loadDeletePanel.add(delete);
			      
			      VerticalPanel matchPanel = new VerticalPanel();
			      Label matchtitle = new Label(messages.matchwith());
			      
			      final LightArray<String> fields = CollectionFactory.<String> constructArray();
			      fields.push("emails"); //search in displayname       
			      final ContactFindOptions findOptions = new ContactFindOptions("", true);
			      final MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
			      
				  phoneGap.getContacts().find(fields, new ContactFindCallback() {

			            @Override
			            public void onSuccess(LightArray<Contact> contacts) {
			                    //read contacts here..
			            	if(contacts!=null){
			            		for(int i = 0 ; i < contacts.length() ; i++){
			            			for(int j = 0 ; j < contacts.get(i).getEmails().length() ; j++){
			            				oracle.add(contacts.get(i).getEmails().get(j).getValue());
			            				Window.alert(contacts.get(i).getEmails().get(j).getValue());
			            			}			            			
			            		}
			            		
			            	}else{
			            		Window.alert("null");
			            	}
			          	  
			            }

			            @Override
			            public void onFailure(ContactError error) {
			                    //something went wrong, doh!
			          	  Window.alert(error.toString());

			            }

						
			    }, findOptions);  
					 
			      final SuggestBox opponentBox = new SuggestBox(oracle);
			      opponentBox.setWidth("100px");
			      HorizontalPanel matchButtonPanel = new HorizontalPanel();
			      Button randMatchButton = new Button(messages.quickmatch());	
			      Button userMatchButton = new Button(messages.custommatch());
			      randMatchButton.setWidth("70px");
			      userMatchButton.setWidth("70px");
			      matchButtonPanel.add(randMatchButton);
			      matchButtonPanel.add(userMatchButton);
			      matchPanel.add(matchtitle);
			      matchPanel.add(opponentBox);
			      matchPanel.add(matchButtonPanel);
			      
			      matchUpPanel.add(savedGamePanel);
			      matchUpPanel.add(loadDeletePanel);
			      matchUpPanel.add(matchPanel);
			      matchUpPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			      
			      load.addClickHandler(new ClickHandler(){

			  		@Override
			  		public void onClick(ClickEvent event) {
			  			// TODO Auto-generated method stub
			  			String[] matchInfo = savedList.getItemText(savedList.getSelectedIndex()).split(",");
			  			String playerId = matchInfo[0];
			  			final long matchId = Long.parseLong(matchInfo[1]);
			  			AsyncCallback<Match> callback = new AsyncCallback<Match>(){
			  				@Override
			  				public void onFailure(Throwable caught) {
			  					// TODO Auto-generated method stub
			  					Window.alert(messages.loadfailed());
			  					
			  				}

			  				@Override
			  				public void onSuccess(Match result) {
			  					// TODO Auto-generated method stub
			  					State state = serializer.stringToState(result.getState());
			  					presenter.setNewState(state);
			  					final long id = Long.parseLong(result.getId().toString());
			  					presenter.setGameId(id);
			  					if(result.getBlackId().equals(loginInfo.getEmailAddress())){
			  						presenter.setPlayerColor(Color.BLACK);
			  					}else{
			  						presenter.setPlayerColor(Color.WHITE);
			  					}
			  					if(result.getBlackId().equals(loginInfo.getEmailAddress())){
			  						presenter.setOpponentId(result.getWhiteId());
			  					}else{
			  						presenter.setOpponentId(result.getBlackId());
			  					}					
			  					presenter.view.reDraw();
			  					presenter.setState();
			  					presenter.setGameId(result.getId());
			  					presenter.startDate = result.getStartDate();
			  					Window.alert(messages.playerfound(result.getId())+"\n"+messages.startdate(result.getStartDate()));
			  										
			  				}
			  			};

			  			ChessService.LoadMatchById(matchId, callback);		
			  		}
			  			
			  		
			  		
			        });
			        
			        delete.addClickHandler(new ClickHandler(){

			    		@Override
			    		public void onClick(ClickEvent event) {
			    			// TODO Auto-generated method stub
			    			String[] matchInfo = savedList.getItemText(savedList.getSelectedIndex()).split(",");
			  			String playerId = matchInfo[0];
			  			final long matchId = Long.parseLong(matchInfo[1]);
			  			AsyncCallback<String> callback = new AsyncCallback<String>(){
			  				@Override
			  				public void onFailure(Throwable caught) {
			  					// TODO Auto-generated method stub
			  					Window.alert(messages.deletefailed());
			  				}

			  				@Override
			  				public void onSuccess(String result) {
			  					// TODO Auto-generated method stub
			  					State state = new State();
			  					presenter.setNewState(state);
			  					savedList.removeItem(savedList.getSelectedIndex());
			  					
			  				}
			  			};
			  			
			  			ChessService.Delete(playerId, matchId, callback);
			    		}
			        	  
			          });
			        
			        randMatchButton.addClickHandler(new ClickHandler(){

			    		@Override
			    		public void onClick(ClickEvent event) {
			    			// TODO Auto-generated method stub
			    			AsyncCallback<Match> callback = new AsyncCallback<Match>(){
			  				@Override
			  				public void onFailure(Throwable caught) {
			  					// TODO Auto-generated method stub
			  					Window.alert(messages.matchfailed());
			  				}

			  				@Override
			  				public void onSuccess(Match result) {
			  					// TODO Auto-generated method stub
			  					if(result.hasId()){
			  					if(result.getBlackId().equals(loginInfo.getEmailAddress())){
			  						savedList.addItem(result.getWhiteId()+" "+result.getId());
			  					}
			  					else if(result.getWhiteId().equals(loginInfo.getEmailAddress())){
			  						savedList.addItem(result.getBlackId()+" "+result.getId());
			  					}
			  					
			  					State state = serializer.stringToState(result.getState());
			  					presenter.setNewState(state);	
			  					final long id = Long.parseLong(result.getId().toString());
			  					presenter.setGameId(id);
			  					presenter.startDate = result.getStartDate();
			  					if(result.getBlackId().equals(loginInfo.getEmailAddress())){
			  						presenter.setPlayerColor(Color.BLACK);
			  					}else{
			  						presenter.setPlayerColor(Color.WHITE);
			  					}
			  					if(result.getBlackId().equals(loginInfo.getEmailAddress())){
			  						presenter.setOpponentId(result.getWhiteId());
			  					}else{
			  						presenter.setOpponentId(result.getBlackId());
			  					}
			  					presenter.view.reDraw();
			  					presenter.setState();
			  					Window.alert(messages.playerfound(presenter.getGameId())+"\n"+messages.startdate(result.getStartDate()));
			  					
			  					}else{
			  						Window.alert(messages.nootherplayer());
			  					}
			  					
			  				}
			  				
			  			};
			  			
			  			ChessService.SearchGame(loginInfo.getEmailAddress(), callback);
			    		}
			        	  
			          });
			        
			        userMatchButton.addClickHandler(new ClickHandler(){

			    		@Override
			    		public void onClick(ClickEvent event) {
			    			// TODO Auto-generated method stub
			    			String opponent = opponentBox.getText();
			    			AsyncCallback<Match> callback = new AsyncCallback<Match>(){
			  				@Override
			  				public void onFailure(Throwable caught) {
			  					// TODO Auto-generated method stub
			  					Window.alert(messages.matchfailed());
			  				}

			  				@Override
			  				public void onSuccess(Match result) {
			  					// TODO Auto-generated method stub
			  					if(result.getBlackId().equals(loginInfo.getEmailAddress())){
			  						savedList.addItem(result.getWhiteId()+" "+result.getId());
			  					}
			  					else if(result.getWhiteId().equals(loginInfo.getEmailAddress())){
			  						savedList.addItem(result.getBlackId()+" "+result.getId());
			  					}
			  					
			  					State state = serializer.stringToState(result.getState());
			  					presenter.setNewState(state);
			  					final long id = Long.parseLong(result.getId().toString());
			  					presenter.setGameId(id);
			  					presenter.startDate = result.getStartDate();
			  					Window.alert(messages.playerfound(presenter.getGameId())+"\n"+messages.startdate(result.getStartDate()));
			  					if(result.getBlackId().equals(loginInfo.getEmailAddress())){
			  						presenter.setPlayerColor(Color.BLACK);
			  					}else{
			  						presenter.setPlayerColor(Color.WHITE);
			  					}
			  					if(result.getBlackId().equals(loginInfo.getEmailAddress())){
			  						presenter.setOpponentId(result.getWhiteId());
			  					}else{
			  						presenter.setOpponentId(result.getBlackId());
			  					}
			  					presenter.view.reDraw();
			  					presenter.setState();
			  					Window.alert(messages.playerfound(presenter.getGameId()));
			  					
			  				}
			  			};
			  			ChessService.PlayWith(loginInfo.getEmailAddress(), opponent, callback);
			    		}
			        	  
			          });
			        
			        ChessService.login(GWT.getHostPageBaseURL()+"bohuang.html", new AsyncCallback<LoginInfo>(){

			    		@Override
			    		public void onFailure(Throwable caught) {
			    			// TODO Auto-generated method stub
			    			
			    		}

			    		@Override
			    		public void onSuccess(LoginInfo result) {
			    			// TODO Auto-generated method stub
			    			loginInfo = result;
			    			if(loginInfo.isLoggedIn()){
			    				presenter.setId(loginInfo.getEmailAddress());
			    				presenter.userInfo = result;
			    				
			    				Window.alert(messages.welcome(loginInfo.getEmailAddress()));
			    				Socket socket =new ChannelFactoryImpl().createChannel(loginInfo.getToken()).open(new SocketListener() {

			    					@Override
			    					public void onOpen() {
			    						// TODO Auto-generated method stub
			    						//Window.alert("Connection Opened!");
			    						AsyncCallback<LinkedList<Match>> callback = new AsyncCallback<LinkedList<Match>>(){

			    							@Override
			    							public void onFailure(Throwable caught) {
			    								// TODO Auto-generated method stub
			    								//Window.alert("Fail to load saved games");
			    							}

			    							@Override
			    							public void onSuccess(LinkedList<Match> result) {
			    								// TODO Auto-generated method stub
			    								if(!result.isEmpty()){
			    								for(int i = 0 ; i < result.size() ; i++){
			    									Match temp = result.get(i);
			    									String onturn;
			    									State state = serializer.stringToState(temp.getState());
			    									if(state.getTurn().isWhite()){
			    										onturn = temp.getWhiteId();
			    									}else{
			    										onturn = temp.getBlackId();
			    									}
			    									if(temp.getBlackId().equals(loginInfo.getEmailAddress())){
			    										savedList.addItem(temp.getWhiteId()+","+temp.getId()+","+onturn);
			    									}else if(temp.getWhiteId().equals(loginInfo.getEmailAddress())){
			    										savedList.addItem(temp.getBlackId()+","+temp.getId()+","+onturn);
			    									}
			    								}
			    							}
			    								

			    							}
			    							
			    						};
			    						
			    						AsyncCallback<Double> rankcallback = new AsyncCallback<Double>(){

			    							@Override
			    							public void onFailure(Throwable caught) {
			    								// TODO Auto-generated method stub
			    								
			    							}

			    							@Override
			    							public void onSuccess(Double result) {
			    								presenter.setPlayerRank(result);								
			    							}
			    							
			    						};
			    						
			    						ChessService.LoadMatch(loginInfo.getEmailAddress(), callback);
			    						ChessService.getRank(loginInfo.getEmailAddress(), rankcallback);
			    					}

			    					@Override
			    					public void onMessage(String message) {
			    						// TODO Auto-generated method stub
			    						String[] receive = message.split(",");
			    						if(receive[0].equals("start")){
			    							savedList.addItem(receive[1]);
			    							final long id = Long.parseLong(receive[2]);
			    							presenter.setGameId(id);
			    							Window.alert(messages.playerfound(presenter.getGameId())+"\n"+messages.yourrank(receive[3])+"\n"+messages.opporank(receive[4]));
			    							presenter.setPlayerColor(Color.BLACK);
			    							presenter.setOpponentId(receive[1]);
			    							presenter.setNewState(new State());
			    							presenter.view.reDraw();
			    							presenter.setState();
			    						}else if(receive[0].equals("move")){
			    							presenter.setNewState(serializer.stringToState(receive[1]));							
			    							presenter.view.reDraw();
			    							presenter.setState();
			    						}
			    					}

			    					@Override
			    					public void onError(ChannelError error) {
			    						// TODO Auto-generated method stub
			    						Window.alert("Connection error: " + error.getDescription());
			    					}

			    					@Override
			    					public void onClose() {
			    						// TODO Auto-generated method stub
			    						Window.alert("Connection closed!");
			    					}
			    					
			    				});
			    			}
			    			else{
			    				LoginPanel log=  new LoginPanel(loginInfo.getLoginUrl());
			    		        log.center();
			    			}
			    			
			    		}
			        	  
			          });
			        
			        presenter.setView(graphics);
			        RootPanel.get().add(graphics);
			        RootPanel.get().add(matchUpPanel);
			     	
			    
			}});      
      phoneGap.addHandler(new PhoneGapTimeoutHandler(){

		@Override
		public void onPhoneGapTimeout(PhoneGapTimeoutEvent event) {
			// TODO Auto-generated method stub
			
		}
    	  
      });
      phoneGap.initializePhoneGap();    
  }
  
  
  

}


