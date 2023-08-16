package com.example.mallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class ChooseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        // TODO: Remove this button and its functions before release
        setupNoLoginButton();

        // Find and set up the login button
        setupLoginButton();
    }

    // TODO: Delete this method and related views before release
    private void setupNoLoginButton() {
        MaterialButton noLogin = findViewById(R.id.no_login);
        noLogin.setOnClickListener(view -> {
            Intent intent = new Intent(ChooseActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }

    private void setupLoginButton() {
        MaterialButton logInButton = findViewById(R.id.choose_log_in_btn);
        logInButton.setOnClickListener(v -> {
            // Open LogInActivity
            Intent intent = new Intent(ChooseActivity.this, LogInActivity.class);
            startActivity(intent);
        });
    }
}
