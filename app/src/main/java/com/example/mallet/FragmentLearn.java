package com.example.mallet;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.mallet.databinding.DialogLearnOptionsBinding;
import com.example.mallet.databinding.DialogLearningFinishedBinding;
import com.example.mallet.databinding.FragmentLearnBinding;
import com.example.mallet.utils.ModelFlashcard;
import com.example.mallet.utils.ModelWritten;
import com.example.mallet.utils.Utils;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class FragmentLearn extends Fragment {
    private FragmentLearnBinding binding;
    private int checkedSwitches;
    private LinearLayout questionsLl;
    private MaterialSwitch multipleChoiceMs, writtenMs;
    private static final String KEY_MULTIPLE_CHOICE = "multipleChoice";
    private static final String KEY_WRITTEN = "written";
    private static final String PREFS_NAME = "FragmentLearnSettings";
    private View writtenQuestionView;
    private TextView writtenQuestionTv;
    private TextInputEditText writtenAnswerEt;
    private String answer;
    private List<ModelWritten> writtenQuestions;
    private TextView nextTv, prevTv, finishTv;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLearnBinding.inflate(inflater, container, false);
        writtenQuestionView = inflater.inflate(R.layout.model_written, container, false);

        setupContents();

        learnOptionsDialog();

        writtenQuestions = handleWrittenQuestions();
        currentQuestionIndex = 0;
        Utils.showItem(nextTv);
        Utils.hideItem(finishTv);

        nextTv.setOnClickListener(v -> {
            if (currentQuestionIndex < writtenQuestions.size()) {
                displayWrittenQuestion(writtenQuestions, questionsLl, inflater);
                currentQuestionIndex++;
            } else {
                learningFinishedDialog();
            }

            if (currentQuestionIndex == writtenQuestions.size()) {
                Utils.showItem(finishTv);
                Utils.makeItemsClickable(finishTv);
                Utils.hideItem(nextTv);
                Utils.makeItemsUnclickable(nextTv);
            } else {
            }
            System.out.println(currentQuestionIndex);
        });

        finishTv.setOnClickListener(v -> {
            learningFinishedDialog();
        });

        System.out.println(currentQuestionIndex);
        return binding.getRoot();
    }


    private void setupContents() {
        setupToolbar();
        questionsLl = binding.learnQuestionLl;

        nextTv = binding.learnNextTv;
        prevTv = binding.learnPrevTv;
        finishTv = binding.learnFinishTv;

        writtenQuestionTv = writtenQuestionView.findViewById(R.id.written_questionTv);
        writtenAnswerEt = writtenQuestionView.findViewById(R.id.written_answerEt);
        answer = writtenAnswerEt.getText().toString();


    }

    private void setupToolbar() {
        Toolbar toolbar = binding.learnToolbar;
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(""); // Set the title to an empty string

        binding.learnToolbarBackIv.setOnClickListener(v -> getActivity().finish());
        binding.learnOptionsIv.setOnClickListener(v -> learnOptionsDialog());
    }

    private void learnOptionsDialog() {
        Dialog dialog = createDialog(R.layout.dialog_forgot_password);
        DialogLearnOptionsBinding dialogBinding = DialogLearnOptionsBinding.inflate(getLayoutInflater());
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setContentView(dialogBinding.getRoot());
        dialog.show();

        multipleChoiceMs = dialogBinding.learnOptionsMultipleChoiceMs;
        writtenMs = dialogBinding.learnOptionsWrittenMs;
        TextView restartTv = dialogBinding.learnOptionsRestartTv;
        TextView startTv = dialogBinding.learnOptionsStartTv;

        multipleChoiceMs.setChecked(Utils.getSwitchState(requireContext(), PREFS_NAME, KEY_MULTIPLE_CHOICE));
        multipleChoiceMs.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (checkedSwitches < 2) {
                    checkedSwitches++;
                }
                System.out.println(checkedSwitches);
            } else {
                if (checkedSwitches > 0) {
                    checkedSwitches--;
                }
                System.out.println(checkedSwitches);
            }
            Utils.saveSwitchState(requireContext(), PREFS_NAME, KEY_MULTIPLE_CHOICE, isChecked);
        });

        writtenMs.setChecked(Utils.getSwitchState(requireContext(), PREFS_NAME, KEY_WRITTEN));
        writtenMs.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (checkedSwitches < 2) {
                    checkedSwitches++;
                }
                System.out.println(checkedSwitches);
            } else {
                if (checkedSwitches > 0) {
                    checkedSwitches--;
                }
                System.out.println(checkedSwitches);
            }
            Utils.saveSwitchState(requireContext(), PREFS_NAME, KEY_WRITTEN, isChecked);
        });

        restartTv.setOnClickListener(v -> {
            currentQuestionIndex = 0;
            writtenQuestions = handleWrittenQuestions();
            dialog.dismiss();
        });

        startTv.setOnClickListener(v -> {
            displayWrittenQuestion(writtenQuestions, questionsLl, getLayoutInflater());
            currentQuestionIndex = 1;
            dialog.dismiss();
        });
    }

    private int currentQuestionIndex = 0; // Initialize the index

    private void displayWrittenQuestion(List<ModelWritten> questions, LinearLayout ll, LayoutInflater inflater) {
        ll.removeAllViews();

        //if (currentQuestionIndex < questions.size()) {
        ModelWritten question = questions.get(currentQuestionIndex);

        View questionItem = inflater.inflate(R.layout.model_written, ll, false);
        TextView writtenQuestionTv = questionItem.findViewById(R.id.written_questionTv);
        writtenQuestionTv.setText(question.getQuestion());

        ll.addView(questionItem);
        //} else {
        //    learningFinishedDialog();
        //}
    }

    private void learningFinishedDialog() {
        Dialog dialog = createDialog(R.layout.dialog_learning_finished);
        DialogLearningFinishedBinding dialogBinding = DialogLearningFinishedBinding.inflate(getLayoutInflater());
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setContentView(dialogBinding.getRoot());
        dialog.show();

        TextView dialogFinishTv = dialogBinding.learningFinishedFinishTv;
        TextView dialogRestartTv = dialogBinding.learningFinishedRestartTv;

        dialogFinishTv.setOnClickListener(v -> requireActivity().finish());
        dialogRestartTv.setOnClickListener(v -> {
            currentQuestionIndex = 0;
            writtenQuestions = handleWrittenQuestions();
            Utils.hideItem(finishTv);
            Utils.makeItemsUnclickable(finishTv);
            Utils.showItem(nextTv);
            Utils.makeItemsClickable(nextTv);
            dialog.dismiss();
        });
    }

    private Dialog createDialog(int layoutResId) {
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layoutResId);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        return dialog;
    }

    private List<ModelWritten> handleWrittenQuestions() {
        List<ModelFlashcard> flashcardList = new ArrayList<>();
        flashcardList.add(new ModelFlashcard("Dog", "A domesticated mammal", "Pies"));
        flashcardList.add(new ModelFlashcard("Cat", "A small domesticated carnivorous mammal", "Kot"));
        flashcardList.add(new ModelFlashcard("Elephant", "A large, herbivorous mammal with a trunk", "Słoń"));
        flashcardList.add(new ModelFlashcard("Lion", "A large wild cat known for its mane", "Lew"));
        flashcardList.add(new ModelFlashcard("Giraffe", "A tall, long-necked African mammal", "Żyrafa"));
        flashcardList.add(new ModelFlashcard("Snail", "A shelled gastropod", "Ślimak"));

        List<ModelWritten> questionList = new ArrayList();
        Random random = new Random();

        int questionPosition = random.nextInt(2);

        for (ModelFlashcard flashcard : flashcardList) {
            List<String> options = new ArrayList<>();
            options.add(flashcard.getTerm());
            options.add(flashcard.getDefinition());
            options.add(flashcard.getTranslation());

            Collections.shuffle(options);

            int correctAnswerPosition = 1, alternativeAnswerPosition = 2;

            switch (questionPosition) {
                case 0:
                    correctAnswerPosition = 1;
                    alternativeAnswerPosition = 2;
                    break;
                case 1:
                    correctAnswerPosition = 0;
                    alternativeAnswerPosition = 2;
                    break;
                case 2:
                    correctAnswerPosition = 1;
                    alternativeAnswerPosition = 0;
                    break;
            }

            ModelWritten written = new ModelWritten(options.get(questionPosition), options.get(correctAnswerPosition), options.get(alternativeAnswerPosition));

            questionList.add(written);

            System.out.println(written);
        }

        return questionList;
    }
}