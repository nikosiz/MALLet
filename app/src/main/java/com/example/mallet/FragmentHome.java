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
import java.util.List;

public class FragmentHome extends Fragment {

    private FragmentHomeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentHomeBinding binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        List<ModelKit> homeKitList = getHomeKitList();
        for (ModelKit kit : homeKitList) {
            View kitItemView = inflater.inflate(R.layout.model_kit, binding.homeKitLl, false);

            TextView kitNameTv = kitItemView.findViewById(R.id.kit_model_name_tv);
            kitNameTv.setText(kit.getKitName());

            TextView kitTermsTv = kitItemView.findViewById(R.id.kit_model_terms_tv);
            kitTermsTv.setText(kit.getKitTerms() + " terms");

            TextView kitCreatorTv = kitItemView.findViewById(R.id.kit_model_creator_tv);
            kitCreatorTv.setText(kit.getKitCreator());

            // Add folderItemView to the linearLayout
            binding.homeKitLl.addView(kitItemView);
        }

        // Your code to populate the LinearLayout
        List<ModelFolder> homeFoldersList = getHomeFoldersList();
        for (ModelFolder folder : homeFoldersList) {
            View folderItemView = inflater.inflate(R.layout.model_folder, binding.homeFoldersLl, false);

            TextView folderNameTv = folderItemView.findViewById(R.id.folder_model_name_tv);
            folderNameTv.setText(folder.getFolderName());

            TextView folderCreatorTv = folderItemView.findViewById(R.id.folder_model_creator_tv);
            folderCreatorTv.setText(folder.getFolderCreator());

            // Add folderItemView to the linearLayout
            binding.homeFoldersLl.addView(folderItemView);
        }

        List<ModelGroup> homeGroupList = getHomeGroupList();
        for (ModelGroup group : homeGroupList) {
            View groupItemView = inflater.inflate(R.layout.model_group, binding.homeGroupsLl, false);

            TextView groupNameTv = groupItemView.findViewById(R.id.group_model_name_tv);
            groupNameTv.setText(group.getGroupName());

            // Add folderItemView to the linearLayout
            binding.homeGroupsLl.addView(groupItemView);
        }

        setupClickListeners(binding);

        return view;
    }

    private void setupClickListeners(FragmentHomeBinding binding) {
        binding.homeButton.setOnClickListener(v -> startLearnActivity());
        binding.homeKitViewAllTv.setOnClickListener(v->);
        binding.homeFoldersViewAllTv.setOnClickListener(v->);
        binding.homeGroupsViewAllTv.setOnClickListener(v->);
    }

    private List<ModelKit> getHomeKitList() {
        List<ModelKit> kitList = new ArrayList<>();
        kitList.add(new ModelKit("Set #1", "102", "user123"));
        kitList.add(new ModelKit("Set #2", "144", "user123"));
        kitList.add(new ModelKit("Set #3", "256", "user123"));
        kitList.add(new ModelKit("Set #4", "138", "user123"));
        kitList.add(new ModelKit("Set #5", "101", "user123"));
        return kitList;
    }

    private List<ModelFolder> getHomeFoldersList() {
        List<ModelFolder> folderList = new ArrayList<>();
        folderList.add(new ModelFolder("user123", "Folder #1"));
        folderList.add(new ModelFolder("user123", "Folder #2"));
        folderList.add(new ModelFolder("user123", "Folder #3"));
        folderList.add(new ModelFolder("user123", "Folder #4"));
        folderList.add(new ModelFolder("user123", "Folder #5"));
        return folderList;
    }

    private List<ModelGroup> getHomeGroupList() {
        List<ModelGroup> groupList = new ArrayList<>();
        groupList.add(new ModelGroup("Group #1"));
        groupList.add(new ModelGroup("Group #2"));
        groupList.add(new ModelGroup("Group #3"));
        groupList.add(new ModelGroup("Group #4"));
        groupList.add(new ModelGroup("Group #5"));
        return groupList;
    }

    private void startLearnActivity() {
        Intent intent = new Intent(getContext(), ActivityLearn.class);
        startActivity(intent);
    }
}
