package com.example.mallet;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.mallet.databinding.ActivityCreateFolderBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;


public class ActivityCreateFolder extends AppCompatActivity {
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
        getSupportActionBar().setTitle(""); // Set the title to an empty string

        // Display back arrow on the toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void setupClickListeners() {
        binding.createFolderSaveBtn.setOnClickListener(v -> createFolder());
        binding.createFolderAddDescriptionLl.setOnClickListener(v -> addDescription());
    }

    private void addDescription() {
        TextInputLayout folderDescriptionTextInputLayout = binding.createFolderDescriptionTil;
        TextInputEditText folderDescriptionEditText = binding.createFolderDescriptionEt;
        String folderDescription = Objects.requireNonNull(folderDescriptionEditText.getText()).toString();

        FrontendUtils.showItem(folderDescriptionTextInputLayout);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            // Finish this activity and return to the previous activity (if any)
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    // TODO
    private void createFolder() {
        TextInputEditText folderNameEditText = binding.createFolderNameEt;
        String folderName = Objects.requireNonNull(folderNameEditText.getText()).toString();
        TextInputLayout createSetNameTil = binding.createFolderNameTil;
        TextView emptyError = binding.createFolderEmptyNameError;

        if (!TextUtils.isEmpty(folderName)) {
            FrontendUtils.hideItem(emptyError);
            FrontendUtils.showToast(this, "The folder will be created... In the future... With backend...");
            finish();
        } else {
            FrontendUtils.showItem(emptyError);
        }
    }
}
