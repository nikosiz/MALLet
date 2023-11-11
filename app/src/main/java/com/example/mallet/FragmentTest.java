package com.example.mallet;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.mallet.databinding.DialogTestAreYouReadyBinding;
import com.example.mallet.databinding.DialogTestFinishedBinding;
import com.example.mallet.databinding.FragmentTestBinding;
import com.example.mallet.utils.ModelAnswer;
import com.example.mallet.utils.ModelFlashcard;
import com.example.mallet.utils.ModelMultipleChoice;
import com.example.mallet.utils.ModelTrueFalse;
import com.example.mallet.utils.ModelWritten;
import com.example.mallet.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;

import org.apache.commons.lang3.time.StopWatch;

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
    private final StopWatch stopWatch = new StopWatch();


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

        startTestDialog();

        questionsLl = binding.testQuestionsLl;

        nextTv = binding.testNextTv;
        nextTv.setOnClickListener(v -> nextQuestion());
        Utils.showItems(nextTv);

        finishTv = binding.testFinishTv;
        finishTv.setOnClickListener(v -> testFinishedDialog());
        Utils.hideItems(finishTv);

        writtenQuestionTv = writtenQuestionView.findViewById(R.id.written_questionTv);
        writtenAnswerEt = writtenQuestionView.findViewById(R.id.written_answerEt);
    }

    private void setupToolbar() {
        Toolbar toolbar = binding.testToolbar;
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("");
        binding.testToolbarBackIv.setOnClickListener(v -> getActivity().finish());
        binding.testOptionsIv.setOnClickListener(v -> startTestDialog());
    }

    private void startTestDialog() {
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

            currentQuestionIndex = 0;
            dialog.dismiss();
            stopWatch.start();
        });
    }

    private void nextQuestion() {
        if (currentQuestionIndex < 29) {
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
                        testFinishedDialog();
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
                        testFinishedDialog();
                    }
                }
            } else if (currentQuestionIndex < 19) {

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
                        testFinishedDialog();
                    }
                } else {
                    System.out.println("Points: " + points);
                    currentQuestionIndex++;
                    System.out.println("Question index: " + currentQuestionIndex);

                    if (currentQuestionIndex - 10 < multipleChoiceQuestions.size()) {
                        displayMultipleChoiceQuestion(multipleChoiceQuestions, questionsLl, getLayoutInflater());
                    }
                }
            } else if (currentQuestionIndex < 30) {
                boolean isTrueFalseCorrect = checkTrueFalseAnswer(trueFalseClicked, trueFalseCorrectAnswer);

                if (isTrueFalseCorrect) {
                    points++;
                    System.out.println("Points: " + points);
                    currentQuestionIndex++;
                    System.out.println("Question index: " + currentQuestionIndex);

                    if (currentQuestionIndex - 20 < trueFalseQuestions.size()) {
                        displayTrueFalseQuestion(trueFalseQuestions, questionsLl, getLayoutInflater());
                    }
                } else {
                    currentQuestionIndex++;
                    System.out.println("Question index: " + currentQuestionIndex);

                    if (currentQuestionIndex - 10 < trueFalseQuestions.size()) {
                        displayTrueFalseQuestion(trueFalseQuestions, questionsLl, getLayoutInflater());
                    }
                }
            }
        } else {
            testFinishedDialog();
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
        ModelTrueFalse trueFalseQuestion = questions.get(currentQuestionIndex - 20);

        View trueFalseQuestionItem = inflater.inflate(R.layout.model_true_false, ll, false);

        TextView trueFalseQuestionTv = trueFalseQuestionItem.findViewById(R.id.trueFalse_questionTv);
        trueFalseQuestionTv.setText(trueFalseQuestion.getQuestion());

        TextView trueFalseAnswerTv = trueFalseQuestionItem.findViewById(R.id.trueFalse_answerTv);
        trueFalseAnswerTv.setText(trueFalseQuestion.getDisplayedAnswer());

        trueFalseCorrectAnswer = trueFalseQuestion.getCorrectAnswer();

        trueTv = trueFalseQuestionItem.findViewById(R.id.trueFalse_trueTv);
        falseTv = trueFalseQuestionItem.findViewById(R.id.trueFalse_falseTv);

        Typeface remBold = Typeface.createFromAsset(requireContext().getAssets(), "rem_bold.ttf");
        Typeface remRegular = Typeface.createFromAsset(requireContext().getAssets(), "rem_regular.ttf");

        trueTv.setOnClickListener(v -> {
            trueFalseClicked = 1;
            checkTrueFalseAnswer(trueFalseClicked, trueFalseCorrectAnswer);

            trueTv.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
            trueTv.setTypeface(remBold);

            falseTv.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            falseTv.setTypeface(remRegular);
        });

        falseTv.setOnClickListener(v -> {
            trueFalseClicked = 2;
            checkTrueFalseAnswer(trueFalseClicked, trueFalseCorrectAnswer);

            trueTv.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            trueTv.setTypeface(remRegular);

            falseTv.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
            falseTv.setTypeface(remBold);
        });

        ll.addView(trueFalseQuestionItem);
        //} else {
        // All questions have been shown
        //}
    }

    private int trueFalseClicked;
    private boolean trueFalseCorrectAnswer;

    private boolean checkTrueFalseAnswer(int clickedAnswer, boolean correctAnswer) {
        if (correctAnswer && clickedAnswer == 1) {
            return true;
        } else if (!correctAnswer && clickedAnswer == 2) {
            return true;
        } else if (correctAnswer && clickedAnswer == 2) {
            return false;
        } else {
            return false;
        }
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

        Typeface remBold = Typeface.createFromAsset(requireContext().getAssets(), "rem_bold.ttf");
        Typeface remRegular = Typeface.createFromAsset(requireContext().getAssets(), "rem_regular.ttf");

        option1Tv.setOnClickListener(v -> {
            multipleChoiceClickedPosition = 0;
            Utils.showToast(getContext(), String.valueOf(multipleChoiceClickedPosition));

            option1Tv.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
            option1Tv.setTypeface(remBold);

            option2Tv.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            option2Tv.setTypeface(remRegular);

            option3Tv.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            option3Tv.setTypeface(remRegular);

            option4Tv.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            option4Tv.setTypeface(remRegular);
        });

        option2Tv.setOnClickListener(v -> {
            multipleChoiceClickedPosition = 1;
            Utils.showToast(getContext(), String.valueOf(multipleChoiceClickedPosition));

            option1Tv.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            option1Tv.setTypeface(remRegular);

            option2Tv.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
            option2Tv.setTypeface(remBold);

            option3Tv.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            option3Tv.setTypeface(remRegular);

            option4Tv.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            option4Tv.setTypeface(remRegular);
        });

        option3Tv.setOnClickListener(v -> {
            multipleChoiceClickedPosition = 2;
            Utils.showToast(getContext(), String.valueOf(multipleChoiceClickedPosition));

            option1Tv.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            option1Tv.setTypeface(remRegular);

            option2Tv.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            option2Tv.setTypeface(remRegular);

            option3Tv.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
            option3Tv.setTypeface(remBold);

            option4Tv.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            option4Tv.setTypeface(remRegular);
        });

        option4Tv.setOnClickListener(v -> {
            multipleChoiceClickedPosition = 3;
            Utils.showToast(getContext(), String.valueOf(multipleChoiceClickedPosition));

            option1Tv.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            option1Tv.setTypeface(remRegular);

            option2Tv.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            option2Tv.setTypeface(remRegular);

            option3Tv.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            option3Tv.setTypeface(remRegular);

            option4Tv.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
            option4Tv.setTypeface(remBold);
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


    private void testFinishedDialog() {
        Dialog finishedDialog = Utils.createDialog(requireContext(), R.layout.dialog_test_finished, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT), Gravity.BOTTOM);
        DialogTestFinishedBinding dialogBinding = DialogTestFinishedBinding.inflate(getLayoutInflater());
        finishedDialog.setContentView(dialogBinding.getRoot());
        finishedDialog.show();
        stopWatch.stop();

        TextView scoreTimeTv = dialogBinding.testFinishedScoreTimeTv;
        scoreTimeTv.setText(getActivity().getString(R.string.you_finished_and_scored, formatTime(stopWatch.getTime()), String.valueOf(points)));

        TextView motivationalMessageTv = dialogBinding.testFinishedMessageTv;
        motivationalMessageTv.setText(motivationalMessage());

        TextView dialogFinishTv = dialogBinding.testFinishedFinishTv;
        TextView dialogRestartTv = dialogBinding.testFinishedRestartTv;

        dialogFinishTv.setOnClickListener(v -> {
            finishedDialog.dismiss();
            requireActivity().finish();
        });

        dialogRestartTv.setOnClickListener(v -> {
            currentQuestionIndex = 0;
            writtenQuestions = activityLearn.generateWrittenQuestions();
            multipleChoiceQuestions = activityLearn.generateMultipleChoiceQuestions();
            trueFalseQuestions = activityLearn.generateTrueFalseQuestions();

            Utils.hideItems(finishTv);
            Utils.makeItemsUnclickable(finishTv);

            Utils.showItems(nextTv);
            Utils.makeItemsClickable(nextTv);

            finishedDialog.dismiss();
            stopWatch.reset();

            startTestDialog();
        });


    }

    private String motivationalMessage() {
        String motivationalMessage;
        if (points >= 0 && points < 5) {
            motivationalMessage = getResources().getString(R.string.motivational_message05);
        } else if (points >= 5 && points < 10) {
            motivationalMessage = getResources().getString(R.string.motivational_message510);
        } else if (points >= 10 && points < 15) {
            motivationalMessage = getResources().getString(R.string.motivational_message1015);
        } else if (points >= 15 && points < 20) {
            motivationalMessage = getResources().getString(R.string.motivational_message1520);
        } else if (points >= 20 && points < 25) {
            motivationalMessage = getResources().getString(R.string.motivational_message2025);
        } else if (points >= 25 && points < 30) {
            motivationalMessage = getResources().getString(R.string.motivational_message2530);
        } else {
            motivationalMessage = getResources().getString(R.string.motivational_message30);
        }

        return motivationalMessage;
    }

    private String formatTime(long elapsedTime) {
        long seconds = elapsedTime / 1000;
        long minutes = seconds / 60;
        seconds %= 60;

        if (minutes > 0) {
            return String.format("%d minutes %d seconds", minutes, seconds);
        } else {
            return String.format("%d seconds", seconds);
        }
    }

}