package com.example.hinduja_health_first;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private SearchView searchView;
    private Button bookAppoint, bookTest, healthBlog, healthCheckup;
    private String[] item = {"Hinduja Hospital Mahim (West)", "Hinduja Hospital Khar(West)"};
    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<String> adapterItems;
    private SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Initialize SharedPreferences manager
        sharedPrefManager = SharedPrefManager.getInstance(this);
        
        // Check if user is logged in
        if (!sharedPrefManager.isLoggedIn()) {
            startActivity(new Intent(MainActivity.this, loginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        // Initialize views
        searchView = findViewById(R.id.search_bar);
        bookAppoint = findViewById(R.id.submit_button);
        bookTest = findViewById(R.id.book_services);
        healthBlog = findViewById(R.id.health_blog);
        healthCheckup = findViewById(R.id.health_checkup);

        autoCompleteTextView = findViewById(R.id.auto_complete_txt);
        adapterItems = new ArrayAdapter<String>(this, R.layout.list_item, item);
        autoCompleteTextView.setAdapter(adapterItems);

        autoCompleteTextView.setOnItemClickListener((adapterView, view, i, l) -> {
            String item = adapterView.getItemAtPosition(i).toString();
            Toast.makeText(MainActivity.this, "Hospital:" + item, Toast.LENGTH_SHORT).show();

            bookAppoint.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, RecycleActivity.class);
                startActivity(intent);
            });

            bookTest.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, Doctor_Info.class);
                startActivity(intent);
            });

            healthCheckup.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, SlotBooking.class);
                startActivity(intent);
            });
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            sharedPrefManager.logout();
            startActivity(new Intent(MainActivity.this, loginActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}