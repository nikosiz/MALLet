package com.example.mallet.utils;

import android.os.Parcel;
import android.os.Parcelable;

public class ModelFlashcard implements Parcelable {
    private String term, definition, translation;

    // Default constructor
    public ModelFlashcard() {
    }

    // Parameterized constructor to initialize the flashcard with data
    public ModelFlashcard(String term, String definition, String translation) {
        this.term = term;
        this.definition = definition;
        this.translation = translation;
    }

    public String getTerm() {
        return term;
    }

    // Setter for the flashcard term
    public void setTerm(String term) {
        this.term = term;
    }

    // Getter for the flashcard definition
    public String getDefinition() {
        return definition;
    }

    // Setter for the flashcard definition
    public void setDefinition(String definition) {
        this.definition = definition;
    }

    // Getter for the flashcard translation
    public String getTranslation() {
        return translation;
    }

    // Setter for the flashcard translation
    public void setTranslation(String translation) {
        this.translation = translation;
    }

    // Parcelable constructor to create a flashcard from a Parcel
    protected ModelFlashcard(Parcel in) {
        term = in.readString();
        definition = in.readString();
        translation = in.readString();
    }

    // Parcelable.Creator to create flashcards from Parcel
    public static final Creator<ModelFlashcard> CREATOR = new Creator<ModelFlashcard>() {
        @Override
        public ModelFlashcard createFromParcel(Parcel in) {
            return new ModelFlashcard(in);
        }

        @Override
        public ModelFlashcard[] newArray(int size) {
            return new ModelFlashcard[size];
        }
    };

    // Describe the contents of the Parcelable (not used)
    @Override
    public int describeContents() {
        return 0;
    }

    // Write the flashcard data to a Parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(term);
        dest.writeString(definition);
        dest.writeString(translation);
    }
}
