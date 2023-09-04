package com.example.mallet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.mallet.databinding.FragmentUserLibraryFoldersBinding;

import java.util.ArrayList;
import java.util.List;

public class FragmentUserLibraryFolders extends Fragment implements AdapterFolder.OnFolderClickListener {
    private FragmentUserLibraryFoldersBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUserLibraryFoldersBinding.inflate(inflater, container, false);

        LinearLayout userLibraryFoldersLl = binding.userLibraryFoldersLl; // Change to LinearLayout
        List<ModelFolder> userLibraryFoldersList = getUserLibraryFoldersList();

        for (ModelFolder folder : userLibraryFoldersList) {
            View folderItemView = inflater.inflate(R.layout.model_folder, userLibraryFoldersLl, false);

            TextView folderNameTV = folderItemView.findViewById(R.id.folder_model_name_tv);
            folderNameTV.setText(folder.getFolderName());

            // Add folderItemView to the linearLayout
            userLibraryFoldersLl.addView(folderItemView);
        }

        return binding.getRoot();
    }

    private List<ModelFolder> getUserLibraryFoldersList() {
        List<ModelFolder> folderList = new ArrayList<>();
        folderList.add(new ModelFolder("Folder #1", "user123", "3"));
        folderList.add(new ModelFolder("Folder #2", "user123", "7"));
        folderList.add(new ModelFolder("Folder #3", "user123", "2"));
        folderList.add(new ModelFolder("Folder #4", "user123", "8"));
        folderList.add(new ModelFolder("Folder #5", "user123", "1"));
        return folderList;
    }

    public void onFolderClick(ModelFolder folder) {
        FrontendUtils.showToast(getContext(), "ASDF");
    }
}
