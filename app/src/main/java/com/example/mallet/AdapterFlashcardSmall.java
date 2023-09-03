package com.example.mallet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterFlashcardSmall extends RecyclerView.Adapter<AdapterFlashcardSmall.FlashcardViewHolder> {
    private List<ModelFlashcardSmall> flashcards;

    public AdapterFlashcardSmall(List<ModelFlashcardSmall> flashcards) {
        this.flashcards = flashcards;
    }

    @NonNull
    @Override
    public FlashcardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_flashcard_small, parent, false);
        return new FlashcardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlashcardViewHolder holder, int position) {
        ModelFlashcardSmall flashcard = flashcards.get(position);
        holder.WordTV.setText(flashcard.getWord());
        holder.TranslationTV.setText(flashcard.getTranslation());
    }

    @Override
    public int getItemCount() {
        return flashcards.size();
    }

    static class FlashcardViewHolder extends RecyclerView.ViewHolder {
        TextView WordTV;
        TextView TranslationTV;

        FlashcardViewHolder(View itemView) {
            super(itemView);
            WordTV = itemView.findViewById(R.id.model_small_word);
            TranslationTV = itemView.findViewById(R.id.model_small_translation);
        }
    }
}
