package com.example.vivlio;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class mybook_pending extends AppCompatActivity {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mybook_pending);


        listofBooks = findViewById(R.id.pendingList);
        bookDataList = new ArrayList<>();
        bookAdapter = new pendingCustomList(mybook_pending.this, bookDataList);
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
                    DocumentReference docRef = db.collection("users")
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
    }


}