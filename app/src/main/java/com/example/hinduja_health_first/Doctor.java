package com.example.hinduja_health_first;

public class Doctor {
    private String name;
    private String specialty;
    private String experience;

    public Doctor(String name, String specialty, String experience) {
        this.name = name;
        this.specialty = specialty;
        this.experience = experience;
    }

    public String getName() {
        return name;
    }

    public String getSpecialty() {
        return specialty;
    }

    public String getExperience() {
        return experience;
    }
} 