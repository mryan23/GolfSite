package com.example.golfapp;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.golfapp.model.GPSPoints;
import com.example.golfapp.model.Obstacles;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class GPSActivity extends Activity {
	
	public static boolean  USE_YARDS = true, firstTime = true;
	LocationManager locationManager;
	GoogleMap map;
	ArrayList<MarkerOptions> markerOptions;
	Location myLocation, myTarget, greenCenter=null;
	TextView targetTextView, greenCenterView, greenFrontView, greenBackView, obstacleTextView;
	BitmapDescriptor crossHairs, golfer, waterStart, waterEnd, bunkerStart, bunkerEnd;
	GPSPoints points;
	ArrayList<Obstacles> obstacles;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 String location_context = Context.LOCATION_SERVICE;
		locationManager = (LocationManager) getSystemService(location_context);
		
		setContentView(R.layout.activity_gps);
		Intent intent = getIntent();
		points = (GPSPoints)(intent.getParcelableExtra("GPS Points"));
		markerOptions = new ArrayList<MarkerOptions>();
		crossHairs = BitmapDescriptorFactory.fromResource(R.drawable.crosshairs);
		golfer = BitmapDescriptorFactory.fromResource(R.drawable.golfer);
		if(points != null)
		{
			BitmapDescriptor teeBitmap = BitmapDescriptorFactory.fromResource(R.drawable.tee_box);
			markerOptions.add(new MarkerOptions().position(points.teeBox).icon(teeBitmap));
			BitmapDescriptor frontBitmap = BitmapDescriptorFactory.fromResource(R.drawable.front_green);
			markerOptions.add(new MarkerOptions().position(points.frontGreen).icon(frontBitmap).anchor(.5f, .5f));
			
			BitmapDescriptor centerBitmap = BitmapDescriptorFactory.fromResource(R.drawable.center_green);
			markerOptions.add(new MarkerOptions().position(points.centerGreen).icon(centerBitmap).anchor(.5f, .5f));
			BitmapDescriptor backBitmap = BitmapDescriptorFactory.fromResource(R.drawable.back_green);
			markerOptions.add(new MarkerOptions().position(points.backGreen).icon(backBitmap).anchor(.5f, .5f));
			obstacles = new ArrayList<Obstacles>();
			if(points.waterHazards != null)
			{
				waterStart = BitmapDescriptorFactory.fromResource(R.drawable.water_start);
				waterEnd = BitmapDescriptorFactory.fromResource(R.drawable.water_end);
				for(Obstacles o: points.waterHazards)
				{
					markerOptions.add(new MarkerOptions().position(o.start).icon(waterStart).anchor(.5f, .5f));
					markerOptions.add(new MarkerOptions().position(o.end).icon(waterEnd).anchor(.5f, .5f));
					obstacles.add(o);
				}
			}
			
			if(points.bunkers != null)
			{
				bunkerStart = BitmapDescriptorFactory.fromResource(R.drawable.bunker_start);
				bunkerEnd= BitmapDescriptorFactory.fromResource(R.drawable.bunker_end);
				for(Obstacles o: points.bunkers)
				{
					markerOptions.add(new MarkerOptions().position(o.start).icon(bunkerStart));
					markerOptions.add(new MarkerOptions().position(o.end).icon(bunkerEnd));
					obstacles.add(o);
				}
			}
			
			if(points.trees != null)
			{
				BitmapDescriptor treeIcon = BitmapDescriptorFactory.fromResource(R.drawable.tree);
				for(Obstacles o: points.trees)
				{
					markerOptions.add(new MarkerOptions().position(o.start).icon(treeIcon));
					obstacles.add(o);
				}
			}
			
			if(points.hazards != null)
			{
				BitmapDescriptor hazardIcon = BitmapDescriptorFactory.fromResource(R.drawable.hazard);
				for(Obstacles o: points.hazards)
				{
					markerOptions.add(new MarkerOptions().position(o.start).icon(hazardIcon).anchor(.5f, .5f));
					if(o.end != null)
						markerOptions.add(new MarkerOptions().position(o.end).icon(hazardIcon).anchor(.5f, .5f));
					obstacles.add(o);
				}
			}
			
			if(points.eofs != null)
			{
				BitmapDescriptor eofIcon = BitmapDescriptorFactory.fromResource(R.drawable.eof);
				for(Obstacles o: points.eofs)
				{
					markerOptions.add(new MarkerOptions().position(o.start).icon(eofIcon).anchor(.5f, .5f));
					obstacles.add(o);
				}
			}
			
			if(points.others != null)
			{
				BitmapDescriptor otherIcon = BitmapDescriptorFactory.fromResource(R.drawable.other_marker);
				for(Obstacles o: points.others)
				{
					markerOptions.add(new MarkerOptions().position(o.start).icon(otherIcon));
					if(o.end != null)
						markerOptions.add(new MarkerOptions().position(o.end).icon(otherIcon));
					obstacles.add(o);
				}
			}
			
		}
		targetTextView = (TextView)findViewById(R.id.gpsTargetTextView);
		targetTextView.setText("");
		greenCenterView = (TextView)findViewById(R.id.greenTextView);
		greenFrontView = (TextView)findViewById(R.id.greenFrontTextView);
		greenBackView = (TextView)findViewById(R.id.greenBackTextView);
		obstacleTextView=(TextView)findViewById(R.id.obstacleTextView);
		map = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
		//map.addMarker(new MarkerOptions().position(new LatLng(0,0)).title("Pleaseee"));
		LocationManager mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
		map.getUiSettings().setCompassEnabled(false);
		
		
		

	     LocationListener mlocListener = new MyLocationListener(this);
	     List<String> providers = mlocManager.getProviders(true);
	     Location l = null;
	     for(int i = providers.size() -1; i>=0; i--)
	     {
	    	 l=mlocManager.getLastKnownLocation(providers.get(i));
	    	 if(l!=null)
	    		 break;
	     }
	     myTarget =null;
	     if(l!= null)
	     {
	    	 doStuff(l);
	     }
	     greenCenter= new Location(l);
		greenCenter.setLatitude(points.centerGreen.latitude);
		greenCenter.setLongitude(points.centerGreen.longitude);
	     
	     mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 1000, 1, mlocListener);
	     map.setOnMapClickListener(new OnMapClickListener(){

			@Override
			public void onMapClick(LatLng point) {
				onClick(point);
			}
	    	 
	     });
	     map.setOnMarkerClickListener(new OnMarkerClickListener(){

			@Override
			public boolean onMarkerClick(Marker arg0) {
				onClick(arg0.getPosition());
				return false;
			}
				
			});
	     
	     setupButtons();
	     updateGreenPoints();
		
	}
	public void updateGreenPoints()
	{
		if(myLocation != null)
		{
			Location center = new Location(myLocation);
			center.setLatitude(points.centerGreen.latitude);
			center.setLongitude(points.centerGreen.longitude);
			greenCenterView.setText(""+getDistance(myLocation.distanceTo(center)));
			Location front = new Location(myLocation);
			front.setLatitude(points.frontGreen.latitude);
			front.setLongitude(points.frontGreen.longitude);
			greenFrontView.setText(""+getDistance(myLocation.distanceTo(front)));
			Location back = new Location(myLocation);
			back.setLatitude(points.backGreen.latitude);
			back.setLongitude(points.backGreen.longitude);
			greenBackView.setText(""+getDistance(myLocation.distanceTo(back)));
			
			String obstaclesString="";
			Obstacles o;
			for(int i = 0; i < obstacles.size(); i++)
			{
				o=obstacles.get(i);
				obstaclesString+=o.name+"\n";
				Location startLoc = new Location(myLocation);
				startLoc.setLatitude(o.start.latitude);
				startLoc.setLongitude(o.start.longitude);
				int distanceToStart = getDistance(myLocation.distanceTo(startLoc));
				obstaclesString+=distanceToStart;
				if(o.end != null)
				{
					Location endLoc = new Location(myLocation);
					endLoc.setLatitude(o.end.latitude);
					endLoc.setLongitude(o.end.longitude);
					int distanceToEnd = getDistance(myLocation.distanceTo(endLoc));
					obstaclesString+="/"+distanceToEnd;
				}
				if(i < obstacles.size() -1)
					obstaclesString+="\n";
			}
			obstacleTextView.setText(obstaclesString);
		}
	}
	
	private void onClick(LatLng point)
	{
		map.clear();
		//map.addMarker(new MarkerOptions().position(point).icon(crossHairs).anchor(.5f, .5f));
		myTarget = new Location(myLocation);
		myTarget.setLatitude(point.latitude);
		myTarget.setLongitude(point.longitude);
		drawPoints();
	}
	
	public static int getDistance(double distance)
	{
		if(USE_YARDS)
			return (int)(distance * 1.09361);
		return (int)distance;
	}
	public static String getDistanceString(double distanceMeters)
	{
		if(USE_YARDS)
		{
			return getDistance(distanceMeters)+" yards";
		}
		return getDistance(distanceMeters)+" meters";
	}
	public void doStuff(Location loc)
	{
		myLocation = loc;
		map.clear();
		
		
		if(firstTime)
		{
			LatLng centerGreen = markerOptions.get(2).getPosition();
			Location centerLoc = new Location(loc);
			centerLoc.setLatitude(centerGreen.latitude);
			centerLoc.setLongitude(centerGreen.longitude);
			
			LatLng midPoint = new LatLng((centerGreen.latitude + loc.getLatitude())/2.0,(centerGreen.longitude + loc.getLongitude())/2);
			
			
			float distanceToCenterMeters = loc.distanceTo(centerLoc);
			
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(midPoint,
					calculateZoomLevel(100,(int)distanceToCenterMeters+1)));
			
			map.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
			.target(map.getCameraPosition().target)
			.zoom(map.getCameraPosition().zoom)
			.bearing(loc.bearingTo(centerLoc))
			.build()));
			firstTime = false;
		}
		drawPoints();
		
	}
	
	private void drawPoints()
	{
		map.clear();
		map.addMarker(new MarkerOptions().position(new LatLng(myLocation.getLatitude(),myLocation.getLongitude())).icon(golfer));
		for(MarkerOptions m: markerOptions)
		{
			map.addMarker(m);
		}
		if(myTarget !=null)
		{
			map.addMarker(new MarkerOptions().position(new LatLng(myTarget.getLatitude(), myTarget.getLongitude())).icon(crossHairs).anchor(.5f, .5f));
			if(myTarget.distanceTo(greenCenter)< 27.432)
			{
				targetTextView.setText("Target: "+getDistanceString(myLocation.distanceTo(myTarget)));
			}
			else
			{
				targetTextView.setText("Target: "+getDistanceString(myLocation.distanceTo(myTarget))+"\n"+
										"To Go: "+getDistanceString(myTarget.distanceTo(greenCenter)));
				map.addPolyline(new PolylineOptions().add(new LatLng(myLocation.getLatitude(), myLocation.getLongitude()),
														new LatLng(myTarget.getLatitude(), myTarget.getLongitude()),
														new LatLng(greenCenter.getLatitude(), greenCenter.getLongitude()))
														.width(5).color(Color.RED));
			}
		}
		else
		{
			targetTextView.setText("");
		}
	}
	private void setupButtons()
	{
		Button greenZoom = (Button)findViewById(R.id.gpsGreenViewButton);
		greenZoom.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {	
				Location front = new Location(myLocation);
				front.setLatitude(points.frontGreen.latitude);
				front.setLongitude(points.frontGreen.longitude);
				Location back = new Location(myLocation);
				back.setLatitude(points.backGreen.latitude);
				back.setLongitude(points.backGreen.longitude);
				double distanceFrontToBack = front.distanceTo(back);
				map.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
				.target(points.centerGreen)
				.zoom(calculateZoomLevel(100,(int)distanceFrontToBack))
				.bearing(front.bearingTo(back))
				.build()));
				myTarget = null;
				drawPoints();	
			}
			
		});
		
		
		Button holeZoom = (Button)findViewById(R.id.gpsFullHoleView);
		holeZoom.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Location center = new Location(myLocation);
				center.setLatitude(points.centerGreen.latitude);
				center.setLongitude(points.centerGreen.longitude);
				Location tee = new Location(myLocation);
				tee.setLatitude(points.teeBox.latitude);
				tee.setLongitude(points.teeBox.longitude);
				
				LatLng midPoint = new LatLng((center.getLatitude() + tee.getLatitude())/2.0,(center.getLongitude()+ tee.getLongitude())/2);
				double distanceTeeToGreen = tee.distanceTo(center);
				map.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
				.target(midPoint)
				.zoom(calculateZoomLevel(100,(int)distanceTeeToGreen))
				.bearing(tee.bearingTo(center))
				.build()));
				myTarget = null;
				drawPoints();	
				
			}
			
		});
		
		Button approachZoom = (Button)findViewById(R.id.gpsGolferViewButton);
		approachZoom.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Location center = new Location(myLocation);
				center.setLatitude(points.centerGreen.latitude);
				center.setLongitude(points.centerGreen.longitude);
				double distanceTeeToGreen = myLocation.distanceTo(center);
				LatLng midPoint = new LatLng((center.getLatitude() + myLocation.getLatitude())/2.0,(center.getLongitude()+ myLocation.getLongitude())/2);
				map.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
				.target(midPoint)
				.zoom(calculateZoomLevel(100,(int)distanceTeeToGreen))
				.bearing(myLocation.bearingTo(center))
				.build()));
				myTarget = null;
				drawPoints();	
				
			}
			
		});
	}
	private int calculateZoomLevel(int screenWidth, int distance) {
	    double equatorLength = 40075004; // in meters
	    double widthInPixels = screenWidth;
	    double metersPerPixel = equatorLength / 256;
	    int zoomLevel = 1;
	    while ((metersPerPixel * widthInPixels) > distance) {
	        metersPerPixel /= 2;
	        ++zoomLevel;
	    }
	    return zoomLevel;
	}
	public class MyLocationListener implements LocationListener
    {
		GPSActivity activity;
		public MyLocationListener(GPSActivity act)
		{
			activity = act;
		}

      @Override
      public void onLocationChanged(Location loc)
      {
    	activity.myTarget = null;
    	activity.targetTextView.setText("");
        activity.doStuff(loc);
        activity.updateGreenPoints();

        //Toast.makeText( getApplicationContext(), Text, Toast.LENGTH_SHORT).show();
      }

      @Override
      public void onProviderDisabled(String provider)
      {
        Toast.makeText( getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT ).show();
      }

      @Override
      public void onProviderEnabled(String provider)
      {
        Toast.makeText( getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
      }

      @Override
      public void onStatusChanged(String provider, int status, Bundle extras)
      {

      }
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.g, menu);
		return true;
	}

}
