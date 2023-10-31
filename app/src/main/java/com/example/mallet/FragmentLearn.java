//TODO: make this class display written/multipleChoice based on which switches are selected

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
import android.view.WindowManager;
import android.widget.Button;
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
import com.example.mallet.utils.ModelMultipleChoice;
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
    private static final String PREFS_NAME = "FragmentLearnSettings";
    private static final String KEY_MULTIPLE_CHOICE = "multipleChoice";
    private static final String KEY_WRITTEN = "written";
    private static final int MAX_QUESTIONS = 20;
    private static int WRITTEN_QUESTIONS, MULTIPLE_CHOICE_QUESTIONS;
    private FragmentLearnBinding binding;
    private MaterialSwitch multipleChoiceMs, writtenMs;
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
    private Button answerA, answerB, answerC, answerD;
    private List<ModelMultipleChoice> multipleChoiceQuestions;
    private int multipleChoiceCorrectAnswerPosition, multipleChoiceAlternativeAnswerPosition, multipleChoiceWrongAnswer1Position, multipleChoiceWrongAnswer2Position, multipleChoiceWrongAnswer3Position;
    private TextView nextTv, prevTv, finishTv, errorTv;
    private int currentQuestionIndex = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLearnBinding.inflate(inflater, container, false);

        writtenQuestionView = inflater.inflate(R.layout.model_written, container, false);
        multipleChoiceQuestionView = inflater.inflate(R.layout.model_multiple_choice, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        setupContents();

        learnOptionsDialog();
        Utils.showItems(nextTv);
        Utils.hideItems(finishTv);


        Utils.hideItems(errorTv);

        nextTv.setOnClickListener(v -> {
            if (!multipleChoiceMs.isChecked()) {
                writtenAnswer = writtenAnswerEt.getText().toString().toLowerCase().trim();
                ModelWritten writtenQuestion = writtenQuestions.get(currentQuestionIndex);
                String writtenCorrectAnswer = writtenQuestion.getCorrectAnswer();
                String writtenAlternativeAnswer = writtenQuestion.getAlternativeAnswer();

                boolean isCorrect = checkAnswer(writtenAnswer, writtenCorrectAnswer, writtenAlternativeAnswer);

                if (isCorrect) {
                    writtenAnswerEt.setText("");
                    currentQuestionIndex++;

                    if (currentQuestionIndex < MAX_QUESTIONS && currentQuestionIndex < writtenQuestions.size()) {
                        displayWrittenQuestions(writtenQuestions, questionsLl, inflater);
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
                String multipleChoiceCorrectAnswer = multipleChoiceQuestion.getCorrectAnswer();
                String multipleChoiceAlternativeAnswer = multipleChoiceQuestion.getAlternativeAnswer();
                currentQuestionIndex++;

                System.out.println(multipleChoiceCorrectAnswer + "\n" + multipleChoiceAlternativeAnswer);

                if (currentQuestionIndex < MAX_QUESTIONS && currentQuestionIndex < multipleChoiceQuestions.size()) {
                    displayMultipleChoiceQuestion(multipleChoiceQuestions, questionsLl, inflater);
                } else {
                    Utils.showItems(finishTv);
                    Utils.makeItemsClickable(finishTv);
                    Utils.hideItems(nextTv);
                    Utils.makeItemsUnclickable(nextTv);
                    learningFinishedDialog();
                }
            }
        });

        finishTv.setOnClickListener(v -> learningFinishedDialog());

        return binding.getRoot();
    }

    private boolean checkAnswer(String userAnswer, String correctAnswer, String
            alternativeAnswer) {
        String userInputLower = userAnswer.toLowerCase();
        String correctAnswerLower = correctAnswer.toLowerCase();
        String alternativeAnswerLower = alternativeAnswer.toLowerCase();
        return userInputLower.equals(correctAnswerLower) || userInputLower.equals(alternativeAnswerLower);
    }

    private void setupContents() {
        setupToolbar();

        questionsLl = binding.learnQuestionsLl;
        nextTv = binding.learnNextTv;
        prevTv = binding.learnPrevTv;
        finishTv = binding.learnFinishTv;

        writtenQuestionTv = writtenQuestionView.findViewById(R.id.written_questionTv);
        writtenAnswerEt = writtenQuestionView.findViewById(R.id.written_answerEt);

        multipleChoiceQuestionTv = multipleChoiceQuestionView.findViewById(R.id.multipleChoice_questionTv);
    }

    private void setupToolbar() {
        Toolbar toolbar = binding.learnToolbar;
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("");
        binding.learnToolbarBackIv.setOnClickListener(v -> getActivity().finish());
        binding.learnOptionsIv.setOnClickListener(v -> learnOptionsDialog());
    }

    private void learnOptionsDialog() {
        Dialog optionsDialog = Utils.createDialog(requireContext(), R.layout.dialog_forgot_password, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT), Gravity.BOTTOM);
        DialogLearnOptionsBinding optionsDialogBinding = DialogLearnOptionsBinding.inflate(getLayoutInflater());
        optionsDialog.setContentView(optionsDialogBinding.getRoot());
        optionsDialog.show();

        multipleChoiceMs = optionsDialogBinding.learnOptionsMultipleChoiceMs;
        multipleChoiceMs.setChecked(Utils.getSwitchState(requireContext(), PREFS_NAME, KEY_MULTIPLE_CHOICE));
        multipleChoiceMs.setOnCheckedChangeListener((buttonView, isChecked) -> handleSwitchChange(multipleChoiceMs, KEY_MULTIPLE_CHOICE, isChecked));

        writtenMs = optionsDialogBinding.learnOptionsWrittenMs;
        writtenMs.setChecked(Utils.getSwitchState(requireContext(), PREFS_NAME, KEY_WRITTEN));
        writtenMs.setOnCheckedChangeListener((buttonView, isChecked) -> handleSwitchChange(writtenMs, KEY_WRITTEN, isChecked));

        TextView restartTv = optionsDialogBinding.learnOptionsRestartTv;
        TextView startTv = optionsDialogBinding.learnOptionsStartTv;
        errorTv = optionsDialogBinding.learnOptionsErrorTv;

        restartTv.setOnClickListener(v -> {
            currentQuestionIndex = 0;
            optionsDialog.dismiss();
        });

        startTv.setOnClickListener(v -> {
            if (writtenMs.isChecked() && !multipleChoiceMs.isChecked()) {
                writtenQuestions = generateWrittenQuestions();
                WRITTEN_QUESTIONS = MAX_QUESTIONS;
                displayWrittenQuestions(writtenQuestions, questionsLl, getLayoutInflater());
                optionsDialog.dismiss();
            } else if (!writtenMs.isChecked() && multipleChoiceMs.isChecked()) {
                multipleChoiceQuestions = generateMultipleChoiceQuestions();
                MULTIPLE_CHOICE_QUESTIONS = MAX_QUESTIONS;
                displayMultipleChoiceQuestion(multipleChoiceQuestions, questionsLl, getLayoutInflater());
                optionsDialog.dismiss();
            } else if (!writtenMs.isChecked() && !multipleChoiceMs.isChecked()) {
                Utils.showToast(getContext(), "NOPE");
            } else if (writtenMs.isChecked() && multipleChoiceMs.isChecked()) {
                MULTIPLE_CHOICE_QUESTIONS = 10;
                WRITTEN_QUESTIONS = 10;
                Utils.showToast(getContext(), "NOPE");
            }
            currentQuestionIndex = 0;
            //optionsDialog.dismiss();
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
        } else {
            // All questions have been shown
        }
    }

    private void displayMultipleChoiceQuestion
            (List<ModelMultipleChoice> mcqQuestions, LinearLayout ll, LayoutInflater inflater) {
        ll.removeAllViews();

        if (currentQuestionIndex < mcqQuestions.size()) {
            ModelMultipleChoice question = mcqQuestions.get(currentQuestionIndex);

            View multipleChoiceQuestionItem = inflater.inflate(R.layout.model_multiple_choice, ll, false);
            multipleChoiceQuestionTv = multipleChoiceQuestionItem.findViewById(R.id.multipleChoice_questionTv);
            multipleChoiceQuestionTv.setText(question.getQuestion());

            answerA = multipleChoiceQuestionItem.findViewById(R.id.multipleChoice_answerABtn);
            answerB = multipleChoiceQuestionItem.findViewById(R.id.multipleChoice_answerBBtn);
            answerC = multipleChoiceQuestionItem.findViewById(R.id.multipleChoice_answerCBtn);
            answerD = multipleChoiceQuestionItem.findViewById(R.id.multipleChoice_answerDBtn);

            answerA.setText("A");
            answerB.setText("B");
            answerC.setText("C");
            answerD.setText("D");


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
            writtenQuestions = generateWrittenQuestions();
            Utils.hideItems(finishTv);
            Utils.makeItemsUnclickable(finishTv);
            Utils.showItems(nextTv);
            Utils.makeItemsClickable(nextTv);
            finishedDialog.dismiss();
            learnOptionsDialog();
        });

        finishedDialog.show();
    }

    private Dialog createDialog(int layoutResId) {
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layoutResId);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        return dialog;
    }

    private List<ModelWritten> generateWrittenQuestions() {
        List<ModelFlashcard> flashcardList = getFlashcards();
        List<ModelWritten> questionList = new ArrayList();
        Random random = new Random();
        int writtenQuestionPosition = random.nextInt(2);

        for (ModelFlashcard flashcard : flashcardList) {
            List<String> options = new ArrayList<>();
            options.add(flashcard.getTerm());
            options.add(flashcard.getDefinition());
            options.add(flashcard.getTranslation());
            Collections.shuffle(options);

            switch (writtenQuestionPosition) {
                case 0:
                    writtenCorrectAnswerPosition = 1;
                    writtenAlternativeAnswerPosition = 2;
                    break;
                case 1:
                    writtenCorrectAnswerPosition = 0;
                    writtenAlternativeAnswerPosition = 2;
                    break;
                case 2:
                    writtenCorrectAnswerPosition = 1;
                    writtenAlternativeAnswerPosition = 0;
                    break;
            }

            ModelWritten written = new ModelWritten(options.get(writtenQuestionPosition), options.get(writtenCorrectAnswerPosition), options.get(writtenAlternativeAnswerPosition));
            questionList.add(written);
            //System.out.println(written);
        }
        return questionList;
    }

    private List<ModelMultipleChoice> generateMultipleChoiceQuestions() {
        List<ModelFlashcard> flashcardList = getFlashcards();
        List<ModelMultipleChoice> multipleChoiceQuestionList = new ArrayList();
        Random random = new Random();
        int multipleChoiceQuestionPosition = random.nextInt(2);

        for (ModelFlashcard flashcard : flashcardList) {
            List<String> options = new ArrayList<>();
            options.add(flashcard.getTerm());
            options.add(flashcard.getDefinition());
            options.add(flashcard.getTranslation());
            Collections.shuffle(options);

            switch (multipleChoiceQuestionPosition) {
                case 0:
                    multipleChoiceCorrectAnswerPosition = 1;
                    multipleChoiceAlternativeAnswerPosition = 2;
                    break;
                case 1:
                    multipleChoiceCorrectAnswerPosition = 0;
                    multipleChoiceAlternativeAnswerPosition = 2;
                    break;
                case 2:
                    multipleChoiceCorrectAnswerPosition = 1;
                    multipleChoiceAlternativeAnswerPosition = 0;
                    break;
            }

            ModelMultipleChoice generatedMultipleChoiceQuestion = new ModelMultipleChoice(options.get(multipleChoiceQuestionPosition), options.get(writtenCorrectAnswerPosition), options.get(writtenAlternativeAnswerPosition));
            multipleChoiceQuestionList.add(generatedMultipleChoiceQuestion);
            System.out.println(generatedMultipleChoiceQuestion);
        }
        return multipleChoiceQuestionList;
    }

    private List<ModelFlashcard> getFlashcards() {
        List<ModelFlashcard> flashcardList = new ArrayList<>();
        flashcardList.add(new ModelFlashcard("Dog", "A domesticated mammal", "Pies"));
        flashcardList.add(new ModelFlashcard("Cat", "A small domesticated carnivorous mammal", "Kot"));
        flashcardList.add(new ModelFlashcard("Elephant", "A large, herbivorous mammal with a trunk", "Słoń"));
        flashcardList.add(new ModelFlashcard("Lion", "A large wild cat known for its mane", "Lew"));
        flashcardList.add(new ModelFlashcard("Giraffe", "A tall, long-necked African mammal", "Żyrafa"));
        flashcardList.add(new ModelFlashcard("Snail", "A shelled gastropod", "Ślimak"));
        System.out.println(flashcardList);
        return flashcardList;
    }
}
