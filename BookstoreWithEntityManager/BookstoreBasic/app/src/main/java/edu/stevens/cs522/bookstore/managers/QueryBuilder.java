package edu.stevens.cs522.bookstore.managers;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

/**
 * Created by Bin Qi on 2015/3/14.
 */
public class QueryBuilder<T> implements LoaderManager.LoaderCallbacks<Cursor> {
    private IEntityCreator<T> tiEntityCreator;
    private IQueryListener<T> listener;
    private Context context;
    private Uri uri;
    private int loaderId;

    private QueryBuilder(Context context, Uri uri, int loaderId,IEntityCreator<T> creator, IQueryListener<T> listener) {
        this.context = context;
        this.uri = uri;
        this.loaderId = loaderId;
        this.tiEntityCreator = creator;
        this.listener = listener;
    }
    public static <T> void executeQuery(Activity activity,Uri uri,int loaderId,IEntityCreator<T> creator,IQueryListener<T> listener) {
        QueryBuilder<T> queryBuilder = new QueryBuilder<T>(activity, uri, loaderId, creator, listener);
        activity.getLoaderManager().initLoader(loaderId, null, queryBuilder);
    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        if (id == this.loaderId) {
            return new CursorLoader(this.context, this.uri, null, null, null, null);
        }
        return null;
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (loader.getId() == this.loaderId) {
            this.listener.handleResults(new TypedCursor<T>(cursor, tiEntityCreator));
        }
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (loader.getId() == this.loaderId) {
            this.listener.closeResults();
        }
    }

}
