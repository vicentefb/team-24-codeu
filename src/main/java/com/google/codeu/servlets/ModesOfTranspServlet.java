package com.google.codeu.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.codeu.data.Datastore;
import com.google.gson.Gson;
import java.util.ArrayList;

@WebServlet("/modesOfTransp")

public class ModesOfTranspServlet extends HttpServlet {
    
    private Datastore datastore;

    @Override
    public void init() {
        datastore = new Datastore();
    }

    public class TranspMode {
        String name = "";
        double miles = 0.0;

        public TranspMode(String name, double miles) {
            this.name = name;
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