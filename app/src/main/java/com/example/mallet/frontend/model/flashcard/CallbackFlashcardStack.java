package com.example.mallet.frontend.model.flashcard;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;
import java.util.Objects;

public class CallbackFlashcardStack extends DiffUtil.Callback {

    private final List<ModelFlashcard> oldItem; // List of old items
    private final List<ModelFlashcard> newItem; // List of new items

    // Constructor to initialize the callback with old and new item lists
    public CallbackFlashcardStack(List<ModelFlashcard> oldItem, List<ModelFlashcard> newItem) {
        this.oldItem = oldItem;
        this.newItem = newItem;
    }

    @Override
    public int getOldListSize() {
        return oldItem.size(); // Return the size of the old item list
    }

    @Override
    public int getNewListSize() {
        return newItem.size(); // Return the size of the new item list
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        // Compare whether the items at the specified positions are the same based on a unique identifier (term in this case)
        return Objects.equals(oldItem.get(oldItemPosition).getTerm(), newItem.get(newItemPosition).getTerm());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        // Compare whether the contents of the items at the specified positions are the same
        // Note: This implementation checks if the old and new items at the given positions are the same object reference
        return oldItem.get(oldItemPosition) == newItem.get(newItemPosition);
    }
}
