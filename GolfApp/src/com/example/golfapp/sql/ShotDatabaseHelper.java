package com.example.golfapp.sql;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.golfapp.model.FullShot;
import com.example.golfapp.model.Hole;
import com.example.golfapp.model.Round;
import com.example.golfapp.model.RoundSQL;
import com.example.golfapp.model.Shot;

public class ShotDatabaseHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "golfAppDatabase";
	private static final String TABLE_SHOTS = "shots";
	private static final String TABLE_ROUNDS="rounds";
	private static final String TABLE_HOLES="holes";
	
	public static final String KEY_ID = "id";
	public static final String KEY_CLUB = "club";
	public static final String KEY_DISTANCE = "distance";
	public static final String KEY_DIRECTION = "direction";
	public static final String KEY_SHAPE = "shape";
	public static final String KEY_TEE = "teeShot";
	public static final String KEY_SURFACE = "surface";
	public static final String KEY_COURSE = "course";
	public static final String KEY_HOLE = "hole";
	
	public static final String KEY_COURSE_NAME="course";
	public static final String KEY_HOLES_PLAYED ="holes";
	public static final String KEY_NINE_PLAYED = "nine";
	public static final String KEY_FRONT_SCORE = "frontScore";
	public static final String KEY_BACK_SCORE = "backScore";
	public static final String KEY_TOTAL_SCORE = "totalScore";
	public static final String KEY_FRONT_PUTTS = "frontPutts";
	public static final String KEY_FRONT_FAIRWAYS = "frontFairways";
	public static final String KEY_FRONT_POSSIBLE_FAIRWAYS="frontPossibleFairways";
	public static final String KEY_FRONT_GREENS = "frontGreens";
	public static final String KEY_BACK_PUTTS = "backPutts";
	public static final String KEY_BACK_FAIRWAYS = "backFairways";
	public static final String KEY_BACK_POSSIBLE_FAIRWAYS="backPossibleFairways";
	public static final String KEY_BACK_GREENS = "backGreens";
	public static final String KEY_TOTAL_PUTTS = "totalPutts";
	public static final String KEY_TOTAL_FAIRWAYS = "totalFairways";
	public static final String KEY_TOTAL_POSSIBLE_FAIRWAYS = "totalPossibleFairways";
	public static final String KEY_TOTAL_GREENS = "totalGreens";
	
	public static final String KEY_HOLE_NUMBER = "hole";
	public static final String KEY_PAR = "par";
	//public static final String KEY_COURSE="course";
	public static final String KEY_DATE="date";
	public static final String KEY_FAIRWAY="fairway";
	public static final String KEY_GREEN="green";
	public static final String KEY_PUTTS="putts";
	public static final String KEY_TEE_CLUB ="teeClub";
	public static final String KEY_TEE_RESULT="teeResult";
	public static final String KEY_SCORE="score";
	
	
	
	
	public ShotDatabaseHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_SHOTS_TABLE = "CREATE TABLE "+TABLE_SHOTS+"("
				+KEY_ID+" INTEGER PRIMARY KEY, "+KEY_CLUB+" TEXT,"
				+KEY_DISTANCE+" INTEGER,"+KEY_DIRECTION+" INTEGER,"
				+KEY_SHAPE+" INTEGER,"+KEY_TEE+" INTEGER,"
				+KEY_SURFACE+" INTEGER,"+KEY_COURSE+" TEXT,"
				+KEY_HOLE+" INTEGER"+")";
		db.execSQL(CREATE_SHOTS_TABLE);
		
		String CREATE_ROUNDS_TABLE = "CREATE TABLE "+TABLE_ROUNDS+"("
				+KEY_ID+" INTEGER PRIMARY KEY, "+KEY_COURSE_NAME+" TEXT,"
				+KEY_HOLES_PLAYED+" INTEGER,"+KEY_NINE_PLAYED+" INTEGER,"
				+KEY_FRONT_SCORE+" INTEGER,"+KEY_BACK_SCORE+" INTEGER,"
				+KEY_TOTAL_SCORE+" INTEGER,"+KEY_FRONT_PUTTS+" INTEGER,"
				+KEY_FRONT_FAIRWAYS+" INTEGER,"+KEY_FRONT_POSSIBLE_FAIRWAYS+" INTEGER,"
				+KEY_FRONT_GREENS+" INTEGER,"+KEY_BACK_PUTTS+" INTEGER,"
				+KEY_BACK_FAIRWAYS+" INTEGER,"+KEY_BACK_POSSIBLE_FAIRWAYS+" INTEGER,"
				+KEY_BACK_GREENS+" INTEGER,"+KEY_TOTAL_PUTTS+" INTEGER,"
				+KEY_TOTAL_FAIRWAYS+" INTEGER,"+KEY_TOTAL_POSSIBLE_FAIRWAYS+" INTEGER,"
				+KEY_TOTAL_GREENS+" INTEGER"+")";
		db.execSQL(CREATE_ROUNDS_TABLE);
		String CREATE_HOLES_TABLE="CREATE TABLE "+TABLE_HOLES+"("
				+KEY_ID+" INTEGER PRIMARY KEY, "+KEY_HOLE_NUMBER+" INTEGER,"
				+KEY_PAR+" INTEGER,"+KEY_COURSE+" TEXT,"
				+KEY_DATE+" TEXT,"
				+KEY_FAIRWAY+" INTEGER,"+KEY_GREEN+" INTEGER,"
				+KEY_PUTTS+" INTEGER,"+KEY_TEE_CLUB+" TEXT,"
				+KEY_TEE_RESULT+" INTEGER,"+KEY_SCORE+" INTEGER"+")";
		db.execSQL(CREATE_HOLES_TABLE);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVerson, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_SHOTS);
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_ROUNDS);
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_HOLES);
		onCreate(db);

	}
	
	public void addFullShot(FullShot s)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_CLUB, s.getClub());
		values.put(KEY_DISTANCE, s.getDistanceInMeters());
		values.put(KEY_DIRECTION, s.getDirection());
		values.put(KEY_SHAPE, s.getShape());
		values.put(KEY_TEE, (s.isTeeShot()==true)?1:0);
		values.put(KEY_SURFACE, s.getSurface());
		values.put(KEY_COURSE, s.getCourse());
		values.put(KEY_HOLE, s.getHole());
		
		db.insert(TABLE_SHOTS,null, values);
		db.close();
	}
	
	public void addHole(Hole h, Round r)
	{
		String teeClub="";
		int teeResult=0;
		for(Shot s:h.getShots())
		{
			FullShot fs = (FullShot)s;
			if(fs.isTeeShot())
			{
				teeClub = fs.getClub();
				teeResult=fs.getDirection();
			}
		}
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_HOLE_NUMBER, h.getNumber());
		values.put(KEY_PAR, h.getPar());
		values.put(KEY_COURSE, r.getCourseName());
		values.put(KEY_DATE, r.getDate().toString());
		values.put(KEY_FAIRWAY, h.hitFairway()?1:0);
		values.put(KEY_GREEN, h.hitGreen()?1:0);
		values.put(KEY_PUTTS, h.getPutts());
		values.put(KEY_TEE_CLUB,teeClub);
		values.put(KEY_TEE_RESULT, teeResult);
		values.put(KEY_SCORE, h.getScore(0));
		
		db.insert(TABLE_HOLES, null, values);
		db.close();
	}
	
	public void addRound(Round r)
	{
		Hole[] holes = r.getHoles();
		int frontScore = 0;
		int backScore = 0;
		int totalScore = 0;
		
		int frontPutts = 0, frontFairways = 0, frontPossibleFairways=0, frontGreens = 0;
		int backPutts = 0, backFairways = 0, backPossibleFairways=0, backGreens = 0;
		int totalPutts = 0, totalFairways = 0, totalPossibleFairways=0, totalGreens = 0;
		Hole currentHole;
		for(int i = 0; i < r.getNumberOfHoles(); i++)
		{
			currentHole = holes[i];
			if(currentHole.getNumber()< 10)
			{
				frontScore+=currentHole.getScore(0);
				frontPutts+=currentHole.getPutts();
				frontFairways+=currentHole.hitFairway()?1:0;
				frontPossibleFairways+=currentHole.getPar()>3?1:0;
				frontGreens += currentHole.hitGreen()?1:0;
			}
			else
			{
				backScore+=currentHole.getScore(0);
				backPutts+=currentHole.getPutts();
				backFairways+=currentHole.hitFairway()?1:0;
				backPossibleFairways+=currentHole.getPar()>3?1:0;
				backGreens += currentHole.hitGreen()?1:0;
			}
			totalScore+=currentHole.getScore(0);
			totalPutts+=currentHole.getPutts();
			totalFairways+=currentHole.hitFairway()?1:0;
			totalPossibleFairways+=currentHole.getPar()>3?1:0;
			totalGreens += currentHole.hitGreen()?1:0;
		}
		
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_COURSE_NAME, r.getCourseName());
		values.put(KEY_HOLES_PLAYED, r.getNumberOfHoles());
		values.put(KEY_NINE_PLAYED, r.getStartingHole());
		values.put(KEY_FRONT_SCORE, frontScore);
		values.put(KEY_BACK_SCORE, backScore);
		values.put(KEY_TOTAL_SCORE, totalScore);
		values.put(KEY_FRONT_PUTTS, frontPutts);
		values.put(KEY_FRONT_FAIRWAYS, frontFairways);
		values.put(KEY_FRONT_POSSIBLE_FAIRWAYS, frontPossibleFairways);
		values.put(KEY_FRONT_GREENS, frontGreens);
		
		values.put(KEY_BACK_PUTTS, backPutts);
		values.put(KEY_BACK_FAIRWAYS, backFairways);
		values.put(KEY_BACK_POSSIBLE_FAIRWAYS, backPossibleFairways);
		values.put(KEY_BACK_GREENS, backGreens);
		
		values.put(KEY_TOTAL_PUTTS, totalPutts);
		values.put(KEY_TOTAL_FAIRWAYS, totalFairways);
		values.put(KEY_TOTAL_POSSIBLE_FAIRWAYS, totalPossibleFairways);
		values.put(KEY_TOTAL_GREENS, totalGreens);
		
		db.insert(TABLE_ROUNDS,null, values);
		db.close();
	}
	
	public ArrayList<FullShot> getAllFullShots()
	{
		String selectQuery = "SELECT * FROM "+TABLE_SHOTS;
		return getAllShotsByQuery(selectQuery);
	}
	
	public ArrayList<FullShot> getTeeShotsOnHole(String course, int hole)
	{
		String selectQuery = "SELECT * FROM "+TABLE_SHOTS+" WHERE "+KEY_TEE+ "=1 AND "+KEY_COURSE+" ='"+course+"' AND "+KEY_HOLE_NUMBER+"="+hole;
		return getAllShotsByQuery(selectQuery);
	}
	
	public ArrayList<FullShot> getAllShotsWithClub(String club)
	{
		String selectQuery = "SELECT * FROM "+TABLE_SHOTS+" WHERE "+KEY_CLUB+"='"+club+"'";
		return getAllShotsByQuery(selectQuery);
	}
	
	private ArrayList<FullShot>  getAllShotsByQuery(String selectQuery)
	{
		ArrayList<FullShot> shotList = new ArrayList<FullShot>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst())
		{
			do{
				String club =cursor.getString(1);
				int distance = Integer.parseInt(cursor.getString(2));
				int direction = Integer.parseInt(cursor.getString(3));
				int shape = Integer.parseInt(cursor.getString(4));
				boolean tee = Integer.parseInt(cursor.getString(5)) == 1;
				int surface = Integer.parseInt(cursor.getString(6));
				String course = cursor.getString(7);
				int hole = Integer.parseInt(cursor.getString(8));
				FullShot s = new FullShot(club, course, hole, tee, direction, shape, distance, surface);
				shotList.add(s);
			}while(cursor.moveToNext());
		}	
		db.close();
		return shotList;
	}
	
	public ArrayList<Hole> getAllHolesByCourse(String course)
	{
		String query = "SELECT * FROM "+TABLE_HOLES+" WHERE "+KEY_COURSE+"='"+course+"'";
		return getAllHolesByQuery(query);
	}
	public ArrayList<Hole> getAllHolesByHole(String course, int num)
	{
		String query = "SELECT * FROM "+TABLE_HOLES+" WHERE "+KEY_COURSE+"='"+course+"' AND "+KEY_HOLE_NUMBER+"="+num;
		return getAllHolesByQuery(query);
	}
	
	private ArrayList<Hole> getAllHolesByQuery(String query)
	{
		ArrayList<Hole> holeList = new ArrayList<Hole>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		
		if(cursor.moveToFirst())
		{
			do{
				Hole h = new Hole();
				h.setNumber(Integer.parseInt(cursor.getString(1)));
				h.setPar(Integer.parseInt(cursor.getString(2)));
				h.setFairway(Integer.parseInt(cursor.getString(5))==1);
				h.setGreen(Integer.parseInt(cursor.getString(6))==1);
				h.setPutts(Integer.parseInt(cursor.getString(7)));
				h.setScore(0, Integer.parseInt(cursor.getString(10)));
				holeList.add(h);
				
			}while(cursor.moveToNext());
		}
		db.close();
		return holeList;
	}
	
	public ArrayList<RoundSQL> getAllRounds()
	{
		String selectQuery = "SELECT * FROM "+TABLE_ROUNDS;
		return getAllRoundsByQuery(selectQuery);
	}
	
	public ArrayList<RoundSQL> getAllRoundsOnCourse(String course)
	{
		String selectQuery = "SELECT * FROM "+TABLE_ROUNDS+" WHERE "+KEY_COURSE_NAME+"="+course;
		return getAllRoundsByQuery(selectQuery);
	}
	
	private ArrayList<RoundSQL> getAllRoundsByQuery(String query)
	{ 
		ArrayList<RoundSQL> roundList = new ArrayList<RoundSQL>();
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		
		if(cursor.moveToFirst())
		{
			do{
				RoundSQL r = new RoundSQL();
				r.course=cursor.getString(1);
				r.holesPlayed=Integer.parseInt(cursor.getString(2));
				r.startingHole=Integer.parseInt(cursor.getString(3));
				r.frontScore=Integer.parseInt(cursor.getString(4));
				r.backScore=Integer.parseInt(cursor.getString(5));
				r.totalScore=Integer.parseInt(cursor.getString(6));
				r.frontPutts=Integer.parseInt(cursor.getString(7));
				r.frontFairways=Integer.parseInt(cursor.getString(8));
				r.frontPossibleFairways=Integer.parseInt(cursor.getString(9));
				r.frontGreens = Integer.parseInt(cursor.getString(10));
				
				r.backPutts=Integer.parseInt(cursor.getString(11));
				r.backFairways=Integer.parseInt(cursor.getString(12));
				r.backPossibleFairways=Integer.parseInt(cursor.getString(13));
				r.backGreens = Integer.parseInt(cursor.getString(14));
				
				r.totalPutts=Integer.parseInt(cursor.getString(15));
				r.totalFairways=Integer.parseInt(cursor.getString(16));
				r.totalPossibleFairways=Integer.parseInt(cursor.getString(17));
				r.totalGreens = Integer.parseInt(cursor.getString(18));
				roundList.add(r);
			}while(cursor.moveToNext());
		}	
		db.close();
		return roundList;
	}
	

}
