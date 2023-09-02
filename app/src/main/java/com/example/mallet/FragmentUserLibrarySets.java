package com.example.mallet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.mallet.databinding.FragmentUserLibrarySetsBinding;

public class FragmentUserLibrarySets extends Fragment {

    private FragmentUserLibrarySetsBinding binding;

    public static FragmentUserLibrarySets newInstance(String param1, String param2) {
        FragmentUserLibrarySets fragment = new FragmentUserLibrarySets();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_library_sets, container, false);
    }
}