package com.example.mallet;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mallet.databinding.DialogLearnOptionsBinding;
import com.example.mallet.databinding.FragmentLearnBinding;
import com.example.mallet.databinding.ModelWrittenBinding;
import com.example.mallet.utils.LearnPagerAdapter;
import com.example.mallet.utils.ModelMultipleChoice;
import com.example.mallet.utils.ModelWritten;
import com.example.mallet.utils.Utils;

import java.util.Objects;

public class FragmentLearn extends Fragment {
    FragmentLearnBinding binding;
    private ViewPager2 questionVp2;
    private TextView nextQuestionTv;
    private LearnPagerAdapter pagerAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLearnBinding.inflate(inflater, container, false);

        learnOptionsDialog();

        setupContents();
        questionVp2 = binding.learnQuestionVp2;
        nextQuestionTv = binding.learnNextTv;

        pagerAdapter = new LearnPagerAdapter();
        questionVp2.setAdapter(pagerAdapter);
        questionVp2.setUserInputEnabled(false);
        nextQuestionTv.setOnClickListener(v -> {
            int currentItem = questionVp2.getCurrentItem();
            if (currentItem < pagerAdapter.getItemCount() - 1) {
                questionVp2.setCurrentItem(currentItem + 1, true);
            }
        });

        return binding.getRoot();
    }

    private void setupContents() {
        setupToolbar();
    }

    private void setupToolbar() {
        Toolbar toolbar = binding.learnToolbar;
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(""); // Set the title to an empty string

        binding.learnToolbarBackIv.setOnClickListener(v -> getActivity().finish());
        binding.learnOptionsIv.setOnClickListener(v -> learnOptionsDialog());
    }

    private boolean isFirstTimeLearnOptionsDialog = true;
    private boolean isVisible = false;

    private void learnOptionsDialog() {
        final Dialog dialog = Utils.createDialog(getContext(), R.layout.dialog_learn_options);
        DialogLearnOptionsBinding dialogBinding = DialogLearnOptionsBinding.inflate(getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());
        dialog.show();

        LinearLayout mainLl = dialogBinding.learnOptionsMainLl;

        // Check if it's the first time the dialog is opened
        if (isFirstTimeLearnOptionsDialog) {
            Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            dialogBinding.learnOptionsBackIv.setOnClickListener(v -> requireActivity().finish());
            dialogBinding.learnOptionsStartTv.setOnClickListener(v -> {
                updateLearnSettings();
                dialog.dismiss();
            });

            isFirstTimeLearnOptionsDialog = false;

        } else {
            Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mainLl.setBackgroundResource(R.drawable.bg_corners_rounded);

            Utils.hideItem(dialogBinding.learnOptionsBackIv);
            dialogBinding.learnOptionsStartTv.setOnClickListener(v -> {
                updateLearnSettings();
                dialog.dismiss();
            });

        }
    }

    private void updateLearnSettings() {
        // TODO
    }

    private void displayMultipleChoice(ModelMultipleChoice question, LinearLayout linearLayout, LayoutInflater inflater) {
        linearLayout.removeAllViews();
        View questionView = inflater.inflate(R.layout.model_multiple_choice, linearLayout, false);

        CardView mainCv = questionView.findViewById(R.id.multipleChoice_mainCv);

        TextView questionTv = questionView.findViewById(R.id.multipleChoice_questionTv);

        // Now you can work with questionTv and answerEt as needed

        linearLayout.addView(questionView);
    }

    private void displayWritten(ModelWritten question, LinearLayout linearLayout, LayoutInflater inflater) {
        linearLayout.removeAllViews();
        View questionView = inflater.inflate(R.layout.model_written, linearLayout, false);
        ModelWrittenBinding questionBinding = ModelWrittenBinding.inflate(LayoutInflater.from(getContext()));

        CardView mainCv = questionView.findViewById(R.id.written_mainCv);

        TextView questionTv = questionBinding.writtenQuestionTv;
        EditText answerEt = questionBinding.writtenAnswerEt;

        String answer = answerEt.getText().toString().trim();
        System.out.println(answer);

        linearLayout.addView(questionView);
    }
}
