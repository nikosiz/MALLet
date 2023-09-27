package com.example.mallet;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.mallet.databinding.DialogLearningFinishedBinding;
import com.example.mallet.databinding.FragmentLearnBinding;
import com.example.mallet.databinding.ModelWrittenBinding;
import com.example.mallet.utils.AdapterLearn;
import com.example.mallet.utils.ModelMultipleChoice;
import com.example.mallet.utils.ModelWritten;
import com.example.mallet.utils.Utils;
import com.google.android.material.materialswitch.MaterialSwitch;

import java.util.Objects;

public class FragmentLearn extends Fragment {
    FragmentLearnBinding binding;
    private ViewPager2 questionVp2;
    private AdapterLearn pagerAdapter;
    private MaterialSwitch multipleChoiceMs, writtenMs;
    private int checkedSwitches = 0, clickCount = 0;
    private static final String PREFS_NAME = "LearnSettings";
    private static final String KEY_MULTIPLE_CHOICE = "multipleChoice";
    private static final String KEY_WRITTEN = "written";
    private static final String KEY_FORMAT = "questionFormat";
    private int questionFormat;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLearnBinding.inflate(inflater, container, false);

        learnOptionsDialog();

        setupContents();
        questionVp2 = binding.learnQuestionVp2;
        TextView prevQuestionTv = binding.learnPrevTv;
        TextView nextQuestionTv = binding.learnNextTv;
        TextView restartTv = binding.learnRestartTv;

        pagerAdapter = new AdapterLearn(multipleChoiceMs.isChecked(), writtenMs.isChecked());
        questionVp2.setAdapter(pagerAdapter);
        questionVp2.setUserInputEnabled(false);

        prevQuestionTv.setOnClickListener(v -> {
            int currentItem = questionVp2.getCurrentItem();
            if (currentItem < pagerAdapter.getItemCount() + 1) {
                questionVp2.setCurrentItem(currentItem - 1, true);
            }
        });

        nextQuestionTv.setOnClickListener(v -> {
            int currentItem = questionVp2.getCurrentItem();
            if (currentItem < pagerAdapter.getItemCount() - 1) {
                questionVp2.setCurrentItem(currentItem + 1, true);
            }
        });

        questionVp2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // TODO: display dialog after 10th question is answered
                if (position == 9) {
                    learningFinishedDialog();
                    Utils.hideItem(nextQuestionTv);
                    Utils.showItem(restartTv);
                } else {
                    Utils.showItem(nextQuestionTv);
                    Utils.hideItem(restartTv);
                }
            }
        });

        return binding.getRoot();
    }

    private void learningFinishedDialog() {
        final Dialog dialog = Utils.createDialog(getContext(), R.layout.dialog_learning_finished);
        DialogLearningFinishedBinding dialogBinding = DialogLearningFinishedBinding.inflate(getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);


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

    private void learnOptionsDialog() {
        final Dialog dialog = Utils.createDialog(getContext(), R.layout.dialog_learn_options);
        DialogLearnOptionsBinding dialogBinding = DialogLearnOptionsBinding.inflate(getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);

        multipleChoiceMs = dialogBinding.learnOptionsMultipleChoiceMs;
        multipleChoiceMs.setChecked(getSwitchState(KEY_MULTIPLE_CHOICE));

        multipleChoiceMs.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (checkedSwitches < 2) {
                    checkedSwitches++;
                }
            } else {
                if (checkedSwitches > 0) {
                    checkedSwitches--;
                }
            }

            saveSwitchState(KEY_MULTIPLE_CHOICE, isChecked);
            pagerAdapter.setMultipleChoiceEnabled(isChecked);
        });

        writtenMs = dialogBinding.learnOptionsWrittenMs;
        writtenMs.setChecked(getSwitchState(KEY_WRITTEN));

        writtenMs.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (checkedSwitches < 2) {
                    checkedSwitches++;
                }
            } else {
                if (checkedSwitches > 0) {
                    checkedSwitches--;
                }
            }

            saveSwitchState(KEY_WRITTEN, isChecked);
            pagerAdapter.setWrittenEnabled(isChecked);
        });

        LinearLayout questionFormatLl = dialogBinding.learnOptionsFormatLl;
        TextView questionFormatTv = dialogBinding.learnOptionsFormatTv;
        questionFormat = getFormat(KEY_FORMAT);

        if (questionFormat == 0) {
            questionFormatTv.setText("Term");
        } else if (questionFormat == 1) {
            questionFormatTv.setText("Definition");
        } else if (questionFormat == 2) {
            questionFormatTv.setText("Both");
        }

        questionFormatLl.setOnClickListener(v -> {
            clickCount++;
            if (clickCount % 3 == 0) {
                questionFormatTv.setText("Term");
                saveQuestionFormat(KEY_FORMAT, 0);
                // TODO
            } else if (clickCount % 3 == 1) {
                questionFormatTv.setText("Definition");
                saveQuestionFormat(KEY_FORMAT, 1);
                // TODO
            } else {
                questionFormatTv.setText("Both");
                saveQuestionFormat(KEY_FORMAT, 2);
                clickCount = 0;
                // TODO
            }
        });

        TextView startLearningTv = dialogBinding.learnOptionsStartTv;
        TextView errorTv = dialogBinding.learnOptionsErrorTv;

        startLearningTv.setOnClickListener(v -> {
            checkedSwitches = countCheckedSwitches();
            if (checkedSwitches > 0) {
                dialog.dismiss();

                // Reset the ViewPager to position 0
                questionVp2.setCurrentItem(0, false); // Use 'false' to disable smooth scrolling

                applyLearnSettings();
            } else {
                errorTv.setVisibility(View.VISIBLE);
            }
        });
    }

    private void applyLearnSettings() {
        // TODO
        System.out.println("Checked Switches:");

        if (multipleChoiceMs.isChecked()) {
            System.out.println("Multiple Choice switch is checked");
        }

        if (writtenMs.isChecked()) {
            System.out.println("Written switch is checked");
        }

        Utils.showToast(getContext(), Integer.toString(checkedSwitches));
    }

    private int countCheckedSwitches() {
        int count = 0;
        if (multipleChoiceMs.isChecked()) count++;
        if (writtenMs.isChecked()) count++;
        return count;
    }

    private void saveSwitchState(String switchKey, boolean isChecked) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(switchKey, isChecked);
        editor.apply();
    }

    private boolean getSwitchState(String switchKey) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(switchKey, false); // Default value (false) if key not found
    }

    private void saveQuestionFormat(String formatKey, int format) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(formatKey, format);
        editor.apply();
    }

    private int getFormat(String formatKey) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(formatKey, 0);
    }

    private void displayMultipleChoice(ModelMultipleChoice question, LinearLayout linearLayout, LayoutInflater inflater) {
        linearLayout.removeAllViews();
        View questionView = inflater.inflate(R.layout.model_multiple_choice, linearLayout, false);

        CardView mainCv = questionView.findViewById(R.id.multipleChoice_mainCv);

        TextView questionTv = questionView.findViewById(R.id.multipleChoice_questionTv);

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
