package com.example.vivlio.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vivlio.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.MetadataChanges;

public class SuccessExchangeActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private CollectionReference collectionReference;
    private FirebaseAuth mAuth;

    /**
     * Gets the ISBN that the user scanned as well as the UID of the user that the exchange is being
     * done with. checks the database to see if the scanned isbns match. if they do, the exchange
     * is successful and the appropriate message is displayed, along iwth a button to take the user
     * to the home page. The status of the books in the database will also be updated from
     * "accepted" to "borrowed"
     * If the scanned ISBNs do not match, the appropriate message is displayed, along iwth a button
     * to take the user to the home page.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_exchange);

        final ProgressBar progressBarPB = findViewById(R.id.SUCEX_PBloading);
        final ImageButton homeIB = findViewById(R.id.SUCEX_IBhome);
        final TextView successTV = findViewById(R.id.SUCEX_TVsuccess);
        final TextView staySafeTV = findViewById(R.id.SUCEX_TVstaysafe);
        final TextView homeTV = findViewById(R.id.SUCEX_TVhome);

        final Boolean isBorrower;
        final Boolean isReturner;
        final Boolean isReturn;
        final Boolean isExchange;
        final String myISBN;
        final String otherUID;

        final Bundle extras = getIntent().getExtras();
        if(extras.get("BORROWER") != null){
            isBorrower = true;
            isReturner = false;
            isReturn = false;
            isExchange = true;
            myISBN = extras.getString("BORROWER");
        } else if(extras.get("LENDER") != null) {
            isExchange = true;
            isReturn = false;
            isBorrower = false;
            isReturner = false;
            myISBN = extras.getString("LENDER");
        } else if(extras.get("RETURNER") != null) {
            isReturn = true;
            isBorrower = false;
            isExchange = false;
            isReturner = true;
            myISBN = extras.getString("RETURNER");
        } else {
            isBorrower = false;
            isReturner = false;
            isExchange = false;
            isReturn = true;
            myISBN = extras.getString("RECIEVER");
        }
        otherUID = extras.getString("OTHER_UID").substring(1, extras.getString("OTHER_UID").length()-1);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        DocumentReference updateUser = db.collection("users")
                .document(mAuth.getCurrentUser().getUid());
        updateUser.update("scanned isbn", myISBN);

        Log.e("other user",otherUID);

        DocumentReference docRef = db.collection("users")
                .document(otherUID);
        docRef.addSnapshotListener(MetadataChanges.INCLUDE, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String otherISBN = value.getString("scanned isbn");
                    if(myISBN.equals(otherISBN)){
                        Log.e("otherisbn1 ",otherISBN);
                        Log.e("equals", "equals");

                        if(isReturn) {
                            successTV.setText("Return Successful!");
                        }
                        successTV.setVisibility(View.VISIBLE);
                        staySafeTV.setVisibility(View.VISIBLE);

                        homeIB.setVisibility(View.VISIBLE);
                        homeTV.setVisibility(View.VISIBLE);
                        progressBarPB.setVisibility(View.INVISIBLE);

                        DocumentReference updateUser = db.collection("users")
                                .document(mAuth.getCurrentUser().getUid());
                        updateUser.update("scanned isbn", "");

                        //TODO CHANGE FOR BORROWER "accepted" > "borrowed"
                        if(isBorrower){
                            db.collection("users")
                                    .document(mAuth.getCurrentUser().getUid() +
                                    "/requested/" + myISBN.substring(0,3) + "-" + myISBN.substring(3))
                            .update("status","borrowed");

                        } else if (isExchange) {
                            //TODO CHANGE FOR OWNER "accepted" > "borrowed"
                            db.collection("users")
                                    .document(mAuth.getCurrentUser().getUid() +
                                            "/owned/" + myISBN.substring(0,3) + "-" + myISBN.substring(3))
                                    .update("status","borrowed");
                        } else if (isReturner) {
                            //TODO for returner delete from requested
                            db.collection("users")
                                    .document(mAuth.getCurrentUser().getUid() +
                                            "/requested/" + myISBN.substring(0,3) + "-" + myISBN.substring(3))
                                    .delete();
                        } else {
                            //TODO for owner remove borrower and change status borrowed > available
                            db.collection("users")
                                    .document(mAuth.getCurrentUser().getUid() +
                                            "/owned/" + myISBN.substring(0,3) + "-" + myISBN.substring(3))
                                    .update("status","available");
                            db.collection("users")
                                    .document(mAuth.getCurrentUser().getUid() +
                                            "/owned/" + myISBN.substring(0,3) + "-" + myISBN.substring(3))
                                    .update("borrowers","");
                        }

                    } else if (otherISBN == ""){
                        Log.e("empty", "empty");
                        Log.e("otherisbn2 ",otherISBN);

                    } else if (myISBN.equals(otherISBN) == false){
                        Log.e("diff", "diff");
                        Log.e("otherisbn3 ",otherISBN);

                        if(isExchange) {
                            successTV.setText("Exchange failed!");
                        } else {
                            successTV.setText("Return failed!");
                        }
                        staySafeTV.setText("Please try again!");

                        successTV.setVisibility(View.VISIBLE);
                        staySafeTV.setVisibility(View.VISIBLE);
                        homeIB.setVisibility(View.VISIBLE);
                        homeTV.setVisibility(View.VISIBLE);
                        progressBarPB.setVisibility(View.INVISIBLE);

                        DocumentReference updateUser = db.collection("users")
                                .document(mAuth.getCurrentUser().getUid());
                        updateUser.update("scanned isbn", "");
                    }
                }
        });

        homeIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHome();
            }
        });
    }

    /**
     * called after the user taps on the home button to take the user to the main page.
     */
    public void openHome(){
        Intent intent = new Intent(SuccessExchangeActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
