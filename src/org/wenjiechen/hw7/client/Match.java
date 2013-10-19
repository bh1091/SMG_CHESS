package org.wenjiechen.hw7.client;

import java.util.Date;

import org.shared.chess.Color;
import org.wenjiechen.hw7.client.Player;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class Match {
	@Id Long matchId;
	Key<Player> whitePlayer;
	Key<Player> blackPlayer;
	Date date;
	String	state = "";
	
	public Match(){};
	
	public Match(Key<Player> whitePlayer, Key<Player> blackPlayer, String state){
		this.whitePlayer = whitePlayer;
		this.blackPlayer = blackPlayer;
		this.state = state;
		this.date = new Date();
	}
	
	public Date getDate(){
		return date;
	}
	
	public Long getMatchId(){
		return matchId;
	}
	
	public Key<Player> getWhitePlayer(){
		return whitePlayer;		
	}
	
	public Key<Player> getBlackPlayer(){
		return blackPlayer;
	}
	
	public String getState(){
		return state;
	}
		
	public void setWhitePlayer(Key<Player> white){
		whitePlayer = white;
	}
	
	public void setBlackPlayer(Key<Player> black){
		blackPlayer = black;
	}
	
	public void setState(String state){
		this.state = state;		
	}
		
	public Key<Player> getOtherPlayer(Key<Player> currentPlayer) {
		if (currentPlayer.equals(whitePlayer))
			return blackPlayer;
		else
			return whitePlayer;
	}
	
	public Color getColor(Key<Player> curPlayer){
		if(curPlayer.equals(whitePlayer)){
			return Color.WHITE;
		}
		else{
			return Color.BLACK;
		}
	}
}
