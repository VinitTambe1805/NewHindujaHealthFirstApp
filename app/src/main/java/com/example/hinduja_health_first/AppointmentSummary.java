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
            appointmentTimeText.setText(appointmentTime != null ? appointmentTime : "Today at 10:00 AM");
            timeRemainingText.setText(location != null ? location : "Hinduja Hospital, Mahim");
        } else {
            Toast.makeText(this, "No appointment details found", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Calculate appointment time in milliseconds
        appointmentTimeMillis = calculateAppointmentTimeMillis("10:00 AM");

        // Start countdown timer
        startCountdownTimer();

        // Set up confirm button click listener
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show confirmation message
                Toast.makeText(AppointmentSummary.this, "Appointment confirmed successfully!", Toast.LENGTH_LONG).show();
                
                // Create intent for MainActivity
                Intent intent = new Intent(AppointmentSummary.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
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