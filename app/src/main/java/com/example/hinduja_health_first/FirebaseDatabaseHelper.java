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
    private static FirebaseDatabaseHelper instance;
    private final DatabaseReference databaseReference;

    // Database paths
    private static final String APPOINTMENTS_PATH = "appointments";
    private static final String BOOKED_SLOTS_PATH = "booked_slots";

    // Interface for time slot availability check
    public interface OnTimeSlotCheckListener {
        void onTimeSlotChecked(boolean isAvailable);
    }

    // Interface for operation completion
    public interface OnCompleteListener {
        void onSuccess();
        void onFailure(Exception e);
    }

    // Interface for appointments loaded
    public interface OnAppointmentsLoadedListener {
        void onAppointmentsLoaded(List<Patient> appointments);
    }

    private FirebaseDatabaseHelper() {
        try {
            databaseReference = FirebaseDatabase.getInstance().getReference();
        } catch (Exception e) {
            Log.e(TAG, "Error initializing Firebase: " + e.getMessage());
            throw new RuntimeException("Failed to initialize Firebase", e);
        }
    }

    public static synchronized FirebaseDatabaseHelper getInstance() {
        if (instance == null) {
            instance = new FirebaseDatabaseHelper();
        }
        return instance;
    }

    public void checkTimeSlotAvailability(String doctorName, String date, String timeSlot, 
            OnTimeSlotCheckListener listener) {
        if (doctorName == null || date == null || timeSlot == null) {
            Log.e(TAG, "Invalid parameters for time slot check");
            listener.onTimeSlotChecked(false);
            return;
        }

        String slotKey = generateSlotKey(doctorName, date, timeSlot);
        
        databaseReference.child(BOOKED_SLOTS_PATH)
            .child(slotKey)
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    listener.onTimeSlotChecked(!dataSnapshot.exists());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG, "Error checking time slot: " + databaseError.getMessage());
                    listener.onTimeSlotChecked(false);
                }
            });
    }

    public void savePatientAppointment(Patient patient, OnCompleteListener listener) {
        if (patient == null) {
            Log.e(TAG, "Patient object is null");
            listener.onFailure(new IllegalArgumentException("Patient object cannot be null"));
            return;
        }

        try {
            // Generate a unique key for the appointment
            String appointmentId = databaseReference.child(APPOINTMENTS_PATH).push().getKey();
            if (appointmentId == null) {
                throw new RuntimeException("Failed to generate appointment ID");
            }
            patient.setId(appointmentId);

            // Save appointment details
            databaseReference.child(APPOINTMENTS_PATH)
                .child(appointmentId)
                .setValue(patient)
                .addOnSuccessListener(aVoid -> {
                    // Mark the slot as booked
                    String slotKey = generateSlotKey(
                        patient.getDoctorName(),
                        patient.getAppointmentDate(),
                        patient.getAppointmentTime()
                    );
                    
                    databaseReference.child(BOOKED_SLOTS_PATH)
                        .child(slotKey)
                        .setValue(true)
                        .addOnSuccessListener(aVoid1 -> listener.onSuccess())
                        .addOnFailureListener(e -> {
                            Log.e(TAG, "Error saving booked slot: " + e.getMessage());
                            listener.onFailure(e);
                        });
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error saving appointment: " + e.getMessage());
                    listener.onFailure(e);
                });
        } catch (Exception e) {
            Log.e(TAG, "Error in savePatientAppointment: " + e.getMessage());
            listener.onFailure(e);
        }
    }

    private String generateSlotKey(String doctorName, String date, String timeSlot) {
        if (doctorName == null || date == null || timeSlot == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }
        return doctorName + "_" + date + "_" + timeSlot.replace(":", "_");
    }

    public void getPatientAppointments(String patientEmail, OnAppointmentsLoadedListener listener) {
        if (patientEmail == null || patientEmail.isEmpty()) {
            Log.e(TAG, "Invalid patient email");
            listener.onAppointmentsLoaded(new ArrayList<>());
            return;
        }

        databaseReference.child(APPOINTMENTS_PATH)
            .orderByChild("email")
            .equalTo(patientEmail)
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<Patient> appointments = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        try {
                            Patient appointment = snapshot.getValue(Patient.class);
                            if (appointment != null) {
                                appointments.add(appointment);
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Error parsing appointment: " + e.getMessage());
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
} 