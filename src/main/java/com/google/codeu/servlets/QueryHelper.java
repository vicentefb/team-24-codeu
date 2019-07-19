package com.google.codeu.servlets;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

public class QueryHelper  {
  private String apiKey;
  private final String queryFormattedLink = "https://maps.googleapis.com/maps/api/directions/json?origin=%s&destination=%s&key=%s&units=metric";
  
  public QueryHelper(String key)  {
    apiKey = key;
  }

  public Map mapApiCall(String startAddress, String endAddress)  
      throws IOException  {
    String queryLink = String.format(
      queryFormattedLink, startAddress.replace(' ', '+'), endAddress.replace(' ', '+'), apiKey);
    Map result = (Map) new HashMap();
    try  {
      URL url = new URL(queryLink);
      HttpURLConnection cnxn = (HttpURLConnection) url.openConnection();
      cnxn.setRequestMethod("GET");
      result = jsonStringToMap(readOpenCnxn(cnxn));
      cnxn.disconnect();
    }
    catch (MalformedURLException error)  {
      System.out.println(queryLink);
      System.out.println("failed");
    }
    return result;
  }

  private String readOpenCnxn(HttpURLConnection cnxn) throws IOException	{
    BufferedReader in = new BufferedReader(new InputStreamReader(cnxn.getInputStream()));
    String inputLine;
    StringBuffer content = new StringBuffer();
    while ((inputLine = in.readLine()) != null)	{
	    content.append(inputLine);
    }
    in.close();
    return content.toString();
  }

  public Map<String, ?>  jsonStringToMap(String jsonString)	{
    Gson gson = new Gson();
    Map map = gson.fromJson(jsonString, Map.class);
    return map;
  }
}
