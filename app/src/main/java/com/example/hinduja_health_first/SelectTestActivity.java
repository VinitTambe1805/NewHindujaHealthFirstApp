package com.example.hinduja_health_first;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectTestActivity extends AppCompatActivity {
    private static final String CHANNEL_ID = "test_memo_channel";
    private static final int NOTIFICATION_ID = 2; // Different ID from appointment notification
    private RecyclerView recyclerView;
    private TestAdapter adapter;
    private TextView nameText, selectTestsText;
    private Button createMemoButton;
    private ImageButton backButton;

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

        String department = getIntent().getStringExtra("department");
        List<String> tests = TESTS_MAP.get(department);
        if (tests == null) tests = Arrays.asList("Test 1", "Test 2", "Test 3");

        nameText = findViewById(R.id.nameText);
        selectTestsText = findViewById(R.id.selectTestsText);
        createMemoButton = findViewById(R.id.createMemoButton);
        backButton = findViewById(R.id.backButton);
        recyclerView = findViewById(R.id.recyclerViewTests);

        nameText.setText("Select Tests & Services");
        selectTestsText.setText("Select Tests:");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TestAdapter(tests);
        recyclerView.setAdapter(adapter);

        // Create notification channel
        createNotificationChannel();

        backButton.setOnClickListener(v -> finish());
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
} 