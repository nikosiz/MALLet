package com.example.mallet;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mallet.databinding.ActivityOpeningBinding;
import com.example.mallet.utils.Utils;

public class ActivityOpening extends AppCompatActivity {
    private ActivityOpeningBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOpeningBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupAnimatedLogo();
        setupClickListeners();

        // TODO: Delete
        setupNoLoginBtn();
    }

    private void setupAnimatedLogo() {
        TextView logoTV = binding.openingLogoTv;
        Animation pulseAnimation = AnimationUtils.loadAnimation(this, R.anim.pulse_anim);
        logoTV.startAnimation(pulseAnimation);
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
