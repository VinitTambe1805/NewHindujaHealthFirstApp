package com.example.hinduja_health_first;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DepartmentAdapter extends RecyclerView.Adapter<DepartmentAdapter.ViewHolder> {
    private List<String> departments;

    public DepartmentAdapter(List<String> departments) {
        this.departments = departments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_department, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String department = departments.get(position);
        holder.departmentName.setText(department);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), SelectTestActivity.class);
            intent.putExtra("department", department);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return departments.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView departmentName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            departmentName = itemView.findViewById(R.id.departmentName);
        }
    }
} 