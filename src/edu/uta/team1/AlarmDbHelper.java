package edu.uta.team1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AlarmDbHelper extends SQLiteOpenHelper {
	
	public static final String DB_NAME="alarms.db";
	public static final String TABLE_NAME="alarms";
	public static final String _ID="_ID";
	public static final String NAME_CLM="name";
	public static final String LATT_CLM="lattitude";
	public static final String LONGI_CLM="longitude";
	public static final String DESC_CLM="description";
	public static final String DAY_CLM="day";
	public static final String MONTH_CLM="month";
	public static final String YEAR_CLM="year";
	public static final String HOUR_CLM="hour";
	public static final String MIN_CLM="minute";
	public static final String enabled_CLM="enabled";
	
	private static final String TABLE_QUERY ="create table "+TABLE_NAME +"("+_ID+" INTEGER PRIMARY KEY,"
		+NAME_CLM+" text,"+DESC_CLM+" text,"+ LATT_CLM+" float,"+	LONGI_CLM+" float,"+DAY_CLM+ " integer,"+ 
		MONTH_CLM+" integer,"+ YEAR_CLM+" integer,"+HOUR_CLM+" integer,"+MIN_CLM+" integer,"+enabled_CLM+" integer)";

	
	public AlarmDbHelper(Context context) {
		super(context, DB_NAME, null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(TABLE_QUERY);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
