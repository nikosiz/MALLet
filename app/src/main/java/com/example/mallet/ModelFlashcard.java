package com.example.mallet;

// Define a class named ItemModel
public class ModelFlashcard {

    // Private fields to hold word, definition, and translation data
    private String word, definition, translation;

    // Default constructor for the ItemModel class
    public ModelFlashcard() {
        // Empty constructor
    }

    // Parameterized constructor to initialize the fields with data
    public ModelFlashcard(String word, String definition, String translation) {
        this.word = word;
        this.definition = definition;
        this.translation = translation;
    }

    // Getter method to retrieve the word data
    public String getWord() {
        return word;
    }

    // Getter method to retrieve the definition data
    public String getDefinition() {
        return definition;
    }

    // Getter method to retrieve the translation data
    public String getTranslation() {

        return translation;
    }
}
