package com.example.golfapp.view;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

public class ShotDistributionView extends View{

	public ShotDistributionView(Context context, String[][] vals) {
		super(context);
		this.setBackground(getResources().getDrawable(com.example.golfapp.R.drawable.target));
		int width = this.getWidth()/3;
		int height = this.getHeight()/3;
		for(int r = 0; r < vals.length; r++){
			for(int c = 0; c < vals[0].length; c++){
				TextView tv = new TextView(context);
				tv.setText(vals[r][c]);
			}
		}
	}

}
