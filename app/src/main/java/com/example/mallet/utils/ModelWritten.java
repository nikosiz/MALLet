package com.example.mallet.utils;

public class ModelWritten {
    private final String question;
    private final String correctAnswer;

    public ModelWritten(String question, String correctAnswer) {
        this.question = question;
        this.correctAnswer = correctAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }


    @Override
    public String toString() {
        return "Written: \n" +
                "Question: " + question + "\n" +
                "Correct answer: " + correctAnswer + "\n";
    }
}
