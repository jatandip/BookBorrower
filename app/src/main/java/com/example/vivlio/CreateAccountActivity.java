package com.example.vivlio;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class CreateAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        EditText firstNameET = findViewById(R.id.CREACC_ETfirstName);
        EditText lastNameET = findViewById(R.id.CREACC_ETlastName);
        EditText usernameET = findViewById(R.id.CREACC_ETusername);
        EditText emailET = findViewById(R.id.CREACC_ETemail);
        EditText phoneET = findViewById(R.id.CREACC_ETphone);
        EditText passwordET = findViewById(R.id.CREACC_ETpassword);
        EditText repasswordET = findViewById(R.id.CREACC_ETrepassword);
        ImageButton createBTN = findViewById(R.id.CREACC_BTNcreate);

        String firstName = firstNameET.getText().toString();
        String lastName = lastNameET.getText().toString();
        String username = usernameET.getText().toString();
        String email = emailET.getText().toString();
        String phone = phoneET.getText().toString();
        String password = passwordET.getText().toString();
        String repassword = repasswordET.getText().toString();

        createBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Handle account creation
                //TODO Handle valid parameters
            }
        });

    }
}
