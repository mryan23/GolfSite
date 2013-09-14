package com.example.golfapp;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.golfapp.model.FullShot;
import com.example.golfapp.sql.ShotDatabaseHelper;

public class StatsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stats);
		addButtonListeners();
		TextView debug = (TextView)findViewById(R.id.statsDebugTextView);
		List<FullShot> list = new ShotDatabaseHelper(this).getAllShotsWithClub("8 Iron");
		String result = "";
		for(FullShot s: list)
			result+=s.toString()+"\n";
		debug.setText(result);
	}
	
	private void addButtonListeners()
	{
		Button backButton = (Button) findViewById(R.id.statsBackButton);
		backButton.setOnClickListener(new OnClickListener() {
			 
			@Override
			public void onClick(View v) {
 
				finish();
 
			}
 
		});
		
		Button clubsButton = (Button)findViewById(R.id.clubsStatsButton);
		clubsButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(getApplicationContext(), ClubStatsActivity.class);
				startActivity(i);
				
			}
			
		});
		
		Button holesButton = (Button)findViewById(R.id.courseStatsButton);
		holesButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(getApplicationContext(), CourseStatsActivity.class);
				startActivity(i);
				
			}
			
		});
		
		Button shortGameButton = (Button)findViewById(R.id.shortGameStatsButton);
		shortGameButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), ShortGameStatsActivity.class);
				startActivity(i);
				
			}
			
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.stats, menu);
		return true;
	}
	
	

}
