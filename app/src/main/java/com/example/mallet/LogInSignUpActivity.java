package com.example.mallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LogInSignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_login_signup);

        setupLoginButton();
        setupSignUpButton();
        setupNoLogInButton();

        // Initialize views
        TextView pulsatingTextView = findViewById(R.id.choose_logo_tv);

        // Apply pulsating animation to logo
        Animation pulsateAnimation = AnimationUtils.loadAnimation(this, R.anim.pulse_anim);
        pulsatingTextView.startAnimation(pulsateAnimation);
    }

    private void setupLoginButton() {
        Button logInButton = findViewById(R.id.choose_log_in_btn);
        logInButton.setOnClickListener(v -> {
            // Open LogInActivity
            Intent intent = new Intent(LogInSignUpActivity.this, LogInActivity.class);
            startActivity(intent);
        });
    }

    private void setupSignUpButton() {
        Button signUpButton = findViewById(R.id.choose_sign_up_btn);
        signUpButton.setOnClickListener(v -> {
            // Open SignUpActivity
            Intent intent = new Intent(LogInSignUpActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }

    private void setupNoLogInButton() {
        Button noLogInButton = findViewById(R.id.choose_no_log_in_btn);
        noLogInButton.setOnClickListener(v -> {
            // Open MainActivity
            Intent intent = new Intent(LogInSignUpActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }
}
