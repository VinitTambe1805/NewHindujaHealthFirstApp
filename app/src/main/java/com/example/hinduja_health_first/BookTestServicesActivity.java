package com.example.hinduja_health_first;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import java.util.Arrays;
import java.util.List;

public class BookTestServicesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DepartmentAdapter adapter;
    private List<String> departments = Arrays.asList(
            "Cardiology", "Orthology", "Pathology", "Diabetologist/Endocrinology", "Gastroenterology"
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_test_services);

        recyclerView = findViewById(R.id.recyclerViewDepartments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DepartmentAdapter(departments);
        recyclerView.setAdapter(adapter);

        // Handle back button
        findViewById(R.id.backButton).setOnClickListener(v -> finish());
    }
} 