package com.example.mallet;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.mallet.databinding.ActivityEditLearningSetBinding;

import java.util.Objects;

public class ActivityEditLearningSet extends AppCompatActivity {
    private ActivityEditLearningSetBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditLearningSetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize and set up the toolbar
        setupToolbar();
        setupClickListeners();
    }

    // Initialize and set up the toolbar with back arrow functionality.
    private void setupToolbar() {
        Toolbar toolbar = binding.editSetToolbar;
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(""); // Set the title to an empty string

        // Display back arrow on the toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void setupClickListeners() {
        binding.editSetOptionsIv.setOnClickListener(v -> createSet());

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

    public void createSet() {
        // TODO
        FrontendUtils.showToast(this, "Set created");
    }


}
