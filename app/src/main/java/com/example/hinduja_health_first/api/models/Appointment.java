package com.example.hinduja_health_first.api.models;

import com.google.gson.annotations.SerializedName;

public class Appointment {
    @SerializedName("id")
    private String id;

    @SerializedName("patientId")
    private String patientId;

    @SerializedName("doctorId")
    private String doctorId;

    @SerializedName("appointmentTime")
    private String appointmentTime;

    @SerializedName("status")
    private String status;

    @SerializedName("patientName")
    private String patientName;

    @SerializedName("doctorName")
    private String doctorName;

    // Constructor
    public Appointment(String patientId, String doctorId, String appointmentTime, String patientName, String doctorName) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentTime = appointmentTime;
        this.status = "SCHEDULED";
        this.patientName = patientName;
        this.doctorName = doctorName;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
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

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }
} 