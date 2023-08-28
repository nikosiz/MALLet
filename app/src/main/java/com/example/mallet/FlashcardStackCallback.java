package com.example.mallet;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;
import java.util.Objects;

public class FlashcardStackCallback extends DiffUtil.Callback {

    private List<FlashcardModel> oldItem;

    public FlashcardStackCallback(List<FlashcardModel> oldItem, List<FlashcardModel> newItem) {
        this.oldItem = oldItem;
        this.newItem = newItem;
    }

    private List<FlashcardModel> newItem;

    @Override
    public int getOldListSize() {
        return oldItem.size();
    }

    @Override
    public int getNewListSize() {
        return newItem.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return Objects.equals(oldItem.get(oldItemPosition).getWord(), newItem.get(newItemPosition).getWord());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldItem.get(oldItemPosition) == newItem.get(newItemPosition);
    }
}
