package edu.stevens.cs522.bookstore.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import edu.stevens.cs522.bookstore.contracts.AuthorContract;
import edu.stevens.cs522.bookstore.contracts.BookContract;

/**
 * Created by BinQi on 2015/2/15.
 */

public class CartDbAdapter {
    static final String DATABASE_NAME = "database.bookstore";
    static final String DATABASE_TABLE_BOOK = "books";
    static final String DATABASE_TABLE_AUTHOR = "authors";
    static final int DATABASE_VERSION = 1;
    static final String TAG = CartDbAdapter.class.getCanonicalName();
    //SQL part
    static final String CREATE_BOOK = "CREATE TABLE " +
            DATABASE_TABLE_BOOK + " (" +
            BookContract.ID + " INTEGER PRIMARY KEY, " +
            BookContract.TITLE + " TEXT NOT NULL, " +
            BookContract.ISBN + " TEXT NOT NULL, " +
            BookContract.PRICE + " TEXT NOT NULL" + ");";
    static final String CREATE_AUTHOR = "CREATE TABLE " +
            DATABASE_TABLE_AUTHOR + " (" +
            AuthorContract.ID + " INTEGER PRIMARY KEY, " +
            AuthorContract.NAME + " TEXT NOT NULL, " +
            AuthorContract.BOOK + " INTEGER, "
            + "FOREIGN KEY (" + AuthorContract.BOOK + ") REFERENCES " +
            DATABASE_TABLE_BOOK + "(" +
            BookContract.ID + ") ON DELETE CASCADE"+ ");";
    static final String CREATE_INDEX = "CREATE INDEX IF NOT EXISTS " +
            DATABASE_TABLE_AUTHOR + DATABASE_TABLE_BOOK + " ON " +
            DATABASE_TABLE_AUTHOR + "(" + AuthorContract.BOOK + ");";
    static final String GET_ALLBOOK = "SELECT " +
            DATABASE_TABLE_BOOK + "." + BookContract.ID + ", " +
            BookContract.TITLE + ", " +
            BookContract.PRICE + ", " +
            BookContract.ISBN + ", " + "GROUP_CONCAT(" + AuthorContract.NAME + ", '|') as " +
            BookContract.AUTHOR + " FROM " +
            DATABASE_TABLE_BOOK + " LEFT OUTER JOIN " + DATABASE_TABLE_AUTHOR + " ON " +
            DATABASE_TABLE_BOOK + "." + BookContract.ID + "=" +
            DATABASE_TABLE_AUTHOR + "." + AuthorContract.BOOK + " GROUP BY " + DATABASE_TABLE_BOOK + "." + BookContract.ID + ", " +
            BookContract.TITLE + ", " +
            BookContract.PRICE + ", " + BookContract.ISBN;
    static final String GET_ONEBOOK = "SELECT " +
            DATABASE_TABLE_BOOK + "." + BookContract.ID + ", " + BookContract.TITLE + ", " +
            BookContract.PRICE + ", " + BookContract.ISBN + ", " + "GROUP_CONCAT(" + AuthorContract.NAME + ", '|') as " +
            BookContract.AUTHOR + " FROM " + DATABASE_TABLE_BOOK + " LEFT OUTER JOIN " + DATABASE_TABLE_AUTHOR + " ON " +
            DATABASE_TABLE_BOOK + "." + BookContract.ID + "=" + DATABASE_TABLE_AUTHOR + "." + AuthorContract.BOOK +
            " WHERE " + DATABASE_TABLE_BOOK + "." + BookContract.ID + "=?" +
            " GROUP BY " + DATABASE_TABLE_BOOK + "." + BookContract.ID + ", " + BookContract.TITLE + ", " +
            BookContract.PRICE + ", " + BookContract.ISBN;

    Context dbContext;
    DatabaseHelper databaseHelper;
    SQLiteDatabase sqLiteDatabase;

    //database helper part
    private static class DatabaseHelper extends SQLiteOpenHelper {
        //Constructor
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        //Override
        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_BOOK);
                db.execSQL(CREATE_AUTHOR);
                db.execSQL(CREATE_INDEX);
            } catch (SQLException e) {
                Log.e(TAG, e.toString());
            }
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_BOOK);
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_AUTHOR);
            onCreate(db);
        }
    }

    //Constructor part
    public CartDbAdapter(Context c) {
        dbContext = c;
        databaseHelper = new DatabaseHelper(c);
    }

    public void open() throws SQLException {
        sqLiteDatabase = databaseHelper.getWritableDatabase();
        sqLiteDatabase.execSQL("PRAGMA foreign_keys = ON;");
    }

    public long addBook(ContentValues ctValue) {
        return sqLiteDatabase.insert(DATABASE_TABLE_BOOK, null, ctValue);
    }
    public long addAuthor(ContentValues ctValue) {
        return sqLiteDatabase.insert(DATABASE_TABLE_AUTHOR, null, ctValue);
    }

    public int delete(long r) {
        return sqLiteDatabase.delete(DATABASE_TABLE_BOOK,BookContract.ID + "=" + r, null);
    }
    public int deleteAll() {
        return sqLiteDatabase.delete(DATABASE_TABLE_BOOK, "1", null);
    }

    public Cursor getBook(long r) {
        Cursor cursor = sqLiteDatabase.rawQuery(GET_ONEBOOK, new String[]{Long.toString(r)});
        cursor.moveToFirst();
        return cursor;
    }
    public Cursor getAllBooks() {
        Cursor cursor = sqLiteDatabase.rawQuery(GET_ALLBOOK, null);
        return cursor;
    }

}
