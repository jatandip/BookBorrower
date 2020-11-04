package com.example.vivlio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class mybook_borrowed extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mybook_borrowed);

        Intent intent = getIntent();
        book = (Book) intent.getSerializableExtra("book");

        titleView = findViewById(R.id.titleViewBorrowed);
        authorView = findViewById(R.id.authorViewBorrowed);
        isbnView = findViewById(R.id.isbnViewBorrowed);
        editBtn = findViewById(R.id.editBtnBorrowed);

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