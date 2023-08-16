package com.example.mallet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class LogInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        // Initialize views
        TextInputEditText editTextEmail = findViewById(R.id.log_in_email_et);
        TextView textViewError = findViewById(R.id.log_in_error_tv);
        Button confirmLogIn = findViewById(R.id.log_in_confirm_btn);
        TextView forgotPassword = findViewById(R.id.log_in_forgot_password_tv);
        TextView pulsatingTextView = findViewById(R.id.log_in_logo);
        Animation pulsateAnimation = AnimationUtils.loadAnimation(this, R.anim.pulse_anim);
        pulsatingTextView.startAnimation(pulsateAnimation);

        // Validate email on focus change
        editTextEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                validateEmail(Objects.requireNonNull(editTextEmail.getText()).toString(), textViewError);
            }
        });

        // Handle log in button click
        confirmLogIn.setOnClickListener(v -> {
            // Show a Toast message when the button is clicked
            Toast.makeText(LogInActivity.this, "Log in material button clicked", Toast.LENGTH_SHORT).show();
        });

        // Handle forgot password click
        forgotPassword.setOnClickListener(v -> {
            // Show a Toast message when the button is clicked
            Toast.makeText(LogInActivity.this, "Forgot password field clicked", Toast.LENGTH_SHORT).show();
        });
    }

    // Validate email format
    private void validateEmail(String email, TextView errorTextView) {
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorTextView.setVisibility(View.GONE);
        } else {
            errorTextView.setVisibility(View.VISIBLE);
        }
    }
}
