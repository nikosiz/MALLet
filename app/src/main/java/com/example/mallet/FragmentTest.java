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

import com.example.mallet.databinding.DialogTestAreYouReadyBinding;
import com.example.mallet.databinding.DialogTestFinishedBinding;
import com.example.mallet.databinding.FragmentTestBinding;
import com.example.mallet.utils.ModelAnswer;
import com.example.mallet.utils.ModelFlashcard;
import com.example.mallet.utils.ModelLearningSet;
import com.example.mallet.utils.ModelMultipleChoice;
import com.example.mallet.utils.ModelTrueFalse;
import com.example.mallet.utils.ModelWritten;
import com.example.mallet.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FragmentTest extends Fragment {
    private ActivityLearn activityLearn;
    private FragmentTestBinding binding;
    private ModelLearningSet learningSet;
    private String learningSetName;
    private List<ModelFlashcard> flashcardList;
    private static final String PREFS_NAME = "TestSettings";
    private static final String KEY_NR_OF_QUESTIONS = "nrOfQuestions";
    private static final String KEY_TRUE_FALSE = "trueFalse";
    private static final String KEY_MULTIPLE_CHOICE = "multipleChoice";
    private static final String KEY_WRITTEN = "written";


    // dialog test options
    private TextView setNameTv;
    private final int nrOfQuestions = 30;
    private TextView startTestTv;

    //contents
    private List<ModelWritten> writtenQuestions;
    private TextView writtenQuestionTv;
    private TextInputEditText writtenAnswerEt;
    private String writtenAnswer;
    private TextView trueFalseAnswerTv;
    private List<ModelMultipleChoice> multipleChoiceQuestions;
    private TextView multipleChoiceQuestionTv;
    private List<ModelTrueFalse> trueFalseQuestions;
    private TextView trueFalseQuestionTv;
    private TextView nextTv, finishTv, errorTv;
    private int currentQuestionIndex = 0;
    private LinearLayout questionsLl;

    // dialog test finished
    private TextView scoreTv, timeTv;
    private LinearLayout userAnswersLl;
    private TextView messageTv;
    private TextView restartTestTv, finishActivityTv;


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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTestBinding.inflate(inflater, container, false);
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        setupContents();

        testOptionsDialog();

        return binding.getRoot();
    }

    private void setupContents() {
        flashcardList = getLearningSetData();

        Bundle args = getArguments();
        if (args != null) {
            learningSet = args.getParcelable("learningSet");
            learningSetName = args.getParcelable("learningSetName");
        }

        setupToolbar();

        currentQuestionIndex = 0;

        questionsLl = binding.testQuestionsLl;

        nextTv = binding.testNextTv;
        nextTv.setOnClickListener(v -> nextQuestion());
        Utils.showItems(nextTv);

        finishTv = binding.testFinishTv;
        finishTv.setOnClickListener(v -> testFinishedDialog());
        // Utils.hideItems(finishTv, errorTv);

        nextTv = binding.testNextTv;
        restartTestTv = binding.testRestartTv;
    }

    private void setupToolbar() {
        Toolbar toolbar = binding.testToolbar;
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(""); // Set the title to an empty string

        binding.testToolbarBackIv.setOnClickListener(v -> requireActivity().finish());
        binding.testOptionsIv.setOnClickListener(v -> testOptionsDialog());
    }

    private void testOptionsDialog() {
        Dialog dialog = Utils.createDialog(requireContext(), R.layout.dialog_test_are_you_ready, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT), Gravity.BOTTOM);
        DialogTestAreYouReadyBinding dialogBinding = DialogTestAreYouReadyBinding.inflate(LayoutInflater.from(requireContext()));
        dialog.setContentView(dialogBinding.getRoot());
        dialog.show();

        TextView startTestTv = dialogBinding.testReadyStartTv;
        startTestTv.setOnClickListener(v -> {
            startTest();
            dialog.dismiss();
        });
    }

    private void startTest() {
        nextQuestion();
    }

    private void nextQuestion() {
        if (currentQuestionIndex < nrOfQuestions) {
            if (currentQuestionIndex < 10) {
                writtenQuestions = activityLearn.generateWrittenQuestions();

                // Display a written question
                if (currentQuestionIndex < writtenQuestions.size()) {
                    displayWrittenQuestion(writtenQuestions, questionsLl, getLayoutInflater());
                }
            } else if (currentQuestionIndex < 20) {
                multipleChoiceQuestions = activityLearn.generateMultipleChoiceQuestions();

                // Display a multiple-choice question
                if (currentQuestionIndex - 10 < multipleChoiceQuestions.size()) {
                    displayMultipleChoiceQuestion(multipleChoiceQuestions, questionsLl, getLayoutInflater());
                }
            } else if (currentQuestionIndex < 30) {
                trueFalseQuestions = activityLearn.generateTrueFalseQuestions();

                // Display a true/false question
                if (currentQuestionIndex - 20 < trueFalseQuestions.size()) {
                    displayTrueFalseQuestion(trueFalseQuestions, questionsLl, getLayoutInflater());
                }
            }
            currentQuestionIndex++;
        } else {
            // Reached the maximum number of questions
            Utils.showItems(finishTv);
            Utils.makeItemsClickable(finishTv);
            Utils.hideItems(nextTv);
            Utils.makeItemsUnclickable(nextTv);
            testFinishedDialog();
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

        return clickedOption == correctAnswerPosition;
    }

    private void displayTrueFalseQuestion(List<ModelTrueFalse> questions, LinearLayout ll, LayoutInflater inflater) {
        ll.removeAllViews();

        //if (currentQuestionIndex < questions.size()) {
        int i = 0;
        ModelTrueFalse trueFalseQuestion = questions.get(i);

        View trueFalseQuestionItem = inflater.inflate(R.layout.model_true_false, ll, false);

        trueFalseQuestionTv = trueFalseQuestionItem.findViewById(R.id.trueFalse_questionTv);
        trueFalseQuestionTv.setText(trueFalseQuestion.getQuestion());

        Button trueBtn = trueFalseQuestionItem.findViewById(R.id.trueFalse_answerTrueBtn);
        Button falseBtn = trueFalseQuestionItem.findViewById(R.id.trueFalse_answerFalseBtn);

        trueBtn.setOnClickListener(v -> checkTrueFalseAnswer(true));
        falseBtn.setOnClickListener(v -> checkTrueFalseAnswer(false));

        ll.addView(trueFalseQuestionItem);
        i++;
        //} else {
        // All questions have been shown
        //}
    }

    private void checkTrueFalseAnswer(boolean userAnswer) {
        // Handle the user's answer for true/false question
        // You can compare it with the correct answer and proceed accordingly
    }


    private TextView writtenAnswerTv;

    private void displayWrittenQuestion(List<ModelWritten> questions, LinearLayout
            ll, LayoutInflater inflater) {
        ll.removeAllViews();

        // if (currentQuestionIndex < questions.size()) {
        int i = 0;
        ModelWritten writtenQuestion = questions.get(i);

        View writtenQuestionItem = inflater.inflate(R.layout.model_written, ll, false);
        writtenQuestionTv = writtenQuestionItem.findViewById(R.id.written_questionTv);
        writtenQuestionTv.setText(writtenQuestion.getQuestion());

        writtenAnswerTv = writtenQuestionItem.findViewById(R.id.written_correctAnswersTv);
        writtenAnswerTv.setText("\"" + writtenQuestion.getCorrectAnswer() + "\" or \"" + writtenQuestion.getAlternativeAnswer() + "\"");

        writtenAnswerEt = writtenQuestionItem.findViewById(R.id.written_answerEt);

        writtenAnswerEt.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                writtenAnswer = Objects.requireNonNull(writtenAnswerEt.getText()).toString();
            }
        });

        ll.addView(writtenQuestionItem);
        i++;
        // System.out.println((currentQuestionIndex));
        //} else {
        // All questions have been shown
        //}
    }

    private void displayMultipleChoiceQuestion
            (List<ModelMultipleChoice> questions, LinearLayout ll, LayoutInflater inflater) {
        ll.removeAllViews();

        //if (currentQuestionIndex < questions.size()) {
        int i = 0;
        ModelMultipleChoice multipleChoiceQuestion = questions.get(i);

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

        System.out.println(correctAnswerPosition);

        option1Tv.setOnClickListener(v -> checkMultipleChoiceAnswer(0, correctAnswerPosition));
        option2Tv.setOnClickListener(v -> checkMultipleChoiceAnswer(1, correctAnswerPosition));
        option3Tv.setOnClickListener(v -> checkMultipleChoiceAnswer(2, correctAnswerPosition));
        option4Tv.setOnClickListener(v -> checkMultipleChoiceAnswer(3, correctAnswerPosition));

        ll.addView(multipleChoiceQuestionItem);
        i++;
        //} else{
        // All questions have been shown
        //}
    }

    private void testFinishedDialog() {
        Dialog finishedDialog = Utils.createDialog(requireContext(), R.layout.dialog_test_finished, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT), Gravity.BOTTOM);
        DialogTestFinishedBinding dialogBinding = DialogTestFinishedBinding.inflate(getLayoutInflater());
        finishedDialog.setContentView(dialogBinding.getRoot());

        TextView dialogFinishTv = dialogBinding.testFinishedFinishTv;
        TextView dialogRestartTv = dialogBinding.testFinishedRestartTv;

        dialogFinishTv.setOnClickListener(v -> {
            finishedDialog.dismiss();
            requireActivity().finish();
        });

        dialogRestartTv.setOnClickListener(v -> {
            currentQuestionIndex = 0;
            Utils.hideItems(finishTv);
            Utils.makeItemsUnclickable(finishTv);
            Utils.showItems(nextTv);
            Utils.makeItemsClickable(nextTv);
            finishedDialog.dismiss();
            testOptionsDialog();
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
