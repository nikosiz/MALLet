package com.example.mallet;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class CreateSetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_set);

        // Initialize and set up the toolbar
        setupToolbar();
    }

    // Initialize and set up the toolbar with back arrow functionality.
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.create_set_toolbar);
        setSupportActionBar(toolbar);

        /*// Display back arrow on the toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }*/
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

    private void closeActivity() {
        finish(); // Finish the LogInActivity
    }

    // TODO
    private void createGroup() {
        showToast("The set will be created... In the future... With backend...");
        finish();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
