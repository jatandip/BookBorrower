package com.example.vivlio.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vivlio.Book;
import com.example.vivlio.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateAccountActivity extends AppCompatActivity {
    private EditText firstNameET;
    private EditText lastNameET;
    private EditText usernameET;
    private EditText emailET;
    private EditText phoneET;
    private EditText passwordET;
    private EditText repasswordET;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private CollectionReference collectionReference;
    private boolean uniqueUsername = true;

    /**
     * Handles creation of a new account. ocne all the fields have been properly filled, will call
     * Firebase Auth to create an account. if successful, will create a new user document in
     * FireStore with the appropriate details. Opens Login activity again from where the user can
     * log in with their new account.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        ImageButton createBTN = findViewById(R.id.CREACC_BTNcreate);
        firstNameET = findViewById(R.id.CREACC_ETfirstName);
        lastNameET = findViewById(R.id.CREACC_ETlastName);
        usernameET = findViewById(R.id.CREACC_ETusername);
        emailET = findViewById(R.id.CREACC_ETemail);
        phoneET = findViewById(R.id.CREACC_ETphone);
        passwordET = findViewById(R.id.CREACC_ETpassword);
        repasswordET = findViewById(R.id.CREACC_ETrepassword);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        createBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Handle valid parameters
                final String firstName = firstNameET.getText().toString().trim();
                final String lastName = lastNameET.getText().toString().trim();
                final String username = usernameET.getText().toString().trim();
                final String email = emailET.getText().toString().trim();
                final String phone = phoneET.getText().toString().trim();
                final String password = passwordET.getText().toString().trim();
                String repassword = repasswordET.getText().toString().trim();
                //tester
                if(TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(username) ||
                        TextUtils.isEmpty(email) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(password) ||
                        TextUtils.isEmpty(repassword)) {
                    Toast.makeText(CreateAccountActivity.this, "Please fill in all fields",
                            Toast.LENGTH_SHORT).show();
                }else if(password.equals(repassword) == false){
                    Toast.makeText(CreateAccountActivity.this, "Passwords do not match!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    uniqueUsername = true;
                    collectionReference = db.collection("users");
                    collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                                FirebaseFirestoreException error) {
                            for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                            {
                                Log.e("peek username", doc.getData().get("username").toString());
                                if (doc.getData().get("username").toString().equals(username)) {
                                    Log.e("unique","in");
                                    uniqueUsername = false;
                                }
                            }
                            if (uniqueUsername) {
                                mAuth.createUserWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(CreateAccountActivity.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    // Sign in success, update UI with the signed-in user's information
                                                    Log.d("login Success", "signInWithEmail:success");
                                                    Map<String, Object> userInfo = new HashMap<>();
                                                    userInfo.put("email", email);
                                                    userInfo.put("fname", firstName);
                                                    userInfo.put("lname", lastName);
                                                    userInfo.put("phone", phone);
                                                    userInfo.put("scanned isbn", "");
                                                    userInfo.put("username", username);
                                                    db.collection("users")
                                                            .document(mAuth.getCurrentUser().getUid())
                                                            .set(userInfo);
                                                    HashMap<String, Object> info = new HashMap<>();
                                                    info.put("blank", "BLANK");
                                                    db.collection("users").document(
                                                            mAuth.getCurrentUser().getUid() +
                                                                    "/requested" + "/BLANK_BOOK").set(info);
                                                    db.collection("users").document(
                                                            mAuth.getCurrentUser().getUid() +
                                                                    "/owned" + "/BLANK_BOOK").set(info);
                                                    //collectionReference = db.collection("users" + "/" + currentUID + "/requested");

                                                    openLoginActivity();
                                                } else {
                                                    // If sign in fails, display a message to the user.
                                                    Log.w("Login Failed", "signInWithEmail:failure", task.getException());
                                                    Toast.makeText(CreateAccountActivity.this, "Authentication failed.",
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } else {
                                Toast.makeText(CreateAccountActivity.this, "Username already in use!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }});


                }
            }
        });
    }

    /**
     * called to open login activity after successful account creation
     */
    private void openLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
