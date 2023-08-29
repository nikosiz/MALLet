package com.example.mallet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class ActivitySignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setupLogInField();

        // Initialize views
        TextInputEditText editTextEmail = findViewById(R.id.sign_up_email_et);
        TextView textViewError = findViewById(R.id.sign_up_error_tv);
        Button confirmLogIn = findViewById(R.id.sign_up_confirm_btn);
        TextView pulsatingTextView = findViewById(R.id.sign_up_logo);
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
            Toast.makeText(ActivitySignUp.this, "Sign up button was clicked", Toast.LENGTH_SHORT).show();
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

    private void setupLogInField() {
        TextView signUp = findViewById(R.id.log_in_btn);

        signUp.setOnClickListener(v -> {
            Intent intent = new Intent(ActivitySignUp.this, ActivityLogIn.class);
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
        // Start ChooseLogInSignUpActivity and clear the back stack
        Intent intent = new Intent(ActivitySignUp.this, ActivityOpening.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish(); // Finish the SignUpActivity
    }
}
