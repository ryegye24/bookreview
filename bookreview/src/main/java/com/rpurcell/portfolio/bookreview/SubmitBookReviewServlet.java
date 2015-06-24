package com.rpurcell.portfolio.bookreview;

import com.googlecode.objectify.ObjectifyService;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SubmitBookReviewServlet extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //TODO: Add use of UserService for authentication instead of trusting passed 'username' parameter.

        String username = req.getParameter("username");
        String ISBN = req.getParameter("isbn");
        String content = req.getParameter("content");

        //For now we'll just check if a username was provided for authentication
        if (username == null) {
            resp.sendError(resp.SC_FORBIDDEN);
        }
        if (ISBN == null || content == null) {
            resp.getWriter().write("Error: ISBN or review content not provided.");
            resp.sendError(resp.SC_PRECONDITION_FAILED);
            return;
        }
        resp.setStatus(resp.SC_OK);
        BookModel book = BookModel.getByISBN(ISBN);
        ReviewModel.CreateReview(book, content, username);
    }
}
