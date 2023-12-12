package com.mallet.frontend.view.common.activity;

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

import com.google.android.material.textfield.TextInputEditText;
import com.mallet.MALLet;
import com.mallet.R;
import com.mallet.backend.client.configuration.ResponseHandler;
import com.mallet.backend.client.user.boundary.UserServiceImpl;
import com.mallet.backend.exception.MalletException;
import com.mallet.databinding.ActivitySignupBinding;
import com.mallet.databinding.DialogChooseUsernameBinding;
import com.mallet.databinding.DialogConfirmAccountBinding;
import com.mallet.frontend.utils.ViewUtils;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

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

        this.getOnBackPressedDispatcher().addCallback(this, callback);

        setupContents();
    }

    private void setupContents() {
        progressBar = binding.signupProgressBar;
        ViewUtils.hideItems(progressBar);

        TextView logo = binding.signupLogoTv;
        ViewUtils.setupAnimation(this, logo);

        userService = new UserServiceImpl(StringUtils.EMPTY);

        emailEt = binding.signupEmailEt;
        emailErrTv = binding.signupEmailErrorTv;
        emailIncorrect = getString(R.string.email_incorrect);

        passwordEt = binding.signupPasswordEt;
        passwordErrTv = binding.signupPasswordErrorTv;
        passwordIncorrect = getString(R.string.password_incorrect);

        ViewUtils.setupEmailTextWatcher(emailEt, emailErrTv);
        ViewUtils.setupSignupPasswordTextWatcher(passwordEt, passwordErrTv);

        signupContinueTv = binding.signupContinueTv;
        signupContinueTv.setOnClickListener(v -> validateSignupData());

        signupLoginHereTv = binding.signupLoginHereTv;
        signupLoginHereTv.setOnClickListener(v -> loginActivity());
    }

    private void chooseUsernameDialog() {
        Dialog dialog = ViewUtils.createDialog(this, R.layout.dialog_choose_username, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT), Gravity.BOTTOM);
        DialogChooseUsernameBinding dialogBinding = DialogChooseUsernameBinding.inflate(getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());
        dialog.show();

        dialogUsernameEt = dialogBinding.chooseUsernameEt;
        dialogErrTv = dialogBinding.chooseUsernameErrorTv;
        usernameIncorrect = getString(R.string.username_incorrect);

        ViewUtils.setupUniversalTextWatcher(dialogUsernameEt, dialogErrTv);

        TextView cancelTv = dialogBinding.chooseUsernameCancelTv;
        TextView confirmTv = dialogBinding.chooseUsernameCreateAccTv;

        cancelTv.setOnClickListener(v -> {
            dialog.dismiss();
            ViewUtils.resetEditText(dialogUsernameEt, dialogErrTv);
        });

        confirmTv.setOnClickListener(v -> {
            dialog.dismiss();
            handleSignup();
        });
    }

    private void handleSignup() {
        handleSignupWithRestart(0);
    }

    private void handleSignupWithRestart(int attemptCount) {
        username = Objects.requireNonNull(dialogUsernameEt.getText()).toString().trim();

        if (!ViewUtils.isErrVisible(dialogErrTv)) {
            ViewUtils.disableItems(emailEt, passwordEt, signupContinueTv, signupContinueTv, signupLoginHereTv);
            ViewUtils.showItems(progressBar);
            userService.signUp(username, password, email, new Callback<>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    try {
                        ResponseHandler.handleResponse(response);
                        //AuthenticationUtils.save(getApplicationContext(), email, password);
                        ViewUtils.resetEditText(emailEt, emailErrTv);
                        ViewUtils.resetEditText(passwordEt, passwordErrTv);
                        ViewUtils.resetEditText(dialogUsernameEt, dialogErrTv);

                        confirmAccountDialog();

                        ViewUtils.enableItems(emailEt, passwordEt, signupContinueTv, signupContinueTv, signupLoginHereTv);
                        ViewUtils.hideItems(progressBar);
                    } catch (MalletException e) {
                        ViewUtils.showToast(getApplicationContext(), e.getMessage());
                        ViewUtils.enableItems(emailEt, passwordEt, signupContinueTv, signupContinueTv, signupLoginHereTv);
                        ViewUtils.hideItems(progressBar);
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    if (attemptCount < MALLet.MAX_RETRY_ATTEMPTS) {
                       // System.out.println(attemptCount);
                        // Retry the operation
                        handleSignupWithRestart(attemptCount + 1);
                    } else {
                        ViewUtils.enableItems(emailEt, passwordEt, signupContinueTv, signupContinueTv, signupLoginHereTv);
                        ViewUtils.showToast(getApplicationContext(), "Network error");
                        //Utils.resetEditText(emailEt, emailErrTv);
                        //Utils.resetEditText(passwordEt, passwordErrTv);
                        ViewUtils.hideItems(progressBar);
                        //Utils.showToast(getApplicationContext(), "Email or password are incorrect");
                    }
                }
            });
        } else {
            ViewUtils.enableItems(emailEt, passwordEt, signupContinueTv, signupContinueTv, signupLoginHereTv);
            ViewUtils.hideItems(progressBar);
           // System.out.println("Error is visible");
        }
    }

    private ActivitySignup getThisContext() {
        return this;
    }

    private void close() {
        finish();
    }

    private void validateSignupData() {
        String enteredEmail = Objects.requireNonNull(emailEt.getText()).toString().trim();
        String enteredPassword = Objects.requireNonNull(passwordEt.getText()).toString();

        if (!ViewUtils.isErrVisible(emailErrTv) && !ViewUtils.isErrVisible(passwordErrTv)) {
            email = enteredEmail;
            password = enteredPassword;

            chooseUsernameDialog();
        } else {
           // System.out.println("Error is visible");
        }
    }

    private void confirmAccountDialog() {
        Dialog dialog = ViewUtils.createDialog(this, R.layout.dialog_confirm_account, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT), Gravity.BOTTOM);
        DialogConfirmAccountBinding dialogBinding = DialogConfirmAccountBinding.inflate(getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());

        TextView cancelTv = dialogBinding.confirmAccountCancelTv;

        cancelTv.setOnClickListener(v -> {
            ViewUtils.resetEditText(emailEt, emailErrTv);
            ViewUtils.resetEditText(passwordEt, passwordErrTv);
            ViewUtils.hideItems(emailErrTv, passwordErrTv);
            dialog.dismiss();
        });

        dialog.show();
    }

    private void loginActivity() {
        ViewUtils.openActivity(this, ActivityLogin.class); // Open the login activity
    }
}