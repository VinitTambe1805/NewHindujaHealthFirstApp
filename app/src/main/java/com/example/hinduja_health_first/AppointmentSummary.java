package com.example.hinduja_health_first;

import android.os.Bundle;
import android.os.CountDownTimer;
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

        // Get data from intent
        String doctorName = getIntent().getStringExtra("DOCTOR_NAME");
        String doctorSpecialty = getIntent().getStringExtra("DOCTOR_SPECIALTY");
        String selectedTimeSlot = getIntent().getStringExtra("SELECTED_TIME_SLOT");

        // Set doctor information
        doctorNameText.setText(doctorName);
        doctorSpecialtyText.setText(doctorSpecialty);

        // Set appointment time
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault());
        String currentDate = dateFormat.format(calendar.getTime());
        appointmentTimeText.setText("Appointment Time: " + currentDate + " at " + selectedTimeSlot);

        // Calculate appointment time in milliseconds
        appointmentTimeMillis = calculateAppointmentTimeMillis(selectedTimeSlot);

        // Start countdown timer
        startCountdownTimer();

        // Set confirm button click listener
        confirmButton.setOnClickListener(v -> {
            // Stop the countdown timer
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }
            Toast.makeText(this, "Appointment confirmed!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private long calculateAppointmentTimeMillis(String timeSlot) {
        Calendar calendar = Calendar.getInstance();
        String[] timeParts = timeSlot.split(":");
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1].split(" ")[0]);
        String amPm = timeParts[1].split(" ")[1];

        // Convert to 24-hour format
        if (amPm.equals("PM") && hour != 12) {
            hour += 12;
        } else if (amPm.equals("AM") && hour == 12) {
            hour = 0;
        }

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // If the appointment time has passed for today, set it for tomorrow
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        return calendar.getTimeInMillis();
    }

    private void startCountdownTimer() {
        countDownTimer = new CountDownTimer(appointmentTimeMillis - System.currentTimeMillis(), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                long minutes = seconds / 60;
                long hours = minutes / 60;
                long days = hours / 24;

                String timeRemaining = String.format(Locale.getDefault(),
                        "Time remaining: %d days, %d hours, %d minutes",
                        days, hours % 24, minutes % 60);
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