package com.example.mallet;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mallet.databinding.ActivitySignupBinding;
import com.example.mallet.databinding.DialogChooseUsernameBinding;
import com.example.mallet.databinding.DialogConfirmAccountBinding;
import com.example.mallet.utils.Utils;

import java.util.Objects;
import java.util.regex.Pattern;

public class ActivitySignup extends AppCompatActivity {
    // Initialize email, password, and username variables
    private String email, password, username = "";
    // Declare EditText fields
    private EditText emailEt;
    private EditText passwordEt;
    // Declare TextViews for error messages
    private TextView emailErrorTv;
    private TextView passwordErrorTv;
    // Binding for the activity's layout
    private ActivitySignupBinding binding;
    // Define password pattern using regex
    private final Pattern passwordPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%^*()<>?/|}{~:]).{8,}$");
    // Define username pattern using regex
    private final Pattern usernamePattern = Pattern.compile("^[a-zA-Z0-9_]{1,20}$");
    // Define error message for incorrect email
    private final String emailIncorrectMsg = "Email incorrect";
    // Define error message for incorrect password
    private final String passwordIncorrect = "The password must be at least 8 characters long and contain at least one digit, one small letter, one big letter and one special character";
    // Define error message for incorrect username
    private final String usernameIncorrect = "The username can only consist of letters, numbers, and underscores";
    // Create a handler for main thread operations

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize email EditText
        emailEt = binding.signupEmailEt;
        // Initialize email error TextView
        emailErrorTv = binding.signupEmailErrorTv;

        // Initialize password EditText
        passwordEt = binding.signupPasswordEt;
        // Initialize password error TextView
        passwordErrorTv = binding.signupPasswordErrorTv;

        // Call a method to set up UI elements and listeners
        setupContents();
    }

    private void setupContents() {
        // Call a method to set up animation for the logo
        setupAnimation();

        // Set up TextWatcher for email field
        Utils.setupTextWatcher(emailEt, emailErrorTv, Patterns.EMAIL_ADDRESS, emailIncorrectMsg);
        // Set up TextWatcher for password field
        Utils.setupTextWatcher(passwordEt, passwordErrorTv, passwordPattern, passwordIncorrect);

        // Set up click listener for continue button
        binding.signupContinueTv.setOnClickListener(v -> handleSignup());
        // Set up click listener for Google button
        binding.signupGoogleMatbtn.setOnClickListener(v -> handleSignup());
        // Set up click listener for Facebook button
        binding.signupFacebookMatbtn.setOnClickListener(v -> handleSignup());
        // Set up click listener for "Login here" text
        binding.signupLoginHereTv.setOnClickListener(v -> loginActivity());
    }

    private void setupAnimation() {
        // Get the logo TextView
        TextView logo = binding.signupLogoTv;
        // Set up animation for the logo
        Utils.setupAnimation(this, logo);
    }

    private void chooseUsernameDialog() {
        // Create a custom dialog
        Dialog dialog = Utils.createDialog(this, R.layout.dialog_forgot_password);
        // Inflate the dialog's layout
        DialogChooseUsernameBinding dialogBinding = DialogChooseUsernameBinding.inflate(getLayoutInflater());
        // Set the dialog's content view
        assert dialog != null;
        dialog.setContentView(dialogBinding.getRoot());
        // Show the dialog
        dialog.show();

        // Get the username EditText from the dialog
        EditText dialogUsernameEt = dialogBinding.chooseUsernameEt;
        // Get the error TextView from the dialog
        TextView dialogErrorTv = dialogBinding.chooseUsernameErrorTv;

        // Set up TextWatcher for the username field in the dialog
        Utils.setupTextWatcher(dialogUsernameEt, dialogErrorTv, usernamePattern, usernameIncorrect);

        // Get the cancel TextView from the dialog
        TextView cancel = dialogBinding.chooseUsernameCancelTv;
        // Get the confirm TextView from the dialog
        TextView confirm = dialogBinding.chooseUsernameCreateAccTv;

        cancel.setOnClickListener(v -> dialog.dismiss()); // Set up click listener to dismiss the dialog when canceled
        confirm.setOnClickListener(v -> {
            username = Objects.requireNonNull(dialogUsernameEt.getText()).toString().trim(); // Assign the entered username to the class variable

            // TODO: Here email, username, and password hash need to
            //  be passed to AuthenticationManager
            System.out.println(email + "\n" + password + "\n" + username); // Print email, password, and username

            // Validate the username input
            Utils.validateInput(dialogUsernameEt, dialogErrorTv, usernamePattern, usernameIncorrect);

            if (!Utils.isErrorVisible(dialogErrorTv)) {
                // TODO: Implement sending an email with a password-resetting link
                dialog.dismiss(); // Dismiss the dialog
                confirmAccountDialog(); // Call a method to show the confirmation dialog
                //Utils.openActivity(this, ActivityMain.class);
            }
        });
    }

    private void handleSignup() {
        String enteredEmail = emailEt.getText().toString().trim(); // Get entered email
        String enteredPassword = passwordEt.getText().toString(); // Get entered password

        // Validate email input
        Utils.validateInput(emailEt, emailErrorTv, Patterns.EMAIL_ADDRESS, emailIncorrectMsg);
        // Validate password input
        Utils.validateInput(passwordEt, passwordErrorTv, passwordPattern, passwordIncorrect);

        if (!Utils.isErrorVisible(emailErrorTv) && !Utils.isErrorVisible(passwordErrorTv)) {
            email = enteredEmail; // Assign entered email to the class variable
            password = enteredPassword; // Assign entered password to the class variable

            chooseUsernameDialog(); // Call a method to show the username dialog
        } else {
            System.out.println("Error is visible"); // Print a message if errors are visible
        }
    }

    private void confirmAccountDialog() {
        // Create a custom dialog for confirmation
        Dialog dialog = Utils.createDialog(this, R.layout.dialog_confirm_account);
        // Inflate the dialog's layout
        DialogConfirmAccountBinding dialogBinding = DialogConfirmAccountBinding.inflate(getLayoutInflater());
        // Set the dialog's content view
        assert dialog != null;
        dialog.setContentView(dialogBinding.getRoot());
        // Show the dialog
        dialog.show();

        // Get the cancel TextView from the dialog
        TextView cancel = dialogBinding.confirmAccountCancelTv;
        // Get the confirm TextView from the dialog
        TextView confirm = dialogBinding.confirmAccountOpenTv;

        cancel.setOnClickListener(v -> {
            Utils.resetEditText(emailEt, emailErrorTv); // Reset email EditText and error TextView
            Utils.resetEditText(passwordEt, passwordErrorTv); // Reset password EditText and error TextView
            dialog.dismiss(); // Dismiss the dialog
            System.out.println("Email and password ETs reset"); // Print a message
            Utils.openActivity(this, ActivitySignup.class); // Open the signup activity
        });

        confirm.setOnClickListener(v -> {
            dialog.dismiss(); // Dismiss the dialog
            Utils.openEmailClient(this); // Open the email client
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
    }
}
