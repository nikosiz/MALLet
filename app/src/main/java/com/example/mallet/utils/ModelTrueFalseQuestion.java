package com.example.mallet.utils;

public class ModelTrueFalseQuestion {
    private String question, answer;
    private String term, translation, definition;

    public ModelTrueFalseQuestion(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public boolean isTrue() {
        /* IF (question MATCHES answer)
            return true;
           ELSE
            return false;
        */

        return true;
    }

    public String getQuestionText() {
        // Return the question text to display to the user
        // You can format it as needed based on your examples
        return term + "\n" + definition + "\n" + translation;
    }
}
