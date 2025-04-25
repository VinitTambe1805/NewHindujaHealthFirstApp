package com.example.hinduja_health_first;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Doctor_Info extends AppCompatActivity {
    private static final String TAG = "Doctor_Info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_doctor_info);

            String specialty = getIntent().getStringExtra("SPECIALTY");
            Log.d(TAG, "Received specialty: " + specialty);

            if (specialty == null) {
                specialty = "Cardiologist"; // Default specialty if none is passed
                Log.d(TAG, "No specialty received, using default: " + specialty);
            }

            RecyclerView recyclerView = findViewById(R.id.recyclerView2);
            if (recyclerView == null) {
                Log.e(TAG, "RecyclerView not found");
                Toast.makeText(this, "Error: RecyclerView not found", Toast.LENGTH_SHORT).show();
                return;
            }

            List<Doc_item> items = new ArrayList<>();
            // Add doctors based on the specialty
            switch (specialty) {
                case "Cardiologist":
                    items.add(new Doc_item("Dr. Ajit M", "Cardiologist", "10 years", R.drawable.img7));
                    items.add(new Doc_item("Dr. Avesh T", "Cardiologist", "12 years", R.drawable.img7));
                    items.add(new Doc_item("Dr. Anushka S", "Cardiologist", "20 years", R.drawable.img7));
                    items.add(new Doc_item("Dr. Saksham Gupta", "Cardiologist", "20 years", R.drawable.img7));
                    break;
                case "Dermatologist":
                    items.add(new Doc_item("Dr. Priya K", "Dermatologist", "8 years", R.drawable.img7));
                    items.add(new Doc_item("Dr. Rahul M", "Dermatologist", "15 years", R.drawable.img7));
                    items.add(new Doc_item("Dr. Sneha P", "Dermatologist", "5 years", R.drawable.img7));
                    break;
                case "Diabetologist":
                    items.add(new Doc_item("Dr. Rajesh K", "Diabetologist", "12 years", R.drawable.img7));
                    items.add(new Doc_item("Dr. Meera S", "Diabetologist", "18 years", R.drawable.img7));
                    items.add(new Doc_item("Dr. Vikram P", "Diabetologist", "7 years", R.drawable.img7));
                    break;
                case "Gastroenterologist":
                    items.add(new Doc_item("Dr. Amit R", "Gastroenterologist", "14 years", R.drawable.img7));
                    items.add(new Doc_item("Dr. Neha M", "Gastroenterologist", "9 years", R.drawable.img7));
                    items.add(new Doc_item("Dr. Sanjay K", "Gastroenterologist", "16 years", R.drawable.img7));
                    break;
                default:
                    // Default case if specialty doesn't match any known type
                    items.add(new Doc_item("Dr. John D", "General Physician", "10 years", R.drawable.img7));
                    items.add(new Doc_item("Dr. Jane S", "General Physician", "8 years", R.drawable.img7));
                    break;
            }

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new MyDocAdapter(this, items));
        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate: " + e.getMessage(), e);
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}