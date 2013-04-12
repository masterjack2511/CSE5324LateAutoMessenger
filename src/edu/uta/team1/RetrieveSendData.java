package edu.uta.team1;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class RetrieveSendData{
	
	
	
	AlarmDbHelper alarmDbHelper;
	SQLiteDatabase database;
	
	
	private static String[] clms={
		AlarmDbHelper._ID,
		AlarmDbHelper.NAME_CLM,
		AlarmDbHelper.DESC_CLM,
		AlarmDbHelper.LATT_CLM,
		AlarmDbHelper.LONGI_CLM,
		AlarmDbHelper.DAY_CLM,
		AlarmDbHelper.MONTH_CLM,
		AlarmDbHelper.YEAR_CLM,
		AlarmDbHelper.HOUR_CLM,
		AlarmDbHelper.MIN_CLM,
		AlarmDbHelper.CON_CLM,
		AlarmDbHelper.enabled_CLM
		
	};
	
	public String message;
	public String [] contactsList;
	public double longtidue;
	public double latitude;

	public RetrieveSendData(Context context) 
	{
		alarmDbHelper = new AlarmDbHelper(context);
		
	}
	
	public void open()
	{
		Log.d("RETRIEVE_DATA", "Database opened");
		database = alarmDbHelper.getReadableDatabase();
	}

	public void close()
	{
		Log.d("RETRIEVE_DATA", "Database closed");
		database.close();
	}
	
	public void retrieveData(int rowNumber)
	{
		//ArrayList <ProximityAlert> dataAlert = new ArrayList <ProximityAlert>();
		Log.d("RETRIEVE_DATA", "Inside retrieveData Method");
		Cursor  cursor = database.query(AlarmDbHelper.TABLE_NAME, clms, null, null, null, null, null);
		
		Log.d("RETRIEVE_DATA", "Returned " +cursor.getCount()+ " rows");
		if (cursor.getCount() >0)
		{
				cursor.moveToPosition(rowNumber-1);
				//ProximityAlert proximityAlert = new ProximityAlert();
				//proximityAlert.Hour = cursor.getInt(cursor.getColumnIndex(alarmDbHelper.HOUR_CLM));
				message = cursor.getString(cursor.getColumnIndex(AlarmDbHelper.DESC_CLM));
				String contacts = cursor.getString(cursor.getColumnIndex(AlarmDbHelper.CON_CLM));
				longtidue = cursor.getDouble(cursor.getColumnIndex(AlarmDbHelper.LONGI_CLM));
				latitude = cursor.getDouble(cursor.getColumnIndex(AlarmDbHelper.LATT_CLM));
				Log.d("RETRIEVE_DATA", "Message: " + message);
				Log.d("RETRIEVE_DATA", "Contacts: " + contacts);
				
				//dataAlert.add(proximityAlert);
			
			contactsList = contacts.split(" ");
			
			for(int i = 0; i < contactsList.length; i++)
			{
				Log.d("RETRIEVE_DATA", "Contact: " + contactsList[i]);
			}
			
		}
	}
	
	public void sendSMSMessage(String distance, String duration)
	{
		StringBuilder builder = new StringBuilder();
		builder.append(message + " I am " +distance + " meters away and " + duration + " out.");
		try 
		{
			SmsManager smsManager = SmsManager.getDefault();
			for(int i = 0; i < contactsList.length; i++)
			{
				
				smsManager.sendTextMessage(contactsList[i], null, builder.toString(), null, null);
			}
			
			//Toast.makeText(getApplicationContext(), "SMS Sent!",Toast.LENGTH_LONG).show();
		 } 
		catch (Exception e) 
		{
			//Toast.makeText(getApplicationContext(),"SMS fail, please try again later!",Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		
	}
	

}
