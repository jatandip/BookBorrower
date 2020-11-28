package com.example.vivlio.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vivlio.Book;
import com.example.vivlio.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class RequestDetailActivity extends AppCompatActivity {

    private Book book;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_detail);

        Intent intent = getIntent();
        book = (Book) intent.getSerializableExtra("book");

        // set book information TextViews
        TextView titleView = findViewById(R.id.tv_request_title);
        TextView authorView = findViewById(R.id.tv_request_author);
        TextView isbnView = findViewById(R.id.tv_request_isbn);
        TextView statusView = findViewById(R.id.tv_request_status);

        titleView.setText(book.getTitle());
        authorView.setText(book.getAuthor());
        isbnView.setText(book.getISBN());
        statusView.setText(book.getStatus());

        // set image
        ImageView imageView = (ImageView) findViewById(R.id.iv_request_image);
        Picasso.with(RequestDetailActivity.this)
                .load(book.getPhotoURL())
                .placeholder(R.drawable.ic_dashboard_black_24dp)
                .into(imageView);

        // create location button and hide if status is not accepted
        Button locationButton = findViewById(R.id.btn_request_location);
        if (!book.getStatus().equals("accepted")) {
            locationButton.setVisibility(View.GONE);
        }



        // turn owner code into the username to display
        // update owner TextViews
        db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.document("users/" + book.getOwner());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();

                // make sure that document isn't null (crashes the app if it is)
                if (document.getData() != null) {
                    String userName = document.getData().get("username").toString();
                    // Log.d("username", userName);

                    TextView ownerView = findViewById(R.id.tv_request_owner);
                    ownerView.setText(userName);
                }


            }
        });








    }
}