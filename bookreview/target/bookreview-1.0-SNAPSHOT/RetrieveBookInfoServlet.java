package com.rpurcell.portfolio.bookreview;
//http://stackoverflow.com/questions/6154845/returning-json-response-from-servlet-to-javascript-jsp-page
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import com.googlecode.objectify.Key
import com.googlecode.objectify.ObjectifyService

import org.json.JSONArray; 
import org.json.JSONObject;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RetrieveBookInfoServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {
    if (req.getParameter("bookISBN")) {
      ISBN = req.getParameter("bookISBN");
      JSONObject result = new JSONObject();
      JSONObject book = BookModel.getJSONByISBN(ISBN);
      JSONArray reviews = new JSONArray();
      List<ReviewModel> review_models = ReviewModel.getReviewsByISBN(ISBN);
      for (int i =0; i < review_models.size(); i++) {
        JSONObject review = review_models[i].getJSONReview();
        reviews.add(review);
      }
      result.put("reviews", reviews);
      result.put("book", book);
      resp.getWriter().write(result.toString());
    }
      
    if (req.getParameter("testing") == null) {
      resp.setContentType("application/json"); 
      resp.getWriter().println("Hello, this is a testing servlet. \n\n");
      Properties p = System.getProperties();
      p.list(resp.getWriter());

    } else {
      UserService userService = UserServiceFactory.getUserService();
      User currentUser = userService.getCurrentUser();

      if (currentUser != null) {
        resp.setContentType("application/json");
        resp.getWriter().println("Hello, " + currentUser.getNickname());
      } else {
        resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));
      }
    }
  }
}
