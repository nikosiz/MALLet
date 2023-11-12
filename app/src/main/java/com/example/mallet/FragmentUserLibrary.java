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

import java.util.Objects;

public class FragmentUserLibrary extends Fragment {
    FragmentUserLibraryBinding binding;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    public static FragmentUserLibrary newInstance(int selectedTabIndex) {
        FragmentUserLibrary fragment = new FragmentUserLibrary();
        Bundle args = new Bundle();
        args.putInt("selectedTabIndex", selectedTabIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUserLibraryBinding.inflate(inflater, container, false);
        ActivityMain activityMain = (ActivityMain) requireActivity();

        viewPager = binding.userLibraryVp2;
        tabLayout = binding.userLibraryTl;

       // List<ModelLearningSet> learningSets = activityMain.createSetList();
       // List<ModelFolder> folders = activityMain.createFolderList();
       // List<ModelGroup> groups = activityMain.createGroupList();

        setupContents(); // Call the setupTabLayout method

        return binding.getRoot();
    }
    int selectedTabIndex;
    private void setupContents() {
//        selectedTabIndex = getArguments().getInt("selectedTabIndex", 0); // Default to 0 if not provided
        setupTabLayout();
    }



    private void setupTabLayout() {
        FragmentStateAdapter adapter = new FragmentStateAdapter(getChildFragmentManager(), getLifecycle()) {
            @Override
            public int getItemCount() {
                return 2; // Nr of tabs
            }

            @NonNull
            @Override
            public Fragment createFragment(int position) {
                Fragment result = null;
                // Return the appropriate fragment for each tab
                if (position == 0) {
                    result = new FragmentUserLibrary_Sets();
                } /*else if (position == 1) {
                    result = new FragmentUserLibrary_Folders();
                }*/ else if (position == 1) {
                    result = new FragmentUserLibrary_Groups();
                }
                return Objects.requireNonNull(result);
            }
        };

        viewPager.setAdapter(adapter);

        // Wait for the adapter to set up the tabs before setting tab titles
        viewPager.post(() -> {
            Objects.requireNonNull(tabLayout.getTabAt(0)).setText("Sets");
            //Objects.requireNonNull(tabLayout.getTabAt(1)).setText("Folders");
            Objects.requireNonNull(tabLayout.getTabAt(1)).setText("Groups");

            // Customize the text size for each tab
            for (int i = 0; i < tabLayout.getTabCount(); i++) {
                TextView tabTv = (TextView) LayoutInflater.from(requireActivity())
                        .inflate(R.layout.tab_text, tabLayout, false); // Provide tabLayout as the root view
                tabTv.setText(Objects.requireNonNull(tabLayout.getTabAt(i)).getText());
                tabTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15); // Adjust the size as needed
                Objects.requireNonNull(tabLayout.getTabAt(i)).setCustomView(tabTv);

                ViewGroup.LayoutParams layoutParams = tabTv.getLayoutParams();
                layoutParams.width = 200;
                tabTv.setLayoutParams(layoutParams);
            }
        });

        // Connect the TabLayout with the ViewPager
        TabLayoutMediator mediator = new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    // Tab titles are set beforehand
                }
        );

        // Attach the mediator
        mediator.attach();

        // Automatically select the tab based on the provided index
        tabLayout.selectTab(tabLayout.getTabAt(selectedTabIndex));
    }
}
