package com.example.hinduja_health_first;

public class Doc_item {

    String name;
    String Specialty;
    String experience;
    int image;

    public Doc_item(String name, String specialty, String experience, int image) {
        this.name = name;
        Specialty = specialty;
        this.experience = experience;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialty() {
        return Specialty;
    }

    public void setSpecialty(String specialty) {
        Specialty = specialty;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}