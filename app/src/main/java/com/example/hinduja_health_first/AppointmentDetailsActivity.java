package com.example.hinduja_health_first;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AppointmentDetailsActivity extends AppCompatActivity {
    private LinearLayout appointmentsList;
    private MaterialButton backButton;
    private SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_details);

        // Initialize SharedPreferences manager
        sharedPrefManager = SharedPrefManager.getInstance(this);

        // Initialize views
        appointmentsList = findViewById(R.id.appointmentsList);
        backButton = findViewById(R.id.backButton);

        // Load and display appointment details
        loadAppointmentDetails();

        // Set up back button click listener
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create intent for MainActivity
                Intent intent = new Intent(AppointmentDetailsActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadAppointmentDetails() {
        // Clear existing views
        appointmentsList.removeAllViews();

        // Get all appointments
        List<SharedPrefManager.Appointment> appointments = sharedPrefManager.getAllAppointments();

        if (appointments.isEmpty()) {
            Toast.makeText(this, "No appointments found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Sort appointments by timestamp (newest first)
        Collections.sort(appointments, new Comparator<SharedPrefManager.Appointment>() {
            @Override
            public int compare(SharedPrefManager.Appointment a1, SharedPrefManager.Appointment a2) {
                return Long.compare(a2.getTimestamp(), a1.getTimestamp());
            }
        });

        // Add appointment cards
        for (SharedPrefManager.Appointment appointment : appointments) {
            View cardView = LayoutInflater.from(this).inflate(R.layout.item_appointment_card, appointmentsList, false);

            TextView doctorNameText = cardView.findViewById(R.id.doctorNameText);
            TextView doctorSpecialtyText = cardView.findViewById(R.id.doctorSpecialtyText);
            TextView appointmentTimeText = cardView.findViewById(R.id.appointmentTimeText);
            TextView locationText = cardView.findViewById(R.id.locationText);

            doctorNameText.setText(appointment.getDoctorName());
            doctorSpecialtyText.setText(appointment.getSpecialty());
            appointmentTimeText.setText(appointment.getAppointmentTime());
            locationText.setText(appointment.getLocation());

            appointmentsList.addView(cardView);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh appointment details when activity resumes
        loadAppointmentDetails();
    }
} 