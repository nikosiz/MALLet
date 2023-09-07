package com.example.mallet;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FlashcardManager {
    public static ModelLearningSet readFlashcards(Context context, String filePath) {
        ModelLearningSet learningSet = null;
        List<ModelFlashcard> flashcards = new ArrayList<>();
        String setName = "";
        String setCreator = "";

        try {
            AssetManager assetManager = context.getAssets();
            InputStreamReader inputStreamReader = new InputStreamReader(assetManager.open(filePath));
            BufferedReader reader = new BufferedReader(inputStreamReader);

            // Read the first two lines as set name and set creator
            setName = reader.readLine();
            setCreator = reader.readLine();

            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 3) {
                    String term = parts[0].trim();
                    String definition = parts[1].trim();
                    String translation = parts[2].trim();
                    flashcards.add(new ModelFlashcard(term, definition, translation));
                }
            }

            reader.close();

            // Create the ModelLearningSet object with the extracted information
            learningSet = new ModelLearningSet(setName, flashcards, setCreator, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return learningSet;
    }



}
