package com.example.mallet.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mallet.R;

import java.util.List;

public class AdapterFlashcardStack extends RecyclerView.Adapter<AdapterFlashcardStack.ViewHolder> {
    private List<ModelFlashcard> items;

    public AdapterFlashcardStack(List<ModelFlashcard> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.model_flashcard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(items.get(position));

        // This gets handled in FragmentFlashcards
        holder.itemView.setOnClickListener(v -> {
            if (holder.translation.getVisibility() == View.VISIBLE) {
                holder.translation.setVisibility(View.GONE);
            } else {
                holder.translation.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView term;
        final TextView definition;
        final TextView translation;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            term = itemView.findViewById(R.id.flashcard_termTv);
            definition = itemView.findViewById(R.id.flashcard_definitionTv);
            translation = itemView.findViewById(R.id.flashcard_translationTv);
        }

        public void setData(ModelFlashcard data) {
            term.setText(data.getTerm());
            definition.setText(data.getDefinition());
            translation.setText(data.getTranslation());

            translation.setVisibility(View.GONE);
        }
    }

    public List<ModelFlashcard> getItems() {
        return items;
    }

    public void setItems(List<ModelFlashcard> items) {
        this.items = items;
    }
}
