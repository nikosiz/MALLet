package com.mallet.frontend.model.flashcard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mallet.R;
import com.mallet.frontend.utils.ViewUtils;

import java.util.List;
import java.util.Objects;

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
            ViewUtils.hideItems(holder.termTv, holder.definitionView, holder.definitionTv);
            ViewUtils.showItems(holder.translationTv);

            holder.translationTv.setText(flashcard.getTranslation());
        } else {
            ViewUtils.showItems(holder.termTv);
            holder.termTv.setText(flashcard.getTerm());

            if (Objects.nonNull(flashcard.getDefinition())) {
                holder.definitionTv.setVisibility(View.VISIBLE);
                holder.definitionTv.setText(flashcard.getDefinition());
            }

            ViewUtils.hideItems(holder.translationView, holder.translationTv);
        }

        holder.itemView.setOnClickListener(v -> {
            isFlipped = !isFlipped;
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        if (flashcards != null) {
            return flashcards.size();
        } else {
            return 0;
        }
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
