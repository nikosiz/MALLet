package com.example.mallet.utils;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;

@Builder
public class ModelLearningSet implements Parcelable {
    private long id;
    @NonNull
    private String name;
    private String description;
    private String identifier;
    private String creator;
    private int nrOfTerms;
    private List<ModelFlashcard> terms;
    private String nextChunkUri;

    public ModelLearningSet(long id, String name, String description, String identifier, String creator, int nrOfTerms, List<ModelFlashcard> terms, String nextChunkUri) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.identifier = identifier;
        this.creator = creator;
        this.nrOfTerms = nrOfTerms;
        this.terms = terms;
        this.nextChunkUri = nextChunkUri;
    }

    public ModelLearningSet(long id, String name, String description, String creator, int nrOfTerms, List<ModelFlashcard> terms, String nextChunkUri) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.creator = creator;
        this.nrOfTerms = nrOfTerms;
        this.terms = terms;
        this.nextChunkUri = nextChunkUri;
    }

    public ModelLearningSet(String name, String description, List<ModelFlashcard> terms) {
        this.name = name;
        this.description = description;
        this.terms = terms;
    }

    public ModelLearningSet(String name,
                            String creator,
                            String description,
                            List<ModelFlashcard> terms,
                            long id, String nextChunkUri) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.creator = creator;
        this.terms = terms;
        this.nextChunkUri = nextChunkUri;
    }


    public ModelLearningSet(long id, String name,String identifier, String description, String creator, int nrOfTerms) {
        this.id = id;
        this.name = name;
        this.identifier = identifier;
        this.description = description;
        this.creator = creator;
        this.nrOfTerms = nrOfTerms;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getId() {
        return id;
    }
    public String getIdentifier() {
        return identifier;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<ModelFlashcard> getTerms() {
        return terms;
    }

    public void setFlashcards(List<ModelFlashcard> terms) {
        this.terms = terms;
    }

    public int getNrOfTerms() {
        return nrOfTerms;
    }

    public void setNrOfTerms(int nrOfTerms) {
        this.nrOfTerms = nrOfTerms;
    }

    protected ModelLearningSet(Parcel in) {
        id = in.readLong();
        name = in.readString();
        creator = in.readString();
        terms = in.createTypedArrayList(ModelFlashcard.CREATOR);
        nrOfTerms = in.readInt();
    }

    @Override
    public String toString() {
        return identifier;
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
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(creator);
        dest.writeTypedList(terms);
        dest.writeInt(nrOfTerms);
    }
}