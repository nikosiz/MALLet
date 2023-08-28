package com.example.mallet;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.mallet.databinding.ActivityCreateFolderBinding;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;


public class CreateFolderActivity extends AppCompatActivity {
    private ActivityCreateFolderBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateFolderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize and set up the toolbar
        setupToolbar();
        setupClickListeners();
    }

    // Initialize and set up the toolbar with back arrow functionality.
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.create_folder_toolbar);
        setSupportActionBar(toolbar);

        /*// Display back arrow on the toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }*/
    }

    private void setupClickListeners() {
        binding.createFolderCancelBtn.setOnClickListener(v -> closeActivity());
        binding.createFolderConfirmBtn.setOnClickListener(v -> createFolder());
    }

    private void closeActivity() {
        finish(); // Finish the LogInActivity
    }

    // TODO
    private void createFolder() {
        TextView emptyNameError = binding.createFolderNameError;
        TextInputEditText folderNameEditText = binding.createFolderNameEt;
        String folderName = Objects.requireNonNull(folderNameEditText.getText()).toString();

        if (TextUtils.isEmpty(folderName)) {
            emptyNameError.setVisibility(View.VISIBLE);
        } else {
            showToast("The folder will be created... In the future... With backend...");
            finish();
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
