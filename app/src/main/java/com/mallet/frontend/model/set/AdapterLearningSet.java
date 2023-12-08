package com.mallet.frontend.model.set;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mallet.R;
import com.mallet.frontend.utils.ViewUtils;

import java.util.List;

public class AdapterLearningSet extends RecyclerView.Adapter<AdapterLearningSet.ViewHolder> {
    ModelLearningSet binding;
    private final Context context;
    private final List<ModelLearningSet> learningSetList;
    private final AdapterLearningSet.OnLearningSetClickListener setClickListener;

    public AdapterLearningSet(Context context, List<ModelLearningSet> learningSetList, OnLearningSetClickListener setClickListener) {
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

        if (learningSet != null) {
            holder.learningSetNameTv.setText(learningSet.getName());
            holder.learningSetIdentifierTv.setText(learningSet.getIdentifier());
            holder.learningSetCreatorTv.setText(learningSet.getCreator());
            holder.learningSetNrOfTermsTv.setText(learningSet.getNrOfTerms() + " terms");
        } else {
            // Handle the case when learningSet is null.
        }

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
        final TextView learningSetNameTv, learningSetIdentifierTv, learningSetCreatorTv, learningSetNrOfTermsTv;
        private final ImageView deleteIv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            learningSetNameTv = itemView.findViewById(R.id.learningSet_nameTv);
            learningSetIdentifierTv = itemView.findViewById(R.id.learningSet_identifierTv);
            learningSetNrOfTermsTv = itemView.findViewById(R.id.learningSet_nrOfTermsTv);
            learningSetCreatorTv = itemView.findViewById(R.id.learningSet_creatorTv);
            deleteIv = itemView.findViewById(R.id.learningSet_deleteIv);
            ViewUtils.hideItems(deleteIv);
        }
    }

    public interface OnLearningSetClickListener {
        void onSetClick(ModelLearningSet set);
    }
}