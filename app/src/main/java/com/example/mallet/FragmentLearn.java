package com.example.mallet;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
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

public class FragmentLearn extends Fragment {
    private ActivityLearn activityLearn;
    private static final String PREFS_NAME = "FragmentLearnSettings";
    private static final String KEY_MULTIPLE_CHOICE = "multipleChoice";
    private static final String KEY_WRITTEN = "written";
    private static final int MAX_QUESTIONS = 20;
    private FragmentLearnBinding binding;
    private List<ModelFlashcard> flashcardList;
    private int WRITTEN_QUESTIONS, MULTIPLE_CHOICE_QUESTIONS;
    private MaterialSwitch multipleChoiceMs;
    private MaterialSwitch writtenMs;
    private int checkedSwitches;
    private LinearLayout questionsLl, answersLl;
    private View writtenQuestionView;
    private TextView writtenQuestionTv;
    private TextInputEditText writtenAnswerEt;
    private String writtenAnswer;
    private List<ModelWritten> writtenQuestions;
    private int writtenCorrectAnswerPosition, writtenAlternativeAnswerPosition;
    private TextView correctAnswersTv;
    private View multipleChoiceQuestionView;
    private TextView multipleChoiceQuestionTv;
    private String multipleChoiceAnswer;
    private Button option1Btn, option2Btn, option3Btn, option4Btn;
    private List<ModelMultipleChoice> multipleChoiceQuestions;
    private int multipleChoiceCorrectAnswerPosition;
    private int multipleChoiceOption1Position;
    private int multipleChoiceOption2Position;
    private int multipleChoiceOption3Position;
    private int multipleChoiceOption4Position;
    private TextView nextTv, prevTv, finishTv, errorTv;
    private int currentQuestionIndex = 0;

