package com.example.vivlio.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.example.vivlio.Models.Book;
import com.example.vivlio.R;
import com.example.vivlio.Controllers.ValidateISBN;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * This is the AddBook class; it allows user to enter book description, take a picture, open
 * their gallery to select a picture, and upload when all required fields (title, author,
 * ISBN) are filled out
 */
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
    private ImageButton uploadButton;
    private ImageButton scanButton;
    private ImageView bookImageView;
    private String currentPath;
    private Uri uri;
    private Book book;

    StorageReference storageReference;

    /**
     * Handles the task of AddBook, sets onclicklisteners for the following buttons:
     * galleryPictureButton, cameraPictureButton, and uploadButton
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        Log.d(TAG, "onCreate started");
        titleEditText = findViewById(R.id.edit_title);
        authorEditText = findViewById(R.id.edit_author);
        ISBNEditText = findViewById(R.id.edit_isbn);
        galleryPictureButton = findViewById(R.id.button_choose_image);
        cameraPictureButton = findViewById(R.id.button_camera_image);
        uploadButton = findViewById(R.id.button_upload);
        bookImageView = findViewById(R.id.image_view);
        scanButton = findViewById(R.id.button_scan);

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

        scanButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                scanButtonPressed();
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

    /**
     * After getting current picture, whether it be from a camera picture or from the gallery
     * and upload it to the Firebase storage. If successful, will return a message stating that
     * the image is uploaded, otherwise will display a message saying the upload has failed.
     * If the upload is successful, the currentPath (which is the link to get the picture) will be
     * updated with the result of the downloadUri
     * @param name
     * @param uri
     */
    private void uploadFile(final String name, final Uri uri){
        final StorageReference image = storageReference.child("pictures/" + name);
        image.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("tag", "onSuccess: Uploaded Image URl is " + uri.toString());
                        currentPath = uri.toString();
                    }
                });
                Toast.makeText(AddBook.this, "Image Is Uploaded.", Toast.LENGTH_SHORT).show();
                Task<Uri> downloadUri = taskSnapshot.getStorage().getDownloadUrl();
                if (downloadUri.isSuccessful()){
                    Log.d("tag", "Url for image is " + "gs://vivlio-14a4a-appspot.com/pictures/" + name);
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
     * If camera permission has been allowed, then take a picture by calling the function
     * takePicture, otherwise ask for permission to take picture
     */
    private void getCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.CAMERA},
                    CAMERA_PERM_CODE);
        }else{
            takePicture();
        }
    }

    /**
     * after getting/rejecting permission from getcamerapermission, checks if permission has been
     * granted. This will either open up the camera app by calling the function takePicture, or will
     * display a message telling the user that they must allow camera usage to use the function
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if(requestCode == CAMERA_PERM_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                takePicture();
            }else {
                Toast.makeText(this, "You must allow camera usage to use this function",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * function for taking a picture, creates a file, and sets a uri where the output will be
     * stored in the uri. The camera function is then called, and when returned, the uri should
     * contain the correct information
     */
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
                        "com.example.vivlio.provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

    /**
     * creates an image file with a timestamp, making the file a jpeg. When a picture is captured,
     * it will be put into the picture directory, and a temp file will be created and returned
     * @return
     * @throws IOException
     */
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

    /**
     * handles the result after a picture has been selected from either a camera taken picture, or
     * from the gallery. It then creates an intent to scan the file, before sending all relevant
     * information to the function uploadFile to upload
     * @param requestCode
     * @param resultCode
     * @param data
     */
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
        if (requestCode == 0){
            if (resultCode == Activity.RESULT_OK){
                String isbn = data.getStringExtra("isbn");
                String title = data.getStringExtra("title");
                String author = data.getStringExtra("author");
                ISBNEditText.setText(isbn);
                titleEditText.setText(title);
                authorEditText.setText(author);
                ISBNEditText.setFocusable(false);
                titleEditText.setFocusable(false);
                authorEditText.setFocusable(false);
                ISBNEditText.setEnabled(false);
                titleEditText.setEnabled(false);
                authorEditText.setEnabled(false);
            }
        }
    }

    /**
     * handles when the upload button is pressed. This checks if the required fields are filled out
     * (title, author, ISBN), but a picture is not required to proceed. If the fields are completed,
     * a book class is created, added in, and sent back with an intent containing all the
     * information. If the fields are not completed, the user will receive a message stating that
     * there are missing fields required, and will not proceed until they are filled out
     */
    private void uploadButtonPressed(){
        book = new Book();
        String title, author, ISBN;

        //get current user to assign book
        title = titleEditText.getText().toString();
        author = authorEditText.getText().toString();
        ISBN = ISBNEditText.getText().toString();

        if (!title.isEmpty() && !author.isEmpty() && !ISBN.isEmpty()) {
            ValidateISBN validator = new ValidateISBN();
            //Boolean bool = isISBN.verify(ISBN);
            //Boolean bool = true;
            Boolean isISBN = validator.verify(ISBN);
            if (isISBN) {
                String status = "available";
                Intent intent = new Intent();
                ArrayList<String> newInfo = new ArrayList<>();
                newInfo.add(title);
                newInfo.add(author);
                newInfo.add(ISBN);
                newInfo.add(currentPath);
                newInfo.add(status);
                intent.putStringArrayListExtra("result", newInfo);
                setResult(RESULT_OK, intent);
                finish();
            } else {
//                Toast.makeText(this, "Missing fields required", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Not a valid ISBN", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Missing fields required", Toast.LENGTH_SHORT).show();
        }
    }

    private void scanButtonPressed(){
        Intent scanBook = new Intent(this, BarcodeScannerActivity.class);
        startActivityForResult(scanBook, 0);
    }

}
