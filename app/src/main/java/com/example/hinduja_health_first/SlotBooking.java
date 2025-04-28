package com.example.hinduja_health_first;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.PendingIntentCompat;

import com.example.hinduja_health_first.ui.login.LoginActivity;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    private static final String CHANNEL_ID = "appointment_reminder";
    private static final int NOTIFICATION_ID = 1;
    private static final int PERMISSION_REQUEST_CODE = 1001;

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private TextInputEditText pincodeEditText;
    private MaterialButton submitButton;
    private MaterialButton saveButton;
    private MaterialButton[] timeSlots = new MaterialButton[4];
    private MaterialButton selectedTimeSlotButton;
    private TextView movePinText;
    private TextView doctorInfoText;
    private LatLng selectedLocation;
    private ExecutorService executorService;
    private Geocoder geocoder;
    private double estimatedTravelTime = -1;
    private boolean isPeakHour = false;

    // Time slots for different scenarios
    private final String[] morningSlots = {"9:00 AM", "9:30 AM", "9:45 AM", "10:00 AM", "10:30 AM", "11:00 AM", "11:30 AM"};
    private final String[] afternoonSlots = {"2:00 PM", "2:30 PM", "3:00 PM", "3:30 PM"};
    private final String[] eveningSlots = {"4:00 PM", "4:30 PM", "5:00 PM", "5:30 PM"};

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabaseHelper databaseHelper;
    private String selectedDoctorName;
    private String selectedSpecialty;
    private String selectedTimeSlotString;
    private String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slot_booking);

        // Create notification channel
        createNotificationChannel();

        // Initialize Firebase
        try {
            mAuth = FirebaseAuth.getInstance();
            currentUser = mAuth.getCurrentUser();
            databaseHelper = FirebaseDatabaseHelper.getInstance();
        } catch (Exception e) {
            Log.e(TAG, "Error initializing Firebase: " + e.getMessage());
            Toast.makeText(this, "Error initializing app. Please try again later.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Get doctor's information from intent
        selectedDoctorName = getIntent().getStringExtra("DOCTOR_NAME");
        selectedSpecialty = getIntent().getStringExtra("DOCTOR_SPECIALTY");
        selectedDate = getIntent().getStringExtra("SELECTED_DATE");
        String doctorExperience = getIntent().getStringExtra("DOCTOR_EXPERIENCE");

        if (selectedDoctorName == null || selectedSpecialty == null || selectedDate == null) {
            Toast.makeText(this, "Invalid doctor information", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Display doctor's information
        doctorInfoText = findViewById(R.id.doctorInfoText);
        if (doctorInfoText != null && selectedDoctorName != null) {
            String doctorInfo = "Doctor: " + selectedDoctorName + "\n" +
                    "Specialty: " + selectedSpecialty + "\n" +
                    "Experience: " + doctorExperience;
            doctorInfoText.setText(doctorInfo);
        }

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
        } else if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Retry showing notification after permission is granted
                String doctorName = getIntent().getStringExtra("DOCTOR_NAME");
                String specialty = getIntent().getStringExtra("DOCTOR_SPECIALTY");
                if (doctorName != null && specialty != null) {
                    showAppointmentNotification(doctorName, specialty);
                }
            } else {
                Toast.makeText(this, "Notification permission is required for appointment reminders",
                        Toast.LENGTH_SHORT).show();
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
        try {
            if (selectedSlot == null) {
                return;
            }

            // Reset all buttons to default state
            int defaultColor = ContextCompat.getColor(this, R.color.black);
            int selectedColor = ContextCompat.getColor(this, R.color.teal_700);

            for (MaterialButton slot : timeSlots) {
                if (slot != null) {
                    slot.setStrokeColor(ColorStateList.valueOf(defaultColor));
                    slot.setTextColor(defaultColor);
                }
            }

            // Highlight selected button
            selectedSlot.setStrokeColor(ColorStateList.valueOf(selectedColor));
            selectedSlot.setTextColor(selectedColor);
            selectedTimeSlotString = selectedSlot.getText().toString();
            selectedTimeSlotButton = selectedSlot;

            // Check if user is logged in
            if (currentUser == null) {
                startActivity(new Intent(this, LoginActivity.class));
                return;
            }

            // Check if pincode is entered
            if (pincodeEditText.getText() == null || pincodeEditText.getText().toString().isEmpty()) {
                return;
            }

            // Check if database helper is initialized
            if (databaseHelper == null) {
                return;
            }

            // Check time slot availability
            checkTimeSlotAvailability();
        } catch (Exception e) {
            Log.e(TAG, "Error in selectTimeSlot: " + e.getMessage());
        }
    }

    private void checkTimeSlotAvailability() {
        try {
            if (selectedDoctorName == null || selectedDate == null || selectedTimeSlotString == null) {
                Toast.makeText(this, "Please select all required fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (databaseHelper == null) {
                Toast.makeText(this, "Please try again later", Toast.LENGTH_SHORT).show();
                return;
            }

            // Show loading indicator
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Checking slot availability...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            databaseHelper.checkTimeSlotAvailability(selectedDoctorName, selectedDate, selectedTimeSlotString,
                new FirebaseDatabaseHelper.OnTimeSlotCheckListener() {
                    @Override
                    public void onTimeSlotChecked(boolean isAvailable) {
                        try {
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            
                            if (isAvailable) {
                                // Slot is available, proceed with booking
                                bookAppointment();
                            } else {
                                Toast.makeText(SlotBooking.this, 
                                    "This time slot is no longer available. Please select another slot.", 
                                    Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Error in onTimeSlotChecked: " + e.getMessage());
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            Toast.makeText(SlotBooking.this, 
                                "Please try selecting another slot", 
                                Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        } catch (Exception e) {
            Log.e(TAG, "Error in checkTimeSlotAvailability: " + e.getMessage());
            Toast.makeText(this, "Please try again", Toast.LENGTH_SHORT).show();
        }
    }

    private void bookAppointment() {
        if (currentUser == null) {
            Toast.makeText(this, "Please login to book an appointment", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }

        // Show loading indicator
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Booking your appointment...");
        progressDialog.show();

        try {
            // Create patient object
            Patient patient = new Patient(
                null, // ID will be set by Firebase
                currentUser.getDisplayName(),
                currentUser.getEmail(),
                currentUser.getPhoneNumber(),
                selectedDoctorName,
                selectedSpecialty,
                selectedDate,
                selectedTimeSlotString
            );

            // Save appointment to Firebase
            databaseHelper.savePatientAppointment(patient, new FirebaseDatabaseHelper.OnCompleteListener() {
                @Override
                public void onSuccess() {
                    progressDialog.dismiss();
                    // Show success message
                    Toast.makeText(SlotBooking.this, 
                        "Appointment booked successfully!", 
                        Toast.LENGTH_SHORT).show();

                    // Show notification
                    showAppointmentNotification(selectedDoctorName, selectedSpecialty);

                    // Navigate to appointment summary
                    Intent intent = new Intent(SlotBooking.this, AppointmentSummary.class);
                    intent.putExtra("DOCTOR_NAME", selectedDoctorName);
                    intent.putExtra("DOCTOR_SPECIALTY", selectedSpecialty);
                    intent.putExtra("APPOINTMENT_TIME", selectedTimeSlotString);
                    intent.putExtra("APPOINTMENT_DATE", selectedDate);
                    intent.putExtra("LOCATION", "Hinduja Hospital, Mahim");
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFailure(Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(SlotBooking.this, 
                        "Failed to book appointment: " + e.getMessage(), 
                        Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            progressDialog.dismiss();
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void saveAndContinue() {
        if (selectedTimeSlotString == null) {
            Toast.makeText(this, "Please select a time slot", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedLocation == null && pincodeEditText.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please select a location or enter pincode", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get doctor information from intent
        String doctorName = getIntent().getStringExtra("DOCTOR_NAME");
        String doctorSpecialty = getIntent().getStringExtra("DOCTOR_SPECIALTY");
        String selectedTime = selectedTimeSlotString;

        // Create intent for AppointmentSummary activity
        Intent intent = new Intent(this, AppointmentSummary.class);
        intent.putExtra("DOCTOR_NAME", doctorName);
        intent.putExtra("DOCTOR_SPECIALTY", doctorSpecialty);
        intent.putExtra("APPOINTMENT_TIME", selectedTime);
        intent.putExtra("LOCATION", "Hinduja Hospital, Mahim");

        // Show appointment confirmation notification
        showAppointmentNotification(doctorName, doctorSpecialty);

        // Start the AppointmentSummary activity
        startActivity(intent);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Appointment Reminders";
            String description = "Notifications for appointment reminders";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500});

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showAppointmentNotification(String doctorName, String specialty) {
        // Check for notification permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                        PERMISSION_REQUEST_CODE);
                return;
            }
        }

        // Create intent for notification click
        Intent intent = new Intent(this, AppointmentSummary.class);
        intent.putExtra("DOCTOR_NAME", doctorName);
        intent.putExtra("DOCTOR_SPECIALTY", specialty);
        intent.putExtra("APPOINTMENT_TIME", selectedTimeSlotString);
        intent.putExtra("LOCATION", "Hinduja Hospital, Mahim");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Create PendingIntent with proper flags
        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flags |= PendingIntent.FLAG_IMMUTABLE;
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, flags);

        // Build notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Appointment Confirmed")
                .setContentText("Your appointment with " + doctorName + " (" + specialty + ") has been confirmed")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setVibrate(new long[]{100, 200, 300, 400, 500})
                .setContentIntent(pendingIntent);

        // Show notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        try {
            notificationManager.notify(NOTIFICATION_ID, builder.build());
            Toast.makeText(this, "Notification sent successfully", Toast.LENGTH_SHORT).show();
        } catch (SecurityException e) {
            Toast.makeText(this, "Failed to show notification: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}