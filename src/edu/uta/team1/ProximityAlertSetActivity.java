package edu.uta.team1;

import edu.uta.team1.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.TextView;
import android.widget.Toast;

public class ProximityAlertSetActivity extends Activity {
	  private static final int DLG_CANCEL = 1;
	  private static final int DLG_DELETE = 2;
	  private static final int CONTACT_LIST_REQUEST = 1001;
	  private double lat = 00;
	  private double lng = 00;
	  private boolean newReminder = true;
	  private String contactList = "";
	  private ProximityAlert current;
	  private CheckBox currentC;
	  
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.alert_reminder);
	    currentC = (CheckBox) findViewById(R.id.currentC);
	    this.newReminder = !(getIntent().getExtras() != null && getIntent().getExtras().containsKey("current"));
	    if (this.newReminder) {
	    
	    	
	    	
	      String context = Context.LOCATION_SERVICE;
	      updateWithNewLocation(((LocationManager) getSystemService(context)).getLastKnownLocation(ProximityAlertHelper.getInstance().Provider));
	    }
	    else {
	      currentC.setVisibility(CheckBox.INVISIBLE);
	      current = retrieveCurrentAlertReminder();
	      ((TextView) findViewById(R.id.headerT)).setText("Lat:" + current.Lat + "\nLong:" + current.Lng);
	      ((TextView) findViewById(R.id.nameE)).setText(current.Name);
	      ((TextView) findViewById(R.id.descrE)).setText(current.Desc);
	      DatePicker picker = (DatePicker)findViewById(R.id.expiryD);
	      TimePicker timePicker = (TimePicker)findViewById(R.id.time);
	      picker.updateDate(current.Year, current.Month, current.DayOfMonth);
	      timePicker.setCurrentHour(current.Hour);
	      timePicker.setCurrentMinute(current.Min);
	      contactList = current.Contacts;
	      
	      if(current.Enabled)
	      {
	    	  CheckBox enable = (CheckBox) findViewById(R.id.enabledC);
	    	  enable.setChecked(true);
	      }
	      
	    }
	  }

	  // Creates the menu items
	  public boolean onCreateOptionsMenu(android.view.Menu menu) {
	    getMenuInflater().inflate(R.menu.alert_menu, menu);
	    if(this.newReminder)
	    	menu.removeItem(R.id.map);
	    return true;
	  }

	  // Handles item selections
	  public boolean onOptionsItemSelected(android.view.MenuItem item) {
	    switch (item.getItemId()) {
	      case R.id.save:
	        addAlert();
	        return true;
	      case R.id.map:
	    	 // showDialog(DLG_CANCEL);geo:0,180?z=1
	    	  Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:"+current.Lat+","+current.Lng+"?q="+current.Name+"&z=8"));
				startActivity(i);
	       // DialogHelper.getInstance().displayConfirmationDialog(this, this.dialogCancelResultHandler, "Confirmation", "Would you like to cancel adding this alert?");
	        return true;
	      case R.id.delete:
	    	  showDialog(DLG_DELETE);
	      //  DialogHelper.getInstance().displayConfirmationDialog(this, this.dialogDeleteResultHandler, "Confirmation", "Would you like to delete this alert?");
	        return true;
	      case R.id.settings:
	        Intent intent = new Intent(this, ProximityAlertPreferencesActivity.class);
	        startActivityForResult(intent, 0);
	        return true;
	     /* case R.id.add_menu_about:
	        showDialog(0);
	        return true;*/
	    }

	    return false;
	  }

	  protected ProximityAlert retrieveCurrentAlertReminder() {
	    ProximityAlert current = null;

	    try {
	      current = getIntent().getParcelableExtra("current");
	    	  //new ProximityAlert(getIntent().getExtras().getString("current"));
	    }
	    catch (Exception ex) {
	      Log.e("ProximityAlert", "failure in AlertReminderActivity", ex);
	    }

	    return current;
	  }

	  protected void addAlert() {
	    String name = ((TextView) findViewById(R.id.nameE)).getText().toString();
	    String desc = ((TextView) findViewById(R.id.descrE)).getText().toString();
	    DatePicker picker = (DatePicker)findViewById(R.id.expiryD);
	    TimePicker timePicker = (TimePicker)findViewById(R.id.time);
	    CheckBox enabledC = (CheckBox) findViewById(R.id.enabledC);
	    if(!currentC.isChecked())
	    {
	    	Address addr=ProximityAlertHelper.getInstance().getLocation(this, name);
	    	if(addr!=null)
	    	{
	    	this.lat= addr.getLatitude();
	    	this.lng = addr.getLongitude();
	    	}
	    	else
	    	{
	    		Toast.makeText(this, "Could not convert into address..try later", Toast.LENGTH_LONG).show();
	    		finish();
	    	}
	    //If using current location, generates address string from lat/long coordinates
	    } else {
	    	Address addr = ProximityAlertHelper.getInstance().getAddressName(this, this.lat, this.lng);
	    	if(addr != null){
	    		StringBuilder addressBuilder = new StringBuilder();
	    		String thoroughfare = addr.getThoroughfare();
	    		String sublocality = addr.getSubLocality();
	    		String adminArea = addr.getAdminArea();
	    		String country = addr.getCountryName();
	    		String postalCode = addr.getPostalCode();
	    		
	    		if(thoroughfare != null) addressBuilder.append(thoroughfare + ", ");
	    		if(sublocality != null) addressBuilder.append(sublocality + ", ");
	    		if(adminArea != null) addressBuilder.append(adminArea + ", ");
	    		if(country != null) addressBuilder.append(country + ", ");
	    		if(postalCode !=null) addressBuilder.append(postalCode);
	    		
	    		String address = addressBuilder.toString();
	    		if(address.endsWith(", ")){
	    			address = address.substring(0, address.length()-2);
	    		}
	    		name = address;
	    	}
	    }
	    

	    // add proximity alert intent
	    if (newReminder) {
	      ProximityAlert reminder = new ProximityAlert(this.lat, this.lng, name, desc, picker.getMonth(), picker.getDayOfMonth(), picker.getYear(),timePicker.getCurrentHour(), timePicker.getCurrentMinute(), contactList, enabledC.isChecked());
	      ProximityAlertHelper.getInstance().addProximityAlerts(this, AlarmWrapper.add(this, reminder));
	    }
	    else
	    {
	    	 ProximityAlert reminder = new ProximityAlert(current.Id,this.lat, this.lng, name, desc, picker.getMonth(), picker.getDayOfMonth(), picker.getYear(),timePicker.getCurrentHour(), timePicker.getCurrentMinute(), contactList, enabledC.isChecked());
		      ProximityAlertHelper.getInstance().addProximityAlerts(this, AlarmWrapper.edit(this, reminder));
		 
	    }

	    finish();
	  }

	 /* protected Handler dialogCancelResultHandler = new Handler() {
	    @Override
	    public void handleMessage(Message msg) {
	      if (Boolean.parseBoolean(msg.obj.toString())) {
	        msg.ToastShort(ProximityAlertSetActivity.this, "Alert has been canceled!");
	        finish();
	      }
	      else {
	        msg.ToastShort(ProximityAlertSetActivity.this, "Alert has NOT been canceled");
	      }
	    }
	  };

	  protected Handler dialogDeleteResultHandler = new Handler() {
	    @Override
	    public void handleMessage(Message msg) {
	      if (Boolean.parseBoolean(msg.obj.toString())) {
	        ProximityAlert current = retrieveCurrentAlertReminder();
	        ProximityAlertHelper.getInstance().removeAlert(ProximityAlertSetActivity.this, current);
	        msg.ToastShort(ProximityAlertSetActivity.this, "Alert has been deleted!");
	        finish();
	      }
	      else {
	        msg.ToastShort(ProximityAlertSetActivity.this, "Alert has NOT been deleted");
	      }
	    }
	  };
*/
	  @Override
	  protected Dialog onCreateDialog(int id) {
	   Dialog dlg =null;
	   switch(id)
	   {
	   case DLG_CANCEL:
	   	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Would you like to cancel?");
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});
		
		dlg = builder.create();
	   	}
	   	break;
	   	
	   case DLG_DELETE:
	   {
		   AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Would you like to delete this event?");
			builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					ProximityAlertHelper.getInstance().removeAlert(ProximityAlertSetActivity.this, current);
					AlarmWrapper.delete(ProximityAlertSetActivity.this, current);
					finish();
				}
			});
			
			builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.cancel();
				}
			});
			
			dlg = builder.create();
	   }
	   break;
	   }
	   return dlg;
	  }

	  //Register for the updates when Activity is in foreground
	  @Override
	  protected void onResume() {
	    super.onResume();
	    if (this.newReminder) {
	      ProximityAlertHelper.getInstance().requestLocationUpdates(this, this.locationListener);
	    }
	  }

	  // Stop the updates when Activity is paused
	  @Override
	  protected void onPause() {
	    super.onPause();
	    if (this.newReminder) {
	      String context = Context.LOCATION_SERVICE;
	      ((LocationManager) getSystemService(context)).removeUpdates(this.locationListener);
	    }
	  }

	  private final LocationListener locationListener = new LocationListener() {
	    public void onLocationChanged(Location location) {
	      updateWithNewLocation(location);
	    }

	    public void onProviderDisabled(String provider) {
	      updateWithNewLocation(null);
	    }

	    public void onProviderEnabled(String provider) {
	    }

	    public void onStatusChanged(String provider, int status, Bundle extras) {
	    }
	  };

	  private void updateWithNewLocation(Location location) 
	  {
	    String latLongString;
	    TextView myLocationText = (TextView) findViewById(R.id.headerT);
	    if (location != null) {
	      this.lat = location.getLatitude();
	      this.lng = location.getLongitude();
	      latLongString = "Lat:" + lat + "\nLong:" + lng;
	    } else {
	      latLongString = "No location found - Please move more than " + PreferenceManager.getDefaultSharedPreferences(this).getString("minDistance", "1000") + " from current position";
	    }

	    myLocationText.setText("Your Current Position is:\n" + latLongString);
	  }

	  public void addContactList(View view)
	  {
		  Intent contactIntent = new Intent(this, Activity_Contacts.class);
		  contactIntent.putExtra("contacts", contactList);
		  startActivityForResult(contactIntent, CONTACT_LIST_REQUEST);
	  }
	  
	  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		  if(requestCode == CONTACT_LIST_REQUEST){
			  if(resultCode == RESULT_OK) {
				  contactList = data.getStringExtra("contacts");
				  Toast.makeText(getApplicationContext(), "Got the contacts", Toast.LENGTH_SHORT).show();
			  }
		  }
	  }
}
