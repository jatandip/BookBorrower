package com.example.vivlio.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.vivlio.BookDetailFetcher;
import com.example.vivlio.R;
import com.example.vivlio.ValidateISBN;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.util.ArrayList;

/**
 * This activity class requests the phone's camera and
 * creates a preview screen for the camera. It detects
 * for any barcode from the camera and upon first detection
 * the data is saved in the intent and the activity is terminated.
 */
public class BarcodeScannerActivity extends AppCompatActivity {
    private BookDetailFetcher detailFetcher;
    private SurfaceView surfaceView;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int CAMERA_REQUEST_CODE = 100;
    private String barcodeData;
    private ValidateISBN validator;

    /**
     * ISBN is returned as an intent. To access the ISBN string
     * use Intent.getStringExtra("isbn")
     */
    private Intent returnedData;

    /**
     * Initialize all resources and attributes upon the activity's creation
     * @param savedInstanceState Saved state of the app
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);

        /**
         * SurfaceView is used because it can create a drawing surface
         * on a separate thread. A camera preview screen requires the screen
         * to be updated constantly, which would bottleneck the main thread.
         */
        surfaceView = findViewById(R.id.surface_view);

        returnedData = new Intent();
        detailFetcher = new BookDetailFetcher();
        validator = new ValidateISBN();
        initDetectorSources();
    }

    /**
     * Initializes the BarcodeDetector, CameraSource, SurfaceHolder.Callback objects
     */
    private void initDetectorSources() {
        // initialize and build detector, enable all barcode formats
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        // initialize and build camera source
        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true)
                .build();

        // implement a SurfaceHolder.Callback for surfaceView
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            /**
             * Checks for camera permission when the surface is first created,
             * request permission from user otherwise.
             * @param surfaceHolder Interface that allows for manipulation of Surface
             */
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                try {
                    if (ActivityCompat.checkSelfPermission(BarcodeScannerActivity.this,
                            Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

                        cameraSource.start(surfaceView.getHolder());
                    }
                    // TODO: displays a screen telling why the user needs to enable permission
//                    else if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {}
                    else {
                        ActivityCompat.requestPermissions(BarcodeScannerActivity.this,
                                new String[] {Manifest.permission.CAMERA},
                                CAMERA_REQUEST_CODE);
                    }
                } catch (IOException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width,
                                       int height) {}

            // release camera resources when surface is destroyed
            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                cameraSource.stop();
            }
        });

        initDetectorProcessor(barcodeDetector);
    }

    /**
     * Initializes Detector.Processor objects for BarcodeDetector objects
     * @param detector BarcodeDetector object that has already been instantiated
     */
    // TODO: display toast notification if the ISBN isn't found on google api
    // TODO: verify ISBN and display toast notification if invalid
    // TODO: temporarily halt killing the activity to let toast message linger
    private void initDetectorProcessor(BarcodeDetector detector) {
        detector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                // TODO: display small popup to inform user the scanner has been stopped
//                Toast.makeText(getApplicationContext(), " ", Toast.LENGTH_SHORT).show();
            }

            /**
             * Sets the processor behaviour when a detection is received
             * @param detections detected codes from the detector
             */
            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                // All detected barcodes are saved in a SparseArray
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    barcodeData = barcodes.valueAt(0).displayValue;
                    Boolean isISBN = validator.verify(barcodeData);
                    if (isISBN) {
                        detailFetcher.request(barcodeData);
                        System.out.println(detailFetcher.getTitle());
                        System.out.println(detailFetcher.getAuthor());
                        returnedData.putExtra("isbn", barcodeData);
                        returnedData.putExtra("title", detailFetcher.getTitle());
                        returnedData.putExtra("author", detailFetcher.getAuthor());
                        setResult(RESULT_OK, returnedData);
                    } else {
                        Toast.makeText(getApplicationContext(), "Not a valid ISBN",
                                        Toast.LENGTH_SHORT).show();
                        returnedData.putExtra("isbn", (String)null);
                        returnedData.putExtra("title", (String)null);
                        returnedData.putExtra("author", (String)null);

                        // TODO: might wanna return different request code
                        setResult(RESULT_OK, returnedData);
                    }
                    finish();
                }
            }
        });
    }

//    /**
//     * Checks if the scanned in barcode is an ISBN code
//     * @param code ISBN code
//     * @return return true if the barcode is an ISBN code, false otherwise
//     */
//    private boolean isISBN(String code) {
//        if (code.length() == 13) {
//
//        } else if (code.length() == 10) {
//
//        } else {
//            return false;
//        }
//        return true;
//    }

    /**
     * Release camera resources when the activity is paused
     */
    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
    }

    /**
     * Reinitialize camera resources when activity is resumed
     */
    @Override
    protected void onResume() {
        super.onResume();
        initDetectorSources();
    }
}
