package com.example.mallet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.mallet.databinding.FragmentYourLibraryFoldersBinding;

import java.util.ArrayList;
import java.util.List;

public class FragmentYourLibraryFolders extends Fragment implements AdapterFolder.OnFolderClickListener {
    private FragmentYourLibraryFoldersBinding binding;
    private LinearLayout yourLibraryFoldersLl;
    private List<ModelFolder> yourLibraryFoldersList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_your_library_folders, container, false);

        yourLibraryFoldersLl = view.findViewById(R.id.your_library_folders_ll); // Change to LinearLayout
        yourLibraryFoldersList = getYourLibraryFoldersList();

        for (ModelFolder folder : yourLibraryFoldersList) {
            View folderItemView = inflater.inflate(R.layout.model_folder, yourLibraryFoldersLl, false);

            TextView folderNameTextView = folderItemView.findViewById(R.id.folder_model_name_tv);
            folderNameTextView.setText(folder.getFolderName());

            // Add folderItemView to the linearLayout
            yourLibraryFoldersLl.addView(folderItemView);
        }

        return view;
    }

    private List<ModelFolder> getYourLibraryFoldersList() {
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

    public void onFolderClick(ModelFolder folder) {
        showToast("ASDF");
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
