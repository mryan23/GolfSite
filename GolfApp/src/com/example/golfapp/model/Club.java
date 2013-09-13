package com.example.golfapp.model;

public class Club {
	private String name;
	private boolean enabled;
	
	public Club(String n, boolean en)
	{
		enabled = en;
		name = n;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
