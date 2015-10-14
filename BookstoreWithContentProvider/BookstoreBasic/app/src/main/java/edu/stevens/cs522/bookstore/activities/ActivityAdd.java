package edu.stevens.cs522.bookstore.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import edu.stevens.cs522.bookstore.R;
import edu.stevens.cs522.bookstore.entities.Author;
import edu.stevens.cs522.bookstore.entities.Book;

public class ActivityAdd extends Activity {

    public static final String SEARCH_BOOK = ActivityAdd.class.getCanonicalName()+"SEARCH_BOOK";

    @SuppressWarnings("unused")
    private static final String TAG = ActivityAdd.class.getCanonicalName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_book);
    }

    public Book addBook() {
        EditText title = (EditText) findViewById(R.id.search_title);
        String t = title.getText().toString();

        EditText isbn = (EditText) findViewById(R.id.search_isbn);
        String i = isbn.getText().toString();

        EditText author =(EditText) findViewById(R.id.search_author);
        String a = author.getText().toString();

        String[] as = a.split(",");
        int asLength = as.length;
        Author[] aus = new Author[asLength];

        int j = 0;
        while(j < aus.length){
            aus[j] = new Author(as[j]);
            j++;
        }
        return new Book(t, aus, i, "9999");
    }

    //search and cancel items Menu part
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_add, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem choice) {
        super.onOptionsItemSelected(choice);
        switch (choice.getItemId()) {
            case R.id.item_search: {
                Intent intent = this.getIntent();
                Book book = this.addBook();
                intent.putExtra(SEARCH_BOOK, book);
                this.setResult(RESULT_OK, intent);
                this.finish();
                return true;
            }
            case R.id.item_cancel: {
                this.setResult(RESULT_CANCELED, null);
                this.finish();
                return true;
            }
        }
        return false;
    }
}