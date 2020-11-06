package com.example.vivlio;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class SuccessExchangeActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private CollectionReference collectionReference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_exchange);

        final ProgressBar progressBarPB = findViewById(R.id.SUCEX_PBloading);
        final ImageButton homeIB = findViewById(R.id.SUCEX_IBhome);
        final TextView successTV = findViewById(R.id.SUCEX_TVsuccess);
        final TextView staySafeTV = findViewById(R.id.SUCEX_TVstaysafe);
        final TextView homeTV = findViewById(R.id.SUCEX_TVhome);

        Boolean isBorrower;
        final String myISBN;
        final String otherUID;

        final Bundle extras = getIntent().getExtras();
        if(extras.get("BORROWER") == null){
            isBorrower = false;
            myISBN = extras.getString("LENDER");
        } else {
            isBorrower = true;
            myISBN = extras.getString("BORROWER");
        }
        otherUID = extras.getString("OTHER_UID").substring(1, extras.getString("OTHER_UID").length()-1);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        //collectionReference = db.collection("users" + "/" + otherUID);

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

                        successTV.setVisibility(View.VISIBLE);
                        staySafeTV.setVisibility(View.VISIBLE);
                        homeIB.setVisibility(View.VISIBLE);
                        homeTV.setVisibility(View.VISIBLE);
                        progressBarPB.setVisibility(View.INVISIBLE);

                        DocumentReference updateUser = db.collection("users")
                                .document(mAuth.getCurrentUser().getUid());
                        updateUser.update("scanned isbn", "");

                    } else if (otherISBN == ""){
                        Log.e("empty", "empty");
                        Log.e("otherisbn2 ",otherISBN);

                    } else if (myISBN.equals(otherISBN) == false){
                        Log.e("diff", "diff");
                        Log.e("otherisbn3 ",otherISBN.toString());

                        successTV.setText("Exchange failed!");
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

    public void openHome(){
        Intent intent = new Intent(SuccessExchangeActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
