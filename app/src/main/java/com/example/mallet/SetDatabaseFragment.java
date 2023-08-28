package com.example.mallet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class SetDatabaseFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_database, container, false);

        ViewPager2 viewPager = view.findViewById(R.id.your_library_view_pager);
        TabLayout tabLayout = view.findViewById(R.id.your_library_tab_layout);

        // Create a FragmentStateAdapter
        FragmentStateAdapter adapter = new FragmentStateAdapter(getChildFragmentManager(), getLifecycle()) {
            @Override
            public int getItemCount() {
                return 3; // Number of tabs
            }

            @Override
            public Fragment createFragment(int position) {
                // Return the appropriate fragment for each tab
                switch (position) {
                    case 0:
                        return new LibrarySetsFragment();
                    case 1:
                        return new LibraryFoldersFragment();
                    case 2:
                        return new LibraryGroupsFragment();
                    default:
                        return null;
                }
            }
        };

        viewPager.setAdapter(adapter);

        // Connect the TabLayout with the ViewPager
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText("Tab " + (position + 1))
        ).attach();

        return view;
    }
}
