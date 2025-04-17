package com.example.hinduja_health_first;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class Splashscreen extends AppCompatActivity {

    private VideoView splashVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        splashVideo = findViewById(R.id.splashVideo);

        // Set video URI
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.v1);
        splashVideo.setVideoURI(videoUri);

        // Start playing
        splashVideo.start();

        // When video finishes, go to MainActivity
        splashVideo.setOnCompletionListener(mediaPlayer -> {
            startActivity(new Intent(Splashscreen.this, loginActivity.class));
            finish();
        });
    }
}