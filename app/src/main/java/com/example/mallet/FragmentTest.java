package com.example.mallet;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mallet.databinding.DialogTestOptionsBinding;
import com.example.mallet.databinding.FragmentTestBinding;
import com.example.mallet.utils.AdapterTest;
import com.example.mallet.utils.Utils;
import com.google.android.material.materialswitch.MaterialSwitch;

import java.util.Objects;

public class FragmentTest extends Fragment {
    private FragmentTestBinding binding;
    private ViewPager2 questionVp2;
    private AdapterTest pagerAdapter;
    private final int minNrOfQuestions = 1;
    private int nrOfQuestions = minNrOfQuestions;
    private final int maxNrOfQuestions = 50;
    private MaterialSwitch trueFalseMs, multipleChoiceMs, matchMs, writtenMs;
    private int checkedSwitches = 0;
    private static final String PREFS_NAME = "TestSettings";
    private static final String KEY_NR_OF_QUESTIONS = "nrOfQuestions";
    private static final String KEY_TRUE_FALSE = "trueFalse";
    private static final String KEY_MULTIPLE_CHOICE = "multipleChoice";
    private static final String KEY_MATCH = "match";
    private static final String KEY_WRITTEN = "written";

    public FragmentTest() {
        // Required empty public constructor
    }

    public static FragmentTest newInstance(String param1, String param2) {
        return new FragmentTest();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTestBinding.inflate(inflater, container, false);

        testOptionsDialog();
        setupContents();

        questionVp2 = binding.testQuestionVp2;
        TextView prevQuestionTv = binding.testPrevTv;
        TextView nextQuestionTv = binding.testNextTv;
        TextView restartTv = binding.testRestartTv;

        pagerAdapter = new AdapterTest(
                nrOfQuestions,
                multipleChoiceMs.isChecked(),
                writtenMs.isChecked(),
                trueFalseMs.isChecked(),
                matchMs.isChecked()
        );

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

        questionVp2.setAdapter(pagerAdapter);
        questionVp2.setUserInputEnabled(false);

        return binding.getRoot();
    }

    private void setupContents() {
        setupToolbar();
    }

    private void setupToolbar() {
        Toolbar toolbar = binding.testToolbar;
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(""); // Set the title to an empty string

        binding.testToolbarBackIv.setOnClickListener(v -> getActivity().finish());
        binding.testOptionsIv.setOnClickListener(v -> testOptionsDialog());
    }

    private void testOptionsDialog() {
        final Dialog dialog = Utils.createDialog(getContext(), R.layout.dialog_test_options);
        DialogTestOptionsBinding dialogBinding = DialogTestOptionsBinding.inflate(LayoutInflater.from(requireContext()));
        dialog.setContentView(dialogBinding.getRoot());
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);

        TextView setNameTv = dialogBinding.testOptionsSetNameTv;

        TextView nrOfQuestionsTv = dialogBinding.testOptionsNumberOfQuestionsTitleTv;
        nrOfQuestionsTv.setText("Questions (max"/*"+learningSet.getNrOfTerms()+*/ + ")");
        EditText nrOfQuestionsEt = dialogBinding.testOptionsNumberOfQuestionsEt;

        nrOfQuestionsEt.setText(Integer.toString(nrOfQuestions));

        ImageView lessQuestionsIv = dialogBinding.testOptionsNumberOfQuestionsLessIv;
        lessQuestionsIv.setOnClickListener(v -> {
            if (nrOfQuestions > minNrOfQuestions) {
                nrOfQuestions--;
                nrOfQuestionsEt.setText(Integer.toString(nrOfQuestions));
            }
        });

        ImageView moreQuestionsIv = dialogBinding.testOptionsNumberOfQuestionsMoreIv;
        moreQuestionsIv.setOnClickListener(v -> {
            if (nrOfQuestions < maxNrOfQuestions) {
                nrOfQuestions++;
                nrOfQuestionsEt.setText(Integer.toString(nrOfQuestions));
            }
        });

