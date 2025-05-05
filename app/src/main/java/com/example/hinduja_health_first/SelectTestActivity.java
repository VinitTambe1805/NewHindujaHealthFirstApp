package com.example.hinduja_health_first;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectTestActivity extends AppCompatActivity {
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

        nameText.setText("Name:");
        selectTestsText.setText("Select Tests:");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TestAdapter(tests);
        recyclerView.setAdapter(adapter);

        backButton.setOnClickListener(v -> finish());
        createMemoButton.setOnClickListener(v -> {
            // Handle memo creation here
        });
    }
} 