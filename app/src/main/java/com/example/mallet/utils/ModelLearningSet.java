package com.example.mallet.utils;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ModelLearningSet implements Parcelable {
    private long id;
    private String name;
    private String description;
    private String creator;
    private int nrOfTerms;
    private List<ModelFlashcard> terms;
    private String nextChungUri;

    // Constructor to initialize a learning set
    public ModelLearningSet(String name,
                            String creator,
                            String description,
                            List<ModelFlashcard> terms,
                            long id, String nextChungUri) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.creator = creator;
        this.terms = terms;
        this.nextChungUri = nextChungUri;
    }

    // Getter for the name of the learning set
    public String getName() {
        return name;
    }

    // Setter for the name of the learning set
    public void setName(String name) {
        this.name = name;
    }

    // Getter for the creator of the learning set
    public String getCreator() {
        return creator;
    }

    // Setter for the creator of the learning set
    public void setCreator(String creator) {
        this.creator = creator;
    }

    // Getter for the description of the learning set
    public String getDescription() {
        return description;
    }

    // Setter for the description of the learning set
    public void setDescription(String description) {
        this.description = description;
    }

    // Getter for the ID of the learning set
    public long getId() {
        return id;
    }

    // Setter for the ID of the learning set
    public void setId(int id) {
        this.id = id;
    }

    // Getter for the list of flashcards (terms) in the learning set
    public List<ModelFlashcard> getTerms() {
        return terms;
    }

    // Setter for the list of flashcards (terms) in the learning set
    public void setFlashcards(List<ModelFlashcard> terms) {
        this.terms = terms;
        this.nrOfTerms = terms.size();
    }

    // Getter for the nr of terms in the learning set
    public int getNrOfTerms() {
        return nrOfTerms;
    }

    // Setter for the nr of terms in the learning set
    public void setNrOfTerms(int nrOfTerms) {
        this.nrOfTerms = nrOfTerms;
    }

    // Implement the Parcelable interface methods
    protected ModelLearningSet(Parcel in) {
        name = in.readString();
        creator = in.readString();
        terms = in.createTypedArrayList(ModelFlashcard.CREATOR);
        nrOfTerms = in.readInt();
    }

    public static final Creator<ModelLearningSet> CREATOR = new Creator<ModelLearningSet>() {
        @Override
        public ModelLearningSet createFromParcel(Parcel in) {
            return new ModelLearningSet(in);
        }

        @Override
        public ModelLearningSet[] newArray(int size) {
            return new ModelLearningSet[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(creator);
        dest.writeTypedList(terms);
        dest.writeInt(nrOfTerms);
    }
}