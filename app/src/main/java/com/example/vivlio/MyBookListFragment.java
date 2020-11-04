package com.example.vivlio;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class MyBookListFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TabLayout tabBar;
    private ListView listofBooks;
    private FloatingActionButton add;
    private FirebaseFirestore db;
    private ArrayAdapter<Book> bookAdapter;
    private ArrayList<Book> bookDataList;
    private FirebaseAuth mAuth;
    private int position;
    private CollectionReference collectionReference;
    private TabLayout taby;

    private String mParam1;
    private String mParam2;

    public MyBookListFragment() {
    }

    public static MyBookListFragment newInstance(String param1, String param2) {
        MyBookListFragment fragment = new MyBookListFragment();
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
        return inflater.inflate(R.layout.fragment_my_book_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        tabBar = view.findViewById(R.id.tabBar);
        listofBooks = view.findViewById(R.id.bookListView);
        add = view.findViewById(R.id.addBtn);
        taby = view.findViewById(R.id.tabBar);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser Firebaseuser = mAuth.getCurrentUser();
        final String uid = Firebaseuser.getUid();
        collectionReference = db.collection("users" + "/" + uid + "/owned" );

        bookDataList = new ArrayList<>();
        bookAdapter = new BookList(getActivity(),bookDataList);
        listofBooks.setAdapter(bookAdapter);


        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                bookDataList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    ArrayList<String> borrowers = (ArrayList<String>) doc.getData().get("borrowers");
                    if (!borrowers.get(0).equals("")) {
                        String currentOwner = borrowers.get(0);
                        Book book = new Book(doc.getData().get("title").toString(), doc.getData().get("author").toString(), doc.getId(), doc.getData().get("status").toString(), uid, currentOwner, "link");
                        bookDataList.add(book);
                    }else {
                        Book book = new Book(doc.getData().get("title").toString(), doc.getData().get("author").toString(), doc.getId(), doc.getData().get("status").toString(), uid, uid, "link");
                        bookDataList.add(book);
                    }
                }
                bookAdapter.notifyDataSetChanged();
            }
        });


        taby.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                                FirebaseFirestoreException error) {
                            bookDataList.clear();
                            for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                            {

                                ArrayList<String> borrowers = (ArrayList<String>) doc.getData().get("borrowers");
                                if (!borrowers.get(0).equals("")) {
                                    String currentOwner = borrowers.get(0);
                                    Book book = new Book(doc.getData().get("title").toString(), doc.getData().get("author").toString(), doc.getId(), doc.getData().get("status").toString(), uid, currentOwner, "link");
                                    bookDataList.add(book);
                                }else {
                                    Book book = new Book(doc.getData().get("title").toString(), doc.getData().get("author").toString(), doc.getId(), doc.getData().get("status").toString(), uid, uid, "link");
                                    bookDataList.add(book);
                                }
                            }
                            bookAdapter.notifyDataSetChanged();
                        }
                    });
                }

                if (tab.getPosition() == 1) {
                    collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                                FirebaseFirestoreException error) {
                            bookDataList.clear();
                            for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                            {

                                if (doc.getData().get("status").toString().equals("accepted")) {
                                    Book book = new Book(doc.getData().get("title").toString(), doc.getData().get("author").toString(), doc.getId(), doc.getData().get("status").toString(), uid, uid, "link");
                                    bookDataList.add(book);
                                }

                            }
                            bookAdapter.notifyDataSetChanged();
                        }
                    });
                }

                if (tab.getPosition() == 2) {
                    collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                                FirebaseFirestoreException error) {
                            bookDataList.clear();
                            for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                            {
                                if (doc.getData().get("status").toString().equals("available")) {
                                    Book book = new Book(doc.getData().get("title").toString(), doc.getData().get("author").toString(), doc.getId(), doc.getData().get("status").toString(), uid, uid, "link");
                                    bookDataList.add(book);
                                }
                            }
                            bookAdapter.notifyDataSetChanged();
                        }
                    });
                }


                if (tab.getPosition() == 3) {
                    collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                                FirebaseFirestoreException error) {
                            bookDataList.clear();
                            for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                            {
                                if (doc.getData().get("status").toString().equals("pending")) {
                                    Book book = new Book(doc.getData().get("title").toString(), doc.getData().get("author").toString(), doc.getId(), doc.getData().get("status").toString(), uid, uid, "link");
                                    bookDataList.add(book);
                                }
                            }
                            bookAdapter.notifyDataSetChanged();
                        }
                    });
                }

                if (tab.getPosition() == 4) {
                    collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                                FirebaseFirestoreException error) {
                            bookDataList.clear();
                            for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                            {
                                ArrayList<String> borrowers = (ArrayList<String>) doc.getData().get("borrowers");
                                if (!borrowers.get(0).equals("")) {
                                    String currentOwner = borrowers.get(0);
                                    Book book = new Book(doc.getData().get("title").toString(), doc.getData().get("author").toString(), doc.getId(), doc.getData().get("status").toString(), uid, currentOwner, "link");
                                    bookDataList.add(book);
                                }
                            }
                            bookAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });




        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent addBook = new Intent(MyBookListFragment.this.getActivity(), AddBook.class);
                startActivityForResult(addBook, 0);


            }
        });


        listofBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Book selected = ((Book) listofBooks.getItemAtPosition(i));

                if (selected.getStatus().equals("available")) {
                    Intent editIntent = new Intent(MyBookListFragment.this.getActivity(), mybook_avalible.class);
                    editIntent.putExtra("book", selected);
                    startActivity(editIntent);
                }

                if (selected.getStatus().equals("borrowed")) {
                    Intent editIntent = new Intent(MyBookListFragment.this.getActivity(), mybook_borrowed.class);
                    editIntent.putExtra("book", selected);
                    startActivity(editIntent);
                }

                if (selected.getStatus().equals("accepted")) {
                    Intent editIntent = new Intent(MyBookListFragment.this.getActivity(), mybook_accepted.class);
                    editIntent.putExtra("book", selected);
                    startActivity(editIntent);
                }
            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                ArrayList<String> result = data.getStringArrayListExtra("result");

                String title = result.get(0);
                String author = result.get(1);
                String isbn = result.get(2);
                String currentpath = result.get(3);
                String status = result.get(4);


                Log.i("hello", result.get(0));

                db = FirebaseFirestore.getInstance();
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser Curruser = mAuth.getCurrentUser();

                DocumentReference docRef = db.collection("users")
                        .document(Curruser.getUid());


                //final CollectionReference collectionReference = db.collection("users/" + "jj1424" + "/owned/" + isbn);
                HashMap<String, Object> info = new HashMap<>();
                ArrayList<String> empty = new ArrayList<String>();

                empty.add("");
                info.put("borrowers", empty);
                GeoPoint location = new GeoPoint(0,0);
                info.put("location" , location);
                info.put("author", author);
                info.put("title", title);
                info.put("status", "available");
                info.put("path", currentpath);


                db.collection("users").document(Curruser.getUid() + "/" + "owned/" + isbn)
                        .set(info)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document", e);
                            }
                        });




                /*
                DocumentReference washingtonRef = db.collection("users").document(Curruser.getUid() + "/" + "owned/" + isbn);

                washingtonRef
                        .update("borrowers", empty);

                washingtonRef
                        .update("location", location);



                 */


            }
        }
    }
}