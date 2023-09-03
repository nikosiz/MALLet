package com.example.mallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mallet.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentHome extends Fragment {

    private FragmentHomeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentHomeBinding binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        List<ModelLearningSet> homeLearningSetList = getHomeLearningSetList();
        List<ModelFolder> homeFolderList = getHomeFolderList();
        List<ModelGroup> homeGroupList = getHomeGroupList();

        setupClickListeners(binding);
        displaySets(homeLearningSetList, binding, inflater);
        displayFolders(homeFolderList, binding, inflater);
        displayGroups(homeGroupList, binding, inflater);
        return view;
    }

    private void setupClickListeners(FragmentHomeBinding binding) {
        binding.homeButton.setOnClickListener(v -> startViewSetActivity());
        binding.homeLearningSetViewAllTv.setOnClickListener(v -> showAllLearningSets());
        binding.homeFolderViewAllTv.setOnClickListener(v -> showAllFolders());
        binding.homeGroupViewAllTv.setOnClickListener(v -> showAllGroups());
    }

    private void displaySets(List<ModelLearningSet> learningSetList, FragmentHomeBinding binding, LayoutInflater inflater) {
        for (ModelLearningSet learningSet : learningSetList) {
            View learningSetItemView = inflater.inflate(R.layout.model_learning_set, binding.homeLearningSetLl, false);

            TextView learningSetNameTv = learningSetItemView.findViewById(R.id.learning_set_model_name_tv);
            learningSetNameTv.setText(learningSet.getLearningSetName());

            TextView learningSetTermsTv = learningSetItemView.findViewById(R.id.learning_set_model_terms_tv);
            learningSetTermsTv.setText(learningSet.getLearningSetTerms() + " terms");

            TextView learningSetCreatorTv = learningSetItemView.findViewById(R.id.learning_set_model_creator_tv);
            learningSetCreatorTv.setText(learningSet.getLearningSetCreator());

            // Add folderItemView to the linearLayout
            binding.homeLearningSetHsvLl.addView(learningSetItemView);

            // Set click listener for the learningSetItemView
            learningSetItemView.setOnClickListener(v -> {
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("set_name", learningSet.getLearningSetName());
                dataMap.put("set_creator", learningSet.getLearningSetCreator());
                //dataMap.put("set_word", learningSet.getLearningSetWord());
                //dataMap.put("set_definition", learningSet.getLearningSetDefinition());
                Intent intent = new Intent(getContext(), ActivityViewLearningSet.class);
                passDataToActivity(intent, dataMap);
                startActivity(intent);
            });
        }
    }

    private void displayFolders(List<ModelFolder> folderList, FragmentHomeBinding binding, LayoutInflater inflater) {
        for (ModelFolder folder : folderList) {
            View folderItemView = inflater.inflate(R.layout.model_folder, binding.homeFolderLl, false);

            TextView folderNameTv = folderItemView.findViewById(R.id.folder_model_name_tv);
            folderNameTv.setText(folder.getFolderName());

            TextView folderCreatorTv = folderItemView.findViewById(R.id.folder_model_creator_tv);
            folderCreatorTv.setText(folder.getFolderCreator());

            // Add folderItemView to the linearLayout
            binding.homeFoldersHsvLl.addView(folderItemView);

            // Set click listener for the learningSetItemView
            folderItemView.setOnClickListener(v -> {
                Map<String, Object> dataMap = new HashMap<>();
                //TODO: Change to:
                // intent.putExtra("learningSetId", learningSet.getId());
                //intent.putExtra("folder_name", folder.getFolderName());
                dataMap.put("folder_name", folder.getFolderName());
                dataMap.put("folder_creator", folder.getFolderCreator());
                dataMap.put("folder_sets", folder.getSets());
                Intent intent = new Intent(getContext(), ActivityViewFolder.class);
                passDataToActivity(intent, dataMap);
                startActivity(intent);
            });
        }
    }

    private void displayGroups(List<ModelGroup> groupList, FragmentHomeBinding binding, LayoutInflater inflater) {
        for (ModelGroup group : groupList) {
            View groupItemView = inflater.inflate(R.layout.model_group, binding.homeGroupLl, false);

            TextView groupNameTv = groupItemView.findViewById(R.id.group_model_name_tv);
            groupNameTv.setText(group.getGroupName());

            // Add folderItemView to the linearLayout
            binding.homeGroupHsvLl.addView(groupItemView);

            groupItemView.setOnClickListener(v -> {
                Map<String, Object> dataMap = new HashMap<>();

                dataMap.put("group_name", group.getGroupName());
                dataMap.put("group_sets", group.getSetAmount());

                Intent intent = new Intent(getContext(), ActivityViewGroup.class);
                passDataToActivity(intent, dataMap);
                startActivity(intent);
            });
        }
    }

    private void showAllGroups() {
        // TODO: Here should open UserLibraryFragment with Groups tab selected
        FrontendUtils.showToast(getContext(), "Here should open UserLibraryFragment with Groups tab selected");
    }

    private void showAllFolders() {
        // TODO: Here should open UserLibraryFragment with Folders tab selected
        FrontendUtils.showToast(getContext(), "Here should open UserLibraryFragment with Folders tab selected");

    }

    private void showAllLearningSets() {
        // TODO: Here should open UserLibraryFragment with Sets tab selected
        FrontendUtils.showToast(getContext(), "Here should open UserLibraryFragment with Sets tab selected");
    }


    private List<ModelLearningSet> getHomeLearningSetList() {
        List<ModelLearningSet> learningSetList = new ArrayList<>();
        learningSetList.add(new ModelLearningSet("Set #1", "102", "user123", "Apple", "Jabłko"));
        learningSetList.add(new ModelLearningSet("Set #2", "144", "user123", "Apple", "Jabłko"));
        learningSetList.add(new ModelLearningSet("Set #3", "256", "user123", "Apple", "Jabłko"));
        learningSetList.add(new ModelLearningSet("Set #4", "138", "user123", "Apple", "Jabłko"));
        learningSetList.add(new ModelLearningSet("Set #5", "101", "user123", "Apple", "Jabłko"));
        return learningSetList;
    }

    private List<ModelFolder> getHomeFolderList() {
        List<ModelFolder> folderList = new ArrayList<>();
        folderList.add(new ModelFolder("Folder #1", "user123", "3"));
        folderList.add(new ModelFolder("Folder #2", "user123", "7"));
        folderList.add(new ModelFolder("Folder #3", "user123", "2"));
        folderList.add(new ModelFolder("Folder #4", "user123", "8"));
        folderList.add(new ModelFolder("Folder #5", "user123", "1"));
        return folderList;
    }

    private List<ModelGroup> getHomeGroupList() {
        List<ModelGroup> groupList = new ArrayList<>();
        groupList.add(new ModelGroup("Group #1", "2"));
        groupList.add(new ModelGroup("Group #2", "5"));
        groupList.add(new ModelGroup("Group #3", "2"));
        groupList.add(new ModelGroup("Group #4", "8"));
        groupList.add(new ModelGroup("Group #5", "5"));
        return groupList;
    }

    private void startViewSetActivity() {
        Intent intent = new Intent(getContext(), ActivityViewLearningSet.class);
        startActivity(intent);
    }

    public static void passDataToActivity(Intent intent, Map<String, Object> dataMap) {
        for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof String) {
                intent.putExtra(key, (String) value);
            } else if (value instanceof Integer) {
                intent.putExtra(key, (int) value);
            } else if (value instanceof Boolean) {
                intent.putExtra(key, (boolean) value);
            } else if (value instanceof Float) {
                intent.putExtra(key, (float) value);
            } // Add more data types as needed
        }
    }

}
