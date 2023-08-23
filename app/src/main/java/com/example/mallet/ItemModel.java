package com.example.mallet;

public class ItemModel {
    private String word, definition, translation;

    public ItemModel() {

    }

    public ItemModel(String word, String definition, String translation) {
        this.word = word;
        this.definition = definition;
        this.translation = translation;
    }

    public String getWord() {
        return word;
    }

    public String getDefinition() {
        return definition;
    }

    public String getTranslation() {
        return translation;
    }
}
