package com.example.hinduja_health_first.api.models;

import com.google.gson.annotations.SerializedName;

public class Doctor {
    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("specialty")
    private String specialty;

    @SerializedName("experience")
    private String experience;

    @SerializedName("availableSlots")
    private String[] availableSlots;

    public Doctor(String id, String name, String specialty, String experience) {
        this.id = id;
        this.name = name;
        this.specialty = specialty;
        this.experience = experience;
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
        this.name = name;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String[] getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(String[] availableSlots) {
        this.availableSlots = availableSlots;
    }
} 