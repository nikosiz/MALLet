package com.example.mallet;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mallet.backend.client.user.boundary.UserServiceImpl;
import com.example.mallet.backend.exception.MalletException;
import com.example.mallet.databinding.ActivityViewLearningSetBinding;
import com.example.mallet.databinding.DialogViewSetToolbarOptionsBinding;
import com.example.mallet.utils.AdapterFlashcardViewPager;
import com.example.mallet.utils.ModelFlashcard;
import com.example.mallet.utils.ModelLearningSet;
import com.example.mallet.utils.Utils;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityViewLearningSet extends AppCompatActivity {
    private ActivityViewLearningSetBinding binding;
    private UserServiceImpl userService;
    private ModelLearningSet learningSet;
    private long setId;
    private ImageView toolbarBackIv, toolbarOptionsIv;
    private ViewPager2 flashcardsVp2;
    private TextView setNameTv, setCreatorTv, nrOfTermsTv;
    private LinearLayout flashcardsLl, learnLl, testLl, matchLl;
    private ExtendedFloatingActionButton viewSetStudyEfab;

    // toolbar options
    private ImageView toolbarOptionsBackIv;
    private TextView toolbarOptionsEditTv, toolbarOptionsAddToUsersCollectionTv, toolbarOptionsDeleteTv, toolbarOptionsCancelTv;
    private final boolean isSetNew = false;
    private List<ModelFlashcard> flashcards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewLearningSetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupContents();
    }

    private void setupContents() {
        userService = new UserServiceImpl(StringUtils.EMPTY);

        learningSet = getIntent().getParcelableExtra("learningSet");
        setId = learningSet.getId();

        flashcards = Utils.createFlashcardList(learningSet);

        setupToolbar();

        flashcardsVp2 = binding.viewSetFlashcardVp2;

        if (flashcards == null || flashcards.size() == 0) {
            Utils.setViewLayoutParams(flashcardsVp2, MATCH_PARENT, 0);
            Utils.hideItems(flashcardsVp2);
            Utils.showItems(binding.viewSetNoVocabHereLl);
            TextView addVocabTv = binding.viewSetAddVocabTv;
            addVocabTv.setOnClickListener(v -> editSet());
        } else {
            Utils.showItems(flashcardsVp2);
            displayFlashcardsInViewPager(flashcards, flashcardsVp2);
        }

        setNameTv = binding.viewSetNameTv;
        setNameTv.setText(learningSet.getName());
        setCreatorTv = binding.viewSetCreatorTv;
        setCreatorTv.setText(learningSet.getCreator());
        nrOfTermsTv = binding.viewSetNrOfTermsTv;
        //nrOfTermsTv.setText(learningSet.getNrOfTerms());

        flashcardsLl = binding.viewSetFlashcardsLl;
        flashcardsLl.setOnClickListener(v -> startFlashcards());

        learnLl = binding.viewSetLearnLl;
        learnLl.setOnClickListener(v -> startLearn());

        testLl = binding.viewSetTestLl;
        testLl.setOnClickListener(v -> Utils.openActivityWithFragment(this, FragmentTest.class, ActivityLearn.class));

        matchLl = binding.viewSetMatchLl;
        matchLl.setOnClickListener(v -> Utils.openActivityWithFragment(this, FragmentMatch.class, ActivityLearn.class));

        displayFlashcardsInLinearLayout(flashcards, binding.viewSetAllFlashcardsLl, getLayoutInflater());

        viewSetStudyEfab = binding.viewSetStudyEfab;
        viewSetStudyEfab.setOnClickListener(v -> startFlashcards());
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

    private void startFlashcards() {
        Intent intent = new Intent(this, ActivityLearn.class);

        intent.putExtra("fragment_class", FragmentFlashcards.class.getName()); // Pass the class name
        intent.putExtra("learningSet", learningSet);

        startActivity(intent);
    }

    private void startLearn() {
        Intent intent = new Intent(this, ActivityLearn.class);

        intent.putExtra("fragment_class", FragmentLearn.class.getName()); // Pass the class name
        intent.putExtra("learningSet", learningSet);

        startActivity(intent);
    }


    private void viewSetOptionsDialog() {
        Dialog dialog = Utils.createDialog(this, R.layout.dialog_view_set_toolbar_options, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, MATCH_PARENT), Gravity.BOTTOM);
        DialogViewSetToolbarOptionsBinding dialogBinding = DialogViewSetToolbarOptionsBinding.inflate(LayoutInflater.from(this));
        Objects.requireNonNull(dialog).setContentView(dialogBinding.getRoot());
        dialog.show();

        toolbarOptionsBackIv = dialogBinding.viewSetOptionsBackIv;
        toolbarOptionsBackIv.setOnClickListener(v -> dialog.dismiss());

        toolbarOptionsEditTv = dialogBinding.viewSetOptionsEditTv;
        toolbarOptionsEditTv.setOnClickListener(v -> {
            dialog.dismiss();

            if (!isUserSet(setId)) {
                addSetToUsersCollection();
            }

            editSet();
        });

        toolbarOptionsAddToUsersCollectionTv = dialogBinding.viewSetOptionsAddToCollectionTv;
        toolbarOptionsAddToUsersCollectionTv.setOnClickListener(v -> {
            dialog.dismiss();
            addSetToUsersCollection();
        });

        toolbarOptionsDeleteTv = dialogBinding.viewSetOptionsDeleteSetTv;
        toolbarOptionsDeleteTv.setOnClickListener(v -> {
            dialog.dismiss();
            deleteSet();
        });

        toolbarOptionsCancelTv = dialogBinding.viewSetOptionsCancelTv;
        toolbarOptionsCancelTv.setOnClickListener(v -> dialog.dismiss());
    }

    private void editSet() {
        Intent intent = new Intent(this, ActivityEditLearningSet.class);

        intent.putExtra("isSetNew", isSetNew);
        intent.putExtra("learningSet",learningSet);
        /*intent.putExtra("learningSetId", learningSet.getId());
        intent.putExtra("learningSetName", learningSet.getName());
        intent.putExtra("learningSetDescription", learningSet.getDescription());
        intent.putParcelableArrayListExtra("learningSetTerms", new ArrayList<>(learningSet.getTerms()));*/

        startActivity(intent);
    }

    private boolean isUserSet(long id) {
        id = setId;
        // TODO zapytać starego Michała

        return false;
    }

    // If learningSet != userSet:
    private void addSetToUsersCollection() {
        // TODO kazać staremu Michałowi sprawdzić
        userService.addSetToUserSets(setId, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                try {
                    Utils.showToast(getApplicationContext(), "Added to collection");

                } catch (MalletException e) {
                    Utils.showToast(getApplicationContext(), e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    // If learningSet == userSet:
    private void deleteSet() {
        userService.deleteUserSet(setId, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // TODO stary Michał mocno śpi
                try {
                    Utils.showToast(getApplicationContext(), learningSet.getName() + " was deleted");

                } catch (MalletException e) {
                    Utils.showToast(getApplicationContext(), e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    private void displayFlashcardsInViewPager(List<ModelFlashcard> flashcards, ViewPager2
            viewPager) {
        if (flashcards != null && flashcards.size() > 0) {
            AdapterFlashcardViewPager adapter = new AdapterFlashcardViewPager(flashcards);
            viewPager.setAdapter(adapter);
            viewPager.setPageTransformer(Utils::applySwipeTransformer);
        }
    }

    private void displayFlashcardsInLinearLayout
            (List<ModelFlashcard> flashcards, LinearLayout linearLayout, LayoutInflater inflater) {
        linearLayout.removeAllViews();

        if (flashcards != null && flashcards.size() > 0) {
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
                    View aboveDefinition = flashcardItemView.findViewById(R.id.flashcard_aboveDefinitionView);
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
    }

    /*private void getLearningSetData() {
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
    }*/
}