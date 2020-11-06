package com.example.vivlio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class RequestCustomList extends ArrayAdapter {

    public ArrayList<Book> books;
    public Context context;

    private FirebaseFirestore db;
    private String userName;


    public RequestCustomList( Context context, ArrayList<Book> books) {
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
            view = LayoutInflater.from(context).inflate(R.layout.content_request_list, parent, false);

        }

        Book book = books.get(position);

        // turn owner code into the username to display
        db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.document("users/" + book.getOwner());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                userName = task.getResult().get("username").toString();
            }
        });


        TextView title = view.findViewById(R.id.tv_title);
        TextView author = view.findViewById(R.id.tv_author);
        TextView owner = view.findViewById(R.id.tv_owner);
        TextView status = view.findViewById(R.id.tv_status);

        title.setText(book.getTitle());
        author.setText(book.getAuthor());
        owner.setText(userName);
        status.setText(book.getStatus());

        //return the view
        return view;
    }
}
