package edu.stevens.cs522.bookstore.managers;

import android.database.ContentObserver;
import android.database.Cursor;

/**
 * Created by Bin Qi on 2015/3/14.
 */

public class TypedCursor<T> {
    private Cursor cursor;
    private IEntityCreator<T> creator;

    public TypedCursor(Cursor cursor, IEntityCreator<T> creator) {
        this.cursor = cursor;
        this.creator = creator;
    }

    public Cursor getCursor() {
        return cursor;
    }

    public int getCount() {
        return cursor.getCount();
    }

    public boolean moveToFirst() {
        return cursor.moveToFirst();
    }

    public boolean moveToNext() {
        return cursor.moveToNext();
    }

    public T getEntity() {
        return creator.create(cursor);
    }

    public T create(Cursor cursor) {
        return creator.create(cursor);
    }

    public void close() {
        cursor.close();
    }

    public void registerContentObserver(ContentObserver observer) {
        cursor.registerContentObserver(observer);
    }

    public void unregisterContentObserver(ContentObserver observer) {
        cursor.unregisterContentObserver(observer);
    }

}
