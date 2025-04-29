package com.example.hinduja_health_first;

import android.app.Application;
import com.google.firebase.FirebaseApp;

public class HindujaApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
}