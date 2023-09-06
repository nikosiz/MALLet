package com.example.mallet;

import java.util.List;

public class ModelLearningSet {
    private String learningSetName;
    private String learningSetCreator;
    private final List<ModelFlashcard> learningSetTerms; // Now this field holds a ModelFlashcard instance
    private int numberOfTerms;

    public ModelLearningSet(String learningSetName,
                            List<ModelFlashcard> learningSetTerms,
                            String learningSetCreator, int id) {
        this.learningSetName = learningSetName;
        this.learningSetCreator = learningSetCreator;
        this.learningSetTerms = learningSetTerms;
        this.numberOfTerms = learningSetTerms.size();
    }

    // Getter and setter for the number of terms
    public int getNumberOfTerms() {
        return numberOfTerms;
    }

    public void setNumberOfTerms(int numberOfTerms) {
        this.numberOfTerms = numberOfTerms;
    }

    public String getLearningSetName() {
        return learningSetName;
    }

    public void setLearningSetName(String learningSetName) {
        this.learningSetName = learningSetName;
    }

    public List<ModelFlashcard> getLearningSetTerms() {
        return learningSetTerms;
    }

    public List<ModelFlashcard> setLearningSetTerms() {
        return learningSetTerms;
    }

    public String getLearningSetCreator() {
        return learningSetCreator;
    }

    public void setLearningSetCreator(String learningSetCreator) {
        this.learningSetCreator = learningSetCreator;
    }
}
