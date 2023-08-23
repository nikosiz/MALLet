package com.example.mallet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CardStackAdapter extends RecyclerView.Adapter<CardStackAdapter.ViewHolder> {

    private List<ItemModel> items;

    public CardStackAdapter(List<ItemModel> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView word, definition, translation;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            word = itemView.findViewById(R.id.item_word);
            definition = itemView.findViewById(R.id.item_definition);
            translation = itemView.findViewById(R.id.item_translation);
        }

        public void setData(ItemModel data) {
            word.setText(data.getWord());
            definition.setText(data.getDefinition());
            translation.setText(data.getTranslation());
        }
    }
}
