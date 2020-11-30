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
import com.google.firebase.firestore.GeoPoint;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

/**
 * This Activity displays all the information about a book in the user's Request List.
 *
 * If there is no image, then a placeholder will be present.
 *
 * If a pending request has been accepted by the book's owner, the user will have the option to see
 * the location the owner has set.
 *
 * Issues:
 * Placeholder image isn't great
 * Location doesn't work currently.
 *
 * Things to consider:
 * Should the user be able to cancel their request? I could add a button to do that. Would appear
 * when the status is pending or accepted, but not borrowed.
 */

public class RequestDetailActivity extends AppCompatActivity {

    private Book book;
    private String uid;
    private FirebaseFirestore db;

    /**
     * Updates all the text and image fields for the selected book.
     * If appropriate, displays the location button and listens for a click. If clicked, launches
     * into LocationActivity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_detail);

        Intent intent = getIntent();
        book = (Book) intent.getSerializableExtra("book");
        uid = (String) intent.getSerializableExtra("user");


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
                .into(imageView);

        // create location button and hide if status is not accepted
        Button locationButton = findViewById(R.id.btn_request_location);
        if (!book.getStatus().equals("accepted")) {
            locationButton.setVisibility(View.GONE);
        }

        // turn owner code into the username to display
        // update owner TextViews
        db = FirebaseFirestore.getInstance();
        final DocumentReference docRef = db.document("users/" + book.getOwner());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();

                // make sure that document isn't null (crashes the app if it is)
                if (document.getData() != null) {
                    String userName = document.getData().get("username").toString();
                    String fname = document.getData().get("fname").toString();
                    String lname = document.getData().get("lname").toString();
                    String name = fname + " " + lname;
                    // Log.d("username", userName);

                    TextView nameView = findViewById(R.id.tv_request_name);
                    TextView ownerView = findViewById(R.id.tv_request_owner);

                    nameView.setText(name);
                    ownerView.setText(userName);
                }


            }
        });

        // listener for button to launch into LocationActivity
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get location from database
                db = FirebaseFirestore.getInstance();
                DocumentReference docRef = db.document("users/" + uid
                    + "/requested/" + book.getISBN());
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();

                        // make sure that document isn't null (crashes the app if it is)
                        // if it is, there's no location, the button shouldn't do anything
                        if (document.getData() != null) {
                            GeoPoint geo = (GeoPoint) document.getData().get("location");

                            Log.d("location", geo.toString());

                            Intent intent = new Intent(RequestDetailActivity.this, LocationActivity.class);

                            Bundle bundle = new Bundle();
                            bundle.putDouble("lat", geo.getLatitude());
                            bundle.putDouble("long", geo.getLongitude());
                            bundle.putInt("check", 1);
                            bundle.putString("isbn", book.getISBN());

                            intent.putExtras(bundle);

                            startActivity(intent);
                        }
                        else {
                            Log.d("null", "location was null");
                        }


                    }
                });
            }
        });
    }
}