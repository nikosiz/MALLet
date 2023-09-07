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

        setupToolbar();

        setupClickListeners(binding);

        getLearningSetData();

        ModelLearningSet learningSet = getIntent().getParcelableExtra("learningSet");

        displayFlashcardsInViewPager(Utils.createFlashcardList(learningSet), binding.viewSetViewpager);
        displayFlashcardsInLinearLayout(Utils.createFlashcardList(learningSet), binding.viewSetAllFlashcardsLl, getLayoutInflater());
    }

    private void setupToolbar() {
        Toolbar toolbar = binding.viewSetToolbar;
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        binding.viewSetOptionsBtn.setOnClickListener(v -> dialogSetOptions());
    }

    private void dialogSetOptions() {
        final Dialog dialog = createDialog(R.layout.dialog_set_options);
        DialogSetOptionsBinding binding = DialogSetOptionsBinding.inflate(LayoutInflater.from(this));

        binding.setToolbarOptionsEdit.setOnClickListener(v -> Utils.openActivity(this, ActivityEditLearningSet.class));

        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.show();
    }

    private void getLearningSetData() {
        Intent intent = getIntent();
        if (intent != null) {
            ModelLearningSet learningSet = intent.getParcelableExtra("learningSet");
            if (learningSet != null) {
                String setName = learningSet.getLearningSetName();
                String setCreator = learningSet.getLearningSetCreator();
                String setTerms = String.valueOf(learningSet.getNumberOfTerms());
                List<ModelFlashcard> flashcards = learningSet.getLearningSetTerms();

                TextView setNameTV = binding.viewSetNameTv;
                TextView setCreatorTV = binding.viewSetCreatorTv;
                TextView setTermsTV = binding.viewSetTermsTv;

                if (setName != null) {
                    setNameTV.setText(setName);
                }

                if (setCreator != null) {
                    setCreatorTV.setText(setCreator);
                }

                if (setTerms != null) {
                    setTermsTV.setText(setTerms + " terms");
                }
            }
        }
    }

    private void displayFlashcardsInViewPager(List<ModelFlashcard> flashcards, ViewPager2 viewPager) {
        List<ModelFlashcard> simplifiedFlashcards = new ArrayList<>();

        for (ModelFlashcard flashcard : flashcards) {
            ModelFlashcard simplifiedFlashcard = new ModelFlashcard(flashcard.getTerm(), "", flashcard.getTranslation());
            simplifiedFlashcards.add(simplifiedFlashcard);
        }

        AdapterFlashcard adapter = new AdapterFlashcard(simplifiedFlashcards, v -> Utils.openActivityWithFragment(this, FragmentFlashcards.class, ActivityLearn.class));
        viewPager.setAdapter(adapter);
        viewPager.setPageTransformer(Utils::applySwipeTransformer);
    }

    private void setupClickListeners(ActivityViewLearningSetBinding binding) {
        binding.viewSetViewpager.setOnClickListener(v -> Utils.openActivityWithFragment(this, FragmentFlashcards.class, ActivityLearn.class));
        binding.viewSetFlashcards.setOnClickListener(v -> Utils.openActivityWithFragment(this, FragmentFlashcards.class, ActivityLearn.class));
        binding.viewSetLearn.setOnClickListener(v -> Utils.openActivityWithFragment(this, FragmentLearn.class, ActivityLearn.class));
        binding.viewSetTest.setOnClickListener(v -> Utils.openActivityWithFragment(this, FragmentTest.class, ActivityLearn.class));
        binding.viewSetMatch.setOnClickListener(v -> Utils.openActivityWithFragment(this, FragmentMatch.class, ActivityLearn.class));
    }

    private void displayFlashcardsInLinearLayout(List<ModelFlashcard> flashcards, LinearLayout linearLayout, LayoutInflater inflater) {
        linearLayout.removeAllViews();

        for (ModelFlashcard flashcard : flashcards) {
            View flashcardItemView = inflater.inflate(R.layout.model_flashcard, linearLayout, false);

            LinearLayout flashcardCvLL = flashcardItemView.findViewById(R.id.flashcard_cv_ll);
            flashcardCvLL.setPadding(100, 75, 100, 75);

            TextView flashcardTermTV = flashcardItemView.findViewById(R.id.flashcard_term);
            flashcardTermTV.setVisibility(View.VISIBLE);
            flashcardTermTV.setText(flashcard.getTerm());

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

    private Dialog createDialog(int layoutResId) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layoutResId);
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        return dialog;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}