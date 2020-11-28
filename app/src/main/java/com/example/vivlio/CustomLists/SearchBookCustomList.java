package com.example.vivlio.CustomLists;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.vivlio.Book;
import com.example.vivlio.R;
import com.example.vivlio.User;

import java.util.ArrayList;

/**
 * This model provides the format of the list of results when searching.
 * The class can create views with the book name and author match the search terms provided.
 * These views are used to populate the ListView of search results.
 */
public class SearchBookCustomList extends ArrayAdapter<Book> {

    public ArrayList<Book> books;
    public Context context;

    public SearchBookCustomList(Context context, ArrayList<Book> books) {
        super(context,0,  books);
        this.books = books;
        this.context = context;
    }

    /**
     * This method returns the view for a single book in the custom list.
     * @param position
     * @param convertView
     * @param parent
     * @return The view populated with information
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.search_content, parent, false);
        }

        Book book = books.get(position);

        TextView title = view.findViewById(R.id.SL_title);
        TextView author = view.findViewById(R.id.SL_author);

        title.setText(book.getTitle());
        author.setText(book.getAuthor());

        return view;
    }
}
