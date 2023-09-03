package com.example.mallet;

public class ModelLearningSet {
    private String learningSetName;
    private String learningSetCreator;
    private String learningSetTerms;

    public ModelLearningSet(String learningSetName,
                            String learningSetTerms,
                            String learningSetCreator, int id) {
        this.learningSetName = learningSetName;
        this.learningSetCreator = learningSetCreator;
        this.learningSetTerms = learningSetTerms;
    }

    public String getLearningSetName() {
        return learningSetName;
    }

    public void setLearningSetName(String learningSetName) {
        this.learningSetName = learningSetName;
    }

    public String getLearningSetTerms() {
        return learningSetTerms;
    }

    public void setLearningSetTerms(String learningSetTerms) {
        this.learningSetTerms = learningSetTerms;
    }

    public String getLearningSetCreator() {
        return learningSetCreator;
    }

    public void setLearningSetCreator(String learningSetCreator) {
        this.learningSetCreator = learningSetCreator;
    }
}
