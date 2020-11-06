package com.example.vivlio;

//testing//
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import static android.content.ContentValues.TAG;

/**
 * A Fragment for the profile
 * Gets the users profile information and displays it
 * If the user decides they want to edit their profile information
 * Leads to the EditProfile Activity
 * If the user changes their information we change the information in the firebase database
 */
public class ProfileFragment extends Fragment {

    private TextView nameTextView;
    private TextView usernameTextView;
    private TextView emailTextView;
    private TextView phoneNumTextView;
    private FloatingActionButton editProfile;
    private ArrayList<String> profileInfo;
    private User user;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * General OnCreate method
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    /**
     * Handles the result of the EditProfile activity
     * If changes need to be made it will do so in the firestore
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            if(resultCode == Activity.RESULT_OK){
                ArrayList<String> result=data.getStringArrayListExtra("result");

                phoneNumTextView.setText(result.get(0));
                emailTextView.setText(result.get(1));

                //user.setPhonenumber(result.get(0));
                //user.setEmail(result.get(1));

                LoginActivity.currentUser.setPhonenumber(result.get(0));
                LoginActivity.currentUser.setEmail(result.get(1));



                db = FirebaseFirestore.getInstance();

                mAuth = FirebaseAuth.getInstance();
                FirebaseUser Curruser = mAuth.getCurrentUser();


                DocumentReference userRef = db.collection("users").document(Curruser.getUid());
                userRef
                        .update("phone", LoginActivity.currentUser.getPhonenumber())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully updated!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error updating document", e);
                            }
                        });

                DocumentReference userRef2 = db.collection("users").document(Curruser.getUid());
                userRef2
                        .update("email", LoginActivity.currentUser.getEmail())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully updated!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error updating document", e);
                            }
                        });
            }

            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//


    /**
     * Inflates the view
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }


    /**
     * Gets and loads the profile information
     * Handles the edit button click and passing in the profile information to
     * EditProfile, starts the EditProfile activity
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
                    nameTextView = view.findViewById(R.id.nameView);
                    usernameTextView = view.findViewById(R.id.usernameView);
                    phoneNumTextView = view.findViewById(R.id.mobileView);
                    emailTextView = view.findViewById(R.id.emailView);
                    editProfile = view.findViewById(R.id.editButton);

                    Intent intent = getActivity().getIntent();
                    user = (User) intent.getSerializableExtra("User");

                    mAuth = FirebaseAuth.getInstance();
                    FirebaseUser currUser = mAuth.getCurrentUser();

                    //LoginActivity.currentUser.getName();


                    Log.i("is empty? ", LoginActivity.currentUser.getName().toString());
                    nameTextView.setText( LoginActivity.currentUser.getName());
                    usernameTextView.setText( LoginActivity.currentUser.getUsername());
                    phoneNumTextView.setText(LoginActivity.currentUser.getPhonenumber());
                    emailTextView.setText(LoginActivity.currentUser.getEmail());

                    editProfile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ArrayList<String> userInfo = new ArrayList<String>();

                            Intent editIntent = new Intent(ProfileFragment.this.getActivity(), EditProfileActivity.class);

                            userInfo.add(LoginActivity.currentUser.getEmail());
                            userInfo.add(LoginActivity.currentUser.getPhonenumber());

                editIntent.putStringArrayListExtra("userInfo",userInfo);
                startActivityForResult(editIntent, 0);

            }
        });

    }


}