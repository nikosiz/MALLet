package com.example.mallet;

import android.app.Dialog;
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

import com.example.mallet.databinding.ActivityEditLearningSetBinding;
import com.example.mallet.databinding.DialogAddSetToFolderBinding;
import com.example.mallet.databinding.DialogAddSetToGroupBinding;
import com.example.mallet.databinding.DialogDeleteSetBinding;
import com.example.mallet.databinding.DialogEditSetToolbarOptionsBinding;
import com.example.mallet.utils.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class ActivityEditLearningSet extends AppCompatActivity {
    private ActivityEditLearningSetBinding binding;
    TextInputLayout setDescriptionTil;
    ScrollView scrollView;
    EditText setNameEt, setDescriptionEt;
    TextView setNameErrTv;
    private final Pattern namePattern = Pattern.compile(".*");
    private int termCounter = 0;
    EditText setTermEt, setDefinitionEt, setTranslationEt;
    LinearLayout flashcardsLl;
    FloatingActionButton addTermFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditLearningSetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize views for email and password fields
        setNameEt = binding.editSetNameEt;
        setNameErrTv = binding.editSetErrorTv;
        setDescriptionTil = binding.editSetDescriptionTil;
        Utils.hideItem(setDescriptionTil);
        setDescriptionEt = binding.editSetDescriptionEt;
        scrollView = binding.editSetFlashcardsSv;
        flashcardsLl = binding.editSetCardsLl;
        addTermFab = binding.floatingActionButton;

        // Initialize and set up the toolbar
        setupContents();

        // Get the "learningSetName" and "learningSetTerms" from the intent extras
        String learningSetName = getIntent().getStringExtra("learningSetName");
        String learningSetDescription = getIntent().getStringExtra("learningSetDescription");
        ArrayList<ModelFlashcard> learningSetTerms = getIntent().getParcelableArrayListExtra("learningSetTerms");

        // Set the learningSetName to the EditText
        if (learningSetName != null) {
            setNameEt.setText(learningSetName);
        }

        // Use learningSetTerms to populate your flashcards or terms UI elements
        assert learningSetTerms != null;
        populateFlashcardsUI(learningSetTerms);
    }


    private void setupContents() {
        setupToolbar();

        Utils.setupTextWatcher(setNameEt, setNameErrTv, namePattern, "Set name incorrect");

        binding.editSetOptionsIv.setOnClickListener(v -> setOptionsDialog());
        binding.editSetSaveIv.setOnClickListener(v -> saveSet());
        binding.editSetAddDescriptionTv.setOnClickListener(v -> addSetDescription());

        addTermFab.setOnClickListener(v -> addFlashcard(flashcardsLl, getLayoutInflater(), "", "", ""));

    }

    private void setupToolbar() {
        Toolbar toolbar = binding.editSetToolbar;
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        binding.editSetOptionsIv.setOnClickListener(v -> setOptionsDialog());
    }

    private void setOptionsDialog() {
        final Dialog dialog = Utils.createDialog(this, R.layout.dialog_edit_set_toolbar_options);
        DialogEditSetToolbarOptionsBinding dialogBinding = DialogEditSetToolbarOptionsBinding.inflate(LayoutInflater.from(this));
        Objects.requireNonNull(dialog).setContentView(dialogBinding.getRoot());
        dialog.show();

        dialogBinding.editSetToolbarOptionsAddToFolder.setOnClickListener(v -> {
            dialog.dismiss();
            addSetToFolderDialog();
        });
        dialogBinding.editSetToolbarOptionsAddToGroup.setOnClickListener(v -> {
            dialog.dismiss();
            addSetToGroupDialog();
        });
        dialogBinding.editSetToolbarOptionsDelete.setOnClickListener(v -> {
            dialog.dismiss();
            deleteSetDialog();
        });
    }

    private void addSetToFolderDialog() {
        final Dialog dialog = Utils.createDialog(this, R.layout.dialog_add_set_to_folder);
        DialogAddSetToFolderBinding dialogBinding = DialogAddSetToFolderBinding.inflate(LayoutInflater.from(this));
        Objects.requireNonNull(dialog).setContentView(dialogBinding.getRoot());

        List<ModelFolder> folders = createFolderList();

        dialogBinding.addToFolderCloseIv.setOnClickListener(v -> dialog.dismiss());

        displayFolders(folders, dialogBinding.addToFolderFoldersLl, getLayoutInflater());

        dialog.show();
    }

    private void addSetToGroupDialog() {
        final Dialog dialog = Utils.createDialog(this, R.layout.dialog_add_set_to_group);
        DialogAddSetToGroupBinding dialogBinding = DialogAddSetToGroupBinding.inflate(LayoutInflater.from(this));
        Objects.requireNonNull(dialog).setContentView(dialogBinding.getRoot());

        List<ModelGroup> groups = createGroupList();

        dialogBinding.addToGroupCloseIv.setOnClickListener(v -> dialog.dismiss());

        displayGroups(groups, dialogBinding.addToGroupGroupsLl, getLayoutInflater());

        dialog.show();

    }

    private void deleteSetDialog() {
        final Dialog dialog = Utils.createDialog(this, R.layout.dialog_delete_set);
        DialogDeleteSetBinding dialogBinding = DialogDeleteSetBinding.inflate(LayoutInflater.from(this));
        Objects.requireNonNull(dialog).setContentView(dialogBinding.getRoot());

        dialog.show();
    }

    private void addSetDescription() {
        String setDescription = setDescriptionEt.getText().toString().trim();

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

    public void saveSet() {
        String learningSetName = getIntent().getStringExtra("learningSetName");

        if (learningSetName != null) {
            setNameEt.clearFocus();
            setNameEt.setText(learningSetName);
        }

        String learningSetDescription = getIntent().getStringExtra("learningSetDescription");
        if (learningSetDescription != null) {
            setDescriptionEt.clearFocus();
            setDescriptionEt.setText(learningSetDescription);

            if (learningSetDescription.isEmpty()) {
                Utils.hideItem(setDescriptionTil);
            } else {
                Utils.showItem(setDescriptionTil);
            }
        }

        // TODO: Implement functionality creating set
        Utils.showToast(this, "Set with the name " + learningSetName + " was created");
        finish();
    }

    private void populateFlashcardsUI(List<ModelFlashcard> flashcards) {
        for (ModelFlashcard flashcard : flashcards) {
            // Create and populate UI elements for each flashcard
            addFlashcard(flashcardsLl, getLayoutInflater(), flashcard.getTerm(), flashcard.getDefinition(), flashcard.getTranslation());
        }
    }

    private void addFlashcard(LinearLayout linearLayout, LayoutInflater inflater, String term, String definition, String translation) {
        // Create and populate UI elements for a flashcard
        View flashcardItemView = inflater.inflate(R.layout.model_edit_set_flashcard, linearLayout, false);

        // Find the relevant UI elements in flashcardItemView and set their values
        EditText flashcardTermEt = flashcardItemView.findViewById(R.id.editSet_term_et);
        EditText flashcardDefinitionEt = flashcardItemView.findViewById(R.id.editSet_definition_et);
        EditText flashcardTranslationEt = flashcardItemView.findViewById(R.id.editSet_translation_et);


        TextView counter = flashcardItemView.findViewById(R.id.editSet_counter_tv);

        flashcardTermEt.setText(term);
        flashcardDefinitionEt.setText(definition);
        flashcardTranslationEt.setText(translation);

        // Add the flashcard to the layout
        linearLayout.addView(flashcardItemView);
        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
        termCounter++;
        counter.setText("Term #" + termCounter);
    }

    private void displayFolders(List<ModelFolder> folders, LinearLayout linearLayout, LayoutInflater inflater) {
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

    private void displayGroups(List<ModelGroup> groups, LinearLayout linearLayout, LayoutInflater inflater) {
        linearLayout.removeAllViews();

        for (ModelGroup folder : groups) {
            View folderItemView = inflater.inflate(R.layout.model_group, linearLayout, false);

            // Find views in the folderItemView based on your layout
            TextView folderNameTextView = folderItemView.findViewById(R.id.group_model_name_tv);

            // Set folder data to the views
            folderNameTextView.setText(folder.getGroupName());

            linearLayout.addView(folderItemView);
        }
    }

    // TODO: Replace with actual data containing account's folders and groups
    private List<ModelFolder> createFolderList() {
        List<ModelFolder> folders = new ArrayList<>();
        folders.add(new ModelFolder("Folder #1", "user123", "3"));
        folders.add(new ModelFolder("Folder #2", "user123", "7"));
        folders.add(new ModelFolder("Folder #3", "user123", "2"));
        folders.add(new ModelFolder("Folder #4", "user123", "8"));
        folders.add(new ModelFolder("Folder #5", "user123", "1"));
        folders.add(new ModelFolder("Folder #6", "user123", "1"));
        return folders;
    }

    private List<ModelGroup> createGroupList() {
        List<ModelGroup> groups = new ArrayList<>();
        groups.add(new ModelGroup("Group #1", "3"));
        groups.add(new ModelGroup("Group #2", "7"));
        groups.add(new ModelGroup("Group #3", "2"));
        groups.add(new ModelGroup("Group #4", "8"));
        groups.add(new ModelGroup("Group #5", "1"));
        return groups;
    }
}
