package com.example.vivlio;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class pendingCustomList extends ArrayAdapter<User> {

    public ArrayList<User> users;
    public Context context;


    public pendingCustomList( Context context, ArrayList<User> users) {
        super(context,0,users);
        this.users = users;
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        DecimalFormat priceFormat = new DecimalFormat("0.00");
        View view = convertView;
        //if the view is null create and new view and inflate it
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.pending_content, parent, false);

        }

        User user = users.get(position);
        Log.i("user", String.valueOf(user.getName()));


        TextView author = view.findViewById(R.id.pendingNameView);
        TextView title = view.findViewById(R.id.usernamePendingView);

        author.setText(user.getName());
        title.setText(user.getUsername());


        return view;
    }



}