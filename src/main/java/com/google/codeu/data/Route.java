package com.google.codeu.data;

import com.google.gson.Gson;

import com.google.codeu.data.Trip;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/** 
 * All timestamps are unix epoch time.
 */
public class Route	{
  private UUID id;
  private String user;
  private List<Trip> tripList;
  private long timestamp;

  public Route(List<Trip> tripList, String user)  {
	  this(UUID.randomUUID(), user, tripList, System.currentTimeMillis());
  }

  public Route(UUID id, String user, List<Trip> tripList, long timestamp)	{
	  this.id = id;
	  this.user = user;
	  this.tripList = tripList;
	  this.timestamp = timestamp;
  }

  public UUID getId()  {
    return this.id;
  }

  public String getUser()  {
    return this.user;
  }

  public long getTimestamp()  {
    return this.timestamp;
  }

  public String getRouteJson()  {
    Gson gson = new Gson();
    ArrayList<String> tripJsonList = new ArrayList<String>();
    for (Trip trip : this.tripList)  {
      tripJsonList.add(trip.toJson());
    }
    return gson.toJson(tripJsonList);
  }

  public HashMap<String, Double> getTotalDistanceByTransportationMode()	{
    HashMap<String, Double> transModeToDist = new HashMap<String, Double>();
    for (Trip trip : this.tripList)	{
      double currDist = 0;
      String mode = trip.getTransportationMode();
      if (transModeToDist.containsKey(mode))  {
        currDist = transModeToDist.get(mode);
      }
      transModeToDist.put(mode, currDist + trip.getDistanceTravelled());
    }
    return transModeToDist;
  }
  public List<Trip> asList()	{
    return this.tripList;
  }
}
