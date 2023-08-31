package com.example.mallet;

import com.example.mallet.databinding.ModelFolderBinding;

public class ModelFolder {
    private ModelFolderBinding binding;
    private String folderCreator;
    private String folderName;

    public ModelFolder(String folderName, String folderCreator) {
        this.folderCreator = folderCreator;
        this.folderName = folderName;
    }

    public String getFolderCreator() {
        return folderCreator;
    }

    public String getFolderName() {
        return folderName;
    }
}
