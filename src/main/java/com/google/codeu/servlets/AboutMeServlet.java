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
    } 
    else {
      ArrayList<String> userInfoJsonArray = new ArrayList<String>();
      
      String firstName = userData.getFirstName();
      Gson firstName_gson = new Gson();
      String firstName_json = firstName_gson.toJson(firstName);
      userInfoJsonArray.add(firstName_json);

      //FIXME making changes....
      /*
      if(userData.getFirstName() != null) {

      } else {
        userInfoJsonArray.add("");
      }
      */

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

    //all fields in the user profile form
    String firstName;
    String lastName;
    String city;
    String stateProvince;
    String country;
    String email = userService.getCurrentUser().getEmail();
    String aboutMe;

    //retrieves what is currently in datastore and what the new input is
    String currentFirstName = datastore.getUser(email).getFirstName();
    String newInputFirstName = request.getParameter("first-name");

    /*if the new input is different from what is stored AND the input isn't blank,
     * the new input is saved
     */ 
    if(!(currentFirstName.equals(newInputFirstName)) && !(newInputFirstName.equals(""))) {
      //there was new info entered, so it must be cleaned
      firstName = Jsoup.clean(request.getParameter("first-name"), Whitelist.none());
    } else {
      //no new info entered, keep what was already in datastore
      firstName = currentFirstName;
    }

    //retrieves what is currently in datastore and what the new input is
    String currentLastName = datastore.getUser(email).getLastName();
    String newInputLastName = request.getParameter("last-name");

    /*if the new input is different from what is stored AND the input isn't blank,
     * the new input is saved
     */ 
    if(!(currentLastName.equals(newInputLastName)) && !(newInputLastName.equals(""))) {
      //there was new info entered, so it must be cleaned
      lastName = Jsoup.clean(request.getParameter("last-name"), Whitelist.none());
    } else {
      //no new info entered, keep what was already in datastore
      lastName = currentLastName;
    }

    //retrieves what is currently in datastore and what the new input is
    String currentCity = datastore.getUser(email).getCity();
    String newInputCity = request.getParameter("city");

    /*if the new input is different from what is stored AND the input isn't blank,
     * the new input is saved
     */ 
    if(!(currentCity.equals(newInputCity)) && !(newInputCity.equals(""))) {
      //there was new info entered, so it must be cleaned
      city = Jsoup.clean(request.getParameter("city"), Whitelist.none());
    } else {
      //no new info entered, keep what was already in datastore
      city = currentCity;
    }

    //retrieves what is currently in datastore and what the new input is
    String currentStateProvince = datastore.getUser(email).getStateProvince();
    String newInputStateProvince = request.getParameter("state-province");

    /*if the new input is different from what is stored AND the input isn't blank,
     * the new input is saved
     */ 
    if(!(currentStateProvince.equals(newInputStateProvince)) && !(newInputStateProvince.equals(""))) {
      //there was new info entered, so it must be cleaned
      stateProvince = Jsoup.clean(request.getParameter("state-province"), Whitelist.none());
    } else {
      //no new info entered, keep what was already in datastore
      stateProvince = currentStateProvince;
    }

    //retrieves what is currently in datastore and what the new input is
    String currentCountry = datastore.getUser(email).getCountry();
    String newInputCountry = request.getParameter("country");

    /*if the new input is different from what is stored AND the input isn't blank,
     * the new input is saved
     */ 
    if(!(currentCountry.equals(newInputCountry)) && !(newInputCountry.equals(""))) {
      //there was new info entered, so it must be cleaned
      country = Jsoup.clean(request.getParameter("country"), Whitelist.none());
    } else {
      //no new info entered, keep what was already in datastore
      country = currentCountry;
    }

    //retrieves what is currently in datastore and what the new input is
    String currentAboutMe = datastore.getUser(email).getAboutMe();
    String newInputAboutMe = request.getParameter("about-me");

    /*if the new input is different from what is stored AND the input isn't blank,
     * the new input is saved
     */ 
    if(!(currentAboutMe.equals(newInputAboutMe)) && !(newInputAboutMe.equals(""))) {
      //there was new info entered, so it must be cleaned
      aboutMe = Jsoup.clean(request.getParameter("about-me"), Whitelist.none());
    } else {
      //no new info entered, keep what was already in datastore
      aboutMe = currentAboutMe;
    }
    
    //updates User values
    User user = new User(firstName, lastName, city, stateProvince, country, email, aboutMe);
    datastore.storeUser(user);

    response.sendRedirect("/user-page.html?user=" + email);
  }
}
