package com.example.vivlio;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddBook extends AppCompatActivity {
    private static final String TAG = "AddBook";
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int GALLERY_REQUEST_CODE = 2;
    private static final int CAMERA_PERM_CODE = 3;


    private EditText titleEditText;
    private EditText authorEditText;
    private EditText ISBNEditText;
    private Button galleryPictureButton;
    private Button cameraPictureButton;
    private Button uploadButton;
    private ImageView bookImageView;
    private String currentPath;
    private Uri uri;
    private Book book;

    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate started");
        titleEditText = findViewById(R.id.edit_title);
        authorEditText = findViewById(R.id.edit_author);
        ISBNEditText = findViewById(R.id.edit_isbn);
        galleryPictureButton = findViewById(R.id.button_choose_image);
        cameraPictureButton = findViewById(R.id.button_camera_image);
        uploadButton = findViewById(R.id.button_upload);
        bookImageView = findViewById(R.id.image_view);

        storageReference = FirebaseStorage.getInstance().getReference();

        galleryPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileChooser();
            }
        });

        cameraPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCameraPermission();
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadButtonPressed();
            }
        });
    }
    private void fileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    private void uploadFile(String name, Uri uri){
        final StorageReference image = storageReference.child("pictures/" + name);
        image.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("tag", "onSuccess: Uploaded Image URl is " + uri.toString());
                    }
                });

                Toast.makeText(AddBook.this, "Image Is Uploaded.", Toast.LENGTH_SHORT).show();
                Task<Uri> downloadUri = taskSnapshot.getStorage().getDownloadUrl();
                if (downloadUri.isSuccessful()){
                    currentPath = downloadUri.getResult().toString();


                    //determine if user requires image to upload, otherwise implement setter outside



                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddBook.this, "Upload Failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private String getFileExt(Uri uri) {
        ContentResolver c = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(uri));
    }

    private void getCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        }else{
            takePicture();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CAMERA_PERM_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                takePicture();
            }else {
                Toast.makeText(this, "You must allow camera usage to use this function", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void takePicture(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null){
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPath = image.getAbsolutePath();
        return image;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                File f = new File(currentPath);
                bookImageView.setImageURI(Uri.fromFile(f));
                Log.d("tag", "Url for image is " + Uri.fromFile(f));

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);
                uploadFile(f.getName(), contentUri);
            }
        }
        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri contentUri = data.getData();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "." + getFileExt(contentUri);
                Log.d("tag", "onActivityResult Uri:  " + imageFileName);
                bookImageView.setImageURI(contentUri);
                uploadFile(imageFileName, contentUri);
            }
        }
    }

    public void uploadButtonPressed(){
        book = new Book();
        String title, author, ISBN;

        //get current user to assign book
        title = titleEditText.getText().toString();
        author = authorEditText.getText().toString();
        ISBN = ISBNEditText.getText().toString();

        if (!title.isEmpty() && !author.isEmpty() && !ISBN.isEmpty()) {
            book.setAuthor(author);
            book.setTitle(title);
            book.setISBN(ISBN);
            book.setPhotoURL(currentPath);
            Intent intent = new Intent();
            intent.putExtra("Title", title);
            intent.putExtra("Author", author);
            intent.putExtra("ISBN", ISBN);
            intent.putExtra("photoURL", currentPath);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Toast.makeText(this, "Missing fields required", Toast.LENGTH_SHORT).show();
        }
    }

}
