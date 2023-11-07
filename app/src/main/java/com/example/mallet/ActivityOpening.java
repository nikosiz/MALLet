package com.example.mallet;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.example.mallet.databinding.ActivityOpeningBinding;
import com.example.mallet.utils.AuthenticationUtils;
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

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Utils.terminateApp(thisActivity());
            }
        };

        // Register the callback with the onBackPressedDispatcher
        this.getOnBackPressedDispatcher().addCallback(this, callback);

        // Check if the user is logged in
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        isLogged = sharedPreferences.getBoolean("isLogged", false);

        if (isLogged) {
            LinearLayout openingLl = binding.openingLl;
            Utils.hideItems(openingLl);

            Intent intent = new Intent(this,ActivityMain.class);
            startActivity(intent);
        } else if (!isLogged) {
            // User is not logged in, proceed with setting up the opening activity
            LinearLayout openingLl = binding.openingLl;
            setupContents();
            Utils.showItems(openingLl);
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
        binding.openingNoLoginBtn.setOnClickListener(v -> {
            //todo do testow
            AuthenticationUtils.save(getApplicationContext(), "12345niewiemcotupisac@gmail.com", "SzefBoss123!@#");
            Utils.openActivity(this, ActivityMain.class);
        });
    }

    private Activity thisActivity() {
        Activity thisActivity = this;
        return thisActivity;
    }

}
