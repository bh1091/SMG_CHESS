package org.bohuang.hw7;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
@Entity
public class Player implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Index
	private String email;
	
	private double rank ;
	private double RD ;
	public double getRD() {
		return RD;
	}

	public void setRD(double rD) {
		RD = rD;
	}
	private Date lastPlayed;
	
	public Player(String id,int rank){
		this.email = id;
		this.rank = rank;
		this.lastPlayed = new Date();
		this.RD = 350;
	}
	
	public double getRank() {
		return rank;
	}
	public void setRank(double rank) {
		this.rank = rank;
	}
	public Date getLastPlayed() {
		return lastPlayed;
	}
	public void setLastPlayed(Date lastPlayed) {
		this.lastPlayed = lastPlayed;
	}
	
	
	
	Player(){}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}


	
	

}
