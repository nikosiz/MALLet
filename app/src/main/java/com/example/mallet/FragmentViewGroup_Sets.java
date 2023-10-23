package com.example.mallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mallet.databinding.FragmentViewGroupSetsBinding;
import com.example.mallet.utils.AdapterFolder;
import com.example.mallet.utils.ModelFlashcard;
import com.example.mallet.utils.ModelFolder;
import com.example.mallet.utils.ModelLearningSet;
import com.example.mallet.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class FragmentViewGroup_Sets extends Fragment implements AdapterFolder.OnFolderClickListener {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentViewGroupSetsBinding binding = FragmentViewGroupSetsBinding.inflate(inflater, container, false);

        LinearLayout userLibrarySetsLl = binding.viewGroupSetsSetListLl; // Change to LinearLayout
        List<ModelLearningSet> userLibrarySetList = getUserLibrarySetList();

        for (ModelLearningSet set : userLibrarySetList) {
            View memberItemView = inflater.inflate(R.layout.model_learning_set, userLibrarySetsLl, false);

            TextView usernameTv = memberItemView.findViewById(R.id.learningSet_nameTv);
            usernameTv.setText(set.getName());

            userLibrarySetsLl.addView(memberItemView);

            //memberItemView.setOnClickListener(v -> startViewFolderActivity());

        }

        return binding.getRoot();
    }

    private List<ModelLearningSet> getUserLibrarySetList() {
        List<ModelLearningSet> setList = new ArrayList<>();
        List<ModelFlashcard> flashcardList = new ArrayList<>();
        setList.add(new ModelLearningSet("Fruits", "user123", null, flashcardList, 1, ""));
        setList.add(new ModelLearningSet("Animals", "user123", null, flashcardList, 2, ""));
        setList.add(new ModelLearningSet("Nrs", "user123", null, flashcardList, 3, ""));
        setList.add(new ModelLearningSet("Countries", "user123", null, flashcardList, 4, ""));
        setList.add(new ModelLearningSet("Colors", "user123", null, flashcardList, 5, ""));
        return setList;
    }

    private void startViewFolderActivity() {
        Intent intent = new Intent(getContext(), ActivityViewFolder.class);
        startActivity(intent);
    }

    public void onFolderClick(ModelFolder folder) {
        Utils.showToast(getContext(), "ASDF");
    }
}
