package edu.stevens.cs522.bookstore.managers;

import android.database.Cursor;

/**
 * Created by Bin Qi on 2015/3/14.
 */
public interface IEntityCreator<T> {
    public T create(Cursor cursor);
}
