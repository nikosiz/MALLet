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

import com.example.mallet.databinding.ActivityLoginBinding;
import com.example.mallet.databinding.DialogForgotPasswordBinding;
import com.example.mallet.utils.Utils;

import java.util.regex.Pattern;

public class ActivityLogin extends AppCompatActivity {

    private EditText emailEt, passwordEt, dialogEmailEt;
    private TextView emailErrorTv, passwordErrorTv, dialogEmailErrorTv;
    private ActivityLoginBinding binding;
    //private DialogForgotPasswordBinding dialogBinding;
    private Pattern passwordPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%^*()<>?/|}{~:]).{8,}$");
    private String emailIncorrectMsg = "Email incorrect", passwordIncorrect = "The password must be at least 8 characters long and contain at least one digit, one small letter, one big letter and one special character.";
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        emailEt = binding.loginEmailEt;
        emailErrorTv = binding.loginEmailErrorTv;
        passwordEt = binding.loginPasswordEt;
        passwordErrorTv = binding.loginPasswordErrorTv;

        DialogForgotPasswordBinding dialogBinding = DialogForgotPasswordBinding.inflate(getLayoutInflater());

        dialogEmailEt = dialogBinding.forgotPasswordEmailEt;
        dialogEmailErrorTv = dialogBinding.forgotPasswordErrorTv;

        setupContents();

    }

    private void setupContents() {
        setupAnimation();

        Utils.setupTextWatcher(emailEt, emailErrorTv, Patterns.EMAIL_ADDRESS, emailIncorrectMsg);
        Utils.setupTextWatcher(passwordEt, passwordErrorTv, passwordPattern, passwordIncorrect);

        binding.loginForgotPasswordTv.setOnClickListener(v -> forgotPasswordDialog());

        binding.loginBtn.setOnClickListener(v -> handleLogin());
        binding.loginGoogleMatbtn.setOnClickListener(v -> handleLogin());
        binding.loginSignupHereTv.setOnClickListener(v -> signupActivity());

        System.out.println("Listeners setup");
    }

    private void setupAnimation() {
        TextView logo = binding.loginLogoTv;
        Utils.setupAnimation(this, logo);
    }

    private void forgotPasswordDialog() {
        Dialog dialog = Utils.createDialog(this, R.layout.dialog_forgot_password);
        DialogForgotPasswordBinding dialogBinding = DialogForgotPasswordBinding.inflate(getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());
        dialog.show();

        EditText emailEt = dialogBinding.forgotPasswordEmailEt;
        TextView errorTv = dialogBinding.forgotPasswordErrorTv;

        TextView cancel = dialogBinding.forgotPasswordCancelTv;
        TextView confirm = dialogBinding.forgotPasswordConfirmTv;

        cancel.setOnClickListener(v -> dialog.dismiss());
        confirm.setOnClickListener(v -> {
            String email = emailEt.getText().toString().trim(); // Get the trimmed email input
            Utils.validateInput(emailEt, errorTv, Patterns.EMAIL_ADDRESS, emailIncorrectMsg);

            if (Utils.isErrorVisible(errorTv)) {
                System.out.println("Forgot password error is visible");
            } else {

                // TODO: Implement sending an email with password-resetting link
                dialog.dismiss();
                Utils.showToast(this, "Email sent");
            }
        });
    }

    private void handleLogin() {
        String email = binding.loginEmailEt.getText().toString();
        TextView emailErrorTv = binding.loginEmailErrorTv;
        String password = binding.loginPasswordEt.getText().toString();
        TextView passwordErrorTv = binding.loginPasswordErrorTv;

        Utils.validateInput(emailEt, emailErrorTv, Patterns.EMAIL_ADDRESS, emailIncorrectMsg);
        Utils.validateInput(passwordEt, passwordErrorTv, passwordPattern, passwordIncorrect);

        if (!Utils.isErrorVisible(emailErrorTv) && !Utils.isErrorVisible(passwordErrorTv)) {

            // TODO: Handle login through AuthenticationManager
            Utils.openActivity(this, ActivityMain.class);
        } else {
            System.out.println("Error is visible");
        }
    }

    private void signupActivity() {
        Utils.openActivity(this, ActivitySignup.class);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ActivityLogin.this, ActivityOpening.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}