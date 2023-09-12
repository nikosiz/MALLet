package com.example.mallet.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mallet.ModelFlashcard;
import com.example.mallet.R;

import java.util.List;

public class AdapterFlashcard extends RecyclerView.Adapter<AdapterFlashcard.FlashcardViewHolder> {
    private final List<ModelFlashcard> flashcards;
    private final AdapterFlashcard.OnFlashcardClickListener flashcardClickListener;


    public AdapterFlashcard(List<ModelFlashcard> flashcards, AdapterFlashcard.OnFlashcardClickListener flashcardClickListener) {
        this.flashcards = flashcards;
        this.flashcardClickListener = flashcardClickListener;
    }

    @NonNull
    @Override
    public FlashcardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_flashcard, parent, false);
        return new FlashcardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlashcardViewHolder holder, int position) {
        ModelFlashcard flashcard = flashcards.get(position);
        holder.termTV.setText(flashcard.getTerm());
        holder.definitionTV.setText(flashcard.getDefinition());
        holder.translationTV.setText(flashcard.getTranslation());

        // Set a click listener for the group item
        holder.itemView.setOnClickListener(view -> {
            if (flashcardClickListener != null) {
                flashcardClickListener.onFlashcardClick(flashcard);
            }
        });
    }

    @Override
    public int getItemCount() {
        return flashcards.size();
    }

    static class FlashcardViewHolder extends RecyclerView.ViewHolder {
        final TextView termTV;
        final TextView translationTV;
        final TextView definitionTV;

        FlashcardViewHolder(View itemView) {
            super(itemView);
            termTV = itemView.findViewById(R.id.flashcard_termTv);
            definitionTV = itemView.findViewById(R.id.flashcard_definitionTv);
            translationTV = itemView.findViewById(R.id.flashcard_translationTv);
        }
    }

    public interface OnFlashcardClickListener {
        void onFlashcardClick(ModelFlashcard flashcard);
    }
}
