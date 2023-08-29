package com.example.mallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mallet.databinding.FragmentHomeBinding;
import com.example.mallet.databinding.FragmentProfileBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    private void setupClickListeners() {

    }

    private void setupHomeBtn(View rootView) {
        Button startLearn = rootView.findViewById(R.id.home_button);

        startLearn.setOnClickListener(v -> startLearnActivity());
    }

    private void startLearnActivity() {
        Intent intent = new Intent(getContext(), LearnActivity.class);
        startActivity(intent);
    }
}
