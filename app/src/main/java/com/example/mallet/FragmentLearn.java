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
    private FragmentLearnBinding binding;
    private static final String PREFS_NAME = "FragmentLearnSettings";
    private MaterialSwitch multipleChoiceMs, writtenMs;
    private static final String KEY_MULTIPLE_CHOICE = "multipleChoice";
    private static final String KEY_WRITTEN = "written";
    private int checkedSwitches;
    private static final int MAX_QUESTIONS = 20;
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
    private List<ModelMultipleChoice> multipleChoiceQuestions;
    private int multipleChoiceCorrectAnswerPosition, multipleChoiceAlternativeAnswerPosition, multipleChoiceWrongAnswer1Position, multipleChoiceWrongAnswer2Position, multipleChoiceWrongAnswer3Position;
    private TextView nextTv, prevTv, finishTv, errorTv;
    private int currentQuestionIndex = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLearnBinding.inflate(inflater, container, false);

        writtenQuestionView = inflater.inflate(R.layout.model_written, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        setupContents();

        learnOptionsDialog();
        Utils.showItems(nextTv);
        Utils.hideItems(finishTv);

        //writtenQuestions = generateWrittenQuestions();
        multipleChoiceQuestions = generateMultipleChoiceQuestions();

        Utils.hideItems(errorTv);

        /*nextTv.setOnClickListener(v -> {
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
        });*/

        prevTv.setOnClickListener(v -> {
            writtenAnswerEt.setText(writtenAnswer);

            currentQuestionIndex--;

            if (currentQuestionIndex > 0) {
                displayWrittenQuestions(writtenQuestions, questionsLl, inflater);

            } else if (currentQuestionIndex == 0) {
                displayWrittenQuestions(writtenQuestions, questionsLl, inflater);
            } else {
                currentQuestionIndex = 0;
            }
            Utils.hideItems(answersLl);
            System.out.println(currentQuestionIndex);
        });


        finishTv.setOnClickListener(v ->

                learningFinishedDialog());

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
        questionsLl = binding.learnQuestionLl;
        nextTv = binding.learnNextTv;
        prevTv = binding.learnPrevTv;
        finishTv = binding.learnFinishTv;
        writtenQuestionTv = writtenQuestionView.findViewById(R.id.written_questionTv);
        writtenAnswerEt = writtenQuestionView.findViewById(R.id.written_answerEt);
    }

    private void setupToolbar() {
        Toolbar toolbar = binding.learnToolbar;
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("");
        binding.learnToolbarBackIv.setOnClickListener(v -> getActivity().finish());
        binding.learnOptionsIv.setOnClickListener(v -> learnOptionsDialog());
    }

    private void learnOptionsDialog() {
        Dialog optionsDialog = createDialog(R.layout.dialog_forgot_password);
        DialogLearnOptionsBinding optionsDialogBinding = DialogLearnOptionsBinding.inflate(getLayoutInflater());
        Objects.requireNonNull(optionsDialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        optionsDialog.setContentView(optionsDialogBinding.getRoot());

        multipleChoiceMs = optionsDialogBinding.learnOptionsMultipleChoiceMs;
        writtenMs = optionsDialogBinding.learnOptionsWrittenMs;
        TextView restartTv = optionsDialogBinding.learnOptionsRestartTv;
        TextView startTv = optionsDialogBinding.learnOptionsStartTv;
        errorTv = optionsDialogBinding.learnOptionsErrorTv;

        multipleChoiceMs.setChecked(Utils.getSwitchState(requireContext(), PREFS_NAME, KEY_MULTIPLE_CHOICE));
        multipleChoiceMs.setOnCheckedChangeListener((buttonView, isChecked) -> handleSwitchChange(multipleChoiceMs, KEY_MULTIPLE_CHOICE, isChecked));

        writtenMs.setChecked(Utils.getSwitchState(requireContext(), PREFS_NAME, KEY_WRITTEN));
        writtenMs.setOnCheckedChangeListener((buttonView, isChecked) -> handleSwitchChange(writtenMs, KEY_WRITTEN, isChecked));

        handleSwitchState();

        restartTv.setOnClickListener(v -> {
            currentQuestionIndex = 0;
            optionsDialog.dismiss();
        });

        startTv.setOnClickListener(v -> {
            if (writtenMs.isChecked() && !multipleChoiceMs.isChecked()) {
                displayWrittenQuestions(writtenQuestions, questionsLl, getLayoutInflater());
            } else if (!writtenMs.isChecked() && multipleChoiceMs.isChecked()) {
                displayMultipleChoiceQuestion(multipleChoiceQuestions, questionsLl, getLayoutInflater());
            }
            currentQuestionIndex = 0;
            optionsDialog.dismiss();
        });

        optionsDialog.show();
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

    private void displayWrittenQuestions(List<ModelWritten> questions, LinearLayout
            ll, LayoutInflater inflater) {
        ll.removeAllViews();

        if (currentQuestionIndex < questions.size()) {
            ModelWritten question = questions.get(currentQuestionIndex);

            View questionItem = inflater.inflate(R.layout.model_written, ll, false);
            writtenQuestionTv = questionItem.findViewById(R.id.written_questionTv);
            writtenQuestionTv.setText(question.getQuestion());

            correctAnswersTv = questionItem.findViewById(R.id.written_correctAnswersTv);
            correctAnswersTv.setText("\"" + question.getCorrectAnswer() + "\" or \"" + question.getAlternativeAnswer() + "\"");

            writtenAnswerEt = questionItem.findViewById(R.id.written_answerEt);
            answersLl = questionItem.findViewById(R.id.written_correctAnswersLl);
            Utils.hideItems(answersLl);

            writtenAnswerEt.setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus) {
                    writtenAnswer = writtenAnswerEt.getText().toString();
                }
            });

            ll.addView(questionItem);
        } else {
            // All questions have been shown
        }
    }

    private void displayMultipleChoiceQuestion
            (List<ModelMultipleChoice> questions, LinearLayout ll, LayoutInflater inflater) {
        ll.removeAllViews();

        if (currentQuestionIndex < questions.size()) {
            ModelMultipleChoice question = questions.get(currentQuestionIndex);

            View questionItem = inflater.inflate(R.layout.model_multiple_choice, ll, false);
            multipleChoiceQuestionTv = questionItem.findViewById(R.id.multipleChoice_questionTv);
            multipleChoiceQuestionTv.setText(question.getQuestion());

            ll.addView(questionItem);
        } else {
            // All questions have been shown
        }
    }

    private void learningFinishedDialog() {
        Dialog finishedDialog = createDialog(R.layout.dialog_learning_finished);
        DialogLearningFinishedBinding dialogBinding = DialogLearningFinishedBinding.inflate(getLayoutInflater());
        Objects.requireNonNull(finishedDialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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

            writtenCorrectAnswerPosition = 1;
            writtenAlternativeAnswerPosition = 2;

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
            System.out.println(written);
        }
        return questionList;
    }

    private List<ModelMultipleChoice> generateMultipleChoiceQuestions() {
        List<ModelFlashcard> flashcardList = getFlashcards();
        List<ModelMultipleChoice> questionList = new ArrayList<>();
        Random random = new Random();

        int multipleChoiceQuestionPosition = random.nextInt(2);

        for (ModelFlashcard flashcard : flashcardList) {
            List<String> options = new ArrayList<>();
            options.add(flashcard.getTerm());
            options.add(flashcard.getDefinition());
            options.add(flashcard.getTranslation());

            // Shuffle the options
            Collections.shuffle(options);

            multipleChoiceCorrectAnswerPosition = 1;
            multipleChoiceAlternativeAnswerPosition = 2;
            multipleChoiceWrongAnswer1Position = random.nextInt(2);
            multipleChoiceWrongAnswer2Position = random.nextInt(2);
            multipleChoiceWrongAnswer3Position = random.nextInt(2);

            switch (multipleChoiceQuestionPosition) {
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

            ModelMultipleChoice multipleChoice = new ModelMultipleChoice(
                    options.get(multipleChoiceQuestionPosition),
                    options.get(multipleChoiceCorrectAnswerPosition),
                    options.get(multipleChoiceAlternativeAnswerPosition),
                    options.get(multipleChoiceWrongAnswer1Position),
                    options.get(multipleChoiceWrongAnswer2Position),
                    options.get(multipleChoiceWrongAnswer3Position)
            );

            // Add the question to the list
            questionList.add(multipleChoice);

            // Print the question and its answers to the CMD
            System.out.println("Question: " + flashcard.getTerm());
            System.out.println("Correct Answer: " + multipleChoice.getCorrectAnswer());
            System.out.println("Alternative Answer: " + multipleChoice.getAlternativeAnswer());
            System.out.println("Wrong Answer 1: " + multipleChoice.getWrongAnswer1());
            System.out.println("Wrong Answer 2: " + multipleChoice.getWrongAnswer2());
            System.out.println("Wrong Answer 3: " + multipleChoice.getWrongAnswer3());
            System.out.println();
        }

        return questionList;
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
