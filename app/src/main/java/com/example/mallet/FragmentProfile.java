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

    private String username, email;

    private void setupContents() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        isLogged = sharedPreferences.getBoolean("isLogged", false);
        username = sharedPreferences.getString("username", "");
        email = sharedPreferences.getString("email", "");

        TextView emailTv = binding.profileEmailTv;
        emailTv.setText(email);

        TextView usernameTv = binding.profileUsernameTv;
        usernameTv.setText(username);

        LinearLayout themeLl = binding.profileThemeLl;
        themeTv = binding.profileThemeTv;
        RadioGroup themeRg = binding.profileThemeRg;
        lightThemeRb = binding.profileLightThemeRb;
        darkThemeRb = binding.profileDarkThemeRb;
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

        logoutTv.setOnClickListener(v -> logOut());

        deleteAccTv.setOnClickListener(v -> deleteAccountDialog());
    }

    private void saveSelectedTheme(int selectedTheme) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("AppSettings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("selectedTheme", selectedTheme);

        editor.apply();
    }

    private boolean isLogged;

    private void logOut() {
        clickCount++;
        if (clickCount == 1) {
            Utils.showToast(getContext(), "Click once again to log out");
        } else if (clickCount == 2) {
            isLogged = false;

            sharedPreferences.edit().putBoolean("isLogged", isLogged).apply();

            Intent intent = new Intent(requireActivity(), ActivityOpening.class);

            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

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
                Utils.hideItems(deleteAccPasswordErrTv, deleteAccCbErrTv);
            }
        });
    }
}