package edu.uta.team1;

import java.util.ArrayList;
import java.util.Calendar;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class ProximityAlert implements Parcelable {
	  private static long nextId = 0;
	  public long Id = -1;
	  public double Lat = 0;
	  public double Lng = 0;
	  public String Name = "";
	  public String Desc = "";
	  public String Expiration = "";
	  public int Month = -1;
	  public int DayOfMonth = -1;
	  public int Year = -1;
	  public int Hour = -1;
	  public int Min = -1;
	  public ArrayList<String> Items = new ArrayList<String>();
	  public boolean Enabled = false;
	  public boolean IsNew = true;

	  public ProximityAlert(long id, double lat, double lng, String name, String desc, int month, int dayOfMonth, int year, int hour, int min, boolean enabled) {
	    this.Lat = lat;
	    this.Lng = lng;
	    this.Id = id;
	    this.Name = name;
	    this.Desc = desc;
	    this.Month = month;
	    this.DayOfMonth = dayOfMonth;
	    this.Year = year;
	    this.Hour = hour;
	    this.Min = min;
	    this.Enabled = enabled;
	  }

	  public ProximityAlert(double lat, double lng, String name, String desc, int month, int dayOfMonth, int year, int hour, int min, boolean enabled) {
	    this(nextId++, lat, lng, name, desc, month, dayOfMonth, year, hour, min, enabled);
	  }

	  public ProximityAlert(long id, String name, String desc) {
	    this(id, 0.0, 0.0, name, desc, Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), false);
	  }

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		Log.d("writeToParcel", "inside");
		dest.writeLong(this.Id);
		dest.writeString(Name);
		dest.writeString(Desc);
		dest.writeDouble(Lat);
		dest.writeDouble(Lng);
		dest.writeInt(DayOfMonth);
		dest.writeInt(Month);
		dest.writeInt(Year);
		dest.writeInt(Hour);
		dest.writeInt(Min);
		if(this.Enabled)
			dest.writeInt(1);
		else
			dest.writeInt(0);
		
		Log.d("writeToParcel", "returning");
	}

	public static final Parcelable.Creator<ProximityAlert> CREATOR = new Parcelable.Creator<ProximityAlert>() {   
		public ProximityAlert createFromParcel(Parcel in) {    
			return new ProximityAlert(in);  
			}         
	public ProximityAlert[] newArray(int size) {    
		return new ProximityAlert[size];     
		}    
	};
	  
	
	 private ProximityAlert(Parcel in) {    
		 Log.d("ProximityAlert", "inside parcel");
		 this.Id = in.readLong();  
		 this.Name = in.readString();
		 this.Desc = in.readString();
		 this.Lat = in.readDouble();
		 this.Lng = in.readDouble();
		 this.DayOfMonth = in.readInt();
		 this.Month = in.readInt();
		 this.Year = in.readInt();
		 this.Hour = in.readInt();
		 this.Min = in.readInt();
		 int enabled = in.readInt();
		 if(enabled ==1)
			 this.Enabled=true;
		 else
			 this.Enabled=false;
		 
		 Log.d("ProximityAlert", "return parcel");
		 }
	}
