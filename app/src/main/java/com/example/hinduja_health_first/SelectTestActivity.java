package com.example.hinduja_health_first;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectTestActivity extends AppCompatActivity {
    private static final String CHANNEL_ID = "test_memo_channel";
    private static final int NOTIFICATION_ID = 2; // Different ID from appointment notification
    private RecyclerView recyclerView;
    private TestAdapter adapter;
    private Button createMemoButton;
    private ImageButton backButton;
    private TextView departmentTitle;

    // Example test data for each department
    private static final Map<String, List<String>> TESTS_MAP = new HashMap<>();

    static {
        TESTS_MAP.put("Cardiology", Arrays.asList("Electrocardiogram(ECG)", "X-Ray", "CT Scan", "M.R.I", "Stress Test"));
        TESTS_MAP.put("Pathology", Arrays.asList("Blood Test", "Urine Test", "Biopsy", "CBC", "Liver Function Test"));
        TESTS_MAP.put("Orthology", Arrays.asList("Bone Density Test", "X-Ray", "MRI", "CT Scan", "Arthroscopy"));
        TESTS_MAP.put("Diabetologist/Endocrinology", Arrays.asList("Blood Sugar Test", "Thyroid Test", "Insulin Test", "HbA1c", "C-Peptide Test"));
        TESTS_MAP.put("Gastroenterology", Arrays.asList("Endoscopy", "Colonoscopy", "Liver Function Test", "Stool Test", "Ultrasound"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_test);

        // Get the selected department from intent
        String department = getIntent().getStringExtra("department");
        
        // Initialize views
        backButton = findViewById(R.id.backButton);
        departmentTitle = findViewById(R.id.departmentTitle);
        recyclerView = findViewById(R.id.recyclerViewTests);

        // Set department title
        departmentTitle.setText(department + " Tests");

        // Setup back button
        backButton.setOnClickListener(v -> finish());

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get tests based on department
        List<String> tests = getTestsForDepartment(department);
        adapter = new TestAdapter(tests);
        recyclerView.setAdapter(adapter);

        // Create notification channel
        createNotificationChannel();

        createMemoButton = findViewById(R.id.createMemoButton);
        createMemoButton.setOnClickListener(v -> {
            List<String> selectedTests = adapter.getSelectedTests();
            if (selectedTests.isEmpty()) {
                Toast.makeText(this, "Please select at least one test", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // Save test memo data
            SharedPrefManager.getInstance(this).saveTestMemo(department, selectedTests);
            
            // Show notification
            showTestMemoNotification(department, selectedTests);
            
            // Launch the queue activity
            Intent intent = new Intent(SelectTestActivity.this, TestMemoQueueActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Test Memo Notifications";
            String description = "Notifications for test memos";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500});

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showTestMemoNotification(String department, List<String> selectedTests) {
        // Create intent for notification click
        Intent intent = new Intent(this, MainActivity.class);
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
                .setContentTitle("Tests Memo Created")
                .setContentText("Your tests memo for " + department + " has been created")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setVibrate(new long[]{100, 200, 300, 400, 500})
                .setContentIntent(pendingIntent);

        // Show notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        try {
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        } catch (SecurityException e) {
            Toast.makeText(this, "Failed to show notification: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private List<String> getTestsForDepartment(String department) {
        switch (department.toLowerCase()) {
            case "laboratory":
                return Arrays.asList(
                    "Complete Blood Count (CBC)",
                    "Blood Glucose Test",
                    "Lipid Profile",
                    "Liver Function Test",
                    "Kidney Function Test",
                    "Thyroid Function Test",
                    "Hemoglobin A1C",
                    "Urine Analysis"
                );
            case "radiology":
                return Arrays.asList(
                    "X-Ray Chest",
                    "X-Ray Spine",
                    "CT Scan Brain",
                    "CT Scan Chest",
                    "MRI Brain",
                    "MRI Spine",
                    "Ultrasound Abdomen",
                    "Ultrasound Pelvis"
                );
            case "pathology":
                return Arrays.asList(
                    "Biopsy Analysis",
                    "Cytology Test",
                    "Histopathology",
                    "Immunohistochemistry",
                    "Molecular Pathology",
                    "Flow Cytometry",
                    "Genetic Testing",
                    "Tumor Markers"
                );
            case "cardiology":
                return Arrays.asList(
                    "ECG (Electrocardiogram)",
                    "Echocardiogram",
                    "Stress Test",
                    "Holter Monitor",
                    "Cardiac CT Scan",
                    "Cardiac MRI",
                    "Coronary Angiography",
                    "Cardiac Biomarkers"
                );
            default:
                return new ArrayList<>();
        }
    }
} 