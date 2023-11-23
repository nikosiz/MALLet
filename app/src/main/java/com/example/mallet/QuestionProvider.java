package com.example.mallet;

import com.example.mallet.backend.exception.MalletException;
import com.example.mallet.utils.ModelAnswer;
import com.example.mallet.utils.ModelFlashcard;
import com.example.mallet.utils.ModelMultipleChoice;
import com.example.mallet.utils.ModelTrueFalse;
import com.example.mallet.utils.ModelWritten;
import com.example.mallet.utils.QuestionType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class QuestionProvider {

    private QuestionProvider() {
    }

    private static final Random random = new Random();

    public static List<ModelMultipleChoice> generateMultipleChoiceQuestions(int maxPerType, List<ModelFlashcard> flashcardList) {
        int MAX_QUESTIONS = Math.min(maxPerType, flashcardList.size());
        int questionCounter = 1;

        List<ModelMultipleChoice> result = new ArrayList<>();
        Collections.shuffle(flashcardList);

        boolean includeDefinitionType = shouldIncludeDefinitionType(flashcardList);
        for (ModelFlashcard correctQuestion : flashcardList) {
            if (questionCounter > MAX_QUESTIONS) {
                return result;
            }
            QuestionType questionType;
            if (includeDefinitionType) {
                questionType = QuestionType.randomType();

            } else {
                questionType = QuestionType.randomTypeWithoutDefinitionType();
            }
            String currentQuestionSpecificType = determineResultQuestionField(correctQuestion, questionType);

            if (currentQuestionSpecificType.isEmpty()) {
                continue;
            }

            List<String> allQuestions = getAllQuestions(flashcardList, questionType);
            List<String> wrongAnswers = getWrongAnswers(allQuestions, currentQuestionSpecificType);

            ModelMultipleChoice modelMultipleChoice = buildModelMultipleChoice(mapFlashcardToQuestionType(correctQuestion, questionType, includeDefinitionType), currentQuestionSpecificType, wrongAnswers);
            result.add(modelMultipleChoice);

            questionCounter++;
        }
        return result;
    }

    private static boolean shouldIncludeDefinitionType(List<ModelFlashcard> flashcardList) {
        long definitionCount = flashcardList.stream()
                .map(ModelFlashcard::getDefinition)
                .filter(Objects::nonNull)
                .filter(definition -> !definition.isEmpty())
                .count();
        return (double) definitionCount / (double) flashcardList.size() >= 0.5;
    }

    private static ModelMultipleChoice buildModelMultipleChoice(String question,
                                                                String correctAnswer,
                                                                List<String> wrongAnswers) {
        getAllModelAnswers(correctAnswer, wrongAnswers);

        return ModelMultipleChoice.builder()
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

    private static List<String> getAllQuestions(List<ModelFlashcard> flashcardList, QuestionType questionType) {
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

    private static String mapFlashcardToQuestionType(ModelFlashcard correctQuestion,
                                                     QuestionType questionType,
                                                     boolean includeDefinitionType) {
        return switch (questionType) {
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

    private static String determineResultQuestionField(ModelFlashcard question, QuestionType questionType) {
        return switch (questionType) {
            case DEFINITION ->
                    Objects.isNull(question.getDefinition()) ? "" : question.getDefinition();
            case TERM -> question.getTerm();
            case TRANSLATION -> question.getTranslation();
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

        List<ModelFlashcard> shuffledFlashcardList = new ArrayList<>(flashcardList);
        Collections.shuffle(shuffledFlashcardList);

        int MAX_QUESTIONS = Math.min(maxPerType, shuffledFlashcardList.size());
        int questionCount = 0;

        for (ModelFlashcard flashcard : shuffledFlashcardList) {
            if (questionCount >= MAX_QUESTIONS) {
                break;
            }

            int writtenQuestionPosition = random.nextInt(1);
            int writtenCorrectAnswerPosition = (writtenQuestionPosition + 1) % 2;


            List<String> options = new ArrayList<>();
            options.add(flashcard.getTerm());
            options.add(flashcard.getTranslation());
            Collections.shuffle(options);


            switch (writtenQuestionPosition) {
                case 0 -> {
                    writtenCorrectAnswerPosition = 1;
                    //writtenAlternativeAnswerPosition = 2;
                }
                case 1 -> {
                    writtenCorrectAnswerPosition = 0;
                    //writtenAlternativeAnswerPosition = 2;
                }
            }

            ModelWritten written = new ModelWritten(
                    options.get(writtenQuestionPosition),
                    options.get(writtenCorrectAnswerPosition));

            if (written.getQuestion().isEmpty() || written.getCorrectAnswer().isEmpty()) {
                continue;
            }
            questionList.add(written);

            questionCount++;
        }

        return questionList;
    }


    public static List<ModelTrueFalse> generateTrueFalseQuestions(int maxPerType, List<
            ModelFlashcard> flashcardTable) {
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

            String questionType = getRandomType();

            String question = getRandomContent(questionCard, questionType);
            String answer = getRandomContent(answerCard, questionType);

            if (Objects.isNull(question) || Objects.isNull(answer) || question.isEmpty() || answer.isEmpty()) {
                continue;
            }

            boolean isAnswerCorrect = questionRow == answerRow;
            if (isAnswerCorrect) {
                correctAnswerCount++;
            }

            ModelTrueFalse trueFalse = new ModelTrueFalse(question, answer, isAnswerCorrect);

            // System.out.println("Question: " + question);
            // System.out.println("Displayed Answer: " + answer);
            // System.out.println("Is Answer Correct? " + isAnswerCorrect);
            // System.out.println();

            trueFalseList.add(trueFalse);

            MAX_QUESTIONS++;
        }

        return trueFalseList;
    }


    private static String getRandomType() {
        List<String> types = Arrays.asList("term", "definition", "translation");
        Random random = new Random();
        return types.get(random.nextInt(types.size()));
    }

    private static String getRandomContent(List<String> flashcard, String type) {
        return switch (type) {
            case "term" -> flashcard.get(0);
            case "definition" -> flashcard.get(1);
            case "translation" -> flashcard.get(2);
            default -> "";
        };
    }

    private static int getRandomNeighborRow(int currentRow, int totalRows) {
        // Ensure the neighboring row is within the valid range
        int neighborRow = currentRow + random.nextInt(11) - 5;
        return Math.max(0, Math.min(totalRows - 1, neighborRow));
    }

    private static List<List<String>> getLearningSetDataAsList
            (List<ModelFlashcard> flashcardList) {
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
