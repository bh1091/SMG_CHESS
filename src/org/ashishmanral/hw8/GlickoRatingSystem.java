package org.ashishmanral.hw8;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.ashishmanral.hw7.MultiplayerChessServiceImpl;

/**
 * GlickoRatingSystem.java
 * @author Ashish
 * This class calculates the new RD and rank for the player.
 */
public class GlickoRatingSystem {

	private static final double c = Math.sqrt((Math.pow(350,2) - Math.pow(50,2))/100);
	private static final double q = 0.00575646273;

	private static Logger logger = Logger.getLogger(MultiplayerChessServiceImpl.class.toString());
	  
	public static double findRating(double r0, double RD, double s, double ri, double RDi){
		return r0+((q/((1/(RD*RD))+(1/d2(RDi, r0, ri))))*g(RDi)*(s-E(RDi, r0, ri)));
	}
	
	public static double g(double RDi){
		return 1/Math.sqrt(1+(3*q*q*RDi*RDi/Math.pow(Math.PI, 2)));
	}
	
	public static double E(double RDi, double r0, double ri){
		return 1/(1+Math.pow(10, ((g(RDi)*(r0-ri))/-400)));
	}
	
	public static double d2(double RDi, double r0, double ri){
		return 1/(q*q*g(RDi)*g(RDi)*E(RDi, r0, ri)*(1-E(RDi, r0, ri)));
	}
	
	public static double RD(double RD0, int t){
		return Math.min(Math.sqrt((RD0*RD0)+(c*c*t)), 350);
	}
	
}
