package com.google.codeu.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.io.BufferedReader;
import java.net.URLDecoder;

import com.google.gson.Gson;

import com.google.codeu.data.Datastore;
import com.google.codeu.servlets.DatastoreHelper;
import com.google.codeu.servlets.QueryHelper;
import com.google.codeu.servlets.RouteFinderServlet;
import com.google.codeu.data.Route;

/*
 * Provides a storage interface for the maps frontend.
 * The user picks a route, then the frontend literally sends
 *     exactly the json that it would have recieved from Gmaps.
 *
 * Example POST request would be sending data that looks like:
 *
 * {
 *     directions : exactly whatever gmaps api sends you
 *     user : the email of the user, for example "test@example.com"
 * }
 */
@WebServlet("/storeroute")

public class StoreRouteServlet extends HttpServlet {
    private Datastore datastore;

    @Override
    public void init() {
        this.datastore = new Datastore();
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
	System.out.println("StoreRouteServlet got POST request!");
	QueryHelper helper = new QueryHelper("");
	StringBuilder sb = new StringBuilder();
	BufferedReader reader = request.getReader();
	RouteFinderServlet rfinder = new RouteFinderServlet();
	try	{
		String line;
		while ((line = reader.readLine()) != null) sb.append(line).append('\n');
	} finally	{
		reader.close();
	}
	String urlEncodedString = sb.toString();
	URLDecoder dec = new URLDecoder();
	String decodedString = dec.decode(urlEncodedString);
	String user = "UNKNOWN";
	String directionsJson = "";
	for (String s : decodedString.split("&"))  {
		String[] parts = s.split("=");
		String key = parts[0];
		String value = "";
		for (int i = 1; i < parts.length; i++) value += parts[i];

		if (key.equals("user"))  {
			user = value;
		}
		if (key.equals("directions"))  {
			directionsJson = value;
		}
	}
	System.out.println(directionsJson);
	Map<String, ?> directionsResult = helper.jsonStringToMap(directionsJson);
	List<Route> routeList = rfinder.directionsResultToRouteList(directionsResult, "null");
	this.datastore.storeRoute(routeList.get(0));  
	// routeList should only be of size 1 in this situation
    }
}
