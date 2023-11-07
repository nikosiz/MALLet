package com.example.mallet;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.example.mallet.databinding.DialogAboutBinding;
import com.example.mallet.databinding.DialogChangeEmailBinding;
import com.example.mallet.databinding.DialogChangePasswordBinding;
import com.example.mallet.databinding.DialogChangeUsernameBinding;
import com.example.mallet.databinding.DialogDeleteAccountBinding;
import com.example.mallet.databinding.FragmentProfileBinding;
import com.example.mallet.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;
import java.util.regex.Pattern;

public class FragmentProfile extends Fragment {
    private static final String PREFS_NAME = "ProfileAppSettings";
    private static final String KEY_NOTIFICATIONS = "notifications";
    private static final String KET_SETS_OFFLINE = "setsOffline";
    private final Pattern usernamePattern = Pattern.compile("^[a-zA-Z0-9_]+$");
    private final Pattern passwordPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%^*()<>?/|}{~:]).{8,}$");
    int[] themeClickCounter = {0};
    private FragmentProfileBinding binding;
    private int clickCount = 0;
    // Declare your strings here, but don't initialize them immediately
    private String usernameIncorrect;
    private String emailIncorrect;
    private String passwordIncorrect;
    private TextView themeTv;
    private LinearLayout themeRgLl;
    private RadioButton systemThemeRb;
    private RadioButton lightThemeRb;
    private RadioButton darkThemeRb;
    private int selectedTheme;
    private TextInputEditText passwordEt;
    private String password;
    private TextView passwordErrTv, cbErrTv;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        setupContents();
        // Set themeTv based on the current theme
        int currentTheme = AppCompatDelegate.getDefaultNightMode();
        if (currentTheme == AppCompatDelegate.MODE_NIGHT_NO) {
            themeTv.setText("Light theme");
            lightThemeRb.setChecked(true);
            darkThemeRb.setChecked(false);
        } else if (currentTheme == AppCompatDelegate.MODE_NIGHT_YES) {
            themeTv.setText("Dark theme");
            lightThemeRb.setChecked(false);
            darkThemeRb.setChecked(true);
        }

        // Initialize the strings requiring the context here
        usernameIncorrect = getActivity().getString(R.string.username_incorrect);
        emailIncorrect = getActivity().getString(R.string.email_incorrect);
        passwordIncorrect = getActivity().getString(R.string.password_incorrect);

        return binding.getRoot();
    }

    private void setupContents() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        isLogged = sharedPreferences.getBoolean("isLogged", false);

        LinearLayout themeLl = binding.profileThemeLl;
        themeTv = binding.profileThemeTv;
        RadioGroup themeRg = binding.profileThemeRg;
        lightThemeRb = binding.profileLightThemeRb;
        darkThemeRb = binding.profileDarkThemeRb;
        TextView aboutTv = binding.profileAboutTv;
        TextView logoutTv = binding.profileLogoutTv;
        TextView deleteAccTv = binding.profileDeleteAccTv;

        Utils.hideItems(themeRg);
        Utils.disableItems(themeRg);

        themeLl.setOnClickListener(v -> {
            themeClickCounter[0]++;
            if (themeClickCounter[0] % 2 != 0) {
                Utils.showItems(themeRg);
                Utils.enableItems(themeRg);
            } else {
                Utils.hideItems(themeRg);
                Utils.disableItems(themeRg);
            }
        });

        themeRg.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.profile_lightThemeRb) {
                themeTv.setText("Light theme");
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                saveSelectedTheme(AppCompatDelegate.MODE_NIGHT_NO);
            } else if (checkedId == R.id.profile_darkThemeRb) {
                themeTv.setText("Dark theme");
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                saveSelectedTheme(AppCompatDelegate.MODE_NIGHT_YES);
            }
        });

        aboutTv.setOnClickListener(v -> aboutDialog());
        logoutTv.setOnClickListener(v -> logOut());

        deleteAccTv.setOnClickListener(v -> deleteAccountDialog());
    }

    private void saveSelectedTheme(int selectedTheme) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("AppSettings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("selectedTheme", selectedTheme);

        editor.apply();
    }

    private void changeEmailDialog() {
        Dialog dialog = Utils.createDialog(requireContext(), R.layout.dialog_change_email, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT), Gravity.BOTTOM);
        DialogChangeEmailBinding dialogBinding = DialogChangeEmailBinding.inflate(LayoutInflater.from(requireContext()));
        dialog.setContentView(dialogBinding.getRoot());
        dialog.show();

        TextInputEditText newEmailEt = dialogBinding.changeEmailEt;
        TextView emailErrTv = dialogBinding.changeEmailErrorTv;
        Utils.setupEmailTextWatcher(newEmailEt, emailErrTv);

        TextView cancelTv = dialogBinding.changeEmailCancelTv;
        TextView confirmTv = dialogBinding.changeEmailConfirmTv;

        cancelTv.setOnClickListener(v -> {
            Utils.resetEditText(newEmailEt, emailErrTv);
            dialog.dismiss();
        });

        confirmTv.setOnClickListener(v -> {
            String newEmail = Objects.requireNonNull(newEmailEt.getText()).toString();

            if (!newEmail.isEmpty()) {
                if (!Utils.isErrVisible(emailErrTv)) {
                    // TODO: Implement email change
                    dialog.dismiss();
                    System.out.println(newEmail);
                    Utils.showToast(getContext(), "Email changed");
                }
            } else {
                Utils.showItems(emailErrTv);
                emailErrTv.setText(R.string.field_cannot_be_empty);
            }
        });
    }

    private void changeUsernameDialog() {
        Dialog dialog = Utils.createDialog(requireContext(), R.layout.dialog_change_username, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT), Gravity.BOTTOM);
        DialogChangeUsernameBinding dialogBinding = DialogChangeUsernameBinding.inflate(LayoutInflater.from(requireContext()));
        dialog.setContentView(dialogBinding.getRoot());
        dialog.show();

        TextInputEditText newUsernameEt = dialogBinding.changeUsernameNewEt;
        TextView usernameErrTv = dialogBinding.changeUsernameErrorTv;
        Utils.setupUniversalTextWatcher(newUsernameEt, usernameErrTv);

        TextView cancelTv = dialogBinding.changeUsernameCancelTv;
        TextView confirmTv = dialogBinding.changeUsernameConfirmTv;

        cancelTv.setOnClickListener(v -> dialog.dismiss());

        confirmTv.setOnClickListener(v -> {
            String newUsername = Objects.requireNonNull(newUsernameEt.getText()).toString();

            if (!newUsername.isEmpty()) {

                if (!Utils.isErrVisible(usernameErrTv)) {
                    // TODO: Implement username change
                    dialog.dismiss();
                    System.out.println(newUsername);
                    Utils.showToast(getContext(), "Username changed");
                }
            } else {
                Utils.showItems(usernameErrTv);
                usernameErrTv.setText(R.string.field_cannot_be_empty);
            }
        });
    }

    private void changePasswordDialog() {
        Dialog dialog = Utils.createDialog(requireContext(), R.layout.dialog_change_password, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT), Gravity.BOTTOM);
        DialogChangePasswordBinding dialogBinding = DialogChangePasswordBinding.inflate(LayoutInflater.from(requireContext()));
        dialog.setContentView(dialogBinding.getRoot());
        dialog.show();

        TextInputEditText oldPasswordEt = dialogBinding.changePasswordOldEt;
        TextView oldPasswordErrorTv = dialogBinding.changePasswordOldErrorTv;
        Utils.setupPasswordTextWatcher(oldPasswordEt, oldPasswordErrorTv);

        TextInputEditText newPasswordEt = dialogBinding.changePasswordNewEt;
        TextView newPasswordErrorTv = dialogBinding.changePasswordNewErrorTv;
        Utils.setupPasswordTextWatcher(newPasswordEt, newPasswordErrorTv);

        TextInputEditText confirmNewPasswordEt = dialogBinding.changePasswordConfirmNewEt;
        TextView confirmNewPasswordErrorTv = dialogBinding.changePasswordConfirmNewErrorTv;
        Utils.setupConfirmPasswordTextWatcher(newPasswordEt, confirmNewPasswordEt, confirmNewPasswordErrorTv, passwordPattern, passwordIncorrect);

        TextView cancelBtn = dialogBinding.changePasswordCancelTv;
        TextView confirmBtn = dialogBinding.changePasswordConfirmTv;

        cancelBtn.setOnClickListener(v -> dialog.dismiss());

        confirmBtn.setOnClickListener(v -> {
            String oldPassword = Objects.requireNonNull(oldPasswordEt.getText()).toString();

            String newPassword = Objects.requireNonNull(newPasswordEt.getText()).toString();

            String confirmNewPassword = Objects.requireNonNull(confirmNewPasswordEt.getText()).toString();

            if (!Utils.isErrVisible(oldPasswordErrorTv) && !Utils.isErrVisible(newPasswordErrorTv) && !Utils.isErrVisible(confirmNewPasswordErrorTv) && newPassword.equals(confirmNewPassword)) {
                // TODO: Implement password change
                Utils.resetEditText(oldPasswordEt, oldPasswordErrorTv);
                Utils.resetEditText(newPasswordEt, newPasswordErrorTv);
                Utils.resetEditText(confirmNewPasswordEt, confirmNewPasswordErrorTv);
                dialog.dismiss();
                System.out.println(oldPassword + "\n" + newPassword + "\n" + confirmNewPassword);
                Utils.showToast(getContext(), "Password changed");
            } else {
                Utils.showItems(confirmNewPasswordErrorTv);
                confirmNewPasswordErrorTv.setText("Passwords do not match");
                //Utils.showToast(getContext(), "Error is visible or passwords do not match");
            }
        });
    }

    private void saveSwitchState(String switchKey, boolean isChecked) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(switchKey, isChecked);
        editor.apply();
    }

    private boolean getSwitchState(String switchKey) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(switchKey, false); // Default value (false) if key not found
    }

    private void aboutDialog() {
        Dialog dialog = Utils.createDialog(requireContext(), R.layout.dialog_about, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT), Gravity.BOTTOM);
        DialogAboutBinding dialogBinding = DialogAboutBinding.inflate(LayoutInflater.from(requireContext()));
        dialog.setContentView(dialogBinding.getRoot());
        dialog.show();
    }

    private boolean isLogged;

    private void logOut() {
        // TODO: Implement logic for logging out

        clickCount++;
        if (clickCount == 1) {
            Utils.showToast(getContext(), "Click once again to log out");
        } else if (clickCount == 2) {
            isLogged = false;

            sharedPreferences.edit().putBoolean("isLogged", isLogged).commit();

            Intent intent = new Intent(requireActivity(), ActivityOpening.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            requireActivity().finish();

            clickCount = 0;
        }
    }

    // TODO
    private void deleteAccountDialog() {
        Dialog dialog = Utils.createDialog(requireContext(), R.layout.dialog_delete_account, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT), Gravity.BOTTOM);
        DialogDeleteAccountBinding dialogBinding = DialogDeleteAccountBinding.inflate(LayoutInflater.from(requireContext()));
        dialog.setContentView(dialogBinding.getRoot());
        dialog.show();

        TextInputEditText deleteAccPasswordEt = dialogBinding.deleteAccountPasswordEt;
        TextView deleteAccPasswordErrTv = dialogBinding.deleteAccountErrorTv;
        Utils.setupPasswordTextWatcher(deleteAccPasswordEt, deleteAccPasswordErrTv);

        String deleteAccEnteredPassword = Objects.requireNonNull(deleteAccPasswordEt.getText()).toString();

        CheckBox deleteAccCb = dialogBinding.deleteAccountCb;
        TextView deleteAccCbErrTv = dialogBinding.deleteAccountCbErrorTv;
        deleteAccCbErrTv.setText(R.string.box_needs_to_be_checked);

        TextView deleteAccCancelTv = dialogBinding.deleteAccountCancelTv;
        TextView deleteAccConfirmTv = dialogBinding.deleteAccountConfirmTv;

        deleteAccCancelTv.setOnClickListener(v -> dialog.dismiss());

        deleteAccConfirmTv.setOnClickListener(v -> {
            if (!deleteAccCb.isChecked()) {
                Utils.showItems(deleteAccCbErrTv);
            }

            if (deleteAccEnteredPassword.isEmpty()) {
                Utils.showItems(deleteAccPasswordErrTv);
                deleteAccPasswordErrTv.setText(R.string.field_cannot_be_empty);
            }

            if (!deleteAccEnteredPassword.isEmpty() && deleteAccCb.isChecked()) {
                Utils.hideItems(deleteAccPasswordErrTv,deleteAccCbErrTv);
                validatePassword(deleteAccEnteredPassword, deleteAccPasswordErrTv);
            }
        });
    }

    private void validatePassword(String enteredPassword, TextView err) {

            password = enteredPassword;
            //TODO
    }
}