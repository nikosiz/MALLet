package com.example.mallet;

import static com.example.mallet.MALLet.MAX_RETRY_ATTEMPTS;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.mallet.backend.client.configuration.ResponseHandler;
import com.example.mallet.backend.client.group.boundary.GroupServiceImpl;
import com.example.mallet.backend.client.user.boundary.UserServiceImpl;
import com.example.mallet.backend.entity.set.SetCreateContainer;
import com.example.mallet.backend.entity.set.SetCreateContainerMapper;
import com.example.mallet.databinding.ActivityEditLearningSetBinding;
import com.example.mallet.databinding.DialogConfirmExitBinding;
import com.example.mallet.utils.AuthenticationUtils;
import com.example.mallet.utils.ModelFlashcard;
import com.example.mallet.utils.ModelLearningSet;
import com.example.mallet.utils.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityEditLearningSet extends AppCompatActivity {
    private UserServiceImpl userService;
    private ActivityEditLearningSetBinding binding;
    private ModelLearningSet learningSet;
    private List<ModelFlashcard> flashcards;
    private boolean isSetNew;
    private boolean isSetInGroup;
    private long learningSetId;
    private ProgressBar progressBar;
    private ImageView toolbarBackIv, toolbarSaveIv;
    private TextInputEditText setNameEt;
    private TextView setNameErrTv;
    private TextView addDescriptionTv;
    private TextInputLayout setDescriptionTil;
    private TextInputEditText setDescriptionEt;
    private int descriptionTvClickCount = 0;
    private ScrollView scrollView;
    private LinearLayout flashcardsLl;
    private FloatingActionButton addFlashcardFab;
    private int termCounter = 0;
    private TextInputEditText flashcardTermEt;
    private TextInputEditText flashcardDefinitionEt;
    private TextInputEditText flashcardTranslationEt;
    private Long groupId;
    private String groupName;
    private boolean canUserEditSet, isUserSet;
    private TextView toolbarTitleTv;
    private ModelLearningSet newLearningSet;
    private TextView flashCardCounterTv;
    private GroupServiceImpl groupService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditLearningSetBinding.inflate(getLayoutInflater());
        setupContents();
        setContentView(binding.getRoot());

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                confirmExitDialog();
            }
        };

        this.getOnBackPressedDispatcher().addCallback(this, callback);

    }

    private void setupContents() {
        String credential = AuthenticationUtils.get(getApplicationContext());
        this.userService = new UserServiceImpl(credential);
        this.groupService = new GroupServiceImpl(credential);

        groupName = getIntent().getStringExtra("groupName");
        groupId = getIntent().getLongExtra("groupId", 0L);

        learningSet = getIntent().getParcelableExtra("learningSet");

        isSetNew = getIntent().getBooleanExtra("isSetNew", true);
        isUserSet = getIntent().getBooleanExtra("isUserSet", true);
        isSetInGroup = getIntent().getBooleanExtra("isSetInGroup", false);
        canUserEditSet = getIntent().getBooleanExtra("canUserEditSet", false);

        if (learningSet != null && learningSet.getTerms() != null) {
            flashcards = learningSet.getTerms();
        } else {

        }

        progressBar = binding.editSetProgressBar;
        Utils.hideItems(progressBar);

        setupToolbar();

        setNameEt = binding.editSetNameEt;
        setNameErrTv = binding.editSetErrorTv;
        Utils.setupUniversalTextWatcher(setNameEt, setNameErrTv);

        addDescriptionTv = binding.editSetAddDescriptionTv;

        setDescriptionTil = binding.editSetDescriptionTil;
        setDescriptionEt = binding.editSetDescriptionEt;
        Utils.hideItems(setDescriptionTil);

        scrollView = binding.editSetFlashcardsSv;
        flashcardsLl = binding.editSetCardsLl;

        addFlashcardFab = binding.editSetAddFab;

        if (isSetNew) {
            Utils.hideItems(progressBar);

            Utils.resetEditText(setNameEt, setNameErrTv);
            Utils.hideItems(setNameErrTv);

            addFlashcard(flashcardsLl, getLayoutInflater(), null, null, null);
        } else if (!isSetNew || isSetInGroup) {
            setNameEt.setText(learningSet.getName());
            setDescriptionEt.setText(learningSet.getDescription());

            if (learningSet.getTerms() != null) {
                populateFlashcardsUI();
            }
        }

        addDescriptionTv.setOnClickListener(v -> {
            descriptionTvClickCount++;
            if (descriptionTvClickCount == 1) {
                Utils.showItems(setDescriptionTil);
            } else if (descriptionTvClickCount == 2) {
                Utils.hideItems(setDescriptionTil);
                descriptionTvClickCount = 0;
            }
        });

        addFlashcardFab.setOnClickListener(v -> {
            addFlashcard(flashcardsLl, getLayoutInflater(), "", "", "");
        });
    }

    private void setupToolbar() {
        Toolbar toolbar = binding.editSetToolbar;
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");

        toolbarBackIv = binding.editSetToolbarBackIv;
        toolbarBackIv.setOnClickListener(v -> confirmExitDialog());

        toolbarTitleTv = binding.editSetToolbarTitleTv;
        if (isSetNew) {
            toolbarTitleTv.setText("Create set");
        } else if (isSetInGroup || !isSetNew) {
            toolbarTitleTv.setText("Edit set");
        }

        toolbarSaveIv = binding.editSetSaveIv;
        toolbarSaveIv.setOnClickListener(v -> {
            saveSet();
        });
    }

    public void confirmExitDialog() {
        Dialog dialog = Utils.createDialog(this, R.layout.dialog_confirm_exit, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT), Gravity.BOTTOM);
        DialogConfirmExitBinding dialogBinding = DialogConfirmExitBinding.inflate(LayoutInflater.from(this));
        Objects.requireNonNull(dialog).setContentView(dialogBinding.getRoot());
        dialog.show();

        dialogBinding.confirmExitCancelTv.setOnClickListener(v -> dialog.dismiss());

        dialogBinding.confirmExitConfirmTv.setOnClickListener(v -> {
            dialog.dismiss();
            this.finish();
        });
    }

    public void saveSet() {
        String enteredSetName = Objects.requireNonNull(setNameEt.getText()).toString();
        String enteredSetDescription = Objects.requireNonNull(setDescriptionEt.getText()).toString();

        List<ModelFlashcard> enteredFlashcards = new ArrayList<>();

        for (int i = 0; i < flashcardsLl.getChildCount(); i++) {
            View flashcardItemView = flashcardsLl.getChildAt(i);
            TextInputEditText termEt = flashcardItemView.findViewById(R.id.addFlashcard_termEt);
            TextInputEditText definitionEt = flashcardItemView.findViewById(R.id.addFlashcard_definitionEt);
            TextInputEditText translationEt = flashcardItemView.findViewById(R.id.addFlashcard_translationEt);

            String enteredTerm = Objects.requireNonNull(termEt.getText()).toString();
            String enteredDefinition = Objects.requireNonNull(definitionEt.getText()).toString();
            String enteredTranslation = Objects.requireNonNull(translationEt.getText()).toString();

            ModelFlashcard addedFlashcard = new ModelFlashcard(enteredTerm, enteredDefinition, enteredTranslation);
            enteredFlashcards.add(addedFlashcard);
        }

        newLearningSet = new ModelLearningSet(enteredSetName, enteredSetDescription, enteredFlashcards);

        if (!isSetInGroup) {
            if (isSetNew) {
                handleSetCreation(SetCreateContainerMapper.from(newLearningSet));
            }

            if (!isSetNew) {
                //todo usuwanie seta uzywkoniak  i tworzenie nowego
                userService.deleteUserSet(learningSet.getId(), new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        userService.createUserSet(SetCreateContainerMapper.from(newLearningSet), new Callback<Long>() {
                            @Override
                            public void onResponse(Call<Long> call, Response<Long> response) {
                                learningSetId = ResponseHandler.handleResponse(response);

                                Intent intent = new Intent(getApplicationContext(), ActivityViewLearningSet.class);

                                intent.putExtra("setId", learningSetId);
                                intent.putExtra("learningSet", learningSet);

                                startActivity(intent);

                                Utils.showToast(getApplicationContext(), "Set edited");

                                close();
                            }

                            @Override
                            public void onFailure(Call<Long> call, Throwable t) {
                                Utils.showToast(getApplicationContext(), "Network error");
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Utils.showToast(getApplicationContext(), "Network error");
                    }
                });
            }
        } else if (isSetInGroup) {
            if (isSetNew) {
                handleSetInGroupCreation(SetCreateContainerMapper.from(newLearningSet));
            }
            if (!isSetNew) {
                groupService.removeSet(groupId, learningSet.getId(), new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        groupService.createSet(groupId, SetCreateContainerMapper.from(newLearningSet), new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                Utils.showToast(getApplicationContext(), "Set edited");

                                Intent intent = new Intent(getApplicationContext(), ActivityViewGroup.class);

                                intent.putExtra("groupId", groupId);
                                intent.putExtra("groupName", groupName);

                                startActivity(intent);

                                close();
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Utils.showToast(getApplicationContext(), "Network error");
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Utils.showToast(getApplicationContext(), "Network error");
                    }
                });
            }
        }
    }


    private void addFlashcard(LinearLayout linearLayout, LayoutInflater inflater, String
            term, String definition, String translation) {
        View flashcardItemView = inflater.inflate(R.layout.model_add_flashcard, linearLayout, false);

        termCounter++;
        flashCardCounterTv = flashcardItemView.findViewById(R.id.addFlashcard_counterTv);
        flashCardCounterTv.append(String.valueOf(termCounter));

        ImageView deleteIv = flashcardItemView.findViewById(R.id.addFlashcard_deleteIv);
        deleteIv.setOnClickListener(v -> {
            termCounter--;
            linearLayout.removeView(flashcardItemView);
            updateFlashcardCounterText();
        });

        flashcardTermEt = flashcardItemView.findViewById(R.id.addFlashcard_termEt);
        flashcardDefinitionEt = flashcardItemView.findViewById(R.id.addFlashcard_definitionEt);
        flashcardTranslationEt = flashcardItemView.findViewById(R.id.addFlashcard_translationEt);

        flashcardTermEt.setText(term);
        flashcardDefinitionEt.setText(definition);
        flashcardTranslationEt.setText(translation);

        linearLayout.addView(flashcardItemView);
        // todo naprawic
        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
        updateFlashcardCounterText();
    }

    private void updateFlashcardCounterText() {
        flashCardCounterTv.setText(getString(R.string.term, String.valueOf(termCounter)));
    }

    private void populateFlashcardsUI() {
        for (ModelFlashcard flashcard : flashcards) {
            addFlashcard(flashcardsLl, getLayoutInflater(), flashcard.getTerm(), flashcard.getDefinition(), flashcard.getTranslation());
        }
    }

    private void handleSetCreationWithRestart(SetCreateContainer newSetContainer, int attemptCount) {
        Utils.disableItems(toolbarBackIv, toolbarSaveIv, setNameEt,
                addDescriptionTv, setDescriptionEt, flashcardTermEt, flashcardDefinitionEt,
                flashcardTranslationEt, addFlashcardFab);
        userService.createUserSet(newSetContainer, new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                Utils.enableItems(toolbarSaveIv);

                Long setId = ResponseHandler.handleResponse(response);
                Intent intent = new Intent(getApplicationContext(), ActivityViewLearningSet.class);

                intent.putExtra("learningSet", newLearningSet);
                intent.putExtra("setId", setId);

                startActivity(intent);

                close();
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                if (attemptCount < MAX_RETRY_ATTEMPTS) {
                    System.out.println(attemptCount);
                    // Retry the network call
                    handleSetCreationWithRestart(newSetContainer, attemptCount + 1);
                } else {
                    Utils.enableItems(toolbarBackIv, toolbarSaveIv, setNameEt,
                            addDescriptionTv, setDescriptionEt, flashcardTermEt,
                            flashcardDefinitionEt, flashcardTranslationEt, addFlashcardFab);
                    Utils.showToast(getApplicationContext(), "Set was not created due to a network error");
                }
            }
        });
    }

    private void handleSetCreation(SetCreateContainer newSetContainer) {
        int attemptCount = MAX_RETRY_ATTEMPTS;
        handleSetCreationWithRestart(newSetContainer, attemptCount);
    }

    private void handleSetInGroupCreationWithRestart(SetCreateContainer newSetContainer, int attemptCount) {
        Utils.disableItems(toolbarBackIv, toolbarSaveIv, setNameEt,
                addDescriptionTv, setDescriptionEt, flashcardTermEt, flashcardDefinitionEt,
                flashcardTranslationEt, addFlashcardFab);
        groupService.createSet(groupId, newSetContainer, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Utils.enableItems(toolbarSaveIv);

                Intent intent = new Intent(getApplicationContext(), ActivityViewGroup.class);

                intent.putExtra("groupId", groupId);
                intent.putExtra("groupName", groupName);

                startActivity(intent);

                close();

                Utils.showToast(getApplicationContext(), "Set created");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                if (attemptCount < MAX_RETRY_ATTEMPTS) {
                    System.out.println(attemptCount);
                    // Retry the network call
                    handleSetCreationWithRestart(newSetContainer, attemptCount + 1);
                } else {
                    Utils.enableItems(toolbarBackIv, toolbarSaveIv, setNameEt,
                            addDescriptionTv, setDescriptionEt, flashcardTermEt, flashcardDefinitionEt,
                            flashcardTranslationEt, addFlashcardFab);
                    Utils.showToast(getApplicationContext(), "Set was not created due to a network error");
                }
            }
        });
    }

    private void handleSetInGroupCreation(SetCreateContainer newSetContainer) {
        int attemptCount = MAX_RETRY_ATTEMPTS;
        handleSetInGroupCreationWithRestart(newSetContainer, attemptCount);
    }

    private void close() {
        this.finish();
    }
}