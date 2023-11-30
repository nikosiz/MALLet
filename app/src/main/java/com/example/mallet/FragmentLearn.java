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

import com.example.mallet.databinding.DialogLearnFinishedBinding;
import com.example.mallet.databinding.DialogLearnOptionsBinding;
import com.example.mallet.databinding.FragmentLearnBinding;
import com.example.mallet.utils.ModelWritten;
import com.example.mallet.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.Objects;

public class FragmentLearn extends Fragment {
    private static final String PREFS_NAME = "FragmentLearnSettings";
    private static final int MAX_QUESTIONS = 20;
    private ActivityLearn activityLearn;
    private FragmentLearnBinding binding;
    private LinearLayout questionsLl, answersLl;
    private View writtenQuestionView;
    private TextView writtenQuestionTv;
    private TextInputEditText writtenAnswerEt;
    private String writtenAnswer;
    private List<ModelWritten> writtenQuestions;
    private TextView nextTv, finishTv, errorTv;
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

        writtenQuestionView = inflater.inflate(R.layout.model_written, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        setupContents();

        return binding.getRoot();
    }

    private void setupContents() {
        setupToolbar();

        learnOptionsDialog();

        questionsLl = binding.learnQuestionsLl;

        nextTv = binding.learnNextTv;
        nextTv.setOnClickListener(v -> handleNextTvClick());
        Utils.showItems(nextTv);

        finishTv = binding.learnFinishTv;
        finishTv.setOnClickListener(v -> learningFinishedDialog());
        Utils.hideItems(finishTv, errorTv);

        writtenQuestionTv = writtenQuestionView.findViewById(R.id.written_questionTv);
        writtenAnswerEt = writtenQuestionView.findViewById(R.id.written_answerEt);

        writtenQuestions = activityLearn.generateWrittenQuestions();
        displayWrittenQuestions(writtenQuestions, questionsLl, getLayoutInflater());
    }

    private void setupToolbar() {
        Toolbar toolbar = binding.learnToolbar;
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("");
        binding.learnToolbarBackIv.setOnClickListener(v -> getActivity().finish());
    }

    private void learnOptionsDialog() {
        Dialog dialog = Utils.createDialog(requireContext(), R.layout.dialog_learn_options, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT), Gravity.BOTTOM);
        DialogLearnOptionsBinding dialogBinding = DialogLearnOptionsBinding.inflate(getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());
        dialog.show();

        TextView welcomeSubtitleTv = dialogBinding.learnOptionsSubtitleTv;
        welcomeSubtitleTv.setText(getActivity().getString(R.string.in_this_session_you_will_be_asked, String.valueOf(MAX_QUESTIONS)));
        TextView startTv = dialogBinding.learnOptionsStartTv;
        errorTv = dialogBinding.learnOptionsErrorTv;

        startTv.setOnClickListener(v -> {
            displayWrittenQuestions(writtenQuestions, questionsLl, getLayoutInflater());

            currentQuestionIndex = 0;

            dialog.dismiss();
        });
    }

    private void handleNextTvClick() {
        writtenAnswer = writtenAnswerEt.getText().toString().toLowerCase().trim();
        ModelWritten writtenQuestion = writtenQuestions.get(currentQuestionIndex);
        String writtenCorrectAnswer = writtenQuestion.getCorrectAnswer();

        boolean isCorrect = checkWrittenAnswer(writtenAnswer, writtenCorrectAnswer);

        if (isCorrect) {
            writtenAnswerEt.setText("");
            currentQuestionIndex++;

            if (currentQuestionIndex < MAX_QUESTIONS && currentQuestionIndex < writtenQuestions.size()) {
                displayWrittenQuestions(writtenQuestions, questionsLl, getLayoutInflater());
            } else {
                Utils.showItems(finishTv);
                Utils.enableItems(finishTv);
                Utils.hideItems(nextTv);
                Utils.disableItems(nextTv);
                learningFinishedDialog();
            }
        } else {

            Utils.showItems(answersLl);
        }

    }


    private boolean checkWrittenAnswer(String userAnswer, String correctAnswer) {
        String userInputLower = userAnswer.toLowerCase();
        String correctAnswerLower = correctAnswer.toLowerCase();
        return userInputLower.equals(correctAnswerLower);
    }


    private void displayWrittenQuestions(List<ModelWritten> wQuestions, LinearLayout
            ll, LayoutInflater inflater) {
        ll.removeAllViews();

        if (currentQuestionIndex < wQuestions.size()) {
            ModelWritten writtenQuestion = wQuestions.get(currentQuestionIndex);

            View writtenQuestionItem = inflater.inflate(R.layout.model_written, ll, false);
            writtenQuestionTv = writtenQuestionItem.findViewById(R.id.written_questionTv);
            writtenQuestionTv.setText(writtenQuestion.getQuestion());

            TextView correctAnswersTv = writtenQuestionItem.findViewById(R.id.written_correctAnswersTv);

            correctAnswersTv.setText("\"" + writtenQuestion.getCorrectAnswer() + "\"");

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

    private void learningFinishedDialog() {
        Dialog finishedDialog = Utils.createDialog(requireContext(), R.layout.dialog_learn_finished, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT), Gravity.BOTTOM);
        DialogLearnFinishedBinding dialogBinding = DialogLearnFinishedBinding.inflate(getLayoutInflater());
        finishedDialog.setContentView(dialogBinding.getRoot());

        TextView dialogFinishTv = dialogBinding.learningFinishedFinishTv;
        TextView dialogRestartTv = dialogBinding.learningFinishedRestartTv;

        dialogFinishTv.setOnClickListener(v -> {
            finishedDialog.dismiss();

            requireActivity().finish();

            finishedDialog.setOnDismissListener(d -> requireActivity().finish());
        });

        dialogRestartTv.setOnClickListener(v -> {
            currentQuestionIndex = 0;
            writtenQuestions = activityLearn.generateWrittenQuestions();
            Utils.hideItems(finishTv);
            Utils.disableItems(finishTv);
            Utils.showItems(nextTv);
            Utils.enableItems(nextTv);
            finishedDialog.dismiss();
            learnOptionsDialog();
        });

        finishedDialog.show();
    }
}