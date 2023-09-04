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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentHome extends Fragment {

    private FragmentHomeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        setupClickListeners(binding);

        setupSetsViewpager();
        setupFoldersViewpager();
        setupGroupsViewpager();

        return binding.getRoot();
    }

    private void setupSetsViewpager() {
        ViewPager2 viewPager = binding.homeSetsViewpager;
        AdapterLearningSet adapterSets = new AdapterLearningSet(getContext(), createSetList(), v -> startViewSetActivity());
        viewPager.setAdapter(adapterSets);

        // Set a PageTransformer to achieve the scaling and centering effect
        viewPager.setPageTransformer(new ViewPager2.PageTransformer() {
            private static final float MIN_Y_SCALE = 0.9f;
            private static final float MIN_X_SCALE = 0.9f;

            private static final float MIN_ALPHA = 0.9f;

            @Override
            public void transformPage(@NonNull View page, float position) {
                float absPosition = Math.abs(position);

                // Center element (scale it fully)
                if (absPosition < 1) {
                    page.setScaleY(Math.max(MIN_Y_SCALE, 1 - absPosition));
                    page.setScaleX(Math.max(MIN_X_SCALE, 1 - absPosition));
                    page.setAlpha(Math.max(MIN_ALPHA, 1 - absPosition));
                }
                // Elements to the left
                else if (position < 0) {
                    page.setScaleY(MIN_Y_SCALE);
                    page.setScaleX(MIN_X_SCALE);

                    page.setAlpha(MIN_ALPHA);
                }
                // Elements to the right
                else {
                    page.setScaleY(MIN_Y_SCALE);
                    page.setScaleX(MIN_X_SCALE);
                    page.setAlpha(MIN_ALPHA);
                }
            }
        });
    }

    // Create a list of flashcards (you can replace this with your actual data)
    private List<ModelLearningSet> createSetList() {
        List<ModelLearningSet> sets = new ArrayList<>();

        sets.add(new ModelLearningSet("Set #1", "102", "user123", 566));
        sets.add(new ModelLearningSet("Set #2", "144", "user123", 915));
        sets.add(new ModelLearningSet("Set #3", "256", "user123", 684));
        sets.add(new ModelLearningSet("Set #4", "138", "user123", 694));
        sets.add(new ModelLearningSet("Set #5", "101", "user123", 354));

        return sets;
    }

    private void setupFoldersViewpager() {
        ViewPager2 viewPager = binding.homeFoldersViewpager;
        AdapterFolder adapterFolders = new AdapterFolder(getContext(), createFolderList(), v -> startViewFolderActivity());

        viewPager.setAdapter(adapterFolders);

        // Set a PageTransformer to achieve the scaling and centering effect
        viewPager.setPageTransformer(new ViewPager2.PageTransformer() {
            private static final float MIN_Y_SCALE = 0.9f;
            private static final float MIN_X_SCALE = 0.9f;

            private static final float MIN_ALPHA = 0.9f;

            @Override
            public void transformPage(@NonNull View page, float position) {
                float absPosition = Math.abs(position);

                // Center element (scale it fully)
                if (absPosition < 1) {
                    page.setScaleY(Math.max(MIN_Y_SCALE, 1 - absPosition));
                    page.setScaleX(Math.max(MIN_X_SCALE, 1 - absPosition));
                    page.setAlpha(Math.max(MIN_ALPHA, 1 - absPosition));
                }
                // Elements to the left
                else if (position < 0) {
                    page.setScaleY(MIN_Y_SCALE);
                    page.setScaleX(MIN_X_SCALE);

                    page.setAlpha(MIN_ALPHA);
                }
                // Elements to the right
                else {
                    page.setScaleY(MIN_Y_SCALE);
                    page.setScaleX(MIN_X_SCALE);
                    page.setAlpha(MIN_ALPHA);
                }
            }
        });
    }

    // Create a list of flashcards (you can replace this with your actual data)
    private List<ModelFolder> createFolderList() {
        List<ModelFolder> folders = new ArrayList<>();
        folders.add(new ModelFolder("Folder #1", "user123", "3"));
        folders.add(new ModelFolder("Folder #2", "user123", "7"));
        folders.add(new ModelFolder("Folder #3", "user123", "2"));
        folders.add(new ModelFolder("Folder #4", "user123", "8"));
        folders.add(new ModelFolder("Folder #5", "user123", "1"));
        // Add more flashcards as needed
        return folders;
    }

    private void setupGroupsViewpager() {
        ViewPager2 viewPager = binding.homeGroupsViewpager;
        AdapterGroup adapterGroups = new AdapterGroup(getContext(), createGroupList(), v -> startViewGroupActivity());
        viewPager.setAdapter(adapterGroups);

        // Set a PageTransformer to achieve the scaling and centering effect
        viewPager.setPageTransformer(new ViewPager2.PageTransformer() {
            private static final float MIN_Y_SCALE = 0.9f;
            private static final float MIN_X_SCALE = 0.9f;

            private static final float MIN_ALPHA = 0.9f;

            @Override
            public void transformPage(@NonNull View page, float position) {
                float absPosition = Math.abs(position);

                // Center element (scale it fully)
                if (absPosition < 1) {
                    page.setScaleY(Math.max(MIN_Y_SCALE, 1 - absPosition));
                    page.setScaleX(Math.max(MIN_X_SCALE, 1 - absPosition));
                    page.setAlpha(Math.max(MIN_ALPHA, 1 - absPosition));
                }
                // Elements to the left
                else if (position < 0) {
                    page.setScaleY(MIN_Y_SCALE);
                    page.setScaleX(MIN_X_SCALE);

                    page.setAlpha(MIN_ALPHA);
                }
                // Elements to the right
                else {
                    page.setScaleY(MIN_Y_SCALE);
                    page.setScaleX(MIN_X_SCALE);
                    page.setAlpha(MIN_ALPHA);
                }
            }
        });
    }

    // Create a list of flashcards (you can replace this with your actual data)
    private List<ModelGroup> createGroupList() {
        List<ModelGroup> groups = new ArrayList<>();
        groups.add(new ModelGroup("Group #1", "2"));
        groups.add(new ModelGroup("Group #2", "5"));
        groups.add(new ModelGroup("Group #3", "2"));
        groups.add(new ModelGroup("Group #4", "8"));
        groups.add(new ModelGroup("Group #5", "5"));
        return groups;
    }

    private void setupClickListeners(FragmentHomeBinding binding) {
        binding.homeLearningSetViewAllTv.setOnClickListener(v -> showAllLearningSets());
        binding.homeFolderViewAllTv.setOnClickListener(v -> showAllFolders());
        binding.homeGroupViewAllTv.setOnClickListener(v -> showAllGroups());
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

    private void startViewSetActivity() {
        Intent intent = new Intent(getContext(), ActivityViewLearningSet.class);

        List<ModelLearningSet> sets = createSetList();
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("learning_sets", sets);
        passDataToActivity(intent, dataMap);
        startActivity(intent);
    }

    private void startViewFolderActivity() {
        Intent intent = new Intent(getContext(), ActivityViewFolder.class);
        startActivity(intent);
    }

    private void startViewGroupActivity() {
        Intent intent = new Intent(getContext(), ActivityViewGroup.class);
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
