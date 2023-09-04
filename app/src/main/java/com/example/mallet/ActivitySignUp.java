package com.example.mallet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mallet.databinding.ActivitySignUpBinding;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class ActivitySignUp extends AppCompatActivity {

    ActivitySignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupLogInField();

        // Initialize views
        TextInputEditText editTextEmail = binding.signUpEmailEt;
        TextView textViewError = binding.signUpErrorTv;
        Button confirmLogIn = binding.signUpConfirmBtn;
        TextView pulsatingTV = binding.signUpLogo;
        Animation pulseAnimation = AnimationUtils.loadAnimation(this, R.anim.pulse_anim);
        pulsatingTV.startAnimation(pulseAnimation);

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
    private void validateEmail(String email, TextView errorTV) {
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorTV.setVisibility(View.GONE);
        } else {
            errorTV.setVisibility(View.VISIBLE);
        }
    }

    private void setupLogInField() {
        TextView signUp = binding.logInBtn;

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
