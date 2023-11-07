package com.example.mallet;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mallet.databinding.FragmentSetDatabaseBinding;

public class FragmentSetsDatabase extends Fragment {
    private ActivityMain activityMain;
    private FragmentSetDatabaseBinding binding;
    private ProgressBar progressBar;
    private ScrollView databaseSv;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof ActivityMain) {
            activityMain = (ActivityMain) context;
        } else {
            // Handle the case where the activity does not implement the method
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_set_database, container, false);
    }

    private void setupContents() {
        progressBar = binding.databaseProgressBar;
        databaseSv=binding.databaseSv;
    }

}
