package com.example.golfapp;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.golfapp.model.Round;
import com.example.golfapp.model.ShortGameShot;
import com.example.golfapp.xml.CourseParser;

public class MakeRoundActivity extends FragmentActivity
{

	private TextView tvDisplayDate;
	private Button btnChangeDate;
	private File[] courses;

	private int year;
	private int month;
	private int day;
	int holeIndex = 0;
	boolean firstTime = true;

	private Round round = new Round();
	private String[] tees;

	private Context thisContext = this;

	static final int DATE_DIALOG_ID = 999;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_round);
		populateCourses();
		addListenerOnButtons();
		setCurrentDateOnView();
	}
	

	public void populateCourses()
	{
		Spinner courseSpinner = (Spinner) findViewById(R.id.courseSpinner);
		File coursesDir = new File(getFilesDir(), "courses");
		courses = coursesDir.listFiles();
		String[] courseFileNames = new String[courses.length];
		for (int i = 0; i < courses.length; i++)
		{
			courseFileNames[i] = CourseParser.getCourseName(courses[i]);
		}
		populateTees(courses[0]);

		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item, courseFileNames);
		spinnerArrayAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		courseSpinner.setAdapter(spinnerArrayAdapter);
		courseSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3)
			{
				populateTees(courses[arg0.getSelectedItemPosition()]);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0)
			{
				populateTees(null);

			}

		});
	}

	public void populateTees(File f)
	{
		Spinner teeSpinner = (Spinner) findViewById(R.id.teeSpinner);
		if (f == null)
		{
			teeSpinner.setEnabled(false);
		} else
		{
			teeSpinner.setEnabled(true);
			tees = CourseParser.getTees(f);
			ArrayAdapter<String> teeArrayAdapter = new ArrayAdapter<String>(
					this, android.R.layout.simple_spinner_item, tees);
			teeArrayAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			teeSpinner.setAdapter(teeArrayAdapter);

		}
	}

	public void addListenerOnButtons()
	{

		Button startButton = (Button) findViewById(R.id.startRoundButton);

		final Spinner numPlayersText = (Spinner) findViewById(R.id.playerSpinner);

		final NameDialog nd = new NameDialog();
		nd.setParent(this);
		nd.setRound(round);
		startButton.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				int numPlayers = numPlayersText.getSelectedItemPosition();
				round.setNumberOfPlayers(numPlayers);
				Spinner courseSpinner = (Spinner) findViewById(R.id.courseSpinner);
				round.setCourseFile(courses[courseSpinner
						.getSelectedItemPosition()]);
				round.setCourseName(CourseParser.getCourseName(round
						.getCourseFile()));
				round.setDate(new Date(year - 1900, month, day));
				Spinner holeSpinner = (Spinner) findViewById(R.id.holesSpinner);
				if (holeSpinner.getSelectedItemPosition() == 0)
					round.setNumberOfHoles(18);
				else
					round.setNumberOfHoles(9);

				Spinner startSpinner = (Spinner) findViewById(R.id.nineSpinner);
				round.setStartingHole(1 + 9 * startSpinner
						.getSelectedItemPosition());
				Spinner teeSpinner = (Spinner) findViewById(R.id.teeSpinner);
				round.setTee(tees[teeSpinner.getSelectedItemPosition()]);
				round.setUpHoles();
				if (numPlayers == 0)
				{
					// PASS PARAMETERS
					Intent i = new Intent(thisContext, RoundActivity.class);
					i.putExtra("Round", round);
					startActivity(i);
					finish();
					return;
				}
				nd.setNumPlayers(numPlayers);
				nd.setTees(tees);
				FragmentManager dummy = getSupportFragmentManager();
				nd.show(dummy, "Dialog");

			}

		});

		btnChangeDate = (Button) findViewById(R.id.changeDateButton);

		btnChangeDate.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{

				showDialog(DATE_DIALOG_ID);

			}

		});

		Button backButton = (Button) findViewById(R.id.newRoundBackButton);
		backButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{

				finish();

			}

		});

	}

	protected Dialog onCreateDialog(int id)
	{
		switch (id)
		{
		case DATE_DIALOG_ID:
			// set date picker as current date
			return new DatePickerDialog(this, datePickerListener, year, month,
					day);
		}
		return null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_round, menu);
		return true;
	}

	public void setCurrentDateOnView()
	{

		tvDisplayDate = (TextView) findViewById(R.id.dateTextView);

		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);

		// set current date into textview
		tvDisplayDate.setText(new StringBuilder()
				// Month is 0 based, just add 1
				.append(month + 1).append("-").append(day).append("-")
				.append(year).append(" "));

		// set current date into datepicker

	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener()
	{

		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay)
		{
			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;

			// set selected date into textview
			tvDisplayDate.setText(new StringBuilder().append(month + 1)
					.append("-").append(day).append("-").append(year)
					.append(" "));

			// set selected date into datepicker also

		}
	};

}
