package com.example.mallet;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mallet.databinding.ActivityLogInBinding;
import com.example.mallet.databinding.DialogForgotPasswordBinding;
import com.example.mallet.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;
import java.util.regex.Pattern;

public class ActivityLogIn extends AppCompatActivity {

    private ActivityLogInBinding binding;
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = com.example.mallet.databinding.ActivityLogInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupAnimation();
        setupListeners();
        initializeViews();
    }

    // Initialize views and attach text change listeners
    private void initializeViews() {
        EditText emailEditText = binding.logInEmailEt;
        EditText passwordEditText = binding.logInPasswordEt;
        TextView emailErrorTV = binding.logInEmailErrorTv;
        TextView emailEmptyTV = binding.logInEmailEmptyEmailTv;
        TextView passwordErrorTV = binding.logInPasswordErrorTv;
        TextView passwordEmptyTV = binding.logInEmailEmptyPasswordTv;
        TextView passwordSpaceErrorTV = binding.logInPasswordSpaceErrorTv;

        Pattern passwordPattern = Pattern.compile("^" +
                "(?=.*[0-9])" +         // at least 1 digit
                "(?=.*[a-z])" +         // at least 1 lowercase letter
                "(?=.*[A-Z])" +         // at least 1 uppercase letter
                "(?=.*[@#$%^&+=])" +    // at least 1 special character
                "(?=\\S+$)" +           // no white spaces
                ".{8,}" +               // at least 8 characters
                "$");

        // Initialize text change listeners for email and password fields
        initializeTextWatchers(emailEditText, passwordEditText, emailErrorTV, emailEmptyTV, passwordErrorTV, passwordEmptyTV, passwordSpaceErrorTV, passwordPattern);
    }

    // Initialize text change listeners for email and password fields
    private void initializeTextWatchers(
            EditText emailEditText,
            EditText passwordEditText,
            TextView emailErrorTV,
            TextView emailEmptyTV,
            TextView passwordErrorTV,
            TextView passwordEmptyTV,
            TextView passwordSpaceErrorTV,
            Pattern passwordPattern) {

        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // This method is not used here, but you can implement it if needed.
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
                // This method is not used here, but you can implement it if needed.
            }
        });

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // This method is not used here, but you can implement it if needed.
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
                // This method is not used here, but you can implement it if needed.
            }
        });
    }

    // Handle the "Continue" button click and show the username dialog if data is valid
    private void handleLogin() {
        String email = Objects.requireNonNull(binding.logInEmailEt.getText()).toString();
        String password = Objects.requireNonNull(binding.logInPasswordEt.getText()).toString();

        boolean isValid = validateLoginData(email, password);

        if (!isValid) {
            //TODO delete
            Utils.showToast(this, "Invalid email or password");
        } else {

            //TODO delete and validate login
            Utils.showToast(this, "Email and password correct. Logging in...");
            handler.postDelayed(() -> Utils.openActivity(this, ActivityMain.class), 3000);

        }
    }

    // Set up animation for the pulsating logo
    private void setupAnimation() {
        TextView pulsatingLogoTV = binding.logInLogo;
        Animation pulseAnimation = AnimationUtils.loadAnimation(this, R.anim.pulse_anim);
        pulsatingLogoTV.startAnimation(pulseAnimation);
    }

    // Validate signup data including email and password
    private boolean validateLoginData(String email, String password) {
        TextView emailErrorTV = binding.logInEmailErrorTv;
        TextView emailEmptyTV = binding.logInEmailEmptyEmailTv;
        TextView passwordErrorTV = binding.logInPasswordErrorTv;
        TextView passwordEmptyTV = binding.logInEmailEmptyPasswordTv;
        TextView passwordSpaceErrorTV = binding.logInPasswordSpaceErrorTv;

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

        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        // Find and set up cancel and confirm buttons
        TextView cancelBtn = binding.forgotPasswordCancelBtn;
        TextView confirmBtn = binding.forgotPasswordConfirmBtn;

        cancelBtn.setOnClickListener(v -> dialog.dismiss());

        confirmBtn.setOnClickListener(v -> {
            TextView emptyEmailError = binding.forgotPasswordEmptyErrorTv;
            TextView doesNotExistError = binding.forgotPasswordDoesNotExistErrorTv;
            TextView validError = binding.forgotPasswordProvideValidErrorTv;
            TextInputEditText emailEditText = binding.forgotPasswordNewEt;

            String email = Objects.requireNonNull(emailEditText.getText()).toString();

            if (TextUtils.isEmpty(email)) {
                emptyEmailError.setVisibility(View.VISIBLE);
                validError.setVisibility(View.GONE);
                doesNotExistError.setVisibility(View.GONE);
            } else if (!TextUtils.isEmpty(email) && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emptyEmailError.setVisibility(View.GONE);
                validError.setVisibility(View.VISIBLE);
                doesNotExistError.setVisibility(View.GONE);
            } else {
                // TODO: Add functionality checking if email exists
                /* if(!email.exists) {
                    validEmailError.setVisibility(View.GONE);
                    emptyFieldError.setVisibility(View.GONE);
                    doesNotExistError.setVisibility(View.VISIBLE);
                } else {
                    validEmailError.setVisibility(View.GONE);
                    emptyFieldError.setVisibility(View.GONE);
                    doesNotExistError.setVisibility(View.GONE);
                    dismiss();
                    sendEmailWithResetLink();
                }*/
                // Hide errors, close the dialog, and show toast

                Utils.showToast(this, "OK button was clicked. The email with reset link be sent but there is no backend yet.");
                dialog.dismiss();
            }
        });
    }

    private void setupListeners() {
        binding.logInConfirmBtn.setOnClickListener(v -> handleLogin());
        binding.logInGoogleBtn.setOnClickListener(v -> Utils.showToast(this, "Log in with Google button was clicked"));
        binding.logInFacebookBtn.setOnClickListener(v -> Utils.showToast(this, "Log in with Facebook button was clicked"));
        binding.logInForgotPasswordTv.setOnClickListener(v -> forgotPasswordDialog());
        binding.signUpBtn.setOnClickListener(v -> signUpRedirect());
    }

    private void signUpRedirect() {
        Intent intent = new Intent(ActivityLogIn.this, ActivitySignUp.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        // Start ChooseLogInSignUpActivity and clear the back stack
        Intent intent = new Intent(ActivityLogIn.this, ActivityOpening.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish(); // Finish the LogInActivity
    }
}
