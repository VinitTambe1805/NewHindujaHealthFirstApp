package com.example.hinduja_health_first;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.TestViewHolder> {
    private List<String> tests;
    private List<String> selectedTests;

    public TestAdapter(List<String> tests) {
        this.tests = tests;
        this.selectedTests = new ArrayList<>();
    }

    @NonNull
    @Override
    public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_test, parent, false);
        return new TestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestViewHolder holder, int position) {
        String test = tests.get(position);
        holder.testCheckBox.setText(test);
        holder.testCheckBox.setChecked(selectedTests.contains(test));

        holder.testCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (!selectedTests.contains(test)) {
                    selectedTests.add(test);
                }
            } else {
                selectedTests.remove(test);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tests.size();
    }

    public List<String> getSelectedTests() {
        return new ArrayList<>(selectedTests);
    }

    static class TestViewHolder extends RecyclerView.ViewHolder {
        CheckBox testCheckBox;

        TestViewHolder(View itemView) {
            super(itemView);
            testCheckBox = itemView.findViewById(R.id.testCheckBox);
        }
    }
} 