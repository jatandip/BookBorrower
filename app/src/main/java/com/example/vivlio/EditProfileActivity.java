package com.example.vivlio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;



/**
 * Activity that allows user to edit their profile information
 * Activity will load and display the the users information and user can edit their description if they want to
 * User can then save the information using the save button
 * User can also cancel the changes made to their profile information using the cancel button
 */


public class EditProfileActivity extends AppCompatActivity {

    EditText emailEdit;
    EditText phoneEdit;
    Button save;
    Button cancel;
    String newEmail;
    String newPhone;


    /**
     * General OnCreate method.
     * Method loads the profile information of the user and displays it.
     * If user decides to save the changes, method gets the updated fields and sends the information back using the Intent.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Intent intent = getIntent();
        ArrayList<String> userInfo = intent.getStringArrayListExtra("userInfo");

        emailEdit = findViewById(R.id.emailEdit);
        phoneEdit = findViewById(R.id.phoneNumberEdit);
        save = findViewById(R.id.saveButton);
        cancel = findViewById(R.id.cancel_button);

        emailEdit.setText(userInfo.get(1));
        phoneEdit.setText(userInfo.get(0));

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newEmail = emailEdit.getText().toString();
                newPhone = phoneEdit.getText().toString();
                Intent sendBack = new Intent();
                ArrayList<String> newInfo = new ArrayList<>();
                newInfo.add(newEmail);
                newInfo.add(newPhone);
                sendBack.putStringArrayListExtra("result", newInfo);
                setResult(RESULT_OK, sendBack);
                finish();

            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });

    }










}