package com.example.mallet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mallet.backend.client.user.boundary.UserServiceImpl;
import com.example.mallet.databinding.FragmentUserLibrarySetsBinding;
import com.example.mallet.utils.ModelFlashcard;
import com.example.mallet.utils.ModelLearningSet;

import java.util.ArrayList;
import java.util.List;

public class FragmentUserLibrary_Sets extends Fragment {

    private UserServiceImpl userService;

    public static FragmentUserLibrary_Sets newInstance(String param1, String param2) {

        return new FragmentUserLibrary_Sets();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.userService = new UserServiceImpl();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        com.example.mallet.databinding.FragmentUserLibrarySetsBinding binding = FragmentUserLibrarySetsBinding.inflate(inflater, container, false);

        LinearLayout userLibrarySetsLl = binding.userLibrarySetsAllSetsLl;
        List<ModelLearningSet> userLibraryFoldersList = getUserLibrarySetList();

        for (ModelLearningSet set : userLibraryFoldersList) {
            View setItemView = inflater.inflate(R.layout.model_learning_set, userLibrarySetsLl, false);

            TextView setNameTv = setItemView.findViewById(R.id.learningSet_nameTv);
            setNameTv.setText(set.getName());

            TextView setNrOfTermsTv = setItemView.findViewById(R.id.learningSet_nrOfTermsTv);
            setNrOfTermsTv.setText(String.valueOf(set.getNrOfTerms()));

            TextView setCreatorTv = setItemView.findViewById(R.id.learningSet_creatorTv);
            setCreatorTv.setText(set.getCreator());


            userLibrarySetsLl.addView(setItemView);
        }

        return binding.getRoot();
    }

    //todo
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
}