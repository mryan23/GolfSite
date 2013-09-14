package com.example.golfapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ShortGameStatsActivity extends Activity {

	private Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_short_game_stats);
		context = this;
		setListeners();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.short_game_stats, menu);
		return true;
	}
	
	public void setListeners(){
		Button bunkers = (Button)findViewById(R.id.bunkerStatsButton);
		bunkers.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent i = new Intent(context, BunkerStatsActivity.class);
				startActivity(i);
			}
		});
	}
	
	

}
