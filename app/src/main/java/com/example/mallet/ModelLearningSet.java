package com.example.mallet;

public class ModelLearningSet {
    private String learningSetName;
    private String learningSetCreator;
    private String learningSetTerms;

    // TODO: update this to be a list of words, now it's just for test
    private String learningSetWord;
    private String learningSetDefinition;


    public ModelLearningSet(String learningSetName,
                            String learningSetTerms,
                            String learningSetCreator,
                            String learningSetWord, String learningSetDefinition) {
        this.learningSetName = learningSetName;
        this.learningSetCreator = learningSetCreator;
        this.learningSetTerms = learningSetTerms;
        this.learningSetWord = learningSetWord;
        this.learningSetDefinition = learningSetDefinition;
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

    public String getLearningSetWord() {
        return learningSetWord;
    }

    public void setLearningSetWord(String learningSetWord) {
        this.learningSetWord = learningSetWord;
    }

    public String getLearningSetDefinition() {
        return learningSetDefinition;
    }

    public void setLearningSetDefinition(String learningSetDefinition) {
        this.learningSetDefinition = learningSetDefinition;
    }
}
