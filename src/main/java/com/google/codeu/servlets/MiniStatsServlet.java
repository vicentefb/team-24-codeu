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
        
        //FLAG FOR MOCK DATA, TRUE = MOCK DATA, FALSE = ACTUAL IMPLEMENTATION
        boolean mock = true;

        double totalSustainableMiles;
        double totalSustainableMilesGoal;

        double totalSustainableHours;
        double totalSustainableHoursGoal;

        String favMode;
        double favModeMiles;

        int totalChallengesWon;
        int totalChallengesGoal;

        if(mock == false) {
            String userEmail = request.getRemoteUser();

            DatastoreHelper helper = new DatastoreHelper(this.datastore);
            HashMap<String, Double> transpToDist = helper.userToSumDistanceTransportationMap(userEmail);
            
            //variables for first donut chart
            totalSustainableMiles = getSustainableMiles(transpToDist);
            totalSustainableMilesGoal = 50.5;

            //FIXME: Not able to implement due to DepartureTime not being implemented correctly yet.
            //variables for second donut chart
            totalSustainableHours = 7.5;
            totalSustainableHoursGoal = 15.5;

            //variables for third donut chart
            favMode = getMostUsedTransportationMode(transpToDist);
            favModeMiles = 0;
            if (transpToDist.containsKey(favMode))	{
                transpToDist.get(favMode);
            }

            //FIXME: Not able to implement due to Challenges not being implemented correctly yet.
            totalChallengesWon = 1;
            totalChallengesGoal = 5;

        } 
        
        else {
            //MOCK DATA
            //variables for first donut chart
            totalSustainableMiles = 60;
            totalSustainableMilesGoal = 80;

            //variables for second donut chart
            totalSustainableHours = 7.5;
            totalSustainableHoursGoal = 15.5;

            //variables for third donut chart
            favMode = "Bike";
            favModeMiles = 16.7;

            //variables for fourth donut chart
            totalChallengesWon = 1;
            totalChallengesGoal = 5;
        }

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
