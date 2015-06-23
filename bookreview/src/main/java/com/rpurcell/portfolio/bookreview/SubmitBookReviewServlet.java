package com.rpurcell.portfolio.bookreview;



import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.ObjectifyService;

import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SubmitBookReviewServlet extends HttpServlet {
  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    ReviewModel review;

    //I'll need to put this back in once I've got it working with no security
    //UserService userService = UserServiceFactory.getUserService();
    //User user = userService.getCurrentUser();
    String username = req.getParameter("username");
    String ISBN = req.getParameter("isbn");
    String content = req.getParameter("content");
    if (username != null) {
      resp.setStatus(resp.SC_OK);
      BookModel bookModel = BookModel.getByISBN(ISBN);
      ReviewModel revModel = new ReviewModel(ISBN, content, username);
      ObjectifyService.ofy().save().entity(revModel).now();
    } else {
      //Hmm idk if i can use the UserService with the app's POST request, but this'll need to return an error
      resp.sendError(resp.SC_FORBIDDEN);
    }
    // Use Objectify to save the greeting and now() is used to make the call synchronously as we
    // will immediately get a new page using redirect and we want the data to be present.
    //ObjectifyService.ofy().save().entity(review).now();*/
  }
}
