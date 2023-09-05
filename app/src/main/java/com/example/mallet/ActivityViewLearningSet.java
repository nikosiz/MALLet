package com.example.mallet;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mallet.databinding.ActivityViewLearningSetBinding;
import com.example.mallet.databinding.DialogSetOptionsBinding;
import com.example.mallet.utils.AdapterFlashcardSmall;
import com.example.mallet.utils.FrontendUtils;

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
        displayFlashcards(createSmallFlashcardList(), binding, getLayoutInflater());
    }

    private void setupToolbar() {
        Toolbar toolbar = binding.viewSetToolbar;
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(""); // Set the title to an empty string

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        binding.viewSetOptionsBtn.setOnClickListener(v -> dialogSetOptions());
    }

    private void setupViewpager() {
        ViewPager2 viewPager = binding.viewSetViewpager;
        AdapterFlashcardSmall flashcardAdapter = new AdapterFlashcardSmall((createSmallFlashcardList()));
        viewPager.setAdapter(flashcardAdapter);

        viewPager.setPageTransformer(FrontendUtils::applySwipeTransformer);

    }
    // Create a list of flashcards (you can replace this with your actual data)

    private List<ModelFlashcardSmall> createSmallFlashcardList() {
        List<ModelFlashcardSmall> flashcards = new ArrayList<>();
        flashcards.add(new ModelFlashcardSmall("Apple", "Jabłko"));
        flashcards.add(new ModelFlashcardSmall("Pear", "Gruszka"));
        // Add more flashcards as needed
        return flashcards;
    }

    private void setupClickListeners(ActivityViewLearningSetBinding binding) {
        binding.viewSetFlashcards.setOnClickListener(v -> openActivityLearn(FragmentFlashcards.class));
        binding.viewSetLearn.setOnClickListener(v -> openActivityLearn(FragmentLearn.class));
        binding.viewSetTest.setOnClickListener(v -> openActivityLearn(FragmentTest.class));
        binding.viewSetMatch.setOnClickListener(v -> openActivityLearn(FragmentMatch.class));

    }

    private void dialogSetOptions() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogSetOptionsBinding binding = DialogSetOptionsBinding.inflate(LayoutInflater.from(this));
        dialog.setContentView(binding.getRoot());

        binding.setToolbarOptionsEdit.setOnClickListener(v -> openActivity(this, ActivityEditLearningSet.class));
        //binding.setToolbarOptionsAddFolder.setOnClickListener(v ->);
        //binding.setToolbarOptionsAddGroup.setOnClickListener(v ->);
        //binding.setToolbarOptionsDelete.setOnClickListener(v ->);

        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        FrontendUtils.showDialog(dialog);

    }

    public static void openActivity(Context context, Class<?> targetActivityClass) {
        Intent intent = new Intent(context, targetActivityClass);
        context.startActivity(intent);
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

    private void displayFlashcards(List<ModelFlashcardSmall> learningSetList, ActivityViewLearningSetBinding binding, LayoutInflater inflater) {
        // Clear any existing flashcards from the layout
        binding.viewSetAllFlashcardsLl.removeAllViews();

        for (ModelFlashcardSmall flashcardSmall : learningSetList) {
            // Create a new flashcard view
            View flashcardSmallItemView = inflater.inflate(R.layout.model_flashcard_small, binding.viewSetAllFlashcardsLl, false);

            CardView modelSmallCV = flashcardSmallItemView.findViewById(R.id.model_small_card_view);
            ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(
                    ViewGroup.MarginLayoutParams.MATCH_PARENT,
                    ViewGroup.MarginLayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(10, 20, 10, 10);
            modelSmallCV.setLayoutParams(layoutParams);

            LinearLayout modelSmallLL = flashcardSmallItemView.findViewById(R.id.model_small_cv_ll);
            modelSmallLL.setPadding(0, 100, 0, 100);

            TextView learningSetNameTV = flashcardSmallItemView.findViewById(R.id.model_small_term);
            learningSetNameTV.setGravity(0);

            View viewBetweenWordAndTerm = flashcardSmallItemView.findViewById(R.id.model_small_translation_v);
            viewBetweenWordAndTerm.setVisibility(View.VISIBLE);

            TextView learningSetTermsTV = flashcardSmallItemView.findViewById(R.id.model_small_translation);
            learningSetTermsTV.setVisibility(View.VISIBLE);
            learningSetTermsTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

            // Set the text for the word and translation
            learningSetNameTV.setText(flashcardSmall.getWord());
            learningSetTermsTV.setText(flashcardSmall.getTranslation());

            // Add the flashcard to the linearLayout
            binding.viewSetAllFlashcardsLl.addView(flashcardSmallItemView);
        }
    }


}
