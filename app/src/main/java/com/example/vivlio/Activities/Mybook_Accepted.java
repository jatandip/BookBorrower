package com.example.vivlio.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.vivlio.Book;
import com.example.vivlio.R;
import com.example.vivlio.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


/**Activity that handles a book the current user owns that is available
 *Gets the information of the book from the Intent and loads the information in
 *Creates a instance of firebase firestore and auth to load the information of
 *The user that will loan the book
 */


public class Mybook_Accepted extends AppCompatActivity {
    private TextView titleView;
    private TextView authorView;
    private TextView isbnView;
    private Button editBtn;
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
    private Button mapsBtn;


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
                Intent intent = new Intent(Mybook_Accepted.this, LocationActivity.class);
                view.getContext().startActivity(intent);
            }
        });

    }



}