package com.example.hinduja_health_first;

import android.content.Intent;
import android.media.RouteListingPreference;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Doctor_Info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_info);


        RecyclerView recyclerView = findViewById(R.id.recyclerView2);

        List<Doc_item> items = new ArrayList<Doc_item>();
        items.add(new Doc_item("Ajit M", "Cardiologist", "10 years", R.drawable.img7));
        items.add(new Doc_item("Avesh T", "Cardiologist", "12 years", R.drawable.img7));
        items.add(new Doc_item("Anushka S", "Cardiologist", "20 years", R.drawable.img7));
        items.add(new Doc_item("Virat K", "Cardiologist", "32 years", R.drawable.img7));
        items.add(new Doc_item("Rohit S", "Cardiologist", "45 years", R.drawable.img7));
        items.add(new Doc_item("Manish S", "Cardiologist", "12 years", R.drawable.img7));
        items.add(new Doc_item("Ms Dhoni", "Cardiologist", "22 years", R.drawable.img7));
        items.add(new Doc_item("Arun G", "Cardiologist", "23 years", R.drawable.img7));
        items.add(new Doc_item("Jasprit B", "Cardiologist", "15 years", R.drawable.img7));


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyDocAdapter(getApplicationContext(), items));


    }

}