package com.example.mallet;

import com.example.mallet.databinding.ModelFlashcardBinding;

// Define a class named ItemModel
public class ModelFlashcard {
    private ModelFlashcardBinding binding;

    // Private fields to hold term, definition, and translation data
    private String term, definition, translation;

    // Default constructor for the ItemModel class
    public ModelFlashcard() {
        // Empty constructor
    }

    // Parameterized constructor to initialize the fields with data
    public ModelFlashcard(String term, String definition, String translation) {
        this.term = term;
        this.definition = definition;
        this.translation = translation;
    }

    // Getter method to retrieve the word data
    public String getTerm() {
        return term;
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
