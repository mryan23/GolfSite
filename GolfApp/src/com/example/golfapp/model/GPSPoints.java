package com.example.golfapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class GPSPoints implements Parcelable{
	/**
	 * 
	 */
	public LatLng teeBox;
	public LatLng frontGreen;
	public LatLng centerGreen;
	public LatLng backGreen;
	public Obstacles[] waterHazards;
	public Obstacles[] bunkers;
	public Obstacles[] trees;
	public Obstacles[] hazards;
	public Obstacles[] eofs;
	public Obstacles[] others;
	//public ArrayList<Obstacles> obstacles;
	
	public GPSPoints()
	{
		waterHazards = null;
		bunkers = null;
		trees = null;
		hazards = null;
		eofs = null;
		others = null;
	}
	public GPSPoints(Parcel in)
	{
		readFromParcel(in);
	}
	private void readFromParcel(Parcel in)
	{
		teeBox = in.readParcelable(LatLng.class.getClassLoader());
		frontGreen = in.readParcelable(LatLng.class.getClassLoader());
		centerGreen = in.readParcelable(LatLng.class.getClassLoader());
		backGreen = in.readParcelable(LatLng.class.getClassLoader());
		Parcelable[] dummyWater = in.readParcelableArray(Obstacles.class.getClassLoader());
		waterHazards = new Obstacles[dummyWater.length];
		for(int i = 0; i < dummyWater.length; i++)
		{
			if(dummyWater[i] instanceof Obstacles)
				waterHazards[i]=(Obstacles) dummyWater[i];
		}
		
		Parcelable[] dummyBunkers = in.readParcelableArray(Obstacles.class.getClassLoader());
		bunkers = new Obstacles[dummyBunkers.length];
		for(int i = 0; i < dummyBunkers.length; i++)
		{
			if(dummyBunkers[i] instanceof Obstacles)
				bunkers[i]=(Obstacles) dummyBunkers[i];
		}
		
		Parcelable[] dummyTrees = in.readParcelableArray(Obstacles.class.getClassLoader());
		trees = new Obstacles[dummyTrees.length];
		for(int i = 0; i < dummyTrees.length; i++)
		{
			if(dummyTrees[i] instanceof Obstacles)
				trees[i]=(Obstacles) dummyTrees[i];
		}
		
		Parcelable[] dummyHazards = in.readParcelableArray(Obstacles.class.getClassLoader());
		hazards = new Obstacles[dummyHazards.length];
		for(int i = 0; i < dummyHazards.length; i++)
		{
			if(dummyHazards[i] instanceof Obstacles)
				hazards[i]=(Obstacles) dummyHazards[i];
		}
		
		Parcelable[] dummyEofs = in.readParcelableArray(Obstacles.class.getClassLoader());
		eofs = new Obstacles[dummyEofs.length];
		for(int i = 0; i < dummyEofs.length; i++)
		{
			if(dummyEofs[i] instanceof Obstacles)
				eofs[i]=(Obstacles) dummyEofs[i];
		}
		
		Parcelable[] dummyOthers = in.readParcelableArray(Obstacles.class.getClassLoader());
		others = new Obstacles[dummyOthers.length];
		for(int i = 0; i < dummyOthers.length; i++)
		{
			if(dummyOthers[i] instanceof Obstacles)
				others[i]=(Obstacles) dummyOthers[i];
		}
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return hashCode();
	}
	@Override
	public void writeToParcel(Parcel dest, int arg1) {
		dest.writeParcelable(teeBox, arg1);
		dest.writeParcelable(frontGreen, arg1);
		dest.writeParcelable(centerGreen, arg1);
		dest.writeParcelable(backGreen, arg1);
		dest.writeParcelableArray(waterHazards, arg1);
		dest.writeParcelableArray(bunkers, arg1);
		dest.writeParcelableArray(trees, arg1);
		dest.writeParcelableArray(hazards, arg1);
		dest.writeParcelableArray(eofs, arg1);
		dest.writeParcelableArray(others,arg1);
		//dest.writeList(obstacles);
	}

	
	public static final Parcelable.Creator<GPSPoints> CREATOR = new Parcelable.Creator<GPSPoints>() {  
	    
        public GPSPoints createFromParcel(Parcel in) {  
            return new GPSPoints(in);  
        }  
   
        public GPSPoints[] newArray(int size) {  
            return new GPSPoints[size];  
        }  
          
    };
}
