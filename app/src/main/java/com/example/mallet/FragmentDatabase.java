package com.example.mallet;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.agh.api.SetBasicDTO;
import com.agh.api.SetInformationDTO;
import com.example.mallet.backend.client.configuration.ResponseHandler;
import com.example.mallet.databinding.FragmentDatabaseBinding;
import com.example.mallet.utils.ModelLearningSet;
import com.example.mallet.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentDatabase extends Fragment {
    private ActivityMain activityMain;
    private FragmentDatabaseBinding binding;
    private ProgressBar progressBar;
    private ScrollView databaseSv;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        setupContents();

        if (context instanceof ActivityMain) {
            activityMain = (ActivityMain) context;
        } else {
            // Handle the case where the activity does not implement the method
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_database, container, false);
    }

    private LinearLayout databaseSetsLl;

    private void setupContents() {

        progressBar = binding.databaseProgressBar;
        databaseSv = binding.databaseSv;
        databaseSetsLl = binding.databaseSetsSvLl;
    }

    private void displaySetsInLinearLayout
            (List<ModelLearningSet> sets, LinearLayout linearLayout, LayoutInflater inflater) {
        linearLayout.removeAllViews();

        if (sets != null && sets.size() > 0) {
            for (ModelLearningSet set : sets) {
                View setItemView = inflater.inflate(R.layout.model_learning_set, linearLayout, false);

                LinearLayout setLl = setItemView.findViewById(R.id.learningSet_mainLl);

                TextView setNameTv = setItemView.findViewById(R.id.learningSet_nameTv);
                setNameTv.setVisibility(View.VISIBLE);
                setNameTv.setText(set.getName());

                linearLayout.addView(setItemView);
            }
        }
    }
}
