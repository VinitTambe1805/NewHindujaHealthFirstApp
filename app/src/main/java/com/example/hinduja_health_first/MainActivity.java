package com.example.hinduja_health_first;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    SearchView searchView;
    Button bookAppoint, bookTest, healthBlog, healthCheckup;

    String[] item = {"Hinduja Hospital Mahim (West)", "Hinduja Hospital Khar(West)"};

    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchView = findViewById(R.id.search_bar);
        bookAppoint = findViewById(R.id.submit_button);
        bookTest = findViewById(R.id.book_services);
        healthBlog = findViewById(R.id.health_blog);
        healthCheckup = findViewById(R.id.health_checkup);

        autoCompleteTextView = findViewById(R.id.auto_complete_txt);
        adapterItems = new ArrayAdapter<String>(this, R.layout.list_item, item);

        autoCompleteTextView.setAdapter(adapterItems);


        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(MainActivity.this, "Hospital:" + item, Toast.LENGTH_SHORT).show();

                bookAppoint.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(MainActivity.this, RecycleActivity.class);
                        startActivity(i);

                    }
                });

                bookTest.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent r = new Intent(MainActivity.this, Doctor_Info.class);
                        startActivity(r);
                    }
                });

                healthCheckup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent t = new Intent(MainActivity.this, SlotBooking.class);
                        startActivity(t);
                    }
                });

            }


        });
    }
}