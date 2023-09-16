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
    private boolean isFlipped = false;
    private final View.OnClickListener clickListener;

    public AdapterFlashcard(List<ModelFlashcard> flashcards, View.OnClickListener clickListener) {
        this.flashcards = flashcards;
        this.clickListener = clickListener;
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
            holder.termTv.setVisibility(View.GONE);

            holder.translationTv.setVisibility(View.VISIBLE);
            holder.translationTv.setText(flashcard.getTranslation());
        } else {
            holder.termTv.setVisibility(View.VISIBLE);
            holder.termTv.setText(flashcard.getTerm());

            holder.translationTv.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            isFlipped = !isFlipped;
            notifyDataSetChanged();
            if (clickListener != null) {
                clickListener.onClick(v);
            }
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
            definitionView = itemView.findViewById(R.id.flashcard_definitionV);
            definitionTv = itemView.findViewById(R.id.flashcard_definitionTv);
            translationView = itemView.findViewById(R.id.flashcard_translationV);
            translationTv = itemView.findViewById(R.id.flashcard_translationTv);
        }
    }
}
