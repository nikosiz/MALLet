package com.example.mallet;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mallet.databinding.ActivitySetManagementBinding;

public class ActivitySetManagement extends AppCompatActivity {

    private ActivitySetManagementBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = com.example.mallet.databinding.ActivitySetManagementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    
}