package com.example.mallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.mallet.databinding.ActivityViewLearningSetBinding;

import java.util.ArrayList;
import java.util.List;

public class ActivityViewLearningSet extends AppCompatActivity {

    private ActivityViewLearningSetBinding binding;

    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewLearningSetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupToolbar();
        List<ModelLearningSet> homeLearningSetList = getHomeLearningSetList();
        getLearningSetData();

        setupClickListeners(binding);
        displaySets(homeLearningSetList, binding);

    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.view_set_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(""); // Set the title to an empty string

        // Display back arrow on the toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void displaySets(List<ModelLearningSet> learningSetList, ActivityViewLearningSetBinding binding) {
        LayoutInflater inflater = getLayoutInflater(); // Get inflater here

        for (ModelLearningSet learningSet : learningSetList) {
            View learningSetItemView = inflater.inflate(R.layout.model_flashcard_small, binding.viewSetCardsLl, false);

            TextView learningSetWordTv = learningSetItemView.findViewById(R.id.model_flashcard_small_word_tv);
            learningSetWordTv.setText(learningSet.getLearningSetWord());

            TextView learningSetDefinitionTv = learningSetItemView.findViewById(R.id.model_flashcard_small_definition_tv);
            learningSetDefinitionTv.setText(learningSet.getLearningSetDefinition());

            // Add folderItemView to the linearLayout
            binding.viewSetHsvLl.addView(learningSetItemView);

            // Set click listener for the learningSetItemView
            learningSetItemView.setOnClickListener(v -> {
                // Open the SetManagement activity and pass the set name as an extra
                Intent intent = new Intent(this, ActivityViewLearningSet.class);
                intent.putExtra("set_name", learningSet.getLearningSetName());
                startActivity(intent);
            });
        }
    }


    private void setupClickListeners(ActivityViewLearningSetBinding binding) {
        binding.viewSetHsvLl.setOnClickListener(v -> openActivityLearn(FragmentFlashcards.class));
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

    private List<ModelLearningSet> getHomeLearningSetList() {
        List<ModelLearningSet> learningSetList = new ArrayList<>();
        learningSetList.add(new ModelLearningSet("Set #1", "102", "user123", "Apple", "Jabłko"));
        learningSetList.add(new ModelLearningSet("Set #2", "144", "user123", "Apple", "Jabłko"));
        learningSetList.add(new ModelLearningSet("Set #3", "256", "user123", "Apple", "Jabłko"));
        learningSetList.add(new ModelLearningSet("Set #4", "138", "user123", "Apple", "Jabłko"));
        learningSetList.add(new ModelLearningSet("Set #5", "101", "user123", "Apple", "Jabłko"));
        return learningSetList;
    }

    private void startLearnActivity() {
        Intent intent = new Intent(this, ActivityLearn.class);
        startActivity(intent);
    }

    private void getLearningSetData() {
        // TODO: Update PROFILE FRAGMENT to pass more than just name X D
        // Get the folder name from the intent extras
        Intent intent = getIntent();
        if (intent != null) {
            String setName = intent.getStringExtra("set_name");
            String setCreator = intent.getStringExtra("set_creator");
            //String setWord = intent.getStringExtra("set_word");
            //String setDefinition = intent.getStringExtra("set_definition");

            TextView setNameTV = findViewById(R.id.view_set_name_tv);
            TextView setCreatorTV = findViewById(R.id.view_set_creator_tv);
            //TextView setWordTV = findViewById(R.id.view_set_word);
            //TextView setDefinitionTV = findViewById(R.id.view_set_definition);

            if (setName != null) {
                setNameTV.setText(setName);
                setCreatorTV.setText(setCreator);
            }
        }
    }

}
