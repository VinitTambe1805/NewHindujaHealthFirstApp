package com.example.hinduja_health_first;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SharedPrefManager {
    private static final String PREF_NAME = "HindujaAppPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_EMAIL = "key_email";
    private static final String KEY_NAME = "key_name";
    private static final String KEY_PHONE = "key_phone";
    private static final String KEY_APPOINTMENTS = "appointments";
    private static final String KEY_TEST_MEMOS = "test_memos";

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

    public void saveTestMemo(String department, List<String> tests) {
        try {
            // Get existing test memos
            String testMemosJson = sharedPreferences.getString(KEY_TEST_MEMOS, "[]");
            JSONArray testMemosArray = new JSONArray(testMemosJson);

            // Create new test memo object
            JSONObject newTestMemo = new JSONObject();
            newTestMemo.put("department", department);
            newTestMemo.put("tests", new JSONArray(tests));
            newTestMemo.put("timestamp", System.currentTimeMillis());
            newTestMemo.put("patientId", "PID" + String.format("%06d", testMemosArray.length() + 1));

            // Add new test memo to array
            testMemosArray.put(newTestMemo);

            // Save updated test memos
            sharedPreferences.edit().putString(KEY_TEST_MEMOS, testMemosArray.toString()).apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public List<TestMemo> getAllTestMemos() {
        List<TestMemo> testMemos = new ArrayList<>();
        try {
            String testMemosJson = sharedPreferences.getString(KEY_TEST_MEMOS, "[]");
            JSONArray testMemosArray = new JSONArray(testMemosJson);

            for (int i = 0; i < testMemosArray.length(); i++) {
                JSONObject memoObj = testMemosArray.getJSONObject(i);
                String department = memoObj.getString("department");
                JSONArray testsArray = memoObj.getJSONArray("tests");
                List<String> tests = new ArrayList<>();
                for (int j = 0; j < testsArray.length(); j++) {
                    tests.add(testsArray.getString(j));
                }
                long timestamp = memoObj.getLong("timestamp");
                String patientId = memoObj.getString("patientId");

                TestMemo memo = new TestMemo(department, tests);
                memo.timestamp = timestamp;
                memo.patientId = patientId;
                testMemos.add(memo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return testMemos;
    }

    public static class TestMemo {
        private String department;
        private List<String> tests;
        private long timestamp;
        private String patientId;

        public TestMemo(String department, List<String> tests) {
            this.department = department;
            this.tests = tests;
            this.timestamp = System.currentTimeMillis();
        }

        public String getDepartment() {
            return department;
        }

        public List<String> getTests() {
            return tests;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public String getPatientId() {
            return patientId;
        }
    }

    public void clearTestMemos() {
        sharedPreferences.edit().remove(KEY_TEST_MEMOS).apply();
    }
} 