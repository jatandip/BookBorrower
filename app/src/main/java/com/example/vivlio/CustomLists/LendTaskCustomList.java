package com.example.vivlio.CustomLists;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.vivlio.Book;
import com.example.vivlio.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;

public class LendTaskCustomList extends ArrayAdapter<Book>{
    public ArrayList<Book> books;
    public Context context;
    private FirebaseFirestore db;
    public String ownerName;

    public LendTaskCustomList(Context context, ArrayList<Book> books) {
        super(context, 0, books);
        this.books = books;
        this.context = context;
    }

    /**
     * Sets up custom list for lenderTask with the book's title, author and requester.
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        //if the view is null create and new view and inflate it
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.lendtask_list, parent, false);
        }

        db = FirebaseFirestore.getInstance();
        final Book book = books.get(position);

        Log.e("lender name", book.getOwner());

//978014384948
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

                TextView titleTV = finalView.findViewById(R.id.LTL_TVtitle);
                TextView authorTV = finalView.findViewById(R.id.LTL_TVauthor);
                TextView ownerTV = finalView.findViewById(R.id.LTL_TVborrower);

                String ownerText = "Requested by: " + ownerName;

                Log.e("TITLE", book.getTitle());
                Log.e("Auth", book.getAuthor());
                Log.e("owner", ownerText);

                titleTV.setText(book.getTitle());
                authorTV.setText(book.getAuthor());
                ownerTV.setText(ownerText);
            }



        });



        //return the view
        return view;
    }
}
