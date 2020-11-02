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
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyBookListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyBookListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TabLayout tabBar;
    private ListView listofBooks;
    private FloatingActionButton add;
    private FirebaseFirestore db;
    ArrayAdapter<Book> bookAdapter;
    ArrayList<Book> bookDataList;
    private FirebaseAuth mAuth;



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyBookListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyBookList.
     */
    // TODO: Rename and change types and number of parameters
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_book_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        tabBar = view.findViewById(R.id.tabBar);
        listofBooks = view.findViewById(R.id.bookListView);
        add = view.findViewById(R.id.addBtn);

        db = FirebaseFirestore.getInstance();


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser Firebaseuser = mAuth.getCurrentUser();

        final String uid = Firebaseuser.getUid();


        final CollectionReference collectionReference = db.collection("users" + "/" + uid + "/owned" );


        bookDataList = new ArrayList<>();
        bookAdapter = new BookList(getActivity(),bookDataList);

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {

                bookDataList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    User user = new User("NAME", "test", "EMAIL", "PHONE");
                    String city = doc.getId();
                    Log.i("asdasd", String. valueOf(doc.getData().get("author")));
                    Log.i("asdas", city);
                    Book book = new Book(doc.getData().get("title").toString(),doc.getData().get("author").toString(),doc.getId().toString(),doc.getData().get("status").toString(), user, user, "link");



                    //bookDataList.add(new Book());
                }
                bookAdapter.notifyDataSetChanged();
            }
        });

    }
}