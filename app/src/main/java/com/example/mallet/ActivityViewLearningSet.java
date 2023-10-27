package com.example.mallet;

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
import androidx.viewpager2.widget.ViewPager2;

import com.example.mallet.databinding.ActivityViewLearningSetBinding;
import com.example.mallet.databinding.DialogAddToGroupBinding;
import com.example.mallet.databinding.DialogViewSetToolbarOptionsBinding;
import com.example.mallet.utils.AdapterFlashcardViewPager;
import com.example.mallet.utils.ModelFlashcard;
import com.example.mallet.utils.ModelFolder;
import com.example.mallet.utils.ModelGroup;
import com.example.mallet.utils.ModelLearningSet;
import com.example.mallet.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ActivityViewLearningSet extends AppCompatActivity {
    private ActivityViewLearningSetBinding binding;
    private ModelLearningSet learningSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewLearningSetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        learningSet = getIntent().getParcelableExtra("learningSet");

        setupContents();

        getLearningSetData();

        displayFlashcardsInViewPager(Utils.createFlashcardList(learningSet), binding.viewSetViewpager);
        displayFlashcardsInLinearLayout(Utils.createFlashcardList(learningSet), binding.viewSetAllFlashcardsLl, getLayoutInflater());
    }

    private void setupContents() {
        setupToolbar();
        binding.viewSetViewpager.setOnClickListener(v -> flipCard());
        binding.viewSetFlashcards.setOnClickListener(v -> Utils.openActivityWithFragment(this, FragmentFlashcards.class, ActivityLearn.class));
        //TODO ZMIENIĆ
        binding.viewSetLearn.setOnClickListener(v -> Utils.openActivityWithFragment(this, FragmentLearn.class, ActivityLearn.class));
        //binding.viewSetLearn.setOnClickListener(v -> Utils.openActivityWithFragment(this, TestowyFragment.class, TestowaAktywność.class));
        binding.viewSetTest.setOnClickListener(v -> Utils.openActivityWithFragment(this, FragmentTest.class, ActivityLearn.class));
        binding.viewSetMatch.setOnClickListener(v -> Utils.openActivityWithFragment(this, FragmentMatch.class, ActivityLearn.class));
        binding.testBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, ActivityLearn.class);

            // Pass the learning set data to ActivityLearn
            intent.putExtra("learningSet", learningSet);

            startActivity(intent);
        });
    }

    private void setupToolbar() {
        Toolbar toolbar = binding.viewSetToolbar;
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");

        binding.flashcardsBackIv.setOnClickListener(v -> finish());

        binding.viewSetOptionsBtn.setOnClickListener(v -> viewSetOptionsDialog());
    }

    private void flipCard() {

    }

    private void viewSetOptionsDialog() {
        final Dialog dialog = createDialog(R.layout.dialog_view_set_toolbar_options);
        DialogViewSetToolbarOptionsBinding dialogBinding = DialogViewSetToolbarOptionsBinding.inflate(LayoutInflater.from(this));
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        Objects.requireNonNull(dialog).setContentView(dialogBinding.getRoot());
        dialog.show();

        dialogBinding.viewSetOptionsAddToCollectionTv.setOnClickListener(v -> {
            dialog.dismiss();

            //setService.addSet();

        });

        dialogBinding.viewSetOptionsEditTv.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(this, ActivityEditLearningSet.class);

            intent.putExtra("learningSetName", learningSet.getName());
            intent.putExtra("learningSetDescription", learningSet.getDescription());
            intent.putParcelableArrayListExtra("learningSetTerms", new ArrayList<>(learningSet.getTerms()));

            startActivity(intent);
        });

        dialogBinding.viewSetOptionsAddToGroupTv.setOnClickListener(v -> {
            dialog.dismiss();
            addSetToGroupDialog();
        });

    }

    private void addSetToGroupDialog() {
        final Dialog dialog = createDialog(R.layout.dialog_add_to_group);
        DialogAddToGroupBinding dialogBinding = DialogAddToGroupBinding.inflate(LayoutInflater.from(this));
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        Objects.requireNonNull(dialog).setContentView(dialogBinding.getRoot());
        dialog.show();

        ImageView backIv = dialogBinding.addToGroupTitleBackIv;
        TextInputEditText searchEt = dialogBinding.addToGroupSearchEt;
        LinearLayout noGroupsLl = dialogBinding.addToGroupCreateGroupCvLl;

        List<ModelGroup> groups = new ArrayList<>();//createGroupList();

        if (groups.isEmpty()) {
            noGroupsLl.setVisibility(View.VISIBLE);
        } else {
            noGroupsLl.setVisibility(View.GONE);
            displayGroups(groups, dialogBinding.addToGroupGroupListLl, getLayoutInflater());
        }

        backIv.setOnClickListener(v -> {
            searchEt.clearFocus();
            dialog.dismiss();
        });


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
                TextView setTermsTv = binding.viewSetTermsTv;

                List<ModelFlashcard> flashcards = learningSet.getTerms();

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
        List<ModelFlashcard> simplifiedFlashcards = new ArrayList<>();

        for (ModelFlashcard flashcard : flashcards) {
            ModelFlashcard simplifiedFlashcard = new ModelFlashcard(flashcard.getTerm(), "", flashcard.getTranslation());

            simplifiedFlashcards.add(simplifiedFlashcard);
        }

        AdapterFlashcardViewPager adapter = new AdapterFlashcardViewPager(simplifiedFlashcards, v -> flipCard());
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

            LinearLayout flashcardLL = flashcardItemView.findViewById(R.id.flashcard_mainLl);

            flashcardLL.setPadding(paddingInPixels, paddingInPixels, paddingInPixels, paddingInPixels);

            TextView flashcardTermTv = flashcardItemView.findViewById(R.id.flashcard_termTv);
            flashcardTermTv.setVisibility(View.VISIBLE);
            flashcardTermTv.setText(flashcard.getTerm());
            flashcardTermTv.setGravity(Gravity.START);
            flashcardTermTv.setTextSize(30.0f);

            TextView flashcardDefinitionTv = flashcardItemView.findViewById(R.id.flashcard_definitionTv);
            flashcardDefinitionTv.setVisibility(View.VISIBLE);
            flashcardDefinitionTv.setText(flashcard.getDefinition());
            flashcardDefinitionTv.setGravity(Gravity.START);
            flashcardDefinitionTv.setTextSize(20.0f);

            TextView flashcardTranslationTv = flashcardItemView.findViewById(R.id.flashcard_translationTv);
            flashcardTranslationTv.setVisibility(View.VISIBLE);
            flashcardTranslationTv.setText(flashcard.getTranslation());
            flashcardTranslationTv.setGravity(Gravity.START);
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

    // TODO: Replace with appropriate functionality
    private List<ModelFolder> createFolderList() {
        List<ModelFolder> folders = new ArrayList<>();
        folders.add(new ModelFolder("Folder #1", "user123", "3"));
        folders.add(new ModelFolder("Folder #2", "user123", "7"));
        folders.add(new ModelFolder("Folder #3", "user123", "2"));
        folders.add(new ModelFolder("Folder #4", "user123", "8"));
        folders.add(new ModelFolder("Folder #5", "user123", "1"));
        return folders;
    }

    private void displayFolders(List<ModelFolder> folders, LinearLayout
            linearLayout, LayoutInflater inflater) {
        linearLayout.removeAllViews();

        for (ModelFolder folder : folders) {
            View folderItemView = inflater.inflate(R.layout.model_folder, linearLayout, false);

            TextView folderNameTextView = folderItemView.findViewById(R.id.folder_model_nameTv);
            TextView folderCreatorTextView = folderItemView.findViewById(R.id.folder_model_creatorTv);

            folderNameTextView.setText(folder.getFolderName());
            folderCreatorTextView.setText(folder.getFolderCreator());

            linearLayout.addView(folderItemView);
        }
    }

    private List<ModelGroup> createGroupList() {
        List<ModelGroup> groups = new ArrayList<>();
        groups.add(new ModelGroup("Group #1", "user123", "3"));
        groups.add(new ModelGroup("Group #2", "user123", "7"));
        groups.add(new ModelGroup("Group #3", "user123", "2"));
        groups.add(new ModelGroup("Group #4", "user123", "8"));
        groups.add(new ModelGroup("Group #5", "user123", "1"));
        return groups;
    }

    private void displayGroups(List<ModelGroup> groups, LinearLayout
            linearLayout, LayoutInflater inflater) {
        linearLayout.removeAllViews();

        for (ModelGroup group : groups) {
            View groupItemView = inflater.inflate(R.layout.model_group, linearLayout, false);

            TextView groupNameTv = groupItemView.findViewById(R.id.group_nameTv);
            TextView groupNrOfMembersTv = groupItemView.findViewById(R.id.group_nrOfMembersTv);
            TextView groupNrOfSetsTv = groupItemView.findViewById(R.id.group_nrOfSetsTv);

            groupNameTv.setText(group.getGroupName());
            String nrOfMembers = String.valueOf(group.getNrOfMembers());
            groupNrOfMembersTv.setText(getString(R.string.string_members, nrOfMembers));
            String nrOfSets = String.valueOf(group.getNrOfSets());
            groupNrOfSetsTv.setText(getString(R.string.string_sets, nrOfSets));

            linearLayout.addView(groupItemView);
        }
    }
}