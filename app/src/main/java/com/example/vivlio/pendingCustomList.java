package com.example.vivlio;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class pendingCustomList extends ArrayAdapter<User> {

    public ArrayList<User> users;
    public Context context;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    public FirebaseUser firebaseUser;


    public pendingCustomList( Context context, ArrayList<User> users) {
        super(context,0,users);
        this.users = users;
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        DecimalFormat priceFormat = new DecimalFormat("0.00");
        View view = convertView;
        //if the view is null create and new view and inflate it
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.pending_content, parent, false);

        }

        final User user = users.get(position);
        Log.i("user", String.valueOf(user.getName()));

        Button decline = view.findViewById(R.id.declineButtonPending);
        Button accept = view.findViewById(R.id.acceptButtonPending);


        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser Firebaseuser = mAuth.getCurrentUser();
        final String uid = Firebaseuser.getUid();
        //collectionReference = db.collection("users" + "/" + uid + "/owned/" + book.getISBN());

        firebaseUser = mAuth.getCurrentUser();



        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Log.i("uid", user.getIsbn());

                ArrayList<String> updated = new ArrayList<String>();
                updated.add(user.getBorrower());

                db.collection("users").document(uid + "/owned/" + user.getIsbn())
                        .update(
                                "borrowers", updated
                        );


                db.collection("users").document(user.getBorrower() + "/requested/" + user.getIsbn())
                        .update("status", "accepted");

            }
        });







        TextView author = view.findViewById(R.id.pendingNameView);
        TextView title = view.findViewById(R.id.usernamePendingView);
        author.setText(user.getName());
        title.setText(user.getUsername());
        return view;
    }

}