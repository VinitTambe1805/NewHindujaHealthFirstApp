package com.example.hinduja_health_first;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class SlotBooking extends AppCompatActivity implements OnMapReadyCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private TextInputEditText pincodeEditText;
    private MaterialButton submitButton;
    private MaterialButton saveButton;
    private MaterialButton[] timeSlots = new MaterialButton[4];
    private MaterialButton selectedTimeSlot = null;
    private TextView movePinText;  // Changed from movePin to movePinText to match XML
    private LatLng selectedLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slot_booking);

        // Initialize views
        initializeViews();

        // Initialize map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Initialize location services
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Set up click listeners
        setupClickListeners();
    }

    private void initializeViews() {
        pincodeEditText = findViewById(R.id.pincodeEditText);
        submitButton = findViewById(R.id.submitButton);
        saveButton = findViewById(R.id.saveButton);
        movePinText = findViewById(R.id.movePinText);  // Updated ID to match XML

        timeSlots[0] = findViewById(R.id.slot1);
        timeSlots[1] = findViewById(R.id.slot2);
        timeSlots[2] = findViewById(R.id.slot3);
        timeSlots[3] = findViewById(R.id.slot4);
    }


    private void setupClickListeners() {
        submitButton.setOnClickListener(v -> validateAndSubmitPincode());

        for (MaterialButton timeSlot : timeSlots) {
            timeSlot.setOnClickListener(v -> selectTimeSlot((MaterialButton) v));
        }

        saveButton.setOnClickListener(v -> saveAndContinue());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Enable zoom controls
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);

        // Check for location permission
        if (checkLocationPermission()) {
            setupMap();
        } else {
            requestLocationPermission();
        }

        // Handle location selection
        mMap.setOnMapClickListener(latLng -> {
            selectedLocation = latLng;
            updateMapMarker();
        });
    }

    private boolean checkLocationPermission() {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupMap();
            } else {
                // Permission denied - use default location
                setupDefaultLocation();
            }
        }
    }

    private void setupMap() {
        try {
            if (checkLocationPermission() && mMap != null) {
                mMap.setMyLocationEnabled(true);
                getCurrentLocation();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
            setupDefaultLocation();
        }
    }

    private void getCurrentLocation() {
        if (checkLocationPermission()) {
            try {
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(this, location -> {
                            if (location != null) {
                                LatLng currentLocation = new LatLng(
                                        location.getLatitude(),
                                        location.getLongitude()
                                );
                                selectedLocation = currentLocation;
                                updateMapMarker();
                            } else {
                                setupDefaultLocation();
                            }
                        })
                        .addOnFailureListener(e -> setupDefaultLocation());
            } catch (SecurityException e) {
                e.printStackTrace();
                setupDefaultLocation();
            }
        }
    }

    private void setupDefaultLocation() {
        // Default to Mumbai location
        LatLng mumbai = new LatLng(19.0760, 72.8777);
        selectedLocation = mumbai;
        updateMapMarker();
    }

    private void updateMapMarker() {
        if (mMap != null && selectedLocation != null) {
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(selectedLocation));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(selectedLocation, 15f));
        }
    }

    private void validateAndSubmitPincode() {
        String pincode = pincodeEditText.getText().toString();
        if (pincode.length() != 6) {
            pincodeEditText.setError("Please enter a valid 6-digit pincode");
            return;
        }

        // Here you would make an API call to validate the pincode
        // For now, we'll just show a success message
        Toast.makeText(this, "Pincode validated successfully", Toast.LENGTH_SHORT).show();

        // Enable time slots after successful validation
        enableTimeSlots();
    }

    private void enableTimeSlots() {
        for (MaterialButton slot : timeSlots) {
            slot.setEnabled(true);
        }
    }

    private void selectTimeSlot(MaterialButton selectedSlot) {
        // Reset all buttons to default state
        int defaultColor = ContextCompat.getColor(this, R.color.purple_500);
        int selectedColor = ContextCompat.getColor(this, R.color.teal_700);

        for (MaterialButton slot : timeSlots) {
            slot.setStrokeColor(ColorStateList.valueOf(defaultColor));
            slot.setTextColor(defaultColor);
        }

        // Highlight selected button
        selectedSlot.setStrokeColor(ColorStateList.valueOf(selectedColor));
        selectedSlot.setTextColor(selectedColor);
        selectedTimeSlot = selectedSlot;
    }

    private void saveAndContinue() {
        if (selectedTimeSlot == null) {
            Toast.makeText(this, "Please select a time slot", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedLocation == null && pincodeEditText.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please select a location or enter pincode", Toast.LENGTH_SHORT).show();
            return;
        }

        // Here you would typically:
        // 1. Save the selected time slot
        // 2. Save the selected location or pincode
        // 3. Navigate to the next screen

        String timeSlot = selectedTimeSlot.getText().toString();
        String message = "Selected time: " + timeSlot;
        if (selectedLocation != null) {
            message += "\nLocation: " + selectedLocation.latitude + ", " + selectedLocation.longitude;
        } else {
            message += "\nPincode: " + pincodeEditText.getText().toString();
        }

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

        // TODO: Navigate to next screen
        // Intent intent = new Intent(this, NextActivity.class);
        // startActivity(intent);
    }
}