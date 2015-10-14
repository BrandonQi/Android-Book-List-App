package edu.stevens.cs522.bookstore.managers;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import edu.stevens.cs522.bookstore.contracts.BookContract;
import edu.stevens.cs522.bookstore.entities.Author;
import edu.stevens.cs522.bookstore.entities.Book;

/**
 * Created by Bin Qi on 2015/3/14.
 */
public class BookManager extends Manager<Book> {

    public BookManager(Context context, IEntityCreator<Book> creator, int loadId) {
        super(context, creator, loadId);
    }

    public void insertAsync(final Book book) {
        ContentValues contentValues = new ContentValues();
        book.writeToProvider(contentValues);
        getAsyncResolver().insert(BookContract.BOOKS_URI,contentValues,
                new IContinue<Uri>() {
                    @Override
                    public void kontinue(Uri value) {
                        book.id = ContentUris.parseId(value);
                        for (Author author : book.authors) {
                            ContentValues ctValue = new ContentValues();
                            author.writeToProvider(ctValue, book.id);
                            getAsyncResolver().insert(BookContract.AUTHORS_URI, ctValue, null);
                        }
                    }});
    }
    public void removeAsync(long ID) {
        getAsyncResolver().deleteAsync(ContentUris.withAppendedId(BookContract.BOOKS_URI, ID));
    }
    public void removeAsync() {
        getAsyncResolver().deleteAsync(BookContract.BOOKS_URI);
    }


}
