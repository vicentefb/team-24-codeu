package com.google.codeu.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.codeu.data.Datastore;
import com.google.gson.Gson;
import java.util.ArrayList;

/*
 * The purpose of this servlet is to retrieve the data needed for the modes of
 * transportation donut chart used on my-stats.html
 */
@WebServlet("/modesOfTransp")

public class ModesOfTranspServlet extends HttpServlet {

    @Override
    public void init() {
        //datastore = new Datastore(); //implement when using Datastore
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

        //FIXME: replace the following variables with real data from datastore
        ArrayList<TranspMode> modes = new ArrayList<TranspMode>();
        modes.add(new TranspMode("Walk", 14.5));
        modes.add(new TranspMode("Bike", 16.7));
        modes.add(new TranspMode("Scooter", 10.45));
        modes.add(new TranspMode("Rollerblade", 5.27));
        modes.add(new TranspMode("Car (Single Rider)", 9.1));
        modes.add(new TranspMode("Car (Carpool)", 13.2));

        ArrayList<String> modes_data = new ArrayList<String>();

        for(TranspMode m : modes) {
            Gson gson = new Gson();
            String json = gson.toJson(m);
            modes_data.add(json);
        }

        response.getWriter().println(modes_data);
    }
}