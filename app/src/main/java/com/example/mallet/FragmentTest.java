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

import com.example.mallet.databinding.DialogLearningFinishedBinding;
import com.example.mallet.databinding.DialogTestAreYouReadyBinding;
import com.example.mallet.databinding.FragmentTestBinding;
import com.example.mallet.utils.ModelAnswer;
import com.example.mallet.utils.ModelFlashcard;
import com.example.mallet.utils.ModelMultipleChoice;
import com.example.mallet.utils.ModelTrueFalse;
import com.example.mallet.utils.ModelWritten;
import com.example.mallet.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FragmentTest extends Fragment {
    private ActivityLearn activityLearn;
    private FragmentTestBinding binding;
    private List<ModelFlashcard> flashcards;
    private LinearLayout questionsLl, answersLl;
    private View writtenQuestionView;
    private TextView writtenQuestionTv;
    private TextInputEditText writtenAnswerEt;
    private String writtenAnswer;
    private List<ModelWritten> writtenQuestions;
    private TextView correctAnswersTv;
    private View multipleChoiceQuestionView;
    private TextView multipleChoiceQuestionTv;
    private List<ModelMultipleChoice> multipleChoiceQuestions;
    private List<ModelTrueFalse> trueFalseQuestions;

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
        binding = FragmentTestBinding.inflate(inflater, container, false);

        writtenQuestionView = inflater.inflate(R.layout.model_written, container, false);
        multipleChoiceQuestionView = inflater.inflate(R.layout.model_multiple_choice, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        setupContents();

        return binding.getRoot();
    }

    private void setupContents() {
        setupToolbar();

        testOptionsDialog();

        questionsLl = binding.testQuestionsLl;

        nextTv = binding.testNextTv;
        nextTv.setOnClickListener(v -> nextQuestion());
        Utils.showItems(nextTv);

        finishTv = binding.testFinishTv;
        finishTv.setOnClickListener(v -> learningFinishedDialog());
        Utils.hideItems(finishTv);

        writtenQuestionTv = writtenQuestionView.findViewById(R.id.written_questionTv);
        writtenAnswerEt = writtenQuestionView.findViewById(R.id.written_answerEt);
    }

    private void setupToolbar() {
        Toolbar toolbar = binding.testToolbar;
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("");
        binding.testToolbarBackIv.setOnClickListener(v -> getActivity().finish());
        binding.testOptionsIv.setOnClickListener(v -> testOptionsDialog());
    }

    private void testOptionsDialog() {
        Dialog dialog = Utils.createDialog(requireContext(), R.layout.dialog_test_are_you_ready, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT), Gravity.BOTTOM);
        DialogTestAreYouReadyBinding dialogBinding = DialogTestAreYouReadyBinding.inflate(getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());
        dialog.show();

        TextView startTv = dialogBinding.testReadyStartTv;

        startTv.setOnClickListener(v -> {
            writtenQuestions = activityLearn.generateWrittenQuestions();
            multipleChoiceQuestions = activityLearn.generateMultipleChoiceQuestions();
            trueFalseQuestions = activityLearn.generateTrueFalseQuestions();

            displayWrittenQuestion(writtenQuestions, questionsLl, getLayoutInflater());

            dialog.dismiss();

            currentQuestionIndex = 0;
        });
    }

    private void nextQuestion() {
        if (currentQuestionIndex < 9) {
            writtenAnswer = writtenAnswerEt.getText().toString().toLowerCase().trim();
            ModelWritten writtenQuestion = writtenQuestions.get(currentQuestionIndex);
            String writtenCorrectAnswer = writtenQuestion.getCorrectAnswer();
            String writtenAlternativeAnswer = writtenQuestion.getAlternativeAnswer();

            boolean isCorrect = checkWrittenAnswer(writtenAnswer, writtenCorrectAnswer, writtenAlternativeAnswer);

            if (isCorrect) {
                points++;
                System.out.println("Points: " + points);

                writtenAnswerEt.setText("");
                currentQuestionIndex++;
                System.out.println("Question index: " + currentQuestionIndex);

                if (currentQuestionIndex < writtenQuestions.size()) {
                    displayWrittenQuestion(writtenQuestions, questionsLl, getLayoutInflater());
                } else {
                    Utils.showItems(finishTv);
                    Utils.makeItemsClickable(finishTv);
                    Utils.hideItems(nextTv);
                    Utils.makeItemsUnclickable(nextTv);
                    learningFinishedDialog();
                }
            } else {
                System.out.println("Points: " + points);


                writtenAnswerEt.setText("");
                currentQuestionIndex++;
                System.out.println("Question index: " + currentQuestionIndex);

                if (currentQuestionIndex < writtenQuestions.size()) {
                    displayWrittenQuestion(writtenQuestions, questionsLl, getLayoutInflater());
                } else {
                    Utils.showItems(finishTv);
                    Utils.makeItemsClickable(finishTv);
                    Utils.hideItems(nextTv);
                    Utils.makeItemsUnclickable(nextTv);
                    learningFinishedDialog();
                }
            }
        } else if (currentQuestionIndex < 20) {

            boolean isCorrect = checkMultipleChoiceAnswer(multipleChoiceClickedPosition, correctMultipleChoiceAnswerPosition);

            if (isCorrect) {
                points++;
                System.out.println("Points: " + points);

                currentQuestionIndex++;
                System.out.println("Question index: " + currentQuestionIndex);

                if (currentQuestionIndex - 10 < multipleChoiceQuestions.size()) {
                    displayMultipleChoiceQuestion(multipleChoiceQuestions, questionsLl, getLayoutInflater());
                } else {
                    Utils.showItems(finishTv);
                    Utils.makeItemsClickable(finishTv);
                    Utils.hideItems(nextTv);
                    Utils.makeItemsUnclickable(nextTv);
                    learningFinishedDialog();
                }
            } else {
                System.out.println("Points: " + points);
                currentQuestionIndex++;
                System.out.println("Question index: " + currentQuestionIndex);

                if (currentQuestionIndex - 10 < multipleChoiceQuestions.size()) {
                    displayMultipleChoiceQuestion(multipleChoiceQuestions, questionsLl, getLayoutInflater());
                } else {
                    Utils.showItems(finishTv);
                    Utils.makeItemsClickable(finishTv);
                    Utils.hideItems(nextTv);
                    Utils.makeItemsUnclickable(nextTv);
                    learningFinishedDialog();
                }
            }
        } else if (currentQuestionIndex < 30) {
            boolean isTrueFalseCorrect = checkTrueFalseAnswer(trueFalseClicked, trueFalseCorrectAnswer);

            if (isTrueFalseCorrect) {
                points++;
                System.out.println("Points: " + points);
                currentQuestionIndex++;
                System.out.println("Question index: " + currentQuestionIndex);

                if (currentQuestionIndex - 10 < trueFalseQuestions.size()) {
                    displayTrueFalseQuestion(trueFalseQuestions, questionsLl, getLayoutInflater());
                } else {
                    Utils.showItems(finishTv);
                    Utils.makeItemsClickable(finishTv);
                    Utils.hideItems(nextTv);
                    Utils.makeItemsUnclickable(nextTv);
                    learningFinishedDialog();
                }
            } else {
                currentQuestionIndex++;
                System.out.println("Question index: " + currentQuestionIndex);

                if (currentQuestionIndex - 10 < trueFalseQuestions.size()) {
                    displayTrueFalseQuestion(trueFalseQuestions, questionsLl, getLayoutInflater());
                } else {
                    Utils.showItems(finishTv);
                    Utils.makeItemsClickable(finishTv);
                    Utils.hideItems(nextTv);
                    Utils.makeItemsUnclickable(nextTv);
                    learningFinishedDialog();
                }
            }
        }

    }

    private int points = 0;

    private boolean checkWrittenAnswer(String userAnswer, String correctAnswer, String
            alternativeAnswer) {
        String userInputLower = userAnswer.toLowerCase();
        String correctAnswerLower = correctAnswer.toLowerCase();
        String alternativeAnswerLower = alternativeAnswer.toLowerCase();
        return userInputLower.equals(correctAnswerLower) || userInputLower.equals(alternativeAnswerLower);
    }

    private void displayWrittenQuestion(List<ModelWritten> wQuestions, LinearLayout
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
        } else {
            // All questions have been shown
        }
    }

    private TextView trueTv, falseTv;

    private void displayTrueFalseQuestion(List<ModelTrueFalse> questions, LinearLayout ll, LayoutInflater inflater) {
        ll.removeAllViews();

        //if (currentQuestionIndex < questions.size()) {
        int i = 0;
        ModelTrueFalse trueFalseQuestion = questions.get(i);

        View trueFalseQuestionItem = inflater.inflate(R.layout.model_true_false, ll, false);

        TextView trueFalseQuestionTv = trueFalseQuestionItem.findViewById(R.id.trueFalse_questionTv);
        trueFalseQuestionTv.setText(trueFalseQuestion.getQuestion());

        trueTv = trueFalseQuestionItem.findViewById(R.id.trueFalse_trueTv);
        falseTv = trueFalseQuestionItem.findViewById(R.id.trueFalse_falseTv);

        trueTv.setOnClickListener(v -> {
            trueFalseClicked = 1;
            checkTrueFalseAnswer(trueFalseClicked, trueFalseCorrectAnswer);
        });

        falseTv.setOnClickListener(v -> {
            trueFalseClicked = 0;
            checkTrueFalseAnswer(trueFalseClicked, trueFalseCorrectAnswer);
        });

        ll.addView(trueFalseQuestionItem);
        i++;
        //} else {
        // All questions have been shown
        //}
    }

    private int trueFalseClicked;
    private int trueFalseCorrectAnswer;

    private boolean checkTrueFalseAnswer(int clicked, int correctAnswer) {
        return clicked == correctAnswer;
    }

    private TextView writtenAnswerTv;
    private int correctMultipleChoiceAnswerPosition;
    private TextView option1Tv, option2Tv, option3Tv, option4Tv;

    private void displayMultipleChoiceQuestion
            (List<ModelMultipleChoice> questions, LinearLayout ll, LayoutInflater inflater) {
        ll.removeAllViews();

        //if (currentQuestionIndex < questions.size()) {
        ModelMultipleChoice multipleChoiceQuestion = questions.get(currentQuestionIndex - 10);

        View multipleChoiceQuestionItem = inflater.inflate(R.layout.model_multiple_choice, ll, false);
        multipleChoiceQuestionTv = multipleChoiceQuestionItem.findViewById(R.id.multipleChoice_questionTv);

        option1Tv = multipleChoiceQuestionItem.findViewById(R.id.multipleChoice_option1Tv);
        option2Tv = multipleChoiceQuestionItem.findViewById(R.id.multipleChoice_option2Tv);
        option3Tv = multipleChoiceQuestionItem.findViewById(R.id.multipleChoice_option3Tv);
        option4Tv = multipleChoiceQuestionItem.findViewById(R.id.multipleChoice_option4Tv);

        multipleChoiceQuestionTv.setText(multipleChoiceQuestion.getQuestion());

        List<ModelAnswer> answers = multipleChoiceQuestion.getAnswers().stream()
                .collect(Collectors.toList());
        ModelAnswer correctAnswer = answers.stream()
                .filter(ModelAnswer::isCorrect)
                .findAny().get();

        correctMultipleChoiceAnswerPosition = answers.indexOf(correctAnswer);

        option1Tv.setText(answers.get(0).getAnswer());
        option2Tv.setText(answers.get(1).getAnswer());
        option3Tv.setText(answers.get(2).getAnswer());
        option4Tv.setText(answers.get(3).getAnswer());

        System.out.println(correctMultipleChoiceAnswerPosition);

        option1Tv.setOnClickListener(v -> {
            multipleChoiceClickedPosition = 0;
            Utils.showToast(getContext(), String.valueOf(multipleChoiceClickedPosition));
        });
        option2Tv.setOnClickListener(v -> {
            multipleChoiceClickedPosition = 1;
            Utils.showToast(getContext(), String.valueOf(multipleChoiceClickedPosition));
        });
        option3Tv.setOnClickListener(v -> {
            multipleChoiceClickedPosition = 2;
            Utils.showToast(getContext(), String.valueOf(multipleChoiceClickedPosition));
        });
        option4Tv.setOnClickListener(v -> {
            multipleChoiceClickedPosition = 3;
            Utils.showToast(getContext(), String.valueOf(multipleChoiceClickedPosition));
        });


        ll.addView(multipleChoiceQuestionItem);
        //} else{
        // All questions have been shown
        //}
    }

    private int multipleChoiceClickedPosition = 4;

    private boolean checkMultipleChoiceAnswer(int clickedOption, int correctAnswerPosition) {
        return clickedOption == correctAnswerPosition;
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
            Utils.hideItems(finishTv);
            Utils.makeItemsUnclickable(finishTv);
            Utils.showItems(nextTv);
            Utils.makeItemsClickable(nextTv);
            finishedDialog.dismiss();
            testOptionsDialog();
        });

        finishedDialog.show();
    }

}