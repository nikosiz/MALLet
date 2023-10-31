package com.example.mallet;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mallet.databinding.ActivityViewLearningSetBinding;
import com.example.mallet.databinding.DialogViewSetToolbarOptionsBinding;
import com.example.mallet.utils.AdapterFlashcardViewPager;
import com.example.mallet.utils.ModelFlashcard;
import com.example.mallet.utils.ModelLearningSet;
import com.example.mallet.utils.Utils;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ActivityViewLearningSet extends AppCompatActivity {
    private ActivityViewLearningSetBinding binding;
    private ModelLearningSet learningSet;
    private ImageView toolbarBackIv, toolbarOptionsIv;

    private LinearLayout flashcardsLl, learnLl, testLl, matchLl;
    private ExtendedFloatingActionButton viewSetStudyEfab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewLearningSetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        learningSet = getIntent().getParcelableExtra("learningSet");

        setupContents();

        getLearningSetData();

        displayFlashcardsInViewPager(Utils.createFlashcardList(learningSet), binding.viewSetFlashcardVp2);
        displayFlashcardsInLinearLayout(Utils.createFlashcardList(learningSet), binding.viewSetAllFlashcardsLl, getLayoutInflater());
    }

    private void setupContents() {
        setupToolbar();

        flashcardsLl = binding.viewSetFlashcardsLl;
        flashcardsLl.setOnClickListener(v -> startFlashcards());

        learnLl = binding.viewSetLearnLl;
        learnLl.setOnClickListener(v -> Utils.openActivityWithFragment(this, FragmentLearn.class, ActivityLearn.class));

        testLl = binding.viewSetTestLl;
        testLl.setOnClickListener(v -> Utils.openActivityWithFragment(this, FragmentTest.class, ActivityLearn.class));

        matchLl = binding.viewSetMatchLl;
        matchLl.setOnClickListener(v -> Utils.openActivityWithFragment(this, FragmentMatch.class, ActivityLearn.class));

        viewSetStudyEfab = binding.viewSetStudyEfab;
        viewSetStudyEfab.setOnClickListener(v -> Utils.openActivityWithFragment(this, FragmentFlashcards.class, ActivityLearn.class));
    }

    private void startFlashcards() {
        Intent intent = new Intent(this, ActivityLearn.class);

        intent.putExtra("fragment_class", FragmentFlashcards.class.getName()); // Pass the class name
        intent.putExtra("learningSet", learningSet);

        startActivity(intent);
    }


    private void setupToolbar() {
        Toolbar toolbar = binding.viewSetToolbar;
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");

        toolbarBackIv = binding.viewSetToolbarBackIv;
        toolbarOptionsIv = binding.viewSetToolbarOptionsIv;

        binding.viewSetToolbarBackIv.setOnClickListener(v -> finish());

        binding.viewSetToolbarOptionsIv.setOnClickListener(v -> viewSetOptionsDialog());
    }

    private void viewSetOptionsDialog() {
        Dialog dialog = Utils.createDialog(this, R.layout.dialog_view_set_toolbar_options, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, MATCH_PARENT), Gravity.BOTTOM);
        DialogViewSetToolbarOptionsBinding dialogBinding = DialogViewSetToolbarOptionsBinding.inflate(LayoutInflater.from(this));
        Objects.requireNonNull(dialog).setContentView(dialogBinding.getRoot());
        dialog.show();

        dialogBinding.viewSetOptionsBackIv.setOnClickListener(v -> dialog.dismiss());

        dialogBinding.viewSetOptionsEditTv.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(this, ActivityEditLearningSet.class);

            intent.putExtra("learningSetName", learningSet.getName());
            intent.putExtra("learningSetDescription", learningSet.getDescription());
            intent.putParcelableArrayListExtra("learningSetTerms", new ArrayList<>(learningSet.getTerms()));

            startActivity(intent);
        });

        dialogBinding.viewSetOptionsAddToCollectionTv.setOnClickListener(v -> {
            dialog.dismiss();
            addSetToUsersCollection();

        });

        dialogBinding.viewSetOptionsCancelTv.setOnClickListener(v -> dialog.dismiss());
    }

    private void addSetToUsersCollection() {
        // TODO
    }

    private void getLearningSetData() {
        Intent intent = getIntent();
        if (intent != null) {
            ModelLearningSet learningSet = intent.getParcelableExtra("learningSet");
            if (learningSet != null) {
                String setName = learningSet.getName();
                String setCreator = learningSet.getCreator();
                String nrOfTerms = String.valueOf(learningSet.getNrOfTerms());

                String setDescription = learningSet.getDescription();

                TextView setNameTv = binding.viewSetNameTv;
                TextView setCreatorTv = binding.viewSetCreatorTv;
                TextView setDescriptionTv = binding.viewSetDescriptionTv;
                TextView setTermsTv = binding.viewSetNrOfTermsTv;

                if (setName != null) {
                    setNameTv.setText(setName);
                }

                if (setCreator != null) {
                    setCreatorTv.setText(setCreator);
                }

                if (setDescription != null) {
                    Utils.showItems(setDescriptionTv);
                    setDescriptionTv.setText(setDescription);
                }

                setTermsTv.setText(getString(R.string.string_terms, nrOfTerms));
            }
        }
    }

    private void displayFlashcardsInViewPager(List<ModelFlashcard> flashcards, ViewPager2
            viewPager) {
        AdapterFlashcardViewPager adapter = new AdapterFlashcardViewPager(flashcards);
        viewPager.setAdapter(adapter);
        viewPager.setPageTransformer(Utils::applySwipeTransformer);
    }


    private void displayFlashcardsInLinearLayout
            (List<ModelFlashcard> flashcards, LinearLayout linearLayout, LayoutInflater inflater) {
        linearLayout.removeAllViews();

        for (ModelFlashcard flashcard : flashcards) {
            View flashcardItemView = inflater.inflate(R.layout.model_flashcard, linearLayout, false);
            Utils.setMargins(this, flashcardItemView, 0, 0, 0, 10);

            int paddingInDp = 20;
            float scale = getResources().getDisplayMetrics().density;
            int paddingInPixels = (int) (paddingInDp * scale + 0.5f);

            LinearLayout flashcardLL = flashcardItemView.findViewById(R.id.flashcard_mainLl);

            flashcardLL.setPadding(paddingInPixels, paddingInPixels, paddingInPixels, paddingInPixels);

            TextView flashcardTermTv = flashcardItemView.findViewById(R.id.flashcard_termTv);
            flashcardTermTv.setVisibility(View.VISIBLE);
            flashcardTermTv.setText(flashcard.getTerm());
            flashcardTermTv.setGravity(Gravity.START);
            flashcardTermTv.setTextSize(20.0f);
            Utils.makeTextPhat(this, flashcardTermTv);

            if (!flashcard.getDefinition().isEmpty()) {
                View aboveDefinition = (View) flashcardItemView.findViewById(R.id.flashcard_aboveDefinitionView);
                Utils.setViewLayoutParams(aboveDefinition, MATCH_PARENT, 5);
                Utils.showItems(aboveDefinition);

                TextView flashcardDefinitionTv = flashcardItemView.findViewById(R.id.flashcard_definitionTv);
                flashcardDefinitionTv.setVisibility(View.VISIBLE);
                flashcardDefinitionTv.setText(flashcard.getDefinition());
                int textColorResId = R.color.colorPrimaryDark;
                Utils.setTextColor(this, flashcardDefinitionTv, textColorResId);
                flashcardDefinitionTv.setGravity(Gravity.START);
                flashcardDefinitionTv.setTextSize(15.0f);
            }

            View aboveTranslation = flashcardItemView.findViewById(R.id.flashcard_aboveTranslationView);
            Utils.setViewLayoutParams(aboveTranslation, MATCH_PARENT, 5);
            Utils.showItems(aboveTranslation);

            TextView flashcardTranslationTv = flashcardItemView.findViewById(R.id.flashcard_translationTv);
            flashcardTranslationTv.setVisibility(View.VISIBLE);
            flashcardTranslationTv.setText(flashcard.getTranslation());
            flashcardTranslationTv.setGravity(Gravity.START);
            flashcardTranslationTv.setTextSize(15.0f);

            linearLayout.addView(flashcardItemView);
        }
    }

    private Dialog createDialog(int layoutResId) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layoutResId);

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        return dialog;
    }
}