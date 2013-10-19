package org.jiangfengchen.hw3;

import java.util.LinkedList;


import org.jiangfengchen.hw6.client.LoginInfo;
import org.jiangfengchen.hw6.client.LoginService;
import org.jiangfengchen.hw6.client.LoginServiceAsync;
import org.jiangfengchen.hw6.client.MyMessages;
import org.shared.chess.State;
import org.jiangfengchen.hw7.Match;


import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Anchor;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.appengine.channel.client.ChannelFactoryImpl;
import com.google.gwt.appengine.channel.client.Socket;
import com.google.gwt.appengine.channel.client.SocketListener;
import com.google.gwt.appengine.channel.client.ChannelError;
import com.googlecode.gwtphonegap.client.PhoneGap;
import com.googlecode.gwtphonegap.client.PhoneGapAvailableEvent;
import com.googlecode.gwtphonegap.client.PhoneGapAvailableHandler;
import com.googlecode.gwtphonegap.client.PhoneGapTimeoutEvent;
import com.googlecode.gwtphonegap.client.PhoneGapTimeoutHandler;
import com.googlecode.gwtphonegap.client.contacts.Contact;
import com.googlecode.gwtphonegap.client.contacts.ContactError;
import com.googlecode.gwtphonegap.client.contacts.ContactField;
import com.googlecode.gwtphonegap.client.contacts.ContactFindCallback;
import com.googlecode.gwtphonegap.client.contacts.ContactFindOptions;
import com.googlecode.gwtphonegap.client.util.PhonegapUtil;
import com.googlecode.gwtphonegap.collection.shared.CollectionFactory;
import com.googlecode.gwtphonegap.collection.shared.LightArray;



