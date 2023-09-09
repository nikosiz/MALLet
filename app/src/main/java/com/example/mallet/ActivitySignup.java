package com.example.mallet;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mallet.databinding.ActivitySignupBinding;
import com.example.mallet.databinding.DialogChooseUsernameBinding;
import com.example.mallet.databinding.DialogConfirmEmailBinding;
import com.example.mallet.utils.Utils;

import java.util.regex.Pattern;

public class ActivitySignup extends AppCompatActivity {

    private EditText emailEt, passwordEt;
    private TextView emailErrorTv, passwordErrorTv;
    private ActivitySignupBinding binding;
    private Pattern passwordPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%^*()<>?/|}{~:]).{8,}$");
    private String emailIncorrectMsg = "Email incorrect", passwordIncorrect = "The password must be at least 8 characters long and contain at least one digit, one small letter, one big letter and one special character.";
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize views
        emailEt = binding.signupEmailEt;
        emailErrorTv = binding.signupEmailErrorTv;
        passwordEt = binding.signupPasswordEt;
        passwordErrorTv = binding.signupPasswordErrorTv;

        // Setup UI elements and listeners
        setupContents();
    }

    private void setupContents() {
        setupAnimation();

        Utils.setupTextWatcher(emailEt, emailErrorTv, Patterns.EMAIL_ADDRESS, emailIncorrectMsg);
        Utils.setupTextWatcher(passwordEt, passwordErrorTv, passwordPattern, passwordIncorrect);

        binding.signupContinueTv.setOnClickListener(v -> {
            if (!emailEt.getText().toString().isEmpty() && !passwordEt.getText().toString().isEmpty()) {
                chooseUsernameDialog();
            } else {
                System.out.println("Error is visible");
            }
        });
        binding.signupGoogleMatbtn.setOnClickListener(v -> handleSignup());
        binding.signupFacebookMatbtn.setOnClickListener(v -> handleSignup());
        binding.signupLoginHereTv.setOnClickListener(v -> loginActivity());
    }

    private void setupAnimation() {
        TextView logo = binding.signupLogoTv;
        Utils.setupAnimation(this, logo);
    }

    private void chooseUsernameDialog() {
        Dialog dialog = Utils.createDialog(this, R.layout.dialog_choose_username);
        DialogChooseUsernameBinding dialogBinding = DialogChooseUsernameBinding.inflate(getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());





        dialog.show();

        EditText usernameEt = dialogBinding.chooseUsernameEt;
        TextView errorTv = dialogBinding.chooseUsernameErrorTv;

        TextView cancel = dialogBinding.chooseUsernameCancelTv;
        TextView confirm = dialogBinding.chooseUsernameCreateAccTv;

        cancel.setOnClickListener(v -> {
            Utils.resetEditText(usernameEt, errorTv);
            dialog.dismiss();
        });

        confirm.setOnClickListener(v -> handleSignup());
    }

    private void handleSignup() {
        String email = emailEt.getText().toString();
        String password = passwordEt.getText().toString();

        // Validate email and password inputs
        Utils.validateInput(emailEt, emailErrorTv, Patterns.EMAIL_ADDRESS, emailIncorrectMsg);
        Utils.validateInput(passwordEt, passwordErrorTv, passwordPattern, passwordIncorrect);

        // Check if there are no visible errors
        if (!Utils.isErrorVisible(emailErrorTv) && !Utils.isErrorVisible(passwordErrorTv)) {
            // TODO: Handle login through AuthenticationManager
            Utils.openActivity(this, ActivityMain.class);
        } else {
            System.out.println("Error is visible");
        }
    }

    private void confirmAccountDialog() {
        Dialog dialog = Utils.createDialog(this, R.layout.dialog_confirm_email);
        DialogConfirmEmailBinding dialogBinding = DialogConfirmEmailBinding.inflate(getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());
        dialog.show();

        dialogBinding.confirmEmailCancelTv.setOnClickListener(v -> {
            dialog.dismiss();
            Utils.openActivity(this, ActivityOpening.class);
        });
        dialogBinding.confirmEmailOpenTv.setOnClickListener(v -> Utils.openEmailClient(this));

        dialog.setOnDismissListener(v -> {
            Utils.openActivity(this, ActivityOpening.class);
        });
    }

    private void loginActivity() {
        Utils.openActivity(this, ActivityLogin.class);
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


    //delete

}
