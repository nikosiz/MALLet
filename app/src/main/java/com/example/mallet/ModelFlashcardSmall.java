package com.example.mallet;

import com.example.mallet.databinding.ModelFlashcardBinding;

// Define a class named ItemModel
public class ModelFlashcardSmall {
    private ModelFlashcardBinding binding;

    // Private fields to hold word, definition, and translation data
    private String word, translation;

    // Default constructor for the ItemModel class
    public ModelFlashcardSmall() {
        // Empty constructor
    }

    // Parameterized constructor to initialize the fields with data
    public ModelFlashcardSmall(String word, String translation) {
        this.word = word;
        this.translation = translation;
    }

    // Getter method to retrieve the word data
    public String getWord() {
        return word;
    }

    // Getter method to retrieve the translation data
    public String getTranslation() {

        return translation;
    }
}
