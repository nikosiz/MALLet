package com.example.mallet.utils;

public class ModelTrueFalse {
    private final String question;
    private final String displayedAnswer;
    private final String correctAnswer;

    public ModelTrueFalse(String question, String displayedAnswer, String correctAnswer) {
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.displayedAnswer = displayedAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public String getDisplayedAnswer() {
        return displayedAnswer;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    @Override
    public String toString() {
        return "Question: " + question + "\n" +
                "Correct answer: " + correctAnswer + "\n" +
                "Displayed answer: " + displayedAnswer;
    }
}
