package com.example.hinduja_health_first;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

public class BookTestServicesActivity extends AppCompatActivity implements DepartmentAdapter.OnDepartmentClickListener {
    private RecyclerView recyclerView;
    private DepartmentAdapter adapter;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_test_services);

        // Initialize back button
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            finish(); // This will close the current activity and return to MainActivity
        });

        recyclerView = findViewById(R.id.recyclerViewDepartments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create list of departments
        List<String> departments = Arrays.asList(
            "Laboratory",
            "Radiology",
            "Pathology",
            "Cardiology"
        );

        adapter = new DepartmentAdapter(this, departments, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDepartmentClick(String department) {
        Intent intent = new Intent(this, SelectTestActivity.class);
        intent.putExtra("department", department);
        startActivity(intent);
    }
} 