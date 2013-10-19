package org.yuanjia.hw7;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class Player implements IsSerializable{

	@Id 
	String userEmail;
	
	String userName;
	List<String> tokens = new ArrayList<String>();
	List<String> matches = new ArrayList<String>();
	public int rank = 1500;
	public double RD = 350;
	public long lastGameTime = 0;
	
	public Player(){}
	
	public Player(String userEmail, String userName){
		this.userEmail = userEmail;
		this.userName = userName;
	}
	
	
	public List<String> getTokens(){
		return tokens;
	}
	
	public List<String> getMatches(){
		return matches;
	}
	
	public void addToken(String token){
		this.tokens.add(token);
	}
	

	
	public String getUserEmail(){
		return userEmail;
	}
}
