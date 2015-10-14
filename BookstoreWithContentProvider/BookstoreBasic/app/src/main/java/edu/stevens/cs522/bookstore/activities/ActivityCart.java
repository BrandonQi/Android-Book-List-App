package edu.stevens.cs522.bookstore.activities;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import java.util.ArrayList;

import edu.stevens.cs522.bookstore.R;
import edu.stevens.cs522.bookstore.adapters.BookCursorAdapter;
import edu.stevens.cs522.bookstore.contracts.BookContract;
import edu.stevens.cs522.bookstore.entities.Author;
import edu.stevens.cs522.bookstore.entities.Book;

public class ActivityCart extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    BookCursorAdapter CAdapter = null;
    Cursor c = null;
    int count = -1;
    ArrayList<Long> whereID = new ArrayList<Long>();
    public static final String DETAIL_NAME = ActivityCart.class.getCanonicalName() + "DETAIL_NAME";
    public static final String CHECKOUT_NAME = ActivityCart.class.getCanonicalName() + "CHECKOUT_NAME";

    static final private int ADD = 10000;
    static final private int CHECKOUT = 10001;

    private static final int BOOK_LOADER = 20000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);

        //list view setting part
        CAdapter = new BookCursorAdapter(this, c);
        ListView listView = getListView();
        listView.setAdapter(CAdapter);
        //multiple choice part
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater in = getMenuInflater();
                in.inflate(R.menu.menu_select, menu);

                return true;
            }
            @Override
            public void onDestroyActionMode(ActionMode mode) {
                whereID.clear();
            }
            @Override
            public boolean onActionItemClicked(ActionMode m, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete: {
                        for (int i = 0;i < whereID.size();i++) {
                            long ID = whereID.get(i);
                            ContentResolver CR =  getContentResolver();
                            Uri uri = ContentUris.withAppendedId(BookContract.BOOKS_URI, ID);
                            CR.delete(uri, null, null);
                        }
                        count = count - whereID.size();
                        whereID.clear();
                        m.finish();
                        return true;
                    }
                    default: {
                        return false;
                    }
                }
            }

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                if (checked) {
                    whereID.add(id);
                } else {
                    whereID.remove(new Long(id));
                }
                mode.setTitle(whereID.size() + " selected");
            }
        });
        LoaderManager LM = getLoaderManager();
        LM.initLoader(BOOK_LOADER, null, this);
    }

    //add and checkout items menu part
    @Override
    public boolean onCreateOptionsMenu(Menu m) {
        super.onCreateOptionsMenu(m);
        getMenuInflater().inflate(R.menu.menu_cart, m);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem i) {
        super.onOptionsItemSelected(i);
        switch (i.getItemId()) {
            case R.id.add: {
                startActivityForResult(new Intent(this, ActivityAdd.class), ADD);
                return true;
            }
            case R.id.checkout: {
                Intent checkoutIntent = new Intent(this, ActivityCheckOut.class);
                checkoutIntent.putExtra(CHECKOUT_NAME, count);
                startActivityForResult(checkoutIntent, CHECKOUT);
                return true;
            }
        }
        return false;
    }

    //use loader to run background
    @Override
    public Loader<Cursor> onCreateLoader(int lID, Bundle bundle) {
        switch (lID) {
            case BOOK_LOADER:
                Uri uri = BookContract.BOOKS_URI;
                return new CursorLoader(this, uri, null, null, null, null);
            default:
                return null;
        }
    }
    @Override
    public void onLoadFinished(Loader<Cursor> l, Cursor c) {
        CAdapter.swapCursor(c);
        count = c.getCount();
    }
    @Override
    public void onLoaderReset(Loader<Cursor> l) {
        CAdapter.swapCursor(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(BOOK_LOADER, null, this);
    }

    @Override
    protected void onListItemClick(ListView lv, View v, int position, long id) {
        Cursor cursor = new CursorLoader(this, ContentUris.withAppendedId(BookContract.BOOKS_URI, id),
                null,
                null,
                null,
                null).loadInBackground();
        cursor.moveToFirst();

        Intent viewIntent = new Intent(this, ActivityDetail.class);
        viewIntent.putExtra(DETAIL_NAME, new Book(cursor));
        startActivity(viewIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent i) {
        super.onActivityResult(requestCode, resultCode, i);
        switch (requestCode) {
            case ADD: {
                if (resultCode == RESULT_OK) {
                    Book book = i.getParcelableExtra(ActivityAdd.SEARCH_BOOK);

                    ContentValues ctValue = new ContentValues();
                    book.writeToProvider(ctValue);
                    long bookRowId = ContentUris.parseId(getContentResolver().insert(BookContract.BOOKS_URI, ctValue));

                    for (Author author : book.authors) {
                        ContentValues ctValue2 = new ContentValues();
                        author.writeToProvider(ctValue2, bookRowId);
                        getContentResolver().insert(BookContract.AUTHOR_URI, ctValue2);
                    }
                    count++;
                }
                break;
            }
            case CHECKOUT: {
                if (resultCode == RESULT_OK) {
                    ContentResolver CR = getContentResolver();
                    CR.delete(BookContract.BOOKS_URI, null, null);
                    count = 0;
                }
                break;
            }
            default: {
                break;
            }
        }
    }
}