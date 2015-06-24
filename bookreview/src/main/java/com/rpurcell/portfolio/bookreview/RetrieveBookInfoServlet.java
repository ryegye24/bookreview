package com.rpurcell.portfolio.bookreview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RetrieveBookInfoServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        if (req.getParameter("isbn") != null) {
            String ISBN = req.getParameter("isbn");
            JSONObject result = new JSONObject();
            JSONObject book = BookModel.getJSONByISBN(ISBN);

            if (book == null) {
                resp.getWriter().write("Error: No book found for provided ISBN.");
                resp.sendError(resp.SC_EXPECTATION_FAILED );
                return;
            }

            JSONArray reviews = new JSONArray();
            List<ReviewModel> review_models = ReviewModel.getReviewsByISBN(ISBN);
            try {
                for (int i = 0; i < review_models.size(); i++) {
                    JSONObject review = review_models.get(i).getJSON();
                    reviews.put(review);
                }
                result.put("reviews", reviews);
                result.put("book", book);

            } catch (JSONException jse) {
                resp.getWriter().write("Error JSONException" + jse);
                resp.sendError(resp.SC_INTERNAL_SERVER_ERROR);
            }
            resp.getWriter().write(result.toString());

        } else {
            resp.getWriter().write("Error: ISBN not provided");
            resp.sendError(resp.SC_PRECONDITION_FAILED);
        }
    }
}
