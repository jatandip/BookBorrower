package com.example.vivlio.CustomLists;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.vivlio.Models.Book;
import com.example.vivlio.R;

import java.text.DecimalFormat;
import java.util.ArrayList;


/**
 * This is a custom list to store the books for MyBookListFragment
 * This customList will set the view. Sets the author, title, and the status of the book to be displayed
 * In the end will return a view
 */
public class BookList extends ArrayAdapter<Book> {

    public ArrayList<Book> books;
    public Context context;


    public BookList( Context context, ArrayList<Book> books) {
        super(context,0,books);
        this.books = books;
        this.context = context;
    }



    /**
     * General onCreate method, this queries the database to find the owners of the book and displays whether or not the book has already been requested.
     * Allows the user to request available books.
     * @param position
     * @param convertView
     * @param parent
     * @return view
     */
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
