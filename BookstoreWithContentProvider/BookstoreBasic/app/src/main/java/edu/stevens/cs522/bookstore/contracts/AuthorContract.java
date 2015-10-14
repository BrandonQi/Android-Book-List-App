package edu.stevens.cs522.bookstore.contracts;

import android.content.ContentValues;

/**
 * Created by BinQi on 2015/2/20.
 */

public class AuthorContract {
    public static final String ID = "_id";
    public static final String BOOK = "books_fk";
    public static final String NAME = "fullname";

    public static void putBooksFK(ContentValues ctValue, long book) {
        ctValue.put(BOOK, book);
    }
    public static void putFullName(ContentValues ctValue, String name) {
        ctValue.put(NAME, name);
    }
}
