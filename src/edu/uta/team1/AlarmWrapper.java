package edu.uta.team1;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class AlarmWrapper {
	
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
		AlarmDbHelper.enabled_CLM
		
	};
	
	public static ArrayList<ProximityAlert> load(Context caller) {
	    ArrayList<ProximityAlert> reminders = new ArrayList<ProximityAlert>();
	    
	    AlarmDbHelper helper = new AlarmDbHelper(caller);
	    SQLiteDatabase db = helper.getReadableDatabase();
	    
	    Cursor c = db.query(AlarmDbHelper.TABLE_NAME, clms, null, null, null, null, null);
	    
	    if(c.getCount()>0)
	    {
	    	c.moveToFirst();
	    	int id_index = c.getColumnIndex(AlarmDbHelper._ID);
	    	int name_index = c.getColumnIndex(AlarmDbHelper.NAME_CLM);
	    	int desc_index = c.getColumnIndex(AlarmDbHelper.DESC_CLM);
	    	int latt_index = c.getColumnIndex(AlarmDbHelper.LATT_CLM);
	    	int long_index = c.getColumnIndex(AlarmDbHelper.LONGI_CLM);
	    	int day_index = c.getColumnIndex(AlarmDbHelper.DAY_CLM);
	    	int month_index = c.getColumnIndex(AlarmDbHelper.MONTH_CLM);
	    	int year_index = c.getColumnIndex(AlarmDbHelper.YEAR_CLM);
	    	int hour_index = c.getColumnIndex(AlarmDbHelper.HOUR_CLM);
	    	int min_index = c.getColumnIndex(AlarmDbHelper.MIN_CLM);
	    	int enabled_index = c.getColumnIndex(AlarmDbHelper.enabled_CLM);
	    	
	    	do{
	    		int id = c.getInt(id_index);
	    		String name = c.getString(name_index);
	    		String desc = c.getString(desc_index);
	    		Double latt = c.getDouble(latt_index);
	    		Double longi = c.getDouble(long_index);
	    		int day = c.getInt(day_index);
	    		int month = c.getInt(month_index);
	    		int year = c.getInt(year_index);
	    		int hour = c.getInt(hour_index);
	    		int min = c.getInt(min_index);
	    		int enabled = c.getInt(enabled_index);
	    		if(enabled ==1)
	    			reminders.add(new  ProximityAlert(id, latt, longi, name, desc, month, day, year, hour, min, true));
	    		else
	    			reminders.add(new  ProximityAlert(id, latt, longi, name, desc, month, day, year, hour, min, false));
	    	}
	    	while (c.moveToNext());
	    	
	    	
	    }
	    c.close();
	    db.close();
	    
	   /* try {
	      SharedPreferences settingsActivity = PreferenceManager.getDefaultSharedPreferences(caller);
	      String serializedReminders = settingsActivity.getString(caller.getString(R.string.reminders), "").trim();
	      reminders = new ArrayList<ProximityAlert>();

	      if (serializedReminders.length() > 0) {
	        JSONArray jsonObjs = new JSONArray(serializedReminders);
	        for (int i=0; i<jsonObjs.length(); i++) {
	          JSONObject itemObj = jsonObjs.getJSONObject(i);
	          reminders.add(new ProximityAlertReminder(itemObj));
	        }
	      }*/

	      /*if (reminders.size() == 0) {
	        ProximityAlert addReminder = new ProximityAlert(-2, "<click to add new alert>", "Add");
	        reminders.add(0, addReminder);
	      }*/
	   
	    return reminders;
	  }

	public static ArrayList<ProximityAlert> edit(Context caller, ProximityAlert reminder) {
		 ArrayList<ProximityAlert> reminders = load(caller);
		 
		 for(int i=0;i<reminders.size();i++)
		 {
			 ProximityAlert rmd = reminders.get(i);
			 if(rmd.Id == reminder.Id)
			 {
				 reminders.remove(i);
				 Toast.makeText(caller, "Removed from array", Toast.LENGTH_LONG).show();
				 
				 break;
			 }
		 }
			 
			 AlarmDbHelper helper = new AlarmDbHelper(caller);
			    SQLiteDatabase db = helper.getWritableDatabase();
			    
			    ContentValues values = new ContentValues();
			    values.put(AlarmDbHelper.NAME_CLM, reminder.Name);
			    values.put(AlarmDbHelper.LATT_CLM, reminder.Lat);
			    values.put(AlarmDbHelper.LONGI_CLM, reminder.Lng);
			    values.put(AlarmDbHelper.DESC_CLM, reminder.Desc);
			    values.put(AlarmDbHelper.DAY_CLM, reminder.DayOfMonth);
			    values.put(AlarmDbHelper.MONTH_CLM, reminder.Month);
			    values.put(AlarmDbHelper.YEAR_CLM, reminder.Year);
			    values.put(AlarmDbHelper.HOUR_CLM, reminder.Hour);
			    values.put(AlarmDbHelper.MIN_CLM, reminder.Min);
			    if(reminder.Enabled)
			    	values.put(AlarmDbHelper.enabled_CLM, 1);
			    else
			    	values.put(AlarmDbHelper.enabled_CLM, 0);
			    
			    db.update(AlarmDbHelper.TABLE_NAME, values, "_ID="+reminder.Id,null);
		 
		 reminders.add(reminder);
		 
		 db.close();
		return reminders;
		 
		 
	}
	
	
	  public static ArrayList<ProximityAlert> add(Context caller, ProximityAlert reminder) {
	    ArrayList<ProximityAlert> reminders = load(caller);
	    reminders.add(reminder);
	    
	    //save(caller, reminders);
	    
	    ContentValues values = new ContentValues();
	    values.put(AlarmDbHelper.NAME_CLM, reminder.Name);
	    values.put(AlarmDbHelper.LATT_CLM, reminder.Lat);
	    values.put(AlarmDbHelper.LONGI_CLM, reminder.Lng);
	    values.put(AlarmDbHelper.DESC_CLM, reminder.Desc);
	    values.put(AlarmDbHelper.DAY_CLM, reminder.DayOfMonth);
	    values.put(AlarmDbHelper.MONTH_CLM, reminder.Month);
	    values.put(AlarmDbHelper.YEAR_CLM, reminder.Year);
	    values.put(AlarmDbHelper.HOUR_CLM, reminder.Hour);
	    values.put(AlarmDbHelper.MIN_CLM, reminder.Min);
	    if(reminder.Enabled)
	    	values.put(AlarmDbHelper.enabled_CLM, 1);
	    else
	    	values.put(AlarmDbHelper.enabled_CLM, 0);
		   
	    AlarmDbHelper helper = new AlarmDbHelper(caller);
	    SQLiteDatabase db = helper.getWritableDatabase();
	    db.insert(AlarmDbHelper.TABLE_NAME, null, values);
	    
	    db.close();
	    return reminders;
	  }

	 /* public static boolean save(Context caller, ArrayList<ProximityAlert> reminders) {
	    boolean success = false;

	    try {
	      SharedPreferences.Editor prefEditor = PreferenceManager.getDefaultSharedPreferences(caller).edit();

	      ArrayList<JSONObject> itemObjs = new ArrayList<JSONObject>(reminders.size());
	      for (ProximityAlertReminder item:reminders) {
	        JSONObject serializedItemObj = new JSONObject(item.serialize());
	        itemObjs.add(serializedItemObj);
	      }

	      prefEditor.putString(caller.getString(R.string.reminders), new JSONArray(itemObjs).toString());
	      prefEditor.commit();
	      success = true;
	    }
	    catch (Exception ex) {
	      Log.e(AppSettings.DEBUG_TAG, ex.getMessage(), ex);
	      success = false;
	    }

	    return success;
	  }*/

	  public static boolean deleteAll(Context caller) {
	    boolean success = false;

	    /*try {
	      SharedPreferences.Editor prefEditor = PreferenceManager.getDefaultSharedPreferences(caller).edit();
	      prefEditor.putString(caller.getString(R.string.reminders), "");

	      prefEditor.commit();
	      success = true;
	    }
	    catch (Exception ex) {
	      Log.e(AppSettings.DEBUG_TAG, ex.getMessage(), ex);
	      success = false;
	    }*/
	    
	    SQLiteDatabase db = new AlarmDbHelper(caller).getWritableDatabase();
	    
	    db.delete(AlarmDbHelper.TABLE_NAME, null, null);
	    db.close();

	    return success;
	  }
	  
	  public static boolean delete(Context caller, ProximityAlert reminder) {
		    boolean success = false;

		    /*try {
		      SharedPreferences.Editor prefEditor = PreferenceManager.getDefaultSharedPreferences(caller).edit();
		      prefEditor.putString(caller.getString(R.string.reminders), "");

		      prefEditor.commit();
		      success = true;
		    }
		    catch (Exception ex) {
		      Log.e(AppSettings.DEBUG_TAG, ex.getMessage(), ex);
		      success = false;
		    }*/
		    
		    SQLiteDatabase db = new AlarmDbHelper(caller).getWritableDatabase();
		    
		    db.delete(AlarmDbHelper.TABLE_NAME, "_ID="+reminder.Id, null);
		    db.close();

		    return success;
		  }

}
