package com.example.mallet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mallet.databinding.ActivityViewLearningSetBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ActivityViewLearningSet extends AppCompatActivity {

    ActivityViewLearningSetBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewLearningSetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupToolbar();
        getLearningSetData();

        setupClickListeners(binding);

        setupViewpager();

    }

    private void setupViewpager() {
        ViewPager2 viewPager = binding.viewSetViewpager;
        AdapterFlashcardSmall flashcardAdapter = new AdapterFlashcardSmall((createSmallFlashcardList()));
        viewPager.setAdapter(flashcardAdapter);
    }

    // Create a list of flashcards (you can replace this with your actual data)
    private List<ModelFlashcardSmall> createSmallFlashcardList() {
        List<ModelFlashcardSmall> flashcards = new ArrayList<>();
        flashcards.add(new ModelFlashcardSmall("Apple", "Jabłko"));
        flashcards.add(new ModelFlashcardSmall("Pear", "Gruszka"));
        // Add more flashcards as needed
        return flashcards;
    }

    private void setupToolbar() {
        Toolbar toolbar = binding.viewSetToolbar;
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(""); // Set the title to an empty string

        // Display back arrow on the toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void setupClickListeners(ActivityViewLearningSetBinding binding) {
        binding.viewSetFlashcards.setOnClickListener(v -> openActivityLearn(FragmentFlashcards.class));
        binding.viewSetLearn.setOnClickListener(v -> openActivityLearn(FragmentLearn.class));
        binding.viewSetTest.setOnClickListener(v -> openActivityLearn(FragmentTest.class));
        binding.viewSetMatch.setOnClickListener(v -> openActivityLearn(FragmentMatch.class));

    }

    private void openActivityLearn(Class<? extends Fragment> fragmentClass) {
        Intent intent = new Intent(this, ActivityLearn.class);
        intent.putExtra("fragment_class", fragmentClass.getName());
        startActivity(intent);
    }

    private void getLearningSetData() {
        // TODO: Update PROFILE FRAGMENT to pass more than just name X D
        // Get the folder name from the intent extras
        Intent intent = getIntent();
        if (intent != null) {
            String setName = intent.getStringExtra("set_name");
            String setCreator = intent.getStringExtra("set_creator");
            String setTerms = intent.getStringExtra("set_terms");

            TextView setNameTV = binding.viewSetNameTv;
            TextView setCreatorTV = binding.viewSetCreatorTv;
            TextView setTermsTV = binding.viewSetTermsTv;

            if (setName != null) {
                setNameTV.setText(setName);
                setCreatorTV.setText(setCreator);
                setTermsTV.setText(setTerms + " terms");
            }
        }
    }

}
