package com.mallet.frontend.view.common.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.agh.api.SetBasicDTO;
import com.agh.api.SetInformationDTO;
import com.mallet.R;
import com.mallet.backend.client.configuration.ResponseHandler;
import com.mallet.backend.client.set.boundary.SetServiceImpl;
import com.mallet.backend.mapper.set.ModelLearningSetMapper;
import com.mallet.databinding.FragmentDatabaseBinding;
import com.mallet.frontend.model.set.ModelLearningSet;
import com.mallet.frontend.security.CredentialsHandler;
import com.mallet.frontend.utils.ViewUtils;
import com.mallet.frontend.view.common.activity.ActivityMain;
import com.mallet.frontend.view.set.ActivityViewLearningSet;

import java.util.List;
import java.util.Objects;

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
        String credential = CredentialsHandler.get(getContext());
        this.setService = new SetServiceImpl(credential);

        setupContents();
        fetchSets();
        return binding.getRoot();
    }

    private void fetchSets() {
        setService.getBasicSet(0, 100, true, new Callback<SetBasicDTO>() {
            @Override
            public void onResponse(Call<SetBasicDTO> call, Response<SetBasicDTO> response) {
                if (Objects.isNull(getView())) {
                    return;
                }
                SetBasicDTO setBasicDTO = ResponseHandler.handleResponse(response);
                List<SetInformationDTO> sets = setBasicDTO.sets();
                List<ModelLearningSet> modelLearningSets = ModelLearningSetMapper.from(sets);

                setupSetView(modelLearningSets);
            }

            @Override
            public void onFailure(Call<SetBasicDTO> call, Throwable t) {
                ViewUtils.showToast(getContext(), "Network error");
            }
        });
    }

    private void setupSetView(List<ModelLearningSet> modelLearningSets) {
        for (ModelLearningSet set : modelLearningSets) {
            View setItemView = getLayoutInflater().inflate(R.layout.model_learning_set, setsLl, false);

            setItemView.setOnClickListener(v -> viewSet(set));

            ImageView deleteIv = setItemView.findViewById(R.id.learningSet_deleteIv);

            TextView setName = setItemView.findViewById(R.id.learningSet_nameTv);
            TextView setIdentifierTv = setItemView.findViewById(R.id.learningSet_identifierTv);
            TextView nrOfTerms = setItemView.findViewById(R.id.learningSet_nrOfTermsTv);

            View aboveCreator = setItemView.findViewById(R.id.learningSet_aboveCreator);
            TextView creatorTv = setItemView.findViewById(R.id.learningSet_creatorTv);
            ViewUtils.hideItems(deleteIv, aboveCreator, creatorTv);

            setName.setText(set.getName());
            setIdentifierTv.setText(set.getIdentifier());

            if (set.getNrOfTerms() == 1) {
                nrOfTerms.setText(getActivity().getString(R.string.nr_of_terms_singular, String.valueOf(set.getNrOfTerms())));
            } else {
                nrOfTerms.setText(getActivity().getString(R.string.nr_of_terms_plural, String.valueOf(set.getNrOfTerms())));
            }

            setItemView.startAnimation(AnimationUtils.loadAnimation(requireActivity(), R.anim.fade_in));

            setsLl.addView(setItemView);

        }
    }

    private void viewSet(ModelLearningSet set) {
        Intent intent = new Intent(requireContext(), ActivityViewLearningSet.class);

        intent.putExtra("setId", set.getId());
        intent.putExtra("learningSet", set);

        intent.putExtra("isSetNew", false);
        intent.putExtra("isUserSet", false);
        intent.putExtra("isSetInGroup", false);

        startActivity(intent);
    }

    private void setupContents() {
        ProgressBar progressBar = binding.databaseProgressBar;

        setsLl = binding.databaseSetsLl;

        ViewUtils.hideItems(progressBar);
    }
}
