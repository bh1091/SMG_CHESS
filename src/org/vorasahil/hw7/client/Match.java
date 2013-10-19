package org.vorasahil.hw7.client;

import java.util.Date;
import org.shared.chess.Color;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class Match {

	@Id Long id;
	Key<Player> player1;
	Key<Player> player2;
	String state;
	String pid1;
	String pid2;
	Date startDate;
	boolean isSinglePlayer;
	public Match() {}
	
	public Match(Key<Player> white, Key<Player> black, String state, String pid1,String pid2, Date date) {
		this.player1 = white;
		this.player2 = black;
		this.pid1=pid1;
		this.pid2=pid2;
		this.state = state;
		this.startDate=date;
		this.isSinglePlayer=false;
	}
	
	public boolean getIsSinglePlayer(){
		return isSinglePlayer;
	}
	
	public void setIsSinglePlayer(boolean isSinglePlayer){
		this.isSinglePlayer = isSinglePlayer;
	}
	
	public void setDate(Date date){
		this.startDate=date;	
	}
	
	public Date getStartDate(Date date){
		return startDate;
	}
	
	public String getSerialString(){
		try{
		String s=pid1+"%%%%%"+pid2+"%%%%%"+state+"%%%%%"+id+ "%%%%%" + startDate.getTime() + "%%%%%"+ isSinglePlayer;
		return s;
		}
		catch(NullPointerException e){
			return null;
		}
	}
	
	public Long getId() {
		return id;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	public Key<Player> getOpponent(Key<Player> player) {
		if (player.equals(player1))
			return player2;
		else
			return player1;
	}
	
	public Color getPlayerColor(Key<Player> player) {
		return player.equals(player1)?Color.WHITE:Color.BLACK;
	}

	public boolean isPlayerWhite(Key<Player> player){
		return player.equals(player1)?true:false;
	}
	
	public String getPid1() {
		return pid1;
	}

	public void setPid1(String pid1) {
		this.pid1 = pid1;
	}

	public String getPid2() {
		return pid2;
	}

	public void setPid2(String pid2) {
		this.pid2 = pid2;
	}
	
}
