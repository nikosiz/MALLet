package com.example.mallet;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mallet.databinding.ActivityLoginBinding;
import com.example.mallet.databinding.DialogForgotPasswordBinding;
import com.example.mallet.utils.Utils;

import java.util.Objects;
import java.util.regex.Pattern;

public class ActivityLogin extends AppCompatActivity {

    // Declare EditText fields for email and password
    private EditText emailEt;
    private EditText passwordEt;
    // Declare TextViews for email and password error messages
    private TextView emailErrorTv;
    private TextView passwordErrorTv;
    // Binding for the activity's layout
    private ActivityLoginBinding binding;
    // Define password pattern using regex
    private final Pattern passwordPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%^*()<>?/|}{~:]).{8,}$");
    // Define error message for incorrect email
    private final String emailIncorrectMsg = "Email incorrect";
    // Define error message for incorrect password
    private final String passwordIncorrect = "The password must be at least 8 characters long and contain at least one digit, one small letter, one big letter and one special character.";
    // Create a handler for main thread operations

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize views for email and password fields
        emailEt = binding.loginEmailEt;
        emailErrorTv = binding.loginEmailErrorTv;
        passwordEt = binding.loginPasswordEt;
        passwordErrorTv = binding.loginPasswordErrorTv;

        // Call a method to set up UI elements and listeners
        setupContents();
    }

    private void setupContents() {
        // Call a method to set up animation for the logo
        setupAnimation();

        // Set up TextWatchers for email and password fields
        Utils.setupTextWatcher(emailEt, emailErrorTv, Patterns.EMAIL_ADDRESS, emailIncorrectMsg);
        Utils.setupTextWatcher(passwordEt, passwordErrorTv, passwordPattern, passwordIncorrect);

        // Set up click listeners for various buttons and text views
        binding.loginForgotPasswordTv.setOnClickListener(v -> forgotPasswordDialog());
        binding.loginBtn.setOnClickListener(v -> handleLogin());
        binding.loginGoogleMatbtn.setOnClickListener(v -> handleLogin());
        binding.loginFacebookMatbtn.setOnClickListener(v -> handleLogin());
        binding.loginSignupHereTv.setOnClickListener(v -> signupActivity());
    }

    private void setupAnimation() {
        // Get the logo TextView
        TextView logo = binding.loginLogoTv;
        // Set up animation for the logo
        Utils.setupAnimation(this, logo);
    }

    private void forgotPasswordDialog() {
        // Create a custom dialog for resetting password
        Dialog dialog = Utils.createDialog(this, R.layout.dialog_forgot_password);
        // Inflate the dialog's layout
        DialogForgotPasswordBinding dialogBinding = DialogForgotPasswordBinding.inflate(getLayoutInflater());
        // Set the dialog's content view
        assert dialog != null;
        dialog.setContentView(dialogBinding.getRoot());
        // Show the dialog
        dialog.show();

        // Get the email EditText from the dialog
        EditText dialogEmailEt = dialogBinding.forgotPasswordEmailEt;
        // Get the error TextView from the dialog
        TextView dialogErrorTv = dialogBinding.forgotPasswordErrorTv;

        // Set up a TextWatcher for the email field in the dialog
        Utils.setupTextWatcher(dialogEmailEt, dialogErrorTv, Patterns.EMAIL_ADDRESS, emailIncorrectMsg);

        // Get the cancel TextView from the dialog
        TextView cancel = dialogBinding.forgotPasswordCancelTv;
        // Get the confirm TextView from the dialog
        TextView confirm = dialogBinding.forgotPasswordConfirmTv;

        // Set up click listener to dismiss the dialog when canceled
        cancel.setOnClickListener(v -> dialog.dismiss());
        // Set up click listener for confirming the email for password reset
        confirm.setOnClickListener(v -> {
            String email = Objects.requireNonNull(dialogEmailEt.getText()).toString().trim();

            // Validate the email input in the dialog
            Utils.validateInput(dialogEmailEt, dialogErrorTv, Patterns.EMAIL_ADDRESS, emailIncorrectMsg);

            if (!Utils.isErrorVisible(dialogErrorTv)) {
                // TODO: Implement sending an email with a password-resetting link
                dialog.dismiss();
                Utils.showToast(this, "Email sent"); // Show a toast message
            }
        });
    }

    private void handleLogin() {
        // Get email and password inputs
        String email = emailEt.getText().toString();
        String password = passwordEt.getText().toString();

        // Validate email and password inputs
        Utils.validateInput(emailEt, emailErrorTv, Patterns.EMAIL_ADDRESS, emailIncorrectMsg);
        Utils.validateInput(passwordEt, passwordErrorTv, passwordPattern, passwordIncorrect);

        // Check if there are no visible errors
        if (!Utils.isErrorVisible(emailErrorTv) && !Utils.isErrorVisible(passwordErrorTv)) {
            // TODO: Handle login through AuthenticationManager
            Utils.openActivity(this, ActivityMain.class); // Open the main activity
        } else {
            System.out.println("Error is visible"); // Print a message if errors are visible
        }
    }

    private void signupActivity() {
        Utils.openActivity(this, ActivitySignup.class); // Open the signup activity
    }

    @Override
    public void onBackPressed() {
        // Create an intent to navigate to the opening activity
        Intent intent = new Intent(ActivityLogin.this, ActivityOpening.class);
        // Set intent flags
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        // Start the opening activity
        startActivity(intent);
        // Finish the current activity
        finish();
    }
}