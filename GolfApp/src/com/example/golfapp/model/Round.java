package com.example.golfapp.model;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.golfapp.xml.CourseParser;

public class Round implements Parcelable
{
	private String[] players = new String[0];
	private int numPlayers = 1;
	private int numHoles = 18;
	private File courseFile;
	private String courseName;
	private int startingHole = 1;
	private Date date;
	private String tee;
	private Hole[] holes;

	public Round()
	{

	}

	public Round(Parcel in)
	{
		numPlayers = in.readInt();
		players = new String[numPlayers - 1];
		in.readStringArray(players);
		numHoles = in.readInt();
		courseFile = new File(in.readString());
		courseName = in.readString();
		startingHole = in.readInt();
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy");
		try
		{
			date = formatter.parse(in.readString());
		} catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tee = in.readString();
		Parcelable[] parcelArray = in.readParcelableArray(Hole.class.getClassLoader());
		holes = new Hole[parcelArray.length];
		for(int i = 0; i < parcelArray.length; i++)
		{
			if(parcelArray[i] instanceof Hole)
				holes[i]=(Hole) parcelArray[i];
		}
	}

	@Override
	public int describeContents()
	{
		// TODO Auto-generated method stub
		return this.hashCode();
	}

	@Override
	public void writeToParcel(Parcel out, int arg1)
	{
		out.writeInt(numPlayers);
		out.writeStringArray(players);		
		out.writeInt(numHoles);
		out.writeString(courseFile.getAbsolutePath());
		out.writeString(courseName);
		out.writeInt(startingHole);
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy");
		out.writeString(formatter.format(date));
		out.writeString(tee);
		out.writeParcelableArray(holes, arg1);

	}
	
	public static final Parcelable.Creator<Round> CREATOR = new Parcelable.Creator<Round>() {  
	
        public Round createFromParcel(Parcel in) {  
            return new Round(in);  
        }  
   
        public Round[] newArray(int size) {  
            return new Round[size];  
        }  
          
    };


	public Hole[] getHoles()
	{
		return holes;
	}

	public String getTee()
	{
		return tee;
	}

	public void setTee(String t)
	{
		tee = t;
	}

	public int getStartingHole()
	{
		return startingHole;
	}

	public void setStartingHole(int start)
	{
		startingHole = start;
	}

	public File getCourseFile()
	{
		return courseFile;
	}

	public void setCourseFile(File courseFile)
	{
		this.courseFile = courseFile;
	}

	public String getCourseName()
	{
		return courseName;
	}

	public void setCourseName(String courseName)
	{
		this.courseName = courseName;
	}

	public Date getDate()
	{
		return date;
	}

	public void setDate(Date date)
	{
		this.date = date;
	}

	public void setNumberOfHoles(int num)
	{
		numHoles = num;
	}

	public int getNumberOfHoles()
	{
		return numHoles;
	}

	public void setNumberOfPlayers(int num)
	{
		numPlayers = num + 1;
	}

	public int getNumberOfPlayers()
	{
		return numPlayers;
	}

	public void setPlayers(String[] play)
	{
		players = play;
	}

	public String[] getPlayers()
	{
		return players;
	}

	public void setUpHoles()
	{
		CourseParser parser = new CourseParser(courseFile);
		int[][] holeData = parser.getHoleData(tee);
		GPSPoints[] pointsArray = parser.getGPSPoints();
		holes = new Hole[numHoles];
		int holeIndex = startingHole;
		int holeNumberIndex = startingHole;
		for (int i = 0; i < numHoles; i++)
		{
			if (holeIndex - 1 >= holeData.length)
				holeIndex = 1;
			holes[i] = new Hole(holeNumberIndex, holeData[holeIndex - 1][0],
					holeData[holeIndex - 1][1], numPlayers,
					holeData[holeIndex - 1][2]);
			holes[i].setPoints(pointsArray[holeIndex - 1]);
			holeIndex++;
			holeNumberIndex++;
			if (holeNumberIndex > 18)
				holeNumberIndex = 1;

		}
	}

	public String toString()
	{
		String result = "";
		for (String s : players)
			result += s + '\n';
		result += "Holes: " + numHoles + "\n";
		result += "Date: " + date.toString() + "\n";
		result += "Course: " + courseName + "\n";
		result += courseFile.getAbsolutePath() + "\n";
		result += "Tee: " + tee + "\n";
		result += "Start on " + startingHole + "\n";
		for (Hole h : holes)
			result += h + "\n";
		return result;
	}

	public String getName()
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return (cal.get(Calendar.MONTH) + 1) + "_"
				+ cal.get(Calendar.DAY_OF_MONTH) + "_" + cal.get(Calendar.YEAR)
				+ "_" + courseName.replace(' ', '_');
	}

}
