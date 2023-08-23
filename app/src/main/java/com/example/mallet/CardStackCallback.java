package com.example.mallet;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

public class CardStackCallback extends DiffUtil.Callback {

    private List<ItemModel> oldItem;

    public CardStackCallback(List<ItemModel> oldItem, List<ItemModel> newItem) {
        this.oldItem = oldItem;
        this.newItem = newItem;
    }

    private List<ItemModel> newItem;

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
        return oldItem.get(oldItemPosition).getWord() == newItem.get(newItemPosition).getWord();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldItem.get(oldItemPosition) == newItem.get(newItemPosition);
    }
}
