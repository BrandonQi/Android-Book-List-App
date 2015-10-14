package edu.stevens.cs522.bookstore.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import java.util.Arrays;

import edu.stevens.cs522.bookstore.R;
import edu.stevens.cs522.bookstore.entities.Book;

public class ActivityDetail extends Activity {
    @SuppressWarnings("unused")
    private static final String TAG = ActivityDetail.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_book);
        Book book = getIntent().getParcelableExtra(ActivityCart.DETAIL_NAME);
        if (book != null) {
            TextView title =(TextView) findViewById(R.id.view_title);
            title.setText(book.title);
            TextView author = (TextView) findViewById(R.id.view_author);
            String authorString = Arrays.toString(book.authors);
            author.setText(authorString);
            TextView isbn =(TextView) findViewById(R.id.view_isbn);
            isbn.setText(book.isbn);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

}
