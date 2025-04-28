package com.example.hinduja_health_first;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyDocAdapter extends RecyclerView.Adapter<MyDocAdapter.MyDocViewHolder> {
    private static final String TAG = "MyDocAdapter";
    private Context context;
    private List<Doc_item> items;

    public MyDocAdapter(Context context, List<Doc_item> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public MyDocViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.doc_item_view, parent, false);
        return new MyDocViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyDocViewHolder holder, int position) {
        try {
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
                        
                        // Add current date as selected date
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        String currentDate = dateFormat.format(new Date());
                        intent.putExtra("SELECTED_DATE", currentDate);
                        
                        // Start the SlotBooking activity
                        context.startActivity(intent);
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

    public void updateList(List<Doc_item> newList) {
        items = newList;
        notifyDataSetChanged();
    }

    public static class MyDocViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameView;
        TextView specialtyView;
        TextView experienceView;

        public MyDocViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.docimageview);
            nameView = itemView.findViewById(R.id.docname);
            specialtyView = itemView.findViewById(R.id.docSpecs);
            experienceView = itemView.findViewById(R.id.docExp);
        }
    }
}
