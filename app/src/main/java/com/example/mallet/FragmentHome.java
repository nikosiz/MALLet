package com.example.mallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mallet.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class FragmentHome extends Fragment {

    private FragmentHomeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentHomeBinding binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Your code to populate the LinearLayout
        List<ModelFolder> homeFoldersList = getHomeFoldersList();
        for (ModelFolder folder : homeFoldersList) {
            View folderItemView = inflater.inflate(R.layout.model_folder, binding.homeFoldersLl, false);

            TextView folderNameTextView = folderItemView.findViewById(R.id.folder_model_name_tv);
            folderNameTextView.setText(folder.getFolderName());

            // Add folderItemView to the linearLayout
            binding.homeFoldersLl.addView(folderItemView);
        }

        setupClickListeners(binding);

        return view;
    }


    private void setupClickListeners(FragmentHomeBinding binding) {
        binding.homeButton.setOnClickListener(v -> startLearnActivity());
    }

    // TODO: The same for sets and groups

    private List<ModelFolder> getHomeFoldersList() {
        List<ModelFolder> folderList = new ArrayList<>();
        folderList.add(new ModelFolder("ictStudent997", "Folder #1"));
        folderList.add(new ModelFolder("ictStudent997", "Folder #2"));
        folderList.add(new ModelFolder("ictStudent997", "Folder #3"));
        folderList.add(new ModelFolder("ictStudent997", "Folder #4"));
        folderList.add(new ModelFolder("ictStudent997", "Folder #5"));
        folderList.add(new ModelFolder("ictStudent997", "Folder #6"));
        folderList.add(new ModelFolder("ictStudent997", "Folder #7"));
        return folderList;
    }

    private void startLearnActivity() {
        Intent intent = new Intent(getContext(), ActivityLearn.class);
        startActivity(intent);
    }
}
