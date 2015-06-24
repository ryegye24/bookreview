package com.rpurcell.portfolio.bookreview;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;

/**
 * OfyHelper, a ServletContextListener, is setup in web.xml to run before a JSP is run.  This is
 * required to let JSP's access Ofy.
 **/
public class ObjectifyHelper implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        // This will be invoked as part of a warmup request, or the first user
        // request if no warmup request was invoked.
        ObjectifyService.register(BookModel.class);
        ObjectifyService.register(ReviewModel.class);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        // App Engine does not currently invoke this method.
    }
}
