package com.example.vivlio;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SearchDetailActivity extends AppCompatActivity {
    private TextView titleEditText;
    private TextView authorEditText;
    FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private ListView searchDetailList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_detail);

        titleEditText = findViewById(R.id.search_detail_title);
        authorEditText = findViewById(R.id.search_detail_author);

        Intent intent = getIntent();
        Book searchDetailBook = (Book) intent.getSerializableExtra("selected book");

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }
}
