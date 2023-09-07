package com.example.mallet;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mallet.databinding.ActivityLoginBinding;
import com.example.mallet.databinding.DialogForgotPasswordBinding;
import com.example.mallet.utils.Utils;

import java.util.Objects;
import java.util.regex.Pattern;

public class ActivityLogin extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private final Handler handler = new Handler(Looper.getMainLooper());

    private final Pattern passwordPattern = Pattern.compile("^" +
            "(?=.*[0-9])" +         // at least 1 digit
            "(?=.*[a-z])" +         // at least 1 lowercase letter
            "(?=.*[A-Z])" +         // at least 1 uppercase letter
            "(?=.*[@#$%^&+=])" +    // at least 1 special character
            "(?=\\S+$)" +           // no white spaces
            ".{8,}" +               // at least 8 characters
            "$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupAnimation();
        initializeViews();
    }

    private void setupAnimation() {
        TextView pulsatingLogoTV = binding.loginLogo;
        Animation pulseAnimation = AnimationUtils.loadAnimation(this, R.anim.pulse_anim);
        pulsatingLogoTV.startAnimation(pulseAnimation);
    }

    private void initializeViews() {
        initializeTextWatchers();
        setupListeners();
    }

    private void initializeTextWatchers() {
        EditText emailEditText = binding.loginEmailEt;
        EditText passwordEditText = binding.loginPasswordEt;
        TextView emailErrorTV = binding.loginEmailErrorTv;
        TextView emailEmptyTV = binding.loginEmailEmptyEmailTv;
        TextView passwordErrorTV = binding.loginPasswordErrorTv;
        TextView passwordEmptyTV = binding.loginEmailEmptyPasswordTv;
        TextView passwordSpaceErrorTV = binding.loginPasswordSpaceErrorTv;

        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not used here
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkEmailPattern(s.toString().trim(), emailErrorTV, emailEmptyTV);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not used here
            }
        });

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not used here
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkPasswordPattern(s.toString().trim(), passwordErrorTV, passwordEmptyTV, passwordSpaceErrorTV);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not used here
            }
        });
    }

    private boolean checkEmailPattern(String email, TextView emailErrorTV, TextView emailEmptyTV) {
        Utils.hideItem(emailErrorTV);
        Utils.hideItem(emailEmptyTV);

        if (email.isEmpty()) {
            Utils.showItem(emailEmptyTV);
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Utils.showItem(emailErrorTV);
            return false;
        }
        return true;
    }

    private boolean checkPasswordPattern(String password, TextView passwordErrorTV, TextView passwordEmptyTV, TextView passwordSpaceErrorTV) {
        Utils.hideItem(passwordErrorTV);
        Utils.hideItem(passwordEmptyTV);
        Utils.hideItem(passwordSpaceErrorTV);

        if (password.isEmpty()) {
            Utils.showItem(passwordEmptyTV);
            return false;
        } else if (!passwordPattern.matcher(password).matches()) {
            Utils.showItem(passwordErrorTV);
            return false;
        } else if (password.contains(" ")) {
            Utils.showItem(passwordSpaceErrorTV);
            return false;
        }
        return true;
    }

    // Inside handleLogin() method
    private void handleLogin() {
        String email = Objects.requireNonNull(binding.loginEmailEt.getText()).toString();
        String password = Objects.requireNonNull(binding.loginPasswordEt.getText()).toString();

        boolean isValid = validateLoginData(email, password);

        if (isValid) {
            AuthenticationManager authManager = AuthenticationManager.getInstance();
            if (authManager.login(email, password)) {
                Utils.showToast(this, "Login successful");
                handler.postDelayed(() -> Utils.openActivity(this, ActivityMain.class), 3000);
            } else {
                Utils.showToast(this, "Invalid email or password");
            }
        } else {
            Utils.showToast(this, "Invalid email or password");
        }
    }


    private boolean authenticateUser(String email, String password) {
        // TODO: implement authentication logic
        //  Can be done using Firebase Authentication, an API call to backend, etc.
        //  For actual purposes, I assume email and password are valid
        Utils.showToast(this,"Login \"successful\" for now.");
        return true;
    }

    private boolean validateLoginData(String email, String password) {
        TextView emailErrorTV = binding.loginEmailErrorTv;
        TextView emailEmptyTV = binding.loginEmailEmptyEmailTv;
        TextView passwordErrorTV = binding.loginPasswordErrorTv;
        TextView passwordEmptyTV = binding.loginEmailEmptyPasswordTv;
        TextView passwordSpaceErrorTV = binding.loginPasswordSpaceErrorTv;

        Pattern passwordPattern = Pattern.compile("^" +
                "(?=.*[0-9])" +         // at least 1 digit
                "(?=.*[a-z])" +         // at least 1 lowercase letter
                "(?=.*[A-Z])" +         // at least 1 uppercase letter
                "(?=.*[@#$%^&+=])" +    // at least 1 special character
                "(?=\\S+$)" +           // no white spaces
                ".{8,}" +               // at least 8 characters
                "$");

        // Reset error messages initially
        Utils.hideItem(emailErrorTV);
        Utils.hideItem(emailEmptyTV);
        Utils.hideItem(passwordErrorTV);
        Utils.hideItem(passwordEmptyTV);
        Utils.hideItem(passwordSpaceErrorTV);

        boolean isValid = true;

        if (email.isEmpty()) {
            Utils.showItem(emailEmptyTV);
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Utils.showItem(emailErrorTV);
            isValid = false;
        }

        if (password.isEmpty()) {
            Utils.showItem(passwordEmptyTV);
            isValid = false;
        } else if (!passwordPattern.matcher(password).matches()) {
            Utils.showItem(passwordErrorTV);
            isValid = false;
        } else if (password.contains(" ")) {
            Utils.showItem(passwordSpaceErrorTV);
            isValid = false;
        }

        return isValid;
    }

    private void forgotPasswordDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogForgotPasswordBinding binding = DialogForgotPasswordBinding.inflate(LayoutInflater.from(this));
        dialog.setContentView(binding.getRoot());

        // TODO: Set up the "Forgot Password" dialog UI and logic
        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    // Set up click listeners for buttons and views
    private void setupListeners() {
        binding.loginConfirmBtn.setOnClickListener(v -> handleLogin());
        binding.loginGoogleBtn.setOnClickListener(v -> Utils.showToast(this, "Log in with Google button was clicked"));
        binding.loginFacebookBtn.setOnClickListener(v -> Utils.showToast(this, "Log in with Facebook button was clicked"));
        binding.loginForgotPasswordTv.setOnClickListener(v -> forgotPasswordDialog());
        binding.signupBtn.setOnClickListener(v -> signupRedirect());
    }

    // Redirect to the signup activity
    private void signupRedirect() {
        Intent intent = new Intent(ActivityLogin.this, ActivitySignup.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        // Start ChooseLogInSignupActivity and clear the back stack
        Intent intent = new Intent(ActivityLogin.this, ActivityOpening.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish(); // Finish the LogInActivity
    }
}
