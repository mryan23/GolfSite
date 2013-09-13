package com.example.golfapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.golfapp.model.Hole;
import com.example.golfapp.model.Round;

public class ScoreDialog extends DialogFragment {
	
	int numPlayers;
	Round round;
	Dialog dialog;
	int holeIndex;

	public void setNumPlayers(int num)
	{
		numPlayers = num;
	}
	public void setRound( Round r)
	{
		round = r;
	}
	public void setHoleIndex(int hi)
	{
		holeIndex = hi;
	}

    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view=null;
        switch(numPlayers)
        {
        case 1:
        	view =(inflater.inflate(R.layout.set_score_one, null));
        	break;
        case 2:
        	view =(inflater.inflate(R.layout.set_score_two, null));
        	break;
        case 3:
        	view = (inflater.inflate(R.layout.set_score_three, null));
        	break;
        
        }
        // Add action buttons
               builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dia, int id) {
                	   int[] scores = new int[numPlayers];
                	   EditText player1 = (EditText)dialog.findViewById(R.id.player1ScoreEditText);
                	   scores[0]=Integer.parseInt(player1.getText().toString());
                	   if(numPlayers > 1)
                	   {
                		   player1 = (EditText)dialog.findViewById(R.id.player2ScoreEditText);
                		   scores[1]=Integer.parseInt(player1.getText().toString());
                	   }
                	   if(numPlayers > 2)
                	   {
                		   player1 = (EditText)dialog.findViewById(R.id.player3ScoreEditText);
                		   scores[2]=Integer.parseInt(player1.getText().toString());
                	   }
                	   //round.setPlayers(names);
                	   //parent.goToFirstHoleScreen();
                	   Hole hole = round.getHoles()[holeIndex];
                	   for(int i = 0; i < numPlayers; i++)
                	   {
                		   hole.setScore(i+1, scores[i]);
                	   }
                	   
                   }
               })
               /*.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       //Do something
                   }
               })*/; 
               
        
		EditText player1 = (EditText) view
				.findViewById(R.id.player1ScoreEditText);
		player1.setText(round.getHoles()[holeIndex].getScore(1)+"");
		Button plus = (Button) view.findViewById(R.id.player1ScorePlus);
		plus.setOnClickListener(new PlusListener(player1));
		Button minus = (Button) view.findViewById(R.id.player1ScoreMinus);
		minus.setOnClickListener(new MinusListener(player1));
		TextView playerName = (TextView) view.findViewById(R.id.player1Name);
		playerName.setText(round.getPlayers()[0]);
		if (numPlayers > 1) {
			player1 = (EditText) view.findViewById(R.id.player2ScoreEditText);
			player1.setText(round.getHoles()[holeIndex].getScore(2)+"");
			plus = (Button) view.findViewById(R.id.player2ScorePlus);
			plus.setOnClickListener(new PlusListener(player1));
			minus = (Button) view.findViewById(R.id.player2ScoreMinus);
			minus.setOnClickListener(new MinusListener(player1));
			playerName = (TextView) view.findViewById(R.id.player2Name);
			playerName.setText(round.getPlayers()[1]);
		}
		if (numPlayers > 2) {
			player1 = (EditText) view.findViewById(R.id.player3ScoreEditText);
			player1.setText(round.getHoles()[holeIndex].getScore(3)+"");
			plus = (Button) view.findViewById(R.id.player3ScorePlus);
			plus.setOnClickListener(new PlusListener(player1));
			minus = (Button) view.findViewById(R.id.player3ScoreMinus);
			minus.setOnClickListener(new MinusListener(player1));
			playerName = (TextView) view.findViewById(R.id.player3Name);
			playerName.setText(round.getPlayers()[2]);
		}
	   builder.setView(view);
	   dialog = builder.create(); 
        return dialog;
    }

    private class MinusListener implements OnClickListener
    {
    	EditText editText;
    	public MinusListener(EditText et)
    	{
    		editText = et;
    	}
		@Override
		public void onClick(View arg0) {
			int val = Integer.parseInt(editText.getText().toString());
			if( val > 0)
				editText.setText((val-1)+"");
			
		}
    	
    }
    private class PlusListener implements OnClickListener
    {
    	EditText editText;
    	public PlusListener(EditText et)
    	{
    		editText = et;
    	}
		@Override
		public void onClick(View arg0) {
			int val = Integer.parseInt(editText.getText().toString());
			editText.setText((val+1)+"");
			
		}
    	
    }
}