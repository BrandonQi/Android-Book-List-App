package edu.stevens.cs522.bookstore.managers;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bin Qi on 2015/3/14.
 */
public class SimpleQueryBuilder<T> implements IContinue<Cursor> {

    private IEntityCreator<T> CREATOR;
    private ISimpleQueryListener<T> tiSimpleQueryListener;

    private SimpleQueryBuilder(IEntityCreator<T> creator, ISimpleQueryListener<T> listener) {
        this.CREATOR = creator;
        this.tiSimpleQueryListener = listener;
    }
    public static <T> void executeQuery(Context context,Uri uri,IEntityCreator<T> tiEntityCreator,ISimpleQueryListener<T> tiSimpleQueryListener1) {
        SimpleQueryBuilder<T> tSimpleQueryBuilder = new SimpleQueryBuilder<T>(tiEntityCreator, tiSimpleQueryListener1);
        AsyncResolver asyncResolver = new AsyncResolver(context.getContentResolver());
        asyncResolver.queryAsync(uri, tSimpleQueryBuilder);
    }

    @Override
    public void kontinue(Cursor cursor) {
        List<T> instances = new ArrayList<T>();
        if (cursor.moveToFirst()) {
            do {
                T instance = this.CREATOR.create(cursor);
                instances.add(instance);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        this.tiSimpleQueryListener.handleResults(instances);
    }
}

