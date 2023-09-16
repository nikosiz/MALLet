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
import com.example.mallet.databinding.DialogAddSetToFolderBinding;
import com.example.mallet.databinding.DialogDeleteSetBinding;
import com.example.mallet.databinding.DialogViewSetToolbarOptionsBinding;
import com.example.mallet.utils.AdapterFlashcard;
import com.example.mallet.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ActivityViewLearningSet extends AppCompatActivity {
    ActivityViewLearningSetBinding binding;
    ModelLearningSet learningSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewLearningSetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupToolbar();

        setupContents();

        getLearningSetData();

        learningSet = getIntent().getParcelableExtra("learningSet");

        displayFlashcardsInViewPager(Utils.createFlashcardList(learningSet), binding.viewSetViewpager);
        displayFlashcardsInLinearLayout(Utils.createFlashcardList(learningSet), binding.viewSetAllFlashcardsLl, getLayoutInflater());
    }

    private void setupContents() {
        binding.viewSetViewpager.setOnClickListener(v -> flipCard());
        binding.viewSetFlashcards.setOnClickListener(v -> Utils.openActivityWithFragment(this, FragmentFlashcards.class, ActivityLearn.class));
        binding.viewSetLearn.setOnClickListener(v -> Utils.openActivityWithFragment(this, FragmentLearn.class, ActivityLearn.class));
        binding.viewSetTest.setOnClickListener(v -> Utils.openActivityWithFragment(this, FragmentTest.class, ActivityLearn.class));
        binding.viewSetMatch.setOnClickListener(v -> Utils.openActivityWithFragment(this, FragmentMatch.class, ActivityLearn.class));
        binding.testBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, ActivityLearn.class);

            // Pass the learning set data to ActivityLearn
            intent.putExtra("learningSet", learningSet);

            startActivity(intent);
        });

    }

    private void flipCard() {

    }

    private void setupToolbar() {
        Toolbar toolbar = binding.viewSetToolbar;
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");

        binding.viewSetOptionsBtn.setOnClickListener(v -> dialogSetOptions());
    }

    private void dialogSetOptions() {
        final Dialog dialog = createDialog(R.layout.dialog_view_set_toolbar_options);
        DialogViewSetToolbarOptionsBinding dialogBinding = DialogViewSetToolbarOptionsBinding.inflate(LayoutInflater.from(this));
        Objects.requireNonNull(dialog).setContentView(dialogBinding.getRoot());

        dialogBinding.viewSetOptionsEdit.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(this, ActivityEditLearningSet.class);

            intent.putExtra("learningSetName", learningSet.getName());
            intent.putExtra("learningSetDescription", learningSet.getDescription());
            intent.putParcelableArrayListExtra("learningSetTerms", new ArrayList<>(learningSet.getTerms()));

            startActivity(intent);
        });
        dialogBinding.viewSetOptionsAddToFolder.setOnClickListener(v -> {
            dialog.dismiss();
            addSetToFolderDialog();
        });
        dialogBinding.viewSetOptionsAddToGroup.setOnClickListener(v -> {
            //dialog.dismiss();
            Utils.openActivity(this, ActivityEditLearningSet.class);
        });
        dialogBinding.viewSetOptionsDelete.setOnClickListener(v -> {
            dialog.dismiss();
            deleteSetDialog();
        });

        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.show();
    }

    private void deleteSetDialog() {
        final Dialog dialog = Utils.createDialog(this, R.layout.dialog_delete_set);
        DialogDeleteSetBinding dialogBinding = DialogDeleteSetBinding.inflate(LayoutInflater.from(this));
        Objects.requireNonNull(dialog).setContentView(dialogBinding.getRoot());

        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private void addSetToFolderDialog() {
        final Dialog dialog = Utils.createDialog(this, R.layout.dialog_add_set_to_folder);
        DialogAddSetToFolderBinding dialogBinding = DialogAddSetToFolderBinding.inflate(LayoutInflater.from(this));
        Objects.requireNonNull(dialog).setContentView(dialogBinding.getRoot());

        dialogBinding.addToFolderCloseIv.setOnClickListener(v -> dialog.dismiss());

        List<ModelFolder> folders = createFolderList();

        // Assume you have a List<ModelFolder> folders = createFolderList(); defined
        displayFoldersInLinearLayout(folders, dialogBinding.addToFolderFoldersLl, getLayoutInflater());

        dialog.show();
    }

    private void getLearningSetData() {
        Intent intent = getIntent();
        if (intent != null) {
            ModelLearningSet learningSet = intent.getParcelableExtra("learningSet");
            if (learningSet != null) {
                String setName = learningSet.getName();
                String setCreator = learningSet.getCreator();
                String numberOfTerms = String.valueOf(learningSet.getNumberOfTerms());

                String setDescription = learningSet.getDescription();

                TextView setNameTv = binding.viewSetNameTv;
                TextView setCreatorTv = binding.viewSetCreatorTv;
                TextView setDescriptionTv = binding.viewSetDescriptionTv;
                TextView setTermsTv = binding.viewSetTermsTv;

                List<ModelFlashcard> flashcards = learningSet.getTerms();

                if (setName != null) {
                    setNameTv.setText(setName);
                }

                if (setCreator != null) {
                    setCreatorTv.setText(setCreator);
                }

                if (setDescription != null) {
                    Utils.showItem(setDescriptionTv);
                    setDescriptionTv.setText(setDescription);
                }

                setTermsTv.setText(numberOfTerms + " terms");
            }
        }
    }

    private void displayFlashcardsInViewPager(List<ModelFlashcard> flashcards, ViewPager2
            viewPager) {
        List<ModelFlashcard> simplifiedFlashcards = new ArrayList<>();

        for (ModelFlashcard flashcard : flashcards) {
            ModelFlashcard simplifiedFlashcard = new ModelFlashcard(flashcard.getTerm(), "", flashcard.getTranslation());

            simplifiedFlashcards.add(simplifiedFlashcard);
        }

        AdapterFlashcard adapter = new AdapterFlashcard(simplifiedFlashcards, v -> flipCard());
        viewPager.setAdapter(adapter);
        viewPager.setPageTransformer(Utils::applySwipeTransformer);
    }


    private void displayFlashcardsInLinearLayout
            (List<ModelFlashcard> flashcards, LinearLayout linearLayout, LayoutInflater inflater) {
        linearLayout.removeAllViews();

        for (ModelFlashcard flashcard : flashcards) {
            View flashcardItemView = inflater.inflate(R.layout.model_flashcard, linearLayout, false);


            // Convert dp to pixels
            int paddingInDp = 30;
            float scale = getResources().getDisplayMetrics().density;
            int paddingInPixels = (int) (paddingInDp * scale + 0.5f);

            LinearLayout flashcardCvLL = flashcardItemView.findViewById(R.id.flashcard_cvLl);
            flashcardCvLL.setGravity(View.TEXT_ALIGNMENT_TEXT_START);
            flashcardCvLL.setPadding(paddingInPixels, paddingInPixels, paddingInPixels, paddingInPixels);

            TextView flashcardTermTv = flashcardItemView.findViewById(R.id.flashcard_termTv);
            flashcardTermTv.setVisibility(View.VISIBLE);
            flashcardTermTv.setText(flashcard.getTerm());
            flashcardTermTv.setTextSize(25.0f);

            View definitionV = flashcardItemView.findViewById(R.id.flashcard_definitionV);
            definitionV.setVisibility(View.VISIBLE);

            TextView flashcardDefinitionTv = flashcardItemView.findViewById(R.id.flashcard_definitionTv);
            flashcardDefinitionTv.setVisibility(View.GONE);
            flashcardDefinitionTv.setText(flashcard.getDefinition());

            View translationV = flashcardItemView.findViewById(R.id.flashcard_translationV);
            translationV.setVisibility(View.GONE);

            TextView flashcardTranslationTv = flashcardItemView.findViewById(R.id.flashcard_translationTv);
            flashcardTranslationTv.setVisibility(View.VISIBLE);
            flashcardTranslationTv.setText(flashcard.getTranslation());
            flashcardTranslationTv.setTextSize(20.0f);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // TODO: DELETE

    // Create a list of folders
    private List<ModelFolder> createFolderList() {
        List<ModelFolder> folders = new ArrayList<>();
        folders.add(new ModelFolder("Folder #1", "user123", "3"));
        folders.add(new ModelFolder("Folder #2", "user123", "7"));
        folders.add(new ModelFolder("Folder #3", "user123", "2"));
        folders.add(new ModelFolder("Folder #4", "user123", "8"));
        folders.add(new ModelFolder("Folder #5", "user123", "1"));
        return folders;
    }

    private void displayFoldersInLinearLayout(List<ModelFolder> folders, LinearLayout
            linearLayout, LayoutInflater inflater) {
        linearLayout.removeAllViews();

        for (ModelFolder folder : folders) {
            View folderItemView = inflater.inflate(R.layout.model_folder, linearLayout, false);

            // Find views in the folderItemView based on your layout
            TextView folderNameTextView = folderItemView.findViewById(R.id.folder_model_name_tv);
            TextView folderCreatorTextView = folderItemView.findViewById(R.id.folder_model_creator_tv);

            // Set folder data to the views
            folderNameTextView.setText(folder.getFolderName());
            folderCreatorTextView.setText(folder.getFolderCreator());

            linearLayout.addView(folderItemView);
        }
    }
}