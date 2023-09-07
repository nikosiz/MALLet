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
import com.example.mallet.utils.AdapterFlashcard;
import com.example.mallet.utils.AdapterFolder;
import com.example.mallet.utils.AdapterGroup;
import com.example.mallet.utils.AdapterLearningSet;
import com.example.mallet.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FragmentHome extends Fragment {

    private FragmentHomeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        // Set up click listeners for "View All" buttons
        setupClickListeners(binding);

        // Set up ViewPager for Learning Sets
        setupViewPager(binding.homeSetsViewpager, createSetList());

        // Set up ViewPager for Folders
        setupViewPager(binding.homeFoldersViewpager, createFolderList());

        // Set up ViewPager for Groups
        setupViewPager(binding.homeGroupsViewpager, createGroupList());

        return binding.getRoot();

    }

    private void setupViewPager(ViewPager2 viewPager, List<?> itemList) {
        // Determine the appropriate adapter based on the item type
        if (!itemList.isEmpty()) {
            Object item = itemList.get(0);
            if (item instanceof ModelLearningSet) {
                viewPager = binding.homeSetsViewpager;
                AdapterLearningSet adapterSets = new AdapterLearningSet(getContext(), createSetList(), learningSet -> {
                    Intent intent = new Intent(getContext(), ActivityViewLearningSet.class);

                    // Pass the entire learning set object
                    intent.putExtra("learningSet", learningSet);

                    startActivity(intent);
                });

                viewPager.setAdapter(adapterSets);

                viewPager.setPageTransformer(Utils::applySwipeTransformer);
            } else if (item instanceof ModelFolder) {
                viewPager = binding.homeFoldersViewpager;
                AdapterFolder adapterFolders = new AdapterFolder(getContext(), createFolderList(), v -> Utils.openActivity(getContext(), ActivityViewFolder.class));

                viewPager.setAdapter(adapterFolders);

                viewPager.setPageTransformer(Utils::applySwipeTransformer);
            } else if (item instanceof ModelGroup) {
                viewPager = binding.homeGroupsViewpager;
                AdapterGroup adapterGroups = new AdapterGroup(getContext(), createGroupList(), v -> Utils.openActivity(getContext(), ActivityViewGroup.class));
                viewPager.setAdapter(adapterGroups);

                viewPager.setPageTransformer(Utils::applySwipeTransformer);
            }
        }

        // Apply a swipe transformer
        viewPager.setPageTransformer(Utils::applySwipeTransformer);
    }

    // Create a list of learning sets
    private List<ModelLearningSet> createSetList() {
        List<ModelLearningSet> sets = new ArrayList<>();

        ModelLearningSet set1 = FlashcardManager.readFlashcards(getContext(), "fruit.txt");
        ModelLearningSet set2 = FlashcardManager.readFlashcards(getContext(), "animals.txt");
        ModelLearningSet set3 = FlashcardManager.readFlashcards(getContext(), "numbers.txt");
        ModelLearningSet set4 = FlashcardManager.readFlashcards(getContext(), "countries.txt");
        ModelLearningSet set5 = FlashcardManager.readFlashcards(getContext(), "colors.txt");

        sets.add(set1);
        sets.add(set2);
        sets.add(set3);
        sets.add(set4);
        sets.add(set5);

        return sets;
    }


    // Create a list of folders
    private List<ModelFolder> createFolderList() {
        List<ModelFolder> folders = new ArrayList<>();
        folders.add(new ModelFolder("Folder #1", "user123", "3"));
        folders.add(new ModelFolder("Folder #2", "user123", "7"));
        folders.add(new ModelFolder("Folder #3", "user123", "2"));
        folders.add(new ModelFolder("Folder #4", "user123", "8"));
        folders.add(new ModelFolder("Folder #5", "user123", "1"));
        return folders;
    }

    // Create a list of groups
    private List<ModelGroup> createGroupList() {
        List<ModelGroup> groups = new ArrayList<>();
        groups.add(new ModelGroup("Group #1", "2"));
        groups.add(new ModelGroup("Group #2", "5"));
        groups.add(new ModelGroup("Group #3", "2"));
        groups.add(new ModelGroup("Group #4", "8"));
        groups.add(new ModelGroup("Group #5", "5"));
        return groups;
    }

    // Set up click listeners for "View All" buttons
    private void setupClickListeners(FragmentHomeBinding binding) {
        binding.homeLearningSetViewAllTv.setOnClickListener(v -> showAllItems("Learning Sets"));
        binding.homeFolderViewAllTv.setOnClickListener(v -> showAllItems("Folders"));
        binding.homeGroupViewAllTv.setOnClickListener(v -> showAllItems("Groups"));
    }

    // Show all items (e.g., open a list view with all items of a specific type)
    private void showAllItems(String itemType) {

        // TODO: Implement this method to display all items of a specific type
        Utils.showToast(getContext(), "Here should open a list of " + itemType);
    }

    // Utility method to pass data to an activity via an intent
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
