package com.example.hinduja_health_first;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder> {
    private List<Doctor> doctorsList;

    public DoctorAdapter(List<Doctor> doctorsList) {
        this.doctorsList = doctorsList;
    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_doctor, parent, false);
        return new DoctorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
        Doctor doctor = doctorsList.get(position);
        holder.doctorName.setText(doctor.getName());
        holder.doctorSpecialty.setText(doctor.getSpecialty());
        holder.doctorExperience.setText(doctor.getExperience());
    }

    @Override
    public int getItemCount() {
        return doctorsList.size();
    }

    public void updateList(List<Doctor> newList) {
        this.doctorsList = newList;
        notifyDataSetChanged();
    }

    static class DoctorViewHolder extends RecyclerView.ViewHolder {
        TextView doctorName;
        TextView doctorSpecialty;
        TextView doctorExperience;

        DoctorViewHolder(View itemView) {
            super(itemView);
            doctorName = itemView.findViewById(R.id.doctorName);
            doctorSpecialty = itemView.findViewById(R.id.doctorSpecialty);
            doctorExperience = itemView.findViewById(R.id.doctorExperience);
        }
    }
} 