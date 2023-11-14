package com.example.mallet;

import static com.example.mallet.MALLet.MAX_RETRY_ATTEMPTS;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.agh.api.UserDetailDTO;
import com.example.mallet.backend.client.configuration.ResponseHandler;
import com.example.mallet.backend.client.user.boundary.UserServiceImpl;
import com.example.mallet.backend.exception.MalletException;
import com.example.mallet.databinding.ActivityLoginBinding;
import com.example.mallet.utils.AuthenticationUtils;
import com.example.mallet.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityLogin extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private ActivityLoginBinding binding;
    private UserServiceImpl userService;
    private TextInputEditText emailEt, passwordEt;
    private TextView emailErrTv, passwordErrTv;
    private String emailIncorrect, passwordIncorrect;
    private TextView forgotPasswordTv;
    private Button loginBtn;
    private TextView signupRedirectTv;
    private boolean isLogged;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(ActivityLogin.this, ActivityOpening.class);
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
        progressBar = binding.activityLoginProgressBar;
        Utils.hideItems(progressBar);

        // Check if the user is logged in
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        userService = new UserServiceImpl(StringUtils.EMPTY);

        setupAnimation();

        emailEt = binding.loginEmailEt;
        emailErrTv = binding.loginEmailErrorTv;
        emailIncorrect = getString(R.string.email_incorrect);
        Utils.setupEmailTextWatcher(emailEt, emailErrTv);

        passwordEt = binding.loginPasswordEt;
        passwordErrTv = binding.loginPasswordErrorTv;
        passwordIncorrect = getString(R.string.password_incorrect);
        Utils.setupPasswordTextWatcher(passwordEt, passwordErrTv);

        loginBtn = binding.loginBtn;
        loginBtn.setOnClickListener(v -> handleLogin());

        signupRedirectTv = binding.loginSignupHereTv;
        signupRedirectTv.setOnClickListener(v -> signupActivity());
    }

    private void setupAnimation() {
        TextView logo = binding.loginLogoTv;
        Utils.setupAnimation(this, logo);
    }

    private void handleLoginWithRestart(int attemptCount) {
        String email = Objects.requireNonNull(emailEt.getText()).toString();
        String password = Objects.requireNonNull(passwordEt.getText()).toString();

        if (!Utils.isErrVisible(emailErrTv) && !Utils.isErrVisible(passwordErrTv)) {
            Utils.disableItems(emailEt, passwordEt, forgotPasswordTv, loginBtn, signupRedirectTv);
            Utils.showItems(progressBar);
            userService.login(email, password, new Callback<>() {
                @Override
                public void onResponse(Call<UserDetailDTO> call, Response<UserDetailDTO> response) {
                    try {
                        UserDetailDTO userDetailDTO = ResponseHandler.handleResponse(response);
                        AuthenticationUtils.save(getApplicationContext(), email, password);

                        Intent intent = new Intent(getApplicationContext(), ActivityMain.class);
                        startActivity(intent);

                        Utils.resetEditText(emailEt, emailErrTv);
                        Utils.resetEditText(passwordEt, passwordErrTv);

                        isLogged = true;

                        sharedPreferences.edit().putBoolean("isLogged", isLogged).apply();
                        sharedPreferences.edit().putString("username", userDetailDTO.username()).apply();
                        sharedPreferences.edit().putString("email", userDetailDTO.email()).apply();

                        Utils.enableItems(emailEt, passwordEt, forgotPasswordTv, loginBtn, signupRedirectTv);
                        Utils.hideItems(progressBar);
                    } catch (MalletException e) {
                        Utils.showToast(getApplicationContext(), e.getMessage());
                        Utils.enableItems(emailEt, passwordEt, forgotPasswordTv, loginBtn, signupRedirectTv);
                        Utils.hideItems(progressBar);
                    }
                }

                @Override
                public void onFailure(Call<UserDetailDTO> call, Throwable t) {
                    if (attemptCount < MAX_RETRY_ATTEMPTS) {System.out.println(attemptCount);
                        // Retry the operation
                        handleLoginWithRestart(attemptCount + 1);
                    } else {
                        Utils.enableItems(emailEt, passwordEt, forgotPasswordTv, loginBtn, signupRedirectTv);
                        Utils.showToast(getApplicationContext(), "Network error");
                        //Utils.resetEditText(emailEt, emailErrTv);
                        //Utils.resetEditText(passwordEt, passwordErrTv);
                        Utils.hideItems(progressBar);
                    }
                }
            });
        } else {
            Utils.enableItems(emailEt, passwordEt, forgotPasswordTv, loginBtn, signupRedirectTv);
            Utils.hideItems(progressBar);
            System.out.println("Error is visible");
            Utils.hideItems(progressBar);
        }
    }

    private void handleLogin() {
        handleLoginWithRestart(0);
    }

    private void signupActivity() {
        Utils.openActivity(this, ActivitySignup.class);
    }
}