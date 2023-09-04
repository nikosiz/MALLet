package com.example.mallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.mallet.databinding.FragmentUserLibraryGroupsBinding;

import java.util.ArrayList;
import java.util.List;

public class FragmentUserLibraryGroups extends Fragment {
    private FragmentUserLibraryGroupsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
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

            TextView groupNameTV = groupItemView.findViewById(R.id.group_model_name_tv);
            groupNameTV.setText(group.getGroupName());

            TextView groupSetsTV = groupItemView.findViewById(R.id.group_model_sets_tv);
            groupSetsTV.setText(group.getSetAmount() + " sets");

            // Add groupItemView to the linearLayout
            userLibraryGroupsLl.addView(groupItemView);

            // TODO pass needed data
            groupItemView.setOnClickListener(v -> {
                startViewGroupActivity();
            });
        }
    }

    private List<ModelGroup> getGroupList() {
        List<ModelGroup> groupList = new ArrayList<>();
        groupList.add(new ModelGroup("Group #1", "3"));
        groupList.add(new ModelGroup("Group #2", "7"));
        groupList.add(new ModelGroup("Group #3", "2"));
        groupList.add(new ModelGroup("Group #4", "8"));
        groupList.add(new ModelGroup("Group #5", "1"));
        return groupList;
    }

    private void startViewGroupActivity() {
        Intent intent = new Intent(getContext(), ActivityViewGroup.class);
        startActivity(intent);
    }
}