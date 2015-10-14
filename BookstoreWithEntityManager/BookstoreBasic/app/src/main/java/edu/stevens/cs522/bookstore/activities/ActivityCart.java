package edu.stevens.cs522.bookstore.activities;


import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.ArrayList;

import edu.stevens.cs522.bookstore.R;
import edu.stevens.cs522.bookstore.adapters.BookCursorAdapter;
import edu.stevens.cs522.bookstore.contracts.BookContract;
import edu.stevens.cs522.bookstore.entities.Book;
import edu.stevens.cs522.bookstore.managers.BookManager;
import edu.stevens.cs522.bookstore.managers.IEntityCreator;
import edu.stevens.cs522.bookstore.managers.IQueryListener;
import edu.stevens.cs522.bookstore.managers.TypedCursor;

public class ActivityCart extends ListActivity {
    BookCursorAdapter CAdapter = null;
    Cursor c = null;
    ArrayList<Long> whereID = new ArrayList<Long>();
    public static final String DETAIL_NAME = ActivityCart.class.getCanonicalName() + "DETAIL_NAME";

    static final private int ADD = 10000;
    static final private int CHECKOUT = 10001;

    private static final int BOOK_LOADER = 20000;


    BookManager bookManager = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);

        //list view setting part
        CAdapter = new BookCursorAdapter(this, c);
        ListView listView = getListView();
        listView.setAdapter(CAdapter);

        //multiple choice part
        bookManager = new BookManager(this,new IEntityCreator<Book>() {
                    @Override
                    public Book create(Cursor cursor) {
                        return new Book(cursor);
                    }
                },BOOK_LOADER);
        bookManager.executeQuery(BookContract.BOOKS_URI,new IQueryListener<Book>() {
                    @Override
                    public void handleResults(TypedCursor<Book> results) {
                        CAdapter.swapCursor(results.getCursor());
                    }
                    @Override
                    public void closeResults() {
                        CAdapter.swapCursor(null);
                    }
                });

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                getMenuInflater().inflate(R.menu.menu_select, menu);
                return true;
            }
            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }
            @Override
            public void onDestroyActionMode(ActionMode mode) {
                whereID.clear();
            }
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position,long id, boolean checked) {
                if (checked) {
                    whereID.add(id);
                } else {
                    whereID.remove(new Long(id));
                }
                mode.setTitle(whereID.size() + " selected");
            }
            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete:
                        for (Long id : whereID) {
                            bookManager.removeAsync(id);
                        }
                        whereID.clear();
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    //add and check out items menu part
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_cart, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem i) {
        super.onOptionsItemSelected(i);
        switch (i.getItemId()) {
            case R.id.add:
                Intent addIntent = new Intent(this, ActivityAdd.class);
                startActivityForResult(addIntent, ADD);
                return true;
            case R.id.checkout:
                Intent checkoutIntent = new Intent(this, ActivityCheckOut.class);
                startActivityForResult(checkoutIntent, CHECKOUT);
                return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent i) {
        super.onActivityResult(requestCode, resultCode, i);
        switch (requestCode) {
            case ADD: {
                if (resultCode == RESULT_OK) {
                    Book book = i.getParcelableExtra(ActivityAdd.SEARCH_BOOK);
                    bookManager.insertAsync(book);
                }
            }
                break;
            case CHECKOUT:
                if (resultCode == RESULT_OK) {
                    bookManager.removeAsync();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onListItemClick(ListView listView, View view, int position, long id) {
        Intent viewIntent = new Intent(this, ActivityDetail.class);
        viewIntent.putExtra(DETAIL_NAME, id);
        startActivity(viewIntent);
    }

}