package com.example.mallet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class LogInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        //Set up remember checkbox
        setupRememberCheckBox();

        //Set up confirm login button
        setupLogInButton();

        // Set up social login buttons
        setupGoogleButton();
        setupFacebookButton();

        setupSignUpField();

        // Initialize views
        TextView pulsatingTextView = findViewById(R.id.log_in_logo);
        TextInputEditText editTextEmail = findViewById(R.id.log_in_email_et);
        TextView textViewError = findViewById(R.id.log_in_error_tv);
        TextView forgotPassword = findViewById(R.id.log_in_forgot_password_tv);

        // Apply pulsating animation to logo
        Animation pulsateAnimation = AnimationUtils.loadAnimation(this, R.anim.pulse_anim);
        pulsatingTextView.startAnimation(pulsateAnimation);

        // Validate email on focus change
        editTextEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                validateEmail(Objects.requireNonNull(editTextEmail.getText()).toString(), textViewError);
            }
        });

        // Handle forgot password click
        forgotPassword.setOnClickListener(v -> showToast("Forgot password field was clicked"));

    }

    // Validate email format
    private void validateEmail(String email, TextView errorTextView) {
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorTextView.setVisibility(View.GONE);
        } else {
            errorTextView.setVisibility(View.VISIBLE);
        }
    }

    private void setupRememberCheckBox() {
        CheckBox rememberMe = findViewById(R.id.log_in_remember_cb);
        // Handle remember me checkbox click
        rememberMe.setOnClickListener(v -> {
            if (rememberMe.isChecked()) {
                showToast("Remember me checkbox is checked");
            } else {
                showToast("Remember me checkbox is unchecked");
            }
        });
    }

    private void setupLogInButton() {
        Button confirmLogIn = findViewById(R.id.log_in_confirm_btn);
        confirmLogIn.setOnClickListener(v -> showToast("Log in button was clicked"));
    }

    private void setupGoogleButton() {
        MaterialButton logInGoogle = findViewById(R.id.log_in_google_btn);
        logInGoogle.setOnClickListener(v -> showToast("Log in with Google button was clicked"));
    }

    private void setupFacebookButton() {
        MaterialButton logInFacebook = findViewById(R.id.log_in_facebook_btn);
        logInFacebook.setOnClickListener(v -> showToast("Log in with Facebook button was clicked"));
    }

    private void setupSignUpField() {
        TextView signUp = findViewById(R.id.sign_up_btn);

        signUp.setOnClickListener(v -> {
            Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }


    // Show a toast message
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        // Start ChooseLogInSignUpActivity and clear the back stack
        Intent intent = new Intent(LogInActivity.this, ChooseLogInSignUpActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish(); // Finish the LogInActivity
    }
}