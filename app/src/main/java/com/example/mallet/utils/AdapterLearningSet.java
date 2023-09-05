package com.example.mallet.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mallet.ModelLearningSet;
import com.example.mallet.R;

import java.util.List;

public class AdapterLearningSet extends RecyclerView.Adapter<AdapterLearningSet.ViewHolder> {
    ModelLearningSet binding;
    private final Context context;
    private final List<ModelLearningSet> learningSetList;
    private final AdapterLearningSet.OnLearningSetClickListener setClickListener;


    public AdapterLearningSet(Context context, List<ModelLearningSet> learningSetList, AdapterLearningSet.OnLearningSetClickListener setClickListener) {
        this.context = context;
        this.learningSetList = learningSetList;
        this.setClickListener = setClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.model_learning_set, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelLearningSet learningSet = learningSetList.get(position);

        holder.learningSetNameTV.setText(learningSet.getLearningSetName());
        holder.learningSetCreatorTV.setText(learningSet.getLearningSetCreator());
        holder.learningSetTermsTV.setText(learningSet.getLearningSetTerms() + " terms");

        // Set a click listener for the group item
        holder.itemView.setOnClickListener(view -> {
            if (setClickListener != null) {
                setClickListener.onSetClick(learningSet);
            }
        });
    }

    @Override
    public int getItemCount() {
        return learningSetList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView learningSetNameTV;
        final TextView learningSetCreatorTV;
        final TextView learningSetTermsTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            learningSetNameTV = itemView.findViewById(R.id.learning_set_model_name_tv);
            learningSetCreatorTV = itemView.findViewById(R.id.learning_set_model_creator_tv);
            learningSetTermsTV = itemView.findViewById(R.id.learning_set_model_terms_tv);
        }
    }

    public interface OnLearningSetClickListener {
        void onSetClick(ModelLearningSet set);
    }
}
