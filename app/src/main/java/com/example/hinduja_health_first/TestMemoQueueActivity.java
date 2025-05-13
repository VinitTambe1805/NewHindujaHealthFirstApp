package com.example.hinduja_health_first;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TestMemoQueueActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TestMemoQueueAdapter adapter;
    private SharedPrefManager sharedPrefManager;
    private TextView queueCount;
    private ImageButton backButton;
    private MaterialButton backToHomeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_memo_queue);

        sharedPrefManager = SharedPrefManager.getInstance(this);
        recyclerView = findViewById(R.id.queueRecyclerView);
        queueCount = findViewById(R.id.queueCount);
        backButton = findViewById(R.id.backButton);
        backToHomeButton = findViewById(R.id.backToHomeButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get all test memos and sort them by timestamp (most recent first)
        List<SharedPrefManager.TestMemo> testMemos = sharedPrefManager.getAllTestMemos();
        Collections.sort(testMemos, new Comparator<SharedPrefManager.TestMemo>() {
            @Override
            public int compare(SharedPrefManager.TestMemo m1, SharedPrefManager.TestMemo m2) {
                return Long.compare(m2.getTimestamp(), m1.getTimestamp());
            }
        });

        // Update queue count
        updateQueueCount(testMemos.size());

        adapter = new TestMemoQueueAdapter(testMemos);
        recyclerView.setAdapter(adapter);

        // Set up back button
        backButton.setOnClickListener(v -> finish());

        // Set up back to home button
        backToHomeButton.setOnClickListener(v -> {
            Intent intent = new Intent(TestMemoQueueActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }

    private void updateQueueCount(int count) {
        String countText = count + " patient" + (count != 1 ? "s" : "") + " in queue";
        queueCount.setText(countText);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the queue when activity resumes
        List<SharedPrefManager.TestMemo> testMemos = sharedPrefManager.getAllTestMemos();
        Collections.sort(testMemos, new Comparator<SharedPrefManager.TestMemo>() {
            @Override
            public int compare(SharedPrefManager.TestMemo m1, SharedPrefManager.TestMemo m2) {
                return Long.compare(m2.getTimestamp(), m1.getTimestamp());
            }
        });
        adapter.updateData(testMemos);
        updateQueueCount(testMemos.size());
    }
} 