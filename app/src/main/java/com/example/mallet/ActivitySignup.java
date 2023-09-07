package com.example.mallet;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mallet.databinding.ActivitySignupBinding;
import com.example.mallet.databinding.DialogChooseUsernameBinding;
import com.example.mallet.utils.Utils;

import java.util.Objects;
import java.util.regex.Pattern;

public class ActivitySignup extends AppCompatActivity {

    // Binding for the activity layout
    ActivitySignupBinding binding;
    private final Handler handler = new Handler(Looper.getMainLooper());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupAnimation();
        setupClickListeners();
        setupViews();
    }

    // Set up animation for the pulsating logo
    private void setupAnimation() {
        TextView pulsatingLogoTV = binding.signupLogo;
        Animation pulseAnimation = AnimationUtils.loadAnimation(this, R.anim.pulse_anim);
        pulsatingLogoTV.startAnimation(pulseAnimation);
    }

    // Set up click listeners for buttons
    private void setupClickListeners() {
        binding.signupContinueTv.setOnClickListener(v -> chooseUsernameDialog());
        binding.signupGoogleMatbtn.setOnClickListener(v -> signupWithGoogle());
        binding.signupFacebookMatbtn.setOnClickListener(v -> signupWithFacebook());
        binding.signupLoginBtnTv.setOnClickListener(v -> Utils.openActivity(this, ActivityLogin.class));
    }

    private void signupWithGoogle() {
        // TODO
        Utils.showToast(this, "Login with Google");

    }

    private void signupWithFacebook() {
        // TODO
        Utils.showToast(this, "Login with Facebook");
    }

    // Validate signup data including email and password
    private boolean validateSignupData(String email, String password) {
        TextView emailEmptyTV = binding.signupEmailEmptyTv;
        TextView emailErrorTV = binding.signupEmailIncorrectTv;
        TextView passwordErrorTV = binding.signupPasswordIncorrectTv;
        TextView passwordEmptyTV = binding.signupPasswordEmptyTv;
        TextView passwordSpaceErrorTV = binding.signupPasswordSpaceErrorTv;

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

    private void setupViews() {
        EditText emailEditText = binding.signupEmailEt;
        TextView emailErrorTV = binding.signupEmailIncorrectTv;
        TextView emailEmptyTV = binding.signupEmailEmptyTv;

        EditText passwordEditText = binding.signupPasswordEt;
        TextView passwordErrorTV = binding.signupPasswordIncorrectTv;
        TextView passwordEmptyTV = binding.signupPasswordEmptyTv;
        TextView passwordSpaceErrorTV = binding.signupPasswordSpaceErrorTv;

        Pattern passwordPattern = Pattern.compile("^" +
                "(?=.*[0-9])" +         // at least 1 digit
                "(?=.*[a-z])" +         // at least 1 lowercase letter
                "(?=.*[A-Z])" +         // at least 1 uppercase letter
                "(?=.*[@#$%^&+=])" +    // at least 1 special character
                "(?=\\S+$)" +           // no white spaces
                ".{8,}" +               // at least 8 characters
                "$");

        // Initialize text change listeners for email and password fields
        initializeTextWatchers(emailEditText, emailErrorTV, emailEmptyTV, passwordEditText, passwordErrorTV, passwordEmptyTV, passwordSpaceErrorTV, passwordPattern);
    }

    // Initialize text change listeners for email and password fields
    private void initializeTextWatchers(
            EditText emailEditText,
            TextView emailErrorTV,
            TextView emailEmptyTV,
            EditText passwordEditText,
            TextView passwordErrorTV,
            TextView passwordEmptyTV,
            TextView passwordSpaceErrorTV,
            Pattern passwordPattern) {

        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // This method is not used here, but can be implemented if needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String email = s.toString().trim();
                Utils.hideItem(emailErrorTV);
                Utils.hideItem(emailEmptyTV);

                if (email.isEmpty()) {
                    Utils.showItem(emailEmptyTV);
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Utils.showItem(emailErrorTV);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // This method is not used here, but can be implemented if needed
            }
        });

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // This method is not used here, but can be implemented if needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = s.toString().trim();
                Utils.hideItem(passwordErrorTV);
                Utils.hideItem(passwordEmptyTV);
                Utils.hideItem(passwordSpaceErrorTV);

                if (password.isEmpty()) {
                    Utils.showItem(passwordEmptyTV);
                } else if (!passwordPattern.matcher(password).matches()) {
                    Utils.showItem(passwordErrorTV);
                } else if (password.contains(" ")) {
                    Utils.showItem(passwordSpaceErrorTV);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // This method is not used here, but can be implemented if needed
            }
        });
    }

    private void chooseUsernameDialog() {
        String email = Objects.requireNonNull(binding.signupEmailEt.getText()).toString();
        String password = Objects.requireNonNull(binding.signupPasswordEt.getText()).toString();

        boolean isValid = validateSignupData(email, password);

        if (isValid) {
            // Create a dialog for choosing a username
            Dialog dialog = Utils.createDialog(this, R.layout.dialog_choose_username);
            DialogChooseUsernameBinding dialogBinding = DialogChooseUsernameBinding.inflate(getLayoutInflater());
            assert dialog != null;
            dialog.setContentView(dialogBinding.getRoot());
            dialog.show();

            dialogBinding.chooseUsernameCreateAccBtn.setOnClickListener(v -> {
                // Handle the "Create Account" button click within the username dialog
                AuthenticationManager authManager = AuthenticationManager.getInstance();

                if (authManager.signUp(email, password)) {
                    // Registration successful, you can now proceed
                    dialog.dismiss(); // Dismiss the username dialog
                    handler.postDelayed(() -> Utils.openActivity(this, ActivityMain.class), 3000);
                    Utils.showToast(this, "Account created successfully");
                    // You can perform additional actions after successful registration here
                } else {
                    Utils.showToast(this, "Registration failed. Please try again.");
                }
            });

        } else {
            Utils.showToast(this, "Invalid email or password");
        }
    }


    @Override
    public void onBackPressed() {
        // Start ActivityOpening and clear the back stack
        Intent intent = new Intent(ActivitySignup.this, ActivityOpening.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish(); // Finish the SignupActivity
    }
}