package com.example.golfapp.model;

public class Tee {
	
	private int distance;
	private boolean mensTee;
	private String name;
	
	
	public int getDistance() {
		return distance;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}
	public boolean isMensTee() {
		return mensTee;
	}
	public void setMensTee(boolean mensTee) {
		this.mensTee = mensTee;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
