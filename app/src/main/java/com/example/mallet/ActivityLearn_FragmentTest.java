package com.example.mallet;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.mallet.databinding.DialogTestOptionsBinding;
import com.example.mallet.databinding.ActivityLearnFragmentTestBinding;
import com.example.mallet.utils.Utils;

import java.util.Objects;

public class ActivityLearn_FragmentTest extends Fragment {
    ActivityLearnFragmentTestBinding binding;


    public ActivityLearn_FragmentTest() {
        // Required empty public constructor
    }

    public static ActivityLearn_FragmentTest newInstance(String param1, String param2) {

        return new ActivityLearn_FragmentTest();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = ActivityLearnFragmentTestBinding.inflate(inflater, container, false);
        setupContents();
        return binding.getRoot();
    }

    private void setupContents() {
        setupToolbar();
        binding.testToolbarOptionsIv.setOnClickListener(v -> testOptionsDialog());
    }

    private void setupToolbar() {
        Toolbar toolbar = binding.testToolbar;
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(""); // Set the title to an empty string
    }

    private void testOptionsDialog() {
        final Dialog dialog = Utils.createDialog(getContext(), R.layout.dialog_test_options);
        DialogTestOptionsBinding dialogBinding = DialogTestOptionsBinding.inflate(LayoutInflater.from(getContext()));
        Objects.requireNonNull(Objects.requireNonNull(dialog).getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Objects.requireNonNull(dialog).setContentView(dialogBinding.getRoot());
        dialog.show();
    }

    private Dialog createDialog(int layoutResId) {
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layoutResId);
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        return dialog;
    }
}