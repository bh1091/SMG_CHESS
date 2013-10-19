package org.leozis.hw9;

import java.util.Date;

public class Timer {
	private long startTime;
	private int duration;
	
	//duration in milliseconds
	public Timer(int duration){
		this.startTime = new Date().getTime();
		this.duration = duration;
	}
	
	public boolean didTimeout(){
		return new Date().getTime() > startTime + duration;
	}
}
