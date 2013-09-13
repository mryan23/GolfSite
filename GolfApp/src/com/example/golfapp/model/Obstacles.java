package com.example.golfapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class Obstacles implements Parcelable{
	public LatLng start=null;
	public LatLng end=null;
	public String name="";
	
	public Obstacles(LatLng s, LatLng e, String n)
	{
		start = s;
		end = e;
		name = n;
	}
	
	public Obstacles(Parcel in)
	{
		start = in.readParcelable(LatLng.class.getClassLoader());
		end = in.readParcelable(LatLng.class.getClassLoader());
		name = in.readString();
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return hashCode();
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(start, flags);
		dest.writeParcelable(end, flags);
		dest.writeString(name);
		
	}
	
public static final Parcelable.Creator<Obstacles> CREATOR = new Parcelable.Creator<Obstacles>() {  
	    
        public Obstacles createFromParcel(Parcel in) {  
            return new Obstacles(in);  
        }  
   
        public Obstacles[] newArray(int size) {  
            return new Obstacles[size];  
        }  
          
    };

}
