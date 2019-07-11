package com.google.codeu.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.codeu.data.Datastore;
import com.google.codeu.data.User;
import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;


/**
 * Handles fetching and saving user data.
 */
@WebServlet("/about")
public class AboutMeServlet extends HttpServlet {
  
  private Datastore datastore;
  
  @Override
  public void init() {
    datastore = new Datastore();
  }

  /**
   * Responds with the "about me" section for a particular user.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    response.setContentType("application/json");

    String user = request.getParameter("user");

    if(user == null || user.equals("")) {
      // Request is invalid, return empty response
      return;
    }

    User userData = datastore.getUser(user);
    if(userData == null) {
      return;
    } else {
      ArrayList<String> userInfoJsonArray = new ArrayList<String>();
      
      if(userData.getFirstName() != null) {
        String firstName = userData.getFirstName();
        Gson firstName_gson = new Gson();
        String firstName_json = firstName_gson.toJson(firstName);
        userInfoJsonArray.add(firstName_json);

      } else {
        userInfoJsonArray.add("");
      }

      if(userData.getLastName() != null) {
        String lastName = userData.getLastName();
        Gson lastName_gson = new Gson();
        String lastName_json = lastName_gson.toJson(lastName);
        userInfoJsonArray.add(lastName_json);

      } else {
        userInfoJsonArray.add("");
      }

      if(userData.getCity() != null) {
        String city = userData.getCity();
        Gson city_gson = new Gson();
        String city_json = city_gson.toJson(city);
        userInfoJsonArray.add(city_json);

      } else {
        userInfoJsonArray.add("");
      }

      if(userData.getStateProvince() != null) {
        String stateProvince = userData.getStateProvince();
        Gson stateProvince_gson = new Gson();
        String stateProvince_json = stateProvince_gson.toJson(stateProvince);
        userInfoJsonArray.add(stateProvince_json);

      } else {
        userInfoJsonArray.add("");
      }

      if(userData.getCountry() != null) {
        String country = userData.getCountry();
        Gson country_gson = new Gson();
        String country_json = country_gson.toJson(country);
        userInfoJsonArray.add(country_json);

      } else {
        userInfoJsonArray.add("");
      }

      if(userData.getEmail() != null) {
        String email = userData.getEmail();
        Gson email_gson = new Gson();
        String email_json = email_gson.toJson(email);
        userInfoJsonArray.add(email_json);

      } else {
        userInfoJsonArray.add("");
      }

      if(userData.getAboutMe() != null) {
        String aboutMe = userData.getAboutMe();
        Gson aboutMe_gson = new Gson();
        String aboutMe_json = aboutMe_gson.toJson(aboutMe);
        userInfoJsonArray.add(aboutMe_json);

      } else {
        userInfoJsonArray.add("");
      }
      
      response.getWriter().println(userInfoJsonArray);
    }
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    UserService userService = UserServiceFactory.getUserService();
    if (!userService.isUserLoggedIn()) {
      response.sendRedirect("/index.html");
      return;
    }

    String firstName;
    String lastName;
    String city;
    String stateProvince;
    String country;
    String email;
    String aboutMe;

    if(request.getParameter("first-name") != null) {
      firstName = Jsoup.clean(request.getParameter("first-name"), Whitelist.none());
    } else {
      firstName = "";
    }

    if(request.getParameter("last-name") != null) {
      lastName = Jsoup.clean(request.getParameter("last-name"), Whitelist.none());
    } else {
      lastName = "";
    }

    if(request.getParameter("city") != null) {
      city = Jsoup.clean(request.getParameter("city"), Whitelist.none());
    } else {
      city = "";
    }

    if(request.getParameter("state-province") != null) {
      stateProvince = Jsoup.clean(request.getParameter("state-province"), Whitelist.none());
    } else {
      stateProvince = "";
    }

    if(request.getParameter("country") != null) {
      country = Jsoup.clean(request.getParameter("country"), Whitelist.none());
    } else {
      country = "";
    }

    email = userService.getCurrentUser().getEmail();

    if(request.getParameter("about-me") != null) {
      aboutMe = Jsoup.clean(request.getParameter("about-me"), Whitelist.none());
    } else {
      aboutMe = "";
    }
    
    User user = new User(firstName, lastName, city, stateProvince, country, email, aboutMe);
    datastore.storeUser(user);

    response.sendRedirect("/user-page.html?user=" + email);
  }
}
