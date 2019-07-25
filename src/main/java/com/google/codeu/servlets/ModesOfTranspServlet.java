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
import com.google.codeu.data.Route;
import com.google.codeu.data.Trip;
import com.google.codeu.servlets.DatastoreHelper;

/*
 * The purpose of this servlet is to retrieve the data needed for the modes of
 * transportation donut chart used on my-stats.html
 */
@WebServlet("/modesOfTransp")

public class ModesOfTranspServlet extends HttpServlet {
    Datastore datastore;

    @Override
    public void init()	{
        this.datastore = new Datastore();
    }

    private class TranspMode {
        private String name = "";
        private double miles = 0.0;

        private TranspMode(String name, double miles) {
            this.name = name;
            this.miles = miles;    
        }

        public String getName() {
            return name;
        }

        public double getMiles() {
            return miles;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setMiles(double miles) {
            this.miles = miles;
        }
    }


    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        //FLAG FOR MOCK DATA, TRUE = MOCK DATA, FALSE = ACTUAL IMPLEMENTATION
        boolean mock = true;

        ArrayList<TranspMode> modes = new ArrayList<TranspMode>();

        if(mock == false) {
            String userEmail = request.getRemoteUser();
            System.out.println("userEmail " + userEmail);

            DatastoreHelper helper = new DatastoreHelper(datastore);
            HashMap<String, Double> totalTransp = helper.userToSumDistanceTransportationMap(userEmail);

            for (String key : totalTransp.keySet())  {
                modes.add(new TranspMode(key, totalTransp.get(key)));
            }
        }

        else {
            //MOCK DATA
            modes.add(new TranspMode("Walk", 14.5));
            modes.add(new TranspMode("Bike", 16.7));
            modes.add(new TranspMode("Scooter", 10.33));
            modes.add(new TranspMode("Rollerblade", 5.27));
            modes.add(new TranspMode("Car (Single Rider)", 9.1));
            modes.add(new TranspMode("Car (Carpool)", 13.2));
        }

        ArrayList<String> modes_data = new ArrayList<String>();

        for(TranspMode m : modes) {
            Gson gson = new Gson();
            String json = gson.toJson(m);
            modes_data.add(json);
        }

        response.getWriter().println(modes_data);
    }
}
