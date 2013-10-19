package org.bohuang.hw8;

public class RankCalculator {
	
	final double RD = 350;
	final double tRD = 50;
	final double constantC = 0.9;
	final double constantQ = 0.00575646273;
	
	public double calculateRD(int days,double tRD){
		double resultRD = 0;
		resultRD = Math.min(Math.sqrt((tRD*tRD+constantC*constantC*days)), RD);
		return resultRD;
	}
	
	public double gRD(double RD){
		double result;
		result = 1/Math.sqrt(1+(3*Math.pow(constantC, 2)*Math.pow(RD, 2)/Math.pow(Math.PI, 2)));
		return result;
	}
	
	public double getE(double gRD,double diff){
		double result;
		result = 1/(1+Math.pow(10, gRD*diff/-400));
		return result;
		
	}
	
	public double getd2(double E,double gRD){
		double result;
		result = 1/(Math.pow(constantQ, 2)*Math.pow(gRD, 2)*E*(1-E));
		return result;
	}
	
	public double Calculate(double initial,double opponent,double tRD,int days,double win){
		
		double result ;
		double r0 = initial;
		double RD = calculateRD(days,tRD);
		double gRD = gRD(RD);
		double E = getE(gRD,initial-opponent);
		double d2 = getd2(E,gRD);
		
		result = r0 + constantQ*gRD*(win-E)/(1/Math.pow(RD, 2)+1/Math.pow(d2, 2));
		
		
		return result;
		
	}

}
