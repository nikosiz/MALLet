package com.example.mallet;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mallet.databinding.FragmentYourLibraryBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class YourLibraryFragment extends Fragment {

    private FragmentYourLibraryBinding binding;
    private TabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_your_library, container, false);



        ViewPager2 viewPager = view.findViewById(R.id.your_library_view_pager);
        TabLayout tabLayout = view.findViewById(R.id.your_library_tab_layout);


        // Create a FragmentStateAdapter
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
                    result = new LibrarySetsFragment();
                } else if (position == 1) {
                    result = new LibraryFoldersFragment();
                } else if (position == 2) {
                    result = new LibraryGroupsFragment();
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
                TextView tabTextView = (TextView) LayoutInflater.from(requireContext())
                        .inflate(R.layout.tab_text, null); // Use the layout used internally by TabLayout
                tabTextView.setText(Objects.requireNonNull(tabLayout.getTabAt(i)).getText());
                tabTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15); // Adjust the size as needed
                Objects.requireNonNull(tabLayout.getTabAt(i)).setCustomView(tabTextView);

                ViewGroup.LayoutParams layoutParams = tabTextView.getLayoutParams();
                layoutParams.width = 200;
                tabTextView.setLayoutParams(layoutParams);
            }
        });

        // Connect the TabLayout with the ViewPager
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    // Nothing needed here since tab titles are set beforehand
                }
        ).attach();

        return view;

    }
}