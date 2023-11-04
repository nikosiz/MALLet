package com.example.mallet;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.mallet.databinding.ActivityLearnBinding;
import com.example.mallet.databinding.DialogConfirmExitBinding;
import com.example.mallet.utils.ModelFlashcard;
import com.example.mallet.utils.ModelLearningSet;
import com.example.mallet.utils.ModelMultipleChoice;
import com.example.mallet.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class ActivityLearn extends AppCompatActivity {
    private ActivityLearnBinding binding;
    private String fragmentName;
    private ModelLearningSet learningSet;
    private List<ModelFlashcard> flashcardList;
    private List<List<String>> flashcardTable;
    LinearLayout setTermsLl;
    TextView setNameTv, setTermTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLearnBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupContents();

        displayFlashcards(flashcardList, setTermsLl, getLayoutInflater());

        if (fragmentName != null) {
            try {
                Class<?> fragmentClass = Class.forName(fragmentName);
                Fragment fragment = (Fragment) fragmentClass.newInstance();

                Bundle args = new Bundle();
                args.putParcelable("learningSet", learningSet);
                fragment.setArguments(args);

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.learn_mainLl, fragment)
                        .commit();
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    private void setupContents() {
        fragmentName = getIntent().getStringExtra("fragment_class");

        learningSet = getIntent().getParcelableExtra("learningSet");
        flashcardList = learningSet.getTerms();

        setNameTv = binding.ALSetName;
        setNameTv.setText(learningSet.getName());

        setTermsLl = binding.ALSetTermsLl;
        setTermTv = binding.ALSetTerm;
    }

    private void displayFlashcards
            (List<ModelFlashcard> flashcards, LinearLayout linearLayout, LayoutInflater inflater) {
        linearLayout.removeAllViews();

        if (flashcards != null && flashcards.size() > 0) {
            for (ModelFlashcard flashcard : flashcards) {
                View flashcardItemView = inflater.inflate(R.layout.model_flashcard, linearLayout, false);
                Utils.setMargins(this, flashcardItemView, 0, 0, 0, 10);

                int paddingInDp = 5;
                float scale = getResources().getDisplayMetrics().density;
                int paddingInPixels = (int) (paddingInDp * scale + 0.5f);

                LinearLayout flashcardLL = flashcardItemView.findViewById(R.id.flashcard_mainLl);

                flashcardLL.setPadding(paddingInPixels, paddingInPixels, paddingInPixels, paddingInPixels);

                TextView flashcardTermTv = flashcardItemView.findViewById(R.id.flashcard_termTv);
                flashcardTermTv.setVisibility(View.VISIBLE);
                flashcardTermTv.setText(flashcard.getTerm());

                TextView flashcardDefinitionTv = flashcardItemView.findViewById(R.id.flashcard_definitionTv);
                flashcardDefinitionTv.setVisibility(View.VISIBLE);
                flashcardDefinitionTv.setText(flashcard.getDefinition());

                TextView flashcardTranslationTv = flashcardItemView.findViewById(R.id.flashcard_translationTv);
                flashcardTranslationTv.setVisibility(View.VISIBLE);
                flashcardTranslationTv.setText(flashcard.getTranslation());

                linearLayout.addView(flashcardItemView);
            }
        }
    }

    public List<ModelMultipleChoice> generateMultipleChoiceQuestions(List<List<String>> flashcardTable) {
        List<ModelMultipleChoice> questionList = new ArrayList<>();
        Random random = new Random();

        // Check if the table is empty or if it has less than 20 rows
        if (flashcardTable.isEmpty() || flashcardTable.size() <= 1) {
            System.out.println("Table does not have enough data to generate questions.");
            return questionList;
        }

        // Get the header row to access column names
        List<String> headerRow = flashcardTable.get(0);
        int termIndex = headerRow.indexOf("Term");
        int definitionIndex = headerRow.indexOf("Definition");
        int translationIndex = headerRow.indexOf("Translation");

        // Limit the number of questions to generate to 20 or the number of available rows (excluding the header)
        int maxQuestionsToGenerate = Math.min(20, flashcardTable.size() - 1);

        // Create an array to track which rows have been used
        boolean[] rowUsed = new boolean[flashcardTable.size()];

        // Generate and display questions
        for (int questionCount = 0; questionCount < maxQuestionsToGenerate; questionCount++) {
            int randomRowIndex;

            // Select a random unused row
            do {
                randomRowIndex = random.nextInt(flashcardTable.size() - 1) + 1; // Skip the header row
            } while (rowUsed[randomRowIndex]);

            rowUsed[randomRowIndex] = true;

            List<String> rowData = flashcardTable.get(randomRowIndex);

            String question = rowData.get(termIndex); // Take the Term as the question

            // Determine whether to use "Definition" or "Translation" for options
            int correctAnswerIndex;
            String correctAnswerType;

            boolean useDefinitionForOptions = random.nextBoolean();
            if (useDefinitionForOptions) {
                correctAnswerIndex = definitionIndex;
                correctAnswerType = "Definition";
            } else {
                correctAnswerIndex = translationIndex;
                correctAnswerType = "Translation";
            }

            String correctAnswer = rowData.get(correctAnswerIndex); // Take either Definition or Translation as the correct answer

            List<String> wrongAnswers = new ArrayList<>();

            // Collect wrong answers from up to 5 neighboring rows (excluding the current row)
            for (int j = randomRowIndex - 1; j >= Math.max(1, randomRowIndex - 5); j--) {
                if (!rowUsed[j]) {
                    List<String> neighborRow = flashcardTable.get(j);
                    String neighborWrongAnswer = neighborRow.get(correctAnswerIndex);
                    if (!neighborWrongAnswer.isEmpty() && !wrongAnswers.contains(neighborWrongAnswer)) {
                        wrongAnswers.add(neighborWrongAnswer);
                    }
                    if (wrongAnswers.size() >= 3) {
                        break; // Stop collecting wrong answers after 3 are found
                    }
                }
            }

            // Add the correct answer
            wrongAnswers.add(correctAnswer);

            // Shuffle the wrong answers
            Collections.shuffle(wrongAnswers);

            // Create a ModelMultipleChoice object and add it to the list
            ModelMultipleChoice multipleChoice = new ModelMultipleChoice(question, getOption(wrongAnswers, 0), getOption(wrongAnswers, 1), getOption(wrongAnswers, 2), correctAnswer, 4);
            questionList.add(multipleChoice);

            // Print the generated question and options in CMD
            System.out.println("Question: " + question);
            System.out.println("Options:");
            for (int k = 0; k < wrongAnswers.size(); k++) {
                System.out.println((k + 1) + ". " + wrongAnswers.get(k));
            }

            // Print the position of the correct answer
            System.out.println("Correct Answer Type: " + correctAnswerType);
            System.out.println("Correct Answer Position: " + (wrongAnswers.indexOf(correctAnswer) + 1));
            System.out.println();
        }

        return questionList;
    }

    private String getOption(List<String> options, int index) {
        if (index < options.size()) {
            return options.get(index);
        }
        return "";
    }

    public void confirmExitDialog() {
        Dialog dialog = Utils.createDialog(this, R.layout.dialog_confirm_exit, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT), Gravity.BOTTOM);
        DialogConfirmExitBinding dialogBinding = DialogConfirmExitBinding.inflate(LayoutInflater.from(this));
        Objects.requireNonNull(dialog).setContentView(dialogBinding.getRoot());
        dialog.show();

        dialogBinding.confirmExitCancelTv.setOnClickListener(v -> dialog.dismiss());
        dialogBinding.confirmExitConfirmTv.setOnClickListener(v -> {
            this.finish();
            dialog.dismiss();
        });
    }

    @Override
    public void onBackPressed() {
        confirmExitDialog();
    }
}
