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
 * Created by Bin Qi on 2015/2/25.
 */

public class CartDbAdapter {
    static final String DATABASE_NAME = "database.bookstore";

    static final String DATABASE_TABLE_BOOK = "books";
    static final String DATABASE_TABLE_AUTHOR = "authors";
    static final int DATABASE_VERSION = 1;

    static final String TAG = CartDbAdapter.class.getCanonicalName();

    //SQL part
    static final String CREATE_BOOK = "CREATE TABLE " + DATABASE_TABLE_BOOK + " (" +
            BookContract.ID + " INTEGER PRIMARY KEY, " +
            BookContract.TITLE + " TEXT NOT NULL, " +
            BookContract.ISBN + " TEXT NOT NULL, " +
            BookContract.PRICE + " TEXT NOT NULL" + ");";
    static final String CREATE_AUTHOR = "CREATE TABLE " +
            DATABASE_TABLE_AUTHOR + " (" +
            AuthorContract.ID + " INTEGER PRIMARY KEY, " +
            AuthorContract.NAME + " TEXT NOT NULL, " +
            AuthorContract.BOOKS + " INTEGER, " +
            "FOREIGN KEY (" + AuthorContract.BOOKS + ") REFERENCES " + DATABASE_TABLE_BOOK +
            "(" + BookContract.ID + ") ON DELETE CASCADE" + ");";
    static final String CREATE_INDEX = "CREATE INDEX IF NOT EXISTS " +
            DATABASE_TABLE_AUTHOR + DATABASE_TABLE_BOOK + " ON " +
            DATABASE_TABLE_AUTHOR + "(" + AuthorContract.BOOKS + ");";
    static final String DB_SELECT_ALL_BOOKS = "SELECT " +
            DATABASE_TABLE_BOOK + "." + BookContract.ID + ", " +
            BookContract.TITLE + ", " +
            BookContract.PRICE + ", " +
            BookContract.ISBN + ", " +
            "GROUP_CONCAT(" + AuthorContract.NAME + ", '|') as " + BookContract.AUTHOR +
            " FROM " + DATABASE_TABLE_BOOK + " LEFT OUTER JOIN " + DATABASE_TABLE_AUTHOR + " ON " +
            DATABASE_TABLE_BOOK + "." + BookContract.ID + "=" +
            DATABASE_TABLE_AUTHOR + "." + AuthorContract.BOOKS +
            " GROUP BY " + DATABASE_TABLE_BOOK + "." + BookContract.ID + ", " +
            BookContract.TITLE + ", " + BookContract.PRICE + ", " + BookContract.ISBN;
    static final String DB_SELECT_A_BOOK = "SELECT " + DATABASE_TABLE_BOOK + "." + BookContract.ID + ", " +
            BookContract.TITLE + ", " +
            BookContract.PRICE + ", " +
            BookContract.ISBN + ", " +
            "GROUP_CONCAT(" + AuthorContract.NAME + ", '|') as " +
            BookContract.AUTHOR +
            " FROM " + DATABASE_TABLE_BOOK + " LEFT OUTER JOIN " + DATABASE_TABLE_AUTHOR + " ON " +
            DATABASE_TABLE_BOOK + "." + BookContract.ID + "=" +
            DATABASE_TABLE_AUTHOR + "." + AuthorContract.BOOKS + " WHERE "+
            DATABASE_TABLE_BOOK + "." + BookContract.ID + "=?" +
            " GROUP BY " + DATABASE_TABLE_BOOK + "." + BookContract.ID + ", " +
            BookContract.TITLE + ", " +
            BookContract.PRICE + ", " +
            BookContract.ISBN;

    Context dbContext;
    DatabaseHelper databaseHelper;
    SQLiteDatabase sqLiteDatabase;

    //Constructor part
    public CartDbAdapter(Context c) {
        dbContext = c;
        databaseHelper = new DatabaseHelper(c);
    }

    //database part
    static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
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

    //methods part
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
    public int delete(long ID) {
        return sqLiteDatabase.delete(DATABASE_TABLE_BOOK,BookContract.ID + "=" + ID,null);
    }
    public Cursor getBook(long ID) {
        Cursor cursor = sqLiteDatabase.rawQuery(DB_SELECT_A_BOOK,new String[]{Long.toString(ID)});
        cursor.moveToFirst();
        return cursor;
    }

    public int deleteAll() {
        return sqLiteDatabase.delete(DATABASE_TABLE_BOOK, "1", null);
    }
    public Cursor getAllBooks() {
        return sqLiteDatabase.rawQuery(DB_SELECT_ALL_BOOKS, null);
    }
}
