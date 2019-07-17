package com.google.codeu.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.codeu.data.Datastore;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.codeu.data.Datastore;
import com.google.codeu.servlets.DatastoreHelper;

/*
 * The purpose of this servlet is to retrieve the data needed for the four
 * donut charts at the top of the my-stats.html page
 */
@WebServlet("/miniStats")

public class MiniStatsServlet extends HttpServlet {
    private Datastore datastore;

    @Override
    public void init() {
        this.datastore = new Datastore();
    }
    
    //holds info for the miles donut chart
    private class MilesChartData {
        private double totalMiles;
        private double totalMilesGoal;

        private MilesChartData(double totalMiles, double totalMilesGoal) {
            this.totalMiles = totalMiles;
            this.totalMilesGoal = totalMilesGoal;    
        }
    }

    //holds info for the hours donut chart
    private class HoursChartData {
        private double totalHours;
        private double totalHoursGoal;

        private HoursChartData(double totalHours, double totalHoursGoal) {
            this.totalHours = totalHours;
            this.totalHoursGoal = totalHoursGoal;    
        }
    }

    //holds info for the favorite mode donut chart
    private class FavModeChartData {
        private String favMode;
        private double favModeMiles;

        private FavModeChartData(String favMode, double favModeMiles) {
            this.favMode = favMode;
            this.favModeMiles = favModeMiles;   
        }
    }

    //holds info for the challenges donut chart
    private class ChallengesChartData {
        private int totalChallengesWon;
        private int totalChallengesGoal;

        private ChallengesChartData(int totalChallengesWon, int totalChallengesGoal) {
            this.totalChallengesWon = totalChallengesWon;
            this.totalChallengesGoal = totalChallengesGoal;   
        }
    }

    public double getSustainableMiles(HashMap<String, Double> transpToDist)	{
	    double sustainableKilos = 0;
	    for (String key : transpToDist.keySet()) {
		    if (key != "DRIVING") sustainableKilos += transpToDist.get(key);
	    }
	    return sustainableKilos / 1.609;
    }

    public String getMostUsedTransportationMode(HashMap<String, Double> transpToDist)	{
	    String mostUsed = "None";
	    double highestAmount = 0;
	    double amount;
	    for (String key : transpToDist.keySet())	{
		   amount = transpToDist.get(key);
		   if (amount > highestAmount)	{
			   mostUsed = key;
			   highestAmount = amount;
		   }
	    }
	    return mostUsed;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
	String userEmail = request.getRemoteUser();

	DatastoreHelper helper = new DatastoreHelper(this.datastore);
	HashMap<String, Double> transpToDist = helper.userToSumDistanceTransportationMap(userEmail);
        
        //variables for first donut chart
        double totalSustainableMiles = getSustainableMiles(transpToDist);
        double totalSustainableMilesGoal = 50.5;

	//FIXME: Not able to implement due to DepartureTime not being implemented correctly yet.
        //variables for second donut chart
        double totalSustainableHours = 7.5;
        double totalSustainableHoursGoal = 15.5;

        //variables for third donut chart
        String favMode = getMostUsedTransportationMode(transpToDist);
        double favModeMiles = transpToDist.get(favMode);

        //FIXME: Not able to implement due to Challenges not being implemented correctly yet.
        int totalChallengesWon = 1;
        int totalChallengesGoal = 5;

        ArrayList<Object> miniStats = new ArrayList<Object>();
        miniStats.add(new MilesChartData(totalSustainableMiles, totalSustainableMilesGoal));
        miniStats.add(new HoursChartData(totalSustainableHours, totalSustainableHoursGoal));
        miniStats.add(new FavModeChartData(favMode, favModeMiles));
        miniStats.add(new ChallengesChartData(totalChallengesWon, totalChallengesGoal));

        ArrayList<String> mini_stats_data = new ArrayList<String>();

        for(Object m : miniStats) {
            Gson gson = new Gson();
            String json = gson.toJson(m);
            mini_stats_data.add(json);
        }

        response.getWriter().println(mini_stats_data);
    }
}
