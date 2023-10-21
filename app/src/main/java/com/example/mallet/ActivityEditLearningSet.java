package com.example.mallet;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.mallet.databinding.ActivityEditLearningSetBinding;
import com.example.mallet.databinding.DialogDeleteSetBinding;
import com.example.mallet.databinding.DialogEditSetToolbarOptionsBinding;
import com.example.mallet.databinding.DialogViewGroupManageSetsBinding;
import com.example.mallet.utils.ModelFlashcard;
import com.example.mallet.utils.ModelFolder;
import com.example.mallet.utils.ModelGroup;
import com.example.mallet.utils.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class ActivityEditLearningSet extends AppCompatActivity {
    private ActivityEditLearningSetBinding binding;
    private TextInputLayout setDescriptionTil;
    private ScrollView scrollView;
    private EditText setNameEt, setDescriptionEt;
    private String learningSetName;
    private TextView setNameErrTv;
    private final Pattern namePattern = Pattern.compile(".*");
    private int termCounter = 0;
    private EditText setTermEt, setDefinitionEt, setTranslationEt;
    private LinearLayout flashcardsLl;
    private FloatingActionButton addTermFab;
    private int clickCount = 0;


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
        addTermFab = binding.editSetAddFab;

        setupContents();

        learningSetName = getIntent().getStringExtra("learningSetName");
        String learningSetDescription = getIntent().getStringExtra("learningSetDescription");

        ArrayList<ModelFlashcard> learningSetTerms = getIntent().getParcelableArrayListExtra("learningSetTerms");

        if (learningSetName != null) {
            setNameEt.requestFocus();
            setNameEt.setText(learningSetName);
        }

        if (learningSetTerms != null) {
            populateFlashcardsUI(learningSetTerms);
        }

        setDescriptionEt.setText(learningSetDescription);
    }


    private void setupContents() {
        setupToolbar();

        Utils.setupTextWatcher(setNameEt, setNameErrTv, namePattern, "Set name incorrect");

        binding.editSetAddDescriptionTv.setOnClickListener(v -> {
            clickCount++;
            if (clickCount == 1) {
                Utils.showItem(setDescriptionTil);
                addSetDescription();
            } else if (clickCount == 2) {
                Utils.hideItems(setDescriptionTil);
                clickCount = 0;
            }
        });

        addTermFab.setOnClickListener(v -> addFlashcard(flashcardsLl, getLayoutInflater(), "", "", ""));

    }

    private void setupToolbar() {
        Toolbar toolbar = binding.editSetToolbar;
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");

        binding.editSetToolbarBackIv.setOnClickListener(v -> finish());
        binding.editSetOptionsIv.setOnClickListener(v -> setOptionsDialog());
        binding.editSetSaveIv.setOnClickListener(v -> saveSet());
    }

    private void setOptionsDialog() {
        final Dialog dialog = createDialog(R.layout.dialog_edit_set_toolbar_options);
        DialogEditSetToolbarOptionsBinding dialogBinding = DialogEditSetToolbarOptionsBinding.inflate(LayoutInflater.from(this));
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        Objects.requireNonNull(dialog).setContentView(dialogBinding.getRoot());
        dialog.show();

        dialogBinding.editSetToolbarOptionsAddToFolderTv.setOnClickListener(v -> {
            dialog.dismiss();
            addSetToFolderDialog();
        });
        dialogBinding.editSetToolbarOptionsAddToGroupTv.setOnClickListener(v -> {
            dialog.dismiss();
            addSetToGroupDialog();
        });
        dialogBinding.editSetToolbarOptionsDeleteTv.setOnClickListener(v -> {
            dialog.dismiss();
            deleteSetDialog();
        });
    }

    private void addSetToFolderDialog() {
        final Dialog dialog = createDialog(R.layout.dialog_view_group_manage_sets);
        DialogViewGroupManageSetsBinding dialogBinding = DialogViewGroupManageSetsBinding.inflate(LayoutInflater.from(this));
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        Objects.requireNonNull(dialog).setContentView(dialogBinding.getRoot());

        List<ModelFolder> folders = createFolderList();

        displayFolders(folders, dialogBinding.manageSetsSetListLl, getLayoutInflater());

        dialog.show();
    }

    private void addSetToGroupDialog() {
        final Dialog dialog = createDialog(R.layout.dialog_view_group_manage_sets);
        DialogViewGroupManageSetsBinding dialogBinding = DialogViewGroupManageSetsBinding.inflate(LayoutInflater.from(this));
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        Objects.requireNonNull(dialog).setContentView(dialogBinding.getRoot());

        List<ModelGroup> groups = createGroupList();

        displayGroups(groups, dialogBinding.manageSetsSetListLl, getLayoutInflater());

        dialog.show();

    }

    private void deleteSetDialog() {
        final Dialog dialog = createDialog(R.layout.dialog_delete_set);
        DialogDeleteSetBinding dialogBinding = DialogDeleteSetBinding.inflate(LayoutInflater.from(this));
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Objects.requireNonNull(dialog).setContentView(dialogBinding.getRoot());
        dialog.show();

        TextView deleteMessageTv = dialogBinding.deleteSetTv;

        deleteMessageTv.append(learningSetName.toUpperCase());
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
            setNameEt.requestFocus();
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
        Utils.showToast(this, "Set with the name " + learningSetName + " was saved");
        finish();
    }

    private void populateFlashcardsUI(List<ModelFlashcard> flashcards) {
        for (ModelFlashcard flashcard : flashcards) {
            // Create and populate UI elements for each flashcard
            addFlashcard(flashcardsLl, getLayoutInflater(), flashcard.getTerm(), flashcard.getDefinition(), flashcard.getTranslation());
        }
    }

    private void addFlashcard(LinearLayout linearLayout, LayoutInflater inflater, String
            term, String definition, String translation) {
        // Create and populate UI elements for a flashcard
        View flashcardItemView = inflater.inflate(R.layout.model_add_flashcard, linearLayout, false);

        // Find the relevant UI elements in flashcardItemView and set their values
        EditText flashcardTermEt = flashcardItemView.findViewById(R.id.editSet_term_et);
        EditText flashcardDefinitionEt = flashcardItemView.findViewById(R.id.editSet_definition_et);
        EditText flashcardTranslationEt = flashcardItemView.findViewById(R.id.editSet_translation_et);

        TextView counter = flashcardItemView.findViewById(R.id.editSet_counterTv);

        flashcardTermEt.setText(term);
        flashcardDefinitionEt.setText(definition);
        flashcardTranslationEt.setText(translation);

        // Add the flashcard to the layout
        linearLayout.addView(flashcardItemView);
        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
        termCounter++;
        counter.append(String.valueOf(termCounter));

        if (termCounter == 1) {
            flashcardTermEt.requestFocus();
        }

    }

    @Override
    public void onBackPressed() {
        System.out.println(getClass().getSimpleName() + " was closed");
        finish();
    }

    // TODO: Replace with actual data containing account's folders and groups
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

    private void displayFolders(List<ModelFolder> folders, LinearLayout
            linearLayout, LayoutInflater inflater) {
        linearLayout.removeAllViews();

        for (ModelFolder folder : folders) {
            View folderItemView = inflater.inflate(R.layout.model_folder, linearLayout, false);

            // Find views in the folderItemView based on your layout
            TextView folderNameTextView = folderItemView.findViewById(R.id.folder_model_nameTv);
            TextView folderCreatorTextView = folderItemView.findViewById(R.id.folder_model_creatorTv);

            // Set folder data to the views
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
            String nrOfMembers = String.valueOf(group.getNrOfMembers());
            TextView groupNrOfSetsTv = groupItemView.findViewById(R.id.group_nrOfSetsTv);
            String nrOfSets = String.valueOf(group.getNrOfSets());

            groupNameTv.setText(group.getGroupName());
            groupNrOfMembersTv.setText(getString(R.string.string_members, nrOfMembers));
            groupNrOfSetsTv.setText(getString(R.string.string_sets, nrOfSets));

            linearLayout.addView(groupItemView);
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
