package com.example.mallet;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mallet.databinding.ActivitySignupBinding;
import com.example.mallet.databinding.DialogChooseUsernameBinding;
import com.example.mallet.databinding.DialogConfirmAccountBinding;
import com.example.mallet.utils.AuthenticationManager;
import com.example.mallet.utils.Utils;

import java.util.Objects;
import java.util.regex.Pattern;

public class ActivitySignup extends AppCompatActivity {
    private ActivitySignupBinding binding;
    private EditText emailEt, passwordEt;
    private String email, password, username;
    private TextView emailErrTv, passwordErrTv;
    private String emailIncorrect, passwordIncorrect, usernameIncorrect;
    private final Pattern passwordPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%^*()<>?/|}{~:]).{8,}$");
    private final Pattern usernamePattern = Pattern.compile("^[a-zA-Z0-9_]+$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        emailEt = binding.signupEmailEt;
        emailErrTv = binding.signupEmailErrorTv;
        emailIncorrect = getString(R.string.email_incorrect);

        passwordEt = binding.signupPasswordEt;
        passwordErrTv = binding.signupPasswordErrorTv;
        passwordIncorrect = getString(R.string.password_incorrect);

        setupContents();
    }

    private void setupContents() {
        setupAnimation();

        Utils.setupTextWatcher(emailEt, emailErrTv, Patterns.EMAIL_ADDRESS, emailIncorrect);
        Utils.setupTextWatcher(passwordEt, passwordErrTv, passwordPattern, passwordIncorrect);

        binding.signupContinueTv.setOnClickListener(v -> validateSignupData());
        binding.signupGoogleMatbtn.setOnClickListener(v -> validateSignupData());
        binding.signupFacebookMatbtn.setOnClickListener(v -> validateSignupData());
        binding.signupLoginHereTv.setOnClickListener(v -> loginActivity());
    }

    private void setupAnimation() {
        TextView logo = binding.signupLogoTv;
        Utils.setupAnimation(this, logo);
    }

    private void chooseUsernameDialog() {
        Dialog dialog = createDialog(R.layout.dialog_forgot_password);
        DialogChooseUsernameBinding dialogBinding = DialogChooseUsernameBinding.inflate(getLayoutInflater());
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setContentView(dialogBinding.getRoot());
        dialog.show();

        EditText dialogUsernameEt = dialogBinding.chooseUsernameEt;
        TextView dialogErrTv = dialogBinding.chooseUsernameErrorTv;
        usernameIncorrect = getString(R.string.username_incorrect);

        Utils.setupTextWatcher(dialogUsernameEt, dialogErrTv, usernamePattern, usernameIncorrect);

        TextView cancel = dialogBinding.chooseUsernameCancelTv;
        TextView confirm = dialogBinding.chooseUsernameCreateAccTv;

        cancel.setOnClickListener(v -> dialog.dismiss());
        confirm.setOnClickListener(v -> {
            username = Objects.requireNonNull(dialogUsernameEt.getText()).toString().trim();

            // TODO: Handle signup through AuthenticationManager
            Utils.showToast(this, email + "\n" + AuthenticationManager.md5(password) + "\n" + username);
            System.out.println(email + "\n" + password + "\n" + username);

            Utils.validateInput(dialogUsernameEt, dialogErrTv, usernamePattern, usernameIncorrect);

            if (!Utils.isErrVisible(dialogErrTv)) {
                // TODO: Implement sending an email with a password-resetting link
                dialog.dismiss();
                confirmAccountDialog();
            }
        });
    }

    private void validateSignupData() {
        String enteredEmail = emailEt.getText().toString().trim();
        String enteredPassword = passwordEt.getText().toString();

        Utils.validateInput(emailEt, emailErrTv, Patterns.EMAIL_ADDRESS, emailIncorrect);
        Utils.validateInput(passwordEt, passwordErrTv, passwordPattern, passwordIncorrect);

        if (!Utils.isErrVisible(emailErrTv) && !Utils.isErrVisible(passwordErrTv)) {
            email = enteredEmail;
            password = enteredPassword;

            chooseUsernameDialog();
        } else {
            System.out.println("Error is visible");
        }
    }

    private void confirmAccountDialog() {
        Dialog dialog = createDialog(R.layout.dialog_confirm_account);
        DialogConfirmAccountBinding dialogBinding = DialogConfirmAccountBinding.inflate(getLayoutInflater());
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setContentView(dialogBinding.getRoot());
        dialog.show();

        TextView cancel = dialogBinding.confirmAccountCancelTv;
        TextView confirm = dialogBinding.confirmAccountOpenTv;

        cancel.setOnClickListener(v -> {
            Utils.resetEditText(emailEt, emailErrTv); // Reset email EditText and error TextView
            Utils.resetEditText(passwordEt, passwordErrTv); // Reset password EditText and error TextView
            dialog.dismiss(); // Dismiss the dialog
            System.out.println("Email and password ETs reset"); // Print a message
            Utils.hideItems(emailErrTv, passwordErrTv);
        });

        confirm.setOnClickListener(v -> {
            dialog.dismiss();
            Utils.openEmailClient(this);
        });
    }

    private void loginActivity() {
        Utils.openActivity(this, ActivityLogin.class); // Open the login activity
    }

    @Override
    public void onBackPressed() {
        // Create an intent to navigate to the opening activity
        Intent intent = new Intent(ActivitySignup.this, ActivityOpening.class);
        // Set intent flags
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        // Start the opening activity
        startActivity(intent);
        // Finish the current activity
        finish();
        System.out.println(getClass().getSimpleName() + " was closed");
    }

    private Dialog createDialog(int layoutResId) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layoutResId);

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        return dialog;
    }
}