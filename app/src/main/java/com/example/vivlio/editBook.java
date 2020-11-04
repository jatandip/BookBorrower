package com.example.vivlio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class editBook extends AppCompatActivity {

    private EditText title;
    private EditText author;
    private EditText isbn;
    private Button delete;
    private Button cancel;
    private Button save;
    private Book book;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser Firebaseuser = mAuth.getCurrentUser();


        title = findViewById(R.id.titleBox);
        author = findViewById(R.id.authorBox);
        isbn = findViewById(R.id.isbnBox);
        save = findViewById(R.id.saveBtn);
        delete = findViewById(R.id.deleteBtn);
        cancel = findViewById(R.id.cancelBtn);


        Intent intent = getIntent();
        book = (Book) intent.getSerializableExtra("book");


        title.setText(book.getTitle());
        author.setText(book.getAuthor());
        isbn.setText(book.getISBN());

        String UID = book.getCurrentOwner();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                book.setAuthor(author.getText().toString());
                book.setISBN(isbn.getText().toString());
                book.setTitle(title.getText().toString());

                Intent sendBack = new Intent();
                sendBack.putExtra("book", book);
                setResult(RESULT_OK, sendBack);
                finish();

            }
        });


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendBack = new Intent();
                sendBack.putExtra("book", "DELETE_BOOK");
                setResult(5, sendBack);
                finish();


            }
        });



    }




}