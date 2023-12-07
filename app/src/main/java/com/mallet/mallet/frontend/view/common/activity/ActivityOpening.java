package com.mallet.mallet.frontend.view.common.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.example.mallet.R;
import com.example.mallet.databinding.ActivityOpeningBinding;
import com.mallet.mallet.frontend.utils.ViewUtils;

public class ActivityOpening extends AppCompatActivity {
    private ActivityOpeningBinding binding;
    private SharedPreferences sharedPreferences;
    private boolean isLogged;

    private LinearLayout openingLl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOpeningBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                ViewUtils.terminateApp(thisActivity());
            }
        };

        // Register the callback with the onBackPressedDispatcher
        this.getOnBackPressedDispatcher().addCallback(this, callback);

        // Check if the user is logged in
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        isLogged = sharedPreferences.getBoolean("isLogged", false);

        setupContents();

        if (isLogged) {
            //Utils.hideItems(openingLl);

            Intent intent = new Intent(this, ActivityMain.class);
            startActivity(intent);

            finish();
        }
    }

    private void setupContents() {
        openingLl = binding.openingLl;
        ViewUtils.showItems(openingLl);

        setupAnimatedLogo();
        setupNoLoginBtn();
        Button loginBtn = binding.openingLoginBtn;

        loginBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, ActivityLogin.class);

            startActivity(intent);

            finish();
        });

        Button signupBtn = binding.openingSignupBtn;
        signupBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, ActivitySignup.class);

            startActivity(intent);

            finish();
        });
    }

    private void setupAnimatedLogo() {
        TextView logoTv = binding.openingLogoTv;
        Animation pulseAnimation = AnimationUtils.loadAnimation(this, R.anim.pulse_anim);
        logoTv.startAnimation(pulseAnimation);
    }

    // TODO: Delete
    private void setupNoLoginBtn() {
        /*binding.openingNoLoginBtn.setOnClickListener(v -> {
            // todo do testow - usunac
            AuthenticationUtils.save(getApplicationContext(), "12345niewiemcotupisac@gmail.com", "SzefBoss123!@#");
            Utils.openActivity(this, ActivityMain.class);
        });*/
    }


    private Activity thisActivity() {
        Activity thisActivity = this;
        return thisActivity;
    }

}
