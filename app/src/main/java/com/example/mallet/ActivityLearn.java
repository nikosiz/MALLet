package com.example.mallet;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.mallet.databinding.ActivityLearnBinding;
import com.example.mallet.databinding.DialogConfirmExitBinding;
import com.example.mallet.utils.ModelFlashcard;
import com.example.mallet.utils.ModelLearningSet;
import com.example.mallet.utils.ModelMultipleChoice;
import com.example.mallet.utils.ModelWritten;
import com.example.mallet.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;

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
    private final int currentQuestionIndex = 0;


    // Written questions
    private View writtenQuestionView;
    private TextView writtenQuestionTv;
    private TextInputEditText writtenAnswerEt;
    private String writtenAnswer;
    private List<ModelWritten> writtenQuestions;
    private int writtenCorrectAnswerPosition, writtenAlternativeAnswerPosition;
    private TextView correctAnswersTv;

    // Multiple choice questions
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLearnBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupContents();

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
    }

    public List<ModelWritten> generateWrittenQuestions() {
        List<ModelWritten> questionList = new ArrayList<>();
        Random random = new Random();

        // Shuffle the flashcardList to randomize the order of elements
        List<ModelFlashcard> shuffledFlashcardList = new ArrayList<>(flashcardList);
        Collections.shuffle(shuffledFlashcardList);

        int MAX_QUESTIONS = 20;
        int questionCount = 0; // Initialize the question count

        // Iterate through the shuffled flashcardList
        for (ModelFlashcard flashcard : shuffledFlashcardList) {
            // Check if the maximum number of questions (20) has been generated
            if (questionCount >= MAX_QUESTIONS) {
                break;
            }

            // Randomly select the position for the written question
            int writtenQuestionPosition = random.nextInt(2);

            List<String> options = new ArrayList<>();
            options.add(flashcard.getTerm());
            options.add(flashcard.getDefinition());
            options.add(flashcard.getTranslation());
            Collections.shuffle(options);


            // Determine the positions of the correct and alternative answers
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

            // Create a ModelWritten object with the selected options
            ModelWritten written = new ModelWritten(options.get(writtenQuestionPosition), options.get(writtenCorrectAnswerPosition), options.get(writtenAlternativeAnswerPosition));

            // Add the written question to the list
            questionList.add(written);
            System.out.println(written);

            questionCount++; // Increment the question count
        }

        return questionList;
    }

    public List<ModelMultipleChoice> generateMultipleChoiceQuestions() {
        if (flashcardList.size() < 20) {
            flashcardTable = createFlashcardTable(flashcardList);

            List<ModelMultipleChoice> questionList = new ArrayList();

// Check if the table is empty
            if (flashcardTable.isEmpty() || flashcardTable.get(0).isEmpty()) {
                System.out.println("Table is empty. No questions to generate.");
                return questionList;
            }

// Get the header row to access column names
            List<String> headerRow = flashcardTable.get(0);
            int termIndex = headerRow.indexOf("Term");
            int definitionIndex = headerRow.indexOf("Definition");
            int translationIndex = headerRow.indexOf("Translation");

// Iterate over the data rows (skip the header row)
            for (int i = 1; i < flashcardTable.size(); i++) {
                List<String> rowData = flashcardTable.get(i);

                String question = rowData.get(termIndex); // Take the Term as the question
                String correctAnswerType;
                String answer;

                if (question.equals(rowData.get(termIndex)) && !rowData.get(definitionIndex).isEmpty() && !rowData.get(translationIndex).isEmpty()) {
                    // Always set answer type to "Definition" for consistency
                    correctAnswerType = "Definition";
                    answer = rowData.get(definitionIndex);
                } else if (question.equals(rowData.get(definitionIndex)) && !rowData.get(termIndex).isEmpty()) {
                    // Always set answer type to "Term" for consistency
                    correctAnswerType = "Term";
                    answer = rowData.get(termIndex);
                } else if (question.equals(rowData.get(translationIndex)) && !rowData.get(termIndex).isEmpty()) {
                    // Always set answer type to "Term" when question is "Translation"
                    correctAnswerType = "Term";
                    answer = rowData.get(termIndex);
                } else {
                    // If none of the conditions are met, use default values
                    correctAnswerType = "Definition";
                    answer = rowData.get(definitionIndex);
                }

                List<String> wrongAnswers = new ArrayList<>();

                // Collect wrong answers from up to 5 neighboring rows (excluding the current row)
                for (int j = i - 1; j >= Math.max(1, i - 5); j--) {
                    if (j < i) {
                        // Collect from upper rows
                        List<String> neighborRow = flashcardTable.get(j);
                        String neighborWrongAnswer = neighborRow.get(definitionIndex);
                        if (!neighborWrongAnswer.isEmpty() && !wrongAnswers.contains(neighborWrongAnswer)) {
                            wrongAnswers.add(neighborWrongAnswer);
                        }
                    }
                    if (wrongAnswers.size() >= 3) {
                        break; // Stop collecting wrong answers after 3 are found
                    }
                }

                for (int j = i + 1; j <= Math.min(flashcardTable.size() - 1, i + 5); j++) {
                    // Collect from lower rows
                    List<String> neighborRow = flashcardTable.get(j);
                    String neighborWrongAnswer = neighborRow.get(definitionIndex);
                    if (!neighborWrongAnswer.isEmpty() && !wrongAnswers.contains(neighborWrongAnswer)) {
                        wrongAnswers.add(neighborWrongAnswer);
                    }
                    if (wrongAnswers.size() >= 3) {
                        break; // Stop collecting wrong answers after 3 are found
                    }
                }

                // Add the correct answer
                wrongAnswers.add(answer);

                // Shuffle the wrong answers
                Collections.shuffle(wrongAnswers);

                // Ensure there are always 4 answers of the same type
                while (wrongAnswers.size() < 4) {
                    wrongAnswers.add(answer);
                }

                // Create a ModelMultipleChoice object and add it to the list
                ModelMultipleChoice multipleChoice = new ModelMultipleChoice(question, getOption(wrongAnswers, 0), getOption(wrongAnswers, 1), getOption(wrongAnswers, 2), answer, 4);
                questionList.add(multipleChoice);

                // Print the generated question and options in CMD
                System.out.println("Question: " + question);
                System.out.println("Options:");
                for (int k = 0; k < wrongAnswers.size(); k++) {
                    System.out.println((k + 1) + ". " + wrongAnswers.get(k));
                }

                // Display the correct answer position
                int correctAnswerPosition = wrongAnswers.indexOf(answer) + 1;
                System.out.println("Correct Answer: " + answer);
                System.out.println("Correct Answer Position: " + correctAnswerPosition);
                System.out.println("Answer Type: " + correctAnswerType);

                System.out.println();
            }

            return questionList;


        } else {


            flashcardTable = createFlashcardTable(flashcardList);

            List<ModelMultipleChoice> questionList = new ArrayList<>();
            Random random = new Random();

            // Check if the table is empty or if it has less than 20 rows
            if (flashcardTable.isEmpty() || flashcardTable.size() <= 20) {
                System.out.println("Table does not have enough data to generate questions");
                return questionList;
            }

            // Get the header row to access column names
            List<String> headerRow = flashcardTable.get(0);
            int termIndex = headerRow.indexOf("Term");
            int definitionIndex = headerRow.indexOf("Definition");
            int translationIndex = headerRow.indexOf("Translation");

            // Limit the number of questions to generate to 20 or the number of available rows (excluding the header)
            int MAX_QUESTIONS = Math.min(20, flashcardTable.size() - 1);

            // Create an array to track which rows have been used
            boolean[] rowUsed = new boolean[flashcardTable.size()];

            // Generate and display questions
            for (int questionCount = 0; questionCount < MAX_QUESTIONS; questionCount++) {
                int randomRowIndex;

                // Select a random unused row
                do {
                    randomRowIndex = random.nextInt(flashcardTable.size() - 1) + 1; // Skip the header row
                } while (rowUsed[randomRowIndex]);

                rowUsed[randomRowIndex] = true;

                List<String> rowData = flashcardTable.get(randomRowIndex);

                String question = rowData.get(termIndex); // Take the Term as the question

                int termOrDefinition = random.nextInt(1);

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

                // Ensure there are at least 3 unique wrong answers
                while (wrongAnswers.size() < 3) {
                    int randomRowIndexForUniqueWrongAnswer;

                    // Select a random unused row for unique wrong answer
                    do {
                        randomRowIndexForUniqueWrongAnswer = random.nextInt(flashcardTable.size() - 1) + 1; // Skip the header row
                    } while (rowUsed[randomRowIndexForUniqueWrongAnswer]);

                    rowUsed[randomRowIndexForUniqueWrongAnswer] = true;

                    List<String> neighborRow = flashcardTable.get(randomRowIndexForUniqueWrongAnswer);
                    String neighborWrongAnswer = neighborRow.get(correctAnswerIndex);

                    if (!neighborWrongAnswer.isEmpty() && !wrongAnswers.contains(neighborWrongAnswer)) {
                        wrongAnswers.add(neighborWrongAnswer);
                    }
                }

                // Add the correct answer
                wrongAnswers.add(correctAnswer);

                // Shuffle the wrong answers
                Collections.shuffle(wrongAnswers);

                int correctAnswerPosition = wrongAnswers.indexOf(correctAnswer);

                // Create a ModelMultipleChoice object and add it to the list
                ModelMultipleChoice multipleChoice = new ModelMultipleChoice(question, getOption(wrongAnswers, 0), getOption(wrongAnswers, 1), getOption(wrongAnswers, 2), correctAnswer, correctAnswerPosition + 1);
                questionList.add(multipleChoice);

                // Print the generated question and options in CMD
                System.out.println("Question: " + question);
                System.out.println("Options:");
                for (int k = 0; k < wrongAnswers.size(); k++) {
                    System.out.println((k + 1) + ". " + wrongAnswers.get(k));
                }

                // Print the position of the correct answer
                System.out.println("Correct Answer Position: " + (wrongAnswers.indexOf(correctAnswer) + 1));
                System.out.println("\n");
            }

            return questionList;
        }
    }


    private String getOption(List<String> options, int index) {
        if (index < options.size()) {
            return options.get(index);
        }
        return "";
    }

    public List<List<String>> createFlashcardTable(List<ModelFlashcard> flashcardList) {
        List<List<String>> flashcardTable = new ArrayList<>();

        // Add a header row with column names
        List<String> headerRow = new ArrayList<>();
        headerRow.add("Term");
        headerRow.add("Definition");
        headerRow.add("Translation");
        flashcardTable.add(headerRow);

        // Populate the table with flashcard data
        for (ModelFlashcard flashcard : flashcardList) {
            List<String> rowData = new ArrayList<>();
            rowData.add(flashcard.getTerm());
            rowData.add(flashcard.getDefinition());
            rowData.add(flashcard.getTranslation());
            flashcardTable.add(rowData);
        }

        return flashcardTable;
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