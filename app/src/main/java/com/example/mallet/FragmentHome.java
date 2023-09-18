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
import com.example.mallet.utils.ModelFolder;
import com.example.mallet.utils.ModelGroup;
import com.example.mallet.utils.ModelLearningSet;
import com.example.mallet.utils.Utils;

import java.util.List;
import java.util.Objects;

public class FragmentHome extends Fragment {

    private FragmentHomeBinding binding;
    private ActivityMain activityMain;
    private List<ModelLearningSet> learningSets;
    private List<ModelFolder> folders;
    private List<ModelGroup> groups;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        setupContents();

        activityMain = (ActivityMain) getActivity();

        learningSets = Objects.requireNonNull(activityMain).createSetList();
        setupViewPager(binding.homeSetsViewPager, learningSets);

        List<ModelFolder> folders = activityMain.createFolderList();
        setupViewPager(binding.homeSetsViewPager, folders);

        List<ModelGroup> groups = activityMain.createGroupList();
        setupViewPager(binding.homeSetsViewPager, groups);

        // Set up click listeners for "View All" buttons
        setupContents();

        return binding.getRoot();
    }

    private void setupContents() {
        binding.homeSetViewAllTv.setOnClickListener(v -> showAllItems("Learning Sets"));
        binding.homeFolderViewAllTv.setOnClickListener(v -> showAllItems("Folders"));
        binding.homeGroupViewAllTv.setOnClickListener(v -> showAllItems("Groups"));
    }

    private void setupViewPager(ViewPager2 viewPager, List<?> itemList) {
        // Determine the appropriate adapter based on the item type
        if (!itemList.isEmpty()) {
            Object item = itemList.get(0);
            if (item instanceof ModelLearningSet) {
                viewPager = binding.homeSetsViewPager;
                AdapterLearningSet adapterSets = new AdapterLearningSet(getContext(), activityMain.createSetList(), learningSet -> {
                    Intent intent = new Intent(getContext(), ActivityViewLearningSet.class);

                    // Pass the entire learning set object
                    intent.putExtra("learningSet", learningSet);

                    startActivity(intent);
                });

                viewPager.setAdapter(adapterSets);

                viewPager.setPageTransformer(Utils::applySwipeTransformer);
            } else if (item instanceof ModelFolder) {
                viewPager = binding.homeFoldersViewPager;
                AdapterFolder adapterFolders = new AdapterFolder(getContext(), activityMain.createFolderList(), v -> Utils.openActivity(getContext(), ActivityViewFolder.class));

                viewPager.setAdapter(adapterFolders);

                viewPager.setPageTransformer(Utils::applySwipeTransformer);
            } else if (item instanceof ModelGroup) {
                viewPager = binding.homeGroupsViewPager;
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
