package com.example.hinduja_health_first;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TestMemoQueueAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    
    private List<Object> items;
    private SimpleDateFormat dateFormat;

    public TestMemoQueueAdapter(List<SharedPrefManager.TestMemo> testMemos) {
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        organizeItems(testMemos);
    }

    // Add a simple model for header data
    static class DepartmentHeader {
        String departmentName;
        int count;
        DepartmentHeader(String departmentName, int count) {
            this.departmentName = departmentName;
            this.count = count;
        }
    }

    private void organizeItems(List<SharedPrefManager.TestMemo> testMemos) {
        items = new ArrayList<>();
        Map<String, List<SharedPrefManager.TestMemo>> groupedMemos = new HashMap<>();

        // Group test memos by department
        for (SharedPrefManager.TestMemo memo : testMemos) {
            String department = memo.getDepartment();
            if (department != null && !department.isEmpty()) {
                if (!groupedMemos.containsKey(department)) {
                    groupedMemos.put(department, new ArrayList<>());
                }
                groupedMemos.get(department).add(memo);
            }
        }

        // Add headers and items to the list
        for (Map.Entry<String, List<SharedPrefManager.TestMemo>> entry : groupedMemos.entrySet()) {
            items.add(new DepartmentHeader(entry.getKey(), entry.getValue().size())); // Add header object
            items.addAll(entry.getValue()); // Add items
        }
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) instanceof DepartmentHeader ? TYPE_HEADER : TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_test_memo_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_test_memo_queue, parent, false);
            return new ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            DepartmentHeader header = (DepartmentHeader) items.get(position);
            headerHolder.departmentName.setText(header.departmentName);
            headerHolder.patientCount.setText("Patients in Queue: " + header.count);
        } else if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemHolder = (ItemViewHolder) holder;
            SharedPrefManager.TestMemo testMemo = (SharedPrefManager.TestMemo) items.get(position);
            
            itemHolder.patientId.setText("Patient ID: " + testMemo.getPatientId());
            StringBuilder testsText = new StringBuilder("Tests:\n");
            for (String test : testMemo.getTests()) {
                testsText.append("• ").append(test).append("\n");
            }
            itemHolder.tests.setText(testsText.toString());
            itemHolder.timestamp.setText("Created: " + dateFormat.format(testMemo.getTimestamp()));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateData(List<SharedPrefManager.TestMemo> newTestMemos) {
        organizeItems(newTestMemos);
        notifyDataSetChanged();
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView departmentName;
        TextView patientCount;

        HeaderViewHolder(View view) {
            super(view);
            departmentName = view.findViewById(R.id.departmentName);
            patientCount = view.findViewById(R.id.patientCount);
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView patientId;
        TextView tests;
        TextView timestamp;

        ItemViewHolder(View view) {
            super(view);
            patientId = view.findViewById(R.id.patientId);
            tests = view.findViewById(R.id.tests);
            timestamp = view.findViewById(R.id.timestamp);
        }
    }
} 