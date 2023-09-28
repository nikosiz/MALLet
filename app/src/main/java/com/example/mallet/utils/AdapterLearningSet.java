package com.example.mallet.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mallet.R;

import java.util.List;

public class AdapterLearningSet extends RecyclerView.Adapter<AdapterLearningSet.ViewHolder> {
    ModelLearningSet binding; // This line seems to be unrelated to the adapter and can be removed.
    private final Context context;
    private final List<ModelLearningSet> learningSetList;
    private final AdapterLearningSet.OnLearningSetClickListener setClickListener;

    // Constructor to initialize the adapter with data and click listener
    public AdapterLearningSet(Context context, List<ModelLearningSet> learningSetList, OnLearningSetClickListener setClickListener) {
        this.context = context;
        this.learningSetList = learningSetList;
        this.setClickListener = setClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the learning_set_item.xml layout for each item view
        View itemView = LayoutInflater.from(context).inflate(R.layout.model_learning_set, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the learning set data at the current position
        ModelLearningSet learningSet = learningSetList.get(position);

        // Bind learning set data to the views within the ViewHolder
        holder.learningSetNameTv.setText(learningSet.getName());
        holder.learningSetCreatorTv.setText(learningSet.getCreator());
        holder.learningSetTermsTv.setText(learningSet.getNrOfTerms() + " terms");

        // Set a click listener for the learning set item
        holder.itemView.setOnClickListener(view -> {
            if (setClickListener != null) {
                setClickListener.onSetClick(learningSet);
            }
        });
    }

    @Override
    public int getItemCount() {
        // Return the nr of learning sets in the list
        return learningSetList.size();
    }

    // ViewHolder class that holds references to the views within each item view
    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView learningSetNameTv, learningSetCreatorTv, learningSetTermsTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            learningSetNameTv = itemView.findViewById(R.id.learningSet_nameTv);
            learningSetTermsTv = itemView.findViewById(R.id.learningSet_nrOfTermsTv);
            learningSetCreatorTv = itemView.findViewById(R.id.learningSet_creatorTv);
        }
    }

    public interface OnLearningSetClickListener {
        void onSetClick(ModelLearningSet set);
    }
}