package com.mallet;

import static org.assertj.core.api.Assertions.assertThat;

import com.mallet.frontend.model.question.ModelAnswer;
import com.mallet.frontend.model.flashcard.ModelFlashcard;
import com.mallet.frontend.model.question.ModelSingleChoice;
import com.mallet.frontend.model.question.ModelTrueFalse;
import com.mallet.frontend.model.question.ModelWritten;
import com.mallet.frontend.utils.QuestionProvider;

import org.junit.Test;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class QuestionProviderTest {

    @Test
    public void testGenerateMultipleChoiceQuestions() {
        List<ModelFlashcard> modelFlashcards = generateFlashcards(1, 112);
        int maxPerType = getNumberOfQuestions(modelFlashcards);

        List<ModelSingleChoice> generatedQuestions = QuestionProvider.generateSingleChoiceQuestions(maxPerType, modelFlashcards);

        assertThat(generatedQuestions)
                .filteredOn(getModelMultipleChoiceAssertPredicate())
                .hasSizeGreaterThanOrEqualTo(20);
    }

    @Test
    public void testGenerateMultipleChoiceQuestionsWithEmptyDefinitions() {
        List<ModelFlashcard> modelFlashcards = generateFlashcardsWithEmptyDefinition();
        int maxPerType = getNumberOfQuestions(modelFlashcards);

        List<ModelSingleChoice> generatedQuestions = QuestionProvider.generateSingleChoiceQuestions(maxPerType, modelFlashcards);

        assertThat(generatedQuestions)
                .filteredOn(getModelMultipleChoiceAssertPredicate())
                .hasSizeGreaterThanOrEqualTo(10);
    }

    @Test
    public void testGenerateTrueFalseQuestions() {
        List<ModelFlashcard> modelFlashcards = generateFlashcards(1, 30);
        int maxPerType = getNumberOfQuestions(modelFlashcards);

        List<ModelTrueFalse> generatedQuestions = QuestionProvider.generateTrueFalseQuestions(maxPerType, modelFlashcards);

        assertThat(generatedQuestions)
                .filteredOn(getModelTrueFalseAssertPredicate())
                .hasSizeGreaterThanOrEqualTo(maxPerType);
    }

    @Test
    public void testGenerateTrueFalseQuestionsWithEmptyDefinition() {
        List<ModelFlashcard> modelFlashcards = generateFlashcardsWithEmptyDefinition();
        int maxPerType = getNumberOfQuestions(modelFlashcards);

        List<ModelTrueFalse> generatedQuestions = QuestionProvider.generateTrueFalseQuestions(maxPerType, modelFlashcards);

        assertThat(generatedQuestions)
                .filteredOn(getModelTrueFalseAssertPredicate())
                .hasSizeGreaterThanOrEqualTo(10);
    }

    @Test
    public void testGenerateWrittenQuestions() {
        List<ModelFlashcard> modelFlashcards = generateFlashcards(2, 54);
        int maxPerType = getNumberOfQuestions(modelFlashcards);

        List<ModelWritten> generatedQuestions = QuestionProvider.generateWrittenQuestions(maxPerType, modelFlashcards);

        assertThat(generatedQuestions)
                .filteredOn(getModelWrittenAssertPredicate())
                .hasSizeGreaterThanOrEqualTo(maxPerType);
    }

    @Test
    public void testGenerateWrittenQuestionsEmptyDefinition() {
        List<ModelFlashcard> modelFlashcards = generateFlashcardsWithEmptyDefinition();
        int maxPerType = getNumberOfQuestions(modelFlashcards);

        List<ModelWritten> generatedQuestions = QuestionProvider.generateWrittenQuestions(maxPerType, modelFlashcards);

        assertThat(generatedQuestions)
                .allMatch(getModelWrittenAssertPredicate())
                .filteredOn(getModelWrittenAssertPredicate())
                .hasSizeGreaterThanOrEqualTo(10);
    }

    private Predicate<ModelWritten> getModelWrittenAssertPredicate() {
        return model -> {
            boolean objectsNonNull = Objects.nonNull(model) && Objects.nonNull(model.getQuestion()) && Objects.nonNull(model.getCorrectAnswer());
            return objectsNonNull && !model.getQuestion().isEmpty() && !model.getCorrectAnswer().isEmpty();
        };
    }

    private Predicate<ModelTrueFalse> getModelTrueFalseAssertPredicate() {
        return model -> {
            boolean objectsNonNull = Objects.nonNull(model) && Objects.nonNull(model.getQuestion());
            return objectsNonNull && !model.getQuestion().isEmpty() && !model.getDisplayedAnswer().isEmpty();
        };
    }

    private Predicate<ModelSingleChoice> getModelMultipleChoiceAssertPredicate() {
        return question -> {
            boolean objectsNonNull = Objects.nonNull(question) && Objects.nonNull(question.getQuestion()) && Objects.nonNull(question.getAnswers());
            Set<ModelAnswer> answers = question.getAnswers();
            boolean containsNonEmptyAnswers = answers.stream()
                    .noneMatch(answer -> Objects.isNull(answer) && answer.getAnswer().isEmpty());
            return objectsNonNull && containsNonEmptyAnswers && !question.getQuestion().isEmpty();
        };
    }

    private int getNumberOfQuestions(List<ModelFlashcard> flashcardList) {
        if (flashcardList.size() < 5) {
            return 10;
        } else {
            return Math.min(20, flashcardList.size());
        }
    }

    private List<ModelFlashcard> generateFlashcardsWithEmptyDefinition() {
        return IntStream.range(1, 11)
                .mapToObj(value -> generateFlashcardWithEmptyDefinition(String.valueOf(value)))
                .collect(Collectors.toList());
    }

    private ModelFlashcard generateFlashcardWithEmptyDefinition(String value) {
        return new ModelFlashcard("Term" + value, null, "Translation" + value);
    }

    private List<ModelFlashcard> generateFlashcards(int startInclusive, int endExclusive) {
        return IntStream.range(startInclusive, endExclusive)
                .mapToObj(value -> generateFlashcard(String.valueOf(value)))
                .collect(Collectors.toList());
    }

    private ModelFlashcard generateFlashcard(String value) {
        return new ModelFlashcard("Term" + value, "Definition" + value, "Translation" + value);
    }

}
