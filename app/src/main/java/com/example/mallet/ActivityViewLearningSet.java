package com.example.mallet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mallet.databinding.ActivityViewLearningSetBinding;

public class ActivityViewLearningSet extends AppCompatActivity {

    private @NonNull ActivityViewLearningSetBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = com.example.mallet.databinding.ActivityViewLearningSetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getLearningSetData();

    }

    public void getLearningSetData() {
        // TODO: Update PROFILE FRAGMENT to pass more than just name X D
        // Get the set name from the intent extras
        Intent intent = getIntent();
        if (intent != null) {
            String setName = intent.getStringExtra("set_name");

            TextView setNameTextView = findViewById(R.id.set_management_name);

            if (setName != null) {
                setNameTextView.setText(setName);
            }
        }
    }

}