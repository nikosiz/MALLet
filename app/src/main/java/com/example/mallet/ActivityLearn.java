package com.example.mallet;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.mallet.databinding.ActivityLearnBinding;

public class ActivityLearn extends AppCompatActivity {

    private ActivityLearnBinding binding;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);

        String fragmentClassName = getIntent().getStringExtra("fragment_class");

        if (fragmentClassName != null) {
            try {
                Fragment fragment = (Fragment) Class.forName(fragmentClassName).newInstance();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .commit();
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    
}
