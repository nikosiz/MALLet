package com.example.mallet.frontend.model.flashcard;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Builder;

@Builder
public class ModelFlashcard implements Parcelable {
    private String term, definition, translation;

    public ModelFlashcard() {
    }

    public ModelFlashcard(String term, String definition, String translation) {
        this.term = term;
        this.definition = definition;
        this.translation = translation;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

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
