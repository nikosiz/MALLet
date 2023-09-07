package com.example.mallet;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mallet.databinding.ActivityViewLearningSetBinding;
import com.example.mallet.databinding.DialogSetOptionsBinding;
import com.example.mallet.utils.AdapterFlashcard;
import com.example.mallet.utils.Utils;

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

        // Setup the toolbar
        setupToolbar();

        // Setup click listeners for buttons
        setupClickListeners(binding);

        // Display flashcards in ViewPager
        displayFlashcardsInViewPager(createFlashcardList(), binding.viewSetViewpager);

        // Display flashcards in a LinearLayout
        displayFlashcardsInLinearLayout(createFlashcardList(), binding.viewSetAllFlashcardsLl, getLayoutInflater());
    }

    // Setup the toolbar
    private void setupToolbar() {
        Toolbar toolbar = binding.viewSetToolbar;
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(""); // Set the title to an empty string

        // Enable the home/up button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Set a click listener for the options button
        binding.viewSetOptionsBtn.setOnClickListener(v -> dialogSetOptions());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle clicks on the home/up button in the toolbar
        if (item.getItemId() == android.R.id.home) {
            // Handle the navigation back action here, e.g., finish the activity or navigate up
            onBackPressed(); // This will simulate the back button press
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Show the options dialog
    private void dialogSetOptions() {
        final Dialog dialog = createDialog(R.layout.dialog_set_options);
        DialogSetOptionsBinding binding = DialogSetOptionsBinding.inflate(LayoutInflater.from(this));

        // Set click listeners for options in the dialog
        binding.setToolbarOptionsEdit.setOnClickListener(v -> Utils.openActivity(this, ActivityEditLearningSet.class));
        // Add more click listeners for other options as needed

        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.show();;
    }

    // Create a list of flashcards
    private List<ModelFlashcard> createFlashcardList() {
        return Utils.readFlashcardsFromFile(this, "vocab.txt");
    }

    // Setup click listeners for buttons
    private void setupClickListeners(ActivityViewLearningSetBinding binding) {
        binding.viewSetViewpager.setOnClickListener(v -> Utils.openActivityWithFragment(this, FragmentFlashcards.class, ActivityLearn.class));
        binding.viewSetFlashcards.setOnClickListener(v -> Utils.openActivityWithFragment(this, FragmentFlashcards.class, ActivityLearn.class));
        binding.viewSetLearn.setOnClickListener(v -> Utils.openActivityWithFragment(this, FragmentLearn.class, ActivityLearn.class));
        binding.viewSetTest.setOnClickListener(v -> Utils.openActivityWithFragment(this, FragmentTest.class, ActivityLearn.class));
        binding.viewSetMatch.setOnClickListener(v -> Utils.openActivityWithFragment(this, FragmentMatch.class, ActivityLearn.class));
    }

    // Get learning set data from the intent
    private void getLearningSetData() {
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

    // Display flashcards in a ViewPager
    private void displayFlashcardsInViewPager(List<ModelFlashcard> flashcards, ViewPager2 viewPager) {
        List<ModelFlashcard> simplifiedFlashcards = new ArrayList<>();

        for (ModelFlashcard flashcard : flashcards) {
            // Create simplified flashcards with only TERM and TRANSLATION
            ModelFlashcard simplifiedFlashcard = new ModelFlashcard(flashcard.getTerm(), "", flashcard.getTranslation());
            simplifiedFlashcards.add(simplifiedFlashcard);
        }

        AdapterFlashcard adapter = new AdapterFlashcard(simplifiedFlashcards, v -> Utils.openActivityWithFragment(this, FragmentFlashcards.class, ActivityLearn.class));
        viewPager.setAdapter(adapter);
        viewPager.setPageTransformer(Utils::applySwipeTransformer);
    }

    // Display flashcards in a LinearLayout
    private void displayFlashcardsInLinearLayout(List<ModelFlashcard> flashcards, LinearLayout linearLayout, LayoutInflater inflater) {
        linearLayout.removeAllViews();

        for (ModelFlashcard flashcard : flashcards) {
            View flashcardItemView = inflater.inflate(R.layout.model_flashcard, linearLayout, false);

            LinearLayout flashcardCvLL = flashcardItemView.findViewById(R.id.flashcard_cv_ll);
            flashcardCvLL.setPadding(100, 75, 100, 75);

            TextView flashcardTermTV = flashcardItemView.findViewById(R.id.flashcard_term);
            flashcardTermTV.setVisibility(View.VISIBLE);
            flashcardTermTV.setText(flashcard.getTerm());

            // Hide unused views
            View viewDefinition = flashcardItemView.findViewById(R.id.flashcard_view_definition);
            viewDefinition.setVisibility(View.GONE);

            TextView flashcardDefinitionTV = flashcardItemView.findViewById(R.id.flashcard_definition);
            flashcardDefinitionTV.setVisibility(View.GONE);
            flashcardDefinitionTV.setText("");

            View viewTranslation = flashcardItemView.findViewById(R.id.flashcard_view_translation);
            viewTranslation.setVisibility(View.GONE);

            TextView flashcardTranslationTV = flashcardItemView.findViewById(R.id.flashcard_translation);
            flashcardTranslationTV.setVisibility(View.VISIBLE);
            flashcardTranslationTV.setText(flashcard.getTranslation());

            linearLayout.addView(flashcardItemView);
        }
    }

    // Create a dialog with the specified layout
    private Dialog createDialog(int layoutResId) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layoutResId);
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        return dialog;
    }
}
