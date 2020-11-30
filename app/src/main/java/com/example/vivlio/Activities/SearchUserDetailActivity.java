package com.example.vivlio.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vivlio.R;
import com.example.vivlio.Models.User;
import com.google.firebase.auth.FirebaseAuth;

public class SearchUserDetailActivity extends AppCompatActivity {

    private TextView nameEditText;
    private TextView usernameEditText;
    private TextView mobileEditText;
    private TextView emailEditText;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user_detail);

        nameEditText = findViewById(R.id.SUD_name);
        usernameEditText = findViewById(R.id.SUD_username);
        mobileEditText = findViewById(R.id.SUD_mobile);
        emailEditText = findViewById(R.id.SUD_email);

        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        User clickedUser = (User) intent.getSerializableExtra("selected user");

        nameEditText.setText(clickedUser.getName());
        usernameEditText.setText(clickedUser.getUsername());
        mobileEditText.setText(clickedUser.getPhonenumber());
        emailEditText.setText(clickedUser.getEmail());
    }
}
