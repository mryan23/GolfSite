package com.example.golfapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesUtil;

public class MainActivity extends Activity {

	Activity stats = new StatsActivity();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setButtonListeners();
		

		File clubsFile = new File(getFilesDir(),"clubs.xml");
		if(!clubsFile.exists())
		{
			makeClubsFile(clubsFile.getName());
		}
		//File clubsFile = new File(getFilesDir(),"clubs.xml");
		//clubsFile.delete();
		
		File testFile = new File(getFilesDir(),"courses/vtCourse.xml");
		testFile.mkdirs();
		DownloadFileFromURL downloader = new DownloadFileFromURL(new File(getFilesDir(),"courses/vtCourse.xml").getAbsolutePath());
		downloader.execute("http://www.oaktonchristmaslights.com/VT%20Golf%20Course.xml");
		TextView debugView = (TextView)findViewById(R.id.mainDebugTextView);
		debugView.setText(new File(getFilesDir(),"courses/vtCourse.xml").exists()+"");
		   
		
	
	}
	
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.aboutMenu:
			showAbout();
			return true;
		default:
			return true;
		
		}
	}
	private void showAbout()
	{
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("About");
		alert.setMessage(GooglePlayServicesUtil.getOpenSourceSoftwareLicenseInfo(this));
		alert.show();
	}
	
	private void makeClubsFile(String fileName)
	{
		String result = "";
	    try {
	        String[] clubs={"Driver", "3 Wood", "3 Iron", "4 Iron", "5 Iron",
	        				"6 Iron","7 Iron", "8 Iron", "9 Iron",
	        				"Pitching Wedge","Sand Wedge"};
	        for(String s: clubs)
	        {
		        result+=s+" true\n";
	        }
	        FileOutputStream output = openFileOutput(fileName, Context.MODE_PRIVATE);
	        output.write(result.getBytes());
	        output.close();
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}
	
	public static String convertStreamToString(InputStream is) throws Exception {
	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();
	    String line = null;
	    while ((line = reader.readLine()) != null) {
	      sb.append(line).append("\n");
	    }
	    return sb.toString();
	}

	public static String getStringFromFile (String filePath) throws Exception {
	    File fl = new File(filePath);
	    FileInputStream fin = new FileInputStream(fl);
	    String ret = convertStreamToString(fin);
	    //Make sure you close all streams.
	    fin.close();        
	    return ret;
	}
	
	protected void setButtonListeners()
	{
		Button statsButton = (Button)findViewById(R.id.statsButton);
		statsButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent i = new Intent(getApplicationContext(), StatsActivity.class);
				startActivity(i);
				
			}
		});
		
		Button newRoundButton = (Button)findViewById(R.id.newRoundButton);
		newRoundButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), MakeRoundActivity.class);
				startActivity(i);
				
			}
		});
		
		Button clubButton = (Button)findViewById(R.id.bagButton);
		clubButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(getApplicationContext(), ClubsActivity.class);
				startActivity(i);
			}
			
		});
		
		Button measureShotButton =(Button)findViewById(R.id.measureShotButton);
		measureShotButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(getApplicationContext(), ShotTrackerActivity.class);
				startActivity(i);
				
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
