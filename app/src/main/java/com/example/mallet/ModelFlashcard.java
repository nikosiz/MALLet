package com.example.mallet;

import android.os.Parcel;
import android.os.Parcelable;

public class ModelFlashcard implements Parcelable {
    // Private fields to hold term, definition, and translation data
    private String term, definition, translation;

    // Default constructor for the ModelFlashcard class
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

    // Implement the Parcelable interface methods
    protected ModelFlashcard(Parcel in) {
        term = in.readString();
        definition = in.readString();
        translation = in.readString();
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(term);
        dest.writeString(definition);
        dest.writeString(translation);
    }
}
