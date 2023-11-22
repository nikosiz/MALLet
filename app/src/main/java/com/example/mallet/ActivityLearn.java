package com.example.mallet;

import static com.example.mallet.MALLet.MIN_FLASHCARDS_FOR_TEST;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.mallet.databinding.ActivityLearnBinding;
import com.example.mallet.databinding.DialogConfirmExitBinding;
import com.example.mallet.utils.ModelFlashcard;
import com.example.mallet.utils.ModelLearningSet;
import com.example.mallet.utils.ModelMultipleChoice;
import com.example.mallet.utils.ModelTrueFalse;
import com.example.mallet.utils.ModelWritten;
import com.example.mallet.utils.Utils;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class ActivityLearn extends AppCompatActivity {
    List<List<String>> flashcardTable;
    private ActivityLearnBinding binding;
    private String fragmentName;
    private ModelLearningSet learningSet;
    static List<ModelFlashcard> flashcardList;
    private Random random;
    private int writtenCorrectAnswerPosition, writtenAlternativeAnswerPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLearnBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                confirmExitDialog();
            }
        };

        // Register the callback with the onBackPressedDispatcher
        this.getOnBackPressedDispatcher().addCallback(this, callback);

        this.random = new Random();
        setupContents();

        if (fragmentName != null) {
            try {
                Class<?> fragmentClass = Class.forName(fragmentName);
                Fragment fragment = (Fragment) fragmentClass.newInstance();

                Bundle args = new Bundle();
                args.putParcelable("learningSet", learningSet);
                fragment.setArguments(args);

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.learn_mainLl, fragment)
                        .commitNow();
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    private void setupContents() {
        fragmentName = getIntent().getStringExtra("fragment_class");

        learningSet = getIntent().getParcelableExtra("learningSet");

        if (learningSet != null) {
            flashcardList = learningSet.getTerms();
        }

        numberOfQuestions();
    }

    private static int MAX_PER_TYPE;

    public static List<ModelMultipleChoice> generateMultipleChoiceQuestions() {
        return QuestionProvider.generateMultipleChoiceQuestions(MAX_PER_TYPE, flashcardList);
    }

    private void numberOfQuestions() {
        if (flashcardList.size() < MIN_FLASHCARDS_FOR_TEST) {
            //Utils.showToast(getApplicationContext(), "There are not enough flashcards to generate questions");
            MAX_PER_TYPE = 10;
        } else {
            MAX_PER_TYPE = Math.min(20, flashcardList.size());
        }
    }

    public List<ModelWritten> generateWrittenQuestions() {
        return QuestionProvider.generateWrittenQuestions(MAX_PER_TYPE, flashcardList);
    }

    public List<ModelTrueFalse> generateTrueFalseQuestions() {
        return QuestionProvider.generateTrueFalseQuestions(MAX_PER_TYPE, flashcardList);
    }

    public void confirmExitDialog() {
        Dialog dialog = Utils.createDialog(this, R.layout.dialog_confirm_exit, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT), Gravity.BOTTOM);
        DialogConfirmExitBinding dialogBinding = DialogConfirmExitBinding.inflate(LayoutInflater.from(this));
        Objects.requireNonNull(dialog).setContentView(dialogBinding.getRoot());
        dialog.show();

        dialogBinding.confirmExitCancelTv.setOnClickListener(v -> dialog.dismiss());
        dialogBinding.confirmExitConfirmTv.setOnClickListener(v -> {
            this.finish();
            dialog.dismiss();
        });
    }
}