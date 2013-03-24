package edu.uta.team1;

import edu.uta.team1.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class ProximityAlertPreferencesActivity extends PreferenceActivity  {
	  public ProximityAlertPreferencesActivity() {
	  }

	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(null);
	    addPreferencesFromResource(R.xml.settings);
	  }
	}
