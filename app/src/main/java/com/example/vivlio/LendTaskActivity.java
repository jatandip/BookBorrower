package com.example.vivlio;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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

public class LendTaskActivity extends AppCompatActivity{
    ArrayAdapter<Book> bookAdapter;
    ArrayList<Book> bookDataList;
    String selectedISBN;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private CollectionReference collectionReference;
    String otherUID;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lendtask);

        ListView BookListLV;

        BookListLV = findViewById(R.id.LENDT_LVbooks);
        bookDataList = new ArrayList<>();
        bookAdapter = new LendTaskCustomList(this, bookDataList);
        BookListLV.setAdapter(bookAdapter);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        String currentUID = mAuth.getCurrentUser().getUid();
        collectionReference = db.collection("users" + "/" + currentUID + "/owned");

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                bookDataList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    if (doc.getData().get("status").toString().equals("accepted")) {
                        ArrayList<String> requestedBy = new ArrayList<>();
                        requestedBy.add(doc.getData().get("requested by").toString());

                        Book book = new Book(doc.getData().get("title").toString(),
                                doc.getData().get("author").toString(),
                                requestedBy.get(0), doc.getId());
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
                otherUID = bookDataList.get(i).getOwner();
                Log.e("SELECTED BOOK", bookDataList.get(i).getTitle());
                openScanner();

            }
        });
    }

    public void openScanner(){
        Intent intent = new Intent(LendTaskActivity.this, BarcodeScannerActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            String result = data.getStringExtra("isbn");
            Log.e("scanned isbn in task", result);
            if(selectedISBN.equals(result)){
                Intent intent = new Intent(LendTaskActivity.this, SuccessExchangeActivity.class);
                intent.putExtra("LENDER", result);
                intent.putExtra("OTHER_UID", otherUID);
                startActivity(intent);
            }else {
                Toast.makeText(LendTaskActivity.this, "ISBN did not match selected book!",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
