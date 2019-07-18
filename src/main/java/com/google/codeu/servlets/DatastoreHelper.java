package com.google.codeu.servlets;

import java.util.HashMap;

import com.google.codeu.data.Datastore;
import com.google.codeu.data.Route;

public class DatastoreHelper	{
    private Datastore datastore;
    
    public DatastoreHelper(Datastore datastore)	{
	    this.datastore = datastore;
    }

    public HashMap<String, Double> userToSumDistanceTransportationMap(String userEmail)	{
	HashMap<String, Double> totalTransp = new HashMap<String, Double>();

	for (Route route : this.datastore.getRoutes(userEmail))	{
		System.out.println("route.getUser " + route.getUser());
		HashMap<String, Double> transpToDist = route.getTotalDistanceByTransportationMode();

		for (String key : transpToDist.keySet())	{
			double currDist = 0;
			if (totalTransp.containsKey(key))	{
				currDist = totalTransp.get(key);
			}
			totalTransp.put(key, currDist + transpToDist.get(key));
		}
	}
	return totalTransp;
    }
}
