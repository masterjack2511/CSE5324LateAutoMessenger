package edu.uta.team1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class GetDistance {

public static DistanceValues GetRoutDistane(double startLat, final double startLong, double endLat, double endLong)
{ 
	 
	DistanceValues values = new DistanceValues(0, "Cannot Calculate");
	String Duration="error";
	int Distance = 0;
  try {
      	Log.e("Distance Link : ", "http://maps.googleapis.com/maps/api/directions/json?origin="+ startLat +","+ startLong +"&destination="+ endLat +","+ endLong +"&sensor=false");
        JSONObject jsonObj = parser_Json.getJSONfromURL("http://maps.googleapis.com/maps/api/directions/json?origin="+ startLat +","+ startLong +"&destination="+ endLat +","+ endLong +"&sensor=false"); 
        
        JSONArray routes = jsonObj.getJSONArray("routes"); 
         JSONObject zero = routes.getJSONObject(0);
         JSONArray legs = zero.getJSONArray("legs");
         JSONObject zero2 = legs.getJSONObject(0);
         JSONObject dist = zero2.getJSONObject("distance");
         JSONObject dist2 = zero2.getJSONObject("duration");
         Distance = dist.getInt("value");
         Duration = dist2.getString("text");
         values = new DistanceValues(Distance, Duration);
        
    } catch (JSONException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
return values;
}
}


     
    

