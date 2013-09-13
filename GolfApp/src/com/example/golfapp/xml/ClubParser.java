package com.example.golfapp.xml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.example.golfapp.model.Club;

public class ClubParser {
	
	File file;
	public ClubParser(File f)
	{
		file = f;
	}
	
	public Club[] getClubs()
	{
		try {
			String str = getStringFromFile(file.getAbsolutePath());
			ArrayList<Club> clubArrayList = new ArrayList<Club>();
			String[] lines = str.split("\n");
			for(String s: lines)
			{
				int stop = s.lastIndexOf("true");
				if(stop < 0)
					stop = s.lastIndexOf("false");
				String name = s.substring(0,stop);
				boolean enabled;
				if(s.lastIndexOf("true")>s.lastIndexOf("false"))
					enabled = true;
				else
					enabled = false;
				clubArrayList.add(new Club(name, enabled));
			}
			Club[] result = new Club[clubArrayList.size()];
			for(int i = 0; i < clubArrayList.size(); i++)
			{
				result[i]=clubArrayList.get(i);
			}
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
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

	public static String getStringFromFile (String filePath) throws Exception {
	    File fl = new File(filePath);
	    FileInputStream fin = new FileInputStream(fl);
	    String ret = convertStreamToString(fin);
	    //Make sure you close all streams.
	    fin.close();        
	    return ret;
	}

}
