package edu.stevens.cs522.bookstore.entities;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

import edu.stevens.cs522.bookstore.contracts.BookContract;

public class Book implements Parcelable {
    public long id;
    public String title;
    public String isbn;
    public String price;
    public Author[] authors;

    //Constructor part
    public Book(String t, Author[] a, String i, String p) {
        this.title = t;
        this.authors = a;
        this.isbn = i;
        this.price = p;
    }
    private Book(Parcel src) {
        id = src.readLong();
        title = src.readString();
        Parcelable[] array = src.readParcelableArray(Author.class.getClassLoader());
        if (array != null) {
            authors = Arrays.copyOf(array, array.length, Author[].class);
        }
        isbn = src.readString();
        price = src.readString();
    }
    public Book(Cursor c) {
        this.title = BookContract.getTitle(c);
        this.isbn = BookContract.getISBN(c);
        this.price = BookContract.getPrice(c);
        String authorData = BookContract.getAuthors(c);
        String[] authors = authorData.split("\\|");
        this.authors = new Author[authors.length];
        for (int i = 0; i < authors.length; i++) {
            this.authors[i] = new Author(authors[i]);
        }
    }

    //Override part
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeParcelableArray(authors, 0);
        dest.writeString(isbn);
        dest.writeString(price);
    }
    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
    @Override
    public String toString() {
        return this.title + " Price: " + this.price +"$$$";
    }

    //write to provider part
    public void writeToProvider(ContentValues ctValue) {
        BookContract.putTitle(ctValue, this.title);
        BookContract.putISBN(ctValue, this.isbn);
        BookContract.putPrice(ctValue, this.price);
    }

}