package com.example.mallet;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mallet.databinding.ActivityGroupManagementBinding;

public class ActivityGroupManagement extends AppCompatActivity {

    private ActivityGroupManagementBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupManagementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    
}