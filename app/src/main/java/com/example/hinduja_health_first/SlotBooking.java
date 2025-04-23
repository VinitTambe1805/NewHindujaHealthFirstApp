package com.example.hinduja_health_first;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.location.Address;
import android.location.Geocoder;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SlotBooking extends AppCompatActivity implements OnMapReadyCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final String HOSPITAL_PINCODE = "400016";
    private static final LatLng HOSPITAL_LOCATION = new LatLng(19.076090, 72.877426);
    private static final int MAX_TRAVEL_TIME_MINUTES = 60; // Maximum acceptable travel time
    private static final int PEAK_HOUR_MORNING_START = 8; // 8 AM
    private static final int PEAK_HOUR_MORNING_END = 10; // 10 AM
    private static final int PEAK_HOUR_EVENING_START = 17; // 5 PM
    private static final int PEAK_HOUR_EVENING_END = 19; // 7 PM

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private TextInputEditText pincodeEditText;
    private MaterialButton submitButton;
    private MaterialButton saveButton;
    private MaterialButton[] timeSlots = new MaterialButton[4];
    private MaterialButton selectedTimeSlot = null;
    private TextView movePinText;
    private LatLng selectedLocation;
    private ExecutorService executorService;
    private Geocoder geocoder;
    private double estimatedTravelTime = -1;
    private boolean isPeakHour = false;

    // Time slots for different scenarios
    private final String[] morningSlots = {"9:00 AM", "9:30 AM", "9:45 AM", "10:00 AM", "10:30 AM", "11:00 AM", "11:30 AM"};
    private final String[] afternoonSlots = {"2:00 PM", "2:30 PM", "3:00 PM", "3:30 PM"};
    private final String[] eveningSlots = {"4:00 PM", "4:30 PM", "5:00 PM", "5:30 PM"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slot_booking);

        // Initialize Geocoder
        geocoder = new Geocoder(this, Locale.getDefault());

        // Initialize executor service for background tasks
        executorService = Executors.newSingleThreadExecutor();

        // Initialize views
        initializeViews();

        // Initialize map
        try {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
            } else {
                Toast.makeText(this, "Map fragment not found", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error initializing map: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
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
        movePinText = findViewById(R.id.movePinText);

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

    private void validateAndSubmitPincode() {
        String pincode = pincodeEditText.getText().toString();
        if (pincode.length() != 6) {
            pincodeEditText.setError("Please enter a valid 6-digit pincode");
            return;
        }

        submitButton.setEnabled(false);
        submitButton.setText("Searching...");

        executorService.execute(() -> {
            try {
                List<Address> addresses = geocoder.getFromLocationName(pincode + ", India", 1);
                if (addresses != null && !addresses.isEmpty()) {
                    Address address = addresses.get(0);
                    double lat = address.getLatitude();
                    double lng = address.getLongitude();
                    LatLng location = new LatLng(lat, lng);

                    // Get current time
                    Calendar calendar = Calendar.getInstance();
                    int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                    isPeakHour = (currentHour >= PEAK_HOUR_MORNING_START && currentHour <= PEAK_HOUR_MORNING_END) ||
                                (currentHour >= PEAK_HOUR_EVENING_START && currentHour <= PEAK_HOUR_EVENING_END);

                    // Calculate travel time based on distance
                    float[] results = new float[1];
                    Location.distanceBetween(
                            lat, lng,
                            HOSPITAL_LOCATION.latitude, HOSPITAL_LOCATION.longitude,
                            results
                    );
                    
                    // Convert distance to estimated travel time (assuming average speed of 30 km/h)
                    double distanceInKm = results[0] / 1000;
                    estimatedTravelTime = (distanceInKm / 30.0) * 60; // Convert to minutes

                    runOnUiThread(() -> {
                        selectedLocation = location;
                        updateMapMarker();
                        updateTimeSlots();
                        submitButton.setEnabled(true);
                        submitButton.setText("SUBMIT");
                        Toast.makeText(this, "Location found", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Location not found for this pincode", Toast.LENGTH_SHORT).show();
                        submitButton.setEnabled(true);
                        submitButton.setText("SUBMIT");
                    });
                }
            } catch (IOException e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Error finding location: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    submitButton.setEnabled(true);
                    submitButton.setText("SUBMIT");
                });
            }
        });
    }

    private void updateTimeSlots() {
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        
        String[] slotsToShow;
        String titleText;

        if (estimatedTravelTime > MAX_TRAVEL_TIME_MINUTES) {
            // For locations with long travel time, show only non-peak slots
            if (currentHour < 12) {
                slotsToShow = new String[]{"10:30 AM", "11:00 AM", "11:30 AM", "12:00 PM"};
                titleText = "Best Slots (Long Travel Time)";
            } else if (currentHour < 15) {
                slotsToShow = new String[]{"3:00 PM", "3:30 PM", "4:00 PM", "4:30 PM"};
                titleText = "Best Slots (Long Travel Time)";
            } else {
                slotsToShow = new String[]{"5:00 PM", "5:30 PM", "6:00 PM", "6:30 PM"};
                titleText = "Best Slots (Long Travel Time)";
            }
        } else if (isPeakHour) {
            // During peak hours, show slots with buffer time
            if (currentHour < 12) {
                slotsToShow = new String[]{"9:30 AM", "10:00 AM", "10:30 AM", "11:00 AM"};
                titleText = "Best Slots (Peak Hours)";
            } else if (currentHour < 15) {
                slotsToShow = new String[]{"2:30 PM", "3:00 PM", "3:30 PM", "4:00 PM"};
                titleText = "Best Slots (Peak Hours)";
            } else {
                slotsToShow = new String[]{"4:30 PM", "5:00 PM", "5:30 PM", "6:00 PM"};
                titleText = "Best Slots (Peak Hours)";
            }
        } else {
            // Normal hours, show all available slots
            if (currentHour < 12) {
                slotsToShow = morningSlots;
                titleText = "Best Slots (Normal Hours)";
            } else if (currentHour < 15) {
                slotsToShow = afternoonSlots;
                titleText = "Best Slots (Normal Hours)";
            } else {
                slotsToShow = eveningSlots;
                titleText = "Best Slots (Normal Hours)";
            }
        }

        // Update the time slot buttons
        for (int i = 0; i < timeSlots.length; i++) {
            timeSlots[i].setText(slotsToShow[i]);
            timeSlots[i].setEnabled(true);
        }

        // Update the title text
        TextView slotsTitle = findViewById(R.id.slotsTitle);
        slotsTitle.setText(titleText);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mMap = googleMap;

            if (mMap == null) {
                Toast.makeText(this, "Error initializing map", Toast.LENGTH_LONG).show();
                return;
            }

            // Enable zoom controls and other UI settings
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setMapToolbarEnabled(true);
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

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

            // Set initial camera position
            setupDefaultLocation();
        } catch (Exception e) {
            Toast.makeText(this, "Error in onMapReady: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
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
                setupDefaultLocation();
            }
        }
    }

    private void setupMap() {
        try {
            if (checkLocationPermission() && mMap != null) {
                mMap.setMyLocationEnabled(true);
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT).show();
                setupDefaultLocation();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error accessing location: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
        LatLng mumbai = new LatLng(19.076090, 72.877426);
        selectedLocation = mumbai;
        updateMapMarker();
    }

    private void updateMapMarker() {
        if (mMap != null && selectedLocation != null) {
            mMap.clear();
            mMap.addMarker(new MarkerOptions()
                    .position(selectedLocation)
                    .title("Selected Location"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(selectedLocation, 15f));
        }
    }

    private void enableTimeSlots() {
        for (MaterialButton slot : timeSlots) {
            slot.setEnabled(true);
        }
    }

    private void selectTimeSlot(MaterialButton selectedSlot) {
        // Reset all buttons to default state
        int defaultColor = ContextCompat.getColor(this, R.color.black);
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

        String timeSlot = selectedTimeSlot.getText().toString();
        String message = "Selected time: " + timeSlot;
        if (selectedLocation != null) {
            message += "\nLocation: " + selectedLocation.latitude + ", " + selectedLocation.longitude;
        } else {
            message += "\nPincode: " + pincodeEditText.getText().toString();
        }

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}