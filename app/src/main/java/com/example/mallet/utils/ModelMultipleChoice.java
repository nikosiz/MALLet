package com.example.mallet.utils;

public class ModelMultipleChoice {
    // Attributes to define a multiple-choice quiz question
    private String question;
    private String correctAnswer, alternativeAnswer;
    private String wrongAnswer1, wrongAnswer2, wrongAnswer3;

    public ModelMultipleChoice(String question, String correctAnswer, String alternativeAnswer) {
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.alternativeAnswer = alternativeAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getAlternativeAnswer() {
        return alternativeAnswer;
    }

    public void setAlternativeAnswer(String alternativeAnswer) {
        this.alternativeAnswer = alternativeAnswer;
    }

    @Override
    public String toString() {
        return "Multiple choice questions: \n" +
                "Question: " + question + "\n" +
                "Correct answer: " + correctAnswer + "\n" +
                "Alternative answer: " + alternativeAnswer + "\n" +
                "Wrong answer 1: " + "\n" +
                "Wrong answer 2: " + "\n" +
                "Wrong answer 3: " + "\n";
    }


}