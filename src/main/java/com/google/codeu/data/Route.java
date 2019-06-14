package com.google.codeu.data;

import java.util.List;

/** 
 * All timestamps are unix epoch time.
 */
public class Route	{
	private String userEmail;
	private List<String> addressList;
	private float distanceTravelled;
	private float departureTime;
	private float timeSpent;

	public Route(List<String> addressList, float distanceTravelled, 
				 float departureTime, float timeSpent)	{
		this.addressList = addressList;
		this.distanceTravelled = distanceTravelled;
		this.departureTime = departureTime;
		this.timeSpent = timeSpent;
	}

	public List<String> getAddressList()	{
		return addressList;
	}

	public float getDistanceTravelled()	{
	   return distanceTravelled;
	}

	public float getDepartureTime()	{
		return 	departureTime;
	}
	
	public float getTimeSpent()	{
		return timeSpent;
	}
}
