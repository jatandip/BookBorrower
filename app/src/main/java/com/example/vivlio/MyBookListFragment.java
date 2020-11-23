package com.example.vivlio;

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
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

/**
 * Activity that handles the Fragment that handles the users book list
 * Sets up the view using the onCreateView method
 * After the view is created handles loading the books into the customList
 * Depending on the tab that the user selects the activity will display a different set of books
 * If the user selects a book the activity will start a different activity that corresponds to the status of the book
 * The created activity may or may not return new information that we will update the customList with
 * User can also click on the add button to add a book from this fragment
 */


public class MyBookListFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TabLayout tabBar;
    private ListView listofBooks;
    private FloatingActionButton add;
    private FirebaseFirestore db;
    public static ArrayAdapter<Book> bookAdapter;
    public static ArrayList<Book> bookDataList;
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


    /**
     * General OnCreate method that gets the arguments
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
     * Inflates the View
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_book_list, container, false);
    }


    /**
     * After the view is created load the elements of the uml
     * Create and set the adapter and load the books into the adapter
     * Depending on the tab the user is in the adapter will store different set of books
     * Add button opens AddBook activity to allow the user to add a book
     * @param view
     * @param savedInstanceState
     */
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
                    if (!borrowers.isEmpty()) {
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
                                if (!borrowers.isEmpty()) {
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

                                ArrayList<String> borrowers = (ArrayList<String>) doc.getData().get("borrowers");
                                if (!borrowers.isEmpty() && doc.getData().get("status").toString().equals("accepted")) {
                                    String currentOwner = borrowers.get(0);
                                    Book book = new Book(doc.getData().get("title").toString(), doc.getData().get("author").toString(), doc.getId(), doc.getData().get("status").toString(), uid, currentOwner, "link");
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
                                    Log.e("status", "available");
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
                                if (!borrowers.isEmpty() && doc.getData().get("status").toString().equals("borrowed")) {
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
            public void onTabUnselected(TabLayout.Tab tab) {
                    if (tab.getPosition() == 0) {
                        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                                    FirebaseFirestoreException error) {
                                bookDataList.clear();
                                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                                {

                                    ArrayList<String> borrowers = (ArrayList<String>) doc.getData().get("borrowers");
                                    if (!borrowers.isEmpty()) {
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

                                    ArrayList<String> borrowers = (ArrayList<String>) doc.getData().get("borrowers");
                                    if (!borrowers.isEmpty() && doc.getData().get("status").toString().equals("accepted")) {
                                        String currentOwner = borrowers.get(0);
                                        Book book = new Book(doc.getData().get("title").toString(), doc.getData().get("author").toString(), doc.getId(), doc.getData().get("status").toString(), uid, currentOwner, "link");
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
                                    if (!borrowers.isEmpty() && doc.getData().get("status").toString().equals("borrowed")) {
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
            public void onTabReselected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                                FirebaseFirestoreException error) {
                            bookDataList.clear();
                            for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                            {

                                ArrayList<String> borrowers = (ArrayList<String>) doc.getData().get("borrowers");
                                if (!borrowers.isEmpty()) {
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

                                ArrayList<String> borrowers = (ArrayList<String>) doc.getData().get("borrowers");
                                if (!borrowers.isEmpty() && doc.getData().get("status").toString().equals("accepted")) {
                                    String currentOwner = borrowers.get(0);
                                    Book book = new Book(doc.getData().get("title").toString(), doc.getData().get("author").toString(), doc.getId(), doc.getData().get("status").toString(), uid, currentOwner, "link");
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
                                if (!borrowers.isEmpty() && doc.getData().get("status").toString().equals("borrowed")) {
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
                    Intent editIntent = new Intent(MyBookListFragment.this.getActivity(), Mybook_Avalible.class);
                    editIntent.putExtra("book", selected);
                    startActivity(editIntent);
                }

                if (selected.getStatus().equals("borrowed")) {
                    Intent editIntent = new Intent(MyBookListFragment.this.getActivity(), Mybook_Borrowed.class);
                    editIntent.putExtra("book", selected);
                    startActivity(editIntent);
                }

                if (selected.getStatus().equals("accepted")) {
                    Intent editIntent = new Intent(MyBookListFragment.this.getActivity(), Mybook_Accepted.class);
                    editIntent.putExtra("book", selected);
                    startActivity(editIntent);
                }

                if (selected.getStatus().equals("pending")) {
                    Intent editIntent = new Intent(MyBookListFragment.this.getActivity(), Mybook_Pending.class);
                    editIntent.putExtra("book", selected);
                    startActivity(editIntent);
                }



            }
        });

    }

    /**
     * If the user adds a book, the AddBook activity will
     * Return info using an intent, this method gets the information
     * From the intent, and creates a new book, and adds it to the firestore
     * @param requestCode
     * @param resultCode
     * @param data
     */

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

                db = FirebaseFirestore.getInstance();
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser Curruser = mAuth.getCurrentUser();

                DocumentReference docRef = db.collection("users")
                        .document(Curruser.getUid());

                HashMap<String, Object> info = new HashMap<>();
                HashMap<String, Object> BookCollectionInfo = new HashMap<>();
                ArrayList<String> empty = new ArrayList<String>();

                info.put("borrowers", empty);
                GeoPoint location = new GeoPoint(0,0);
                info.put("location" , location);
                info.put("author", author);
                info.put("title", title);
                info.put("status", "available");
                info.put("path", currentpath);



                //Log.i("current path", currentpath);



                ArrayList<String> Owner = new ArrayList<String>();
                Owner.add(Curruser.getUid());

                BookCollectionInfo.put("title", title);
                BookCollectionInfo.put("author", author);
                BookCollectionInfo.put("owners", Owner);

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



                db.collection("books").document(isbn)
                        .set(BookCollectionInfo)
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

            }
        }
    }
}