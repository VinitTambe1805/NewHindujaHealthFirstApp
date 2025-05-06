package com.example.hinduja_health_first;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.button.MaterialButton;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AppointmentDetailsActivity extends AppCompatActivity {
    private LinearLayout appointmentsContainer;
    private LinearLayout testMemosContainer;
    private MaterialButton backButton;
    private SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_details);

        // Initialize SharedPreferences manager
        sharedPrefManager = SharedPrefManager.getInstance(this);

        // Initialize views
        appointmentsContainer = findViewById(R.id.appointmentsContainer);
        testMemosContainer = findViewById(R.id.testMemosContainer);
        backButton = findViewById(R.id.backButton);

        // Load and display appointments
        loadAppointments();

        // Load and display test memos
        loadTestMemos();

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

    private void loadAppointments() {
        List<SharedPrefManager.Appointment> appointments = sharedPrefManager.getAllAppointments();
        
        if (appointments.isEmpty()) {
            appointmentsContainer.setVisibility(View.GONE);
            return;
        }

        // Sort appointments by timestamp in descending order (most recent first)
        Collections.sort(appointments, new Comparator<SharedPrefManager.Appointment>() {
            @Override
            public int compare(SharedPrefManager.Appointment a1, SharedPrefManager.Appointment a2) {
                return Long.compare(a2.getTimestamp(), a1.getTimestamp());
            }
        });

        appointmentsContainer.setVisibility(View.VISIBLE);
        appointmentsContainer.removeAllViews();

        for (SharedPrefManager.Appointment appointment : appointments) {
            CardView appointmentCard = createAppointmentCard(appointment);
            appointmentsContainer.addView(appointmentCard);
        }
    }

    private void loadTestMemos() {
        SharedPrefManager.TestMemo testMemo = sharedPrefManager.getTestMemo();

        if (testMemo == null) {
            testMemosContainer.setVisibility(View.GONE);
            return;
        }

        testMemosContainer.setVisibility(View.VISIBLE);
        testMemosContainer.removeAllViews();

        CardView testMemoCard = createTestMemoCard(testMemo);
        testMemosContainer.addView(testMemoCard);
    }

    private CardView createAppointmentCard(SharedPrefManager.Appointment appointment) {
        CardView card = new CardView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 16);
        card.setLayoutParams(params);
        card.setCardElevation(4);
        card.setRadius(8);

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(16, 16, 16, 16);

        TextView doctorName = new TextView(this);
        doctorName.setText("Doctor: " + appointment.getDoctorName());
        doctorName.setTextSize(18);
        doctorName.setTextColor(getResources().getColor(android.R.color.black));
        content.addView(doctorName);

        TextView specialty = new TextView(this);
        specialty.setText("Specialty: " + appointment.getSpecialty());
        specialty.setTextSize(16);
        specialty.setTextColor(getResources().getColor(android.R.color.black));
        content.addView(specialty);

        TextView time = new TextView(this);
        time.setText("Time: " + appointment.getAppointmentTime());
        time.setTextSize(16);
        time.setTextColor(getResources().getColor(android.R.color.black));
        content.addView(time);

        TextView location = new TextView(this);
        location.setText("Location: " + appointment.getLocation());
        location.setTextSize(16);
        location.setTextColor(getResources().getColor(android.R.color.black));
        content.addView(location);

        card.addView(content);
        return card;
    }

    private CardView createTestMemoCard(SharedPrefManager.TestMemo testMemo) {
        CardView card = new CardView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 16);
        card.setLayoutParams(params);
        card.setCardElevation(4);
        card.setRadius(8);

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(16, 16, 16, 16);

        TextView title = new TextView(this);
        title.setText("Tests Memo");
        title.setTextSize(20);
        title.setTextColor(getResources().getColor(android.R.color.black));
        title.setTypeface(null, android.graphics.Typeface.BOLD);
        content.addView(title);

        TextView department = new TextView(this);
        department.setText("Department: " + testMemo.getDepartment());
        department.setTextSize(18);
        department.setTextColor(getResources().getColor(android.R.color.black));
        content.addView(department);

        TextView testsTitle = new TextView(this);
        testsTitle.setText("Selected Tests:");
        testsTitle.setTextSize(16);
        testsTitle.setTextColor(getResources().getColor(android.R.color.darker_gray));
        content.addView(testsTitle);

        for (String test : testMemo.getTests()) {
            TextView testItem = new TextView(this);
            testItem.setText("• " + test);
            testItem.setTextSize(16);
            testItem.setTextColor(getResources().getColor(android.R.color.darker_gray));
            content.addView(testItem);
        }

        card.addView(content);
        return card;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh appointment details when activity resumes
        loadAppointments();
        loadTestMemos();
    }
} 