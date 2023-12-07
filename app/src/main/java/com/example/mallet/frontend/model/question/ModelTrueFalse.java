package com.example.mallet.frontend.model.question;

public class ModelTrueFalse {
    private final String question;
    private final String displayedAnswer;
    private final boolean isAnswerCorrect;

    public ModelTrueFalse(String question, String displayedAnswer, boolean isAnswerCorrect) {
        this.question = question;
        this.displayedAnswer = displayedAnswer;
        this.isAnswerCorrect = isAnswerCorrect;
    }

    public String getQuestion() {
        return question;
    }

    public String getDisplayedAnswer() {
        return displayedAnswer;
    }

    public boolean getCorrectAnswer() {
        return isAnswerCorrect;
    }

    @Override
    public String toString() {
        return "Question: " + question + "\n" +
                "Displayed answer: " + displayedAnswer +
                "Is answer correct: " + isAnswerCorrect + "\n";
    }
}