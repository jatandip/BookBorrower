package com.example.vivlio.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.vivlio.Controllers.BookDetailFetcher;
import com.example.vivlio.R;
import com.example.vivlio.Controllers.ValidateISBN;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

/**
 * This activity class requests the phone's camera and
 * creates a preview screen for the camera. It detects
 * for any barcode from the camera and upon first detection
 * the data is saved in the intent and the activity is terminated.
 */
public class BarcodeScannerActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
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

        /**
         * SurfaceView is used because it can create a drawing surface
         * on a separate thread. A camera preview screen requires the screen
         * to be updated constantly, which would bottleneck the main thread.
         */
        returnedData = new Intent();
        detailFetcher = new BookDetailFetcher();
        validator = new ValidateISBN();

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                returnedData.putExtra("isbn", (String) null);
                setResult(RESULT_CANCELED, returnedData);
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        if (checkPermission() == true) {
            initDetectorSources();
        } else {
            ActivityCompat.requestPermissions(BarcodeScannerActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_REQUEST_CODE);
        }
    }

    /**
     * Initializes the BarcodeDetector, CameraSource, SurfaceHolder.Callback objects
     */
    private void initDetectorSources() {
        setContentView(R.layout.activity_barcode_scanner);
        surfaceView = findViewById(R.id.surface_view);

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
                    if (checkPermission()) {
                        cameraSource.start(surfaceView.getHolder());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

//                try {
//                    if (checkPermission() == true) {
//                        cameraSource.start(surfaceView.getHolder());
//                    } else {
//                        ActivityCompat.requestPermissions(BarcodeScannerActivity.this,
//                                new String[] {Manifest.permission.CAMERA},
//                                CAMERA_REQUEST_CODE);
//
////                        if (checkPermission() == true) {
////                            cameraSource.start(surfaceView.getHolder());
////                        } else {
////                            finish();
////                        }
//
//                    }
//                } catch (IOException e){
//                    e.printStackTrace();
//                }
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

                        if (!detailFetcher.isFound()) {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(getApplicationContext(),
                                            "Not available in Google Books API",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        returnedData.putExtra("isbn", barcodeData);
                        returnedData.putExtra("title", detailFetcher.getTitle());
                        returnedData.putExtra("author", detailFetcher.getAuthor());
                        setResult(RESULT_OK, returnedData);

                    } else {

                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Not a valid ISBN",
                                                Toast.LENGTH_SHORT).show();
                            }
                        });

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

    private Boolean checkPermission() {
        return ActivityCompat.checkSelfPermission(BarcodeScannerActivity.this,
                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initDetectorSources();
            } else {
                finish();
            }
        }
    }

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
