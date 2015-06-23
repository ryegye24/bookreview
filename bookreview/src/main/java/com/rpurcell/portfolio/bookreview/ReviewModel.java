package com.rpurcell.portfolio.bookreview;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.lang.String;
import java.util.Date;
import java.util.List;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * The @Entity tells Objectify about our entity.  We also register it in OfyHelper.java -- very
 * important. Our primary key @Id is set automatically by the Google Datastore for us.
 * <p/>
 * We add a @Parent to tell the object about its ancestor. We are doing this to support many
 * guestbooks.  Objectify, unlike the AppEngine library requires that you specify the fields you
 * want to index using @Index.  This is often a huge win in performance -- though if you don't Index
 * your data from the start, you'll have to go back and index it later.
 * <p/>
 * NOTE - all the properties are PUBLIC so that can keep this simple, otherwise,
 * Jackson, wants us to write a BeanSerializaer for cloud endpoints.
 **/
@Entity
public class ReviewModel {
    private static String isbn;
    @Parent
    Key<BookModel> theBook;
    @Id
    public Long id;

    @Index
    public String authorEmail;
    public String content;
    @Index
    public Date date;

    /**
     * Simple constructor just sets the date
     **/
    public ReviewModel() {
        this.date = new Date();
    }

    public ReviewModel(BookModel book, String content) {
        this();
        Key<BookModel> parent = book.getKey();
        if (parent == null) {
            parent = Key.create(BookModel.class, isbn);
        }
        this.theBook = parent;
        this.content = content;
    }
    /**
     * A connivence constructor
     **/
    public ReviewModel(String isbn, String content) {
        this(BookModel.getByISBN(isbn), content);
    }

    /**
     * Takes all important fields
     **/
    public ReviewModel(String isbn, String content, String email) {
        this(isbn, content);
        this.authorEmail = email;
    }

    public String getAuthorEmail() {
        return this.authorEmail;
    }

    public String getReviewText() {
        return this.content;
    }

    public Date getDate() {
        return this.date;
    }

    public JSONObject getJSON() {
        try {
            JSONObject review = new JSONObject();
            review.put("ISBN", this.isbn);
            review.put("id", this.id);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = dateFormat.format(this.date);
            review.put("date", dateString);
            review.put("author", this.authorEmail);
            review.put("content", this.content);
            return review;
        } catch (JSONException jse) {
            return null;
        }
    }

    public static List<ReviewModel> getReviewsByISBN(String isbn) {
        Key<BookModel> book = BookModel.getByISBN(isbn).getKey();
        List<ReviewModel> reviewModels = ObjectifyService.ofy().load().type(ReviewModel.class).ancestor(book).order("-date").list();
        return reviewModels;
    }

    public static List<ReviewModel> getReviewsByAuthor(String authorEmail) {
        List<ReviewModel> reviewModels = ObjectifyService.ofy().load().type(ReviewModel.class).filter("authorEmail", authorEmail).order("-date").list();
        return reviewModels;
    }

}
