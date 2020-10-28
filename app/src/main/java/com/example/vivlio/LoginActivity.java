package com.example.vivlio;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private ImageButton loginBTN;
    private EditText usernameET;
    private EditText passwordET;
    private TextView createAccountTV;

    @Override
    protected void onCreate(Bundle savedInstances) {
        super.onCreate(savedInstances);
        setContentView(R.layout.activity_login_page);

        loginBTN = findViewById(R.id.LOGIN_BTNlogin);
        usernameET = findViewById(R.id.LOGIN_ETusername);
        passwordET = findViewById(R.id.LOGIN_ETpassword);
        createAccountTV = findViewById(R.id.LOGIN_TVcreateAccount);

        String username = usernameET.getText().toString();
        String password = passwordET.getText().toString();

        createAccountTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateAccount();
            }
        });

        loginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO handle authentication
            }
        });

    }

    private void openCreateAccount(){
        Intent intent = new Intent(this, CreateAccountActivity.class);
        startActivity(intent);
    }
}
