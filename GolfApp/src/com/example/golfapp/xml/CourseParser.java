package com.example.golfapp.xml;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.example.golfapp.model.GPSPoints;
import com.example.golfapp.model.Obstacles;
import com.google.android.gms.maps.model.LatLng;

public class CourseParser {
	
	File file;
	Document doc;
	public CourseParser(File f)
	{
		file = f;
		try {
			 
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(f);
		    } catch (Exception e) {
		    }
	}
	
	public String getCourseName()
	{
		try {
		 
			//optional, but recommended
			//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();
			
			NodeList list = doc.getElementsByTagName("Course");
			Node course = list.item(0);
			Element courseElement = (Element)course;
			return courseElement.getAttribute("name");
		    } catch (Exception e) {
		    	return "No Name";
		    }
	}
	
	public static int getNumberOfHoles(File f)
	{
		try {
			 
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(f);
		 
			//optional, but recommended
			//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();
			
			NodeList list = doc.getElementsByTagName("Course");
			Node course = list.item(0);
			Element courseElement = (Element)course;
			return Integer.parseInt(courseElement.getAttribute("holes"));
		    } catch (Exception e) {
		    	return 0;
		    }
	}
	
	public String[] getTees()
	{
		try {
			 
			doc.getDocumentElement().normalize();
			
			NodeList list = doc.getElementsByTagName("Course");
			Node course = list.item(0);
			Element courseElement = (Element)course;
			
			NodeList holes = courseElement.getElementsByTagName("Hole");
			Node hole = holes.item(0);
			Element hole1 = (Element)hole;
			
			NodeList teeList = hole1.getElementsByTagName("Tee");
			String[] result = new String[teeList.getLength()];
			for(int i = 0; i < teeList.getLength(); i++)
			{
				Node tee = teeList.item(i);
				Element teeElement = (Element)tee;
				result[i]=teeElement.getAttribute("name");
			}
			return result;
		    } catch (Exception e) {
		    	String [] result = new String[1];
		    	result[0]="No Tees";
		    	return result;
		    }
		
	}
	
	/**
	 * 
	 * each row represents a hole
	 * column 1 is par
	 * column 2 is handicap
	 * column 3 is yardage
	 * 
	 * @param tee The tee being played
	 * @return Hole data see format
	 */
	public int[][] getHoleData(String tee)
	{
		
		NodeList list = doc.getElementsByTagName("Course");
		Node course = list.item(0);
		Element courseElement = (Element)course;
		int numHoles = Integer.parseInt(courseElement.getAttribute("holes"));
		int[][] result = new int [numHoles][3];
		
		String gender = getGenderFromTee(tee);
		String handicapAttr;
		String parAttr;
		if(gender == "men")
		{
			handicapAttr="menHandicap";
			parAttr = "menPar";
		}
		else
		{
			handicapAttr="womenHandicap";
			parAttr="womenPar";
		}
		
		
		NodeList holes = courseElement.getElementsByTagName("Hole");
		for(int i = 0; i < holes.getLength(); i++)
		{
			Node hole = holes.item(i);
			Element holeElement = (Element)hole;
			result[i][0]=Integer.parseInt(holeElement.getAttribute(parAttr));
			result[i][1]=Integer.parseInt(holeElement.getAttribute(handicapAttr));
			
			NodeList teeList = holeElement.getElementsByTagName("Tee");
			for(int j = 0; j < teeList.getLength(); j++)
			{
				Node t = teeList.item(j);
				Element teeElement = (Element)t;
				if(teeElement.getAttribute("name").equals(tee))
				{
					result[i][2]=Integer.parseInt(teeElement.getTextContent());
				}
			}
			
		}
		
		return result;
	}
	
	public String getGenderFromTee(String tee)
	{
		NodeList list = doc.getElementsByTagName("Course");
		Node course = list.item(0);
		Element courseElement = (Element)course;
		NodeList holes = courseElement.getElementsByTagName("Hole");
		Node hole = holes.item(0);
		Element hole1 = (Element)hole;
		
		NodeList teeList = hole1.getElementsByTagName("Tee");
		String[] result = new String[teeList.getLength()];
		for(int i = 0; i < teeList.getLength(); i++)
		{
			Node t = teeList.item(i);
			Element teeElement = (Element)t;
			if(teeElement.getAttribute("name").equals(tee))
			{
				return teeElement.getAttribute("gender");
			}
		}
		return "men";
	}
	
