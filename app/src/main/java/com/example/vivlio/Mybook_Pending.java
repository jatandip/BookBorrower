package com.example.vivlio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * Activity that handles pending books that the user owns.
 * Gets and shows the information of the book
 * Loads and shows the people who want to borrow your book
 */


public class Mybook_Pending extends AppCompatActivity {

    private ListView listofBooks;
    private FloatingActionButton add;
    private FirebaseFirestore db;
    public static ArrayAdapter<User> bookAdapter;
    public static ArrayList<User> bookDataList;
    private FirebaseAuth mAuth;
    private int position;
    private CollectionReference collectionReference;
    private TabLayout taby;
    public FirebaseUser user;
    private Book book;
    private ArrayList<String> borrowers = new ArrayList<String>();
    private String nameN;
    private String usernameN;
    private String phonenumber;
    private String emailN;
    private Button backButton;


    /**
     * General OnCreate Method
     * Handles getting and showing the book information
     * Handles getting all the people who want to borrow the book
     * Handles adding all the people who want to borrow the book to the custom list
     * So the activity can display the people who want to borrow the book
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mybook_pending);

        backButton = findViewById(R.id.backButtonPending);



        listofBooks = findViewById(R.id.pendingList);
        bookDataList = new ArrayList<>();
        bookAdapter = new PendingCustomList(Mybook_Pending.this, bookDataList);
        listofBooks.setAdapter(bookAdapter);
        Intent intent = getIntent();
        book = (Book) intent.getSerializableExtra("book");

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser Firebaseuser = mAuth.getCurrentUser();
        final String uid = Firebaseuser.getUid();
        //collectionReference = db.collection("users" + "/" + uid + "/owned/" + book.getISBN());

        user = mAuth.getCurrentUser();
        DocumentReference docRef = db.collection("users").document(user.getUid() + "/owned/" + book.getISBN());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                borrowers = (ArrayList<String>) document.getData().get("borrowers");
                //Log.i("borrowers", String.valueOf(borrowers));


                for (final String borrower : borrowers) {
                    DocumentReference docRef = db.collection("users/")
                            .document(borrower);
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot document = task.getResult();

                            nameN = document.getData().get("fname").toString() + " " +
                                    document.getData().get("lname");
                            usernameN = document.getData().get("username").toString();
                            emailN = document.getData().get("email").toString();
                            phonenumber = document.getData().get("phone").toString();
                            User add = new User(nameN, usernameN, emailN, phonenumber, book.getISBN(), borrower);
                            Log.i("name", document.getData().get("fname").toString() + " " +
                                    document.getData().get("lname"));

                            bookDataList.add(add);
                            bookAdapter.notifyDataSetChanged();


                        }
                    });
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });
    }


}