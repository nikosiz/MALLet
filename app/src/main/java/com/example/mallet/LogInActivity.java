package com.example.mallet;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class LogInActivity extends AppCompatActivity {

    private com.example.mallet.databinding.ActivityLogInBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = com.example.mallet.databinding.ActivityLogInBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setupClickListeners();

        // Initialize views
        TextView pulsatingTextView = binding.logInLogo;
        TextInputEditText editTextEmail = binding.logInEmailEt;
        TextView textViewError = binding.logInErrorTv;
        TextView forgotPassword = binding.logInForgotPasswordTv;

        // Apply pulsating animation to logo
        Animation pulsateAnimation = AnimationUtils.loadAnimation(this, R.anim.pulse_anim);
        pulsatingTextView.startAnimation(pulsateAnimation);

        // Validate email on focus change
        editTextEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                validateEmail(Objects.requireNonNull(editTextEmail.getText()).toString(), textViewError);
            }
        });

        // Handle forgot password click
        forgotPassword.setOnClickListener(v -> forgotPassword());
    }

    // Validate email format
    private void validateEmail(String email, TextView errorTextView) {
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorTextView.setVisibility(View.GONE);
        } else {
            errorTextView.setVisibility(View.VISIBLE);
        }
    }

    private void forgotPassword() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_forgot_password);

        LinearLayout createCollaboration = dialog.findViewById(R.id.add_new_create_collaboration);

        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        // Find and set up cancel and confirm buttons
        TextView cancelBtn = dialog.findViewById(R.id.forgot_password_cancel_btn);
        TextView confirmBtn = dialog.findViewById(R.id.forgot_password_confirm_btn);

        cancelBtn.setOnClickListener(v -> dialog.dismiss());

        confirmBtn.setOnClickListener(v -> {
            TextView emptyEmailError = dialog.findViewById(R.id.forgot_password_empty_error_tv);
            TextView doesNotExistError = dialog.findViewById(R.id.forgot_password_does_not_exist_error_tv);
            TextView validError = dialog.findViewById(R.id.forgot_password_provide_valid_error_tv);
            TextInputEditText emailEditText = dialog.findViewById(R.id.forgot_password_new_et);

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

                showToast("OK button was clicked. The email with reset link be sent but there is no backend yet.");
                dialog.dismiss();
            }
        });
    }

    private void setupClickListeners() {
        binding.logInConfirmBtn.setOnClickListener(v -> showToast("Log in button was clicked"));
        binding.logInGoogleBtn.setOnClickListener(v -> showToast("Log in with Google button was clicked"));
        binding.logInFacebookBtn.setOnClickListener(v -> showToast("Log in with Facebook button was clicked"));
        binding.signUpBtn.setOnClickListener(v -> signUpRedirect());
    }

    private void signUpRedirect() {
        Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    // Show a toast message
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        // Start ChooseLogInSignUpActivity and clear the back stack
        Intent intent = new Intent(LogInActivity.this, ChooseLogInSignUpActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish(); // Finish the LogInActivity
    }
}
