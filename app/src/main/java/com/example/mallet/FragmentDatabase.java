package com.example.mallet;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mallet.backend.client.set.boundary.SetServiceImpl;
import com.example.mallet.databinding.FragmentDatabaseBinding;
import com.example.mallet.utils.AuthenticationUtils;
import com.example.mallet.utils.ModelLearningSet;
import com.example.mallet.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class FragmentDatabase extends Fragment {
    private ActivityMain activityMain;
    private FragmentDatabaseBinding binding;
    private SetServiceImpl setService;
    private LinearLayout setsLl;
    private Animation fadeInAnimation;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof ActivityMain) {
            activityMain = (ActivityMain) context;
            fadeInAnimation = AnimationUtils.loadAnimation(requireActivity(), R.anim.fade_in);
        } else {
            // Handle the case where the activity does not implement the method
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //todo fetch sets

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDatabaseBinding.inflate(getLayoutInflater());

        String credential = AuthenticationUtils.get(getContext());
        this.setService = new SetServiceImpl(credential);

        setupContents();

        return binding.getRoot();
    }

    private void setupContents() {
        ProgressBar progressBar = binding.databaseProgressBar;

        setsLl = binding.databaseSetsLl;

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
