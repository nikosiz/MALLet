package com.example.mallet;

import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class ChooseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        MaterialButton logInButton = findViewById(R.id.choose_log_in_btn);

        // Set up shared element enter transition
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setSharedElementEnterTransition(
                    TransitionInflater.from(this).inflateTransition(R.transition.fade_and_slide));
        }

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open LogInActivity
                Intent intent = new Intent(ChooseActivity.this, LogInActivity.class);
                startActivity(intent);
            }
        });
    }
}
