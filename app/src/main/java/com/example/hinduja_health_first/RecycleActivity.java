package com.example.hinduja_health_first;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecycleActivity extends AppCompatActivity {
    private List<item> allItems;
    private MyAdapter adapter;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle);

        // Initialize search view
        searchView = findViewById(R.id.search_bar2);
        setupSearchView();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        allItems = new ArrayList<item>();
        allItems.add(new item("Cardiologist", R.drawable.c));
        allItems.add(new item("Dermatologist", R.drawable.dt));
        allItems.add(new item("Diabetologist", R.drawable.db));
        allItems.add(new item("Gastroenterologist", R.drawable.g));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyAdapter(this, allItems);
        recyclerView.setAdapter(adapter);
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterSpecialties(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterSpecialties(newText);
                return true;
            }
        });

        // Set search view properties
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("Search specialties...");
        searchView.setSubmitButtonEnabled(true);
    }

    private void filterSpecialties(String query) {
        List<item> filteredList = new ArrayList<>();
        query = query.toLowerCase().trim();

        for (item specialty : allItems) {
            if (specialty.getName().toLowerCase().contains(query)) {
                filteredList.add(specialty);
            }
        }

        adapter.updateList(filteredList);
    }
}