public class ChessEntryPoint implements EntryPoint {
  private LoginInfo loginInfo = null;
  long SelectGame;
  State state=Presenter.Deserialize("Wtttt$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$0");
  LoginServiceAsync loginService = GWT.create(LoginService.class);
  @Override
  public void onModuleLoad() {
	  final PhoneGap phoneGap = (PhoneGap) GWT.create(PhoneGap.class);
		phoneGap.addHandler(new PhoneGapAvailableHandler(){

			@Override
			public void onPhoneGapAvailable(PhoneGapAvailableEvent event) {
				((ServiceDefTarget) loginService).setServiceEntryPoint("http://kanppa.appspot.com/jiangfengchen/login");
				 PhonegapUtil.prepareService((ServiceDefTarget) loginService, "http://kanppa.appspot.com/jiangfengchen/", "login");
				    final MyMessages myMessage = GWT.create(MyMessages.class);
				    final Presenter presenter = new Presenter();  
				    final String par = Window.Location.getParameter("ckeyid");
				    final long xx = System.currentTimeMillis();
				    PickupDragController dragController = new PickupDragController(RootPanel.get(),false);
				    dragController.setBehaviorDragStartSensitivity(15);
				    dragController.setBehaviorBoundaryPanelDrop(false);
				    final LinkedList<String> conList=new LinkedList<String>();
					final Graphics graphics = new Graphics();		
					presenter.setView(graphics);
					graphics.init(dragController);
					RootPanel.get().add(graphics);
				    VerticalPanel footer= new VerticalPanel();
				    final MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
				    final SuggestBox mailbox= new SuggestBox(oracle); 
				    mailbox.getElement().setId("mybox");
					LightArray<String> fields = CollectionFactory.<String>constructArray();
					fields.push("emails");
					ContactFindOptions findOptions = new ContactFindOptions("",true);
					phoneGap.getContacts().find(fields, new ContactFindCallback(){

						@Override
						public void onSuccess(LightArray<Contact> contacts) {
							for(int i=0;i<contacts.length();i++){
								Contact friend = contacts.get(i);
								LightArray<ContactField> emails=friend.getEmails();
								for(int j=0;j<emails.length();j++){
									ContactField email=emails.get(j);
								if(isGmail(email.getValue()))	{
									conList.add(email.getValue());
									oracle.add(email.getValue());
								}
									
								}
							}
						
						}
					
						@Override
						public void onFailure(ContactError error) {
							Window.alert(error.toString());
						}}, findOptions);
				    
				    
				    final Anchor signOutLink = new Anchor("Sign Out");
				    HorizontalPanel rbuttons= new HorizontalPanel();
				    HorizontalPanel left = new HorizontalPanel();
				    Button load = new Button(myMessage.load());
				    Button delete = new Button(myMessage.delete());
				    Button fmatch = new Button(myMessage.quickmatch());
				    Button ai = new Button(myMessage.playwithai());
				    Button playwith = new Button(myMessage.playwith());
				    load.setPixelSize(70, 28);
				    delete.setPixelSize(70, 28);
				    fmatch.setPixelSize(100, 35);
				    ai.setPixelSize(100, 35);
				    playwith.setPixelSize(100, 35);
				    Label cg= new Label(myMessage.currentgames());
				    graphics.setLabel(cg);
				    ai.setStylePrimaryName("button"); 
				    fmatch.setStylePrimaryName("button");
				    playwith.setStylePrimaryName("button");
				    load.setStylePrimaryName("button");
				    delete.setStylePrimaryName("button");
				    rbuttons.add(ai);
				    rbuttons.add(playwith);
				    rbuttons.add(fmatch);
				    rbuttons.setSpacing(5);
				    left.setHeight("30px");
				    final ListBox dropdown = new ListBox();
				    dropdown.setHeight("25px");
				    dropdown.setVisibleItemCount(1);
				    dropdown.setWidth("170px");
				    left.add(cg);
				    left.add(dropdown);
				    left.add(load);
				    left.add(delete);
				    left.setSpacing(5);
				 
		             
	
					mailbox.setWidth("320px");
				    mailbox.setHeight("15px");
				    
				   
				    load.addClickHandler(new ClickHandler(){
				    	
						@Override
						public void onClick(ClickEvent event) {
							final int select= dropdown.getSelectedIndex();
							String text = dropdown.getItemText(select);
							String[] res=text.split(" "); 
							final long id = (long) Long.parseLong(res[1]);
							
							AsyncCallback<Match> callback = new AsyncCallback<Match>(){
								@Override
								public void onFailure(Throwable caught) {
									String toD=dropdown.getValue(select);
									SelectGame = id;
									presenter.setId(SelectGame);			
									LinkedList<String> offline= presenter.GetMatch();
									for(String it:offline){
										if(toD.split(" ")[1].equals(it.split(" ")[1])) {
											String[] splits=it.split(" ");
											presenter.setState(Presenter.Deserialize(splits[2]));
											presenter.setWhite(splits[3]);
											presenter.setBlack(splits[4]);
											if(splits[0].equals(splits[3])) presenter.setMail(splits[4]);
											else presenter.setMail(splits[3]);
										}
									}
								}
								@Override
								public void onSuccess(Match result) {
									State st =Presenter.Deserialize(result.getState());
									presenter.setState(st);
									presenter.setBlack(result.getBlack());
									presenter.setWhite(result.getWhite());
									presenter.setMail(loginInfo.getEmailAddress());
									SelectGame =id;
									presenter.setId(SelectGame);
									if(result.isWhiteTurn()&&st.getGameResult()==null){
										graphics.setGameStatus(result.getWhite()+myMessage.wturn());
									}else if(st.getGameResult()==null) {
										graphics.setGameStatus(result.getBlack()+myMessage.bturn());
									}
														
								}};
							loginService.LoadById(id, callback);	
							
						}});
				    delete.addClickHandler(new ClickHandler(){

						@Override
						public void onClick(ClickEvent event) {
							final int select= dropdown.getSelectedIndex();
							String text = dropdown.getItemText(select);
							String[] res=text.split(" "); 
							long id = (long) Long.parseLong(res[1]);
							AsyncCallback<String> callback = new AsyncCallback<String>(){
								@Override
								public void onFailure(Throwable caught) {
									Window.alert("Delete Game fails");
								}
								@Override
								public void onSuccess(String result) {
									presenter.DeleteById(result);
									dropdown.removeItem(select);
									SelectGame =0;
									presenter.setState(state);
					
									graphics.setGameStatus(myMessage.gamestatus());
								}};
							loginService.Delete(loginInfo.getEmailAddress(), id, callback);	
							
						}});
				    playwith.addClickHandler(new ClickHandler(){

						@Override
						public void onClick(ClickEvent event) {
							String op = mailbox.getText();
							AsyncCallback<Match> callback = new AsyncCallback<Match>(){
								@Override
								public void onFailure(Throwable caught) {
									Window.alert("Create Game fails");
								}
								@Override
								public void onSuccess(Match mt) {
									if (mt==null){
										Window.alert("Fail to create game");
										return;
									}
									presenter.setBlack(mt.getBlack());
									presenter.setWhite(mt.getWhite());
									if(mt.getBlack()==loginInfo.getEmailAddress()) {
										presenter.addMatch(mt.getMatchid(), mt.getWhite()+" "+mt.getMatchid()+" "+mt.getState()+" "+mt.getWhite()+" "+mt.getBlack());
										dropdown.addItem(mt.getWhite()+" "+mt.getMatchid());
									}
							    	else if(mt.getWhite()==loginInfo.getEmailAddress()) {
							    		presenter.addMatch(mt.getMatchid(),mt.getBlack()+" "+mt.getMatchid()+" "+mt.getState()+" "+mt.getWhite()+" "+mt.getBlack());
							    		dropdown.addItem(mt.getBlack()+" "+mt.getMatchid());
							    	}
									presenter.setState(Presenter.Deserialize(mt.getState()));
									graphics.setGameStatus(mt.getWhite()+myMessage.wturn());
								
									SelectGame=mt.getMatchid();
									presenter.setId(SelectGame);
									mailbox.setText("");
								}};
							loginService.GameWith(loginInfo.getEmailAddress(), op, callback);
							
							
						}});
				    
				    fmatch.addClickHandler(new ClickHandler(){

						@Override
						public void onClick(ClickEvent event) {
							AsyncCallback<Match> callback = new AsyncCallback<Match>(){
								@Override
								public void onFailure(Throwable caught) {
									Window.alert("Search Game fails, please check your connection!");
								}
								@Override
								public void onSuccess(Match mt) {
									if (mt==null){
										Window.alert("No one is online!");
										return;
									}
									presenter.setBlack(mt.getBlack());
									presenter.setWhite(mt.getWhite());
									if(mt.getBlack()==loginInfo.getEmailAddress()) {
										presenter.addMatch(mt.getMatchid(), mt.getWhite()+" "+mt.getMatchid()+" "+mt.getState()+" "+mt.getWhite()+" "+mt.getBlack());
										dropdown.addItem(mt.getWhite()+" "+mt.getMatchid());
									}
							    	else if(mt.getWhite()==loginInfo.getEmailAddress()) {
							    		presenter.addMatch(mt.getMatchid(),mt.getBlack()+" "+mt.getMatchid()+" "+mt.getState()+" "+mt.getWhite()+" "+mt.getBlack());
							    		dropdown.addItem(mt.getBlack()+" "+mt.getMatchid());
							    	}
									presenter.setState(Presenter.Deserialize(mt.getState()));
									graphics.setGameStatus(mt.getWhite()+myMessage.wturn());
									SelectGame=mt.getMatchid();
								
									presenter.setId(SelectGame);
									mailbox.setText("");
								}};
							loginService.SearchGame(loginInfo.getEmailAddress(),callback);
							
						}});
				    
				 
				 
				    if(par==null) loginService.login("http://kanppa.appspot.com/jiangfengchen.html?ckeyid="+xx, new AsyncCallback<LoginInfo>() {
				    	public void onFailure(Throwable error) {
				    	  Window.alert("If you are online please reload, or local storage will be used");    
				    	  dropdown.clear();
						     LinkedList<String> offline=presenter.GetMatch();
						     for(String it:offline){
						    	 dropdown.addItem(it.split(" ")[0]+" "+it.split(" ")[1]);
						     }
				      }

				      public void onSuccess(LoginInfo result) {
				        loginInfo = result;	      
				        if(loginInfo.isLoggedIn()) {				          
				            presenter.ClearMatch();      
				        	signOutLink.setHref(loginInfo.getLogoutUrl());
				        	presenter.setMail(loginInfo.getEmailAddress());
				        	graphics.setEmail(loginInfo.getEmailAddress());
				        	graphics.setNick(loginInfo.getNickname());
				         	graphics.setRank(((int) loginInfo.getRank())+""); 
				        		Socket socket =new ChannelFactoryImpl().createChannel(loginInfo.getToken()).open(new SocketListener() {
				        				    @Override
				        				    public void onOpen() {
				        				    	AsyncCallback<LinkedList<Match>> callback =new AsyncCallback<LinkedList<Match>>(){
				        						    public void onSuccess(LinkedList<Match> result) {
				        						    		dropdown.clear();
				        						    		
				        								    for(Match mt:result){ 		
				        								    		if(mt.getBlack()==loginInfo.getEmailAddress()) {
				        								    			dropdown.addItem(mt.getWhite()+" "+mt.getMatchid());
				        								    			presenter.addMatch(mt.getMatchid(),mt.getWhite()+" "+mt.getMatchid()+" "+mt.getState()+" "+mt.getWhite()+" "+mt.getBlack());
				        								    		}
					        								    	else if(mt.getWhite()==loginInfo.getEmailAddress()){
					        								    		dropdown.addItem(mt.getBlack()+" "+mt.getMatchid());
					        								    		presenter.addMatch(mt.getMatchid(),mt.getBlack()+" "+mt.getMatchid()+" "+mt.getState()+" "+mt.getWhite()+" "+mt.getBlack());
					        								    	}
				        								    	
				        								    	}
				        							
				        								    }

				        								    public void onFailure(Throwable caught) {
				        								     dropdown.clear();
				        								     LinkedList<String> offline=presenter.GetMatch();
				        								     for(String it:offline){
				        								    	 dropdown.addItem(it.split(" ")[0]+" "+it.split(" ")[1]);
				        								     }
				        								    }
				        								};;
				        				    	 loginService.LoadGame(loginInfo.getEmailAddress(), callback);
				        				    }
				        				    @Override
				        				    public void onMessage(String message) {
				        				     String[] dis = message.split(",");
				        				     int kind =Integer.parseInt(dis[0]);
				        				     if(kind==0) {
				        				    	 String[] ms = dis[1].split(" ");
				        				    	 dropdown.addItem(ms[0]+" "+ms[1]);
				        				    	 presenter.addMatch(Long.parseLong(ms[1]), dis[1]);
				        				     }
				        				     else if(kind==1) {
				        				    	 long id = Long.parseLong(dis[4]);
				        				    	 presenter.ChangeState(id, dis[1]);
				        				    	 if(id!=SelectGame) return;
				        				    	 State st= Presenter.Deserialize(dis[1]);
				        				    	 presenter.setState(st);
				        				    	 if(st.getGameResult()==null){
				        				    		 if(dis[3]=="b")graphics.setGameStatus(dis[2]+myMessage.bturn());
				        				    		 else graphics.setGameStatus(dis[2]+myMessage.wturn());
				        				    	 }
				        				     }else if(kind==2){
				        				    	 graphics.setRank(dis[1]);
				        				     }
				        				    }
				        				    @Override
				        				    public void onError(ChannelError error) {
				        				    }
				        				    @Override
				        				    public void onClose() {
				        				   
				        				    }
				        				  });
				        } else {
				     	   if(par==null)  loginService.SaveContact(xx, getlist(conList), new AsyncCallback<Void>(){

								@Override
								public void onFailure(Throwable caught) {
									Window.alert("Can't save contacts");
								}

								@Override
								public void onSuccess(Void result) {
								}});
				         LoginPanel lg=  new LoginPanel(loginInfo.getLoginUrl());
				         lg.center();
				        }
				      }
				    });
				    else loginService.login("http://kanppa.appspot.com/jiangfengchen.html?ckeyid="+par, new AsyncCallback<LoginInfo>() {
				    	public void onFailure(Throwable error) {
					    	  Window.alert("If you are online please reload, or local storage will be used");    
					    	  dropdown.clear();
							     LinkedList<String> offline=presenter.GetMatch();
							     for(String it:offline){
							    	 dropdown.addItem(it.split(" ")[0]+" "+it.split(" ")[1]);
							     }
					      }

					      public void onSuccess(LoginInfo result) {
					
					        loginInfo = result;	      
					        if(loginInfo.isLoggedIn()) {
					            long kid = Long.parseLong(par);
					            loginService.LoadContact(kid, new AsyncCallback<String>(){

									@Override
									public void onFailure(Throwable caught) {
										Window.alert("failed to retrieve contact");
										
									}

									@Override
									public void onSuccess(String result) {
										String[] toAdd=result.split(",");
										for(String it:toAdd) oracle.add(it);
										mailbox.refreshSuggestionList();
										
									}});
					            presenter.ClearMatch();      
					        	signOutLink.setHref(loginInfo.getLogoutUrl());
					        	presenter.setMail(loginInfo.getEmailAddress());
					        	graphics.setEmail(loginInfo.getEmailAddress());
					        	graphics.setNick(loginInfo.getNickname());
					        	graphics.setRank(((int) loginInfo.getRank())+""); 
					        		Socket socket =new ChannelFactoryImpl().createChannel(loginInfo.getToken()).open(new SocketListener() {
					        				    @Override
					        				    public void onOpen() {
					        				    	AsyncCallback<LinkedList<Match>> callback =new AsyncCallback<LinkedList<Match>>(){
					        						    public void onSuccess(LinkedList<Match> result) {
					        						    		dropdown.clear();
					        						    		
					        								    for(Match mt:result){ 		
					        								    		if(mt.getBlack()==loginInfo.getEmailAddress()) {
					        								    			dropdown.addItem(mt.getWhite()+" "+mt.getMatchid());
					        								    			presenter.addMatch(mt.getMatchid(),mt.getWhite()+" "+mt.getMatchid()+" "+mt.getState()+" "+mt.getWhite()+" "+mt.getBlack());
					        								    		}
						        								    	else if(mt.getWhite()==loginInfo.getEmailAddress()){
						        								    		dropdown.addItem(mt.getBlack()+" "+mt.getMatchid());
						        								    		presenter.addMatch(mt.getMatchid(),mt.getBlack()+" "+mt.getMatchid()+" "+mt.getState()+" "+mt.getWhite()+" "+mt.getBlack());
						        								    	}
					        								    	
					        								    	}
					        							
					        								    }

					        								    public void onFailure(Throwable caught) {
					        								     dropdown.clear();
					        								     LinkedList<String> offline=presenter.GetMatch();
					        								     for(String it:offline){
					        								    	 dropdown.addItem(it.split(" ")[0]+" "+it.split(" ")[1]);
					        								     }
					        								    }
					        								};;
					        				    	 loginService.LoadGame(loginInfo.getEmailAddress(), callback);
					        				    }
					        				    @Override
					        				    public void onMessage(String message) {
					        				     String[] dis = message.split(",");
					        				     int kind =Integer.parseInt(dis[0]);
					        				     if(kind==0) {
					        				    	 String[] ms = dis[1].split(" ");
					        				    	 dropdown.addItem(ms[0]+" "+ms[1]);
					        				    	 presenter.addMatch(Long.parseLong(ms[1]), dis[1]);
					        				     }
					        				     else if(kind==1) {
					        				    	 long id = Long.parseLong(dis[4]);
					        				    	 presenter.ChangeState(id, dis[1]);
					        				    	 if(id!=SelectGame) return;
					        				    	 State st= Presenter.Deserialize(dis[1]);
					        				    	 presenter.setState(st);
					        				    	 if(st.getGameResult()==null){
					        				    		 if(dis[3]=="b")graphics.setGameStatus(dis[2]+myMessage.bturn());
					        				    		 else graphics.setGameStatus(dis[2]+myMessage.wturn());
					        				    	 }
					        				     }else if(kind==2){
					        				    	 graphics.setRank(dis[1]);
					        				     }
					        				    }
					        				    @Override
					        				    public void onError(ChannelError error) {
					        				    }
					        				    @Override
					        				    public void onClose() {
					        				   
					        				    }
					        				  });
					        } else {
					         LoginPanel lg=  new LoginPanel(loginInfo.getLoginUrl());
					         lg.center();
					        }
					      }
					    });
				    
				ai.addClickHandler(new ClickHandler(){

					@Override
					public void onClick(ClickEvent event) {
						presenter.setState(new State());
						presenter.setId(0);
						presenter.setMail("Your");
						presenter.setWhite("Your");
						presenter.setBlack("AI");
						graphics.setGameStatus("Playing against computer");
						
					}});
				 
				footer.add(left);
				footer.add(mailbox);
			    footer.add(rbuttons);
				footer.setSpacing(5);
			    RootPanel.get().add(footer);
				footer.add(signOutLink);
				presenter.setState(state);
			    graphics.setGameStatus(myMessage.gamestatus());
			    RootPanel.get().setPixelSize(500, 710);
			    
			  

			  

				
			}});
		phoneGap.addHandler(new PhoneGapTimeoutHandler(){

			@Override
			public void onPhoneGapTimeout(PhoneGapTimeoutEvent event) {
				// TODO Auto-generated method stub
				
			}});
		phoneGap.initializePhoneGap();
		
		
		
  }
	  
  public boolean isGmail(String email){
		String[] slice = email.split("@");
		if(slice[1].equals("gmail.com")) return true;
		return false;
	}
  public String getlist(LinkedList<String> a){
	  String result="";
	  for(int i=0;i<a.size();i++){
		  if(i==0) result+=a.get(i);
		  else result+=(","+a.get(i));
		  
	  }
	  return result;
  }

 
  }

 

