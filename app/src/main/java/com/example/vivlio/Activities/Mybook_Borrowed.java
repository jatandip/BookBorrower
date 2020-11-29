package com.example.vivlio.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vivlio.Book;
import com.example.vivlio.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

/**Activity that handles a book the current user owns that is borrowed
 *Gets the information of the book from the Intent and loads the information in
 *Creates a instance of firebase firestore and auth to load the information of
 *The user that is currently borrowing the book
 */

public class Mybook_Borrowed extends AppCompatActivity {
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
    private String nameN;
    private String usernameN;


    /**
     * General OnCreate method. Loads the information of the user and the user who is borrowing the book
     * Gets the information using firebase and Intent.
     * It then displays the information.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mybook_borrowed);

        Intent intent = getIntent();
        book = (Book) intent.getSerializableExtra("book");

        titleView = findViewById(R.id.titleViewBorrowed);
        authorView = findViewById(R.id.authorViewBorrowed);
        isbnView = findViewById(R.id.isbnViewBorrowed);
        //editBtn = findViewById(R.id.editBtnBorrowed);

        titleView.setText(book.getTitle());
        authorView.setText(book.getAuthor());
        isbnView.setText(book.getISBN());

        Name = findViewById(R.id.borrowName);
        username = findViewById(R.id.borrowUserName);


        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser Firebaseuser = mAuth.getCurrentUser();

        String uid = Firebaseuser.getUid();


        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        String curr = book.getCurrentOwner();


        ImageView image = (ImageView)findViewById(R.id.imageBorrowed);
        Picasso.with(Mybook_Borrowed.this)
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



    }
}