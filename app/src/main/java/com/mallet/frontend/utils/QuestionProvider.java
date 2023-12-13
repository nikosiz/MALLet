package com.mallet.frontend.utils;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.mallet.backend.exception.MalletException;
import com.mallet.frontend.model.question.ModelAnswer;
import com.mallet.frontend.model.flashcard.ModelFlashcard;
import com.mallet.frontend.model.question.ModelSingleChoice;
import com.mallet.frontend.model.question.ModelTrueFalse;
import com.mallet.frontend.model.question.ModelWritten;
import com.mallet.frontend.model.question.AnswerType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class QuestionProvider {

    private QuestionProvider() {}

    private static final Random random = new Random();

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static List<ModelSingleChoice> generateSingleChoiceQuestions(int maxPerType, List<ModelFlashcard> flashcardList) {
        int maxQuestions = Math.min(maxPerType, flashcardList.size());
        int questionCounter = 1;

        // Inicjalizacja listy wynikowej pytań wielokrotnego wyboru
        List<ModelSingleChoice> result = new ArrayList<>();
        // Przetasowanie listy fiszek
        Collections.shuffle(flashcardList);

        // Sprawdzenie, czy należy uwzględnić pytania związane z definicjami
        boolean includeDefinitionType = shouldIncludeDefinitionType(flashcardList);
        for (ModelFlashcard flashcard : flashcardList) {
            if (questionCounter > maxQuestions) {
                return result;
            }

            // losowanie jakiego typu ma byc pytanie (definicja, term, translation)
            AnswerType answerType = generateRandomQuestionType(includeDefinitionType);

            //  poprawna odpowiedz na podstawie questionType
            Optional<String> correctAnswer = determineResultQuestionField(flashcard, answerType);

            // jesli question jest pusty (jedyny przypadek jak definicja jest pusta) to skipujemy ta iteracje
            if (correctAnswer.isEmpty()) {
                continue;
            }

            // wszystkie pytania z flashcardList danego typu (questionType)
            List<String> allQuestions = getAllQuestions(flashcardList, answerType);

            // losowanie 3 złych odpowiedzi
            List<String> wrongAnswers = getWrongAnswers(allQuestions, correctAnswer.get());

            // determinowanie pytania na podstawie tego jakiego typu sa odpowiedzi oraz czy brac pod uwage definicje
            String question = mapFlashcardToQuestionType(flashcard, answerType, includeDefinitionType);

            //budowanie ModelSingleChoice na podstawie
            // question - pytanie
            // correctAnswer - poprawna odpowiedz
            // wrongAnswers - niepoprawne odpowiedzi
            ModelSingleChoice modelSingleChoice = buildModelMultipleChoice(question, correctAnswer.get(), wrongAnswers);
            result.add(modelSingleChoice);

            questionCounter++;
        }
        return result;
    }

    private static AnswerType generateRandomQuestionType(boolean includeDefinitionType) {
        if (includeDefinitionType) {
            return AnswerType.randomType();
        }
        return AnswerType.randomTypeWithoutDefinitionType();
    }

    private static boolean shouldIncludeDefinitionType(List<ModelFlashcard> flashcardList) {
        long definitionCount = flashcardList.stream()
                .map(ModelFlashcard::getDefinition)
                .filter(Objects::nonNull)
                .filter(definition -> !definition.isEmpty())
                .count();

        return (double) definitionCount / (double) flashcardList.size() >= 0.5;
    }

    private static ModelSingleChoice buildModelMultipleChoice(String question,
                                                              String correctAnswer,
                                                              List<String> wrongAnswers) {
        getAllModelAnswers(correctAnswer, wrongAnswers);

        return ModelSingleChoice.builder()
                .question(question)
                .answers(getAllModelAnswers(correctAnswer, wrongAnswers))
                .build();
    }

    private static Set<ModelAnswer> getAllModelAnswers(String
                                                               correctAnswer, List<String> wrongAnswers) {
        ModelAnswer correctModelAnswer = buildModelAnswer(correctAnswer, true);
        Set<ModelAnswer> answers = wrongAnswers.stream()
                .map(wrongAnswer -> buildModelAnswer(wrongAnswer, false))
                .collect(Collectors.toSet());
        answers.add(correctModelAnswer);

        return answers;
    }

    private static ModelAnswer buildModelAnswer(String answer,
                                                boolean isCorrect) {
        return ModelAnswer.builder()
                .answer(answer)
                .isCorrect(isCorrect)
                .build();
    }

    private static List<String> getAllQuestions(List<ModelFlashcard> flashcardList, AnswerType answerType) {
        return switch (answerType) {
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

    private static String mapFlashcardToQuestionType(ModelFlashcard correctQuestion,
                                                     AnswerType answerType,
                                                     boolean includeDefinitionType) {
        return switch (answerType) {
            case DEFINITION, TRANSLATION -> correctQuestion.getTerm();
            case TERM -> getRandomQuestionForTermType(correctQuestion, includeDefinitionType);
        };
    }

    private static String getRandomQuestionForTermType(ModelFlashcard correctQuestion, boolean includeDefinitionType) {
        if (!includeDefinitionType) {
            return correctQuestion.getTranslation();
        }
        int randomInt = random.nextInt(2);

        return switch (randomInt) {
            case 0 -> correctQuestion.getTranslation();
            case 1 -> {
                String definition = correctQuestion.getDefinition();
                if (Objects.isNull(definition) || definition.isEmpty()) {
                    yield correctQuestion.getTranslation();
                }
                yield definition;
            }
            default -> throw new MalletException("Unexpected value: " + randomInt);
        };
    }

    private static Optional<String> determineResultQuestionField(ModelFlashcard question, AnswerType answerType) {
        return switch (answerType) {
            case DEFINITION ->
                    Objects.isNull(question.getDefinition()) ? Optional.empty() : Optional.of(question.getDefinition());
            case TERM -> Optional.of(question.getTerm());
            case TRANSLATION -> Optional.of(question.getTranslation());
        };
    }

    private static List<String> getWrongAnswers(List<String> questions, String correctQuestion) {
        List<String> questionsWithoutCurrent = new ArrayList<>(questions);
        questionsWithoutCurrent.remove(correctQuestion);
        List<String> notEmptyQuestionsWithoutCurrent = questionsWithoutCurrent.stream()
                .filter(Objects::nonNull)
                .filter(question -> !question.isEmpty())
                .collect(Collectors.toList());

        List<String> wrongQuestions = new ArrayList<>();

        IntStream.range(0, 3)
                .forEach(integer -> getRandomWrongQuestion(notEmptyQuestionsWithoutCurrent, wrongQuestions));

        return wrongQuestions;
    }

    private static void getRandomWrongQuestion(List<String> questionsWithoutCurrent, List<String> wrongQuestions) {
        int wrongQuestionIndex = random.nextInt(questionsWithoutCurrent.size());
        String wrongQuestion = questionsWithoutCurrent.get(wrongQuestionIndex);
        wrongQuestions.add(wrongQuestion);
        questionsWithoutCurrent.remove(wrongQuestion);
    }

    public static List<ModelWritten> generateWrittenQuestions(int maxPerType, List<ModelFlashcard> flashcardList) {
        List<ModelWritten> questionList = new ArrayList<>();
        Random random = new Random();

        // Tasowanie listy fiszek
        List<ModelFlashcard> shuffledFlashcardList = new ArrayList<>(flashcardList);
        Collections.shuffle(shuffledFlashcardList);

        // Ograniczenie liczby pytań do maksymalnej liczby lub liczby fiszek
        int MAX_QUESTIONS = Math.min(maxPerType, shuffledFlashcardList.size());
        int questionCount = 0;

        // Pętla generująca pytania
        for (ModelFlashcard flashcard : shuffledFlashcardList) {
            if (questionCount >= MAX_QUESTIONS) {
                break;
            }

            // Losowanie pozycji pytania pisemnego i poprawnej odpowiedzi
            int writtenQuestionPosition = random.nextInt(1);
            int writtenCorrectAnswerPosition = (writtenQuestionPosition + 1) % 2;

            // Przygotowanie listy opcji odpowiedzi
            List<String> options = new ArrayList<>();
            options.add(flashcard.getTerm());
            options.add(flashcard.getTranslation());
            Collections.shuffle(options);

            // Wybór poprawnej odpowiedzi w zależności od wylosowanej pozycji pytania
            switch (writtenQuestionPosition) {
                case 0 -> {
                    writtenCorrectAnswerPosition = 1;
                }
                case 1 -> {
                    writtenCorrectAnswerPosition = 0;
                }
            }

            // Tworzenie obiektu pytania pisemnego i dodawanie do listy
            ModelWritten written = new ModelWritten(
                    options.get(writtenQuestionPosition),
                    options.get(writtenCorrectAnswerPosition));

            // Pominięcie pustych pytań lub odpowiedzi
            if (written.getQuestion().isEmpty() || written.getCorrectAnswer().isEmpty()) {
                continue;
            }
            questionList.add(written);

            questionCount++;
        }

        return questionList;
    }


    public static List<ModelTrueFalse> generateTrueFalseQuestions(int maxPerType, List<ModelFlashcard> flashcardTable) {
        List<List<String>> flashcardList = getLearningSetDataAsList(flashcardTable); // Assume getLearningSetDataAsList() returns a 2D list

        List<ModelTrueFalse> trueFalseList = new ArrayList<>();
        Random random = new Random();
        int correctAnswerCount = 0;
        int MAX_QUESTIONS = 0;

        while (correctAnswerCount < 5 && MAX_QUESTIONS < maxPerType) {
            int questionRow = random.nextInt(flashcardList.size());
            int answerRow = getRandomNeighborRow(questionRow, flashcardList.size());

            List<String> questionCard = flashcardList.get(questionRow);
            List<String> answerCard = flashcardList.get(answerRow);

            AnswerType answerType = AnswerType.randomType();

            String question = getRandomQuestionContent(questionCard, answerType);
            String answer = getRandomAnswerContent(answerCard, answerType);

            if (Objects.isNull(question) || Objects.isNull(answer) || question.isEmpty() || answer.isEmpty()) {
                continue;
            }

            boolean isAnswerCorrect = questionRow == answerRow;
            if (isAnswerCorrect) {
                correctAnswerCount++;
            }

            ModelTrueFalse trueFalse = new ModelTrueFalse(question, answer, isAnswerCorrect);

            trueFalseList.add(trueFalse);

            MAX_QUESTIONS++;
        }

        return trueFalseList;
    }


    private static String getRandomQuestionContent(List<String> flashcard, AnswerType type) {
        return switch (type) {
            case TERM -> flashcard.get(0);
            case DEFINITION -> flashcard.get(1);
            case TRANSLATION -> flashcard.get(2);
        };
    }

    private static String getRandomAnswerContent(List<String> flashcard, AnswerType type) {
        return switch (type) {
            case TERM -> flashcard.get(1);
            case DEFINITION, TRANSLATION -> flashcard.get(0);
        };
    }

    private  static int getRandomNeighborRow(int currentRow, int totalRows) {
        // Ensure the neighboring row is within the valid range
        int neighborRow = currentRow + random.nextInt(11) - 5;
        return Math.max(0, Math.min(totalRows - 1, neighborRow));
    }

    private static List<List<String>> getLearningSetDataAsList(List<ModelFlashcard> flashcardList) {
        List<List<String>> flashcardTable = new ArrayList<>();

        for (ModelFlashcard flashcard : flashcardList) {
            List<String> row = new ArrayList<>();
            row.add(flashcard.getTerm());
            row.add(flashcard.getDefinition());
            row.add(flashcard.getTranslation());

            flashcardTable.add(row);
        }

        return flashcardTable;
    }


}
