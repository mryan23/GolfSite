package com.example.golfapp.model;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class Hole implements Parcelable
{

	private int par;
	private int handicap;
	private int numPlayers;
	private int[] scores;
	private int yardage;
	private int number;
	private int putts;
	private boolean fairway, green;
	private GPSPoints points;
	private ArrayList<Shot> shots = new ArrayList<Shot>();

	@Override
	public int describeContents()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags)
	{
		out.writeInt(par);
		out.writeInt(handicap);
		out.writeInt(numPlayers);
		out.writeIntArray(scores);
		out.writeInt(yardage);
		out.writeInt(number);
		out.writeInt(putts);
		out.writeInt(fairway==true?1:0);
		out.writeInt(green==true?1:0);
		out.writeParcelable(points, flags);
		Shot[] shotArray= new Shot[shots.size()];
		for(int i = 0; i < shots.size(); i++)
		{
			shotArray[i]=shots.get(i);
		}
		out.writeParcelableArray(shotArray, flags);
		
	}
	
	public Hole(Parcel in)
	{
		par = in.readInt();
		handicap = in.readInt();
		numPlayers = in.readInt();
		scores = new int[numPlayers];
		in.readIntArray(scores);
		yardage = in.readInt();
		number = in.readInt();
		putts = in.readInt();
		fairway = in.readInt()==1;
		green = in.readInt() == 1;
		Parcelable pointsParcel = in.readParcelable(GPSPoints.class.getClassLoader());
		if(pointsParcel instanceof GPSPoints)
			points = (GPSPoints)pointsParcel;
		Parcelable[] shotArray = in.readParcelableArray(Shot.class.getClassLoader());
		shots = new ArrayList<Shot>();
		for(Parcelable p: shotArray)
		{
			if(p instanceof Shot)
				shots.add((Shot)p);
		}
		
	}
	
	public static final Parcelable.Creator<Hole> CREATOR = new Parcelable.Creator<Hole>() {  
	    
        public Hole createFromParcel(Parcel in) {  
            return new Hole(in);  
        }  
   
        public Hole[] newArray(int size) {  
            return new Hole[size];  
        }  
          
    };


	public Hole()
	{
		par = 4;
		handicap = 1;
		numPlayers = 1;
		yardage = 300;
		number = 1;
		scores = new int[numPlayers];
		for (int i = 1; i < numPlayers; i++)
			scores[i] = par;
		scores[0] = 0;
		putts = 0;
		fairway = false;
		green = false;
	}

	public Hole(int num, int p, int h, int np, int y)
	{
		number = num;
		par = p;
		handicap = h;
		numPlayers = np;
		yardage = y;
		scores = new int[numPlayers];
		for (int i = 1; i < numPlayers; i++)
			scores[i] = par;
		scores[0] = 0;
		putts = 0;
		fairway = false;
		green = false;
	}

	
	public GPSPoints getPoints()
	{
		return points;
	}

	public void setPoints(GPSPoints points)
	{
		this.points = points;
	}
	public ArrayList<Shot> getShots()
	{
		return shots;
	}

	public void addShot(Shot shot)
	{
		shots.add(shot);
	}

	public boolean hitFairway()
	{
		return fairway;
	}

	public void setFairway(boolean fairway)
	{
		this.fairway = fairway;
	}

	public boolean hitGreen()
	{
		return green;
	}

	public void setGreen(boolean green)
	{
		this.green = green;
	}

	public String toString()
	{
		String result = "";
		result += "Hole " + number + " Par " + par + " Hdcp " + handicap + " "
				+ yardage + " yards.";
		return result;
	}

	public int getPutts()
	{
		return putts;
	}

	public void setPutts(int p)
	{
		putts = p;
	}

	public String holeString()
	{
		return "Hole " + number;
	}

	public void setScore(int playerNumber, int score)
	{
		scores[playerNumber] = score;
	}

	public int getScore(int playerNumber)
	{
		return scores[playerNumber];
	}

	public int getNumber()
	{
		return number;
	}

	public void setNumber(int n)
	{
		number = n;
	}

	public int getPar()
	{
		return par;
	}

	public void setPar(int par)
	{
		this.par = par;
	}

	public int getHandicap()
	{
		return handicap;
	}

	public void setHandicap(int handicap)
	{
		this.handicap = handicap;
	}

	public int getNumPlayers()
	{
		return numPlayers;
	}

	public void setNumPlayers(int numPlayers)
	{
		this.numPlayers = numPlayers;
	}

	public int[] getScores()
	{
		return scores;
	}

	public void setScores(int[] scores)
	{
		this.scores = scores;
	}

	public int getYardage()
	{
		return yardage;
	}

	public void setYardage(int yardage)
	{
		this.yardage = yardage;
	}

	

}
