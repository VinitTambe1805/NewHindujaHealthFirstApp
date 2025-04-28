package com.example.hinduja_health_first;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AppointmentFragment extends Fragment {
    private TextView doctorNameText;
    private TextView doctorSpecialtyText;
    private TextView appointmentTimeText;
    private TextView locationText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment, container, false);

        // Initialize views
        doctorNameText = view.findViewById(R.id.doctorNameText);
        doctorSpecialtyText = view.findViewById(R.id.doctorSpecialtyText);
        appointmentTimeText = view.findViewById(R.id.appointmentTimeText);
        locationText = view.findViewById(R.id.locationText);

        // Get arguments from bundle
        Bundle args = getArguments();
        if (args != null) {
            String doctorName = args.getString("DOCTOR_NAME");
            String specialty = args.getString("DOCTOR_SPECIALTY");
            String appointmentTime = args.getString("APPOINTMENT_TIME");
            String location = args.getString("LOCATION");

            // Set appointment details
            if (doctorName != null) doctorNameText.setText(doctorName);
            if (specialty != null) doctorSpecialtyText.setText(specialty);
            if (appointmentTime != null) appointmentTimeText.setText(appointmentTime);
            if (location != null) locationText.setText(location);
        }

        return view;
    }
} 