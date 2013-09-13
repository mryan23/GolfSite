package com.example.golfapp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.golfapp.model.Club;
import com.example.golfapp.model.FullShot;
import com.example.golfapp.sql.ShotDatabaseHelper;
import com.example.golfapp.xml.ClubParser;

public class ClubStatsActivity extends Activity {

	private List<ArrayList<FullShot>> shotsByClub;
	private Spinner clubSpinner;
	private Club[] clubs;
	private Context context;
	private ProgressDialog progressDialog;
	private TextView debugTextView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_club_stats);
		context = this;
		clubSpinner= (Spinner)findViewById(R.id.clubStatsClubSpinner);
		debugTextView = (TextView)findViewById(R.id.clubStatDebugTextView);
		ClubParser parser = new ClubParser(new File(getFilesDir(),"clubs.xml"));
		clubs = parser.getClubs();
		shotsByClub = new ArrayList<ArrayList<FullShot>>();
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Compiling Stats");
		progressDialog.setMax(100);
		progressDialog.setIndeterminate(false);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		GetClubStuffFromSQL getStuff = new GetClubStuffFromSQL();
		getStuff.execute(clubs);
		
		String[] clubNames = new String[clubs.length];
		for(int i = 0; i < clubs.length; i++)
			clubNames[i]=clubs[i].getName();
		ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, clubNames);
		spinnerArrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		clubSpinner.setAdapter(spinnerArrayAdapter2);
		clubSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				updateDebugTextView(arg2);
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	private void updateDebugTextView(int index)
	{
		String result = "";
		ArrayList<FullShot> shots = shotsByClub.get(index);
		for(FullShot s: shots)
			result+=s.toString()+"\n";
		debugTextView.setText(result);
		
	}
	
	private class GetClubStuffFromSQL extends AsyncTask<Club, Integer, Long>
	{
		protected Long doInBackground(Club... clubs)
		{
			ShotDatabaseHelper db = new ShotDatabaseHelper(context);
			for(int i = 0; i < clubs.length; i++)
			{
				ArrayList<FullShot> shots = db.getAllShotsWithClub(clubs[i].getName());
				shotsByClub.add(shots);
				publishProgress((int)(i+1/(float)clubs.length)*100);
				if(isCancelled())break;
			}
			db.close();
			return (long)0;
		}
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			progressDialog.show();
		}
		
		protected void onProgressUpdate(Integer... progress)
		{
			super.onProgressUpdate(progress);
			progressDialog.setProgress(progress[0]);
		}
		
		@Override
		protected void onPostExecute(Long result)
		{
			progressDialog.dismiss();
			updateDebugTextView(0);
		}
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.club_stats, menu);
		return true;
	}
}
