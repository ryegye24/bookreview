package com.rpurcell.portfolio.bookreview;

import com.googlecode.objectify.ObjectifyService;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SubmitBookInfoServlet extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //TODO: Add use of UserService for authentication.

        String title = req.getParameter("title");
        String ISBN = req.getParameter("isbn");
        String author = req.getParameter("author");
        if (title == null || ISBN == null || author == null) {
            resp.getWriter().write("Error: Book title, author, or ISBN not provided");
            resp.sendError(resp.SC_PRECONDITION_FAILED);
            return;
        }
        String description = req.getParameter("description");
        resp.setStatus(resp.SC_OK);
        if (description != null) {
            BookModel.CreateBook(ISBN, title, author, description);
        }
        else {
            BookModel.CreateBook(ISBN, title, author, "");
        }
    }

}