	public static String getCourseName(File f)
	{
		try {
			 
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(f);
		 
			//optional, but recommended
			//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();
			
			NodeList list = doc.getElementsByTagName("Course");
			Node course = list.item(0);
			Element courseElement = (Element)course;
			return courseElement.getAttribute("name");
		    } catch (Exception e) {
		    	return "No Name";
		    }
	}
	
	public GPSPoints[] getGPSPoints()
	{
		NodeList list = doc.getElementsByTagName("Course");
		Node course = list.item(0);
		Element courseElement = (Element)course;
		int numHoles = Integer.parseInt(courseElement.getAttribute("holes"));
		GPSPoints[] result = new GPSPoints[numHoles];
		
		
		
		
		NodeList holes = courseElement.getElementsByTagName("Hole");
		for(int i = 0; i < holes.getLength(); i++)
		{
			result[i]=new GPSPoints();
			Node hole = holes.item(i);
			Element holeElement = (Element)hole;
			NodeList GPS = holeElement.getElementsByTagName("GPS");
			Element gpsElement = (Element)GPS.item(0);
			
			
			NodeList teeBox = gpsElement.getElementsByTagName("TeeBox");
			Element teeElement = (Element)teeBox.item(0);
			result[i].teeBox = new LatLng(Double.parseDouble(teeElement.getAttribute("lat")),
											Double.parseDouble(teeElement.getAttribute("lon")));
			
			NodeList greenList = gpsElement.getElementsByTagName("Green");
			Element greenElement = (Element)greenList.item(0);
			Element front = (Element)greenElement.getElementsByTagName("front").item(0);
			result[i].frontGreen = new LatLng(Double.parseDouble(front.getAttribute("lat")),
					Double.parseDouble(front.getAttribute("lon")));
			Element center = (Element)greenElement.getElementsByTagName("center").item(0);
			result[i].centerGreen = new LatLng(Double.parseDouble(center.getAttribute("lat")),
					Double.parseDouble(center.getAttribute("lon")));
			Element back = (Element)greenElement.getElementsByTagName("back").item(0);
			result[i].backGreen = new LatLng(Double.parseDouble(back.getAttribute("lat")),
					Double.parseDouble(back.getAttribute("lon")));
			
			NodeList waterList = gpsElement.getElementsByTagName("Water");
			result[i].waterHazards = new Obstacles[waterList.getLength()];
			for(int j = 0; j < waterList.getLength(); j++)
			{
				Element water = (Element)waterList.item(j);
				Element start = (Element)water.getElementsByTagName("start").item(0);
				Element end = (Element) water.getElementsByTagName("end").item(0);
				String name = "Water";
				String val =water.getAttribute("name");
				if(val != null && !val.equals(""))
					name = val;
				result[i].waterHazards[j]=new Obstacles(new LatLng(Double.parseDouble(start.getAttribute("lat")),
						Double.parseDouble(start.getAttribute("lon"))),
						new LatLng(Double.parseDouble(end.getAttribute("lat")),
								Double.parseDouble(end.getAttribute("lon"))),
								name
						);
			}
			
			
			
			NodeList bunkerList = gpsElement.getElementsByTagName("Bunker");
			result[i].bunkers = new Obstacles[bunkerList.getLength()];
			for(int j = 0; j < bunkerList.getLength(); j++)
			{
				Element bunker = (Element)bunkerList.item(j);
				Element start = (Element)bunker.getElementsByTagName("start").item(0);
				Element end = (Element) bunker.getElementsByTagName("end").item(0);
				String name = "Bunker";
				String val =bunker.getAttribute("name");
				if(val != null && !val.equals(""))
					name = val;
				result[i].bunkers[j]=new Obstacles(new LatLng(Double.parseDouble(start.getAttribute("lat")),
						Double.parseDouble(start.getAttribute("lon"))),
						new LatLng(Double.parseDouble(end.getAttribute("lat")),
								Double.parseDouble(end.getAttribute("lon"))),
								name
						);
			}
			
			NodeList treeList = gpsElement.getElementsByTagName("Tree");
			result[i].trees = new Obstacles[treeList.getLength()];
			for(int j = 0; j < treeList.getLength(); j++)
			{
				Element tree = (Element)treeList.item(j);
				String name = "Tree";
				String val =tree.getAttribute("name");
				if(val != null && !val.equals(""))
					name = val;
				result[i].trees[j]=new Obstacles(new LatLng(Double.parseDouble(tree.getAttribute("lat")),
						Double.parseDouble(tree.getAttribute("lon"))),
						null,name);
			}
			
			NodeList hazardList = gpsElement.getElementsByTagName("Hazard");
			result[i].hazards = new Obstacles[hazardList.getLength()];
			for(int j = 0; j < hazardList.getLength(); j++)
			{
				Element hazard = (Element)hazardList.item(j);
				String name= "Hazard";
				String val = hazard.getAttribute("name");
				Element start = (Element)hazard.getElementsByTagName("start").item(0);
				Element end = (Element) hazard.getElementsByTagName("end").item(0);
				if(val != null && !val.equals(""))
					name=val;
				if(end != null)
				{
				result[i].hazards[j]=new Obstacles(new LatLng(Double.parseDouble(start.getAttribute("lat")),
						Double.parseDouble(start.getAttribute("lon"))),
						new LatLng(Double.parseDouble(end.getAttribute("lat")),
								Double.parseDouble(end.getAttribute("lon"))),
								name
						);
				}
				else
				{
					result[i].hazards[j]=new Obstacles(new LatLng(Double.parseDouble(start.getAttribute("lat")),
							Double.parseDouble(start.getAttribute("lon"))),
							null,name);
				}
			}
			
			
			NodeList eofList = gpsElement.getElementsByTagName("EOF");
			result[i].eofs = new Obstacles[eofList.getLength()];
			for(int j = 0; j < eofList.getLength(); j++)
			{
				Element eof = (Element)eofList.item(j);
				String name = "End of Fairway";
				String val = eof.getAttribute("name");
				if(val != null && !val.equals(""))
					name=val;
				result[i].eofs[j]=new Obstacles(new LatLng(Double.parseDouble(eof.getAttribute("lat")),
						Double.parseDouble(eof.getAttribute("lon"))),
						null,name);
			}
			
			NodeList otherList = gpsElement.getElementsByTagName("Other");
			result[i].others = new Obstacles[otherList.getLength()];
			for(int j = 0; j < otherList.getLength(); j++)
			{
				Element other = (Element)otherList.item(j);
				String name = "Obstacle";
				String val = other.getAttribute("name");
				if(val != null && !val.equals(""))
					name = val;
				
				String readLat = other.getAttribute("lat");
				if(readLat != null && !readLat.equals(""))
				{
					result[i].others[j]= new Obstacles(new LatLng(Double.parseDouble(readLat),
							Double.parseDouble(other.getAttribute("lon"))), null, name);
				}
				else
				{
					Element start = (Element)other.getElementsByTagName("start").item(0);
					Element end = (Element) other.getElementsByTagName("end").item(0);
					result[i].others[j]=new Obstacles(new LatLng(Double.parseDouble(start.getAttribute("lat")),
							Double.parseDouble(start.getAttribute("lon"))),
							new LatLng(Double.parseDouble(end.getAttribute("lat")),
									Double.parseDouble(end.getAttribute("lon"))),
									name
							);
				}
			}
			
		}
		
		
		
		return result;
	}
	
	public static String[] getTees(File f)
	{
		try {
			 
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(f);
			doc.getDocumentElement().normalize();
			
			NodeList list = doc.getElementsByTagName("Course");
			Node course = list.item(0);
			Element courseElement = (Element)course;
			
			NodeList holes = courseElement.getElementsByTagName("Hole");
			Node hole = holes.item(0);
			Element hole1 = (Element)hole;
			
			NodeList teeList = hole1.getElementsByTagName("Tee");
			String[] result = new String[teeList.getLength()];
			for(int i = 0; i < teeList.getLength(); i++)
			{
				Node tee = teeList.item(i);
				Element teeElement = (Element)tee;
				result[i]=teeElement.getAttribute("name");
			}
			return result;
		    } catch (Exception e) {
		    	String [] result = new String[1];
		    	result[0]="No Tees";
		    	return result;
		    }
		
	}

}
