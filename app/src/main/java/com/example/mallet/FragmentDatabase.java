package com.example.mallet;

import android.content.Context;
import android.content.Intent;
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

import com.agh.api.SetBasicDTO;
import com.agh.api.SetInformationDTO;
import com.example.mallet.backend.client.configuration.ResponseHandler;
import com.example.mallet.backend.client.set.boundary.SetServiceImpl;
import com.example.mallet.backend.entity.set.ModelLearningSetMapper;
import com.example.mallet.databinding.FragmentDatabaseBinding;
import com.example.mallet.utils.AuthenticationUtils;
import com.example.mallet.utils.ModelLearningSet;
import com.example.mallet.utils.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentDatabase extends Fragment {
    private ActivityMain activityMain;
    private FragmentDatabaseBinding binding;
    private SetServiceImpl setService;
    private LinearLayout setsLl;
    private Animation fadeInAnimation;
    private LayoutInflater inflater;


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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDatabaseBinding.inflate(getLayoutInflater());
        this.inflater = inflater;
        String credential = AuthenticationUtils.get(getContext());
        this.setService = new SetServiceImpl(credential);

        setupContents();
        fetchSets();
        return binding.getRoot();
    }

    private void fetchSets() {
        setService.getBasicSet(0, 100, true, new Callback<SetBasicDTO>() {
            @Override
            public void onResponse(Call<SetBasicDTO> call, Response<SetBasicDTO> response) {
                SetBasicDTO setBasicDTO = ResponseHandler.handleResponse(response);
                List<SetInformationDTO> sets = setBasicDTO.sets();
                List<ModelLearningSet> modelLearningSets = ModelLearningSetMapper.from(sets);

                setupSetView(modelLearningSets);
            }

            @Override
            public void onFailure(Call<SetBasicDTO> call, Throwable t) {
                Utils.showToast(getContext(), "Network error");
            }
        });
    }

    private void setupSetView(List<ModelLearningSet> modelLearningSets) {
        for (ModelLearningSet set : modelLearningSets) {
            View view = inflater.inflate(R.layout.model_learning_set, setsLl, false);

            view.setOnClickListener(v -> viewSet(set));

            TextView setName = binding.databaseSetNameTv;
            TextView nrOfTerms = binding.databaseNrOfTermsTv;

            setName.setText(set.getName());

            if (set.getNrOfTerms() == 1) {
                nrOfTerms.setText(getActivity().getString(R.string.nr_of_terms_singular, String.valueOf(set.getNrOfTerms())));
            } else {
                nrOfTerms.setText(getActivity().getString(R.string.nr_of_terms_plural, String.valueOf(set.getNrOfTerms())));
            }

            view.startAnimation(AnimationUtils.loadAnimation(requireActivity(), R.anim.fade_in));

            setsLl.addView(view);

        }
    }

    private void viewSet(ModelLearningSet set) {
        Intent intent = new Intent(requireContext(), ActivityViewLearningSet.class);

        intent.putExtra("setId", set.getId());
        intent.putExtra("learningSet", set);

        intent.putExtra("isSetNew", false);
        intent.putExtra("isUserSet", true);
        intent.putExtra("isSetInGroup", false);

        startActivity(intent);
    }

    private void setupContents() {
        ProgressBar progressBar = binding.database_progressBar;

        setsLl = binding.databaseSetsLl;

        Utils.hideItems(progressBar);
    }
}
