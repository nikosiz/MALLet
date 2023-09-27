package com.example.mallet.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mallet.R;

import java.util.List;

// Import necessary classes and packages

public class AdapterFlashcardStack extends RecyclerView.Adapter<AdapterFlashcardStack.ViewHolder> {
    private List<ModelFlashcard> items; // List to hold the data for the adapter

    // Constructor to initialize the adapter with data
    public AdapterFlashcardStack(List<ModelFlashcard> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the flashcard.xml layout for each item view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.model_flashcard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Bind data to the views within the ViewHolder
        holder.setData(items.get(position));

        // Set an OnClickListener to toggle the visibility of the translation
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
        // Return the nr of items in the list
        return items.size();
    }

    // ViewHolder class that holds references to the views within each item view
    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView term;
        final TextView definition;
        final TextView translation;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize the TextViews with their corresponding views from the layout
            term = itemView.findViewById(R.id.flashcard_termTv);
            definition = itemView.findViewById(R.id.flashcard_definitionTv);
            translation = itemView.findViewById(R.id.flashcard_translationTv);
        }

        // Method to set data from the data model to the views
        public void setData(ModelFlashcard data) {
            term.setText(data.getTerm());
            definition.setText(data.getDefinition());
            translation.setText(data.getTranslation());

            // Initially, set the translation view to GONE
            translation.setVisibility(View.GONE);
        }
    }

    // Getter and setter methods for the items list
    public List<ModelFlashcard> getItems() {
        return items;
    }

    public void setItems(List<ModelFlashcard> items) {
        this.items = items;
    }
}
