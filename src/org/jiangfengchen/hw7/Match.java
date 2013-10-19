package org.jiangfengchen.hw7;

import java.io.Serializable;
import java.util.Date;


import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;


@Entity
public class Match implements Serializable{
	@Id long matchid;
	String black="";
	String white="";
	String state="";
	Date lastplayed;
	boolean isWhiteTurn=true;
	boolean deleteW=false;
	boolean deleteB=false;
	private Match(){}
	public Match(String w,String b,long id){
		this.state="Wtttt$$$$RNBQKBNRPPPPPPPP$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$pppppppprnbqkbnr0";
		this.white=w;
		this.black=b;
		this.matchid = id;
		this.isWhiteTurn=true;
		this.deleteB=false;
		this.deleteW=false;
		this.lastplayed=new Date();
	}
	public long getMatchid() {
		return matchid;
	}
	public void setMatchid(long matchid) {
		this.matchid = matchid;
	}
	public String getBlack() {
		return black;
	}
	public void setBlack(String black) {
		this.black = black;
	}
	public String getWhite() {
		return white;
	}
	public void setWhite(String white) {
		this.white = white;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public boolean isWhiteTurn() {
		return isWhiteTurn;
	}
	public void setWhiteTurn(boolean isWhiteTurn) {
		this.isWhiteTurn = isWhiteTurn;
	}
	public boolean isDeleteW() {
		return deleteW;
	}
	public void setDeleteW(boolean deleteW) {
		this.deleteW = deleteW;
	}
	public boolean isDeleteB() {
		return deleteB;
	}
	public void setDeleteB(boolean deleteB) {
		this.deleteB = deleteB;
	}
	public Date getLastplayed() {
		return lastplayed;
	}
	public void setLastplayed(Date lastplayed) {
		this.lastplayed = lastplayed;
	}

	
}
