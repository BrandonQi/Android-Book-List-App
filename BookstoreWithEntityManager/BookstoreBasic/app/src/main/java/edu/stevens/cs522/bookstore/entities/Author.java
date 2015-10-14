package edu.stevens.cs522.bookstore.entities;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import edu.stevens.cs522.bookstore.contracts.AuthorContract;

public class Author implements Parcelable {
    public String first;
    public String middle;
    public String last;

    //Constructor part
    private Author(Parcel src) {
        this.first = src.readString();
        this.middle = src.readString();
        this.last = src.readString();
    }

    public Author(String name) {
        String[] names = name.split(" ");

        if (names.length == 1) {
            this.first = names[0];
            this.middle = "";
            this.last = "";
        }

        if (names.length == 2) {
            this.first = names[0];
            this.middle = "";
            this.last = names[1];
        }

        if (names.length > 2) {
            this.first = names[0];
            this.middle = names[1];
            this.last = names[2];
        }
    }

    //Override part
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel tar, int flags) {
        tar.writeString(this.first);
        tar.writeString(this.middle);
        tar.writeString(this.last);
    }
    public static final Creator<Author> CREATOR = new Creator<Author>() {
        @Override
        public Author createFromParcel(Parcel source) {
            return new Author(source);
        }
        @Override
        public Author[] newArray(int size) {
            return new Author[size];
        }
    };
    @Override
    public String toString() {
        if(this.middle.isEmpty()){
            if(this.last.isEmpty()){
                return this.first;
            }else{
                return this.first + this.last;
            }
        }else{
            return this.first + " " + this.middle + " " + this.last;
        }
    }
    //write to provider part
    public void writeToProvider(ContentValues ctValue, long bookID) {
        AuthorContract.setBookID(ctValue, bookID);
        AuthorContract.setName(ctValue, this.toString());
    }
}
