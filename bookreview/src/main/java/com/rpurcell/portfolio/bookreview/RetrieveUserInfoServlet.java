package com.rpurcell.portfolio.bookreview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RetrieveUserInfoServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        //TODO: Use UserService to get username, possibly create UserModel to store additional information.

        String username = req.getParameter("username");
        if (username == null) {
            resp.getWriter().write("Error: No username provided.");
            resp.sendError(resp.SC_EXPECTATION_FAILED );
            return;
        }

        JSONObject user = new JSONObject();
        List<ReviewModel> reviewModels = ReviewModel.getReviewsByAuthor(username);
        JSONArray reviews = new JSONArray();

        try {
            user.put("username", username);
            for (int i = 0; i < reviewModels.size(); i++) {
                reviews.put(reviewModels.get(i).getJSON());
            }
            user.put("reviews", reviews);
        } catch (JSONException jse) {
            resp.getWriter().write("Error JSONException" + jse);
            resp.sendError(resp.SC_INTERNAL_SERVER_ERROR);
        }

    }
}
