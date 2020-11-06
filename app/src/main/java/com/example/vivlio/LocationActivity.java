package com.example.vivlio;


import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LocationActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private double longitude;
    private double latitude;
    private Boolean looker;
    private Button cancelButton;
    private Button confirmButton;
    private Boolean changed;

    public LocationActivity(double longitude, double latitude){
        this.longitude = longitude;
        this.latitude = latitude;
        looker = true;
    }

    public LocationActivity(){
        longitude = -113.6242;
        latitude = 53.5225;
        looker = false;
        changed = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_activity);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        cancelButton = findViewById(R.id.cancel_button);
        confirmButton = findViewById(R.id.confirm_button);

        cancelButton.setOnClickListener((new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        }));

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkTask();
            }
        });
    }

    public void getLocation(){
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions options = new MarkerOptions();
                options.position(latLng);
                options.title(latLng.latitude + ":" + latLng.longitude);
                options.draggable(true);
                mMap.clear();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));
                mMap.addMarker(options);
                changed = true;
            }
        });
    }

    public void checkTask(){
        if (changed == true){
            Intent intent = new Intent();
            ArrayList<Double> latLong = new ArrayList<>();
            latLong.add(longitude);
            latLong.add(latitude);
            intent.putExtra("latLong arraylist", latLong);
            setResult(RESULT_OK, intent);
            finish();
        }else if (looker == false){
            Toast.makeText(this,"You have not selected a location yet", Toast.LENGTH_SHORT).show();
        }else{
            finish();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (looker == false){
            getLocation();
        }else{
            Geocoder geocoder = new Geocoder(getApplicationContext());
            try {
                List<Address> addresses =
                        geocoder.getFromLocation(latitude, longitude, 1);
                String location = addresses.get(0).getLocality()+ ":"
                        + addresses.get(0).getCountryName();
                LatLng latLng = new LatLng(latitude, longitude);
                mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                mMap.setMaxZoomPreference(20);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}