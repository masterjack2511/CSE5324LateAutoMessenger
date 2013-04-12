package edu.uta.team1;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {

	public void onReceive(Context context, Intent intent) {
		//Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		//Ringtone r = RingtoneManager.getRingtone(context, notification);
		int identifier = (int) intent.getLongExtra("identifier", -9999);
		Log.d("AlarmReceiver", "identifier: " + identifier);
		RetrieveSendData retrieveSendData = new RetrieveSendData(context);
		retrieveSendData.open();
		retrieveSendData.retrieveData(identifier);
		Log.d("AlarmReceiver", "________________________________________________");
		retrieveSendData.close();
		
		DistanceValues values = GetDistance.GetRoutDistane(0.0, 0.0, retrieveSendData.latitude, retrieveSendData.longtidue);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		String radiusStr = prefs.getString("proxRadius", "2");
		double radius = Double.parseDouble(radiusStr);
		if(values.GetDistance() >= radius){
			retrieveSendData.sendSMSMessage(Integer.toString(values.GetDistance()), values.GetDuration());
		}
		//r.play();
	}
	
	public void setAlarm(Context context, long time)
	{
		AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        am.set(AlarmManager.RTC_WAKEUP, time, pi);
	}

}
