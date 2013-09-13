package com.example.golfapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.golfapp.model.Club;
import com.example.golfapp.xml.ClubParser;

public class ClubsActivity extends Activity {

	ArrayList<CheckBox> checkBoxes;
	LinearLayout layout;
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clubs);

		TextView debugTextView = (TextView) findViewById(R.id.debugClubTextView);
		String result = "";
		ClubParser parser = new ClubParser(new File(getFilesDir(), "clubs.xml"));
		Club[] clubs = parser.getClubs();
		context = this;
		// for(Club c: clubs)
		// {
		// result+=c.getName()+"          "+c.isEnabled()+"\n";
		// }
		// debugTextView.setText(result);

		layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		ScrollView scroll = (ScrollView) findViewById(R.id.clubsScrollView);
		checkBoxes = new ArrayList<CheckBox>();
		String[] clubNames = new String[clubs.length];
		for (int i = 0; i < clubs.length; i++)
			clubNames[i] = clubs[i].getName();
		for (Club c : clubs) {

			CheckBox checkBox = new CheckBox(this);
			checkBox.setText(c.getName());
			checkBox.setChecked(c.isEnabled());
			layout.addView(checkBox);
			checkBoxes.add(checkBox);
		}
		// ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		// android.R.layout.simple_list_item_multiple_choice,clubNames);
		// layout.setAdapter(adapter);
		// layout.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		scroll.addView(layout);

		setButtonListeners();
	}

	private void setButtonListeners() {
		Button backButton = (Button) findViewById(R.id.clubsBackButton);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				saveClubs();
				finish();

			}

		});

		Button addClub = (Button) findViewById(R.id.addClubButton);
		final EditText input = new EditText(this);
		final AlertDialog.Builder alert = new AlertDialog.Builder(this);
		addClub.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				alert.setTitle("Add Club");
				alert.setMessage("Add a club to your bag");

				// Set an EditText view to get user input

				alert.setView(input);

				alert.setPositiveButton("Add",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								String value = input.getText().toString();
								CheckBox checkBox = new CheckBox(context);
								Club c = new Club(value, true);
								checkBox.setText(c.getName());
								checkBox.setChecked(c.isEnabled());
								layout.addView(checkBox);
								checkBoxes.add(checkBox);
								saveClubs();
								// Do something with value!
							}
						});

				alert.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// Canceled.
							}
						});

				alert.show();
				// see
				// http://androidsnippets.com/prompt-user-input-with-an-alertdialog

			}

		});
	}

	private void saveClubs() {
		String fileName = "clubs.xml";
		String result = "";
		try {
			for (CheckBox cb : checkBoxes) {
				result += cb.getText() + " " + cb.isChecked() + "\n";
			}
			FileOutputStream output = openFileOutput(fileName,
					Context.MODE_PRIVATE);
			output.write(result.getBytes());
			output.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			saveClubs();
			finish();
			return true;
		}
		return super.onKeyUp(keyCode, event);
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

	public static String getStringFromFile(String filePath) throws Exception {
		File fl = new File(filePath);
		FileInputStream fin = new FileInputStream(fl);
		String ret = convertStreamToString(fin);
		// Make sure you close all streams.
		fin.close();
		return ret;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.clubs, menu);
		return true;
	}

}
