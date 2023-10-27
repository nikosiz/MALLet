package com.example.mallet;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mallet.client.error.ResponseHandler;
import com.example.mallet.client.user.UserServiceImpl;
import com.example.mallet.databinding.ActivitySignupBinding;
import com.example.mallet.databinding.DialogChooseUsernameBinding;
import com.example.mallet.databinding.DialogConfirmAccountBinding;
import com.example.mallet.exception.MalletException;
import com.example.mallet.utils.Utils;

import java.util.Objects;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivitySignup extends AppCompatActivity {
    private ActivitySignupBinding binding;
    private EditText emailEt, passwordEt;
    private String email, password, username;
    private TextView emailErrTv, passwordErrTv;
    private String emailIncorrect, passwordIncorrect, usernameIncorrect;
    private final Pattern usernamePattern = Pattern.compile("^[a-zA-Z0-9_]+$");

    private UserServiceImpl userService;
    private ResponseHandler responseHandler;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        userService = new UserServiceImpl();
        responseHandler = new ResponseHandler();

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

        Utils.setupEmailTextWatcher(emailEt, emailErrTv);
        Utils.setupPasswordTextWatcher(passwordEt, passwordErrTv);

        binding.signupContinueTv.setOnClickListener(v -> validateSignupData());
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

        Utils.setupUniversalTextWatcher(dialogUsernameEt, dialogErrTv);

        TextView cancel = dialogBinding.chooseUsernameCancelTv;
        TextView confirm = dialogBinding.chooseUsernameCreateAccTv;

        cancel.setOnClickListener(v -> {
            dialog.dismiss();
            Utils.resetEditText(dialogUsernameEt, dialogErrTv);
        });
        confirm.setOnClickListener(v -> {
            username = Objects.requireNonNull(dialogUsernameEt.getText()).toString().trim();

            if (!Utils.isErrVisible(dialogErrTv)) {
                userService.signUp(username, password, email, new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        try {
                            responseHandler.handleResponse(response);
                            dialog.dismiss();
                            Utils.resetEditText(emailEt, emailErrTv);
                            Utils.resetEditText(passwordEt, passwordErrTv);
                            Utils.resetEditText(dialogUsernameEt, dialogErrTv);
                            confirmAccountDialog();
                        } catch (MalletException e) {
                            Utils.showToast(getApplicationContext(), e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Utils.showToast(getApplicationContext(), "Network failure");
                    }
                });
            }
        });
    }

    private void validateSignupData() {
        String enteredEmail = emailEt.getText().toString().trim();
        String enteredPassword = passwordEt.getText().toString();

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
            dialog.dismiss();
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