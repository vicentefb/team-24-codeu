/*
 * Copyright 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.codeu.data;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.FetchOptions;

import java.util.*;


  private DatastoreService datastore;

  public Datastore() {
    datastore = DatastoreServiceFactory.getDatastoreService();
  }

  /** Stores the User in Datastore. */
  public void storeUser(User user) {
    Entity userEntity = new Entity("User", user.getEmail());
    userEntity.setProperty("email", user.getEmail());
    userEntity.setProperty("aboutMe", user.getAboutMe());
    datastore.put(userEntity);
  }

  /**
  * Returns the User owned by the email address, or
  * null if no matching User was found.
  */
  public User getUser(String email) {
    Query query = new Query("User")
      .setFilter(new Query.FilterPredicate("email", FilterOperator.EQUAL, email));
	PreparedQuery results = datastore.prepare(query);
    Entity userEntity = results.asSingleEntity();
    
	if(userEntity == null) {
      return null;
    }

    String aboutMe = (String) userEntity.getProperty("aboutMe");
    User user = new User(email, aboutMe);

    return user;
  }

  /**
   * @return all of the users currently stored.
   */
  public Set<String> getUsers(){
    Set<String> users = new HashSet<>();
    Query query = new Query("Message");
    PreparedQuery results = datastore.prepare(query);

    for(Entity entity : results.asIterable()) {
      users.add((String) entity.getProperty("user"));
    }

    return users;
  }

  /** Stores the Message in Datastore. */
  public void storeMessage(Message message) {
    Entity messageEntity = new Entity("Message", message.getId().toString());
    messageEntity.setProperty("user", message.getUser());
    messageEntity.setProperty("text", message.getText());
    messageEntity.setProperty("timestamp", message.getTimestamp());

    datastore.put(messageEntity);
  }

  /**
   * Given an entity, creates its corresponding message object.
   *
   * @return the corresponding message object.
   */
  private Message entityToMessage(Entity entity)  {
    String idString = entity.getKey().getName();
    UUID id = UUID.fromString(idString);
    String text = (String) entity.getProperty("text");
    String user = (String) entity.getProperty("user"); 
    long timestamp = (long) entity.getProperty("timestamp");
    
    return new Message(id, user, text, timestamp);
}

  /**
   * Given a prepared query, obtains the corresponding message list.
   *
   * @return the correpsonding message list.
   */
  private List<Message> getMessageList(PreparedQuery results)	{ 
    List<Message> messages = new ArrayList<>();
    for (Entity entity : results.asIterable()) {
      try {
      	Message message = entityToMessage(entity);
      	messages.add(message);
      } catch (Exception e) {
      	System.err.println("Error reading message.");
      	System.err.println(entity.toString());
      	e.printStackTrace();
        }
    }
    return messages;
}

  /**
   * Gets messages posted by a specific user.
   *
   * @return a list of messages posted by the user, or empty list if user has never posted a
   *     message. List is sorted by time descending.
   */
  public List<Message> getMessages(String user) {

    Query query =
        new Query("Message")
            .setFilter(new Query.FilterPredicate("user", FilterOperator.EQUAL, user))
            .addSort("timestamp", SortDirection.DESCENDING);
    PreparedQuery results = datastore.prepare(query);
    return getMessageList(results);
  }

  /**
   * Gets messages posted by all users.
   *
   * @return a list of messages posted by every user, or an empty list if user has never posted
   *     a message. List is sorted by time descending.
   */
  public List<Message> getAllMessages(){
    List<Message> messages = new ArrayList<>();
    Query query = new Query("Message").addSort("timestamp", SortDirection.DESCENDING);
    PreparedQuery results = datastore.prepare(query);
    
    return getMessageList(results);
  }

  /** Returns the total number of messages for all users. */
  public int getTotalMessageCount(){
    Query query = new Query("Message");
    PreparedQuery results = datastore.prepare(query);
    
    return results.countEntities(FetchOptions.Builder.withLimit(1000));
  }

  /**
   * Given an entity, create and @return its corresponding route object.
   */
  private Route entityToRoute(Entity entity)  {
    String idString = entity.getKey().getName();
    UUID id = UUID.fromString(idString);
    List<String> addressList = (List<String>) entity.getProperty("addressList");
    float distanceTravelled = (float) entity.getProperty("distanceTravelled");
    float departureTime = (float) entity.getProperty("departureTime");
    float timeSpent = (float) entity.getProperty("timeSpent");
    
    return new Route(addressList, distanceTravelled, departureTime, timeSpent);
  }

  /**
   * Given a prepared query, obtains the corresponding route list.
   *
   * @return the correpsonding route list.
   */
  private List<Route> getRouteList(PreparedQuery results)  {
	  List<Route> routes = new ArrayList<>();
	  for (Entity entity : results.asIterable())  {
		  try  {
			  Route route = enttiyToRoute(entity);
			  routes.add(route);
		  } catch (Exception e)  {
			  System.err.println("Error reading route.");
			  System.err.println(entity.toString());
			  e.printStackTrace();
		  }
	  }
	  return routes;
  }

  /** 
   * @return all of the routes currently in the datastore. List is sorted by departure time, descending.
   */
  public List<Route> getAllRoutes()	{
    Query query = new Query("Route").addSort("departureTime", SortDirection.DESCENDING);
	PreparedQuery results = datastore.prepare(query);
	return getRouteList(routes);
  }

  /**
   * Gets all routes used by a specific user.
   */
  public List<Route> getRoutes(String user)	{
	  Query query = 
		  new Query("Route")
		    .setFilter(new Query.FilterPredicate("user", FilterOperator.EQUAL, user))
			.addSort("departureTime", SortDirection.DESCENDING);
	  PreparedQuery results = datastore.prepare(query);
	  return getRouteList(results);
}
