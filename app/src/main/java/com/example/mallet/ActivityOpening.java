package com.example.mallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mallet.databinding.ActivityOpeningBinding;

public class ActivityOpening extends AppCompatActivity {
    private ActivityOpeningBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOpeningBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupAnimatedLogo();
        setupLoginButton();
        setupSignUpButton();
        setupNoLogInButton();
    }

    private void setupAnimatedLogo() {
        TextView logoTV = binding.chooseLogoTv;
        Animation pulseAnimation = AnimationUtils.loadAnimation(this, R.anim.pulse_anim);
        logoTV.startAnimation(pulseAnimation);
    }

    private void setupLoginButton() {
        Button logInButton = binding.chooseLogInBtn;
        logInButton.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityOpening.this, ActivityLogIn.class);
            startActivity(intent);
        });
    }

    private void setupSignUpButton() {
        Button signUpButton = binding.chooseSignUpBtn;
        signUpButton.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityOpening.this, ActivitySignUp.class);
            startActivity(intent);
        });
    }

    // TODO: Delete when log in and sign up are handled
    private void setupNoLogInButton() {
        Button noLogInButton = binding.chooseNoLogInBtn;
        noLogInButton.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityOpening.this, ActivityMain.class);
            startActivity(intent);
        });
    }


}
