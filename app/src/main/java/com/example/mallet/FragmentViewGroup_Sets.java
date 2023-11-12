package com.example.mallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.agh.api.GroupDTO;
import com.example.mallet.backend.entity.set.ModelLearningSetMapper;
import com.example.mallet.databinding.FragmentViewGroupSetsBinding;
import com.example.mallet.utils.ModelLearningSet;

import java.util.List;

public class FragmentViewGroup_Sets extends Fragment {
    private FragmentViewGroupSetsBinding binding;
    private LinearLayout userLibrarySetsLl;
    private final GroupDTO chosenGroup;

    public FragmentViewGroup_Sets(GroupDTO chosenGroup) {
        this.chosenGroup = chosenGroup;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentViewGroupSetsBinding.inflate(inflater, container, false);

        setupContents();

        List<ModelLearningSet> userLibrarySetList = getUserLibrarySetList();

        for (ModelLearningSet set : userLibrarySetList) {
            View setItemView = inflater.inflate(R.layout.model_learning_set, userLibrarySetsLl, false);

            TextView setName = setItemView.findViewById(R.id.learningSet_nameTv);
            TextView nrOfTerms = setItemView.findViewById(R.id.learningSet_nrOfTermsTv);
            TextView creator = setItemView.findViewById(R.id.learningSet_creatorTv);

            setName.setText(set.getName());
            nrOfTerms.setText(String.valueOf(set.getNrOfTerms()));
            creator.setText(set.getCreator());

            userLibrarySetsLl.addView(setItemView);
            setItemView.setOnClickListener(view -> openActivityViewSet(set));

        }

        return binding.getRoot();
    }

    private void setupContents() {
        userLibrarySetsLl = binding.viewGroupSetsSetListLl;
    }

    private List<ModelLearningSet> getUserLibrarySetList() {
        return ModelLearningSetMapper.from(chosenGroup.sets());
    }

    private void openActivityViewSet(ModelLearningSet set) {
        Intent intent = new Intent(getContext(), ActivityViewLearningSet.class);

        intent.putExtra("setId", set.getId());
        intent.putExtra("learningSet", set);
        intent.putExtra("isSetInGroup", true);

        startActivity(intent);
    }

}
