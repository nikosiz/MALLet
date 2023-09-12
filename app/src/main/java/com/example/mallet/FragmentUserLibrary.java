package com.example.mallet;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mallet.databinding.FragmentUserLibraryBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;
import java.util.Objects;

public class FragmentUserLibrary extends Fragment {
    FragmentUserLibraryBinding binding;
    private TabLayout tabLayout;
    private ActivityMain activityMain;
    private ViewPager2 viewPager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUserLibraryBinding.inflate(inflater, container, false);
        activityMain = (ActivityMain) getActivity();

        viewPager = binding.userLibraryViewPager;
        tabLayout = binding.userLibraryTabLayout;

        assert activityMain != null;
        List<ModelLearningSet> learningSets = activityMain.createSetList();
        List<ModelFolder> folders = activityMain.createFolderList();
        List<ModelGroup> groups = activityMain.createGroupList();

        setupContents(); // Call the setupTabLayout method

        return binding.getRoot();
    }

    private void setupContents() {
        setupTabLayout();
    }

    private void setupTabLayout() {
        FragmentStateAdapter adapter = new FragmentStateAdapter(getChildFragmentManager(), getLifecycle()) {
            @Override
            public int getItemCount() {
                return 3; // Number of tabs
            }

            @NonNull
            @Override
            public Fragment createFragment(int position) {
                Fragment result = null;
                // Return the appropriate fragment for each tab
                if (position == 0) {
                    result = new FragmentUserLibrarySets();
                } else if (position == 1) {
                    result = new FragmentUserLibraryFolders();
                } else if (position == 2) {
                    result = new FragmentUserLibraryGroups();
                }
                return Objects.requireNonNull(result);
            }
        };

        viewPager.setAdapter(adapter);

        // Wait for the adapter to set up the tabs before setting tab titles
        viewPager.post(() -> {
            Objects.requireNonNull(tabLayout.getTabAt(0)).setText("Sets");
            Objects.requireNonNull(tabLayout.getTabAt(1)).setText("Folders");
            Objects.requireNonNull(tabLayout.getTabAt(2)).setText("Groups");

            // Customize the text size for each tab
            for (int i = 0; i < tabLayout.getTabCount(); i++) {
                TextView tabTV = (TextView) LayoutInflater.from(requireContext())
                        .inflate(R.layout.tab_text, tabLayout, false); // Provide tabLayout as the root view
                tabTV.setText(Objects.requireNonNull(tabLayout.getTabAt(i)).getText());
                tabTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15); // Adjust the size as needed
                Objects.requireNonNull(tabLayout.getTabAt(i)).setCustomView(tabTV);

                ViewGroup.LayoutParams layoutParams = tabTV.getLayoutParams();
                layoutParams.width = 200;
                tabTV.setLayoutParams(layoutParams);
            }
        });

        // Connect the TabLayout with the ViewPager
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    // Nothing needed here since tab titles are set beforehand
                }
        ).attach();
    }
}
