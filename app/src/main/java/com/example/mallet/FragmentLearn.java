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
import com.example.mallet.utils.ModelTrueFalse;
import com.example.mallet.utils.ModelWritten;
import com.example.mallet.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FragmentLearn extends Fragment {
    private ActivityLearn activityLearn;
    private FragmentLearnBinding binding;
    private ModelLearningSet learningSet;
    private String learningSetName;
    private List<ModelFlashcard> flashcardList;
    private static final String PREFS_NAME = "learnSettings";
    private static final String KEY_NR_OF_QUESTIONS = "nrOfQuestions";
    private static final String KEY_TRUE_FALSE = "trueFalse";
    private static final String KEY_MULTIPLE_CHOICE = "multipleChoice";
    private static final String KEY_WRITTEN = "written";


    // dialog learn options
    private TextView setNameTv;
    private final int nrOfQuestions = 24;
    private TextView startLearningTv;

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

    // dialog learn finished
    private TextView scoreTv, timeTv;
    private LinearLayout userAnswersLl;
    private TextView messageTv;
    private TextView restartlearnTv, finishActivityTv;


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
        binding = FragmentLearnBinding.inflate(inflater, container, false);
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        setupContents();

        learnOptionsDialog();

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

        questionsLl = binding.learnQuestionsLl;

        nextTv = binding.learnNextTv;
        nextTv.setOnClickListener(v -> nextQuestion());
        Utils.showItems(nextTv);

        finishTv = binding.learnFinishTv;
        finishTv.setOnClickListener(v -> learnFinishedDialog());

        nextTv = binding.learnNextTv;
    }

    private void setupToolbar() {
        Toolbar toolbar = binding.learnToolbar;
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(""); // Set the title to an empty string

        binding.learnToolbarBackIv.setOnClickListener(v -> requireActivity().finish());
        binding.learnOptionsIv.setOnClickListener(v -> learnOptionsDialog());
    }

    private void learnOptionsDialog() {
        Dialog dialog = Utils.createDialog(requireContext(), R.layout.dialog_learn_options, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT), Gravity.BOTTOM);
        DialogLearnOptionsBinding dialogBinding = DialogLearnOptionsBinding.inflate(LayoutInflater.from(requireContext()));
        dialog.setContentView(dialogBinding.getRoot());
        dialog.show();

        TextView startTv = dialogBinding.learnOptionsStartTv;
        TextView restartTv = dialogBinding.learnOptionsRestartTv;

        startTv.setOnClickListener(v -> {
            startLearning();
            dialog.dismiss();
        });

        restartTv.setOnClickListener(v -> {
            currentQuestionIndex = 0;
            dialog.dismiss();
        });
    }

    private void startLearning() {
        nextQuestion();
    }

    private LinearLayout answersLl;

    private void nextQuestion() {
        if (currentQuestionIndex < nrOfQuestions) {
            if (currentQuestionIndex < 12) {
                writtenQuestions = activityLearn.generateWrittenQuestions();

                // Display a written question
                if (currentQuestionIndex < writtenQuestions.size()) {
                    displayWrittenQuestion(writtenQuestions, questionsLl, getLayoutInflater());
                }
            } else if (currentQuestionIndex < 24) {
                multipleChoiceQuestions = activityLearn.generateMultipleChoiceQuestions();

                // Display a multiple-choice question
                if (currentQuestionIndex - 10 < multipleChoiceQuestions.size()) {
                    displayMultipleChoiceQuestion(multipleChoiceQuestions, questionsLl, getLayoutInflater());
                }
            }

            currentQuestionIndex++;
        } else {
            // Reached the maximum number of questions
            Utils.showItems(finishTv);
            Utils.makeItemsClickable(finishTv);
            Utils.hideItems(nextTv);
            Utils.makeItemsUnclickable(nextTv);
            learnFinishedDialog();
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

        answersLl = writtenQuestionItem.findViewById(R.id.written_correctAnswersLl);

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

    private void learnFinishedDialog() {
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
