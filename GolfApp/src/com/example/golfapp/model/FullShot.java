package com.example.golfapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class FullShot extends Shot{

	
	
	public static final int ON_TARGET = 1;
	public static final int MISS_LEFT = 2;
	public static final int MISS_RIGHT = 3;
	public static final int MISS_SHORT = 4;
	public static final int MISS_LONG = 5;
	
	public static final int SLICE = 1;
	public static final int FADE = 2;
	public static final int STRAIGHT = 3;
	public static final int DRAW = 4;
	public static final int HOOK = 5;
	
	
	public static final int TEE = 1;
	public static final int FAIRWAY = 2;
	public static final int ROUGH = 3;
	public static final int SAND = 4;
	public static final int PINE = 5;
	public static final int OTHER_SURFACE = 6;
	
	public static final int NO_VALUE = 0;
	
	private boolean isTeeShot;
	private int direction;
	private int shape;
	private int distanceInMeters;
	private int surface;
	
	public FullShot(String club, String course, int hole,
			boolean tee, int direction, int shape, int distance, int surface)
	{
		super(club,course,hole);
		this.isTeeShot = tee;
		this.direction=direction;
		this.shape = shape;
		this.distanceInMeters = distance;
		this.surface = surface;
	}
	
	public FullShot(Parcel in)
	{
		super(in);
		isTeeShot = in.readByte() == 1;
		direction = in.readInt();
		shape = in.readInt();
		distanceInMeters = in.readInt();
		surface = in.readInt();
	}
	
	public int describeContents() {
		// TODO Auto-generated method stub
		return hashCode();
	}
	@Override
	public void writeToParcel(Parcel dest, int arg1) {
		super.writeToParcel(dest,arg1);
		dest.writeByte((byte) (isTeeShot ? 1 : 0));
		dest.writeInt(direction);
		dest.writeInt(shape);
		dest.writeInt(distanceInMeters);
		dest.writeInt(surface);
	}
	
public static final Parcelable.Creator<FullShot> CREATOR = new Parcelable.Creator<FullShot>() {  
	    
        public FullShot createFromParcel(Parcel in) {  
            return new FullShot(in);  
        }  
   
        public FullShot[] newArray(int size) {  
            return new FullShot[size];  
        }  
          
    };

	public int getSurface() {
		return surface;
	}

	public void setSurface(int surface) {
		this.surface = surface;
	}

	public boolean isTeeShot() {
		return isTeeShot;
	}

	public void setTeeShot(boolean isTeeShot) {
		this.isTeeShot = isTeeShot;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public int getShape() {
		return shape;
	}

	public void setShape(int shape) {
		this.shape = shape;
	}

	public int getDistanceInMeters() {
		return distanceInMeters;
	}

	public void setDistanceInMeters(int distanceInMeters) {
		this.distanceInMeters = distanceInMeters;
	}
	
	public String toString()
	{
		String result = super.toString();
		result+=" Direction: "+directionToString(direction)+
				" Shape: "+shapeToString(shape)+" Distance: "+distanceInMeters+
				" Surface: "+surfaceToString(surface);
		if(isTeeShot)
			result+=" teeShot";
		return result;
	}
	
	public static String directionToString(int dir)
	{
		switch(dir)
		{
		case MISS_LEFT:
			return "Left";
		case MISS_RIGHT:
			return "Right";
		case ON_TARGET:
			return "ON Target";
		case MISS_SHORT:
			return "Short";
		case MISS_LONG:
			return "Long";
		default:
			return "";
			
		}
	}
	
	private static String surfaceToString(int startLocation2) {
		switch(startLocation2)
		{
		case TEE:
			return "Tee";
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
	
	public static String shapeToString(int shape)
	{
		switch(shape)
		{
		case SLICE:
			return "Slice";
		case FADE:
			return "Fade";
		case STRAIGHT:
			return "Straight";
		case DRAW:
			return "Draw";
		case HOOK:
			return "Hook";
		default:
			return "";
		}
	}
	
}
