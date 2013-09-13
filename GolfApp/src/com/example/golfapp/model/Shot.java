package com.example.golfapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public abstract class Shot implements Parcelable {
	private String club;
	private String course;
	private int hole;
	
	public Shot()
	{
		this("","",0);
	}
	public Shot(Parcel in)
	{
		readFromParcel(in);
	}
	public void readFromParcel(Parcel in)
	{
		club = in.readString();
		course = in.readString();
		hole = in.readInt();
	}
	public Shot(String club, String course, int hole)
	{
		this.club = club;
		this.course = course;
		this.hole = hole;
	}
	public String getClub() {
		return club;
	}

	public void setClub(String club) {
		this.club = club;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public int getHole() {
		return hole;
	}

	public void setHole(int hole) {
		this.hole = hole;
	}

	public String toString()
	{
		String result="";
		result+="Club: "+club+" Course: "+course+" Hole: "+hole;
		return result;
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return hashCode();
	}
	@Override
	public void writeToParcel(Parcel dest, int arg1) {
		dest.writeString(club);
		dest.writeString(course);
		dest.writeInt(hole);
	}

	
}
