package com.example.golfapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.golfapp.model.Hole;
import com.example.golfapp.model.Round;

public class ScoreCardActivity extends Activity
{

	private boolean personalScoreCard;
	private Round round;
	private TableLayout table;
	private Button switchNineButton;
	int lastStart = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_score_card);
		Intent intent = this.getIntent();
		Bundle b = intent.getExtras();
		personalScoreCard=b.getBoolean("personal");
		Parcelable parcelable = b.getParcelable("round");
		if(parcelable instanceof Round)
			round = (Round)parcelable;
		else
			finish();
		table = (TableLayout)findViewById(R.id.scoreCardScoreCard);
		switchNineButton = (Button)findViewById(R.id.scoreCardSwitchNine);
		if(round.getStartingHole()==10)
			switchNineButton.setText("Front Nine");
		else
			switchNineButton.setText("Back Nine");
		lastStart = round.getStartingHole();
		setUpTable(round.getStartingHole());
		switchNineButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0)
			{
				if(lastStart == 1)
				{
					setUpTable(10);
					lastStart = 10;
					switchNineButton.setText("Front Nine");
				}
				else
				{
					setUpTable(1);
					lastStart = 1;
					switchNineButton.setText("Back Nine");
				}
				table.invalidate();
				switchNineButton.invalidate();
				
			}
			
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{ 
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.score_card, menu);
		return true;
	}
	
	private void setUpTable(int startHole)
	{
		table.removeAllViews();
		table.setBackgroundColor(Color.BLACK);
		TableLayout.LayoutParams params = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
		//table.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
		params.setMargins(2, 2, 2, 2);
		TableRow headerRow= new TableRow(this);
		headerRow.setBackgroundColor(Color.BLUE);
		headerRow.setPadding(5, 0, 5, 0);
		headerRow.setLayoutParams(params);
		TableRow yardageRow = new TableRow(this);
		yardageRow.setBackgroundColor(0xFF2083E6);
		yardageRow.setPadding(5,0,5,0);
		yardageRow.setLayoutParams(params);
		TableRow parRow = new TableRow(this);
		parRow.setBackgroundColor(Color.BLUE);
		parRow.setPadding(5,0,5,0);
		parRow.setLayoutParams(params);
		TableRow hdcpRow = new TableRow(this);
		hdcpRow.setBackgroundColor(0xFF2083E6);
		hdcpRow.setPadding(5,0,5,0);
		hdcpRow.setLayoutParams(params);
		TextView tv1 = new TextView(this);
		tv1.setText("Player");
		tv1.setGravity(Gravity.CENTER);
		table.setColumnStretchable(0, true);
		tv1.setTextColor(Color.WHITE);
		TextView dummy1 = new TextView(this);
		dummy1.setText(" ");
		dummy1.setBackgroundColor(Color.BLACK);
		headerRow.addView(tv1);
		headerRow.addView(dummy1);
		
		TextView yardageTV = new TextView(this);
		yardageTV.setText("Yards");
		yardageTV.setGravity(Gravity.CENTER);
		yardageTV.setTextColor(Color.WHITE);
		TextView yardageDummy = new TextView(this);
		yardageDummy.setText(" ");
		yardageDummy.setBackgroundColor(Color.BLACK);
		yardageRow.addView(yardageTV);
		yardageRow.addView(yardageDummy);
		
		TextView parTV = new TextView(this);
		parTV.setText("Par");
		parTV.setGravity(Gravity.CENTER);
		parTV.setTextColor(Color.WHITE);
		TextView parDummy = new TextView(this);
		parDummy.setText(" ");
		parDummy.setBackgroundColor(Color.BLACK);
		parRow.addView(parTV);
		parRow.addView(parDummy);
		
		TextView hdcpTV = new TextView(this);
		hdcpTV.setText("Hdcp");
		hdcpTV.setGravity(Gravity.CENTER);
		hdcpTV.setTextColor(Color.WHITE);
		TextView hdcpDummy = new TextView(this);
		hdcpDummy.setText(" ");
		hdcpDummy.setBackgroundColor(Color.BLACK);
		hdcpRow.addView(hdcpTV);
		hdcpRow.addView(hdcpDummy);
	
		
		int yardsSum = 0;
		int parSum = 0;
		
		
		Hole[] holes = round.getHoles();
		for(int i = startHole; i < startHole + 9; i++)
		{
			TextView tv = new TextView(this);
			tv.setText(""+(i));
			tv.setGravity(Gravity.CENTER);
			table.setColumnStretchable((i%10)*2, true);
			tv.setTextColor(Color.WHITE);
			TextView dummy = new TextView(this);
			dummy.setText(" ");
			dummy.setBackgroundColor(Color.BLACK);
			
			headerRow.addView(tv);
			headerRow.addView(dummy);
			
			TextView yards = new TextView(this);
			yards.setTextColor(Color.WHITE);
			TextView par = new TextView(this);
			par.setTextColor(Color.WHITE);
			TextView hdcp = new TextView(this);
			hdcp.setTextColor(Color.WHITE);
			if(round.getStartingHole()== startHole)
			{
				yards.setText(holes[i-1].getYardage()+"");
				par.setText(holes[i-1].getPar() + "");
				hdcp.setText(holes[i-1].getHandicap()+"");
				yardsSum += holes[i-1].getYardage();
				parSum += holes[i-1].getPar();
			}
			else
			{
				yards.setText(holes[i-1].getYardage()+"");
				par.setText(holes[i-1].getPar()+"");
				hdcp.setText(holes[i-1].getHandicap()+"");
				yardsSum += holes[i-1].getYardage();
				parSum += holes[i-1].getPar();
			}
			yards.setGravity(Gravity.CENTER);
			TextView dummyYards = new TextView(this);
			dummyYards.setText(" ");
			dummyYards.setBackgroundColor(Color.BLACK);
			
			par.setGravity(Gravity.CENTER);
			TextView dummyPar = new TextView(this);
			dummyPar.setText(" ");
			dummyPar.setBackgroundColor(Color.BLACK);
			
			hdcp.setGravity(Gravity.CENTER);
			TextView dummyhdcp = new TextView(this);
			dummyhdcp.setText(" ");
			dummyhdcp.setBackgroundColor(Color.BLACK);
			
			yardageRow.addView(yards);
			yardageRow.addView(dummyYards);
			parRow.addView(par);
			parRow.addView(dummyPar);
			hdcpRow.addView(hdcp);
			hdcpRow.addView(dummyhdcp);
		}
		table.setColumnStretchable(20, true);
		TextView inOut = new TextView(this);
		if(startHole == 1)
			inOut.setText("Out");
		else
			inOut.setText("In");
		inOut.setGravity(Gravity.CENTER);
		inOut.setTextColor(Color.WHITE);
		
		TextView yardSum = new TextView(this);
		yardSum.setText(yardsSum+"");
		yardSum.setGravity(Gravity.CENTER);
		yardSum.setTextColor(Color.WHITE);
		
		TextView pSum = new TextView(this);
		pSum.setText(parSum+"");
		pSum.setGravity(Gravity.CENTER);
		pSum.setTextColor(Color.WHITE);
		
		headerRow.addView(inOut);
		yardageRow.addView(yardSum);
		parRow.addView(pSum);
		table.addView(headerRow);
		table.addView(yardageRow);
		table.addView(parRow);
		table.addView(hdcpRow);
		
		if(personalScoreCard)
		{
			TableRow scoreRow= new TableRow(this);
			scoreRow.setBackgroundColor(Color.WHITE);
			scoreRow.setPadding(5, 0, 5, 0);
			scoreRow.setLayoutParams(params);
			
			TableRow puttRow= new TableRow(this);
			puttRow.setBackgroundColor(Color.WHITE);
			puttRow.setPadding(5, 0, 5, 0);
			puttRow.setLayoutParams(params);
			
			TableRow fairwayRow= new TableRow(this);
			fairwayRow.setBackgroundColor(Color.WHITE);
			fairwayRow.setPadding(5, 0, 5, 0);
			fairwayRow.setLayoutParams(params);
			
			TableRow greenRow= new TableRow(this);
			greenRow.setBackgroundColor(Color.WHITE);
			greenRow.setPadding(5, 0, 5, 0);
			greenRow.setLayoutParams(params);
			
			TextView scoreTV = new TextView(this);
			scoreTV.setText("Score");
			scoreTV.setGravity(Gravity.CENTER);
			scoreTV.setTextColor(Color.BLACK);
			TextView scoreDummy = new TextView(this);
			scoreDummy.setText(" ");
			scoreDummy.setBackgroundColor(Color.BLACK);
			scoreRow.addView(scoreTV);
			scoreRow.addView(scoreDummy);
			
			TextView puttTV = new TextView(this);
			puttTV.setText("Putt");
			puttTV.setGravity(Gravity.CENTER);
			puttTV.setTextColor(Color.BLACK);
			TextView puttDummy = new TextView(this);
			puttDummy.setText(" ");
			puttDummy.setBackgroundColor(Color.BLACK);
			puttRow.addView(puttTV);
			puttRow.addView(puttDummy);
			
			TextView fairwayTV = new TextView(this);
			fairwayTV.setText("Fairway");
			fairwayTV.setGravity(Gravity.CENTER);
			fairwayTV.setTextColor(Color.BLACK);
			TextView fairwayDummy = new TextView(this);
			fairwayDummy.setText(" ");
			fairwayDummy.setBackgroundColor(Color.BLACK);
			fairwayRow.addView(fairwayTV);
			fairwayRow.addView(fairwayDummy);
			
			TextView greenTV = new TextView(this);
			greenTV.setText("Green");
			greenTV.setGravity(Gravity.CENTER);
			greenTV.setTextColor(Color.BLACK);
			TextView greenDummy = new TextView(this);
			greenDummy.setText(" ");
			greenDummy.setBackgroundColor(Color.BLACK);
			greenRow.addView(greenTV);
			greenRow.addView(greenDummy);
			
			int scoreSum = 0;
			int puttSum = 0;
			int hitFairways = 0;
			int possibleFairways = 0;
			int hitGreens = 0;
			
			for(int i = startHole; i < startHole + 9; i++)
			{
				TextView score = new TextView(this);
				TextView putt = new TextView(this);
				View fairwayView = new TextView(this);
				TextView fairway = (TextView)fairwayView;
				fairway.setGravity(Gravity.CENTER);
				View greenView = new TextView(this);
				TextView green = (TextView)greenView;
				green.setGravity(Gravity.CENTER);
				if(round.getStartingHole()== startHole)
				{
					score.setText(holes[i-1].getScore(0)+"");
					putt.setText(holes[i-1].getPutts()+"");
					scoreSum+=holes[i-1].getScore(0);
					puttSum+=holes[i-1].getPutts();
					if(holes[i-1].hitFairway())
					{
						ImageView image= new ImageView(this);
						image.setImageResource(R.drawable.check_mark);
						fairwayView = image;
						hitFairways++;
						possibleFairways++;
					}
					else if(holes[i-1].getPar()!=3)
					{
						fairway.setText("X");
						fairway.setTextColor(Color.RED);
						possibleFairways++;
					}
					
					if(holes[i-1].hitGreen())
					{
						ImageView image= new ImageView(this);
						image.setImageResource(R.drawable.check_mark);
						greenView = image;
						hitGreens++;
					}
					else
					{
						green.setText("X");
						green.setTextColor(Color.RED);
					}
					
				}
				else
				{
					score.setText(holes[i-1].getScore(0)+"");
					putt.setText(holes[i-1].getPutts()+"");
					scoreSum+=holes[i-1].getScore(0);
					puttSum+=holes[i-1].getPutts();
					if(holes[i-1].hitFairway())
					{
						ImageView image= new ImageView(this);
						image.setImageResource(R.drawable.check_mark);
						fairwayView = image;
						hitFairways++;
						possibleFairways++;
					}
					else if(holes[i-1].getPar()!=3)
					{
						fairway.setText("X");
						fairway.setTextColor(Color.RED);
						possibleFairways++;
					}
					
					if(holes[i-1].hitGreen())
					{
						ImageView image= new ImageView(this);
						image.setImageResource(R.drawable.check_mark);
						greenView = image;
						hitGreens++;
					}
					else
					{
						green.setText("X");
						green.setTextColor(Color.RED);
					}
				}
				score.setGravity(Gravity.CENTER);
				score.setTextColor(Color.BLACK);
				TextView scoreDummyTv = new TextView(this);
				scoreDummyTv.setText(" ");
				scoreDummyTv.setBackgroundColor(Color.BLACK);
				
				putt.setGravity(Gravity.CENTER);
				putt.setTextColor(Color.BLACK);
				TextView puttDummyTv = new TextView(this);
				puttDummyTv.setText(" ");
				puttDummyTv.setBackgroundColor(Color.BLACK);
				
				
				TextView fairwayDummyTv = new TextView(this);
				fairwayDummyTv.setText(" ");
				fairwayDummyTv.setBackgroundColor(Color.BLACK);
				
				TextView greenDummyTv = new TextView(this);
				greenDummyTv.setText(" ");
				greenDummyTv.setBackgroundColor(Color.BLACK);
				
				scoreRow.addView(score);
				scoreRow.addView(scoreDummyTv);
				puttRow.addView(putt);
				puttRow.addView(puttDummyTv);
				fairwayRow.addView(fairwayView);
				fairwayRow.addView(fairwayDummyTv);
				greenRow.addView(greenView);
				greenRow.addView(greenDummyTv);
			}
			
			TextView sSum = new TextView(this);
			sSum.setText(scoreSum+"");
			sSum.setGravity(Gravity.CENTER);
			sSum.setTextColor(Color.BLACK);
			
			TextView puSum = new TextView(this);
			puSum.setText(puttSum+"");
			puSum.setGravity(Gravity.CENTER);
			puSum.setTextColor(Color.BLACK);
			
			TextView fSum = new TextView(this);
			fSum.setText(hitFairways+"/"+possibleFairways);
			fSum.setGravity(Gravity.CENTER);
			fSum.setTextColor(Color.BLACK);
			
			TextView gSum = new TextView(this);
			gSum.setText(hitGreens+"/"+9);
			gSum.setGravity(Gravity.CENTER);
			gSum.setTextColor(Color.BLACK);
			
			scoreRow.addView(sSum);
			puttRow.addView(puSum);
			fairwayRow.addView(fSum);
			greenRow.addView(gSum);
			table.addView(scoreRow);
			table.addView(puttRow);
			table.addView(fairwayRow);
			table.addView(greenRow);
		}
		else
		{
			for(int i = 0; i < round.getNumberOfPlayers(); i++)
			{
				TableRow scoreRow= new TableRow(this);
				scoreRow.setBackgroundColor(Color.WHITE);
				scoreRow.setPadding(5, 0, 5, 0);
				scoreRow.setLayoutParams(params);
				
				TextView scoreTV = new TextView(this);
				if(i==0)
				{
					scoreTV.setText("Me");
				}
				else
				{
					scoreTV.setText(round.getPlayers()[i-1]);
				}
				scoreTV.setGravity(Gravity.CENTER);
				scoreTV.setTextColor(Color.BLACK);
				TextView scoreDummy = new TextView(this);
				scoreDummy.setText(" ");
				scoreDummy.setBackgroundColor(Color.BLACK);
				scoreRow.addView(scoreTV);
				scoreRow.addView(scoreDummy);
				int scoreSum = 0;
				for(int j = startHole; j< startHole + 9; j++)
				{
					TextView score = new TextView(this);
					if(round.getStartingHole()== startHole)
					{
						score.setText(holes[j-1].getScore(i)+"");
						scoreSum+=holes[j-1].getScore(i);
					}
					else
					{
						score.setText(holes[j-1].getScore(i)+"");
						scoreSum+=holes[j-1].getScore(i);
					}
					score.setGravity(Gravity.CENTER);
					score.setTextColor(Color.BLACK);
					TextView scoreDummyTv = new TextView(this);
					scoreDummyTv.setText(" ");
					scoreDummyTv.setBackgroundColor(Color.BLACK);
					scoreRow.addView(score);
					scoreRow.addView(scoreDummyTv);
				}
				TextView sSum = new TextView(this);
				sSum.setText(scoreSum+"");
				sSum.setGravity(Gravity.CENTER);
				sSum.setTextColor(Color.BLACK);
				scoreRow.addView(sSum);
				table.addView(scoreRow);
				
			}
		}
	}

}
