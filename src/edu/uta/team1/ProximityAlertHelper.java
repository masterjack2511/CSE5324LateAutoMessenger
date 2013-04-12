package edu.uta.team1;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class ProximityAlertHelper {
	  private static final String ACTION_ALERT = "edu.uta.team1.action.alert";

	public static ProximityAlertHelper getInstance() {
	    if (instanceOf == null) {
	      instanceOf = new ProximityAlertHelper();
	    }
	    return instanceOf;
	  }

	  public String Provider;

	  private static ProximityAlertHelper instanceOf;
	  private static boolean initialized = false;
	  private LocationListener locationListener;
	  private HashMap<ProximityAlert, PendingIntent> proximityIntentsMap = new HashMap<ProximityAlert, PendingIntent>();
	  //private ProximityIntentReceiver receiver = null;

	  public boolean initializeLocationManager(Context context, LocationManager locationManager) {
	  /*  Criteria criteria = new Criteria();
	    criteria.setAccuracy(Criteria.ACCURACY_FINE);
	    criteria.setAltitudeRequired(false);
	    criteria.setBearingRequired(false);
	    criteria.setCostAllowed(true);
	    criteria.setPowerRequirement(Criteria.POWER_LOW);

	    this.Provider = locationManager.getBestProvider(criteria, false);*/
		  
		  List<String> l=locationManager.getProviders(true);
			if(l.contains(LocationManager.NETWORK_PROVIDER))
				Provider = LocationManager.NETWORK_PROVIDER;
			else if(l.contains(LocationManager.GPS_PROVIDER))
				Provider =LocationManager.GPS_PROVIDER;
		  
		  /*if(l.contains(LocationManager.GPS_PROVIDER))
				Provider = LocationManager.GPS_PROVIDER;
			else if(l.contains(LocationManager.NETWORK_PROVIDER))
				Provider =LocationManager.NETWORK_PROVIDER;*/
			
			Log.d("provider", Provider);
	    //this.receiver = new ProximityIntentReceiver();

	    Log.d("initializeLocation", "Provider="+this.Provider);
	    Toast.makeText(context, "provider="+this.Provider, Toast.LENGTH_LONG).show();
	    //IntentFilter filter = new IntentFilter(ACTION_ALERT);
	    //context.registerReceiver(this.receiver, filter);

	    return initialized = true;
	  }

	  public void requestLocationUpdates(Context caller, LocationListener locationListener) {
	    if (initialized) {
	      this.locationListener = locationListener;
	      String context = Context.LOCATION_SERVICE;
	      LocationManager mgr = ((LocationManager) caller.getSystemService(context));
	      mgr.removeUpdates(locationListener);

	      mgr.removeUpdates(locationListener);
	      long minInc = Long.parseLong(PreferenceManager.getDefaultSharedPreferences(caller).getString("mintimeinc", "2000"));
	      float minDistance = Float.parseFloat(PreferenceManager.getDefaultSharedPreferences(caller).getString("mindistance", "2"));
	      mgr.requestLocationUpdates(this.Provider, minInc, minDistance, locationListener);
	    }
	  }

	  public Address getLocation(Context caller,String address)
	  {
		  String context = Context.LOCATION_SERVICE;
	      LocationManager mgr = ((LocationManager) caller.getSystemService(context));
	    
	      Address addr=null;
	      Geocoder gc = new Geocoder(caller);
	      if(Geocoder.isPresent()){
	    	  Log.d("getLocation", "Service is present");
	      } else {
	    	  Log.d("getLocation", "Service is not present");
	      }
	    	  
	      try {
				List<Address> list= gc.getFromLocationName(address, 10);
				
				if(!list.isEmpty())
				{
					addr = list.get(0);
				}
	      }
	      catch(Exception e)
	      {
	    	  Log.e("getLocation", "error="+e.getMessage());
	      }
		  
		return addr;
		  
	  }
	  
	  public Address getAddressName(Context caller, double lat, double lng){
		  Address addr = null;
		  Geocoder gc = new Geocoder(caller);
		  if(Geocoder.isPresent()){
	    	  Log.d("getLocation", "Service is present");
	      } else {
	    	  Log.d("getLocation", "Service is not present");
	      }
		  try {
			  List<Address> list = gc.getFromLocation(lat, lng, 10);
			  
			  if(!list.isEmpty()) {
				  addr = list.get(0);
			  }
		  } catch (Exception e) {
			  Log.e("getAddressName", "error="+e.getMessage());
		  }
		  return addr;
	  }
	  
	  public boolean addProximityAlerts(Context caller, ArrayList<ProximityAlert> reminders) {
		  
		  Log.d("addProximityAlert", "inside");
	    if (initialized) {
	      //String context = Context.LOCATION_SERVICE;
	      //LocationManager mgr = ((LocationManager) caller.getSystemService(context));

	      Log.d("addProximityAlert", "before removing size ="+reminders.size());
	      removeAlerts(caller);
	      Log.d("addProximityAlert", "after removing size ="+reminders.size());
		  
	      for (int i=0; i <reminders.size(); i++) {
	    	     
	        ProximityAlert reminder = reminders.get(i);
	        Log.d("addProximityAlert", "reminder "+reminder.Name);
    		
	        //Intent intent = new Intent(ACTION_ALERT);
	     //  intent.setExtrasClassLoader(caller.getClassLoader());
	     //   intent.putExtra("current", reminder);
	        //intent.putExtra("name", reminder.Name);
	        //intent.putExtra("descr", reminder.Desc);
	        //PendingIntent proximityIntent = PendingIntent.getBroadcast(caller, i, intent, 0);
	        float proxRadius = Float.parseFloat(PreferenceManager.getDefaultSharedPreferences(caller).getString("proxradius", "2"));
	        //TODO: expiry based on user's input
	        long expiry = getExpiry(reminder);
	        Log.d("addProximityAlert", "adding alert for "+reminder.Name);
  
	        Intent intent = new Intent(caller, AlarmReceiver.class);
	        PendingIntent sender = PendingIntent.getBroadcast(caller, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	        
	        // Get the AlarmManager service
	        AlarmManager am = (AlarmManager) caller.getSystemService(Activity.ALARM_SERVICE);
	        am.set(AlarmManager.RTC_WAKEUP, expiry, sender);
	        
	        //mgr.addProximityAlert(reminder.Lat, reminder.Lng, proxRadius, expiry, proximityIntent);
	        //proximityIntentsMap.put(reminder, proximityIntent);
	      }
	      return true;
	    }
	    return false;
	  }

	  private long getExpiry(ProximityAlert reminder) {
		long exp=0;  
		Calendar c = new GregorianCalendar(reminder.Year, reminder.Month, reminder.DayOfMonth, reminder.Hour, reminder.Min);
		exp = c.getTimeInMillis();
		return exp;
	}

	public void removeAlert(Context caller, ProximityAlert reminder) {
	    if (initialized) {
	      String context = Context.LOCATION_SERVICE;
	      LocationManager mgr = ((LocationManager) caller.getSystemService(context));

	      removeAlert(mgr, reminder);
	    }
	  }

	  public void removeAlerts(Context caller) {
	    if (initialized) {
	      String context = Context.LOCATION_SERVICE;
	      LocationManager mgr = ((LocationManager) caller.getSystemService(context));

	      for (ProximityAlert reminder:this.proximityIntentsMap.keySet()) {
	        removeAlert(mgr, reminder);
	      }
	    }
	  }

	  public void closeLocationManager(Context caller) {
	    if (initialized) {
	      try {
	        String context = Context.LOCATION_SERVICE;
	        LocationManager mgr = ((LocationManager) caller.getSystemService(context));

	        if (this.locationListener != null) {
	          mgr.removeUpdates(this.locationListener);
	        }

	       // removeAlerts(caller);

	       // caller.unregisterReceiver(this.receiver);
	      }
	      catch(Exception ex) {
	        Log.e("ProximityAlert", "exception thrown in closeLocationManager(...)", ex);
	      }
	    }
	  }

	  protected void removeAlert(LocationManager mgr, ProximityAlert reminder) {
	    if (initialized) {
	      if (reminder.Name.trim().length() > 0) {
	        mgr.removeProximityAlert(this.proximityIntentsMap.get(reminder));
	      }
	    }
	  }
	}
