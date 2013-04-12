package edu.uta.team1;

public class DistanceValues {
	
	private String duration;
	private int distance;

	public DistanceValues(int Distance, String Duration) {
		distance = Distance;
		duration = Duration;
		// TODO Auto-generated constructor stub
	}

	public int GetDistance()
	{
		return distance;
	}
	
	public String GetDuration()
	{
		return duration;
	}
}
