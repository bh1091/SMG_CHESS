package org.vorasahil.hw7.client;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.condition.IfTrue;

@Entity
public class Player {
	@Id
	String email;
	Set<Key<Match>> matches;
	@Index(IfTrue.class) boolean matchMe;
	boolean connected;
	int rank=1500;
	
	public Player() {
		this.email = "";
		matches = new HashSet<Key<Match>>();
		matchMe = false;
		rank=1500;
	}

	public int getRank(){
		return rank;
	}
	
	public void setRank(int rank){
		this.rank=rank;
	}
	
	public void setConnected(boolean isC){
		connected=isC;
	}

	public boolean getConnected(){
		return connected;
	}
	
	public Player(String email) {
		this.email = email;
		matches = new HashSet<Key<Match>>();
		matchMe = false;
	}

	public String getEmail() {
		return email;
	}

	public Set<Key<Match>> getMatches() {
		return Collections.unmodifiableSet(matches);
	}

	public void setMatchMe(boolean value) {
		matchMe = value;
	}

	public int numberOfMatches() {
		return matches.size();
	}

	public boolean containsMatch(Key<Match> match) {
		return matches.contains(match);
	}

	public void addMatch(Key<Match> match) {
		matches.add(match);
	}

	public void removeMatch(Key<Match> match) {
		matches.remove(match);
	}
}
