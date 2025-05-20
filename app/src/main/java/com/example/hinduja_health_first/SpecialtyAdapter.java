package com.example.hinduja_health_first;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SpecialtyAdapter extends RecyclerView.Adapter<SpecialtyAdapter.SpecialtyViewHolder> {
    private List<String> specialties;
    private Context context;
    private OnSpecialtyClickListener listener;

    public interface OnSpecialtyClickListener {
        void onSpecialtyClick(String specialty);
    }

    public SpecialtyAdapter(Context context, List<String> specialties, OnSpecialtyClickListener listener) {
        this.context = context;
        this.specialties = specialties;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SpecialtyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_specialty, parent, false);
        return new SpecialtyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpecialtyViewHolder holder, int position) {
        String specialty = specialties.get(position);
        holder.specialtyName.setText(specialty);
        
        // Set the appropriate image based on the specialty
        switch (specialty.toLowerCase()) {
            case "cardiologist":
                holder.specialtyImage.setImageResource(R.drawable.c);
                break;
            case "dermatologist":
                holder.specialtyImage.setImageResource(R.drawable.dm);
                break;
            case "diabetologist":
                holder.specialtyImage.setImageResource(R.drawable.db);
                break;
            case "gastroenterologist":
                holder.specialtyImage.setImageResource(R.drawable.g);
                break;
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSpecialtyClick(specialty);
            }
        });
    }

    @Override
    public int getItemCount() {
        return specialties.size();
    }

    static class SpecialtyViewHolder extends RecyclerView.ViewHolder {
        ImageView specialtyImage;
        TextView specialtyName;

        SpecialtyViewHolder(@NonNull View itemView) {
            super(itemView);
            specialtyImage = itemView.findViewById(R.id.specialtyImage);
            specialtyName = itemView.findViewById(R.id.specialtyName);
        }
    }
} 