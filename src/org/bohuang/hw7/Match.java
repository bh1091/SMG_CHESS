package org.bohuang.hw7;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;


import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class Match implements Serializable{

	
	
	@Id	private Long matchid = Long.MAX_VALUE;	
	
	private String state;
	private String whiteId;
	private String blackId;
	
	private Date startDate;
	
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	boolean DeleteByWhite;
	boolean DeleteByBlack;	

	public Long getId() {
		return matchid;
	}

	public void setId(Long id) {
		this.matchid = id;
	}


	public String getWhiteId() {
		return whiteId;
	}

	public void setWhiteId(String whiteId) {
		this.whiteId = whiteId;
	}

	public String getBlackId() {
		return blackId;
	}

	public void setBlackId(String blackId) {
		this.blackId = blackId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Match(){}
	public Match(String white, String black, long id) {
		super();
		this.matchid = id;
		this.whiteId = white;
		this.blackId = black;
		this.state = "RKBQWBKRPPPPPPPP________________________________pppppppprkbqwbkrwtttt_____00_";
		this.DeleteByBlack = false;
		this.DeleteByWhite = false;
		this.startDate = new Date();
	}

	public boolean isDeleteByWhite() {
		return DeleteByWhite;
	}

	public void setDeleteByWhite(boolean deleteByWhite) {
		DeleteByWhite = deleteByWhite;
	}

	public boolean isDeleteByBlack() {
		return DeleteByBlack;
	}

	public void setDeleteByBlack(boolean deleteByBlack) {
		DeleteByBlack = deleteByBlack;
	}
	
	public boolean hasId(){
		if(this.matchid==0){
			return false;
		}else{
			return true;
		}
	}

}
