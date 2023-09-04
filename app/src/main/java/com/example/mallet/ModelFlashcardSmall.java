package com.example.mallet;

import com.example.mallet.databinding.ModelFlashcardBinding;

// Define a class named ItemModel
public class ModelFlashcardSmall {
    private ModelFlashcardBinding binding;

    // Private fields to hold term, definition, and translation data
    private String term, translation;

    // Default constructor for the ItemModel class
    public ModelFlashcardSmall() {
        // Empty constructor
    }

    // Parameterized constructor to initialize the fields with data
    public ModelFlashcardSmall(String term, String translation) {
        this.term = term;
        this.translation = translation;
    }

    // Getter method to retrieve the term data
    public String getWord() {
        return term;
    }

    // Getter method to retrieve the translation data
    public String getTranslation() {

        return translation;
    }
}
