package com.example.mallet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.mallet.databinding.FragmentUserLibraryGroupsBinding;

public class FragmentUserLibraryGroups extends Fragment {
    private FragmentUserLibraryGroupsBinding binding;

    public static FragmentUserLibraryGroups newInstance(String param1, String param2) {
        FragmentUserLibraryGroups fragment = new FragmentUserLibraryGroups();
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
        return inflater.inflate(R.layout.fragment_user_library_groups, container, false);
    }
}