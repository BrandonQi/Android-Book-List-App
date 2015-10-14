package edu.stevens.cs522.bookstore.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import edu.stevens.cs522.bookstore.contracts.BookContract;

/**
 * Created by Bin Qi on 2015/3/8.
 */
public class BookCursorAdapter extends ResourceCursorAdapter {
    protected static final int row = android.R.layout.simple_list_item_2;

    //Constructor
    public BookCursorAdapter(Context ct, Cursor c) {
        super(ct, row, c, 0);
    }

    //View Part
    @Override
    public View newView(Context ct, Cursor c, ViewGroup viewGroup) {
        LayoutInflater in = (LayoutInflater)ct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return in.inflate(row, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor c) {
        //title part
        TextView viewTitle = (TextView) view.findViewById(android.R.id.text1);
        String titleStr = c.getString(c.getColumnIndexOrThrow(BookContract.TITLE));
        viewTitle.setText(titleStr);

        //author part
        TextView viewAuthor = (TextView) view.findViewById(android.R.id.text2);
        String authorStr = c.getString(c.getColumnIndexOrThrow(BookContract.AUTHOR));
        if (authorStr != null){
            String[] authorStrs = authorStr.split("\\|");
            viewAuthor.setText(authorStrs[0]);
        }
    }
}
