package com.example.golfapp;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.golfapp.model.Hole;
import com.example.golfapp.model.stats.HoleStats;
import com.example.golfapp.sql.ShotDatabaseHelper;
import com.example.golfapp.xml.CourseParser;

public class CourseStatsActivity extends Activity {

	private Spinner courseSpinner;
	private File [] courses;
	private ScrollView scroll;
	private String [] courseFileNames;
	private Context context;
	private ProgressDialog progressDialog;
	private ArrayList<HoleStats> holeStats;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_course_stats);
		courseSpinner = (Spinner)findViewById(R.id.courseStatsCourseSpinner);
		File coursesDir = new File(getFilesDir(),"courses");
		courses = coursesDir.listFiles();
		courseFileNames = new String[courses.length+1];
		courseFileNames[0]="--Select a Course--";
		for(int i = 1; i < courses.length+1; i++)
		{
			courseFileNames[i] = CourseParser.getCourseName(courses[i-1]);
		}
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, courseFileNames);
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		courseSpinner.setAdapter(spinnerArrayAdapter);
		
		scroll = (ScrollView)findViewById(R.id.courseStatsScrollView);
		context = this;
		holeStats = new ArrayList<HoleStats>();
		courseSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				TextView title = (TextView)findViewById(R.id.courseStatsTitleTextView);
				if(arg2==0)
				{
					//Clear
					title.setText("Course Stats");
					scroll.removeAllViewsInLayout();
					scroll.removeAllViews();
					scroll.invalidate();
					holeStats = null;
					return;
				}
				holeStats = new ArrayList<HoleStats>();
				String name = courseFileNames[arg2];
				progressDialog = new ProgressDialog(context);
				progressDialog.setMessage("Compiling Stats");
				progressDialog.setMax(100);
				progressDialog.setIndeterminate(false);
				progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				title.setText(name);
				GetCourseStuffFromSQL getStuff = new GetCourseStuffFromSQL();
				getStuff.execute(courses[arg2-1]);
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	private class GetCourseStuffFromSQL extends AsyncTask<File, Integer, Long>
	{
		protected Long doInBackground(File... courses)
		{
			ShotDatabaseHelper db = new ShotDatabaseHelper(context);
			File course = courses[0];
			if(course == null)
				return (long)0;
			int numHoles = CourseParser.getNumberOfHoles(course);
			String courseName = CourseParser.getCourseName(course);
			ArrayList<Hole> holes;
			for(int i = 0; i < numHoles; i++)
			{
				holes =db.getAllHolesByHole(courseName, i+1);
				int scoreSum = 0;
				int puttSum = 0;
				int fairwaySum = 0;
				int greenSum = 0;
				int possible = holes.size();
				int par = 0;
				for(Hole h: holes)
				{
					if(h.getPar()==3)
						possible = 0;
					scoreSum+=h.getScore(0);
					puttSum += h.getPutts();
					fairwaySum+=h.hitFairway()?1:0;
					greenSum+= h.hitGreen()?1:0;
					par = h.getPar();
				}
				HoleStats stats = new HoleStats((i+1)+"", scoreSum*1.0/holes.size(), puttSum*1.0/holes.size(), fairwaySum, greenSum, possible, holes.size(), par);
				holeStats.add(stats);
				int x = (int)(((i+1.0)/(1.0+numHoles))*100);
				publishProgress(x);
			}
			HoleStats frontStats=null;
			HoleStats backStats=null;
			HoleStats totalStats=null;
			if(numHoles>=9)
			{
				double scoreSum = 0;
				double puttSum = 0;
				int fairwaySum = 0;
				int greenSum = 0;
				int possible = 0;
				int timesPlayed = 0;
				int par = 0;
				HoleStats stats;
				for(int i = 0; i < 9; i++)
				{
					stats = holeStats.get(i);
					scoreSum+=stats.getAverageScore();
					puttSum+=stats.getAveragePutts();
					fairwaySum+=stats.getFairwaysHit();
					greenSum+=stats.getGreensHit();
					possible += stats.getPossibleFairways();
					timesPlayed += stats.getTimesPlayed();
					par+=stats.getPar();
				}
				frontStats = new HoleStats("Front", scoreSum, puttSum, fairwaySum, greenSum, possible, timesPlayed, par);
			}
			if(numHoles==18)
			{
				double scoreSum = 0;
				double puttSum = 0;
				int fairwaySum = 0;
				int greenSum = 0;
				int possible = 0;
				int timesPlayed = 0;
				int par = 0;
				HoleStats stats;
				for(int i = 9; i < 18; i++)
				{
					stats = holeStats.get(i);
					scoreSum+=stats.getAverageScore();
					puttSum+=stats.getAveragePutts();
					fairwaySum+=stats.getFairwaysHit();
					greenSum+=stats.getGreensHit();
					possible += stats.getPossibleFairways();
					timesPlayed += stats.getTimesPlayed();
					par += stats.getPar();
				}
				backStats = new HoleStats("Back", scoreSum, puttSum, fairwaySum, greenSum, possible, timesPlayed, par);
			}
			
			double scoreSum = 0;
			double puttSum = 0;
			int fairwaySum = 0;
			int greenSum = 0;
			int possible = 0;
			int timesPlayed = 0;
			int par = 0;
			HoleStats stats;
			for(int i = 0; i < numHoles; i++)
			{
				stats = holeStats.get(i);
				scoreSum+=stats.getAverageScore();
				puttSum+=stats.getAveragePutts();
				fairwaySum+=stats.getFairwaysHit();
				greenSum+=stats.getGreensHit();
				possible += stats.getPossibleFairways();
				timesPlayed += stats.getTimesPlayed();
				par+= stats.getPar();
			}
			totalStats = new HoleStats("Total", scoreSum, puttSum, fairwaySum, greenSum, possible, timesPlayed, par);
			
			if(frontStats!= null)
				holeStats.add(9, frontStats);
			if(backStats!=null)
				holeStats.add(backStats);
			holeStats.add(totalStats);
			publishProgress(100);
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
			//DRAW THE TABLE
			TableLayout layout = new TableLayout(context);
			layout.removeAllViews();
			layout.removeAllViewsInLayout();
			layout.setPadding(5, 0, 5, 0);
			layout.setBackgroundColor(Color.WHITE);
			TableLayout.LayoutParams params = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
			layout.setLayoutParams(params);
			params.setMargins(2, 2, 2, 2);
			TableRow headerRow= new TableRow(context);
			headerRow.setLayoutParams(params);
			headerRow.setPadding(5, 0, 5, 0);
			headerRow.setBackgroundColor(Color.BLACK);
			String[] cols = {" Hole ", " "," Par "," ", " Score "," "," Putts "," ", " Fairway "," ", " Greens "};
			for(int i = 0; i < cols.length; i++)
			{
				TextView tv = new TextView(context);
				//tv.setPadding(5, 0, 5, 0);
				tv.setText(cols[i]);
				if(i%2==1)
					tv.setBackgroundColor(Color.WHITE);
				headerRow.addView(tv);
			}
			layout.addView(headerRow);
			for(HoleStats hs: holeStats)
			{
				
				TableRow row = new TableRow(context);
				row.setOrientation(LinearLayout.HORIZONTAL);
				row.setLayoutParams(params);
				row.setBackgroundColor(Color.BLACK);
				TextView tv = new TextView(context);				
				tv.setText(hs.getName());
				row.addView(tv);
				TextView dummy = new TextView(context);
				dummy.setText(" ");
				dummy.setBackgroundColor(Color.WHITE);
				row.addView(dummy);
				
				
				tv = new TextView(context);
				tv.setText(hs.getPar()+"");
				row.addView(tv);
				dummy=new TextView(context);
				dummy.setText(" ");
				dummy.setBackgroundColor(Color.WHITE);
				row.addView(dummy);
				
				tv = new TextView(context);
				tv.setText(hs.getAverageScoreString()+"");
				row.addView(tv);
				dummy=new TextView(context);
				dummy.setText(" ");
				dummy.setBackgroundColor(Color.WHITE);
				row.addView(dummy);
				
				tv = new TextView(context);
				tv.setText(hs.getAveragePuttString()+"");
				row.addView(tv);
				dummy=new TextView(context);
				dummy.setText(" ");
				dummy.setBackgroundColor(Color.WHITE);
				row.addView(dummy);
				
				tv = new TextView(context);
				tv.setText(hs.getFairwayPercentString()+"");
				row.addView(tv);
				dummy=new TextView(context);
				dummy.setText(" ");
				dummy.setBackgroundColor(Color.WHITE);
				row.addView(dummy);
				
				tv = new TextView(context);
				tv.setText(hs.getGreenPercentString()+"");
				row.addView(tv);

				
				row.setPadding(5,0,5,0);
				layout.addView(row);
			}
			scroll.removeAllViews();
			scroll.addView(layout);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.course_stats, menu);
		return true;
	}

}
