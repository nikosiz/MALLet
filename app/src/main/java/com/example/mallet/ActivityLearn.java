package com.example.mallet;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.mallet.backend.exception.MalletException;
import com.example.mallet.databinding.ActivityLearnBinding;
import com.example.mallet.databinding.DialogConfirmExitBinding;
import com.example.mallet.utils.ModelAnswer;
import com.example.mallet.utils.ModelFlashcard;
import com.example.mallet.utils.ModelLearningSet;
import com.example.mallet.utils.ModelMultipleChoice;
import com.example.mallet.utils.ModelTrueFalse;
import com.example.mallet.utils.ModelWritten;
import com.example.mallet.utils.QuestionType;
import com.example.mallet.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ActivityLearn extends AppCompatActivity {
    private ActivityLearnBinding binding;
    private String fragmentName;
    private ModelLearningSet learningSet;
    private List<ModelFlashcard> flashcardList;
    private Random random;
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

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                confirmExitDialog();
            }
        };

        // Register the callback with the onBackPressedDispatcher
        this.getOnBackPressedDispatcher().addCallback(this, callback);


        this.random = new Random();
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
                        .commitNow();
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    private void setupContents() {
        fragmentName = getIntent().getStringExtra("fragment_class");

        learningSet = getIntent().getParcelableExtra("learningSet");

        if (learningSet != null) {
            flashcardList = learningSet.getTerms();
        }
    }

    private int trueFalseCorrectAnswerPosition;

    public List<ModelWritten> generateWrittenQuestions() {
        List<ModelWritten> questionList = new ArrayList<>();
        Random random = new Random();

        // Shuffle the flashcardList to randomize the order of elements
        List<ModelFlashcard> shuffledFlashcardList = new ArrayList<>(flashcardList);
        Collections.shuffle(shuffledFlashcardList);

        int MAX_QUESTIONS = Math.min(20, shuffledFlashcardList.size());
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

    public List<ModelTrueFalse> generateTrueFalseQuestions() {
        List<List<String>> flashcardList = getLearningSetDataAsList(); // Assume getLearningSetDataAsList() returns a 2D list

        List<ModelTrueFalse> trueFalseList = new ArrayList<>();
        Random random = new Random();
        int correctAnswerCount = 0;
        int totalQuestions = 0;

        while (correctAnswerCount < 5 && totalQuestions < 20){
            int questionRow = random.nextInt(flashcardList.size());
            int answerRow = getRandomNeighborRow(questionRow, flashcardList.size());

            List<String> questionCard = flashcardList.get(questionRow);
            List<String> answerCard = flashcardList.get(answerRow);

            String questionType = getRandomType();
            String answerType;

            do {
                answerType = getRandomType();
            } while (questionType.equals(answerType));

            String question = getRandomContent(questionCard, questionType);
            String answer = getRandomContent(answerCard, answerType);

            boolean isAnswerCorrect = questionRow == answerRow;

            if (isAnswerCorrect) {
                correctAnswerCount++;
            }

            ModelTrueFalse trueFalse = new ModelTrueFalse(question, answer, isAnswerCorrect);

            // Display the generated true/false statement in the console
            System.out.println("Question: " + question);
            System.out.println("Displayed Answer: " + answer);
            System.out.println("Is Answer Correct? " + isAnswerCorrect);
            System.out.println(); // Add a newline for better readability

            trueFalseList.add(trueFalse);

            totalQuestions++;
        }

        return trueFalseList;
    }

    private String getRandomType() {
        // Add or remove types as needed
        List<String> types = Arrays.asList("term", "definition", "translation");
        Random random = new Random();
        return types.get(random.nextInt(types.size()));
    }

    private String getRandomContent(List<String> flashcard, String type) {
        String content = "";

        switch (type) {
            case "term":
                content = flashcard.get(0);
                break;
            case "definition":
                content = flashcard.get(1);
                break;
            case "translation":
                content = flashcard.get(2);
                break;
            // Add more cases if needed for additional content types
        }

        return content;
    }

    private int getRandomNeighborRow(int currentRow, int totalRows) {
        // Ensure the neighboring row is within the valid range
        int neighborRow = currentRow + random.nextInt(11) - 5;
        return Math.max(0, Math.min(totalRows - 1, neighborRow));
    }

    // Assuming getLearningSetData() returns a List<ModelFlashcard>
    private List<List<String>> getLearningSetDataAsList() {
        flashcardTable = new ArrayList<>();

        for (ModelFlashcard flashcard : flashcardList) {
            List<String> row = new ArrayList<>();
            row.add(flashcard.getTerm());
            row.add(flashcard.getDefinition());
            row.add(flashcard.getTranslation());

            flashcardTable.add(row);
        }

        return flashcardTable;
    }

    List<List<String>> flashcardTable;

    public List<ModelMultipleChoice> generateMultipleChoiceQuestions() {
        int MAX_QUESTIONS = Math.min(20, flashcardList.size());
        int questionCounter = 1;

        List<ModelMultipleChoice> result = new ArrayList<>();
        Collections.shuffle(flashcardList);

        for (ModelFlashcard correctQuestion : flashcardList) {
            if (questionCounter > MAX_QUESTIONS) {
                return result;
            }

            QuestionType questionType = QuestionType.randomType();
            String currentQuestionSpecificType = determineResultQuestionField(correctQuestion, questionType);

            List<String> allQuestions = getAllQuestions(flashcardList, questionType);
            List<String> wrongAnswers = getWrongAnswers(allQuestions, currentQuestionSpecificType);

            ModelMultipleChoice modelMultipleChoice = buildModelMultipleChoice(mapFlashcardToQuestionType(correctQuestion, questionType), currentQuestionSpecificType, wrongAnswers);
            result.add(modelMultipleChoice);

            questionCounter++;
        }
        return result;
    }

    private ModelMultipleChoice buildModelMultipleChoice(String question,
                                                         String correctAnswer,
                                                         List<String> wrongAnswers) {
        getAllModelAnswers(correctAnswer, wrongAnswers);

        return ModelMultipleChoice.builder()
                .question(question)
                .answers(getAllModelAnswers(correctAnswer, wrongAnswers))
                .build();
    }

    private Set<ModelAnswer> getAllModelAnswers(String correctAnswer, List<String> wrongAnswers) {
        ModelAnswer correctModelAnswer = buildModelAnswer(correctAnswer, true);
        Set<ModelAnswer> answers = wrongAnswers.stream()
                .map(wrongAnswer -> buildModelAnswer(wrongAnswer, false))
                .collect(Collectors.toSet());
        answers.add(correctModelAnswer);

        return answers;
    }

    private ModelAnswer buildModelAnswer(String answer,
                                         boolean isCorrect) {
        return ModelAnswer.builder()
                .answer(answer)
                .isCorrect(isCorrect)
                .build();
    }

    private List<String> getAllQuestions(List<ModelFlashcard> flashcardList, QuestionType questionType) {
        return switch (questionType) {
            case TERM -> flashcardList.stream()
                    .map(ModelFlashcard::getTerm)
                    .collect(Collectors.toList());
            case DEFINITION -> flashcardList.stream()
                    .map(ModelFlashcard::getDefinition)
                    .collect(Collectors.toList());
            case TRANSLATION -> flashcardList.stream()
                    .map(ModelFlashcard::getTranslation)
                    .collect(Collectors.toList());
        };
    }

    private String mapFlashcardToQuestionType(ModelFlashcard correctQuestion, QuestionType questionType) {
        return switch (questionType) {
            case DEFINITION -> correctQuestion.getTerm();
            case TERM -> getRandomQuestionForTermType(correctQuestion);
            case TRANSLATION -> correctQuestion.getTerm();
        };
    }

    private String getRandomQuestionForTermType(ModelFlashcard correctQuestion) {
        int randomInt = random.nextInt(2);

        return switch (randomInt) {
            case 0 -> correctQuestion.getTranslation();
            case 1 -> correctQuestion.getDefinition();
            default -> throw new MalletException("Unexpected value: " + randomInt);
        };
    }

    private String determineResultQuestionField(ModelFlashcard question, QuestionType questionType) {
        return switch (questionType) {
            case DEFINITION -> question.getDefinition();
            case TERM -> question.getTerm();
            case TRANSLATION -> question.getTranslation();
        };
    }

    private List<String> getWrongAnswers(List<String> questions, String correctQuestion) {
        List<String> questionsWithoutCurrent = new ArrayList<>(questions);
        questionsWithoutCurrent.remove(correctQuestion);

        List<String> wrongQuestions = new ArrayList<>();

        IntStream.range(0, 3)
                .forEach(integer -> getRandomWrongQuestion(questionsWithoutCurrent, wrongQuestions));

        return wrongQuestions;
    }

    private void getRandomWrongQuestion(List<String> questionsWithoutCurrent, List<String> wrongQuestions) {
        int wrongQuestionIndex = random.nextInt(questionsWithoutCurrent.size());
        String wrongQuestion = questionsWithoutCurrent.get(wrongQuestionIndex);
        wrongQuestions.add(wrongQuestion);
        questionsWithoutCurrent.remove(wrongQuestion);
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
}