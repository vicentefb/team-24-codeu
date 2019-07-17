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

import com.google.codeu.servlets.DatastoreHelper;

/*
 * The purpose of this servlet is to retrieve the data needed for the emissions bar graph
 * on my-stats.html and calculate the amount of CO2 in terms of kilograms
 */
@WebServlet("/emissions")

public class EmissionsServlet extends HttpServlet {
    private Datastore datastore;

    @Override
    public void init() {
        this.datastore = new Datastore();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
	String userEmail = request.getRemoteUser();
	DatastoreHelper helper = new DatastoreHelper(this.datastore);
	HashMap<String, Double> transpToDist = helper.userToSumDistanceTransportationMap(userEmail);
        double totalKilos = 0;
        double totalUnsustainableKilos = transpToDist.get("DRIVING");

	for (String key : transpToDist.keySet()) totalKilos += transpToDist.get(key);
	

	double totalMiles = totalKilos / 1.609;
	double totalUnsustainableMiles = totalUnsustainableKilos / 1.609;
        
        //the user's total unsustainable miles is multiplied by .404 to get amount of carbon in kg
        double actualEmissions = totalUnsustainableMiles * .404;
        Gson actual_gson = new Gson();
        String actual_json = actual_gson.toJson(actualEmissions);

        //the user's total miles is multiplied by .404 to get the hypothetical amount of carbon in kg
        double hypotheticalEmissions = totalMiles * .404;
        Gson hypothetical_gson = new Gson();
        String hypothetical_json = hypothetical_gson.toJson(hypotheticalEmissions);

        //list of all the data needed for the chart
        ArrayList<String> emissions_data = new ArrayList<String>();
        emissions_data.add(actual_json);
        emissions_data.add(hypothetical_json);

        //writes the list to the response
        response.getWriter().println(emissions_data);
    }
}
