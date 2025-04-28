package com.example.hinduja_health_first;

import java.util.Date;

public class Patient {
    private String patientId;
    private String name;
    private String email;
    private String phone;
    private String doctorName;
    private String specialty;
    private String appointmentDate;
    private String appointmentTime;
    private String status; // "booked", "completed", "cancelled"
    private long timestamp;

    // Default constructor required for Firebase
    public Patient() {
    }

    public Patient(String patientId, String name, String email, String phone, 
                  String doctorName, String specialty, String appointmentDate, 
                  String appointmentTime) {
        this.patientId = patientId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.doctorName = doctorName;
        this.specialty = specialty;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.status = "booked";
        this.timestamp = new Date().getTime();
    }

    // Getters and Setters
    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
} 