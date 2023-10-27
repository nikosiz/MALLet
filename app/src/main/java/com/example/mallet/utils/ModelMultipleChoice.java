package com.example.mallet.utils;

public class ModelMultipleChoice {
    private final String question;
    private final String correctAnswer;
    private final String alternativeAnswer;
    private String wrongAnswer1;
    private String wrongAnswer2;
    private String wrongAnswer3;

    public ModelMultipleChoice(String question, String correctAnswer, String alternativeAnswer) {
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.alternativeAnswer = alternativeAnswer;
        this.wrongAnswer1 = wrongAnswer1;
        this.wrongAnswer2 = wrongAnswer2;
        this.wrongAnswer3 = wrongAnswer3;
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

    public String getWrongAnswer1() {
        return wrongAnswer1;
    }

    public String getWrongAnswer2() {
        return wrongAnswer2;
    }

    public String getWrongAnswer3() {
        return wrongAnswer3;
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