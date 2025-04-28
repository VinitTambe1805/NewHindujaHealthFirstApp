package com.example.hinduja_health_first;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDatabaseHelper {
    private static final String TAG = "FirebaseDatabaseHelper";
    private DatabaseReference databaseReference;
    private static FirebaseDatabaseHelper instance;

    private FirebaseDatabaseHelper() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public static synchronized FirebaseDatabaseHelper getInstance() {
        if (instance == null) {
            instance = new FirebaseDatabaseHelper();
        }
        return instance;
    }

    // Save patient appointment
    public void savePatientAppointment(Patient patient, OnCompleteListener listener) {
        String appointmentKey = databaseReference.child("appointments").push().getKey();
        patient.setPatientId(appointmentKey);
        
        databaseReference.child("appointments").child(appointmentKey)
            .setValue(patient)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Also save the time slot as booked
                    saveBookedTimeSlot(patient.getDoctorName(), 
                                     patient.getAppointmentDate(), 
                                     patient.getAppointmentTime());
                    listener.onSuccess();
                } else {
                    listener.onFailure(task.getException());
                }
            });
    }

    // Save booked time slot
    private void saveBookedTimeSlot(String doctorName, String date, String time) {
        String timeSlotKey = doctorName + "_" + date + "_" + time;
        databaseReference.child("booked_slots").child(timeSlotKey)
            .setValue(true);
    }

    // Check if time slot is available
    public void checkTimeSlotAvailability(String doctorName, String date, String time, 
                                        OnTimeSlotCheckListener listener) {
        String timeSlotKey = doctorName + "_" + date + "_" + time;
        
        databaseReference.child("booked_slots").child(timeSlotKey)
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    boolean isBooked = dataSnapshot.exists();
                    listener.onTimeSlotChecked(!isBooked);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG, "Error checking time slot: " + databaseError.getMessage());
                    listener.onTimeSlotChecked(false);
                }
            });
    }

    // Get all appointments for a patient
    public void getPatientAppointments(String patientEmail, OnAppointmentsLoadedListener listener) {
        databaseReference.child("appointments")
            .orderByChild("email")
            .equalTo(patientEmail)
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<Patient> appointments = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Patient appointment = snapshot.getValue(Patient.class);
                        if (appointment != null) {
                            appointments.add(appointment);
                        }
                    }
                    listener.onAppointmentsLoaded(appointments);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG, "Error loading appointments: " + databaseError.getMessage());
                    listener.onAppointmentsLoaded(new ArrayList<>());
                }
            });
    }

    // Interfaces for callbacks
    public interface OnCompleteListener {
        void onSuccess();
        void onFailure(Exception e);
    }

    public interface OnTimeSlotCheckListener {
        void onTimeSlotChecked(boolean isAvailable);
    }

    public interface OnAppointmentsLoadedListener {
        void onAppointmentsLoaded(List<Patient> appointments);
    }
} 