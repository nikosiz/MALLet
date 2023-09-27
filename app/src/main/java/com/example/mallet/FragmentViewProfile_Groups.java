package com.example.mallet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class FragmentViewProfile_Groups extends Fragment {

    public FragmentViewProfile_Groups() {
        // Required empty public constructor
    }

    public static FragmentViewProfile_Groups newInstance(String param1, String param2) {
        FragmentViewProfile_Groups fragment = new FragmentViewProfile_Groups();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_profile__groups, container, false);
    }
}