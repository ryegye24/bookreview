package com.rpurcell.portfolio.bookreview;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

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
 * We add a @Parent to tell the object about its ancestor. We are doing this to support reviews for many
 * books.  Objectify, unlike the AppEngine library requires that you specify the fields you
 * want to index using @Index.  This is often a huge win in performance -- though if you don't Index
 * your data from the start, you'll have to go back and index it later.
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

    public ReviewModel() {
        this.date = new Date();
    }

    public ReviewModel(BookModel book, String content, String email) {
        this();
        Key<BookModel> parent = book.getKey();
        if (parent == null) {
            parent = Key.create(BookModel.class, isbn);
        }
        this.theBook = parent;
        this.content = content;
        this.authorEmail = email;
    }

    /**
     * Takes all important fields
     **/
    public ReviewModel(String isbn, String content, String email) {
        this(BookModel.getByISBN(isbn), content, email);
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

    //These static methods are used to create ReviewModels so clients don't need to manually make ObjectifyService calls.
    public static ReviewModel CreateReview(String isbn, String content, String email) {
        ReviewModel review = new ReviewModel(isbn, content, email);
        ObjectifyService.ofy().save().entity(review).now();
        return review;
    }

    public static ReviewModel CreateReview(BookModel book, String content, String email) {
        ReviewModel review = new ReviewModel(book, content, email);
        ObjectifyService.ofy().save().entity(review).now();
        return review;
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
