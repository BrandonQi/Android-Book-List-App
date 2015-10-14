package edu.stevens.cs522.bookstore.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;

import edu.stevens.cs522.bookstore.contracts.BookContract;
import edu.stevens.cs522.bookstore.databases.CartDbAdapter;

/**
 * Created by BinQi on 2015/3/2.
 */

public class BookProvider extends ContentProvider {

    //create part
    CartDbAdapter cartDbAdapter;
    @Override
    public boolean onCreate() {
        Context context = getContext();
        cartDbAdapter = new CartDbAdapter(context);
        cartDbAdapter.open();
        return true;
    }

    //uri match part
    static final int ALLBOOKS = 10000;
    static final int ONEBOOK = 10001;
    static final int ALLAUTHORS = 10002;
    static final int ONEAUTHOR = 10003;

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(BookContract.AUTHORITY, BookContract.BOOKS_CONTRACT, ALLBOOKS);
        uriMatcher.addURI(BookContract.AUTHORITY, BookContract.BOOKS_CONTRACT + "/#", ONEBOOK);
        uriMatcher.addURI(BookContract.AUTHORITY, BookContract.AUTHORS_CONTRACT, ALLAUTHORS);
        uriMatcher.addURI(BookContract.AUTHORITY, BookContract.AUTHORS_CONTRACT + "/#", ONEAUTHOR);
    }

    //methods override part
    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case ALLBOOKS:{
                count = cartDbAdapter.deleteAll();
                break;
            }
            case ONEBOOK: {
                long rowId = Long.parseLong(uri.getPathSegments().get(1));
                count = cartDbAdapter.delete(rowId);
                break;
            }
            default: {
                throw new IllegalArgumentException("error: " + uri);
            }
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String where, String[] whereArgs, String sort) {
        Cursor cursor;
        switch (uriMatcher.match(uri)) {
            case ALLBOOKS: {
                cursor = cartDbAdapter.getAllBooks();
                break;
            }
            case ONEBOOK: {
                long ID = Long.parseLong(uri.getPathSegments().get(1));
                cursor = cartDbAdapter.getBook(ID);
                break;
            }
            default: {
                throw new IllegalArgumentException("error: " + uri);
            }
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues ctValue, String where, String[] whereArgs) {
        return 0;
    }

    @Override
    public Uri insert(Uri uri, ContentValues ctValue) {
        long ID = -1;
        switch (uriMatcher.match(uri)) {
            case ALLBOOKS: {
                ID = cartDbAdapter.addBook(ctValue);
                break;
            }
            case ALLAUTHORS: {
                ID = cartDbAdapter.addAuthor(ctValue);
                break;
            }
            default: {
                throw new IllegalArgumentException("error: " + uri);
            }
        }
        if (ID > 0) {
            Uri uri1 = ContentUris.withAppendedId(uri, ID);
            getContext().getContentResolver().notifyChange(uri1, null);
            return uri1;
        }
        throw new SQLException("error: " + uri);
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case ALLBOOKS:return "vnd.android.cursor.dir/vnd." + BookContract.AUTHORITY + "." + BookContract.BOOKS_CONTRACT;
            case ONEBOOK:return "vnd.android.cursor.item/vnd." + BookContract.AUTHORITY + "." + BookContract.BOOKS_CONTRACT;
            case ALLAUTHORS:return "vnd.android.cursor.dir/vnd." + BookContract.AUTHORITY + "." + BookContract.AUTHORS_CONTRACT;
            case ONEAUTHOR:return "vnd.android.cursor.item/vnd." + BookContract.AUTHORITY + "." + BookContract.AUTHORS_CONTRACT;
            default:throw new IllegalArgumentException("error: " + uri);
        }
    }
}
