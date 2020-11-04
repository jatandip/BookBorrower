package com.example.vivlio;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;

public class BorrowTaskCustomList extends ArrayAdapter<Book>{
    public ArrayList<Book> books;
    public Context context;


    public BorrowTaskCustomList(Context context, ArrayList<Book> books) {
        super(context, 0, books);
        this.books = books;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        //if the view is null create and new view and inflate it
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.borrowtask_list, parent, false);
        }

        Book book = books.get(position);

        TextView titleTV = view.findViewById(R.id.BTL_TVtitle);
        TextView authorTV = view.findViewById(R.id.BTL_TVauthor);
        TextView ownerTV = view.findViewById(R.id.BTL_TVowner);

        titleTV.setText(book.getAuthor());
        authorTV.setText(book.getTitle());
        ownerTV.setText(book.getStatus());

        //return the view
        return view;
    }
}
