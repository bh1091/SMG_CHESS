package org.ashishmanral.hw7;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import org.ashishmanral.hw7.Match;


import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.condition.IfTrue;

@Entity
public class Player {
  @Id
  private String email;
  private Set<Key<Match>> setOfMatches = new HashSet<Key<Match>>();;
  private int rank = 1500;
  private int RD = 350;
 
  private static Logger logger = Logger.getLogger(MultiplayerChessServiceImpl.class.toString());
  
  public Player(){}
  
  public Player(String email) {
    this.email = email;
  }
  
  public String getEmail() {
    return email;
  }
  
  public Set<Key<Match>> getSetOfMatches() {
    return setOfMatches;
  }
  
  public int getRank(){
	  return rank;
  }
  
  public int getRD(){
	  return RD;
  }
  
  public void addMatchToSetOfMatches(Key<Match> match) {
	  setOfMatches.add(match);
  }
  
  public void deleteMatchFromSetOfMatches(Key<Match> match) {
	  setOfMatches.remove(match);
  }
  
  public boolean isPlayingMatch(Key<Match> match) {
    return setOfMatches.contains(match);
  }
  
  public void setRank(double rank){
	  this.rank = (int)rank;
  }
  
  public void setRD(double RD){
	  this.RD = (int)RD;
  }
  
}
