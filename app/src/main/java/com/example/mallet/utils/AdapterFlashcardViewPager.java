package com.example.mallet.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mallet.R;

import java.util.List;

public class AdapterFlashcardViewPager extends RecyclerView.Adapter<AdapterFlashcardViewPager.FlashcardViewHolder> {
    private final List<ModelFlashcard> flashcards;
    private boolean isFlipped = false;

    public AdapterFlashcardViewPager(List<ModelFlashcard> flashcards) {
        this.flashcards = flashcards;
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

        if (isFlipped) {
            Utils.hideItems(holder.termTv, holder.definitionView, holder.definitionTv);
            Utils.showItems(holder.translationTv);

            holder.translationTv.setText(flashcard.getTranslation());
        } else {
            Utils.showItems(holder.termTv);
            holder.termTv.setText(flashcard.getTerm());

            if (!flashcard.getDefinition().isEmpty()) {
                holder.definitionTv.setVisibility(View.VISIBLE);
                holder.definitionTv.setText(flashcard.getDefinition());
            }

            Utils.hideItems(holder.translationView, holder.translationTv);
        }

        holder.itemView.setOnClickListener(v -> {
            isFlipped = !isFlipped;
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return flashcards.size();
    }

    static class FlashcardViewHolder extends RecyclerView.ViewHolder {
        final TextView termTv;
        final View definitionView;
        final TextView definitionTv;
        final View translationView;
        final TextView translationTv;

        FlashcardViewHolder(View itemView) {
            super(itemView);

            termTv = itemView.findViewById(R.id.flashcard_termTv);

            definitionView = itemView.findViewById(R.id.flashcard_aboveDefinitionView);
            definitionTv = itemView.findViewById(R.id.flashcard_definitionTv);

            translationView = itemView.findViewById(R.id.flashcard_aboveTranslationView);
            translationTv = itemView.findViewById(R.id.flashcard_translationTv);
        }
    }
}
