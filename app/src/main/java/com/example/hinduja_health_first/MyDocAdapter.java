package com.example.hinduja_health_first;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyDocAdapter extends RecyclerView.Adapter<MyDocViewHolder> {

    Context context;
    List<Doc_item> items;

    public MyDocAdapter(Context context, List<Doc_item> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public MyDocViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyDocViewHolder(LayoutInflater.from(context).inflate(R.layout.doc_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyDocViewHolder holder, int position) {
        holder.nameView.setText(items.get(position).getName());
        holder.specialtyView.setText(items.get(position).getSpecialty());
        holder.experienceView.setText(items.get(position).getExperience());
        holder.imageView.setImageResource(items.get(position).getImage());

        // Add click listener to the entire item view
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Get the selected doctor's information
                    Doc_item selectedDoctor = items.get(holder.getAdapterPosition());
                    
                    // Create intent to open SlotBooking activity
                    Intent intent = new Intent(context, SlotBooking.class);
                    
                    // Pass doctor's information to SlotBooking activity
                    intent.putExtra("DOCTOR_NAME", selectedDoctor.getName());
                    intent.putExtra("DOCTOR_SPECIALTY", selectedDoctor.getSpecialty());
                    intent.putExtra("DOCTOR_EXPERIENCE", selectedDoctor.getExperience());
                    
                    // Start the SlotBooking activity
                    context.startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateList(List<Doc_item> newList) {
        items = newList;
        notifyDataSetChanged();
    }
}
