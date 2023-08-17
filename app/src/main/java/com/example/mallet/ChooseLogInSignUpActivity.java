package com.example.mallet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

@SuppressLint("CustomSplashScreen")
public class ChooseLogInSignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_login_signup);

        setupLoginButton();
        setupSignUpButton();
        setupNoLogInButton();

    }

    private void setupLoginButton() {
        MaterialButton logInButton = findViewById(R.id.splash_log_in_btn);
        logInButton.setOnClickListener(v -> {
            // Open LogInActivity
            Intent intent = new Intent(ChooseLogInSignUpActivity.this, LogInActivity.class);
            startActivity(intent);
        });
    }

    private void setupSignUpButton() {
        MaterialButton logInButton = findViewById(R.id.splash_sign_up_btn);
        logInButton.setOnClickListener(v -> {
            // Open LogInActivity
            Intent intent = new Intent(ChooseLogInSignUpActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }

    // TODO: Delete this and everything connected
    private void setupNoLogInButton() {
        MaterialButton logInButton = findViewById(R.id.splash_no_login_btn);
        logInButton.setOnClickListener(v -> {
            // Open LogInActivity
            Intent intent = new Intent(ChooseLogInSignUpActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }
}
