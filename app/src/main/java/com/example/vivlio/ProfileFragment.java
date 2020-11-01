package com.example.vivlio;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private TextView nameTextView;
    private TextView usernameTextView;
    private TextView emailTextView;
    private TextView phoneNumTextView;
    private FloatingActionButton editProfile;
    private ArrayList<String> profileInfo;
    private User user;

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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //final View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_profile, null);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            if(resultCode == Activity.RESULT_OK){
                ArrayList<String> result=data.getStringArrayListExtra("result");

                phoneNumTextView.setText(result.get(0));
                emailTextView.setText(result.get(1));
                user.setPhonenumber(result.get(0));
                user.setEmail(result.get(1));
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        nameTextView = view.findViewById(R.id.nameView);
        usernameTextView = view.findViewById(R.id.usernameView);
        phoneNumTextView = view.findViewById(R.id.mobileView);
        emailTextView = view.findViewById(R.id.emailView);
        editProfile = view.findViewById(R.id.editButton);


        Intent intent = getActivity().getIntent();
        user = (User) intent.getSerializableExtra("User");

        nameTextView.setText(user.getName());
        usernameTextView.setText(user.getUsername());
        phoneNumTextView.setText(user.getPhonenumber());
        emailTextView.setText(user.getEmail());

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> userInfo = new ArrayList<String>();

                Intent editIntent = new Intent(ProfileFragment.this.getActivity(), EditProfileActivity.class);

                userInfo.add(user.getEmail());
                userInfo.add(user.getPhonenumber());

                editIntent.putStringArrayListExtra("userInfo",userInfo);
                startActivityForResult(editIntent, 0);

            }
        });

    }


}