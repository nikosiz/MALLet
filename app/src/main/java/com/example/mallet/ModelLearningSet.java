package com.example.mallet;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ModelLearningSet implements Parcelable {
    private String name, creator;
    private int id, numberOfTerms;
    private List<ModelFlashcard> terms;

    public ModelLearningSet(String name,
                            List<ModelFlashcard> terms,
                            String creator, int id) {
        this.name = name;
        this.creator = creator;
        this.id = id;
        this.terms = terms;
        this.numberOfTerms = terms.size();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<ModelFlashcard> getTerms() {
        return terms;
    }

    public void setFlashcards(List<ModelFlashcard> terms) {
        this.terms = terms;
        this.numberOfTerms = terms.size();
    }

    public int getNumberOfTerms() {
        return numberOfTerms;
    }

    public void setNumberOfTerms(int numberOfTerms) {
        this.numberOfTerms = numberOfTerms;
    }

    // Implement the Parcelable interface methods
    protected ModelLearningSet(Parcel in) {
        name = in.readString();
        creator = in.readString();
        terms = in.createTypedArrayList(ModelFlashcard.CREATOR);
        numberOfTerms = in.readInt();
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
        dest.writeInt(numberOfTerms);
    }
}
