package com.example.vivlio;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class ScanFragment extends Fragment {

    /**
     * Required empty public constructor
     */
    public ScanFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * inflates view with fragment_scan
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scan, container, false);

    }

    /**
     * opens Borrowing or Lending views depending on which button the user taps on
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        FloatingActionButton borrowFAB = view.findViewById(R.id.EXCHANGE_FABborrow);
        FloatingActionButton lendFAB = view.findViewById(R.id.EXCHANGE_FABlend);

        borrowFAB.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                openBorrowing();
            }
        });

        lendFAB.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                openLending();
            }
        });

    }

    /**
     * opens List of books that the current user has accepted the requests of
     */
    public void openLending(){
        Intent intent = new Intent(ScanFragment.this.getActivity(),LendTaskActivity.class);
        startActivity(intent);
    }

    /**
     * opens a list of books that the user has requested that has been accepted
     */
    public void openBorrowing(){
        Intent intent = new Intent(ScanFragment.this.getActivity(), BorrowTaskActivity.class);
        startActivity(intent);
    }
}