package com.example.mallet;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.example.mallet.databinding.ActivityOpeningBinding;
import com.example.mallet.utils.Utils;

public class ActivityOpening extends AppCompatActivity {
    private ActivityOpeningBinding binding;
    private SharedPreferences sharedPreferences;
    private boolean isLogged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOpeningBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Check if the user is logged in
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        isLogged = sharedPreferences.getBoolean("isLogged", false);

        if (isLogged) {
            // User is logged in, open ActivityMain
            Utils.openActivity(this, ActivityMain.class);
        } else {
            // User is not logged in, proceed with setting up the opening activity
            setupContents();
        }
    }

    private void setupContents() {
        setupAnimatedLogo();
        setupClickListeners();
        setupNoLoginBtn();
    }

    private void setupAnimatedLogo() {
        TextView logoTv = binding.openingLogoTv;
        Animation pulseAnimation = AnimationUtils.loadAnimation(this, R.anim.pulse_anim);
        logoTv.startAnimation(pulseAnimation);
    }

    private void setupClickListeners() {
        binding.openingLoginBtn.setOnClickListener(v -> Utils.openActivity(this, ActivityLogin.class));
        binding.openingSignupBtn.setOnClickListener(v -> Utils.openActivity(this, ActivitySignup.class));
    }

    // TODO: Delete
    private void setupNoLoginBtn() {
        binding.openingNoLoginBtn.setOnClickListener(v -> Utils.openActivity(this, ActivityMain.class));
    }

}
