package com.example.mallet.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FlashcardManager {
    public static ModelLearningSet readFlashcards(Context context, String filePath) {
        // Initialize variables
        ModelLearningSet learningSet = null; // Learning set object
        List<ModelFlashcard> flashcards = new ArrayList<>(); // List to store flashcards
        String setName; // Name of the learning set
        String setCreator; // Creator of the learning set
        String setDescription; // Description of the learning set
        long setId = 0;
        String nextChunkUri = "";

        try {
            // Get the asset manager for reading files from the assets folder
            AssetManager assetManager = context.getAssets();

            // Open the file at the specified filePath for reading
            InputStreamReader inputStreamReader = new InputStreamReader(assetManager.open(filePath));
            BufferedReader reader = new BufferedReader(inputStreamReader);

            // Read the first three lines of the file (setName, setCreator, setDescription)
            setName = reader.readLine(); // Read the first line
            setCreator = reader.readLine(); // Read the second line
            setDescription = reader.readLine(); // Read the third line

            String line;

            // Read the remaining lines containing flashcard data
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";"); // Split the line into parts using ';' as the delimiter
                if (parts.length == 3) {
                    // Extract and trim the term, definition, and translation from the parts
                    String term = parts[0].trim();
                    String definition = parts[1].trim();
                    String translation = parts[2].trim();

                    // Create a new ModelFlashcard object and add it to the flashcards list
                    flashcards.add(new ModelFlashcard(term, definition, translation));
                }
            }

            // Close the reader
            reader.close();

            // Create a new ModelLearningSet object with the extracted data
            learningSet = new ModelLearningSet(setName, setCreator, setDescription, flashcards, setId, nextChunkUri);
        } catch (IOException e) {
            // Handle any IOException by printing the stack trace
            e.printStackTrace();
        }

        // Return the populated ModelLearningSet object
        return learningSet;
    }
}
