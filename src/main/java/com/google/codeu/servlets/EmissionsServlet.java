package com.google.codeu.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import com.google.gson.Gson;
import java.util.ArrayList;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.codeu.data.Datastore;
import com.google.gson.JsonObject;

@WebServlet("/emissions")

public class EmissionsServlet extends HttpServlet {
    
    private Datastore datastore;

    @Override
    public void init() {
        datastore = new Datastore();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
   
        //the user's total unsustainable miles is multiplied by .404 to get amount of carbon in kg
        double actualEmissions = datastore.getTotalUnsustainableMiles() * .404;
        Gson actual_gson = new Gson();
        String actual_json = actual_gson.toJson(actualEmissions);

        //the user's total miles is multiplied by .404 to get the hypothetical amount of carbon in kg
        double hypotheticalEmissions = datastore.getTotalMiles() * .404;
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