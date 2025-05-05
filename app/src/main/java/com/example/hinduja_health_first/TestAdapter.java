package com.example.hinduja_health_first;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHolder> {
    private List<String> tests;

    public TestAdapter(List<String> tests) {
        this.tests = tests;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_test, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.testName.setText(tests.get(position));
    }

    @Override
    public int getItemCount() {
        return tests.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView testName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            testName = itemView.findViewById(R.id.testName);
        }
    }
} 