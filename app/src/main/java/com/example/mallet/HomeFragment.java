package com.example.mallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.mallet.databinding.FragmentProfileBinding;

public class HomeFragment extends Fragment {

    private FragmentProfileBinding binding;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        setupHomeBtn(rootView);
        return rootView;
    }

    private void setupHomeBtn(View rootView) {
        Button startLearn = rootView.findViewById(R.id.home_button);

        startLearn.setOnClickListener(v -> {
            startLearnActivity();
        });
    }

    private void startLearnActivity() {
        Intent intent = new Intent(getContext(), LearnActivity.class);
        startActivity(intent);
    }
}
