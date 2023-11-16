package com.example.mallet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mallet.databinding.FragmentDatabaseBinding;
import com.example.mallet.utils.ModelLearningSet;
import com.example.mallet.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class FragmentDatabase extends Fragment {
    private FragmentDatabaseBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDatabaseBinding.inflate(getLayoutInflater());

        setupContents();

        return inflater.inflate(R.layout.fragment_database, container, false);
    }


    private void setupContents() {
        ProgressBar progressBar = binding.databaseProgressBar;

        LinearLayout setsLl = binding.databaseSetsSvLl;



        Utils.hideItems(progressBar);
    }

    private void displaySetsInLinearLayout(List<ModelLearningSet> sets, LinearLayout linearLayout, LayoutInflater inflater) {
        linearLayout.removeAllViews();

        for (ModelLearningSet set : sets) {
            View setItemView = inflater.inflate(R.layout.model_learning_set, linearLayout, false);

            TextView setNameTv = setItemView.findViewById(R.id.learningSet_nameTv);
            setNameTv.setVisibility(View.VISIBLE);
            setNameTv.setText(set.getName());

            TextView nrOfTermsTv = setItemView.findViewById(R.id.learningSet_nrOfTermsTv);
            nrOfTermsTv.setVisibility(View.VISIBLE);
            nrOfTermsTv.setText(String.valueOf(set.getNrOfTerms()));

            TextView creatorTv = setItemView.findViewById(R.id.learningSet_creatorTv);
            creatorTv.setVisibility(View.VISIBLE);
            creatorTv.setText("Bosses of MALLet");

            linearLayout.addView(setItemView);
        }
    }

    private List<ModelLearningSet> getSets() {
        List<ModelLearningSet> sets = new ArrayList<>();

        sets.add(ModelLearningSet.builder().name("Animals").nrOfTerms(30).creator("Bosses of MALLet").build());
        sets.add(ModelLearningSet.builder().name("Colors").nrOfTerms(19).creator("Bosses of MALLet").build());
        sets.add(ModelLearningSet.builder().name("Fruit").nrOfTerms(32).creator("Bosses of MALLet").build());
        sets.add(ModelLearningSet.builder().name("Vegetables").nrOfTerms(54).creator("Bosses of MALLet").build());
        sets.add(ModelLearningSet.builder().name("Idioms").nrOfTerms(34).creator("Bosses of MALLet").build());
        sets.add(ModelLearningSet.builder().name("Phrasal verbs").nrOfTerms(67).creator("Bosses of MALLet").build());

        return sets;
    }
}
