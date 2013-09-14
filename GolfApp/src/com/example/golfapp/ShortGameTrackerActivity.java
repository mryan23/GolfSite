package com.example.golfapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.golfapp.model.ShortGameShot;

public class ShortGameTrackerActivity extends Activity {

	Spinner shotTypeSpinner, surfaceSpinner, startDistanceSpinner, endDistanceSpinner;
	CheckBox greenCheckBox;
	Button saveButton, cancelButton;
	Activity thisActivity;
	ImageView targetViewer;
	TextView directionTextView;
	int missDirection = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_short_game_tracker);
		thisActivity=this;
		setListeners();
		surfaceSpinner.setEnabled(false);
		startDistanceSpinner.setEnabled(false);
		endDistanceSpinner.setEnabled(false);
		saveButton.setEnabled(false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.short_game_tracker, menu);
		return true;
	}
	
	private void setListeners()
	{
		shotTypeSpinner = (Spinner)findViewById(R.id.shortGameShotTypeSpinner);
		surfaceSpinner = (Spinner)findViewById(R.id.shortGameSurfaceSpinner);
		startDistanceSpinner = (Spinner)findViewById(R.id.shortGameStartDistanceSpinner);
		endDistanceSpinner = (Spinner)findViewById(R.id.shortGameEndDistanceSpinner);
		greenCheckBox = (CheckBox) findViewById(R.id.shortGameOnGreenCheckbox);
		saveButton = (Button)findViewById(R.id.shortGameSaveButton);
		cancelButton = (Button)findViewById(R.id.shortGameCancelButton);
		targetViewer = (ImageView)findViewById(R.id.shortGameTargetImageView);
		directionTextView = (TextView)findViewById(R.id.shortGameDirectionTextView);
		shotTypeSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int index, long arg3) {
				if(index ==0)
				{
					if(surfaceSpinner.isEnabled())
					{
						surfaceSpinner.setAdapter(null);
						surfaceSpinner.setEnabled(false);
						startDistanceSpinner.setAdapter(null);
						startDistanceSpinner.setEnabled(false);
						endDistanceSpinner.setAdapter(null);
						endDistanceSpinner.setEnabled(false);
						saveButton.setEnabled(false);

					}
				}
				else if(!surfaceSpinner.isEnabled())
				{
					surfaceSpinner.setEnabled(true);
					ArrayAdapter<String> spinnerArrayAdapter;
					String[] surfaces={"--Enter Surface--","Fairway", "Rough", "Sand", "Pine", "Other"};
					spinnerArrayAdapter= new ArrayAdapter<String>(thisActivity, android.R.layout.simple_spinner_item, surfaces);
					spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					surfaceSpinner.setAdapter(spinnerArrayAdapter);
					if(index==ShortGameShot.BUNKER_SHOT)
					{
						surfaceSpinner.setSelection(ShortGameShot.SAND);
					}
				}
				else if(index==ShortGameShot.BUNKER_SHOT)
				{
					surfaceSpinner.setSelection(ShortGameShot.SAND);
				}
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		surfaceSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int index, long arg3) {
				if(index==3){
					shotTypeSpinner.setSelection(3);
				}else{
					if(shotTypeSpinner.getSelectedItemPosition()==3){
						shotTypeSpinner.setSelection(2);
					}
				}
				if(index ==0)
				{
					if(startDistanceSpinner.isEnabled())
					{
						startDistanceSpinner.setAdapter(null);
						startDistanceSpinner.setEnabled(false);
						endDistanceSpinner.setAdapter(null);
						endDistanceSpinner.setEnabled(false);
						saveButton.setEnabled(false);

					}
				}
				else if(!startDistanceSpinner.isEnabled())
				{
					startDistanceSpinner.setEnabled(true);
					String[] distances ={"--Enter Start Distance--","76+ yards", "51-75 yards", "36-50 yards", "21-35 yards", "10-20 yards", "Under 10 yards"};
					ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(thisActivity, android.R.layout.simple_spinner_item, distances);
					spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					startDistanceSpinner.setAdapter(spinnerArrayAdapter);
				}
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		greenCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if(!endDistanceSpinner.isEnabled())
					return;
				String[] distances;
				if(!greenCheckBox.isChecked())
				{
					String[] dummyDistances={"--Enter End Distance--","76+ yards", "51-75 yards", "36-50 yards", "21-35 yards", "10-20 yards", "Under 10 yards"};
					distances=dummyDistances;
				}
				else
				{
					String[] dummyDistances={"--Enter End Distance--","51+ ft","36-50 ft", "21-35 ft", "11-20 ft", "5-10 ft", "1-4 ft", "In Hole"};
					distances=dummyDistances;
				}
				ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(thisActivity, android.R.layout.simple_spinner_item, distances);
				spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				endDistanceSpinner.setAdapter(spinnerArrayAdapter);
				
				
			}
			
		});
		
		startDistanceSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int index, long arg3) {
				if(index ==0)
				{
					if(endDistanceSpinner.isEnabled())
					{
						endDistanceSpinner.setAdapter(null);
						endDistanceSpinner.setEnabled(false);
						saveButton.setEnabled(false);
					}
				}
				else if(!endDistanceSpinner.isEnabled())
				{
					endDistanceSpinner.setEnabled(true);
					String[] distances;
					if(!greenCheckBox.isChecked())
					{
						String[] dummyDistances={"--Enter End Distance--","76+ yards", "51-75 yards", "36-50 yards", "21-35 yards", "10-20 yards", "Under 10 yards"};
						distances=dummyDistances;
					}
					else
					{
						String[] dummyDistances={"--Enter End Distance--","51+ ft","36-50 ft", "21-35 ft", "11-20 ft", "5-10 ft", "1-4 ft", "In Hole"};
						distances=dummyDistances;
					}
					ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(thisActivity, android.R.layout.simple_spinner_item, distances);
					spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					endDistanceSpinner.setAdapter(spinnerArrayAdapter);
				}
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		
		
		
		
		endDistanceSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int index, long arg3) {
				if(index ==0)
				{
					saveButton.setEnabled(false);
				}
				else
				{
					saveButton.setEnabled(true);
				}
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		cancelButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				setResult(Activity.RESULT_CANCELED);
				finish();
				
			}
			
		});
		
		saveButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent();
				
				//Add ShortGameShot to bundle here
				//Make ShortGameShot Parcelable
				int type=shotTypeSpinner.getSelectedItemPosition();
				int origDist=startDistanceSpinner.getSelectedItemPosition()+100;
				int finalDist=endDistanceSpinner.getSelectedItemPosition();
				boolean green=greenCheckBox.isChecked();
				if(!green)
					finalDist+=100;
				int miss= missDirection;
				int start=surfaceSpinner.getSelectedItemPosition();
				ShortGameShot shot = new ShortGameShot(null, null, 0, type, origDist,finalDist, green, miss, start);
				//b.putParcelable("ShortGameShot", shot);
				
				
				i.putExtra("ShortGameShot", shot);
				setResult(Activity.RESULT_OK, i);
				finish();
				
			}
			
		});
		
		targetViewer.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1)
			{
				
				int x = (int)arg1.getX();
				int y = (int)arg1.getY();
				missDirection = 3*(y/(targetViewer.getHeight()/3)) + (x/(targetViewer.getWidth()/3)) + 1;
				Log.d("SIZE",targetViewer.getHeight()+","+targetViewer.getWidth());
				Log.d("Coordinates","("+x+","+y+") "+missDirection+" "+ShortGameShot.missToString(missDirection));
				directionTextView.setText(ShortGameShot.missToString(missDirection));
				return false;
			}
			
		});
		
		
		
	}

}
