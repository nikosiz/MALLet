package com.example.mallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.mallet.databinding.ActivityCreateFolderBinding;


public class CreateFolderActivity extends AppCompatActivity {
    private ActivityCreateFolderBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_folder);

        // Initialize and set up the toolbar
        setUpToolbar();
        setupClickListeners();
    }

    // Initialize and set up the toolbar with back arrow functionality.
    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.create_folder_toolbar);
        setSupportActionBar(toolbar);

        // Display back arrow on the toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void setupClickListeners() {
        TextView cancelBtn = findViewById(R.id.create_folder_cancel_btn);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close the activity and return to MainActivity with HomeFragment opened
                Intent intent = new Intent(CreateFolderActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
