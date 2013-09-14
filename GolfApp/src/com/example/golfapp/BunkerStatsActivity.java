package com.example.golfapp;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.golfapp.model.Putt;
import com.example.golfapp.model.ShortGameShot;
import com.example.golfapp.sql.ShotDatabaseHelper;

public class BunkerStatsActivity extends Activity {

	static final String[] numbers = new String[] { "Green %", "", "51+ ft", "",
			"36-50ft", "", "21-35ft", "", "11-20ft", "", "5-10ft", "", "1-4ft",
			"", "In Hole", "", "Putt Ave.", "", "1 Putt %", "" };
	static final String[] distro = new String[]{"","","","","","","","",""};
	private static final int GREEN = 0;
	private static final int FIFTY_ONE = 1;
	private static final int THIRTY_SIX = 2;
	private static final int TWENTY_ONE = 3;
	private static final int ELEVEN = 4;
	private static final int FIVE = 5;
	private static final int ONE = 6;
	private static final int IN_HOLE = 7;
	private static final int PUTT_AVE = 8;
	private static final int ONE_PUTT = 9;
	private LinearLayout layout;
	private Context context;
	private GridView grid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bunker_stats);
		context = this;
		layout = (LinearLayout) findViewById(R.id.bunkerDummyLayout);
		addGridView();
		GetBunkerStuffFromSQL stuff = new GetBunkerStuffFromSQL();
		stuff.execute();
		
		Button table = (Button)findViewById(R.id.bunkerTableButton);
		table.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				clearAllViewsFromLayout();
				addGridView();
				
			}
			
		});
		Button distribution = (Button)findViewById(R.id.bunkerDistributionButton);
		distribution.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				clearAllViewsFromLayout();
				addDistribution();
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bunker_stats, menu);
		return true;
	}

	private void addGridView() {
		grid = new GridView(this);
		grid.setNumColumns(2);
		grid.setBackgroundColor(Color.WHITE);
		grid.setVerticalSpacing(10);
		grid.setHorizontalSpacing(10);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.list_item, numbers);

		grid.setAdapter(adapter);
		layout.addView(grid);
	}
	private void addDistribution(){
		GridLayout distribution = new GridLayout(context);
		distribution.setRowCount(3);
		distribution.setColumnCount(3);
		distribution.setBackground(getResources().getDrawable(R.drawable.target));
		distribution.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
		for(int i = 0; i < distro.length; i++){
			TextView tv = new TextView(this);
			tv.setText(distro[i]);
			tv.setMinimumWidth(layout.getWidth()/3);
			tv.setMinimumHeight(layout.getHeight()/3);
			tv.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
			tv.setTextColor(Color.WHITE);
			tv.setTextAppearance(this, android.R.attr.textAppearanceLarge);
			distribution.addView(tv);
		}
		layout.addView(distribution);
		/*GridView distribution = new GridView(this);
		distribution.setNumColumns(3);
		distribution.setBackground(getResources().getDrawable(R.drawable.target));
		distribution.setStretchMode(GridView.STRETCH_SPACING_UNIFORM);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.transparent_list_item, vals);
		distribution.setAdapter(adapter);
		layout.addView(distribution);
		distribution.getChildAt(0).setMinimumHeight(distribution.getHeight()/3);
		distribution.getChildAt(3).setMinimumHeight(distribution.getHeight()/3);
		distribution.getChildAt(6).setMinimumHeight(distribution.getHeight()/3);*/
	}
	private void clearAllViewsFromLayout(){
		layout.removeAllViews();
	}

	private class GetBunkerStuffFromSQL extends
			AsyncTask<Integer, Integer, Integer> {
		
		int numHitGreen=0;
		int numFiftyOne=0;
		int numThirtySix=0;
		int numTwentyOne=0;
		int numEleven=0;
		int numFive=0;
		int numOne=0;
		int numInHole=0;
		int totalPutts=0;
		int onePutts = 0;
		int shotsSize=0;
		
		int numLongLeft=0;
		int numLong = 0;
		int numLongRight = 0;
		int numLeft = 0;
		int numOnTarget = 0;
		int numRight = 0;
		int numShortLeft = 0;
		int numShort = 0;
		int numShortRight = 0;
		protected Integer doInBackground(Integer... dummy) {
			ShotDatabaseHelper db = new ShotDatabaseHelper(context);

			ArrayList<ShortGameShot> shots = db.getAllBunkerShots();
			shotsSize=shots.size();
			for(ShortGameShot s: shots){
				numHitGreen+=s.isHitGreen()?1:0;
				switch(s.getFinalDistRange()){
				case Putt.FIFTYONE_PLUS:
					numFiftyOne++;
					break;
				case Putt.THIRTYSIX_FIFTY:
					numThirtySix++;
					break;
				case Putt.TWENTYONE_THIRTYFIVE:
					numTwentyOne++;
					break;
				case Putt.ELEVEN_TWENTY:
					numEleven++;
					break;
				case Putt.FIVE_TEN:
					numFive++;
					break;
				case Putt.ONE_FOUR:
					numOne++;
					break;
				case Putt.IN_HOLE:
					numInHole++;
					break;
				}
				switch(s.getMissDirection()){
				case ShortGameShot.LONG_LEFT:
					numLongLeft++;
					break;
				case ShortGameShot.LONG:
					numLong++;
					break;
				case ShortGameShot.LONG_RIGHT:
					numLongRight++;
					break;
				case ShortGameShot.LEFT:
					numLeft++;
					break;
				case ShortGameShot.ON_TARGET:
					numOnTarget++;
					break;
				case ShortGameShot.RIGHT:
					numRight++;
					break;
				case ShortGameShot.SHORT_LEFT:
					numShortLeft++;
					break;
				case ShortGameShot.SHORT:
					numShort++;
					break;
				case ShortGameShot.SHORT_RIGHT:
					numShortRight++;
					break;
				}
				totalPutts+=s.getPuttsAfter();
				onePutts+=s.getPuttsAfter()==1?1:0;
			}
			db.close(); 
			
			
			
			return (int) 0;
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected void onPostExecute(Integer result) {
			if(shotsSize==0){
				for(int i = 1; i < numbers.length; i+=2)
					numbers[i]="0%";
				for(int i = 0; i < distro.length; i++)
					distro[i]="0%";
				return;
			}
			Log.d("SHOTS",shotsSize+"");
			Log.d("GREENS",numHitGreen+"");
			numbers[1+2*GREEN]=(100.0*numHitGreen/((float)shotsSize))+"%";
			numbers[1+2*FIFTY_ONE]=(100.0*numFiftyOne/((float)shotsSize))+"%";
			numbers[1+2*THIRTY_SIX]=(100.0*numThirtySix/((float)shotsSize))+"%";
			numbers[1+2*TWENTY_ONE]=(100.0*numTwentyOne/((float)shotsSize))+"%";
			numbers[1+2*ELEVEN]=(100.0*numEleven/((float)shotsSize))+"%";
			numbers[1+2*FIVE]=(100.0*numFive/((float)shotsSize))+"%";
			numbers[1+2*ONE]=(100.0*numOne/((float)shotsSize))+"%";
			numbers[1+2*IN_HOLE]=(100.0*numInHole/((float)shotsSize))+"%";
			numbers[1+2*PUTT_AVE]=(totalPutts/((float)shotsSize))+"";
			numbers[1+2*ONE_PUTT]=(100.0*onePutts/((float)shotsSize))+"%";
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
					R.layout.list_item, numbers);

			grid.setAdapter(adapter);
			grid.invalidate();
			
			distro[0]=(100.0*numLongLeft/((float)shotsSize))+"%";
			distro[1]=(100.0*numLong/((float)shotsSize))+"%";
			distro[2]=(100.0*numLongRight/((float)shotsSize))+"%";
			
			distro[3]=(100.0*numLeft/((float)shotsSize))+"%";
			distro[4]=(100.0*numOnTarget/((float)shotsSize))+"%";
			distro[5]=(100.0*numRight/((float)shotsSize))+"%";
			
			distro[6]=(100.0*numShortLeft/((float)shotsSize))+"%";
			distro[7]=(100.0*numShort/((float)shotsSize))+"%";
			distro[8]=(100.0*numShortRight/((float)shotsSize))+"%";
		}
	}

}
