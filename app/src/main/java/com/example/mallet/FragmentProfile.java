package com.example.mallet;

import static com.example.mallet.MALLet.MAX_RETRY_ATTEMPTS;

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
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.example.mallet.backend.client.user.boundary.UserServiceImpl;
import com.example.mallet.databinding.DialogDeleteAccountBinding;
import com.example.mallet.databinding.FragmentProfileBinding;
import com.example.mallet.utils.AuthenticationUtils;
import com.example.mallet.utils.Utils;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentProfile extends Fragment {
    int[] themeClickCounter = {0};
    private FragmentProfileBinding binding;
    private int clickCount = 0;
    private String usernameIncorrect;
    private String emailIncorrect;
    private String passwordIncorrect;
    private TextView themeTv;
    private RadioButton lightThemeRb;
    private RadioButton darkThemeRb;
    private int selectedTheme;
    private SharedPreferences sharedPreferences;
    private UserServiceImpl userService;
    private Long userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        String credential = AuthenticationUtils.get(getActivity());
        userService = new UserServiceImpl(credential);

        setupContents();

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
        userId = sharedPreferences.getLong("userId", 0L);

        progressBar = binding.profileProgressBar;
        Utils.hideItems(progressBar);

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

        themeTv.setText("Light theme");

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

    private ProgressBar progressBar;

    private void deleteAccountDialogWithRestart(int attemptCount) {
        Dialog dialog = Utils.createDialog(requireContext(), R.layout.dialog_delete_account, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT), Gravity.BOTTOM);
        DialogDeleteAccountBinding dialogBinding = DialogDeleteAccountBinding.inflate(LayoutInflater.from(requireContext()));
        dialog.setContentView(dialogBinding.getRoot());
        dialog.show();

        CheckBox deleteAccCb = dialogBinding.deleteAccountCb;
        TextView deleteAccCbErrTv = dialogBinding.deleteAccountCbErrorTv;
        deleteAccCbErrTv.setText(R.string.box_needs_to_be_checked);

        TextView deleteAccCancelTv = dialogBinding.deleteAccountCancelTv;
        TextView deleteAccConfirmTv = dialogBinding.deleteAccountConfirmTv;

        deleteAccCancelTv.setOnClickListener(v -> dialog.dismiss());

        deleteAccConfirmTv.setOnClickListener(v -> {
            if (!deleteAccCb.isChecked()) {
                Utils.showItems(deleteAccCbErrTv);
            } else {
                Utils.hideItems(deleteAccCbErrTv);
                Utils.enableItems(deleteAccConfirmTv);
                Utils.showItems(progressBar);
                userService.deleteUser(userId, new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (Objects.isNull(getView())) {
                            return;
                        }
                        Utils.hideItems(progressBar);

                        sharedPreferences.edit().clear().commit();
                        System.out.println(userId);
                        isLogged = false;

                        sharedPreferences.edit().remove("credential").apply();
                        sharedPreferences.edit().putBoolean("isLogged", isLogged).apply();
                        sharedPreferences.edit().putInt("selectedTheme", selectedTheme);

                        dialog.dismiss();

                        getActivity().finish();

                        Intent intent = new Intent(requireContext(), ActivityOpening.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        if (attemptCount < MAX_RETRY_ATTEMPTS) {
                            // System.out.println(attemptCount);
                            // Retry the operation
                            deleteAccountDialogWithRestart(attemptCount + 1);
                        } else {
                            Utils.showToast(requireContext(), "Network error");
                            Utils.hideItems(progressBar);
                        }
                    }
                });
            }
        });
    }

    private void deleteAccountDialog() {
        deleteAccountDialogWithRestart(0);
    }
}