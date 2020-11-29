package com.example.vivlio.Activities;

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

import com.example.vivlio.Book;
import com.example.vivlio.CustomLists.BorrowTaskCustomList;
import com.example.vivlio.R;
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
    String otherUID;

    /**
     * Gets list of books that the current user has requested that have been accepted. Displays that
     * list of book with their title, author and owner. Once the user selects a book, the scanner will
     * open for them to scan the ISBN.
     * @param savedInstanceState
     */
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
                    Log.e("current book id", doc.getId());
                    if (!doc.getData().containsKey("blank")) {

                        Log.e("current book id", "in if blank");

                        if (doc.getData().get("status").toString().equals("accepted")) {

                            Log.e("current book id", "in if acc");

                            ArrayList<String> owner = new ArrayList<>();
                            owner.add(doc.getData().get("owners").toString());

                            Book book = new Book(doc.getData().get("title").toString(),
                                    doc.getData().get("author").toString(),
                                    owner.get(0), doc.getId().replace("-",""));
                            //Log.e("ISB", doc.getId());
                            //Log.e("title", doc.getData().get("title").toString());
                            bookDataList.add(book);
                        }
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
                Log.e("SELECTED BOOK ISBN", bookDataList.get(i).getISBN());
                openScanner();

            }
        });
    }

    /**
     * call this method after user taps on a book to open scanner to scan ISBN of book
     */
    public void openScanner(){
        Intent intent = new Intent(BorrowTaskActivity.this, BarcodeScannerActivity.class);
        startActivityForResult(intent, 0);
    }

    /**
     * once the user scans the ISBN, this method will check if it matches the ISBN of the book they
     * had selected. if it does, it will call the Success Exchange activity. otherwise the user will
     * be notified that their isbn does not match and they can select a book again.
     * @param requestCode default
     * @param resultCode default
     * @param data scanned isbn
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            String result = data.getStringExtra("isbn");
            Log.e("scanned isbn in task", result);
            if(selectedISBN.equals(result)){
                Intent intent = new Intent(BorrowTaskActivity.this, SuccessExchangeActivity.class);
                intent.putExtra("BORROWER", result);
                intent.putExtra("OTHER_UID", otherUID);
                startActivity(intent);
            } else {
                Toast.makeText(BorrowTaskActivity.this, "ISBN did not match selected book!",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
