package com.google.codeu.data;

import com.google.codeu.data.Trip;

import java.util.List;

/** 
 * All timestamps are unix epoch time.
 */
public class Route	{
  private List<Trip> tripList;
  
  public Route(List<Trip> tripList)  {
    this.tripList = tripList;
  }
  
  public List asList()	{
    return this.tripList;
  }
}
