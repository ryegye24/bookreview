package com.rpurcell.portfolio.bookreview;

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
  @Id public String isbn;
  @Index public String title;
  @Index public String author;
  public String description;

  public BookModel() {
    this.isbn = "-1";
  }

  public BookModel(String isbn) {
    this();
    this.isbn = isbn;
  }

  public BookModel(String isbn, String title) {
    this(isbn);
    this.title = title;
  }

  public BookModel(String isbn, String title, String author) {
    this(isbn, title);
    this.author = author;

  }

  public BookModel(String isbn, String title, String author, String description) {
    this(isbn, title, author);
    this.description = description;
  }

  public Key<BookModel> getKey() {
    return Key.create(BookModel.class, this.isbn);
  }

  public JSONObject getJSON() {
    try {
      JSONObject bookJson = new JSONObject();
      bookJson.put("ISBN", this.isbn);
      bookJson.put("title", this.title);
      if (this.description == null) {
        this.description = "";
      }
      bookJson.put("description", this.description);
      return bookJson;
    }
    catch (JSONException jse) {
      return null;
    }
  }

  public static BookModel getByISBN(String isbn) {
    BookModel book = ObjectifyService.ofy().load().type(BookModel.class).id(isbn).now();
    return book;
  }

  public static JSONObject getJSONByISBN(String isbn) {
    BookModel book = getByISBN(isbn);
    if (book == null) {
      return null;
    }
    else {
      return book.getJSON();
    }
  }
}
