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

public class SubmitBookInfoServlet extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        //I'll need to put this back in once I've got it working with no security
        //UserService userService = UserServiceFactory.getUserService();
        //User user = userService.getCurrentUser();

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
            BookModel bookModel = new BookModel(ISBN, title, author, description);
            ObjectifyService.ofy().save().entity(bookModel).now();
        }
        else {
            BookModel bookModel = new BookModel(ISBN, title, author);
            ObjectifyService.ofy().save().entity(bookModel).now();
        }
    }

}
