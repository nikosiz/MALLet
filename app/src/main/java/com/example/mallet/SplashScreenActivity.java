package com.example.mallet;

import android.os.Bundle;
import android.os.Handler;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen); // Assuming you have a layout file for the splash screen

        // Add a Handler to post a delayed Runnable that starts MainActivity after 5 seconds
        new Handler().postDelayed(() -> {
            // Start MainActivity
            startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
            // Finish SplashScreenActivity to prevent going back to it when pressing back from MainActivity
            finish();
        }, 2500); // 5000 milliseconds = 5 seconds delay
    }
}
