package edu.uta.team1;

import java.util.ArrayList;

import edu.uta.team1.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ProximityAlertHomeActivity extends ListActivity {
	  private static final int DLG_DELETE = 1;
	  private ArrayList<ProximityAlert> reminders = null;

	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

	    String context = Context.LOCATION_SERVICE;
	    ProximityAlertHelper.getInstance().initializeLocationManager(this, (LocationManager) getSystemService(context));

	    loadReminders();
	  }

	  public void onListItemClick(ListView parent, View v, int position, long id)
	  {
	    /*if (position == 0) {
	      Intent intent = new Intent(this, ProximityAlertSetActivity.class);
	      startActivityForResult(intent, 0);
	      return;
	    }*/

	    editReminder(v, position);
	  }

	  // Creates the menu items
	  public boolean onCreateOptionsMenu(android.view.Menu menu) {
	    getMenuInflater().inflate(R.menu.home, menu);
	    return true;
	  }

	  // Handles item selections
	  public boolean onOptionsItemSelected(android.view.MenuItem item) {
	    switch (item.getItemId()) {
	      case R.id.home_add:
	        Intent intent = new Intent(this, ProximityAlertSetActivity.class);
	        startActivityForResult(intent, 0);
	        return true;
	      case R.id.home_deleteall:
	      showDialog(DLG_DELETE);
	    	  return true;
	      case R.id.home_settings:
	        intent = new Intent(this, ProximityAlertPreferencesActivity.class);
	        startActivityForResult(intent, 0);
	        return true;
	    
	    }

	    return false;
	  }

	  @Override
	  public void finish() {
	   ProximityAlertHelper.getInstance().closeLocationManager(this);
	    super.finish();
	  }

	/*  public void onBackPressed() {
	    ProximityAlertHelper.getInstance().closeLocationManager(this);
	    super.onBackPressed();
	  }*/

	/*  protected Handler dialogResultHandler = new Handler() {
	    @Override
	    public void handleMessage(Message msg) {
	      if (Boolean.parseBoolean(msg.obj.toString())) {
	        ProximityAlertHomeActivity.this.reminders.clear();
	        ProximityAlertHelper.getInstance().removeAlerts(ProximityAlertHomeActivity.this);
	        AlarmWrapper.deleteAll(ProximityAlertHomeActivity.this);
	        msg.ToastShort(ProximityAlertHomeActivity.this, "All Alerts Deleted");
	        loadReminders();
	      }
	      else {
	        msg.ToastShort(ProximityAlertHomeActivity.this, "Canceled");
	      }
	    }
	  };
*/
	  @Override
	  protected Dialog onCreateDialog(int id) {
	    // we have only one - defaulted to 0
	   // return DialogHelper.getInstance().displayAboutDialog(this);
		  
		  Dialog dlg = null;
		  
		  switch(id)
		  {
		  case DLG_DELETE:
		  {
			  AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage("Would you like to delete all events?");
				builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						ProximityAlertHomeActivity.this.reminders.clear();
				        ProximityAlertHelper.getInstance().removeAlerts(ProximityAlertHomeActivity.this);
				        AlarmWrapper.deleteAll(ProximityAlertHomeActivity.this);
				       Toast.makeText(ProximityAlertHomeActivity.this, "All Events Deleted", Toast.LENGTH_LONG).show();
				        loadReminders();
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

	  @Override
	  protected void onResume() {
	    super.onResume();
	    loadReminders();
	  }

	  protected void loadReminders() {
	    setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, loadFormattedAlertReminders()));
	  //?  ProximityAlertHelper.getInstance().addProximityAlerts(this, this.reminders);
	  }

	  protected void editReminder(View v, int position) {
	    Intent intent = new Intent(v.getContext(), ProximityAlertSetActivity.class);
	    intent.putExtra("current", this.reminders.get(position));
	    startActivityForResult(intent, 0);
	  }

	  protected ArrayList<String>loadFormattedAlertReminders()
	  {
	    this.reminders = AlarmWrapper.load(this);
	    Log.d("loadFormattedReminders", "No of reminders="+reminders.size());
	    ArrayList<String> formattedReminders = new ArrayList<String>();

	    for (ProximityAlert reminder:this.reminders) {
	      formattedReminders.add(reminder.Name);
	    }

	    return formattedReminders;
	  }
	}