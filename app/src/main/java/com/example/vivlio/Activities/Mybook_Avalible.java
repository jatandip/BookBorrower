package com.example.vivlio.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vivlio.Book;
import com.example.vivlio.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Activity that handles available books that the user owns.
 * Loads the books information and allows the user to edit the information of the book.
 * If user edits the book the activity loads the new information in, also if user deletes the book then
 * it deletes the book from the database
 */

public class Mybook_Avalible extends AppCompatActivity {
    private TextView titleView;
    private TextView authorView;
    private TextView isbnView;
    private ImageButton editBtn;
    private ImageButton uploadButton;
    private Book book;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private Book updatedBook;
    private Boolean trigger = false;
    private ImageButton imageDlt;
    private ImageView image;
    private static final int GALLERY_REQUEST_CODE = 2;
    StorageReference storageReference;
    private static final String TAG = "Mybook_avalible";
    private String currentPath;
    /**
     * General OnCreate method that loads the books information and sets the information to be displayed
     * editBtn on click listener will lead to a new activity that allows the user to edit the books information. It
     * will also return the updated information if their was a change
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mybook_avalible);
        storageReference = FirebaseStorage.getInstance().getReference();

        Intent intent = getIntent();
        book = (Book) intent.getSerializableExtra("book");



        image = (ImageView)findViewById(R.id.imageAvailable);
        Picasso.with(Mybook_Avalible.this)
                .load(book.getPhotoURL()).into(image);


        Log.i("url", book.getPhotoURL());

        imageDlt = findViewById(R.id.deleteImageBtn);


        titleView = findViewById(R.id.titleView);
        authorView = findViewById(R.id.authorView);
        isbnView = findViewById(R.id.isbnView);
        editBtn = findViewById(R.id.editBtn);
        uploadButton = findViewById(R.id.uploadImageBtn);
        titleView.setText(book.getTitle());
        authorView.setText(book.getAuthor());
        isbnView.setText(book.getISBN());


        Log.i("isbn", book.getISBN());

        imageDlt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image.setImageResource(0);
                Log.i("isbn", book.getISBN());

                db = FirebaseFirestore.getInstance();
                mAuth = FirebaseAuth.getInstance();
                final FirebaseUser Firebaseuser = mAuth.getCurrentUser();

                String uid = Firebaseuser.getUid();

                Log.i("uid", uid);
                db.collection("users").document(uid + "/owned/" + book.getISBN())
                        .update(
                                "path", null
                        );
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileChooser();
            }
        });


        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editIntent = new Intent(Mybook_Avalible.this, EditBook.class);
                if (trigger == false) {
                    editIntent.putExtra("book", book);
                }
                else {
                    editIntent.putExtra("book", updatedBook);
                }
                startActivityForResult(editIntent, 0);

            }
        });

    }


    /**
     * OnActivityResult method that handles the information the editBook book returns
     * If the requestCode is 0 and is equal to Activity.RESULT_OK then it means
     * The books information was updated. So then we update the books information
     * In the database. If the resultCode is equal to Activity.RESULT_CANCELED then
     * no changes were made so we do nothing. If the resultCode is equal to 5, then
     * the user decided that they wanted to delete the book, so we delete the book
     * from the database
     * @param requestCode
     * @param resultCode
     * @param data
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                trigger = true;
                updatedBook  = (Book) data.getSerializableExtra("book");

                titleView.setText(updatedBook.getTitle());
                authorView.setText(updatedBook.getAuthor());
                isbnView.setText(updatedBook.getISBN());

                db = FirebaseFirestore.getInstance();
                mAuth = FirebaseAuth.getInstance();
                final FirebaseUser Firebaseuser = mAuth.getCurrentUser();
                String uid = Firebaseuser.getUid();

                Log.i("uid", uid);
                db.collection("users").document(uid + "/owned/" + updatedBook.getISBN())
                        .update(
                                "author", updatedBook.getAuthor(),
                                "title", updatedBook.getTitle()
                        );


                /*
                db.collection("users").document(uid + "/owned/")
                        .update(
                                book.getISBN(), updatedBook.getISBN()
                        );

                 */
            }
            if (resultCode == 5) {

                db = FirebaseFirestore.getInstance();
                mAuth = FirebaseAuth.getInstance();
                final FirebaseUser Firebaseuser = mAuth.getCurrentUser();

                String uid = Firebaseuser.getUid();

                db.collection("users").document(uid + "/owned/" + book.getISBN())
                        .delete();
                finish();

            }
        }
        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri contentUri = data.getData();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "." + getFileExt(contentUri);
                Log.d(TAG, "onActivityResult Uri:  " + imageFileName);
                image.setImageURI(contentUri);
                Log.d(TAG, ">>CHECKPOINT<<");
                uploadFile(imageFileName, contentUri);
            }
        }
    }

    /**
     * returns file extension type
     * @param uri
     * @return
     */
    private String getFileExt(Uri uri) {
        ContentResolver c = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(uri));
    }

    /**
     * After getting current picture, whether it be from a camera picture or from the gallery
     * and upload it to the Firebase storage. If successful, will return a message stating that
     * the image is uploaded, otherwise will display a message saying the upload has failed.
     * If the upload is successful, the currentPath (which is the link to get the picture) will be
     * updated with the result of the downloadUri
     * @param name
     * @param uri
     */
    private void uploadFile(final String name, final Uri uri) {
        final StorageReference images = storageReference.child("pictures/" + name);
        images.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                images.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("tag", "onSuccess: Uploaded Image URl is " + uri.toString());
                        currentPath = uri.toString();
                        book.setPhotoURL(currentPath);
                        Log.d("tag", "UPLOADED SETPHOTOURL" + currentPath);
                    }
                });
                Toast.makeText(Mybook_Avalible.this, "Image Is Uploaded.", Toast.LENGTH_SHORT).show();
                Task<Uri> downloadUri = taskSnapshot.getStorage().getDownloadUrl();
                if (downloadUri.isSuccessful()) {
                    Log.d("tag", "Url for image is " + "gs://vivlio-14a4a-appspot.com/pictures/" + name);
                    //determine if user requires image to upload, otherwise implement setter outside
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Mybook_Avalible.this, "Upload Failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Creates new intent and sets type to image/*, which allows for image/jpg, image/png
     * and image/gif receivers before starting the activity which will send the request
     * to open up the gallery
     */
    private void fileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }
}
