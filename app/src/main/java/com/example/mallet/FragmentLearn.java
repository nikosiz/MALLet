package com.example.mallet;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.mallet.databinding.DialogLearnOptionsBinding;
import com.example.mallet.databinding.DialogLearningFinishedBinding;
import com.example.mallet.databinding.FragmentLearnBinding;
import com.example.mallet.utils.ModelAnswer;
import com.example.mallet.utils.ModelFlashcard;
import com.example.mallet.utils.ModelLearningSet;
import com.example.mallet.utils.ModelMultipleChoice;
import com.example.mallet.utils.ModelWritten;
import com.example.mallet.utils.Utils;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FragmentLearn extends Fragment {
    private ActivityLearn activityLearn;
    private static final String PREFS_NAME = "FragmentLearnSettings";
    private static final String KEY_MULTIPLE_CHOICE = "multipleChoice";
    private static final String KEY_WRITTEN = "written";
    private static int MAX_QUESTIONS = 20;
    private FragmentLearnBinding binding;
    private List<ModelFlashcard> flashcardList;
    private MaterialSwitch multipleChoiceMs;
    private MaterialSwitch writtenMs;
    private int checkedSwitches;
    private LinearLayout questionsLl, answersLl;
    private TextView writtenQuestionTv;
    private TextInputEditText writtenAnswerEt;
    private String writtenAnswer;
    private List<ModelWritten> writtenQuestions;
    private TextView correctAnswersTv;
    private TextView multipleChoiceQuestionTv;
    private List<ModelMultipleChoice> multipleChoiceQuestions;
    private TextView nextTv, prevTv, finishTv, errorTv;
    private int currentQuestionIndex = 0;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof ActivityLearn) {
            activityLearn = (ActivityLearn) context;
        } else {
            // Handle the case where the activity does not implement the method
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLearnBinding.inflate(inflater, container, false);

        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        setupContents();

        return binding.getRoot();
    }

    private void setupContents() {
        flashcardList = getLearningSetData();

        currentQuestionIndex = 0;

        MAX_QUESTIONS = Math.min(24, flashcardList.size());

        setupToolbar();

        learnOptionsDialog();

        questionsLl = binding.learnQuestionsLl;

        nextTv = binding.learnNextTv;
        nextTv.setOnClickListener(v -> nextQuestion());

        finishTv = binding.learnFinishTv;
        finishTv.setOnClickListener(v -> learningFinishedDialog());
        Utils.hideItems(finishTv, errorTv);
    }

    private void setupToolbar() {
        Toolbar toolbar = binding.learnToolbar;
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("");

        binding.learnToolbarBackIv.setOnClickListener(v -> activityLearn.confirmExitDialog());

        binding.learnOptionsIv.setOnClickListener(v -> learnOptionsDialog());
    }

    private void learnOptionsDialog() {
        Dialog dialog = Utils.createDialog(requireContext(), R.layout.dialog_forgot_password, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT), Gravity.BOTTOM);
        DialogLearnOptionsBinding dialogBinding = DialogLearnOptionsBinding.inflate(getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());
        dialog.show();

        multipleChoiceMs = dialogBinding.learnOptionsMultipleChoiceMs;
        multipleChoiceMs.setChecked(Utils.getSwitchState(requireContext(), PREFS_NAME, KEY_MULTIPLE_CHOICE));
        multipleChoiceMs.setOnCheckedChangeListener((buttonView, isChecked) -> handleSwitchChange(KEY_MULTIPLE_CHOICE, isChecked));

        writtenMs = dialogBinding.learnOptionsWrittenMs;
        writtenMs.setChecked(Utils.getSwitchState(requireContext(), PREFS_NAME, KEY_WRITTEN));
        writtenMs.setOnCheckedChangeListener((buttonView, isChecked) -> handleSwitchChange(KEY_WRITTEN, isChecked));

        TextView restartTv = dialogBinding.learnOptionsRestartTv;
        TextView startTv = dialogBinding.learnOptionsStartTv;
        errorTv = dialogBinding.learnOptionsErrorTv;

        restartTv.setOnClickListener(v -> currentQuestionIndex = 0);

        startTv.setOnClickListener(v -> {
            currentQuestionIndex = 0;

            if (writtenMs.isChecked() && !multipleChoiceMs.isChecked()) {
                WRITTEN_QUESTIONS = MAX_QUESTIONS;
                writtenQuestions = activityLearn.generateWrittenQuestions();
                displayWrittenQuestion(writtenQuestions, questionsLl, getLayoutInflater());

            } else if (!writtenMs.isChecked() && multipleChoiceMs.isChecked()) {
                MULTIPLE_CHOICE_QUESTIONS = MAX_QUESTIONS;
                multipleChoiceQuestions = activityLearn.generateMultipleChoiceQuestions();
                displayMultipleChoiceQuestion(multipleChoiceQuestions, questionsLl, getLayoutInflater());

            } else if (!writtenMs.isChecked() && !multipleChoiceMs.isChecked()) {
                WRITTEN_QUESTIONS = 0;
                MULTIPLE_CHOICE_QUESTIONS = 0;
                Utils.showItems(errorTv);
            }

            dialog.dismiss();
        });
    }

    private void handleSwitchChange(String key, boolean isChecked) {
        if (isChecked) {
            if (checkedSwitches < 2) {
                checkedSwitches++;
            }
        } else {
            if (checkedSwitches > 0) {
                checkedSwitches--;
            }
        }
        Utils.saveSwitchState(requireContext(), PREFS_NAME, key, isChecked);
        handleSwitchState();
    }

    private void handleSwitchState() {
        if (!writtenMs.isChecked() && !multipleChoiceMs.isChecked()) {
            Utils.showItems(errorTv);
            Utils.makeItemsUnclickable(finishTv);
        } else {
            Utils.hideItems(errorTv);
        }
    }

    private int WRITTEN_QUESTIONS, MULTIPLE_CHOICE_QUESTIONS;

    private void nextQuestion() {
        if (!multipleChoiceMs.isChecked()) {
            ModelWritten writtenQuestion = writtenQuestions.get(currentQuestionIndex);
            writtenAnswer = Objects.requireNonNull(writtenAnswerEt.getText()).toString().toLowerCase().trim();
            String writtenCorrectAnswer = writtenQuestion.getCorrectAnswer();
            String writtenAlternativeAnswer = writtenQuestion.getAlternativeAnswer();

            boolean isWrittenCorrect = checkWrittenAnswer(writtenAnswer, writtenCorrectAnswer, writtenAlternativeAnswer);

            if (isWrittenCorrect) {
                writtenAnswerEt.setText("");
                currentQuestionIndex++;

                if (currentQuestionIndex < MAX_QUESTIONS && currentQuestionIndex < writtenQuestions.size()) {
                    displayWrittenQuestion(writtenQuestions, questionsLl, getLayoutInflater());
                } else {
                    Utils.showItems(finishTv);
                    Utils.makeItemsClickable(finishTv);
                    Utils.hideItems(nextTv);
                    Utils.makeItemsUnclickable(nextTv);
                    learningFinishedDialog();
                }
            } else {
                Utils.showItems(answersLl);
            }
        } else if (!writtenMs.isChecked()) {
            ModelMultipleChoice multipleChoiceQuestion = multipleChoiceQuestions.get(currentQuestionIndex);

            currentQuestionIndex++;

            if (currentQuestionIndex < MAX_QUESTIONS && currentQuestionIndex < multipleChoiceQuestions.size()) {
                displayMultipleChoiceQuestion(multipleChoiceQuestions, questionsLl, getLayoutInflater());
            } else {
                Utils.showItems(finishTv);
                Utils.makeItemsClickable(finishTv);
                Utils.hideItems(nextTv);
                Utils.makeItemsUnclickable(nextTv);
                learningFinishedDialog();
            }
        } else if (writtenMs.isChecked() && multipleChoiceMs.isChecked()) {
            if (currentQuestionIndex < MAX_QUESTIONS) {
                if (currentQuestionIndex % 2 == 0) {
                    // Display a written question
                    if (currentQuestionIndex < writtenQuestions.size()) {
                        displayWrittenQuestion(writtenQuestions, questionsLl, getLayoutInflater());
                    } else {
                        // No more written questions
                        displayNextMultipleChoiceQuestion();
                    }
                } else {
                    // Display a multiple-choice question
                    if (currentQuestionIndex < multipleChoiceQuestions.size()) {
                        displayMultipleChoiceQuestion(multipleChoiceQuestions, questionsLl, getLayoutInflater());
                    } else {
                        // No more multiple-choice questions
                        displayNextWrittenQuestion();
                    }
                }

                currentQuestionIndex++;
            } else {
                // Reached the maximum number of questions
                Utils.showItems(finishTv);
                Utils.makeItemsClickable(finishTv);
                Utils.hideItems(nextTv);
                Utils.makeItemsUnclickable(nextTv);
                learningFinishedDialog();
            }
        }
    }

    private void displayNextWrittenQuestion() {
        if (currentQuestionIndex < writtenQuestions.size()) {
            displayWrittenQuestion(writtenQuestions, questionsLl, getLayoutInflater());
        } else {
            // No more written questions
            displayNextMultipleChoiceQuestion();
        }
    }

    private void displayNextMultipleChoiceQuestion() {
        if (currentQuestionIndex < multipleChoiceQuestions.size()) {
            displayMultipleChoiceQuestion(multipleChoiceQuestions, questionsLl, getLayoutInflater());
        } else {
            // No more multiple-choice questions
            displayNextWrittenQuestion();
        }
    }


    private boolean checkWrittenAnswer(String userAnswer, String correctAnswer, String
            alternativeAnswer) {
        String userInputLower = userAnswer.toLowerCase();
        String correctAnswerLower = correctAnswer.toLowerCase();
        String alternativeAnswerLower = alternativeAnswer.toLowerCase();
        return userInputLower.equals(correctAnswerLower) || userInputLower.equals(alternativeAnswerLower);
    }

    private boolean checkMultipleChoiceAnswer(int clickedOption, int correctAnswerPosition) {
        boolean isCorrect = clickedOption == correctAnswerPosition;
        Utils.showToast(getContext(), String.valueOf(isCorrect));
        return clickedOption == correctAnswerPosition;
    }


    private void displayWrittenQuestion(List<ModelWritten> questions, LinearLayout
            ll, LayoutInflater inflater) {
        ll.removeAllViews();

        if (currentQuestionIndex < questions.size()) {
            ModelWritten writtenQuestion = questions.get(currentQuestionIndex);

            View writtenQuestionItem = inflater.inflate(R.layout.model_written, ll, false);
            writtenQuestionTv = writtenQuestionItem.findViewById(R.id.written_questionTv);
            writtenQuestionTv.setText(writtenQuestion.getQuestion());

            correctAnswersTv = writtenQuestionItem.findViewById(R.id.written_correctAnswersTv);
            correctAnswersTv.setText("\"" + writtenQuestion.getCorrectAnswer() + "\" or \"" + writtenQuestion.getAlternativeAnswer() + "\"");

            writtenAnswerEt = writtenQuestionItem.findViewById(R.id.written_answerEt);
            answersLl = writtenQuestionItem.findViewById(R.id.written_correctAnswersLl);
            Utils.hideItems(answersLl);

            writtenAnswerEt.setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus) {
                    writtenAnswer = Objects.requireNonNull(writtenAnswerEt.getText()).toString();
                }
            });

            Utils.hideItems(answersLl);

            ll.addView(writtenQuestionItem);
        } else {
            // All questions have been shown
        }
    }

    private void displayMultipleChoiceQuestion
            (List<ModelMultipleChoice> questions, LinearLayout ll, LayoutInflater inflater) {
        ll.removeAllViews();

        if (currentQuestionIndex < questions.size()) {
            ModelMultipleChoice multipleChoiceQuestion = questions.get(currentQuestionIndex);

            View multipleChoiceQuestionItem = inflater.inflate(R.layout.model_multiple_choice, ll, false);
            multipleChoiceQuestionTv = multipleChoiceQuestionItem.findViewById(R.id.multipleChoice_questionTv);

            TextView option1Tv = multipleChoiceQuestionItem.findViewById(R.id.multipleChoice_option1Tv);
            TextView option2Tv = multipleChoiceQuestionItem.findViewById(R.id.multipleChoice_option2Tv);
            TextView option3Tv = multipleChoiceQuestionItem.findViewById(R.id.multipleChoice_option3Tv);
            TextView option4Tv = multipleChoiceQuestionItem.findViewById(R.id.multipleChoice_option4Tv);

            multipleChoiceQuestionTv.setText(multipleChoiceQuestion.getQuestion());

            List<ModelAnswer> answers = multipleChoiceQuestion.getAnswers().stream()
                    .collect(Collectors.toList());
            ModelAnswer correctAnswer = answers.stream()
                    .filter(ModelAnswer::isCorrect)
                    .findAny().get();
            int correctAnswerPosition = answers.indexOf(correctAnswer);

            option1Tv.setText(answers.get(0).getAnswer());
            option2Tv.setText(answers.get(1).getAnswer());
            option3Tv.setText(answers.get(2).getAnswer());
            option4Tv.setText(answers.get(3).getAnswer());

            option1Tv.setOnClickListener(v -> checkMultipleChoiceAnswer(0, correctAnswerPosition));
            option2Tv.setOnClickListener(v -> checkMultipleChoiceAnswer(1, correctAnswerPosition));
            option3Tv.setOnClickListener(v -> checkMultipleChoiceAnswer(2, correctAnswerPosition));
            option4Tv.setOnClickListener(v -> checkMultipleChoiceAnswer(3, correctAnswerPosition));

            ll.addView(multipleChoiceQuestionItem);
        } else {
            // All questions have been shown
        }
    }

    private void learningFinishedDialog() {
        Dialog finishedDialog = Utils.createDialog(requireContext(), R.layout.dialog_learning_finished, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT), Gravity.BOTTOM);
        DialogLearningFinishedBinding dialogBinding = DialogLearningFinishedBinding.inflate(getLayoutInflater());
        finishedDialog.setContentView(dialogBinding.getRoot());

        TextView dialogFinishTv = dialogBinding.learningFinishedFinishTv;
        TextView dialogRestartTv = dialogBinding.learningFinishedRestartTv;

        dialogFinishTv.setOnClickListener(v -> {
            finishedDialog.dismiss();
            requireActivity().finish();
        });

        dialogRestartTv.setOnClickListener(v -> {
            currentQuestionIndex = 0;
            Utils.hideItems(finishTv);
            Utils.disableItems(finishTv);
            Utils.showItems(nextTv);
            Utils.enableItems(nextTv);
            finishedDialog.dismiss();
            learnOptionsDialog();
        });

        finishedDialog.show();
    }

    private List<ModelFlashcard> getLearningSetData() {
        Bundle args = getArguments();
        if (args != null) {
            ModelLearningSet learningSet = args.getParcelable("learningSet");
            if (learningSet != null) {
                flashcardList = learningSet.getTerms();
                return flashcardList;
            }
        }

        return new ArrayList<>();
    }
}