package com.example.mallet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class AdapterLearningSet extends BaseAdapter {

    private Context context;
    private List<ModelLearningSet> learningSetList;

    public AdapterLearningSet(Context context, List<ModelLearningSet> learningSetList) {
        this.context = context;
        this.learningSetList = learningSetList;
    }

    @Override
    public int getCount() {
        return learningSetList.size();
    }

    @Override
    public Object getItem(int position) {
        return learningSetList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            itemView = inflater.inflate(R.layout.model_learning_set, parent, false);
        }

        ModelLearningSet learningSet = learningSetList.get(position);

        TextView learningSetNameTv = itemView.findViewById(R.id.learning_set_model_name_tv);
        TextView learningSetCreatorTv = itemView.findViewById(R.id.learning_set_model_creator_tv);
        TextView learningSetTermsTv = itemView.findViewById(R.id.learning_set_model_terms_tv);

        learningSetNameTv.setText(learningSet.getLearningSetName());
        learningSetCreatorTv.setText("Created by: " + learningSet.getLearningSetCreator());
        learningSetTermsTv.setText("Terms: " + learningSet.getLearningSetTerms());

        return itemView;
    }
}
