package com.google.codeu.data;

import java.util.HashMap;
import java.util.Map;

/** 
 * All timestamps are unix epoch time.
 */
public class LatLong {
	private double lat;
	private double lng;

	public LatLong(double lat, double lng)	{
	  this.lat = lat;
	  this.lng = lng;
	}

	public double getLat()	{
	  return this.lat;
	}

	public double getLong()  {
	  return this.lng;
	}

	public Map asMap()  {
		HashMap map = new HashMap();
		map.put("lat", lat);
		map.put("long", lng);
		return (Map) map;
	}
}
