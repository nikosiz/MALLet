package com.example.mallet;

import static com.example.mallet.MALLet.MAX_RETRY_ATTEMPTS;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mallet.backend.client.configuration.ResponseHandler;
import com.example.mallet.backend.client.user.boundary.UserServiceImpl;
import com.example.mallet.backend.exception.MalletException;
import com.example.mallet.databinding.ActivitySignupBinding;
import com.example.mallet.databinding.DialogChooseUsernameBinding;
import com.example.mallet.databinding.DialogConfirmAccountBinding;
import com.example.mallet.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivitySignup extends AppCompatActivity {
    private TextInputEditText dialogUsernameEt;
    private TextView dialogErrTv;
    private ActivitySignupBinding binding;
    private TextInputEditText emailEt, passwordEt;
    private String email, password, username;
    private TextView emailErrTv, passwordErrTv;
    private String emailIncorrect, passwordIncorrect, usernameIncorrect;
    private UserServiceImpl userService;
    private Context context;
    private ProgressBar progressBar;
    private TextView signupContinueTv, signupLoginHereTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(ActivitySignup.this, ActivityOpening.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        };

        // Register the callback with the onBackPressedDispatcher
        this.getOnBackPressedDispatcher().addCallback(this, callback);


        setupContents();
    }

    private void setupContents() {
        progressBar = binding.signupProgressBar;
        Utils.hideItems(progressBar);

        userService = new UserServiceImpl(StringUtils.EMPTY);

        emailEt = binding.signupEmailEt;
        emailErrTv = binding.signupEmailErrorTv;
        emailIncorrect = getString(R.string.email_incorrect);

        passwordEt = binding.signupPasswordEt;
        passwordErrTv = binding.signupPasswordErrorTv;
        passwordIncorrect = getString(R.string.password_incorrect);

        setupAnimation();

        Utils.setupEmailTextWatcher(emailEt, emailErrTv);
        Utils.setupPasswordTextWatcher(passwordEt, passwordErrTv);

        signupContinueTv = binding.signupContinueTv;
        signupLoginHereTv = binding.signupLoginHereTv;

        signupContinueTv.setOnClickListener(v -> validateSignupData());
        signupLoginHereTv.setOnClickListener(v -> loginActivity());
    }

    private void setupAnimation() {
        TextView logo = binding.signupLogoTv;
        Utils.setupAnimation(this, logo);
    }

    private void chooseUsernameDialog() {
        Dialog dialog = Utils.createDialog(this, R.layout.dialog_choose_username, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT), Gravity.BOTTOM);
        DialogChooseUsernameBinding dialogBinding = DialogChooseUsernameBinding.inflate(getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());
        dialog.show();

        dialogUsernameEt = dialogBinding.chooseUsernameEt;
        dialogErrTv = dialogBinding.chooseUsernameErrorTv;
        usernameIncorrect = getString(R.string.username_incorrect);

        Utils.setupUniversalTextWatcher(dialogUsernameEt, dialogErrTv);

        TextView cancelTv = dialogBinding.chooseUsernameCancelTv;
        TextView confirmTv = dialogBinding.chooseUsernameCreateAccTv;

        cancelTv.setOnClickListener(v -> {
            dialog.dismiss();
            Utils.resetEditText(dialogUsernameEt, dialogErrTv);
        });

        confirmTv.setOnClickListener(v -> {
            dialog.dismiss();
            handleSignup();
        });
    }

    private void handleSignup3Queries(int attemptCount) {
        username = Objects.requireNonNull(dialogUsernameEt.getText()).toString().trim();

        if (!Utils.isErrVisible(dialogErrTv)) {
            Utils.disableItems(emailEt, passwordEt, signupContinueTv, signupContinueTv, signupLoginHereTv);
            Utils.showItems(progressBar);
            userService.signUp(username, password, email, new Callback<>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    try {
                        ResponseHandler.handleResponse(response);
                        //AuthenticationUtils.save(getApplicationContext(), email, password);
                        Utils.resetEditText(emailEt, emailErrTv);
                        Utils.resetEditText(passwordEt, passwordErrTv);
                        Utils.resetEditText(dialogUsernameEt, dialogErrTv);

                        confirmAccountDialog();

                        Utils.enableItems(emailEt, passwordEt, signupContinueTv, signupContinueTv, signupLoginHereTv);
                        Utils.hideItems(progressBar);
                    } catch (MalletException e) {
                        Utils.showToast(getApplicationContext(), e.getMessage());
                        Utils.enableItems(emailEt, passwordEt, signupContinueTv, signupContinueTv, signupLoginHereTv);
                        Utils.hideItems(progressBar);
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    if (attemptCount < MAX_RETRY_ATTEMPTS) {System.out.println(attemptCount);
                        // Retry the operation
                        handleSignup3Queries(attemptCount + 1);
                    } else {
                        Utils.enableItems(emailEt, passwordEt, signupContinueTv, signupContinueTv, signupLoginHereTv);
                        Utils.showToast(getApplicationContext(), "Network error");
                        //Utils.resetEditText(emailEt, emailErrTv);
                        //Utils.resetEditText(passwordEt, passwordErrTv);
                        Utils.hideItems(progressBar);
                    }
                }
            });
        } else {
            Utils.enableItems(emailEt, passwordEt, signupContinueTv, signupContinueTv, signupLoginHereTv);
            Utils.hideItems(progressBar);
            System.out.println("Error is visible");
        }
    }

    private ActivitySignup getThisContext() {
        return this;
    }

    private void handleSignup() {
        handleSignup3Queries(0);
    }


    private void validateSignupData() {
        String enteredEmail = Objects.requireNonNull(emailEt.getText()).toString().trim();
        String enteredPassword = Objects.requireNonNull(passwordEt.getText()).toString();

        if (!Utils.isErrVisible(emailErrTv) && !Utils.isErrVisible(passwordErrTv)) {
            email = enteredEmail;
            password = enteredPassword;

            chooseUsernameDialog();
        } else {
            System.out.println("Error is visible");
        }
    }

    private void confirmAccountDialog() {
        Dialog dialog = Utils.createDialog(this, R.layout.dialog_confirm_account, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT), Gravity.BOTTOM);
        DialogConfirmAccountBinding dialogBinding = DialogConfirmAccountBinding.inflate(getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());
        dialog.show();

        TextView cancelTv = dialogBinding.confirmAccountCancelTv;

        cancelTv.setOnClickListener(v -> {
            Utils.resetEditText(emailEt, emailErrTv);
            Utils.resetEditText(passwordEt, passwordErrTv);
            dialog.dismiss();
            System.out.println("Email and password ETs reset");
            Utils.hideItems(emailErrTv, passwordErrTv);
        });

    }

    private void loginActivity() {
        Utils.openActivity(this, ActivityLogin.class); // Open the login activity
    }
}