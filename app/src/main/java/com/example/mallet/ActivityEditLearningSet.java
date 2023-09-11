package com.example.mallet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.example.mallet.databinding.ActivityEditLearningSetBinding;
import com.example.mallet.utils.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;
import java.util.regex.Pattern;

public class ActivityEditLearningSet extends AppCompatActivity {
    private ActivityEditLearningSetBinding binding;
    TextInputLayout setDescriptionTil;
    ScrollView scrollView;
    EditText setNameEt, setDescriptionEt;
    TextView setNameErrorTv;
    private final Pattern namePattern = Pattern.compile(".*");
    EditText setTermEt, setDefinitionEt, setTranslationEt;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditLearningSetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize views for email and password fields
        setNameEt = binding.editSetNameEt;
        setNameErrorTv = binding.editSetErrorTv;
        setDescriptionEt = binding.editSetDescriptionEt;

        // Initialize and set up the toolbar
        setupToolbar();
        setupContents();
        createSet();
    }

    private void setupContents() {
        Utils.setupTextWatcher(setNameEt, setNameErrorTv, namePattern, "Set name incorrect");

        binding.editSetOptionsIv.setOnClickListener(v -> setOptionsDialog());

        scrollView = binding.editSetFlashcardsSv;

        setNameEt = binding.editSetNameEt;
        setDescriptionTil = binding.editSetDescriptionTil;
        setDescriptionEt = binding.editSetDescriptionEt;
        Utils.hideItem(setDescriptionTil);

        LinearLayout flashcardsLl = binding.editSetCardsLl;
        fab = binding.floatingActionButton;
        fab.setOnClickListener(v -> addFlashcard(flashcardsLl, getLayoutInflater()));

        addFlashcard(flashcardsLl, getLayoutInflater());
        addFlashcard(flashcardsLl, getLayoutInflater());

    }

    // Initialize and set up the toolbar with back arrow functionality.
    private void setupToolbar() {
        Toolbar toolbar = binding.editSetToolbar;
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");

        // Display back arrow on the toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }
    }

    private void setOptionsDialog() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            // Finish this activity and return to the previous activity (if any)
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void createSet() {
        String learningSetName = getIntent().getStringExtra("learningSetName");
        String learningSetDescription = getIntent().getStringExtra("learningSetDescription");

        if (learningSetName != null) {
            setNameEt.setText(learningSetName);
            setNameEt.clearFocus();
            Utils.showToast(this, learningSetName);
        }

        if (learningSetDescription != null) {
            setDescriptionEt.setText(learningSetDescription);
            setDescriptionEt.clearFocus();
            if (learningSetDescription.isEmpty()) {
                Utils.hideItem(setDescriptionTil);
            } else {
                Utils.showItem(setDescriptionTil);
            }
        }
    }

    private void addFlashcard(LinearLayout linearLayout, LayoutInflater inflater) {
        View flashcardItemView = inflater.inflate(R.layout.model_edit_set_flashcard, linearLayout, false);

        CardView flashcardCv = flashcardItemView.findViewById(R.id.editSet_flashcard_cv);
        LinearLayout flashcardCvLl = flashcardItemView.findViewById(R.id.editSet_cv_ll);
        flashcardCvLl.setPadding(100, 75, 100, 75);

        EditText flashcardTermEt = flashcardItemView.findViewById(R.id.editSet_term_et);
        TextView termErrTv = flashcardItemView.findViewById(R.id.editSet_term_err_tv);
        EditText flashcardDefinitionEt = flashcardItemView.findViewById(R.id.editSet_definition_et);
        EditText flashcardTranslationEt = flashcardItemView.findViewById(R.id.editSet_translation_et);
        TextView translationErrTv = flashcardItemView.findViewById(R.id.editSet_translation_err_tv);

        flashcardTermEt.clearFocus();
        flashcardDefinitionEt.clearFocus();
        flashcardTranslationEt.clearFocus();

        Utils.resetEditText(flashcardTermEt, termErrTv);
        Utils.resetEditText(flashcardDefinitionEt, null);
        Utils.resetEditText(flashcardTranslationEt, translationErrTv);

        linearLayout.addView(flashcardItemView);
        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
    }
}