        TextView answersLanguage = dialogBinding.testOptionsAnswersTv;

        trueFalseMs = dialogBinding.testOptionsTrueFalseMs;
        trueFalseMs.setChecked(getSwitchState(KEY_TRUE_FALSE));

        trueFalseMs.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (checkedSwitches < 4) {
                    checkedSwitches++; // Increment if it's less than 4
                }
            } else {
                if (checkedSwitches > 0) {
                    checkedSwitches--; // Decrement if it's greater than 1
                }
            }

            saveSwitchState(KEY_TRUE_FALSE, isChecked);
            pagerAdapter.setTrueFalseEnabled(isChecked);
        });

        multipleChoiceMs = dialogBinding.testOptionsMultipleChoiceMs;
        multipleChoiceMs.setChecked(getSwitchState(KEY_MULTIPLE_CHOICE));

        multipleChoiceMs.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (checkedSwitches < 4) {
                    checkedSwitches++; // Increment if it's less than 4
                }
            } else {
                if (checkedSwitches > 0) {
                    checkedSwitches--; // Decrement if it's greater than 1
                }
            }

            saveSwitchState(KEY_MULTIPLE_CHOICE, isChecked);
            pagerAdapter.setMultipleChoiceEnabled(isChecked);
        });

        matchMs = dialogBinding.testOptionsMatchMs;
        matchMs.setChecked(getSwitchState(KEY_MATCH));

        matchMs.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (checkedSwitches < 4) {
                    checkedSwitches++; // Increment if it's less than 4
                }
            } else {
                if (checkedSwitches > 0) {
                    checkedSwitches--; // Decrement if it's greater than 1
                }
            }

            saveSwitchState(KEY_MATCH, isChecked);
            pagerAdapter.setMatchEnabled(isChecked);
        });

        writtenMs = dialogBinding.testOptionsWrittenMs;
        writtenMs.setChecked(getSwitchState(KEY_WRITTEN));

        writtenMs.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (checkedSwitches < 4) {
                    checkedSwitches++; // Increment if it's less than 4
                }
            } else {
                if (checkedSwitches > 0) {
                    checkedSwitches--; // Decrement if it's greater than 1
                }
            }

            saveSwitchState(KEY_WRITTEN, isChecked);
            pagerAdapter.setWrittenEnabled(isChecked);
        });

        TextView startTestTv = dialogBinding.testOptionsStartTestTv;
        TextView errorTv = dialogBinding.testOptionsErrorTv;

        startTestTv = dialogBinding.testOptionsStartTestTv;
        errorTv = dialogBinding.testOptionsErrorTv;

        startTestTv.setOnClickListener(v -> {
            checkedSwitches = countCheckedSwitches();
            if (checkedSwitches > 0) {
                dialog.dismiss();

                // Update the number of questions in your pagerAdapter
                pagerAdapter.setNumberOfQuestions(nrOfQuestions);

                applyTestSettings();
            } else {
                //errorTv.setVisibility(View.VISIBLE);
            }
        });
    }

    private int countCheckedSwitches() {
        int count = 0;
        if (trueFalseMs.isChecked()) count++;
        if (multipleChoiceMs.isChecked()) count++;
        if (matchMs.isChecked()) count++;
        if (writtenMs.isChecked()) count++;
        return count;
    }


    private void applyTestSettings() {
        System.out.println("Checked Switches:");

        if (trueFalseMs.isChecked()) {
            System.out.println("True/False switch is checked");
        }

        if (multipleChoiceMs.isChecked()) {
            System.out.println("Multiple Choice switch is checked");
        }

        if (matchMs.isChecked()) {
            System.out.println("Match switch is checked");
        }

        if (writtenMs.isChecked()) {
            System.out.println("Written switch is checked");
        }

        Utils.showToast(getContext(), Integer.toString(checkedSwitches));
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
}
