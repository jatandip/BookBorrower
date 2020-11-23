package com.example.vivlio;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class RecievingTaskCustomList extends ArrayAdapter<Book> {
    public ArrayList<Book> books;
    public Context context;
    private FirebaseFirestore db;
    public String ownerName;

    public RecievingTaskCustomList(Context context, ArrayList<Book> books) {
        super(context, 0, books);
        this.books = books;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.recievetask_list, parent, false);
        }

        db = FirebaseFirestore.getInstance();
        final Book book = books.get(position);

        DocumentReference docRef = db.collection("users")
                .document(book.getOwner().substring(1, book.getOwner().length()-1));
        final View finalView = view;
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                ownerName = document.getData().get("fname").toString() + " " +
                        document.getData().get("lname");
                Log.e("socument", document.getData().get("fname").toString());

                TextView titleTV = finalView.findViewById(R.id.RCTL_TVtitle);
                TextView authorTV = finalView.findViewById(R.id.RCTL_TVauthor);
                TextView ownerTV = finalView.findViewById(R.id.RCTL_TVowner);

                String ownerText = "Borrowed by: " + ownerName;

                Log.e("TITLE", book.getTitle());
                Log.e("Auth", book.getAuthor());
                Log.e("borrower", ownerText);

                titleTV.setText(book.getTitle());
                authorTV.setText(book.getAuthor());
                ownerTV.setText(ownerText);
            }



        });

        return view;

    }
}
