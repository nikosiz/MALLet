package com.example.mallet;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.agh.api.UserDetailDTO;
import com.example.mallet.backend.client.configuration.ResponseHandler;
import com.example.mallet.backend.client.user.boundary.UserServiceImpl;
import com.example.mallet.databinding.ActivityLoginBinding;
import com.example.mallet.databinding.DialogForgotPasswordBinding;
import com.example.mallet.databinding.DialogForgotPasswordOpenEmailBinding;
import com.example.mallet.backend.exception.MalletException;
import com.example.mallet.utils.Utils;

import java.util.Objects;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityLogin extends AppCompatActivity {
    private EditText emailEt, passwordEt;
    private TextView emailErrTv, passwordErrTv;
    private ActivityLoginBinding binding;
    private final Pattern passwordPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%^*()<>?/|}{~:]).{8,}$");
    private String emailIncorrect, passwordIncorrect;
    private UserServiceImpl userService;
    private ResponseHandler responseHandler;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userService = new UserServiceImpl();
        responseHandler = new ResponseHandler();

        emailEt = binding.loginEmailEt;
        emailErrTv = binding.loginEmailErrorTv;
        emailIncorrect = getString(R.string.email_incorrect);

        passwordEt = binding.loginPasswordEt;
        passwordErrTv = binding.loginPasswordErrorTv;
        passwordIncorrect = getString(R.string.password_incorrect);

        setupContents();
    }

    private void setupContents() {
        setupAnimation();

        Utils.setupEmailTextWatcher(emailEt, emailErrTv);
        Utils.setupPasswordTextWatcher(passwordEt, passwordErrTv);

        binding.loginForgotPasswordTv.setOnClickListener(v -> forgotPasswordDialog());
        binding.loginBtn.setOnClickListener(v -> handleLogin());
        binding.loginSignupHereTv.setOnClickListener(v -> signupActivity());
    }

    private void setupAnimation() {
        TextView logo = binding.loginLogoTv;
        Utils.setupAnimation(this, logo);
    }

    private void forgotPasswordDialog() {
        Dialog dialog = createDialog(R.layout.dialog_forgot_password);
        DialogForgotPasswordBinding dialogBinding = DialogForgotPasswordBinding.inflate(getLayoutInflater());
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setContentView(dialogBinding.getRoot());
        dialog.show();

        EditText dialogEmailEt = dialogBinding.forgotPasswordEmailEt;
        TextView dialogErrTv = dialogBinding.forgotPasswordErrorTv;

        Utils.setupPasswordTextWatcher(dialogEmailEt, dialogErrTv);

        TextView cancel = dialogBinding.forgotPasswordCancelTv;
        TextView confirm = dialogBinding.forgotPasswordConfirmTv;

        cancel.setOnClickListener(v -> dialog.dismiss());
        confirm.setOnClickListener(v -> {
            String email = Objects.requireNonNull(dialogEmailEt.getText()).toString().trim();

            if (!Utils.isErrVisible(dialogErrTv)) {
                // TODO: Implement sending an email with a password-resetting link
                Utils.resetEditText(dialogEmailEt, dialogErrTv);
                dialog.dismiss();
                Utils.showToast(this, "Email sent");
                openEmailDialog();
            }
        });
    }

    private void openEmailDialog() {
        Dialog dialog = createDialog(R.layout.dialog_forgot_password_open_email);
        DialogForgotPasswordOpenEmailBinding dialogBinding = DialogForgotPasswordOpenEmailBinding.inflate(getLayoutInflater());
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setContentView(dialogBinding.getRoot());
        dialog.show();

        TextView confirm = dialogBinding.forgotPasswordOpenEmailOpenTv;

        confirm.setOnClickListener(v -> {
            dialog.dismiss();
            Utils.openEmailClient(this);
        });
    }

    private Dialog createDialog(int layoutResId) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layoutResId);

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        return dialog;
    }

    private void handleLogin() {
        String email = emailEt.getText().toString();
        String password = passwordEt.getText().toString();

        if (!Utils.isErrVisible(emailErrTv) && !Utils.isErrVisible(passwordErrTv)) {
            userService.login(email, password, new Callback<>() {
                @Override
                public void onResponse(Call<UserDetailDTO> call, Response<UserDetailDTO> response) {
                    try {
                        responseHandler.handleResponse(response);
                        Utils.showToast(getApplicationContext(), "Logged in");
                        Utils.openActivity(getApplicationContext(), ActivityMain.class);

                        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isLogged", true);
                        editor.apply();
                    } catch (MalletException e) {
                        Utils.showToast(getApplicationContext(), e.getMessage());
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isLogged", false);
                        editor.apply();
                    }
                }

                @Override
                public void onFailure(Call<UserDetailDTO> call, Throwable t) {
                    System.out.println();
                }
            });
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