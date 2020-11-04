package com.example.vivlio;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class BorrowTaskActivity extends AppCompatActivity {
    ArrayAdapter<Book> bookAdapter;
    ArrayList<Book> bookDataList;
    String selectedISBN;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private CollectionReference collectionReference;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrowtask);

        ListView BookListLV;

        BookListLV = findViewById(R.id.BORROWT_LVbooks);

        bookDataList = new ArrayList<>();
        bookAdapter = new BorrowTaskCustomList(this, bookDataList);
        BookListLV.setAdapter(bookAdapter);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        String currentUID = mAuth.getCurrentUser().getUid();
        collectionReference = db.collection("users" + "/" + currentUID + "/requested");

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                bookDataList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    if (doc.getData().get("status").toString().equals("accepted")) {
                        ArrayList<String> owner = new ArrayList<>();
                        owner.add(doc.getData().get("owners").toString());

                        Book book = new Book(doc.getData().get("title").toString(),
                                doc.getData().get("author").toString(),
                                owner.get(0), doc.getId());
                        Log.e("ISB", doc.getId());
                        Log.e("title", doc.getData().get("title").toString());
                        bookDataList.add(book);
                    }
                }
                bookAdapter.notifyDataSetChanged();
            }
        });

        BookListLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedISBN = bookDataList.get(i).getISBN();
                Log.e("SELECTED BOOK", bookDataList.get(i).getTitle());
                openScanner(selectedISBN);

            }
        });
    }

    public void openScanner(String isbn){
        //Intent intent = new Intent(BorrowTaskActivity.this, Scanner.class);
        //intent.putExtra("BORROWER_ISBN", isbn);
        //startActivity(intent);
    }
}
