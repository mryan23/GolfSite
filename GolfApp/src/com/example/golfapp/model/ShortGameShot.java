package com.example.golfapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ShortGameShot extends Shot{

	
	
	
	public static final int CHIP_SHOT = 1;
	public static final int PITCH_SHOT = 2;
	public static final int BUNKER_SHOT = 3;
	
	public static final int SEVENTYSIX_PLUS = 101;
	public static final int FIFTYONE_SEVENTYFIVE = 102;
	public static final int THIRTYSIX_FIFTY = 103;
	public static final int TWENTYONE_THIRTYFIVE = 104;
	public static final int TEN_TWENTY = 105;
	public static final int LESS_TEN = 106;
	
	public static final int FAIRWAY = 1;
	public static final int ROUGH = 2;
	public static final int SAND = 3;
	public static final int PINE = 4;
	public static final int OTHER_SURFACE = 5;
	
	public static final int NO_VALUE = 0;
	public static final int LONG_LEFT = 1;
	public static final int LONG = 2;
	public static final int LONG_RIGHT = 3;
	public static final int LEFT = 4;
	public static final int ON_TARGET = 5;
	public static final int RIGHT = 6;
	public static final int SHORT_LEFT = 7;
	public static final int SHORT = 8;
	public static final int SHORT_RIGHT = 9;
	
	private int shotType;
	private int origDistRange;
	private int finalDistRange;
	private boolean hitGreen;
	private int missDirection;
	private int startLocation;
	private int puttsAfter;
	private int score;
	private int par;
	
	
	public ShortGameShot(Parcel in)
	{
		super(in);
		shotType = in.readInt();
		origDistRange = in.readInt();
		finalDistRange = in.readInt();
		hitGreen = in.readByte() == 1;
		missDirection = in.readInt();
		startLocation = in.readInt();
		puttsAfter = in.readInt();
		score = in.readInt();
		par = in.readInt();
	}
	
	public int describeContents() {
		// TODO Auto-generated method stub
		return hashCode();
	}
	@Override
	public void writeToParcel(Parcel dest, int arg1) {
		super.writeToParcel(dest,arg1);
		dest.writeInt(shotType);
		dest.writeInt(origDistRange);
		dest.writeInt(finalDistRange);
		dest.writeByte((byte) (hitGreen ? 1 : 0));
		dest.writeInt(missDirection);
		dest.writeInt(startLocation);
		dest.writeInt(puttsAfter);
		dest.writeInt(score);
		dest.writeInt(par);
	}
	
public static final Parcelable.Creator<ShortGameShot> CREATOR = new Parcelable.Creator<ShortGameShot>() {  
	    
        public ShortGameShot createFromParcel(Parcel in) {  
            return new ShortGameShot(in);  
        }  
   
        public ShortGameShot[] newArray(int size) {  
            return new ShortGameShot[size];  
        }  
          
    };
	
	
	public ShortGameShot(String club, String course, int hole,
			int shotType, int origDistRange, int finalDistRange,
			boolean green, int missDir, int startLoc)
	{
		super(club, course, hole);
		this.shotType = shotType;
		this.origDistRange=origDistRange;
		this.finalDistRange = finalDistRange;
		this.hitGreen = green;
		this.missDirection = missDir;
		this.startLocation = startLoc;
		puttsAfter = 0;
		score = 0;
		par = 0;
	}
	
	public String toString()
	{
		String result = super.toString();
		result+=" Type: "+typeToString(shotType)+" From: "+distanceToString(origDistRange)+
				" To: "+distanceToString(finalDistRange)+ " Green: "+hitGreen+ " Miss: "+missToString(missDirection)+
				" Surface: "+surfaceToString(startLocation)+" Putts: "+puttsAfter+" Score: "+score +" Par: "+par;
		return result;
	}

	private static String surfaceToString(int startLocation2) {
		switch(startLocation2)
		{
		case ROUGH:
			return "Rough";
		case FAIRWAY:
			return "Fairway";
		case SAND:
			return "Sand";
		case PINE:
			return "Pine";
		default:
			return "Other";
		}
	}

	public int getMissDirection() {
		return missDirection;
	}

	public void setMissDirection(int missDirection) {
		this.missDirection = missDirection;
	}

	public static String missToString(int miss) {
		switch(miss)
		{
		case ON_TARGET:
			return "On Target";
		case LONG_LEFT:
			return "Long Left";
		case LONG:
			return "Long";
		case LONG_RIGHT:
			return "Long Right";
		case LEFT:
			return "Left";
		case RIGHT:
			return "Right";
		case SHORT_LEFT:
			return "Short Left";
		case SHORT:
			return "Short";
		case SHORT_RIGHT:
			return "Short Right";
		default:
			return "";
		}
	}

	private static String distanceToString(int originalDistanceRange) {
		switch(originalDistanceRange)
		{
		case SEVENTYSIX_PLUS:
			return "76+ yds";
		case FIFTYONE_SEVENTYFIVE:
			return "51-75 yds";
		case THIRTYSIX_FIFTY:
			return "36-50 yds";
		case TWENTYONE_THIRTYFIVE:
			return "21-35 yds";
		case TEN_TWENTY:
			return "10-20 yds";
		case LESS_TEN:
			return "<10 yds";
		default:
			return Putt.rangeToString(originalDistanceRange);
		}
	}

	private static String typeToString(int type) {
		switch(type)
		{
		case CHIP_SHOT:
			return "Chip";
		case PITCH_SHOT:
			return "Pitch";
		case BUNKER_SHOT:
			return "Bunker";
		default:
			return "";
		}
	}

	public int getShotType() {
		return shotType;
	}

	public void setShotType(int shotType) {
		this.shotType = shotType;
	}

	public int getOrigDistRange() {
		return origDistRange;
	}

	public void setOrigDistRange(int origDistRange) {
		this.origDistRange = origDistRange;
	}

	public int getFinalDistRange() {
		return finalDistRange;
	}

	public void setFinalDistRange(int finalDistRange) {
		this.finalDistRange = finalDistRange;
	}

	public boolean isHitGreen() {
		return hitGreen;
	}

	public void setHitGreen(boolean hitGreen) {
		this.hitGreen = hitGreen;
	}

	public int getStartLocation() {
		return startLocation;
	}

	public void setStartLocation(int startLocation) {
		this.startLocation = startLocation;
	}

	public int getPuttsAfter() {
		return puttsAfter;
	}

	public void setPuttsAfter(int puttsAfter) {
		this.puttsAfter = puttsAfter;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getPar() {
		return par;
	}

	public void setPar(int par) {
		this.par = par;
	}
}
