package com.example.mallet.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mallet.R;

import java.util.List;

public class AdapterFlashcard extends RecyclerView.Adapter<AdapterFlashcard.FlashcardViewHolder> {

    private final List<ModelFlashcard> flashcards;
    private boolean isFlipped = false; // Flag to track whether the flashcard is flipped
    private final View.OnClickListener clickListener;

    // Constructor to initialize the adapter with flashcards and a click listener
    public AdapterFlashcard(List<ModelFlashcard> flashcards, View.OnClickListener clickListener) {
        this.flashcards = flashcards;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public FlashcardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the flashcard layout and create a ViewHolder for it
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_flashcard, parent, false);
        return new FlashcardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlashcardViewHolder holder, int position) {
        ModelFlashcard flashcard = flashcards.get(position);

        // Check if the flashcard is flipped
        if (isFlipped) {
            holder.termTv.setVisibility(View.GONE); // Hide term TextView

            holder.translationTv.setVisibility(View.VISIBLE); // Show translation TextView
            holder.translationTv.setText(flashcard.getTranslation()); // Set translation text
        } else {
            holder.termTv.setVisibility(View.VISIBLE); // Show term TextView
            holder.termTv.setText(flashcard.getTerm()); // Set term text

            holder.translationTv.setVisibility(View.GONE); // Hide translation TextView
        }

        // Set an OnClickListener for the item view (flashcard)
        holder.itemView.setOnClickListener(v -> {
            isFlipped = !isFlipped; // Toggle the flipped state
            notifyDataSetChanged(); // Notify the adapter that the data has changed
            if (clickListener != null) {
                clickListener.onClick(v); // Trigger the click listener
            }
        });
    }

    @Override
    public int getItemCount() {
        return flashcards.size(); // Return the number of flashcards in the list
    }

    // ViewHolder class for holding the views of a flashcard item
    static class FlashcardViewHolder extends RecyclerView.ViewHolder {
        final TextView termTv;
        final View definitionView;
        final TextView definitionTv;
        final View translationView;
        final TextView translationTv;

        // Constructor to initialize the views
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
