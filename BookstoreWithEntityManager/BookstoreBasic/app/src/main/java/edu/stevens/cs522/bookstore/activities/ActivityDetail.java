package edu.stevens.cs522.bookstore.activities;

import android.app.Activity;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import edu.stevens.cs522.bookstore.R;
import edu.stevens.cs522.bookstore.contracts.BookContract;
import edu.stevens.cs522.bookstore.entities.Book;
import edu.stevens.cs522.bookstore.managers.BookManager;
import edu.stevens.cs522.bookstore.managers.IEntityCreator;
import edu.stevens.cs522.bookstore.managers.ISimpleQueryListener;

public class ActivityDetail extends Activity {

    private static final int LOADER_ID_A_BOOK = 2;
    BookManager bookManager = null;

    @SuppressWarnings("unused")
    private static final String TAG = ActivityDetail.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_book);
        long id = getIntent().getLongExtra(ActivityCart.DETAIL_NAME, -1);
        if (id != -1) {
            bookManager = new BookManager(this,
                    new IEntityCreator<Book>() {
                        @Override
                        public Book create(Cursor cursor) {
                            return new Book(cursor);
                        }
                    },LOADER_ID_A_BOOK);
            Uri uri = ContentUris.withAppendedId(BookContract.BOOKS_URI, id);
            bookManager.executeSimpleQuery(uri,
                    new ISimpleQueryListener<Book>() {
                        @Override
                        public void handleResults(List<Book> results) {
                            TextView title = (TextView) findViewById(R.id.view_title);
                            title.setText(results.get(0).title);
                            TextView author =(TextView) findViewById(R.id.view_author);
                            author.setText(Arrays.toString(results.get(0).authors));
                            TextView isbn =(TextView) findViewById(R.id.view_isbn);
                            isbn.setText(results.get(0).isbn);
                        }});
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

}
