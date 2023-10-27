package com.example.mallet.utils;

public class ModelMultipleChoice {
    private String question;
    private String correctAnswer, alternativeAnswer;
    private String wrongAnswer1, wrongAnswer2, wrongAnswer3;

    public ModelMultipleChoice(String question, String correctAnswer, String alternativeAnswer, String wrongAnswer1, String wrongAnswer2, String wrongAnswer3) {
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

    public String getWrongAnswer1() {
        return wrongAnswer1;
    }

    public void setWrongAnswer1(String wrongAnswer1) {
        this.wrongAnswer1 = wrongAnswer1;
    }

    public String getWrongAnswer2() {
        return wrongAnswer2;
    }

    public void setWrongAnswer2(String wrongAnswer2) {
        this.wrongAnswer2 = wrongAnswer2;
    }

    public String getWrongAnswer3() {
        return wrongAnswer3;
    }

    public void setWrongAnswer3(String wrongAnswer3) {
        this.wrongAnswer3 = wrongAnswer3;
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