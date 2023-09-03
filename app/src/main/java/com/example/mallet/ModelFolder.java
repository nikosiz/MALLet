package com.example.mallet;

import com.example.mallet.databinding.ModelFolderBinding;

public class ModelFolder {
    private ModelFolderBinding binding;
    private final String folderCreator;
    private final String folderName;
    private String sets;

    public ModelFolder(String folderName, String folderCreator, String sets) {
        this.folderCreator = folderCreator;
        this.folderName = folderName;
        this.sets = sets;
    }

    public String getFolderCreator() {
        return folderCreator;
    }

    public String getFolderName() {
        return folderName;
    }

    public String getSets() {
        return sets;
    }

    public void setSets(String sets) {
        this.sets = sets;
    }
}
