package com.example.mallet;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentViewProfile_Sets extends Fragment {

    public FragmentViewProfile_Sets() {
    }

    public static FragmentViewProfile_Sets newInstance(String param1, String param2) {
        FragmentViewProfile_Sets fragment = new FragmentViewProfile_Sets();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_view_profile__sets, container, false);
    }
}