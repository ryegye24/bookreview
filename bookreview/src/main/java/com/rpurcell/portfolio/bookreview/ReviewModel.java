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
 *
 * We add a @Parent to tell the object about its ancestor. We are doing this to support many
 * guestbooks.  Objectify, unlike the AppEngine library requires that you specify the fields you
 * want to index using @Index.  This is often a huge win in performance -- though if you don't Index
 * your data from the start, you'll have to go back and index it later.
 *
 * NOTE - all the properties are PUBLIC so that can keep this simple, otherwise,
 * Jackson, wants us to write a BeanSerializaer for cloud endpoints.
 **/
@Entity
public class ReviewModel {
  private static String isbn;
  @Parent Key<BookModel> theBook;
  @Id public Long id;

  @Index public String author_email;
  public String content;
  @Index public Date date;

  /**
   * Simple constructor just sets the date
   **/
  public ReviewModel() {
    date = new Date();
  }

  /**
   * A connivence constructor
   **/
  public ReviewModel(String bookISBN, String content) {
    this();
    if( bookISBN != null ) {
      theBook = Key.create(BookModel.class, bookISBN);  // Creating the Ancestor key
    } else {
      theBook = Key.create(BookModel.class, "default");
    }
    this.content = content;
  }

  /**
   * Takes all important fields
   **/
  public ReviewModel(String bookISBN, String content, String email) {
    this(bookISBN, content);
    author_email = email;
  }
  
  public static List<ReviewModel> getReviewsByISBN(String ISBN) {
    Key<BookModel> theBook = BookModel.getByISBN(ISBN).getKey();
    List<ReviewModel> reviewModels = ObjectifyService.ofy().load().type(ReviewModel.class).ancestor(theBook).order("-date").list();
    return reviewModels;
  }
  
  public String getAuthorEmail() {
    return author_email;
  }
  
  public String getReviewText() {
    return content;
  }
  
  public Date getDate() {
    return date;
  }
  
  public JSONObject getJSONReview() {
    try {
      JSONObject json = new JSONObject();
      json.put("ISBN", isbn);
      json.put("id", id);
      DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      String dateString = dateFormat.format(date);
      json.put("date", dateString);
      json.put("author", author_email);
      json.put("content", content);
      return json;
    }
    catch (JSONException jse) {
      return null;
    }
  }

}
