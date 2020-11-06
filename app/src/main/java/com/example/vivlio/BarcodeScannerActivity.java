package com.example.vivlio;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class BarcodeScannerActivity extends AppCompatActivity {
    private BookDetailFetcher detailFetcher;
    private SurfaceView surfaceView;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int CAMERA_REQUEST_CODE = 100;
    private String barcodeData;

    /**
     * ISBN is returned as an intent. To access the ISBN string
     * use Intent.getStringExtra("isbn")
     */
    Intent returnedData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);
        surfaceView = findViewById(R.id.surface_view);
        returnedData = new Intent();
        detailFetcher = new BookDetailFetcher();
        initDetectorSources();
    }

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
            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                cameraSource.release();
            }
        });

        initDetectorProcessor(barcodeDetector);
    }

    private void initDetectorProcessor(BarcodeDetector detector) {
        detector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                // TODO: display small popup to inform user the scanner has been stopped
            }

            // implement detector behaviour when a barcode is scanned
            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    barcodeData = barcodes.valueAt(0).displayValue;
                    detailFetcher.request(barcodeData);

                    returnedData.putExtra("isbn", barcodeData);
                    returnedData.putExtra("title", detailFetcher.getTitle());
                    returnedData.putExtra("author", detailFetcher.getAuthor());
                    setResult(RESULT_OK, returnedData);
                    finish();
                }
            }
        });
    }

    private boolean isISBN(String code) {
        // TODO: parse barcode data into int and check if it is an ISBN
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initDetectorSources();
    }
}
