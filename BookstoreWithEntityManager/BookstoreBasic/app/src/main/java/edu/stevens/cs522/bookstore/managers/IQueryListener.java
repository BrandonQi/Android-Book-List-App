package edu.stevens.cs522.bookstore.managers;

/**
 * Created by Bin Qi on 2015/3/14.
 */
public interface IQueryListener<T> {
    public void handleResults(TypedCursor<T> results);

    public void closeResults();
}
