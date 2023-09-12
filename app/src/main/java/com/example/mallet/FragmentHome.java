package com.example.mallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mallet.databinding.FragmentHomeBinding;
import com.example.mallet.utils.AdapterFolder;
import com.example.mallet.utils.AdapterGroup;
import com.example.mallet.utils.AdapterLearningSet;
import com.example.mallet.utils.Utils;

import java.util.List;

public class FragmentHome extends Fragment {

    private FragmentHomeBinding binding;
    private ActivityMain activityMain;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        setupContents();

        activityMain = (ActivityMain) getActivity();

        assert activityMain != null;
        List<ModelLearningSet> learningSets = activityMain.createSetList();
        setupViewPager(binding.homeSetsViewpager, learningSets);

        List<ModelFolder> folders = activityMain.createFolderList();
        setupViewPager(binding.homeSetsViewpager, folders);

        List<ModelGroup> groups = activityMain.createGroupList();
        setupViewPager(binding.homeSetsViewpager, groups);

        // Set up click listeners for "View All" buttons
        setupContents();

        return binding.getRoot();
    }

    private void setupContents() {
        binding.homeLearningSetViewAllTv.setOnClickListener(v -> showAllItems("Learning Sets"));
        binding.homeFolderViewAllTv.setOnClickListener(v -> showAllItems("Folders"));
        binding.homeGroupViewAllTv.setOnClickListener(v -> showAllItems("Groups"));
    }

    private void setupViewPager(ViewPager2 viewPager, List<?> itemList) {
        // Determine the appropriate adapter based on the item type
        if (!itemList.isEmpty()) {
            Object item = itemList.get(0);
            if (item instanceof ModelLearningSet) {
                viewPager = binding.homeSetsViewpager;
                AdapterLearningSet adapterSets = new AdapterLearningSet(getContext(), activityMain.createSetList(), learningSet -> {
                    Intent intent = new Intent(getContext(), ActivityViewLearningSet.class);

                    // Pass the entire learning set object
                    intent.putExtra("learningSet", learningSet);

                    startActivity(intent);
                });

                viewPager.setAdapter(adapterSets);

                viewPager.setPageTransformer(Utils::applySwipeTransformer);
            } else if (item instanceof ModelFolder) {
                viewPager = binding.homeFoldersViewpager;
                AdapterFolder adapterFolders = new AdapterFolder(getContext(), activityMain.createFolderList(), v -> Utils.openActivity(getContext(), ActivityViewFolder.class));

                viewPager.setAdapter(adapterFolders);

                viewPager.setPageTransformer(Utils::applySwipeTransformer);
            } else if (item instanceof ModelGroup) {
                viewPager = binding.homeGroupsViewpager;
                AdapterGroup adapterGroups = new AdapterGroup(getContext(), activityMain.createGroupList(), v -> Utils.openActivity(getContext(), ActivityViewGroup.class));
                viewPager.setAdapter(adapterGroups);

                viewPager.setPageTransformer(Utils::applySwipeTransformer);
            }
        }

        // Apply a swipe transformer
        viewPager.setPageTransformer(Utils::applySwipeTransformer);
    }

    // Show all items (e.g., open a list view with all items of a specific type)
    private void showAllItems(String itemType) {
        // TODO: Implement this method to display all items of a specific type
        Utils.showToast(getContext(), "Here should open a list of " + itemType);
    }

}
