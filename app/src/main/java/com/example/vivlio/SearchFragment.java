package com.example.vivlio;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private EditText searchEditText;
    private Switch searchSwitch;
    private FloatingActionButton searchButton;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private ArrayAdapter<Book> resultAdapter;
    private ArrayList<Book> resultDataList;
    private ListView resultList;
    private CollectionReference bookCollection;
    private CollectionReference userCollection;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchEditText = view.findViewById(R.id.searchTermEditText);
        searchSwitch = view.findViewById(R.id.search_switch);
        searchButton = view.findViewById(R.id.search_button);
        resultList = view.findViewById(R.id.result_list_view);

        resultDataList = new ArrayList<>();
        resultAdapter = new BookList(getActivity(), resultDataList);
        resultList.setAdapter(resultAdapter);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();

        bookCollection = db.collection("books/");
        userCollection = db.collection("users/");


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean switchState = searchSwitch.isChecked(); //Checked = books, unchecked = users
                final String[] searchTerms = searchEditText.getText().toString().split("\\s+");
                Log.d("SEARCHING", "Initiated Search with Term: " + searchTerms[0]);

                if(switchState) {
                    bookCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            resultDataList.clear();
                            for(QueryDocumentSnapshot doc: value) {
                                String title = doc.getData().get("title").toString();
                                String author = doc.getData().get("author").toString();
                                Log.d("INFO", "Current title and author: " + title + author);
                                for(String term : searchTerms) {
                                    if (title.contains(term) || author.contains(term)) {
                                        ArrayList<String> owners = (ArrayList<String>) doc.getData().get("owners");
                                        Book resultBook;
                                        resultBook = new Book(title, author, doc.getId().toString(), owners);
                                        Log.d("POS_RESULT", resultBook.getTitle() + ", " + resultBook.getAuthor());
                                        if(!resultDataList.contains(resultBook)) {
                                            resultDataList.add(resultBook);
                                        }
                                    }
                                }
                            }
                            resultAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });

    }
}