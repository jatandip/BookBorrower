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

/**
 * This class is used to model the indices in the list of owners of a book.
 * The custom list also displays the status of the book, denoting if the user can request the book or not.
 */
public class SearchDetailCustomList extends ArrayAdapter<User> {
    public ArrayList<User> users;
    public Context context;

    public SearchDetailCustomList(Context context, ArrayList<User> users) {
        super(context,0,  users);
        this.users = users;
        this.context = context;
    }

    /**
     * Called to get the view for one item in the list.
     * This method populates the text fields and returns the resulting view
     * @param position
     * @param convertView
     * @param parent
     * @return The view with relevant information
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.search_detail_content, parent, false);
        }

        User user = users.get(position);

        TextView username = view.findViewById(R.id.search_detail_username);
        TextView status = view.findViewById(R.id.search_detail_status);

        username.setText(user.getUsername());
        status.setText(user.getOwnedBookStatus());
        Log.e("CUSTOM_LIST_USERNAME", user.getUsername());

        return view;
    }
}
