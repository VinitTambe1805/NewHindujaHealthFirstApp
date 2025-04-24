package com.example.hinduja_health_first;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private static final String TAG = "MyAdapter";
    Context context;
    List<item> items;

    public MyAdapter(Context context, List<item> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        try {
            return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view, parent, false));
        } catch (Exception e) {
            Log.e(TAG, "Error in onCreateViewHolder: " + e.getMessage(), e);
            Toast.makeText(context, "Error creating view: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            throw e;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        try {
            holder.nameView.setText(items.get(position).getName());
            holder.imageView.setImageResource(items.get(position).getImage());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        String specialty = items.get(position).getName();
                        Log.d(TAG, "Clicked specialty: " + specialty);
                        
                        Intent i = new Intent(context, Doctor_Info.class);
                        i.putExtra("SPECIALTY", specialty);
                        context.startActivity(i);
                    } catch (Exception e) {
                        Log.e(TAG, "Error in onClick: " + e.getMessage(), e);
                        Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error in onBindViewHolder: " + e.getMessage(), e);
            Toast.makeText(context, "Error binding view: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
