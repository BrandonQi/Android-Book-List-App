package edu.stevens.cs522.bookstore.contracts;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by Bin Qi on 2015/2/25.
 */

public class BookContract {
    public static final String ID = "_id";
    public static final String TITLE = "title";
    public static final String ISBN = "isbn";
    public static final String PRICE = "price";
    public static final String AUTHOR = "author";

    public static final String AUTHORITY = "edu.stevens.cs522.bookstore";

    public static final String BOOKS_CONTRACT = "books";
    public static final String AUTHORS_CONTRACT = "authors";

    public static final Uri BOOKS_URI = new Uri.Builder().scheme("content").authority(AUTHORITY).path(BOOKS_CONTRACT).build();
    public static final Uri AUTHORS_URI = new Uri.Builder().scheme("content").authority(AUTHORITY).path(AUTHORS_CONTRACT).build();

    public static void putTitle(ContentValues ctValue, String title) {
        ctValue.put(TITLE, title);
    }
    public static void putPrice(ContentValues ctValue, String price) {
        ctValue.put(PRICE, price);
    }
    public static void putISBN(ContentValues ctValue, String isbn) {
        ctValue.put(ISBN, isbn);
    }

    public static String getPrice(Cursor c) {
        return c.getString(c.getColumnIndexOrThrow(PRICE));
    }
    public static String getTitle(Cursor c) {
        int index = c.getColumnIndexOrThrow(TITLE);
        return c.getString(index);
    }
    public static String getISBN(Cursor c) {
        return c.getString(c.getColumnIndexOrThrow(ISBN));
    }
    public static String getAuthors(Cursor c) {
        return c.getString(c.getColumnIndexOrThrow(AUTHOR));
    }
}
