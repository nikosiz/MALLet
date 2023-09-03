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
        View view = inflater.inflate(R.layout.fragment_user_library_folders, container, false);

        LinearLayout yourLibraryFoldersLl = view.findViewById(R.id.your_library_folders_ll); // Change to LinearLayout
        List<ModelFolder> yourLibraryFoldersList = getYourLibraryFoldersList();

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
        folderList.add(new ModelFolder("Folder #1", "user123", "3"));
        folderList.add(new ModelFolder("Folder #2", "user123", "7"));
        folderList.add(new ModelFolder("Folder #3", "user123", "2"));
        folderList.add(new ModelFolder("Folder #4", "user123", "8"));
        folderList.add(new ModelFolder("Folder #5", "user123", "1"));
        return folderList;
    }

    public void onFolderClick(ModelFolder folder) {
        FrontendUtils.showToast(getContext(),"ASDF");
    }
}
