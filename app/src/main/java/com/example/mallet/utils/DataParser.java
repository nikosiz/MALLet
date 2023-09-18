package com.example.mallet.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataParser {
    public static ModelLearningSet parseLearningSetFromFile(String filePath) {
        ModelLearningSet learningSet = null;
        List<ModelFlashcard> flashcards = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineCount = 0;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue; // Skip empty lines
                }

                switch (lineCount) {
                    case 0:
                        learningSet.setName(line);
                        break;
                    case 1:
                        learningSet.setCreator(line);
                        break;
                    default:
                        String[] parts = line.split(";");
                        if (parts.length == 3) {
                            ModelFlashcard flashcard = new ModelFlashcard(parts[0], parts[1], parts[2]);
                            flashcards.add(flashcard);
                        } else {
                            // Handle invalid flashcard data
                        }
                        break;
                }
                lineCount++;
            }
            learningSet.setFlashcards(flashcards);
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }

        return learningSet;
    }
}
