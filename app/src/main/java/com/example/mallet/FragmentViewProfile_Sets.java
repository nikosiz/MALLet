package com.example.mallet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class FragmentViewProfile_Sets extends Fragment {

    public FragmentViewProfile_Sets() {
    }

    public static FragmentViewProfile_Sets newInstance(String param1, String param2) {

        return new FragmentViewProfile_Sets();
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