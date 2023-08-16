package com.example.mallet;

import androidx.annotation.NonNull;
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

public class LogInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        TextInputEditText editTextEmail = findViewById(R.id.log_in_email_et);
        TextView textViewError = findViewById(R.id.log_in_error_tv);
        Button confirmLogIn = findViewById(R.id.log_in_confirm_btn);
        TextView forgotPassword = findViewById(R.id.log_in_forgot_password_tv);

        TextView pulsatingTextView;
        Animation pulsateAnimation;

        pulsatingTextView = findViewById(R.id.log_in_logo);
        pulsateAnimation = AnimationUtils.loadAnimation(this, R.anim.pulse_anim);
        pulsatingTextView.startAnimation(pulsateAnimation);

        editTextEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    validateEmail(editTextEmail.getText().toString(), textViewError);
                }
            }
        });

        confirmLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show a Toast message when the button is clicked
                Toast.makeText(LogInActivity.this, "Log in material button clicked", Toast.LENGTH_SHORT).show();
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show a Toast message when the button is clicked
                Toast.makeText(LogInActivity.this, "Forgot password field clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void validateEmail(@NonNull String email, TextView errorTextView) {
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorTextView.setVisibility(View.GONE);
        } else {
            errorTextView.setVisibility(View.VISIBLE);
        }
    }
}