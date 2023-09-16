package com.example.mallet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.mallet.databinding.ActivityMainFragmentUserLibrarySetsBinding;

public class ActivityMain_FragmentUserLibrary_Sets extends Fragment {

    private ActivityMainFragmentUserLibrarySetsBinding binding;

    public static ActivityMain_FragmentUserLibrary_Sets newInstance(String param1, String param2) {

        return new ActivityMain_FragmentUserLibrary_Sets();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_main_fragment_user_library_sets, container, false);
    }
}