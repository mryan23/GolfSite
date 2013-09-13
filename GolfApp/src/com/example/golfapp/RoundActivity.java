package com.example.golfapp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.golfapp.model.Club;
import com.example.golfapp.model.FullShot;
import com.example.golfapp.model.Hole;
import com.example.golfapp.model.Round;
import com.example.golfapp.model.ShortGameShot;
import com.example.golfapp.model.Shot;
import com.example.golfapp.sql.ShotDatabaseHelper;
import com.example.golfapp.xml.ClubParser;
import com.example.golfapp.xml.RoundWriter;

public class RoundActivity extends FragmentActivity {

	private TextView activityTrackingLabel;
	private Button trackShotButton;

	int holeIndex = 0;
	boolean firstTime = true;

	private Round round = new Round();
	private Location shotStartLocation, myCurrentLocation;
	private Club[] clubs;
	private FullShot currentFullShot;

	private Spinner trackerClubSpinner, trackerDirectionSpinner,
			trackerSurfaceSpinner, trackerShapeSpinner, clubSpinner;
	private CheckBox trackerTeeCheckBox;
	private int clubIndex = 0;
	private TextView trackerYardView;
	private NumberPicker score;
	private NumberPicker putts;

	private Context thisContext = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.holes);

		Bundle b = this.getIntent().getExtras();
		if (b != null) {
			Parcelable parcel = b.getParcelable("Round");
			if (parcel instanceof Round)
				round = (Round) parcel;
		} else {
			finish();
		}
		TextView holeInfo = (TextView) findViewById(R.id.holeInfoTV);
		activityTrackingLabel = (TextView) findViewById(R.id.trackShotTextView);
		holeInfo.setText(round.getHoles()[0].toString());
		setupHoleListeners();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode != 1)
			return;
		if (resultCode == Activity.RESULT_CANCELED)
			return;

		Bundle b = data.getExtras();
		Parcelable dummyItem = b.getParcelable("ShortGameShot");
		ShortGameShot sgs = null;
		if (dummyItem instanceof ShortGameShot) {
			sgs = (ShortGameShot) dummyItem;
			Toast.makeText(getApplicationContext(), sgs.toString(),
					Toast.LENGTH_LONG).show();
			round.getHoles()[holeIndex].addShot(sgs);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.round, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.endRound:
			endRound();
			finish();
			return true;
		case R.id.myScoreCard: {
			Intent i = new Intent(this, ScoreCardActivity.class);
			i.putExtra("personal", true);
			i.putExtra("round", round);
			startActivity(i);
			return true;
		}
		case R.id.groupScoreCard: {
			Intent i = new Intent(this, ScoreCardActivity.class);
			i.putExtra("personal", false);
			i.putExtra("round", round);
			startActivity(i);
			return true;
		}
		default:
			return true;

		}
	}

	private void endRound() {
		RoundWriter writer = new RoundWriter(round, this);
		new File(getFilesDir(), "rounds").mkdir();
		writer.write(new File(getFilesDir(), "rounds/" + round.getName()
				+ ".xml").getAbsolutePath());
		ShotDatabaseHelper db = new ShotDatabaseHelper(thisContext);
		db.addRound(round);
		for (Hole h : round.getHoles()) {
			db.addHole(h, round);
			for (Shot s : h.getShots()) {
				if (s instanceof FullShot)
					db.addFullShot((FullShot) s);
			}
		}
	}

	private void setupHoleListeners() {

		final Hole[] holes = round.getHoles();
		final Spinner holesSpinner = (Spinner) findViewById(R.id.currentHoleSpinner);
		score = (NumberPicker) findViewById(R.id.scoreNumberPicker);
		putts = (NumberPicker) findViewById(R.id.puttsNumberPicker);
		score.setMinValue(0);
		score.setMaxValue(20);
		score.setOverScrollMode(NumberPicker.OVER_SCROLL_NEVER);
		score.setWrapSelectorWheel(false);
		putts.setMinValue(0);
		putts.setMaxValue(20);
		putts.setOverScrollMode(NumberPicker.OVER_SCROLL_NEVER);
		putts.setWrapSelectorWheel(false);
		score.setOnValueChangedListener(new OnValueChangeListener() {

			@Override
			public void onValueChange(NumberPicker picker, int oldVal,
					int newVal) {
				Hole hole = holes[holesSpinner.getSelectedItemPosition()];
				if (newVal > oldVal || putts.getValue() < newVal)
					hole.setScore(0, newVal);
				else {
					hole.setScore(0, newVal);
					// hole.setPutts(newVal);
					putts.setValue(newVal);
					hole.setPutts(newVal);
				}

			}

		});
		putts.setOnValueChangedListener(new OnValueChangeListener() {

			@Override
			public void onValueChange(NumberPicker arg0, int oldVal, int newVal) {
				Hole hole = holes[holesSpinner.getSelectedItemPosition()];
				hole.setPutts(newVal);
				if (newVal > oldVal || oldVal != score.getValue()) {
					score.setValue(score.getValue() + newVal - oldVal);
					hole.setScore(0, score.getValue());
				} else {
					score.setValue(newVal);
					hole.setScore(0,newVal);
				}

			}

		});
		/*
		 * scorePlus.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { int scoreVal =
		 * Integer.parseInt(score.getText().toString()); Hole hole =
		 * holes[holesSpinner.getSelectedItemPosition()];
		 * score.setText((scoreVal + 1) + ""); hole.setScore(0, scoreVal + 1); }
		 * 
		 * }); scoreMinus.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { int scoreVal =
		 * Integer.parseInt(score.getText().toString()); int puttsVal =
		 * Integer.parseInt(putts.getText().toString()); Hole hole =
		 * holes[holesSpinner.getSelectedItemPosition()]; if (scoreVal > 0 &&
		 * scoreVal > puttsVal) { score.setText((scoreVal - 1) + "");
		 * hole.setScore(0, scoreVal - 1); } else if (scoreVal > 0 && scoreVal
		 * == puttsVal) { score.setText((scoreVal - 1) + ""); hole.setScore(0,
		 * scoreVal - 1); putts.setText((puttsVal - 1) + "");
		 * hole.setPutts(puttsVal - 1); } }
		 * 
		 * });
		 */

		Button gpsButton = (Button) findViewById(R.id.gpsButton);
		gpsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(getApplicationContext(),
						GPSActivity.class);
				// put parameters in b
				Hole curHole = round.getHoles()[holeIndex];
				i.putExtra("GPS Points", curHole.getPoints());
				startActivity(i);

			}

		});

		/*
		 * Button puttPlus = (Button) findViewById(R.id.puttsPlusButton);
		 * 
		 * puttPlus.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { Hole hole =
		 * holes[holesSpinner.getSelectedItemPosition()]; int puttVal =
		 * Integer.parseInt(putts.getText().toString()); putts.setText((puttVal
		 * + 1) + ""); hole.setPutts(puttVal + 1); int scoreVal =
		 * Integer.parseInt(score.getText().toString()); score.setText((scoreVal
		 * + 1) + ""); hole.setScore(0, scoreVal + 1); }
		 * 
		 * }); Button puttsMinus = (Button) findViewById(R.id.puttsMinusButton);
		 * puttsMinus.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { int puttsVal =
		 * Integer.parseInt(putts.getText().toString()); int scoreVal =
		 * Integer.parseInt(score.getText().toString()); Hole hole =
		 * holes[holesSpinner.getSelectedItemPosition()]; if (puttsVal > 0) {
		 * hole.setPutts(puttsVal - 1); putts.setText((puttsVal - 1) + "");
		 * hole.setScore(0, scoreVal - 1); score.setText((scoreVal - 1) + ""); }
		 * 
		 * }
		 * 
		 * });
		 */

		Button shortGameTracker = (Button) findViewById(R.id.shortGameTrackButton);
		shortGameTracker.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(getApplicationContext(),
						ShortGameTrackerActivity.class);
				startActivityForResult(i, 1);

			}

		});

		Button prevTeeShot = (Button) findViewById(R.id.previousRoundsButton);
		prevTeeShot.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			}

		});

		String[] holeStrings = new String[round.getNumberOfHoles()];
		for (int i = 0; i < holes.length; i++) {
			holeStrings[i] = holes[i].holeString();
		}

		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item, holeStrings);
		spinnerArrayAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		holesSpinner.setAdapter(spinnerArrayAdapter);

		holesSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				int holeIndex = holesSpinner.getSelectedItemPosition();
				switchHole(holes[holeIndex], holeIndex);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});

		clubSpinner = (Spinner) findViewById(R.id.clubSpinner);
		ClubParser parser = new ClubParser(new File(getFilesDir(), "clubs.xml"));
		clubs = parser.getClubs();
		ArrayList<String> strings = new ArrayList<String>();
		for (int i = 0; i < clubs.length; i++) {
			if (clubs[i].isEnabled())
				strings.add(clubs[i].getName());
		}
		String[] clubNames = new String[strings.size()];
		for (int i = 0; i < clubNames.length; i++)
			clubNames[i] = strings.get(i);
		ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item, clubNames);
		spinnerArrayAdapter2
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		clubSpinner.setAdapter(spinnerArrayAdapter2);

		CheckBox fairway = (CheckBox) findViewById(R.id.fairwayCheckBox);
		fairway.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				Hole hole = holes[holesSpinner.getSelectedItemPosition()];
				hole.setFairway(arg1);

			}

		});

		CheckBox green = (CheckBox) findViewById(R.id.greenCheckBox);
		green.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				Hole hole = holes[holesSpinner.getSelectedItemPosition()];
				hole.setGreen(isChecked);

			}

		});

		Button nextButton = (Button) findViewById(R.id.nextHoleButton);
		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int holeIndex = holesSpinner.getSelectedItemPosition();
				if (holeIndex < round.getNumberOfHoles() - 1)
					holesSpinner.setSelection(holeIndex + 1);

			}

		});

		Button prevButton = (Button) findViewById(R.id.prevHoleButton);
		prevButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int holeIndex = holesSpinner.getSelectedItemPosition();
				if (holeIndex > 0)
					holesSpinner.setSelection(holeIndex - 1);

			}

		});

		trackShotButton = (Button) findViewById(R.id.trackButton);
		trackShotButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog dialog = new AlertDialog.Builder(RoundActivity.this)
						.create();
				dialog.setTitle("Track Shot");
				if (shotStartLocation != null) {
					dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Save",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// Do Save stuff here
									int distance = GPSActivity.getDistance(shotStartLocation
											.distanceTo(myCurrentLocation));
									shotStartLocation = null;
									/*
									 * String clubName =
									 * currentFullShot.getClub(); boolean isTee
									 * = currentFullShot.isTeeShot(); int
									 * direction =
									 * currentFullShot.getDirection(); int shape
									 * = currentFullShot.getShape();
									 */
									clubIndex = trackerClubSpinner
											.getSelectedItemPosition();
									String clubName = trackerClubSpinner
											.getSelectedItem().toString();
									boolean isTee = trackerTeeCheckBox
											.isChecked();
									int direction = trackerDirectionSpinner
											.getSelectedItemPosition();
									int shape = trackerShapeSpinner
											.getSelectedItemPosition();
									int surface = trackerSurfaceSpinner
											.getSelectedItemPosition();

									Shot shot = new FullShot(clubName, round
											.getCourseName(),
											round.getHoles()[holeIndex]
													.getNumber(), isTee,
											direction, shape, distance, surface);
									round.getHoles()[holeIndex].addShot(shot);
									clearTrackingLabels();
									currentFullShot = null;

									Toast.makeText(getApplicationContext(),
											shot.toString(), Toast.LENGTH_LONG)
											.show();

									/*
									 * int scoreVal = Integer.parseInt(score
									 * .getText().toString()); Hole hole =
									 * holes[holesSpinner
									 * .getSelectedItemPosition()];
									 * score.setText((scoreVal + 1) + "");
									 * hole.setScore(0, scoreVal + 1);
									 */
									score.setValue(score.getValue() + 1);
									if (isTee)
										clubSpinner.setSelection(clubIndex);
									trackShotButton.setText("Track Shot");

								}
							});
				}
				dialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Hide",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// Do hide stuff here
								clubIndex = trackerClubSpinner
										.getSelectedItemPosition();
								String club = trackerClubSpinner
										.getSelectedItem().toString();
								boolean isTee = trackerTeeCheckBox.isChecked();
								int direction = trackerDirectionSpinner
										.getSelectedItemPosition();
								int shape = trackerShapeSpinner
										.getSelectedItemPosition();
								int surface = trackerSurfaceSpinner
										.getSelectedItemPosition();
								currentFullShot = new FullShot(club, "course",
										1, isTee, direction, shape, 0, surface);
								if (shotStartLocation == null)
									getCurrentLocation();
								trackShotButton.setText("End Shot");
							}

						});
				dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// Do Cancel stuff here
								shotStartLocation = null;
								currentFullShot = null;
								clearTrackingLabels();
								trackShotButton.setText("Track Shot");
							}
						});
				LayoutInflater inflater = getLayoutInflater();
				View view = inflater.inflate(R.layout.full_shot, null);
				dialog.setView(view);
				Spinner clubsSpinner = (Spinner) view
						.findViewById(R.id.trackerClubsSpinner);
				trackerDirectionSpinner = (Spinner) view
						.findViewById(R.id.trackerDirectionSpinner);
				trackerShapeSpinner = (Spinner) view
						.findViewById(R.id.trackerShapeSpinner);
				trackerTeeCheckBox = (CheckBox) view
						.findViewById(R.id.trackerTeeCheckBox);
				trackerSurfaceSpinner = (Spinner) view
						.findViewById(R.id.trackerSurfaceSpinner);
				trackerYardView = (TextView) view
						.findViewById(R.id.trackerDistanceTextView);

				trackerTeeCheckBox
						.setOnCheckedChangeListener(new OnCheckedChangeListener() {

							@Override
							public void onCheckedChanged(CompoundButton arg0,
									boolean arg1) {
								if (arg1)
									trackerSurfaceSpinner.setSelection(1);

							}

						});
				ArrayList<String> strings = new ArrayList<String>();
				for (int i = 0; i < clubs.length; i++) {
					if (clubs[i].isEnabled())
						strings.add(clubs[i].getName());
				}
				String[] clubNames = new String[strings.size()];
				for (int i = 0; i < clubNames.length; i++)
					clubNames[i] = strings.get(i);
				ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(
						getBaseContext(), android.R.layout.simple_spinner_item,
						clubNames);
				clubsSpinner.setAdapter(spinnerArrayAdapter2);
				trackerClubSpinner = clubsSpinner;

				if (currentFullShot != null) {
					trackerDirectionSpinner.setSelection(currentFullShot
							.getDirection());
					trackerShapeSpinner.setSelection(currentFullShot.getShape());
					trackerTeeCheckBox.setChecked(currentFullShot.isTeeShot());
					trackerClubSpinner.setSelection(clubIndex);
					trackerSurfaceSpinner.setSelection(currentFullShot
							.getSurface());
				}
				if (shotStartLocation != null && myCurrentLocation != null) {
					double x = shotStartLocation.distanceTo(myCurrentLocation);
					trackerYardView.setText(GPSActivity.getDistanceString(x));
				}
				dialog.show();
			}

		});

	}

	private void updateTrackingLabels() {
		double x = shotStartLocation.distanceTo(myCurrentLocation);
		activityTrackingLabel.setText(GPSActivity.getDistanceString(x));
		if (trackerYardView != null) {
			trackerYardView.setText(GPSActivity.getDistanceString(x));
		}
	}

	private void clearTrackingLabels() {
		activityTrackingLabel.setText("");
	}

	void getCurrentLocation() {
		LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		LocationListener mlocListener = new MyLocationListener(this);
		List<String> providers = mlocManager.getProviders(true);
		Location l = null;
		for (int i = providers.size() - 1; i >= 0; i--) {
			l = mlocManager.getLastKnownLocation(providers.get(i));
			if (l != null)
				break;
		}
		if (l != null) {
		}

		mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,
				1, mlocListener);
	}

	public class MyLocationListener implements LocationListener {
		RoundActivity activity;

		public MyLocationListener(RoundActivity act) {
			activity = act;
		}

		@Override
		public void onLocationChanged(Location loc) {
			if (activity.shotStartLocation == null)
				activity.shotStartLocation = loc;
			activity.myCurrentLocation = loc;
			activity.updateTrackingLabels();

			// Toast.makeText( getApplicationContext(), Text,
			// Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onProviderDisabled(String provider) {
			Toast.makeText(getApplicationContext(), "Gps Disabled",
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onProviderEnabled(String provider) {
			Toast.makeText(getApplicationContext(), "Gps Enabled",
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {

		}
	}

	private void switchHole(Hole hole, int holeIndex) {
		if (round.getNumberOfPlayers() > 1 && !firstTime) {
			ScoreDialog sd = new ScoreDialog();
			sd.setHoleIndex(this.holeIndex);
			sd.setNumPlayers(round.getNumberOfPlayers() - 1);
			sd.setRound(round);
			sd.show(getSupportFragmentManager(), "Dialog");
		}
		Hole prevHole = round.getHoles()[this.holeIndex];
		for (Shot s : prevHole.getShots()) {
			if (s instanceof ShortGameShot) {
				ShortGameShot sgs = (ShortGameShot) s;
				sgs.setPuttsAfter(prevHole.getPutts());
			}
		}
		firstTime = false;
		this.holeIndex = holeIndex;
		/*
		 * EditText score = (EditText) findViewById(R.id.scoreEditText);
		 * score.setText(hole.getScore(0) + ""); EditText putts = (EditText)
		 * findViewById(R.id.puttsEditText); putts.setText(hole.getPutts() +
		 * "");
		 */
		NumberPicker score = (NumberPicker) findViewById(R.id.scoreNumberPicker);
		score.setValue(hole.getScore(0));
		NumberPicker putts = (NumberPicker) findViewById(R.id.puttsNumberPicker);
		putts.setValue(hole.getPutts());
		CheckBox fairway = (CheckBox) findViewById(R.id.fairwayCheckBox);
		fairway.setChecked(hole.hitFairway());
		CheckBox green = (CheckBox) findViewById(R.id.greenCheckBox);
		green.setChecked(hole.hitGreen());
		TextView holeInfo = (TextView) findViewById(R.id.holeInfoTV);
		holeInfo.setText(hole.toString());

		if (hole.getPar() == 3) {
			fairway.setVisibility(View.GONE);
		} else {
			fairway.setVisibility(View.VISIBLE);
		}
		// Spinner holesSpinner =
		// (Spinner)findViewById(R.id.currentHoleSpinner);
		// holesSpinner.setSelection(holeIndex);

		// INSERT CLUB STUFF HERE

		// INSERT SHOT STUFF HERE
	}
}
