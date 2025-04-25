package com.example.hinduja_health_first;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private SearchView searchView;
    private Button bookAppoint, bookTest, healthBlog, healthCheckup;
    private String[] hospitals = {"Hinduja Hospital Mahim (West)", "Hinduja Hospital Khar(West)"};
    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<String> adapterItems;
    private SharedPrefManager sharedPrefManager;
    private ImageButton notificationIcon;

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
        notificationIcon = findViewById(R.id.notification_icon);

        // Setup hospital dropdown
        setupHospitalDropdown();

        // Setup search functionality
        setupSearchView();

        // Setup button click listeners
        setupButtonClickListeners();

        // Setup notification icon click listener
        setupNotificationIcon();
    }

    private void setupNotificationIcon() {
        if (notificationIcon == null) {
            Log.e(TAG, "Notification icon is null");
            return;
        }

        notificationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Log.d(TAG, "Notification icon clicked");
                    
                    // Check if user is logged in
                    if (!sharedPrefManager.isLoggedIn()) {
                        Toast.makeText(MainActivity.this, "Please login to view appointments", 
                            Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, loginActivity.class));
                        return;
                    }

                    // Start AppointmentSummary activity
                    Intent intent = new Intent(MainActivity.this, AppointmentSummary.class);
                    startActivity(intent);
                } catch (Exception e) {
                    Log.e(TAG, "Error opening appointments: " + e.getMessage(), e);
                    Toast.makeText(MainActivity.this, "Error opening appointments: " + e.getMessage(), 
                        Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupHospitalDropdown() {
        autoCompleteTextView = findViewById(R.id.auto_complete_txt);
        adapterItems = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, hospitals);
        autoCompleteTextView.setAdapter(adapterItems);
        
        // Set threshold to show suggestions after 1 character
        autoCompleteTextView.setThreshold(1);
        
        // Handle item selection
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedHospital = (String) parent.getItemAtPosition(position);
                Toast.makeText(MainActivity.this, "Selected: " + selectedHospital, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupButtonClickListeners() {
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
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 0) {
                    performSearch(newText);
                }
                return true;
            }
        });

        // Set search view properties
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("Search hospitals, doctors, services...");
        searchView.setSubmitButtonEnabled(true);
    }

    private void performSearch(String query) {
        query = query.toLowerCase().trim();
        
        // Filter hospitals in the dropdown
        if (query.length() > 0) {
            String[] filteredHospitals = new String[hospitals.length];
            int count = 0;
            for (String hospital : hospitals) {
                if (hospital.toLowerCase().contains(query)) {
                    filteredHospitals[count++] = hospital;
                }
            }
            String[] result = new String[count];
            System.arraycopy(filteredHospitals, 0, result, 0, count);
            adapterItems = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, result);
            autoCompleteTextView.setAdapter(adapterItems);
            autoCompleteTextView.showDropDown();
        } else {
            adapterItems = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, hospitals);
            autoCompleteTextView.setAdapter(adapterItems);
        }

        // Check for action keywords
        if (query.contains("doctor") || query.contains("appointment")) {
            startActivity(new Intent(this, RecycleActivity.class));
        } else if (query.contains("test") || query.contains("service")) {
            startActivity(new Intent(this, Doctor_Info.class));
        } else if (query.contains("checkup") || query.contains("health")) {
            startActivity(new Intent(this, SlotBooking.class));
        }
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