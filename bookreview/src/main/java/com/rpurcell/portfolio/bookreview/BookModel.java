package com.rpurcell.portfolio.bookreview;

import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;



/**
 * The @Entity tells Objectify about our entity.  We also register it in
 * ObjectifyHelper.java -- very important.
 *
 * This is never actually created, but gives a hint to Objectify about our Ancestor key.
 */
@Entity
public class BookModel {
  @Id public String ISBN;
  @Index public String title;
  public String description;
  private static final Logger log = Logger.getLogger(BookModel.class.getName());

  public BookModel() {
    log.info("<div>Inside the BookModel most basic constructor</div>");
    this.ISBN = "-1";
    log.info("<div>Managed to set a default val for the ISBN</div>");
  }
  
  public BookModel(String bookISBN) {
    this();
    this.ISBN = bookISBN;
  }
  
  public BookModel(String bookISBN, String bookTitle) {
    this(bookISBN);
    this.title = bookTitle;
  }
  
  public static BookModel getByISBN(String ISBN) {
    BookModel book = ObjectifyService.ofy().load().type(BookModel.class).id(ISBN).now();
    if (book == null) {
      book = new BookModel(ISBN);
      ObjectifyService.ofy().save().entity(book).now();
    }
    return book;
  }
  
  public Key<BookModel> getKey() {
    return Key.create(BookModel.class, this.ISBN);
  }
  
  public static JSONObject getJSONByISBN(String ISBN) {
    BookModel book_model = getByISBN(ISBN);
    if (book_model == null) {
      book_model = new BookModel(ISBN);
    }
    try {
      JSONObject book_json = new JSONObject();
      book_json.put("ISBN", ISBN);
      if (book_model.title == null) {
        book_model.title = "NOTHING AT ALL!!!";
      }
      book_json.put("title", book_model.title);
      if (book_model.description == null) {
        book_model.description = "NOTHING AT ALL ALSO!!!";
      }
      book_json.put("description", book_model.description);
      ObjectifyService.ofy().save().entity(book_model).now();
      return book_json;
    }
    catch (JSONException jse) {
      return null;
    }
  }
}