    @Override
    public void onAttach(Context context) {
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

        writtenQuestionView = inflater.inflate(R.layout.model_written, container, false);
        multipleChoiceQuestionView = inflater.inflate(R.layout.model_multiple_choice, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        setupContents();

        return binding.getRoot();
    }

    private void setupContents() {
        flashcardList = getLearningSetData();

        currentQuestionIndex = 0;
        setupToolbar();

        learnOptionsDialog();

        questionsLl = binding.learnQuestionsLl;

        nextTv = binding.learnNextTv;
        nextTv.setOnClickListener(v -> handleNextTvClick());

        prevTv = binding.learnPrevTv;
        Utils.showItems(prevTv, nextTv);

        finishTv = binding.learnFinishTv;
        finishTv.setOnClickListener(v -> learningFinishedDialog());
        Utils.hideItems(finishTv, errorTv);

        writtenQuestionTv = writtenQuestionView.findViewById(R.id.written_questionTv);
        writtenAnswerEt = writtenQuestionView.findViewById(R.id.written_answerEt);

        multipleChoiceQuestionTv = multipleChoiceQuestionView.findViewById(R.id.multipleChoice_questionTv);

        option1Btn = multipleChoiceQuestionView.findViewById(R.id.multipleChoice_answerABtn);
        option2Btn = multipleChoiceQuestionView.findViewById(R.id.multipleChoice_answerBBtn);
        option3Btn = multipleChoiceQuestionView.findViewById(R.id.multipleChoice_answerCBtn);
        option4Btn = multipleChoiceQuestionView.findViewById(R.id.multipleChoice_answerDBtn);
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
        multipleChoiceMs.setOnCheckedChangeListener((buttonView, isChecked) -> handleSwitchChange(multipleChoiceMs, KEY_MULTIPLE_CHOICE, isChecked));

        writtenMs = dialogBinding.learnOptionsWrittenMs;
        writtenMs.setChecked(Utils.getSwitchState(requireContext(), PREFS_NAME, KEY_WRITTEN));
        writtenMs.setOnCheckedChangeListener((buttonView, isChecked) -> handleSwitchChange(writtenMs, KEY_WRITTEN, isChecked));

        TextView restartTv = dialogBinding.learnOptionsRestartTv;
        TextView startTv = dialogBinding.learnOptionsStartTv;
        errorTv = dialogBinding.learnOptionsErrorTv;

        restartTv.setOnClickListener(v -> {
            currentQuestionIndex = 0;
        });

        startTv.setOnClickListener(v -> {
            currentQuestionIndex = 0;

            if (writtenMs.isChecked() && !multipleChoiceMs.isChecked()) {
                writtenQuestions = activityLearn.generateWrittenQuestions();
                WRITTEN_QUESTIONS = MAX_QUESTIONS;
                displayWrittenQuestions(writtenQuestions, questionsLl, getLayoutInflater());
            } else if (!writtenMs.isChecked() && multipleChoiceMs.isChecked()) {
                if (activityLearn != null) {
                    multipleChoiceQuestions = activityLearn.generateMultipleChoiceQuestions();
                    MULTIPLE_CHOICE_QUESTIONS = MAX_QUESTIONS;
                    displayMultipleChoiceQuestion(multipleChoiceQuestions, questionsLl, getLayoutInflater());
                }
            } else if (!writtenMs.isChecked() && !multipleChoiceMs.isChecked()) {
                Utils.showItems(errorTv);
            } else if (writtenMs.isChecked() && multipleChoiceMs.isChecked()) {
                if (activityLearn != null) {
                    writtenQuestions = activityLearn.generateWrittenQuestions();
                    multipleChoiceQuestions = activityLearn.generateMultipleChoiceQuestions();
                    displayMixedQuestions(writtenQuestions, multipleChoiceQuestions, questionsLl, getLayoutInflater());
                }


                MULTIPLE_CHOICE_QUESTIONS = 10;
                WRITTEN_QUESTIONS = 10;
                Utils.showToast(getContext(), "NOPE");
            }

            dialog.dismiss();
        });
    }

    private void handleSwitchChange(MaterialSwitch switchView, String key, boolean isChecked) {
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


    private void handleNextTvClick() {
        if (!multipleChoiceMs.isChecked()) {
            ModelWritten writtenQuestion = writtenQuestions.get(currentQuestionIndex);
            writtenAnswer = writtenAnswerEt.getText().toString().toLowerCase().trim();
            String writtenCorrectAnswer = writtenQuestion.getCorrectAnswer();
            String writtenAlternativeAnswer = writtenQuestion.getAlternativeAnswer();

            boolean isCorrect = checkWrittenAnswer(writtenAnswer, writtenCorrectAnswer, writtenAlternativeAnswer);

            if (isCorrect) {
                writtenAnswerEt.setText("");
                currentQuestionIndex++;

                if (currentQuestionIndex < MAX_QUESTIONS && currentQuestionIndex < writtenQuestions.size()) {
                    displayWrittenQuestions(writtenQuestions, questionsLl, getLayoutInflater());
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
            int multipleChoiceCorrectAnswerPosition = multipleChoiceQuestion.getAnswerPosition();
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
        } else if(writtenMs.isChecked()&&multipleChoiceMs.isChecked()){
            if (currentQuestionIndex < MAX_QUESTIONS) {
                if (currentQuestionIndex % 2 == 0) {
                    // Display a written question
                    if (currentQuestionIndex < writtenQuestions.size()) {
                        displayWrittenQuestions(writtenQuestions, questionsLl, getLayoutInflater());
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
            displayWrittenQuestions(writtenQuestions, questionsLl, getLayoutInflater());
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

    private boolean checkMultipleChoiceAnswer(String userAnswer, String correctAnswer, String
            alternativeAnswer) {
        String userInputLower = userAnswer.toLowerCase();
        String correctAnswerLower = correctAnswer.toLowerCase();
        String alternativeAnswerLower = alternativeAnswer.toLowerCase();
        return userInputLower.equals(correctAnswerLower) || userInputLower.equals(alternativeAnswerLower);
    }


    private void displayWrittenQuestions(List<ModelWritten> wQuestions, LinearLayout
            ll, LayoutInflater inflater) {
        ll.removeAllViews();

        if (currentQuestionIndex < wQuestions.size()) {
            ModelWritten writtenQuestion = wQuestions.get(currentQuestionIndex);

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
                    writtenAnswer = writtenAnswerEt.getText().toString();
                }
            });

            ll.addView(writtenQuestionItem);
            // System.out.println((currentQuestionIndex));
        } else {
            // All questions have been shown
        }
    }

    private void displayMultipleChoiceQuestion
            (List<ModelMultipleChoice> mcqQuestions, LinearLayout ll, LayoutInflater inflater) {
        ll.removeAllViews();

        if (currentQuestionIndex < mcqQuestions.size()) {
            ModelMultipleChoice multipleChoiceQuestion = mcqQuestions.get(currentQuestionIndex);

            View multipleChoiceQuestionItem = inflater.inflate(R.layout.model_multiple_choice, ll, false);
            multipleChoiceQuestionTv = multipleChoiceQuestionItem.findViewById(R.id.multipleChoice_questionTv);
            multipleChoiceQuestionTv.setText(multipleChoiceQuestion.getQuestion());

            ll.addView(multipleChoiceQuestionItem);
        } else {
            // All questions have been shown
        }
    }

    private void displayMixedQuestions(List<ModelWritten> writtenQuestions, List<ModelMultipleChoice> multipleChoiceQuestions, LinearLayout ll, LayoutInflater inflater) {
        ll.removeAllViews();

        int maxQuestionsPerType = MAX_QUESTIONS / 2;
        int writtenQuestionsCount = Math.min(maxQuestionsPerType, writtenQuestions.size());
        int multipleChoiceQuestionsCount = Math.min(maxQuestionsPerType, multipleChoiceQuestions.size());

        int totalQuestionsDisplayed = 0;
        int writtenQuestionIndex = 0;
        int multipleChoiceQuestionIndex = 0;

        while (totalQuestionsDisplayed < MAX_QUESTIONS) {
            if (totalQuestionsDisplayed < maxQuestionsPerType && writtenQuestionIndex < writtenQuestionsCount) {
                ModelWritten writtenQuestion = writtenQuestions.get(writtenQuestionIndex);
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
                        writtenAnswer = writtenAnswerEt.getText().toString();
                    }
                });

                ll.addView(writtenQuestionItem);

                writtenQuestionIndex++;
            } else if (multipleChoiceQuestionIndex < multipleChoiceQuestionsCount) {
                ModelMultipleChoice multipleChoiceQuestion = multipleChoiceQuestions.get(multipleChoiceQuestionIndex);
                View multipleChoiceQuestionItem = inflater.inflate(R.layout.model_multiple_choice, ll, false);
                multipleChoiceQuestionTv = multipleChoiceQuestionItem.findViewById(R.id.multipleChoice_questionTv);
                multipleChoiceQuestionTv.setText(multipleChoiceQuestion.getQuestion());

                ll.addView(multipleChoiceQuestionItem);

                multipleChoiceQuestionIndex++;
            } else {
                // All questions have been shown
                break;
            }

            totalQuestionsDisplayed++;
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
            writtenQuestions = activityLearn.generateWrittenQuestions();
            multipleChoiceQuestions = activityLearn.generateMultipleChoiceQuestions();
            Utils.hideItems(finishTv);
            Utils.makeItemsUnclickable(finishTv);
            Utils.showItems(nextTv);
            Utils.makeItemsClickable(nextTv);
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