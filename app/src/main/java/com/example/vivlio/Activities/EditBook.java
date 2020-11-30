package com.example.vivlio.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.vivlio.Models.Book;
import com.example.vivlio.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


/**
 * Activity that allows user to edit a book they own and is available
 * Activity will load and display the books description and user can edit the description to what they want
 * User can then save the information if they decided that they want the changes to the description they made
 * User can also delete the book if they want to
 */




public class EditBook extends AppCompatActivity {

    private EditText title;
    private EditText author;
    private EditText isbn;
    private ImageButton delete;
    private ImageButton cancel;
    private ImageButton save;
    private Book book;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;


    /**General OnCreate method, loads the books information into the activity
     * Gets the information of the book using the book that was passed in using Intent
     * Allows the user to cancel or save the edit they made to book description
     * Allows user to also delete the book if they want to.
     * @param savedInstanceState
     */

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
                if (author.getText().length() == 0 || isbn.getText().toString().length() == 0 || title.getText().toString().length() == 0) {
                    finish();
                }


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