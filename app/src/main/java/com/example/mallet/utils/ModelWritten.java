package com.example.mallet.utils;

public class ModelWritten {
    private final String question;
    private final String correctAnswer; // Store the actual correct answer content
    private final String alternativeAnswer;

    public ModelWritten(String question, String correctAnswer, String alternativeAnswer) {
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.alternativeAnswer = alternativeAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String getAlternativeAnswer() {
        return alternativeAnswer;
    }

    @Override
    public String toString() {
        return "Question: " + question + "\n" +
                "Correct answer: " + correctAnswer + "\n" +
                "Alternative answer: " + alternativeAnswer;
    }
}
