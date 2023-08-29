package com.example.mallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mallet.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class FragmentHome extends Fragment {

    private FragmentHomeBinding binding;
    private LinearLayout homeFoldersLl;
    private List<ModelFolder> homeFoldersList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        homeFoldersLl = view.findViewById(R.id.home_folders_ll); // Change to LinearLayout
        homeFoldersList = getHomeFoldersList();

        for (ModelFolder folder : homeFoldersList) {
            View folderItemView = inflater.inflate(R.layout.model_folder, homeFoldersLl, false);

            TextView folderNameTextView = folderItemView.findViewById(R.id.folder_model_name_tv);
            folderNameTextView.setText(folder.getFolderName());

            // Add folderItemView to the linearLayout
            homeFoldersLl.addView(folderItemView);
        }

        return view;
    }

    private void setupClickListeners() {

    }

    private void setupHomeBtn(View rootView) {
        Button startLearn = rootView.findViewById(R.id.home_button);

        startLearn.setOnClickListener(v -> startLearnActivity());
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
