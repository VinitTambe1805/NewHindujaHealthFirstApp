package com.example.hinduja_health_first;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TestMemoQueueAdapter extends RecyclerView.Adapter<TestMemoQueueAdapter.ViewHolder> {
    private List<SharedPrefManager.TestMemo> testMemos;
    private SimpleDateFormat dateFormat;

    public TestMemoQueueAdapter(List<SharedPrefManager.TestMemo> testMemos) {
        this.testMemos = testMemos;
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_test_memo_queue, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SharedPrefManager.TestMemo testMemo = testMemos.get(position);
        
        // Use the patient ID from the TestMemo object
        holder.patientId.setText("Patient ID: " + testMemo.getPatientId());
        
        holder.department.setText("Department: " + testMemo.getDepartment());
        
        // Format the tests list
        StringBuilder testsText = new StringBuilder("Tests:\n");
        for (String test : testMemo.getTests()) {
            testsText.append("• ").append(test).append("\n");
        }
        holder.tests.setText(testsText.toString());
        
        // Set timestamp
        holder.timestamp.setText("Created: " + dateFormat.format(testMemo.getTimestamp()));
    }

    @Override
    public int getItemCount() {
        return testMemos.size();
    }

    public void updateData(List<SharedPrefManager.TestMemo> newTestMemos) {
        this.testMemos = newTestMemos;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView patientId;
        TextView department;
        TextView tests;
        TextView timestamp;

        ViewHolder(View view) {
            super(view);
            patientId = view.findViewById(R.id.patientId);
            department = view.findViewById(R.id.department);
            tests = view.findViewById(R.id.tests);
            timestamp = view.findViewById(R.id.timestamp);
        }
    }
} 