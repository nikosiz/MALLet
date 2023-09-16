package com.example.mallet.utils;

public class ModelMultipleChoiceQuestion {
    // Attributes to define a multiple-choice quiz question
    private String question;
    private String answerA, answerB, answerC, answerD;
    private String correctAnswer;

    // Constructor to initialize a multiple-choice quiz question
    public ModelMultipleChoiceQuestion(String question, String answerA, String answerB, String answerC, String answerD, String correctAnswer) {
        this.question = question;
        this.answerA = answerA;
        this.answerB = answerB;
        this.answerC = answerC;
        this.answerD = answerD;
        this.correctAnswer = correctAnswer;
    }

    // Getter for the quiz question
    public String getQuestion() {
        return question;
    }

    // Setter for the quiz question
    public void setQuestion(String question) {
        this.question = question;
    }

    // Getter for answer choice A
    public String getAnswerA() {
        return answerA;
    }

    // Setter for answer choice A
    public void setAnswerA(String answerA) {
        this.answerA = answerA;
    }

    // Getter for answer choice B
    public String getAnswerB() {
        return answerB;
    }

    // Setter for answer choice B
    public void setAnswerB(String answerB) {
        this.answerB = answerB;
    }

    // Getter for answer choice C
    public String getAnswerC() {
        return answerC;
    }

    // Setter for answer choice C
    public void setAnswerC(String answerC) {
        this.answerC = answerC;
    }

    // Getter for answer choice D
    public String getAnswerD() {
        return answerD;
    }

    // Setter for answer choice D
    public void setAnswerD(String answerD) {
        this.answerD = answerD;
    }

    // Getter for the correct answer
    public String getCorrectAnswer() {
        return correctAnswer;
    }

    // Setter for the correct answer
    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}