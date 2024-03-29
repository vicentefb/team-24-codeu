package com.google.codeu.servlets;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import com.google.codeu.data.Datastore;
import com.google.codeu.data.Route;
import com.google.codeu.data.Trip;
import com.google.codeu.data.LatLong;
import com.google.codeu.data.User;
import com.google.codeu.servlets.QueryHelper;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/**
 * A servlet that handles all of the route finding.
 */
@WebServlet("/routefinder")
public class RouteFinderServlet extends HttpServlet {
  
  private Datastore datastore;
  
  @Override
  public void init() {
    datastore = new Datastore();
  }

  /**
   * Given a start and end address, @return the available routes,
   *   as calculated by the Google Maps Directions API.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    response.setContentType("text/html");

    String startAddress = request.getParameter("startAddress");
    String endAddress = request.getParameter("endAddress");
    String user = request.getParameter("userEmail");

    if (startAddress == null || startAddress.equals("") ||
       endAddress == null || endAddress.equals("")) {
      response.getOutputStream().println("ERROR! One address is either null or empty.");
    }

    String key = "YOUR_API_KEY";
    QueryHelper queryHelper = new QueryHelper(key);
    Map directionsResult = queryHelper.mapApiCall(startAddress, endAddress);
    List<Route> routeList = directionsResultToRouteList(directionsResult, user);
    response.getOutputStream().println(new Gson().toJson(routeList));
  }

  public List<Route> directionsResultToRouteList(Map<String, ?> directionsResult, String user)	{
    Trip someTrip;
    List<Route> routeList = new ArrayList<Route>();
    for (Map route : (ArrayList<Map>) directionsResult.get("routes"))  {
      for (Map leg : (ArrayList<Map>) route.get("legs"))  {
        ArrayList<Map> steps = (ArrayList<Map>) leg.get("steps");
        ArrayList<Trip> tripList = new ArrayList<Trip>();

        for (Map step : steps) {
		tripList.add(stepToTrip(step));
	}
        routeList.add(new Route(tripList, user));
      }
    }
    return routeList;
  }

  private Trip stepToTrip(Map step)	{
    LatLong startLocation = locationMapToLatLong((Map) step.get("start_location"));
    LatLong endLocation = locationMapToLatLong((Map) step.get("end_location"));
    return new Trip(startLocation, endLocation,
	       (String) step.get("travel_mode").toString(), 
	       ((double) ((Map) step.get("distance")).get("value")) / 1000,
	       ((double) ((Map) step.get("duration")).get("value")) / 1000,
	       20);  // unsure about departure time right now...
  }

  private LatLong locationMapToLatLong(Map<String, Double> map)	{
    double latval = map.get("lat");
    double longval = map.get("lng");
    return new LatLong(latval, longval);
  }
}
