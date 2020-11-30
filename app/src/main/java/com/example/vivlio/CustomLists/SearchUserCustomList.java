package com.example.vivlio.CustomLists;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.vivlio.R;
import com.example.vivlio.Models.User;

import java.util.ArrayList;

/**
 * This model provides the format of the list of results when searching.
 * The class can create views with the username and name of the users who match the search terms provided.
 * These views are used to populate the ListView of search results.
 */
public class SearchUserCustomList extends ArrayAdapter<User> {

    public ArrayList<User> users;
    public Context context;

    public SearchUserCustomList(Context context, ArrayList<User> users) {
        super(context,0,  users);
        this.users = users;
        this.context = context;
    }

    /**
     * This method returns the view for a single user in the custom list.
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

        User user = users.get(position);

        TextView username = view.findViewById(R.id.SL_title);
        TextView name = view.findViewById(R.id.SL_author);

        username.setText(user.getUsername());
        name.setText(user.getName());

        return view;
    }

}
