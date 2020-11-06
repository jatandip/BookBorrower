package com.example.vivlio;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    private ImageButton loginBTN;
    private EditText usernameET;
    private EditText passwordET;
    private TextView createAccountTV;

    private String nameN;
    private String usernameN;
    private String emailN;
    private String phoneN;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public FirebaseUser user;
    public static User currentUser;
    private FirebaseFirestore db;

    /**
     * onCreate handles the authentication of the user. It will take the user's email and password
     * that is inputted and call Firebase Authentication to verify the credentials. if one or both
     * of the fields are empty it will create a warning prompting the user to fill in the details.
     *
     * Once the authentication is successful, we create a new "currentUser" of type User using the
     * details of the current user which can be called from other classes.
     *
     * If the user taps on "new to Vivlio?" text they will be taken to the Create Account activity.
     * @param savedInstances
     */
    @Override
    protected void onCreate(Bundle savedInstances) {
        super.onCreate(savedInstances);
        setContentView(R.layout.activity_login_page);

        //USERNAME IS ACTUALLY EMAIL
        //TODO Update currentUser with logged in user

        loginBTN = findViewById(R.id.LOGIN_BTNlogin);
        usernameET = findViewById(R.id.LOGIN_ETusername);
        passwordET = findViewById(R.id.LOGIN_ETpassword);
        createAccountTV = findViewById(R.id.LOGIN_TVcreateAccount);

        db = FirebaseFirestore.getInstance();

        createAccountTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateAccount();
            }
        });

        loginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = usernameET.getText().toString().trim();
                String password = passwordET.getText().toString().trim();
                Log.e("USERNAME", "username is:" + username);
                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(LoginActivity.this, "Please enter a username!",
                            Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Please enter a password!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.signInWithEmailAndPassword(username, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("login Success", "signInWithEmail:success");
                                        user = mAuth.getCurrentUser();
                                        DocumentReference docRef = db.collection("users")
                                                .document(user.getUid());
                                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                DocumentSnapshot document = task.getResult();
                                                    nameN = document.getData().get("fname").toString() + " " +
                                                     document.getData().get("lname");
                                                    usernameN = document.getData().get("username").toString();
                                                    emailN = document.getData().get("email").toString();
                                                    phoneN = document.getData().get("phone").toString();
                                                    currentUser = new User(nameN, usernameN, emailN, phoneN);
                                            }
                                        });

                                        //Log.i("testingLog", usernameN);
                                        // currentUser = new User(nameN, usernameN, emailN, phoneN);
                                        //Log.i("testingLog", currentUser.getEmail().toString());

                                        openMainActivity();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("Login Failed", "signInWithEmail:failure", task.getException());
                                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    /**
     * Called after "new to Vivlio?" text is tapped on to open Create Account Activity
     */
    private void openCreateAccount(){
        Intent intent = new Intent(this, CreateAccountActivity.class);
        startActivity(intent);
    }

    /**
     * Called after authentication is successful to open the main page
     */
    private void openMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
