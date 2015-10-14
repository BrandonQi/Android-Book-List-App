package edu.stevens.cs522.bookstore.contracts;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by Bin Qi on 2015/2/26.
 */

public class AuthorContract {
    public static final String ID = "_id";
    public static final String BOOKS = "books_fk";
    public static final String NAME = "fullname";

    public static void setBookID(ContentValues ctValue, long bookID) {
        ctValue.put(BOOKS, bookID);
    }
    public static void setName(ContentValues ctValue, String name) {
        ctValue.put(NAME, name);
    }
}
