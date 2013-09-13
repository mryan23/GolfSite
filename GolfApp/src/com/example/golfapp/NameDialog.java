package com.example.golfapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.EditText;

import com.example.golfapp.model.Round;

public class NameDialog extends DialogFragment {

	int numPlayers;
	Round round;
	Activity parent;
	Dialog dialog;
	String[] tees = new String[0];
	public void setNumPlayers(int num) {
		numPlayers = num;
	}

	public void setTees(String[] ts) {
		tees = ts;
	}

	public void setRound(Round r) {
		round = r;
	}

	public void setParent(Activity par) {
		parent = par;
	}

	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();

		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		switch (numPlayers) {
		case 1:
			builder.setView(inflater.inflate(R.layout.add_name_one, null));
			break;
		case 2:
			builder.setView(inflater.inflate(R.layout.add_name_two, null));
			break;
		case 3:
			builder.setView(inflater.inflate(R.layout.add_name_three, null));
			break;

		}
		// Add action buttons
		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dia, int id) {
						String[] names = new String[numPlayers];
						EditText player1 = (EditText) dialog
								.findViewById(R.id.player1EditText);
						names[0] = (player1.getText().toString());
						if (numPlayers > 1) {
							player1 = (EditText) dialog
									.findViewById(R.id.player2EditText);
							names[1] = (player1.getText().toString());
						}
						if (numPlayers > 2) {
							player1 = (EditText) dialog
									.findViewById(R.id.player3EditText);
							names[2] = (player1.getText().toString());
						}
						round.setPlayers(names);
						// PASS PARAMETERS
						Intent i = new Intent(parent, RoundActivity.class);
						i.putExtra("Round",round);
						startActivity(i);
						parent.finish();

					}
				}).setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// Do something
					}
				});
		dialog = builder.create();

		return dialog;
	}
}