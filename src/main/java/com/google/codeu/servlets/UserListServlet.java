package com.google.codeu.servlets;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.codeu.data.Datastore;
import java.util.Set;
import java.util.Iterator;
/**
 * Handles fetching all users for the community page.
 */

@WebServlet("/user-list")
public class UserListServlet extends HttpServlet{
    private Datastore datastore;
    @Override
    public void init() {
        datastore = new Datastore();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.getOutputStream().println("This will be my user list:");
        //Calling getUsers() method and storing the set into users set variable
        Set<String> users = datastore.getUsers();
        //Checking if the user list is empty
        if(users.isEmpty()) {
            response.getOutputStream().println("The user list size is 0.");
        }
        else {
            Iterator<String> it = users.iterator();
            response.getOutputStream().println("The user list size is: " + users.size() + ".");
            //Printing all the users in the list
            while (it.hasNext()) {
                response.getOutputStream().println(it.next());
            }
        }
    }
}
