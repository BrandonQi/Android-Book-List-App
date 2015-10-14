package edu.stevens.cs522.bookstore.managers;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Message;

/**
 * Created by Bin Qi on 2015/3/14.
 */
public class AsyncResolver extends AsyncQueryHandler {
    public AsyncResolver(ContentResolver CR) {
        super(CR);
    }
    public void insert(Uri uri, ContentValues ctValue, IContinue<Uri> cb) {
        startInsert(0, cb, uri, ctValue);
    }
    @Override
    protected void onInsertComplete(int token, Object cookie, Uri uri) {
        if (cookie != null) {
            ((IContinue<Uri>) cookie).kontinue(uri);
        }
    }
    public void deleteAsync(Uri uri) {
        startDelete(0, null, uri, null, null);
    }

    public void queryAsync(Uri uri, IContinue<Cursor> callback) {
        startQuery(0, callback, uri, null, null, null, null);
    }

    @Override
    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
        if (cookie != null) {
            ((IContinue<Cursor>) cookie).kontinue(cursor);
        }
    }

}
