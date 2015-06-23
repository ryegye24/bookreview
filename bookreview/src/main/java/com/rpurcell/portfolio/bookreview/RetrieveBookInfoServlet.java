package com.rpurcell.portfolio.bookreview;
//http://stackoverflow.com/questions/6154845/returning-json-response-from-servlet-to-javascript-jsp-page
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RetrieveBookInfoServlet extends HttpServlet {
  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {
    if (req.getParameter("isbn") != null) {
      String ISBN = req.getParameter("isbn");

      try {
        JSONObject result = new JSONObject();
        JSONObject book = BookModel.getJSONByISBN(ISBN);
        JSONArray reviews = new JSONArray();
        List<ReviewModel> review_models = ReviewModel.getReviewsByISBN(ISBN);
        for (int i = 0; i < review_models.size(); i++) {
          JSONObject review = review_models.get(i).getJSON();
          reviews.put(review);
        }
        result.put("reviews", reviews);
        result.put("book", book);
        resp.getWriter().write(result.toString());
      } catch (JSONException jse) {
        resp.getWriter().write("Error JSONException" + jse);
      }
    } else {
      resp.getWriter().write("Error: ISBN not provided");
      resp.sendError(resp.SC_PRECONDITION_FAILED);
    }
  }
}
