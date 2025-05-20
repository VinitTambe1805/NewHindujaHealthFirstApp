package com.example.hinduja_health_first;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Arrays;
import java.util.List;

public class SpecialtyActivity extends AppCompatActivity implements SpecialtyAdapter.OnSpecialtyClickListener {
    private RecyclerView recyclerView;
    private SpecialtyAdapter adapter;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specialty);

        // Initialize back button
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            finish(); // This will close the current activity and return to MainActivity
        });

        recyclerView = findViewById(R.id.specialtyRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create list of specialties
        List<String> specialties = Arrays.asList(
            "Cardiologist",
            "Dermatologist",
            "Diabetologist",
            "Gastroenterologist"
        );

        adapter = new SpecialtyAdapter(this, specialties, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onSpecialtyClick(String specialty) {
        // Open Doctor_Info activity with the selected specialty
        Intent intent = new Intent(this, Doctor_Info.class);
        intent.putExtra("SPECIALTY", specialty);
        startActivity(intent);
    }
}