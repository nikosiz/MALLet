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
import com.example.mallet.databinding.DialogConfirmEmailBinding;
import com.example.mallet.utils.Utils;

import java.util.regex.Pattern;

public class ActivitySignup extends AppCompatActivity {

    ActivitySignupBinding binding;
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupAnimation();
        setupClickListeners();
        setupTextWatchers();
    }

    private void setupAnimation() {
        TextView pulsatingLogoTV = binding.signupLogo;
        Animation pulseAnimation = AnimationUtils.loadAnimation(this, R.anim.pulse_anim);
        pulsatingLogoTV.startAnimation(pulseAnimation);
    }

    private void setupClickListeners() {
        binding.signupContinueTv.setOnClickListener(v -> chooseUsernameDialog());
        binding.signupGoogleMatbtn.setOnClickListener(v -> signupWithGoogle());
        binding.signupFacebookMatbtn.setOnClickListener(v -> signupWithFacebook());
        binding.signupLoginBtnTv.setOnClickListener(v -> Utils.openActivity(this, ActivityLogin.class));
    }

    private void setupTextWatchers() {
        EditText emailEditText = binding.signupEmailEt;
        EditText passwordEditText = binding.signupPasswordEt;

        initializeTextWatcher(emailEditText, binding.signupEmailErrorTv);
        initializeTextWatcher(passwordEditText, binding.signupPasswordErrorTv);
    }

    private void initializeTextWatcher(EditText editText, TextView... errorViews) {
        Pattern passwordPattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                for (TextView errorView : errorViews) {
                    Utils.hideItem(errorView);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().contains(" ")) {
                    Utils.showItem(binding.signupPasswordErrorTv);
                }
            }
        });
    }

    private void chooseUsernameDialog() {
        String email = binding.signupEmailEt.getText().toString();
        String password = binding.signupPasswordEt.getText().toString();

        boolean isValid = validateSignupData(email, password);

        if (isValid) {
            Dialog dialog = Utils.createDialog(this, R.layout.dialog_choose_username);
            DialogChooseUsernameBinding dialogBinding = DialogChooseUsernameBinding.inflate(getLayoutInflater());

            EditText usernameEt = dialogBinding.chooseUsernameNameEt;

            dialog.setContentView(dialogBinding.getRoot());
            dialog.show();

            dialogBinding.chooseUsernameCreateAccBtn.setOnClickListener(v -> {
                dialog.dismiss();
                handleCreateAccount(usernameEt, email, password);
            });
        } else {
            Utils.showToast(this, "Invalid email or password");
        }
    }

    private boolean validateSignupData(String email, String password) {
        TextView emailErrorTv = binding.signupEmailErrorTv;
        TextView passwordErrorTv = binding.signupPasswordErrorTv;

        Pattern passwordPattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");

        boolean isValid = true;

        Utils.hideItem(emailErrorTv);
        Utils.hideItem(passwordErrorTv);

        if (email.isEmpty()) {
            Utils.showItem(emailErrorTv);
            emailErrorTv.setText("Email is required");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Utils.showItem(emailErrorTv);
            emailErrorTv.setText("Invalid email format");
            isValid = false;
        }

        if (password.isEmpty()) {
            Utils.showItem(passwordErrorTv);
            passwordErrorTv.setText("Password is required");
            isValid = false;
        } else if (!passwordPattern.matcher(password).matches()) {
            Utils.showItem(passwordErrorTv);
            passwordErrorTv.setText("Invalid password format");
            isValid = false;
        }

        return isValid;
    }

    private void handleCreateAccount(EditText usernameEt, String email, String password) {
        String username = usernameEt.getText().toString();
        AuthenticationManager authManager = AuthenticationManager.getInstance();

        if (authManager.signUp(email, password)) {
            confirmAccountDialog();
            System.out.println(email + "\n" + AuthenticationManager.md5(password) + "\n" + username);
            clearFields();
        } else {
            Utils.showToast(this, "Registration failed. Please try again.");
        }
    }

    private void clearFields() {
        Utils.clearField(binding.signupEmailEt);
        Utils.clearField(binding.signupPasswordEt);
    }

    private void confirmAccountDialog() {
        Dialog dialog = Utils.createDialog(this, R.layout.dialog_confirm_email);
        DialogConfirmEmailBinding dialogBinding = DialogConfirmEmailBinding.inflate(getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());
        dialog.show();

        dialogBinding.confirmEmailOpenTv.setOnClickListener(v -> Utils.openEmailClient(this));
        dialogBinding.confirmEmailLaterTv.setOnClickListener(v -> {
            dialog.dismiss();
            Utils.openActivity(this, ActivityOpening.class);
        });
        dialog.setOnDismissListener(v -> {
            // Trigger the "Maybe Later" action
            Utils.openActivity(this, ActivityOpening.class);
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ActivitySignup.this, ActivityOpening.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    // TODO - optional stuff
    private void signupWithGoogle() {
        // TODO
        Utils.showToast(this, "Login with Google");
    }

    private void signupWithFacebook() {
        // TODO
        Utils.showToast(this, "Login with Facebook");
    }
}
