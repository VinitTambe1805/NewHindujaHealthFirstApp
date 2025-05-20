package com.example.hinduja_health_first;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DepartmentAdapter extends RecyclerView.Adapter<DepartmentAdapter.DepartmentViewHolder> {
    private List<String> departments;
    private Context context;
    private OnDepartmentClickListener listener;

    public interface OnDepartmentClickListener {
        void onDepartmentClick(String department);
    }

    public DepartmentAdapter(Context context, List<String> departments, OnDepartmentClickListener listener) {
        this.context = context;
        this.departments = departments;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DepartmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_department, parent, false);
        return new DepartmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DepartmentViewHolder holder, int position) {
        String department = departments.get(position);
        holder.departmentName.setText(department);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDepartmentClick(department);
            }
        });
    }

    @Override
    public int getItemCount() {
        return departments.size();
    }

    static class DepartmentViewHolder extends RecyclerView.ViewHolder {
        TextView departmentName;

        DepartmentViewHolder(@NonNull View itemView) {
            super(itemView);
            departmentName = itemView.findViewById(R.id.departmentName);
        }
    }
} 