package com.example.hinduja_health_first;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AppointmentSummary extends AppCompatActivity {
    private TextView doctorNameText;
    private TextView doctorSpecialtyText;
    private TextView appointmentTimeText;
    private TextView timeRemainingText;
    private MaterialButton confirmButton;
    private CountDownTimer countDownTimer;
    private long appointmentTimeMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_summary);

        // Initialize views
        doctorNameText = findViewById(R.id.doctorNameText);
        doctorSpecialtyText = findViewById(R.id.doctorSpecialtyText);
        appointmentTimeText = findViewById(R.id.appointmentTimeText);
        timeRemainingText = findViewById(R.id.timeRemainingText);
        confirmButton = findViewById(R.id.confirmButton);

        // Get appointment details from intent
        String doctorName = getIntent().getStringExtra("DOCTOR_NAME");
        String specialty = getIntent().getStringExtra("DOCTOR_SPECIALTY");
        String appointmentTime = getIntent().getStringExtra("APPOINTMENT_TIME");
        String location = getIntent().getStringExtra("LOCATION");

        // Set appointment details
        if (doctorName != null && specialty != null) {
            doctorNameText.setText(doctorName);
            doctorSpecialtyText.setText(specialty);
            appointmentTimeText.setText(appointmentTime != null ? appointmentTime : "10:00 AM");
            timeRemainingText.setText(location != null ? location : "Hinduja Hospital, Mahim");
        } else {
            Toast.makeText(this, "No appointment details found", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Calculate appointment time in milliseconds
        appointmentTimeMillis = calculateAppointmentTimeMillis(appointmentTime);

        // Start countdown timer
        startCountdownTimer();

        // Set up confirm button click listener
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get appointment details
                String doctorName = getIntent().getStringExtra("DOCTOR_NAME");
                String specialty = getIntent().getStringExtra("DOCTOR_SPECIALTY");
                String appointmentTime = getIntent().getStringExtra("APPOINTMENT_TIME");
                String location = getIntent().getStringExtra("LOCATION");

                // Save appointment data with current timestamp
                SharedPrefManager.getInstance(AppointmentSummary.this)
                    .saveAppointment(doctorName, specialty, appointmentTime, location);

                // Show confirmation message
                Toast.makeText(AppointmentSummary.this, "Appointment confirmed successfully!", Toast.LENGTH_LONG).show();
                
                // Create intent for MainActivity without appointment details
                Intent intent = new Intent(AppointmentSummary.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private long calculateAppointmentTimeMillis(String timeSlot) {
        if (timeSlot == null || timeSlot.isEmpty()) {
            timeSlot = "10:00 AM"; // Default time if none provided
        }

        Calendar calendar = Calendar.getInstance();
        
        try {
            // Parse the time slot (format: "HH:MM AM/PM")
            String[] parts = timeSlot.split(" ");
            String[] timeParts = parts[0].split(":");
            
            int hour = Integer.parseInt(timeParts[0]);
            int minute = Integer.parseInt(timeParts[1]);
            String amPm = parts[1];

            // Convert to 24-hour format
            if (amPm.equals("PM") && hour != 12) {
                hour += 12;
            } else if (amPm.equals("AM") && hour == 12) {
                hour = 0;
            }

            // Set the appointment time
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            // If the appointment time has passed for today, set it for tomorrow
            if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }

            return calendar.getTimeInMillis();
        } catch (Exception e) {
            // If there's any error in parsing, set default time (10:00 AM)
            calendar.set(Calendar.HOUR_OF_DAY, 10);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            
            if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
            
            return calendar.getTimeInMillis();
        }
    }

    private void startCountdownTimer() {
        long timeUntilAppointment = appointmentTimeMillis - System.currentTimeMillis();
        
        if (timeUntilAppointment <= 0) {
            timeRemainingText.setText("Appointment time has arrived!");
            return;
        }

        countDownTimer = new CountDownTimer(timeUntilAppointment, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                long minutes = seconds / 60;
                long hours = minutes / 60;
                long days = hours / 24;

                String timeRemaining;
                if (days > 0) {
                    timeRemaining = String.format(Locale.getDefault(),
                            "Time remaining: %d days, %d hours, %d minutes",
                            days, hours % 24, minutes % 60);
                } else if (hours > 0) {
                    timeRemaining = String.format(Locale.getDefault(),
                            "Time remaining: %d hours, %d minutes",
                            hours, minutes % 60);
                } else {
                    timeRemaining = String.format(Locale.getDefault(),
                            "Time remaining: %d minutes",
                            minutes);
                }
                timeRemainingText.setText(timeRemaining);
            }

            @Override
            public void onFinish() {
                timeRemainingText.setText("Appointment time has arrived!");
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
} 