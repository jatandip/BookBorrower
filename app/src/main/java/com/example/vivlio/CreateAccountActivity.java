package com.example.vivlio;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CreateAccountActivity extends AppCompatActivity {
    private EditText firstNameET = findViewById(R.id.CREACC_ETfirstName);
    private EditText lastNameET = findViewById(R.id.CREACC_ETlastName);
    private EditText usernameET = findViewById(R.id.CREACC_ETusername);
    private EditText emailET = findViewById(R.id.CREACC_ETemail);
    private EditText phoneET = findViewById(R.id.CREACC_ETphone);
    private EditText passwordET = findViewById(R.id.CREACC_ETpassword);
    private EditText repasswordET = findViewById(R.id.CREACC_ETrepassword);
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);


        ImageButton createBTN = findViewById(R.id.CREACC_BTNcreate);

        mAuth = FirebaseAuth.getInstance();

        createBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Handle valid parameters
                String firstName = firstNameET.getText().toString().trim();
                String lastName = lastNameET.getText().toString().trim();
                String username = usernameET.getText().toString().trim();
                String email = emailET.getText().toString().trim();
                String phone = phoneET.getText().toString().trim();
                String password = passwordET.getText().toString().trim();
                String repassword = repasswordET.getText().toString().trim();

                if(TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(username) ||
                        TextUtils.isEmpty(email) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(password) ||
                        TextUtils.isEmpty(repassword)){
                    Toast.makeText(CreateAccountActivity.this, "Please fill in all fields",
                            Toast.LENGTH_SHORT).show();
                } else if(password.equals(repassword)){
                    mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(CreateAccountActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    //Log.d(TAG, "createUserWithEmail:success");
                                    //FirebaseUser user = mAuth.getCurrentUser();
                                    openMainActivity();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(CreateAccountActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                }
            }
        });
    }

    private void openMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
