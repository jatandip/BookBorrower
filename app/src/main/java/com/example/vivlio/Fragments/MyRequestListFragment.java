package com.example.vivlio.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.vivlio.Activities.RequestDetailActivity;
import com.example.vivlio.Book;
import com.example.vivlio.CustomLists.RequestCustomList;
import com.example.vivlio.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * MyRequestListFragment.java
 *
 * Fragment that displays a list of the User's requested books. The tab at the top can filter by
 * based on the status. User can see All, Requests with status Available, and Requests with status
 * Borrowed.
 *
 * */

public class MyRequestListFragment extends Fragment {

    private ListView listOfRequests;
    private ArrayList<Book> requestDataList;
    private RequestCustomList requestAdapter;
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
     * @return A new instance of fragment MyRequestList.
     */
    public static MyRequestListFragment newInstance() {
        MyRequestListFragment fragment = new MyRequestListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Boilerplate onCreate method
     * Would set parameters but there are none to set.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // no params
        }
    }

    /**
     * Boilerplate onCreateView method
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return created View
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_request_list, container, false);
    }


    /**
     * Handles the task of the MyRequestList.
     * Displays the list of the user's Requested books.
     * Tab at the top can filter by based on the status. User can see All, Requests with status
     * Available, and Requests with status Borrowed.
     *
     * The content in the list contains the book title, author, owner, and status.
     *
     * @param view
     * @param savedInstanceBundle
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceBundle) {
        listOfRequests = view.findViewById(R.id.request_list_view);
        TabLayout tabBar = view.findViewById(R.id.tab_bar);

        // set up firebase access
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        final String uid = mAuth.getCurrentUser().getUid();
        collectionReference = db.collection("users/" + uid + "/requested");

        // set up ListView
        requestDataList = new ArrayList<>();
        requestAdapter = new RequestCustomList(getActivity(), requestDataList);
        listOfRequests.setAdapter(requestAdapter);

        // start by showing all (user will start on All tab)
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                requestDataList.clear();
                for (QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                    if (!doc.getData().containsKey("blank")) {
                        ArrayList<String> owner = (ArrayList<String>) doc.getData().get("owners");
                        if (owner.isEmpty()) {
                            owner.add(mAuth.getCurrentUser().getUid());
                        }

                        String photoPath = null;
                        if (doc.getData().get("path") != null) {
                            photoPath = doc.getData().get("path").toString();
                        }

                        Book book = new Book(doc.getData().get("title").toString(),
                                doc.getData().get("author").toString(),
                                doc.getId(),
                                doc.getData().get("status").toString(),
                                owner.get(0),
                                owner.get(0),
                                photoPath);

                        requestDataList.add(book);
                    }
                }
                requestAdapter.notifyDataSetChanged();
            }
        });

        // filter books by tab
        tabBar.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                            requestDataList.clear();

                            for (QueryDocumentSnapshot doc: queryDocumentSnapshots)
                            {

                                if (!doc.getData().containsKey("blank")) {
                                    ArrayList<String> owner = (ArrayList<String>) doc.getData().get("owners");
                                    if (owner.isEmpty()) {
                                        owner.add(mAuth.getCurrentUser().getUid());
                                    }

                                    String photoPath = null;
                                    if (doc.getData().get("path") != null) {
                                        photoPath = doc.getData().get("path").toString();
                                    }

                                    Book book = new Book(doc.getData().get("title").toString(),
                                            doc.getData().get("author").toString(),
                                            doc.getId(),
                                            doc.getData().get("status").toString(),
                                            owner.get(0),
                                            owner.get(0),
                                            photoPath);

                                    requestDataList.add(book);
                                }
                            }
                            requestAdapter.notifyDataSetChanged();

                        }
                    });
                }

                if (tab.getPosition() == 1) {
                    collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                            requestDataList.clear();

                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                if (!doc.getData().containsKey("blank")) {
                                    if (doc.getData().get("status").toString().equals("accepted")) {

                                        ArrayList<String> owner = (ArrayList<String>) doc.getData().get("owners");
                                        if (owner.isEmpty()) {
                                            owner.add(mAuth.getCurrentUser().getUid());
                                        }

                                        String photoPath = null;
                                        if (doc.getData().get("path") != null) {
                                            photoPath = doc.getData().get("path").toString();
                                        }

                                        Book book = new Book(doc.getData().get("title").toString(),
                                                doc.getData().get("author").toString(),
                                                doc.getId(),
                                                doc.getData().get("status").toString(),
                                                owner.get(0),
                                                owner.get(0),
                                                photoPath);

                                        requestDataList.add(book);
                                    }
                                }
                            }
                            requestAdapter.notifyDataSetChanged();
                        }
                    });
                }

                if (tab.getPosition() == 2) {
                    collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                            requestDataList.clear();

                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                if (!doc.getData().containsKey("blank")) {
                                    if (doc.getData().get("status").toString().equals("borrowed")) {

                                        ArrayList<String> owner = (ArrayList<String>) doc.getData().get("owners");
                                        if (owner.isEmpty()) {
                                            owner.add(mAuth.getCurrentUser().getUid());
                                        }

                                        String photoPath = null;
                                        if (doc.getData().get("path") != null) {
                                            photoPath = doc.getData().get("path").toString();
                                        }

                                        Book book = new Book(doc.getData().get("title").toString(),
                                                doc.getData().get("author").toString(),
                                                doc.getId(),
                                                doc.getData().get("status").toString(),
                                                owner.get(0),
                                                uid,
                                                photoPath);

                                        requestDataList.add(book);
                                    }
                                }
                            }
                            requestAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });



        // launch into Request Detail Activity when book selected
        listOfRequests.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book selected = (Book) listOfRequests.getItemAtPosition(position);

                Intent intent = new Intent(MyRequestListFragment.this.getActivity(), RequestDetailActivity.class);
                intent.putExtra("book", selected);
                startActivity(intent);
            }
        });




    }

}