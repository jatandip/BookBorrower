package com.example.vivlio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SearchDetailCustomList extends ArrayAdapter<User> {
    public ArrayList<User> users;
    public Context context;

    public SearchDetailCustomList(Context context, ArrayList<User> users) {
        super(context,0,  users);
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.search_detail_content, parent, false);
        }

        User user = users.get(position);

        TextView username = view.findViewById(R.id.search_detail_username);
        TextView status = view.findViewById(R.id.maker_View);

        username.setText(user.getUsername());
        status.setText(user.getName());

        return view;
    }
}
