package com.google.codeu.data;

import java.util.HashMap;
import java.util.Map;

import com.google.codeu.data.LatLong;

/** 
 * All timestamps are unix epoch time.
 */
public class Trip	{
	private LatLong startLocation;
	private LatLong endLocation;
	private String transportationMode;
	private double distanceTravelled;
	private double timeSpent;
	private int departureTime;

	public Trip(LatLong startLocation, LatLong endLocation, String transportationMode,
			double distanceTravelled, double timeSpent, int departureTime)	{
		this.startLocation = startLocation;
		this.endLocation = endLocation;
		this.transportationMode = transportationMode;
		this.distanceTravelled = distanceTravelled;
		this.timeSpent = timeSpent;
		this.departureTime = departureTime;
	}

	public LatLong getStartLocation()	{
	  return startLocation;
	}

	public LatLong getEndLocation()	{
	  return endLocation;
	}

	public String getTransportationMode()	{
		return transportationMode;
	}

	public double getDistanceTravelled()	{
		return distanceTravelled;
	}
	
	public double getTimeSpent()	{
		return timeSpent;
	}

	public int getDepartureTime()	{
		return departureTime;
	}

	public Map asMap()	{
		HashMap map = new HashMap();
		map.put("startLocation", startLocation.asMap());
		map.put("endLocation", endLocation.asMap());
		map.put("distanceTravelled", distanceTravelled);
		map.put("timeSpent", timeSpent);
		map.put("departureTime", departureTime);
		return (Map) map;
	}
}
