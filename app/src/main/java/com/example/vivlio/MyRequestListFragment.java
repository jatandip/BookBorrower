package com.example.vivlio;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/*
* Fragment that displays a list of the User's requested books. Selecting a book launches into an
* activity that displays the details of the request.
*
* Should the details be in a fragment?
*
* The some of the code for onViewCreated was made looking at MyBookListFragment - thanks William.
*
*
*
* */



/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyRequestListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyRequestListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayList<Book> requestDataList;
    private BookList requestAdapter;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private CollectionReference collectionReference;


    public MyRequestListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyRequestList.
     */
    // TODO: Rename and change types and number of parameters
    public static MyRequestListFragment newInstance(String param1, String param2) {
        MyRequestListFragment fragment = new MyRequestListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_request_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceBundle) {
        ListView listOfRequests = view.findViewById(R.id.request_list_view);

        // set up firebase access
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        final String uid = mAuth.getCurrentUser().getUid();
        collectionReference = db.collection("users/" + uid + "/requested");

        requestDataList = new ArrayList<>();
        requestAdapter = new BookList(getActivity(), requestDataList);
        listOfRequests.setAdapter(requestAdapter);

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                requestDataList.clear();
                for (QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    ArrayList<String> owner = new ArrayList<>();
                    owner.add(doc.getData().get("owners").toString());

                    Book book = new Book(doc.getData().get("title").toString(),
                            doc.getData().get("author").toString(),
                            doc.getId(),
                            doc.getData().get("status").toString(),
                            owner.get(0),
                            owner.get(0),
                            "link");

                    requestDataList.add(book);
                }
                requestAdapter.notifyDataSetChanged();
            }
        });





    }

}