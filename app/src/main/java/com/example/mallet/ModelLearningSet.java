package com.example.mallet;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class ModelLearningSet implements Parcelable {
    private String learningSetName;
    private String learningSetCreator;
    private List<ModelFlashcard> learningSetTerms;
    private int numberOfTerms;

    public ModelLearningSet(String learningSetName,
                            List<ModelFlashcard> learningSetTerms,
                            String learningSetCreator, int id) {
        this.learningSetName = learningSetName;
        this.learningSetCreator = learningSetCreator;
        this.learningSetTerms = learningSetTerms;
        this.numberOfTerms = learningSetTerms.size();
    }

    // Getter and setter for the number of terms
    public int getNumberOfTerms() {
        return numberOfTerms;
    }

    public void setNumberOfTerms(int numberOfTerms) {
        this.numberOfTerms = numberOfTerms;
    }

    public String getLearningSetName() {
        return learningSetName;
    }

    public void setLearningSetName(String learningSetName) {
        this.learningSetName = learningSetName;
    }

    public List<ModelFlashcard> getLearningSetTerms() {
        return learningSetTerms;
    }

    public List<ModelFlashcard> setLearningSetTerms() {
        return learningSetTerms;
    }

    public String getLearningSetCreator() {
        return learningSetCreator;
    }

    public void setLearningSetCreator(String learningSetCreator) {
        this.learningSetCreator = learningSetCreator;
    }

    // Implement the Parcelable interface methods
    protected ModelLearningSet(Parcel in) {
        learningSetName = in.readString();
        learningSetCreator = in.readString();
        learningSetTerms = in.createTypedArrayList(ModelFlashcard.CREATOR);
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
        dest.writeString(learningSetName);
        dest.writeString(learningSetCreator);
        dest.writeTypedList(learningSetTerms);
        dest.writeInt(numberOfTerms);
    }
}
