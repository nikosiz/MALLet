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

import com.example.mallet.databinding.FragmentUserLibraryGroupsBinding;
import com.example.mallet.utils.ModelGroup;

import java.util.ArrayList;
import java.util.List;

public class FragmentUserLibrary_Groups extends Fragment {
    private FragmentUserLibraryGroupsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserLibraryGroupsBinding.inflate(inflater, container, false);

        fillText(inflater);

        return binding.getRoot();
    }

    private void fillText(LayoutInflater inflater) {
        LinearLayout userLibraryGroupsLl = binding.userLibraryGroupsLl;
        List<ModelGroup> userLibraryGroupsList = getGroupList();

        for (ModelGroup group : userLibraryGroupsList) {
            View groupItemView = inflater.inflate(R.layout.model_group, userLibraryGroupsLl, false);

            TextView groupNameTv = groupItemView.findViewById(R.id.group_nameTv);
            groupNameTv.setText(group.getGroupName());

            TextView groupSetsTv = groupItemView.findViewById(R.id.group_nrOfSetsTv);
            groupSetsTv.setText(group.getNumberOfSets() + " sets");

            // Add groupItemView to the linearLayout
            userLibraryGroupsLl.addView(groupItemView);

            // TODO pass needed data
            groupItemView.setOnClickListener(v -> startViewGroupActivity());
        }
    }

    private List<ModelGroup> getGroupList() {
        List<ModelGroup> groupList = new ArrayList<>();
        groupList.add(new ModelGroup("Group #1", "3","3"));
        groupList.add(new ModelGroup("Group #2", "7","3"));
        groupList.add(new ModelGroup("Group #3", "2","3"));
        groupList.add(new ModelGroup("Group #4", "8","3"));
        groupList.add(new ModelGroup("Group #5", "1","3"));
        return groupList;
    }

    private void startViewGroupActivity() {
        Intent intent = new Intent(getContext(), ActivityViewGroup.class);
        startActivity(intent);
    }
}