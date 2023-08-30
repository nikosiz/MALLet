package com.example.mallet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.mallet.databinding.ActivityCreateFolderBinding;
import com.example.mallet.databinding.ActivityFolderManagementBinding;
import com.example.mallet.databinding.ActivityLearnBinding;

public class ActivityFolderManagement extends AppCompatActivity {

    private ActivityFolderManagementBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFolderManagementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    
}