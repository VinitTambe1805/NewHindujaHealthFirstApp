package com.example.hinduja_health_first;

import java.util.Date;

public class Patient {
    private String id;
    private String name;
    private String email;
    private String phoneNumber;
    private String doctorName;
    private String doctorSpecialty;
    private String appointmentDate;
    private String appointmentTime;
    private String status; // "booked", "completed", "cancelled"
    private long timestamp;

    // Default constructor required for Firebase
    public Patient() {
        this.timestamp = new Date().getTime();
        this.status = "booked";
    }

    public Patient(String id, String name, String email, String phoneNumber, 
                  String doctorName, String doctorSpecialty, 
                  String appointmentDate, String appointmentTime) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.doctorName = doctorName;
        this.doctorSpecialty = doctorSpecialty;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.status = "booked";
        this.timestamp = new Date().getTime();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email address");
        }
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber != null && !phoneNumber.matches("\\d{10}")) {
            throw new IllegalArgumentException("Invalid phone number format");
        }
        this.phoneNumber = phoneNumber;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        if (doctorName == null || doctorName.trim().isEmpty()) {
            throw new IllegalArgumentException("Doctor name cannot be null or empty");
        }
        this.doctorName = doctorName;
    }

    public String getDoctorSpecialty() {
        return doctorSpecialty;
    }

    public void setDoctorSpecialty(String doctorSpecialty) {
        if (doctorSpecialty == null || doctorSpecialty.trim().isEmpty()) {
            throw new IllegalArgumentException("Doctor specialty cannot be null or empty");
        }
        this.doctorSpecialty = doctorSpecialty;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        if (appointmentDate == null || appointmentDate.trim().isEmpty()) {
            throw new IllegalArgumentException("Appointment date cannot be null or empty");
        }
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        if (appointmentTime == null || appointmentTime.trim().isEmpty()) {
            throw new IllegalArgumentException("Appointment time cannot be null or empty");
        }
        this.appointmentTime = appointmentTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (status == null || !(status.equals("booked") || status.equals("completed") || status.equals("cancelled"))) {
            throw new IllegalArgumentException("Invalid status value");
        }
        this.status = status;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
} 