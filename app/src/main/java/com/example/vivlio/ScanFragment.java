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

    public ScanFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scan, container, false);

    }

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

    public void openLending(){
        //Intent intent = new Intent(ScanFragment.this.getActivity(), LendingTaskActivity.class);
        //startActivity(intent);
    }

    public void openBorrowing(){
        //Intent intent = new Intent(ScanFragment.this.getActivity(), BorrowTaskActivity.class);
        //startActivity(intent);
    }
}