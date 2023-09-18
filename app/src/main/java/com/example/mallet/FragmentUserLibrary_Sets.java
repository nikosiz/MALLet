package com.example.mallet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.mallet.databinding.FragmentProfileBinding;
import com.example.mallet.databinding.FragmentUserLibrarySetsBinding;
import com.example.mallet.utils.ModelFlashcard;
import com.example.mallet.utils.ModelFolder;
import com.example.mallet.utils.ModelLearningSet;

import java.util.ArrayList;
import java.util.List;

public class FragmentUserLibrary_Sets extends Fragment {

    private FragmentUserLibrarySetsBinding binding;

    public static FragmentUserLibrary_Sets newInstance(String param1, String param2) {

        return new FragmentUserLibrary_Sets();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserLibrarySetsBinding.inflate(inflater, container, false);

        LinearLayout userLibrarySetsLl= binding.userLibrarySetsAllSetsLl;
        List<ModelLearningSet> userLibraryFoldersList = getUserLibrarySetList();

        for (ModelLearningSet set : userLibraryFoldersList) {
            View setItemView = inflater.inflate(R.layout.model_learning_set, userLibrarySetsLl, false);

            TextView setNameTv = setItemView.findViewById(R.id.learningSet_nameTv);
            setNameTv.setText(set.getName());

            TextView setNumberOfTermsTv = setItemView.findViewById(R.id.learningSet_numberOfTermsTv);
            setNumberOfTermsTv.setText(String.valueOf(set.getNumberOfTerms()));

            TextView setCreatorTv = setItemView.findViewById(R.id.learningSet_creatorTv);
            setCreatorTv.setText(set.getCreator());


            userLibrarySetsLl.addView(setItemView);
        }

        return binding.getRoot();
    }

    private List<ModelLearningSet> getUserLibrarySetList() {
        List<ModelLearningSet> setList = new ArrayList<>();
        List<ModelFlashcard> flashcardList = new ArrayList<>();
        setList.add(new ModelLearningSet("Fruits", "user123", null,flashcardList,1));
        setList.add(new ModelLearningSet("Animals", "user123", null,flashcardList,2));
        setList.add(new ModelLearningSet("Numbers", "user123", null,flashcardList,3));
        setList.add(new ModelLearningSet("Countries", "user123", null,flashcardList,4));
        setList.add(new ModelLearningSet("Colors", "user123", null,flashcardList,5));
        return setList;
    }
}