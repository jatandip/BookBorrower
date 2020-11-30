package com.example.vivlio.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vivlio.Models.Book;
import com.example.vivlio.R;
import com.example.vivlio.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.squareup.picasso.Picasso;


/**Activity that handles a book the current user owns that is available
 *Gets the information of the book from the Intent and loads the information in
 *Creates a instance of firebase firestore and auth to load the information of
 *The user that will loan the book
 */


public class Mybook_Accepted extends AppCompatActivity {
    private TextView titleView;
    private TextView authorView;
    private TextView isbnView;
    private Book book;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private Book updatedBook;
    private Boolean trigger = false;
    private TextView Name;
    private TextView username;
    public FirebaseUser user;
    public static User currentUser;
    private String nameN;
    private String usernameN;
    private ImageButton mapsBtn;
    private GeoPoint geo;


    /**
     * General OnCreate method. Loads the information of the user and the user who wants to borrow.
     * Gets the information using firebase and Intent.
     * It then displays the information.
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mybook_accepted);

        Intent intent = getIntent();
        book = (Book) intent.getSerializableExtra("book");

        titleView = findViewById(R.id.titleViewAccepted);
        authorView = findViewById(R.id.authorViewAccepted);
        isbnView = findViewById(R.id.isbnViewAccepted);

        mapsBtn = findViewById(R.id.mapsBtn);

        titleView.setText(book.getTitle());
        authorView.setText(book.getAuthor());
        isbnView.setText(book.getISBN());

        Name = findViewById(R.id.borrowNameAccepted);
        username = findViewById(R.id.borrowUserNameAccepted);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        String curr = book.getCurrentOwner();



        ImageView image = (ImageView)findViewById(R.id.imageAccepted);
        Picasso.with(Mybook_Accepted.this)
                .load(book.getPhotoURL()).into(image);


        DocumentReference docRef = db.collection("users")
                .document(curr);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();

                nameN = document.getData().get("fname").toString() + " " +
                        document.getData().get("lname");
                usernameN = document.getData().get("username").toString();
                Name.setText(nameN);
                username.setText(usernameN);
            }
        });



        mapsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DocumentReference docRef = db.collection("users")
                        .document(book.getOwner() + "/owned/" + book.getISBN());
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();

                        assert document != null;
                        geo = (GeoPoint) document.getData().get("location");
                        Log.i("geo", String.valueOf(geo));



                        double lat = geo.getLatitude();
                        double lng = geo.getLongitude ();

                        Bundle bundle = new Bundle();
                        bundle.putInt("check",1);
                        bundle.putDouble("lat",lat);
                        bundle.putDouble("long",lng);
                        bundle.putString("isbn" , book.getISBN());
                        Intent intent = new Intent(Mybook_Accepted.this, LocationActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);

                    }

                });
            }
        });

    }



}