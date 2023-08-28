package com.example.mallet;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.mallet.databinding.ActivityCreateGroupBinding;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class CreateGroupActivity extends AppCompatActivity {
    private ActivityCreateGroupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateGroupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize and set up the toolbar
        setupToolbar();
        setupClickListeners();
    }

    // Initialize and set up the toolbar with back arrow functionality.
    private void setupToolbar() {
        Toolbar toolbar = binding.createGroupToolbar;
        setSupportActionBar(toolbar);

        /*// Display back arrow on the toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }*/
    }

    private void setupClickListeners() {
        binding.createGroupCancelBtn.setOnClickListener(v -> closeActivity());
        binding.createGroupConfirmBtn.setOnClickListener(v -> createGroup());

    }

    private void closeActivity() {
        finish(); // Finish the LogInActivity
    }

    // TODO
    private void createGroup() {
        TextView emptyNameError = binding.createGroupNameError;
        TextInputEditText groupNameEditText = binding.createGroupNameEt;
        String groupName = Objects.requireNonNull(groupNameEditText.getText()).toString();

        if (TextUtils.isEmpty(groupName)) {
            emptyNameError.setVisibility(View.VISIBLE);
        } else {
            showToast("The group will be created... In the future... With backend...");
            finish();
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
