package com.example.mallet;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mallet.databinding.ActivityViewProfileBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Objects;

public class ActivityViewProfile extends AppCompatActivity {
    private ActivityViewProfileBinding binding;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        // Initialize the viewPager and tabLayout
        viewPager = findViewById(R.id.viewProfile_viewPager);
        tabLayout = findViewById(R.id.viewProfile_tabLayout);

        setupTabLayout(;
    }

    private void setupTabLayout() {
        FragmentStateAdapter adapter = new FragmentStateAdapter(getSupportFragmentManager(), getLifecycle()) {
            @Override
            public int getItemCount() {
                return 2; // Number of tabs
            }

            @NonNull
            @Override
            public Fragment createFragment(int position) {
                Fragment result = null;
                // Return the appropriate fragment for each tab
                if (position == 0) {
                    result = new FragmentViewProfile_Sets();
                } else {
                    result = new FragmentViewProfile_Groups();
                }
                return Objects.requireNonNull(result);
            }
        };

        viewPager.setAdapter(adapter);

        // Wait for the adapter to set up the tabs before setting tab titles
        viewPager.post(() -> {
            Objects.requireNonNull(tabLayout.getTabAt(0)).setText("Sets");
            Objects.requireNonNull(tabLayout.getTabAt(1)).setText("Groups");

            for (int i = 0; i < tabLayout.getTabCount(); i++) {
                TextView tabTv = (TextView) LayoutInflater.from(this)
                        .inflate(R.layout.tab_text, tabLayout, false);
                tabTv.setText(Objects.requireNonNull(tabLayout.getTabAt(i)).getText());
                tabTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                Objects.requireNonNull(tabLayout.getTabAt(i)).setCustomView(tabTv);

                ViewGroup.LayoutParams layoutParams = tabTv.getLayoutParams();
                layoutParams.width = 200;
                tabTv.setLayoutParams(layoutParams);
            }
        });


        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    // Nothing needed here since tab titles are set beforehand
                }
        ).attach();
    }
}