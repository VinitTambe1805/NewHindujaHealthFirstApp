package com.example.hinduja_health_first;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SharedPrefManager {
    private static final String PREF_NAME = "HindujaAppPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_EMAIL = "key_email";
    private static final String KEY_NAME = "key_name";
    private static final String KEY_PHONE = "key_phone";
    private static final String KEY_APPOINTMENTS = "appointments";

    private static SharedPrefManager instance;
    private SharedPreferences sharedPreferences;

    private SharedPrefManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefManager(context);
        }
        return instance;
    }

    public void userLogin(User user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_EMAIL, user.email);
        editor.putString(KEY_NAME, user.name);
        editor.putString(KEY_PHONE, user.phone);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();
    }

    public User getUser() {
        return new User(
                sharedPreferences.getString(KEY_NAME, null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getString(KEY_PHONE, null),
                null // We don't store password in SharedPreferences
        );
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void setLoggedIn(boolean isLoggedIn) {
        sharedPreferences.edit().putBoolean(KEY_IS_LOGGED_IN, isLoggedIn).apply();
    }

    public void logout() {
        sharedPreferences.edit().clear().apply();
    }

    public void saveAppointment(String doctorName, String specialty, String appointmentTime, String location) {
        try {
            // Get existing appointments
            String appointmentsJson = sharedPreferences.getString(KEY_APPOINTMENTS, "[]");
            JSONArray appointmentsArray = new JSONArray(appointmentsJson);

            // Create new appointment object
            JSONObject newAppointment = new JSONObject();
            newAppointment.put("doctorName", doctorName);
            newAppointment.put("specialty", specialty);
            newAppointment.put("appointmentTime", appointmentTime);
            newAppointment.put("location", location);
            newAppointment.put("timestamp", System.currentTimeMillis());

            // Add new appointment to array
            appointmentsArray.put(newAppointment);

            // Save updated appointments
            sharedPreferences.edit().putString(KEY_APPOINTMENTS, appointmentsArray.toString()).apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public List<Appointment> getAllAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        try {
            String appointmentsJson = sharedPreferences.getString(KEY_APPOINTMENTS, "[]");
            JSONArray appointmentsArray = new JSONArray(appointmentsJson);

            for (int i = 0; i < appointmentsArray.length(); i++) {
                JSONObject appointmentObj = appointmentsArray.getJSONObject(i);
                Appointment appointment = new Appointment(
                    appointmentObj.getString("doctorName"),
                    appointmentObj.getString("specialty"),
                    appointmentObj.getString("appointmentTime"),
                    appointmentObj.getString("location"),
                    appointmentObj.getLong("timestamp")
                );
                appointments.add(appointment);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    public static class Appointment {
        private String doctorName;
        private String specialty;
        private String appointmentTime;
        private String location;
        private long timestamp;

        public Appointment(String doctorName, String specialty, String appointmentTime, String location, long timestamp) {
            this.doctorName = doctorName;
            this.specialty = specialty;
            this.appointmentTime = appointmentTime;
            this.location = location;
            this.timestamp = timestamp;
        }

        public String getDoctorName() {
            return doctorName;
        }

        public String getSpecialty() {
            return specialty;
        }

        public String getAppointmentTime() {
            return appointmentTime;
        }

        public String getLocation() {
            return location;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }
} 