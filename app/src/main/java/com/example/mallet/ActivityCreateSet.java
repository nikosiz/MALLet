package com.example.mallet;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.mallet.databinding.ActivityCreateSetBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class ActivityCreateSet extends AppCompatActivity {
    private ActivityCreateSetBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateSetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize and set up the toolbar
        setupToolbar();
        setupClickListeners();
    }

    // Initialize and set up the toolbar with back arrow functionality.
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.create_set_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(""); // Set the title to an empty string

        // Display back arrow on the toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void setupClickListeners() {
        binding.createSetSaveBtn.setOnClickListener(v -> createSet());
        binding.createSetAddDescriptionLl.setOnClickListener(v -> addDescription());

    }

    private void addDescription() {
        TextInputLayout setDescriptionTextInputLayout = binding.createSetDescriptionTil;
        TextInputEditText setDescriptionEditText = binding.createSetDescriptionEt;
        String setDescription = Objects.requireNonNull(setDescriptionEditText.getText()).toString();

        FrontendUtils.showItem(setDescriptionTextInputLayout);
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
    private void createSet() {
        TextInputEditText setNameEditText = binding.createSetNameEt;
        String setName = Objects.requireNonNull(setNameEditText.getText()).toString();
        TextView emptyNameError = binding.createSetEmptyNameError;

        if (TextUtils.isEmpty(setName)) {
            FrontendUtils.showItem(emptyNameError);
        } else {
            FrontendUtils.hideItem(emptyNameError);
            FrontendUtils.showToast(this,"The set will be created... In the future... With backend...");
            finish();
        }
    }
}
