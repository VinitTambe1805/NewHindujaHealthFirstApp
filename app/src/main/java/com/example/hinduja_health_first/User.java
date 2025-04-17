package com.example.hinduja_health_first;

public class User {

    public String name, email, phone, password;

    public User() {} // Required for Firebase

    public User(String name, String email, String phone, String password) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }
}