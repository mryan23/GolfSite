package com.example.golfapp.model.stats;

import java.text.DecimalFormat;

public class HoleStats {
	private double averageScore;
	private double averagePutts;
	private int possibleFairways;
	private int fairwaysHit;
	private int greensHit;
	private int timesPlayed;
	private int par;
	

	DecimalFormat averageFormat = new DecimalFormat("##.##");
	DecimalFormat percentFormat = new DecimalFormat("##.##%");
	private String name;
	
	public HoleStats(String name, double averageScore, double averagePutts, int fairways, int greens,int possible, int played, int par)
	{
		this.name = name;
		this.averageScore=averageScore;
		this.averagePutts = averagePutts;
		this.fairwaysHit=fairways;
		this.greensHit = greens;
		this.possibleFairways=possible;
		this.timesPlayed = played;
		this.par = par;
	}

	public int getPar() {
		return par;
	}

	public void setPar(int par) {
		this.par = par;
	}

	public double getAverageScore() {
		return averageScore;
	}
	
	public String getAverageScoreString()
	{
		return averageFormat.format(averageScore);
	}

	public void setAverageScore(double averageScore) {
		this.averageScore = averageScore;
	}

	public double getAveragePutts() {
		return averagePutts;
	}
	public String getAveragePuttString()
	{
		return averageFormat.format(averagePutts);
	}

	public void setAveragePutts(double averagePutts) {
		this.averagePutts = averagePutts;
	}

	public int getTimesPlayed() {
		return timesPlayed;
	}

	public void setTimesPlayed(int timesPlayed) {
		this.timesPlayed = timesPlayed;
	}

	public int getPossibleFairways() {
		return possibleFairways;
	}

	public void setPossibleFairways(int possibleFairways) {
		this.possibleFairways = possibleFairways;
	}

	public int getFairwaysHit() {
		return fairwaysHit;
	}
	
	public String getFairwayPercentString()
	{
		if(par == 3)
			return "N/A";
		return percentFormat.format(fairwaysHit*1.0/possibleFairways);
	}

	public void setFairwaysHit(int fairwaysHit) {
		this.fairwaysHit = fairwaysHit;
	}

	public int getGreensHit() {
		return greensHit;
	}
	
	public String getGreenPercentString()
	{
		return percentFormat.format(greensHit*1.0/getTimesPlayed());
	}

	public void setGreensHit(int greensHit) {
		this.greensHit = greensHit;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
