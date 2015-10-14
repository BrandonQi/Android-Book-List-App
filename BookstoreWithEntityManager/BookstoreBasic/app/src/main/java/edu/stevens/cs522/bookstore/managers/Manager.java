package edu.stevens.cs522.bookstore.managers;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

/**
 * Created by Bin Qi on 2015/3/14.
 */

public abstract class Manager<T> {
    ContentResolver contentResolver;
    AsyncResolver asyncResolver;

    final Context context;
    final IEntityCreator<T> CREATOR;
    final int ID;

    protected Manager(Context context, IEntityCreator<T> creator, int ID) {
        this.context = context;
        this.CREATOR = creator;
        this.ID = ID;
    }

    protected AsyncResolver getAsyncResolver() {
        if (asyncResolver == null) {
            asyncResolver = new AsyncResolver(context.getContentResolver());
        }
        return asyncResolver;
    }

    public void executeSimpleQuery(Uri uri, ISimpleQueryListener<T> listener) {
        SimpleQueryBuilder.executeQuery(context, uri, CREATOR, listener);
    }

    public void executeQuery(Uri uri, IQueryListener<T> listener) {
        QueryBuilder.executeQuery((Activity) context, uri, ID,
                CREATOR, listener);
    }
}
