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

import java.text.DecimalFormat;
import java.util.ArrayList;

public class BookList extends ArrayAdapter<Book> {

    public ArrayList<Book> books;
    public Context context;


    public BookList( Context context, ArrayList<Book> books) {
        super(context,0,books);
        this.books = books;
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        DecimalFormat priceFormat = new DecimalFormat("0.00");
        View view = convertView;
        //if the view is null create and new view and inflate it
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.content, parent, false);

        }

        Book book = books.get(position);

        TextView author = view.findViewById(R.id.maker_View);
        TextView title = view.findViewById(R.id.bookName_View);
        TextView request = view.findViewById(R.id.request_View);



        author.setText(book.getAuthor());
        title.setText(book.getTitle());
        request.setText(book.getStatus());

        //Log.i("asdasd", book.getCurrentOwner());


        //return the view
        return view;
    }



}
