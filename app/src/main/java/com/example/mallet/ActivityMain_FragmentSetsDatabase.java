package com.example.mallet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.mallet.databinding.ActivityMainFragmentSetDatabaseBinding;

public class ActivityMain_FragmentSetsDatabase extends Fragment {

    private ActivityMainFragmentSetDatabaseBinding binding;

    public ActivityMain_FragmentSetsDatabase() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_main_fragment_set_database, container, false);
    }

}
