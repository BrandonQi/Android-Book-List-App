package edu.stevens.cs522.bookstore.managers;

import java.util.List;

/**
 * Created by Bin Qi on 2015/3/14.
 */
public interface ISimpleQueryListener<T> {
    public void handleResults(List<T> results);
}
