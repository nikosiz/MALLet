package com.example.mallet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.mallet.databinding.FragmentSetDatabaseBinding;

public class FragmentSetsDatabase extends Fragment {

    private FragmentSetDatabaseBinding binding;

    public FragmentSetsDatabase() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_set_database, container, false);
    }

}
