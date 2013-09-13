package com.example.golfapp.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.StringWriter;

import org.xmlpull.v1.XmlSerializer;

import android.content.Context;
import android.util.Xml;

import com.example.golfapp.model.Hole;
import com.example.golfapp.model.Round;

public class RoundWriter {
	
	private Round round;
	private Context context;
	public RoundWriter(Round r, Context con)
	{
		round = r;
		context = con;
	}
	
	public void write(String dest)
	{
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try{
			//serializer.setOutput(writer);
			//serializer.startDocument("UTF-8", true);
			FileOutputStream fileos = new FileOutputStream(dest);
			serializer.setOutput(fileos, "UTF-8");
			
			serializer.startTag("", "Round");
			serializer.attribute("","holes", round.getNumberOfHoles()+"");
			serializer.attribute("","players",round.getNumberOfPlayers()+"");
			serializer.attribute("", "date", round.getDate().toString());
			serializer.attribute("", "course",round.getCourseName());
			serializer.attribute("", "tee", round.getTee());
			serializer.attribute("", "startOn", round.getStartingHole()+"");
			
			Hole[] holes = round.getHoles();
			for(Hole h: holes)
			{
				serializer.startTag("", "Hole");
				serializer.attribute("", "number", h.getNumber()+"");
				serializer.attribute("", "par", h.getPar()+"");
				serializer.attribute("", "hdcp",h.getHandicap()+"");
				serializer.attribute("","yardage",h.getYardage()+"");
				int[] scores = h.getScores();
				
				serializer.startTag("", "Player");
				serializer.attribute("", "name", "Me");
				serializer.startTag("","Score");
				serializer.text(scores[0]+"");
				serializer.endTag("", "Score");
				serializer.startTag("","Putts");
				serializer.text(h.getPutts()+"");
				serializer.endTag("", "Putts");
				serializer.startTag("","Fairway");
				serializer.text(h.hitFairway()+"");
				serializer.endTag("", "Fairway");
				serializer.startTag("","Green");
				serializer.text(h.hitGreen()+"");
				serializer.endTag("", "Green");
				serializer.endTag("", "Player");
				for(int i = 1; i < scores.length; i++)
				{
					serializer.startTag("","Player");
					serializer.attribute("","name",round.getPlayers()[i-1]);
					serializer.startTag("","Score");
					serializer.text(scores[i]+"");
					serializer.endTag("", "Score");
					serializer.endTag("","Player");
				}
				serializer.endTag("","Hole");
			}
			serializer.endTag("","Round");
			serializer.flush();
			fileos.close();

			/*OutputStream out = new FileOutputStream(new File(dest));
			out.write(writer.toString().getBytes());
			out.close();*/
			
		}catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}

}
