package com.example.mallet;

public class ModelKit {
    private String kitName;
    private String kitCreator;
    private String kitTerms;

    public ModelKit(String kitName, String kitTerms, String kitCreator) {
        this.kitName = kitName;
        this.kitCreator = kitCreator;
        this.kitTerms = kitTerms;
    }

    public String getKitName() {
        return kitName;
    }

    public void setKitName(String kitName) {
        this.kitName = kitName;
    }

    public String getKitTerms() {
        return kitTerms;
    }

    public void setKitTerms(String kitTerms) {
        this.kitTerms = kitTerms;
    }

    public String getKitCreator() {
        return kitCreator;
    }

    public void setKitCreator(String kitCreator) {
        this.kitCreator = kitCreator;
    }
}
