package com.example.golfapp.model;

public class Putt extends Shot {
	
	
	public static final int FIFTYONE_PLUS = 1;
	public static final int THIRTYSIX_FIFTY=2;
	public static final int TWENTYONE_THIRTYFIVE=3;
	public static final int ELEVEN_TWENTY=4;
	public static final int FIVE_TEN=5;
	public static final int ONE_FOUR=6;
	public static final int IN_HOLE=7;
	
	private int origDistanceRange;
	private int finalDistanceRange;
	
	

	public Putt(String course, int hole, int origDistanceRange, int finalDistanceRange)
	{
		super("Putter", course, hole);
		this.origDistanceRange = origDistanceRange;
		this.finalDistanceRange=finalDistanceRange;
	}

	public int getOrigDistanceRange() {
		return origDistanceRange;
	}

	public void setOrigDistanceRange(int origDistanceRange) {
		this.origDistanceRange = origDistanceRange;
	}

	public int getFinalDistanceRange() {
		return finalDistanceRange;
	}

	public void setFinalDistanceRange(int finalDistanceRange) {
		this.finalDistanceRange = finalDistanceRange;
	}
	
	public String toString()
	{
		String result=super.toString();
		result+=" from: "+rangeToString(origDistanceRange)+ " to: "+rangeToString(finalDistanceRange);
		return result;
	}
	
	public static String rangeToString(int range)
	{
		switch(range)
		{
		case FIFTYONE_PLUS:
			return "51+ ft";
		case THIRTYSIX_FIFTY:
			return "36-50 ft";
		case TWENTYONE_THIRTYFIVE:
			return "21-35 ft";
		case ELEVEN_TWENTY:
			return "11-20 ft";
		case FIVE_TEN:
			return "5-10 ft";
		case ONE_FOUR:
			return "1-4 ft";
		case IN_HOLE:
			return "HOLE";
		default:
			return "";
		}
	}
}
