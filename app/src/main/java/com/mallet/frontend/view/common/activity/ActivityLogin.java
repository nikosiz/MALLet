package com.mallet.frontend.view.common.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.agh.api.UserDetailDTO;
import com.google.android.material.textfield.TextInputEditText;
import com.mallet.MALLet;
import com.mallet.R;
import com.mallet.backend.client.configuration.ResponseHandler;
import com.mallet.backend.client.user.boundary.UserServiceImpl;
import com.mallet.backend.exception.MalletException;
import com.mallet.databinding.ActivityLoginBinding;
import com.mallet.frontend.security.CredentialsHandler;
import com.mallet.frontend.utils.ViewUtils;

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
    private Button loginBtn;
    private TextView loginSignupHereTv;
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
        ViewUtils.hideItems(progressBar);

        TextView logoTv = binding.loginLogoTv;
        ViewUtils.setupAnimation(this, logoTv);

        userService = new UserServiceImpl(StringUtils.EMPTY);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        emailEt = binding.loginEmailEt;
        emailErrTv = binding.loginEmailErrorTv;
        emailIncorrect = getString(R.string.email_incorrect);

        passwordEt = binding.loginPasswordEt;
        passwordErrTv = binding.loginPasswordErrorTv;
        passwordIncorrect = getString(R.string.password_incorrect);

        ViewUtils.setupEmailTextWatcher(emailEt, emailErrTv);

        loginBtn = binding.loginBtn;
        loginBtn.setOnClickListener(v -> handleLogin());

        loginSignupHereTv = binding.loginSignupHereTv;
        loginSignupHereTv.setOnClickListener(v -> signupActivity());
    }

    private void handleLogin() {
        handleLoginWithRestart(0);
    }

    private String email, password;

    private void handleLoginWithRestart(int attemptCount) {
        email = Objects.requireNonNull(emailEt.getText()).toString();
        password = Objects.requireNonNull(passwordEt.getText()).toString();

        if (!ViewUtils.isErrVisible(emailErrTv) && !ViewUtils.isErrVisible(passwordErrTv)) {
            ViewUtils.disableItems(emailEt, passwordEt, loginBtn, loginSignupHereTv);
            ViewUtils.showItems(progressBar);
            userService.login(email, password, new Callback<>() {
                @Override
                public void onResponse(Call<UserDetailDTO> call, Response<UserDetailDTO> response) {
                    try {
                        UserDetailDTO userDetailDTO = ResponseHandler.handleResponse(response);
                        CredentialsHandler.save(getApplicationContext(), email, password);

                        Intent intent = new Intent(getApplicationContext(), ActivityMain.class);
                        startActivity(intent);

                        ViewUtils.resetEditText(emailEt, emailErrTv);
                        ViewUtils.resetEditText(passwordEt, passwordErrTv);

                        isLogged = true;

                        sharedPreferences.edit().putBoolean("isLogged", isLogged).apply();
                        sharedPreferences.edit().putLong("userId", userDetailDTO.id()).apply();
                        sharedPreferences.edit().putString("username", userDetailDTO.username()).apply();
                        sharedPreferences.edit().putString("email", userDetailDTO.email()).apply();

                        ViewUtils.enableItems(emailEt, passwordEt, loginBtn, loginSignupHereTv);
                        ViewUtils.hideItems(progressBar);

                        close();
                    } catch (MalletException e) {
                        ViewUtils.showToast(getApplicationContext(), e.getMessage());
                        ViewUtils.enableItems(emailEt, passwordEt, loginBtn, loginSignupHereTv);
                        ViewUtils.hideItems(progressBar);
                    }
                }

                @Override
                public void onFailure(Call<UserDetailDTO> call, Throwable t) {
                    if (attemptCount < MALLet.MAX_RETRY_ATTEMPTS) {
                       // System.out.println(attemptCount);
                        // Retry the operation
                        handleLoginWithRestart(attemptCount + 1);
                    } else {
                        ViewUtils.enableItems(emailEt, passwordEt, loginBtn, loginSignupHereTv);
                        ViewUtils.showToast(getApplicationContext(), "Network error");
                        //Utils.resetEditText(emailEt, emailErrTv);
                        //Utils.resetEditText(passwordEt, passwordErrTv);
                        ViewUtils.hideItems(progressBar);
                    }
                }
            });
        } else {
            ViewUtils.enableItems(emailEt, passwordEt, loginBtn, loginSignupHereTv);
            ViewUtils.hideItems(progressBar);
           // System.out.println("Error is visible");
            ViewUtils.hideItems(progressBar);
        }
    }

    private void signupActivity() {
        ViewUtils.openActivity(this, ActivitySignup.class);
    }

    private void close() {
        finish();
    }
}