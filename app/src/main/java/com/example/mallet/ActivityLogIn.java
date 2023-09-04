package com.example.mallet;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mallet.databinding.ActivityLogInBinding;
import com.example.mallet.databinding.DialogForgotPasswordBinding;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class ActivityLogIn extends AppCompatActivity {

    private ActivityLogInBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = com.example.mallet.databinding.ActivityLogInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupClickListeners();

        // Initialize views
        TextView pulsatingTV = binding.logInLogo;
        TextInputEditText editTextEmail = binding.logInEmailEt;
        TextView textViewError = binding.logInErrorTv;
        TextView forgotPassword = binding.logInForgotPasswordTv;

        // Apply pulsating animation to logo
        Animation pulseAnimation = AnimationUtils.loadAnimation(this, R.anim.pulse_anim);
        pulsatingTV.startAnimation(pulseAnimation);

        // Validate email on focus change
        editTextEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                validateEmail(Objects.requireNonNull(editTextEmail.getText()).toString(), textViewError);
            }
        });

        // Handle forgot password click
        forgotPassword.setOnClickListener(v -> forgotPasswordDialog());
    }

    // Validate email format
    private void validateEmail(String email, TextView errorTextView) {
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorTextView.setVisibility(View.GONE);
        } else {
            errorTextView.setVisibility(View.VISIBLE);
        }
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

                FrontendUtils.showToast(this, "OK button was clicked. The email with reset link be sent but there is no backend yet.");
                dialog.dismiss();
            }
        });
    }

    private void setupClickListeners() {
        binding.logInConfirmBtn.setOnClickListener(v -> FrontendUtils.showToast(this, "Log in button was clicked"));
        binding.logInGoogleBtn.setOnClickListener(v -> FrontendUtils.showToast(this, "Log in with Google button was clicked"));
        binding.logInFacebookBtn.setOnClickListener(v -> FrontendUtils.showToast(this, "Log in with Facebook button was clicked"));
        binding.signUpBtn.setOnClickListener(v -> signUpRedirect());
    }

    private void signUpRedirect() {
        Intent intent = new Intent(ActivityLogIn.this, ActivitySignUp.class);
        startActivity(intent);
    }

    // Show a toast message


    @Override
    public void onBackPressed() {
        // Start ChooseLogInSignUpActivity and clear the back stack
        Intent intent = new Intent(ActivityLogIn.this, ActivityOpening.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish(); // Finish the LogInActivity
    }
}
