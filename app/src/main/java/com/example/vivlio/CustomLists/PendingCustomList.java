package com.example.vivlio.CustomLists;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.vivlio.Activities.LocationActivity;
import com.example.vivlio.Activities.Mybook_Pending;
import com.example.vivlio.R;
import com.example.vivlio.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentSnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * CustomList for displaying all the the people who want to borrow the users books
 * Displays the name and the username of the potential borrower
 * User can accept their request and the database will change the status
 * Of the book to pending
 * The rest of the users who wanted to borrow the users will no longer be able too and
 * Get cleared from the customList
 */



public class PendingCustomList extends ArrayAdapter<User> {

    public ArrayList<User> users;
    public Context context;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    public FirebaseUser firebaseUser;
    private int position;


    public PendingCustomList(Context context, ArrayList<User> users) {
        super(context,0,users);
        this.users = users;
        this.context = context;
    }


    /**
     * General getView method that sets up and displays the name and username
     * Also sets up the accept button so that when the user clicks on accept
     * The firebase will update with the new information and the customlist will
     * get cleared
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        DecimalFormat priceFormat = new DecimalFormat("0.00");
        View view = convertView;
        //if the view is null create and new view and inflate it
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.pending_content, parent, false);

        }

        final User user = users.get(position);
        Log.i("user", String.valueOf(user.getName()));

        ImageButton decline = view.findViewById(R.id.declineButtonPending);
        ImageButton accept = view.findViewById(R.id.acceptButtonPending);


        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser Firebaseuser = mAuth.getCurrentUser();
        final String uid = Firebaseuser.getUid();

        firebaseUser = mAuth.getCurrentUser();



        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Log.i("uid", user.getIsbn());

                ArrayList<String> updated = new ArrayList<String>();
                updated.add(user.getBorrower());

                db.collection("users").document(uid + "/owned/" + user.getIsbn())
                        .update(
                                "borrowers", updated,
                                "status", "accepted"
                        );

                Log.i("owner", uid);
                Log.i("borrower", user.getBorrower());


                Log.i("tag","users/" + user.getBorrower() + "/requested/" + user.getIsbn());
                db.collection("users").document(user.getBorrower() + "/requested/" + user.getIsbn())
                        .update("status", "accepted");



                Mybook_Pending.bookDataList.clear();
                //Mybook_Pending.bookDataList.add(user);
                Mybook_Pending.bookAdapter.notifyDataSetChanged();



                String borr = user.getBorrower();
                Bundle bundle = new Bundle();
                bundle.putInt("check",0);
                bundle.putDouble("lat",1);
                bundle.putDouble("long",1);
                bundle.putString("borrower", borr);
                bundle.putString("isbn" , user.getIsbn());
                Intent intent = new Intent();
                intent.putExtras(bundle);
                intent.setClass(context, LocationActivity.class);
                context.startActivity(intent);




            }
        });

        decline.setTag(position);


        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                DocumentReference docRef = db.collection("users")
                        .document(uid + "/owned/" + user.getIsbn());
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        DocumentSnapshot document = task.getResult();
                        ArrayList<String> borrowers = (ArrayList<String>) document.getData().get("borrowers");

                        borrowers.remove(user.getBorrower());
                        Log.i("borrowers", String.valueOf(borrowers));


                        db.collection("users").document(uid + "/owned/" + user.getIsbn())
                                .update(
                                        "borrowers", borrowers,
                                        "status", document.getData().get("status").toString()
                                );


                        db.collection("users").document(user.getBorrower() + "/requested/" + user.getIsbn())
                                .delete();


                        Mybook_Pending.bookDataList.remove(position);
                        Mybook_Pending.bookAdapter.notifyDataSetChanged();


                        if (borrowers.isEmpty()) {
                            db.collection("users").document(uid + "/owned/" + user.getIsbn())
                                    .update(
                                            "status", "available"
                                    );
                        }

                    }

                });
            }


        });




        TextView author = view.findViewById(R.id.pendingNameView);
        TextView title = view.findViewById(R.id.usernamePendingView);
        author.setText(user.getName());
        title.setText(user.getUsername());
        return view;
    }

}