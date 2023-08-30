package com.example.mallet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        // Inflate the item_card.xml layout for each item view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.model_flashcard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Bind data to the views within the ViewHolder
        holder.setData(items.get(position));
    }

    @Override
    public int getItemCount() {
        // Return the number of items in the list
        return items.size();
    }

    // ViewHolder class that holds references to the views within each item view
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView word, definition, translation;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize the TextViews with their corresponding views from the layout
            word = itemView.findViewById(R.id.item_word);
            definition = itemView.findViewById(R.id.item_definition);
            translation = itemView.findViewById(R.id.item_translation);
        }

        // Method to set data from the data model to the views
        public void setData(ModelFlashcard data) {
            word.setText(data.getWord());
            definition.setText(data.getDefinition());
            translation.setText(data.getTranslation());
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
