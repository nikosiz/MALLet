package com.example.mallet;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mallet.databinding.DialogLearnOptionsBinding;
import com.example.mallet.databinding.DialogLearningFinishedBinding;
import com.example.mallet.databinding.FragmentTestowyBinding;
import com.example.mallet.utils.AdapterLearn;
import com.example.mallet.utils.ModelFlashcard;
import com.example.mallet.utils.ModelWritten;
import com.example.mallet.utils.Utils;
import com.google.android.material.materialswitch.MaterialSwitch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class TestowyFragment extends Fragment {
    private FragmentTestowyBinding binding;
    private ViewPager2 questionVp2;
    private AdapterLearn pagerAdapter;
    private MaterialSwitch multipleChoiceMs, writtenMs;
    private int checkedSwitches = 0, clickCount = 0;
    private static final String PREFS_NAME = "LearnSettings";
    private static final String KEY_MULTIPLE_CHOICE = "multipleChoice";
    private static final String KEY_WRITTEN = "written";
    private static final String KEY_FORMAT = "questionFormat"; // SharedPreferences key for the FORMAT
    private int questionFormat;
    private int formatCounter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTestowyBinding.inflate(inflater, container, false);

        learnOptionsDialog();

        //handleWrittenQuestion();

        setupContents();
        questionVp2 = binding.learnQuestionVp2;
        TextView prevQuestionTv = binding.learnPrevTv;
        TextView nextQuestionTv = binding.learnNextTv;
        TextView restartTv = binding.learnRestartTv;

        List<ModelWritten> questionList = handleWrittenQuestion();
        pagerAdapter = new AdapterLearn(multipleChoiceMs.isChecked(), writtenMs.isChecked(), questionList);
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

        if (formatCounter == 0) {
            questionFormatTv.setText("Term");
        } else if (formatCounter == 1) {
            questionFormatTv.setText("Definition");
        } else if (formatCounter == 2) {
            questionFormatTv.setText("Both");
        }

        questionFormatLl.setOnClickListener(v -> {
            formatCounter++;
            if (formatCounter % 3 == 0) {
                questionFormatTv.setText("Term");
                saveFormat(KEY_FORMAT, 0); // Save the format to SharedPreferences
                // TODO
            } else if (formatCounter % 3 == 1) {
                questionFormatTv.setText("Definition");
                saveFormat(KEY_FORMAT, 1); // Save the format to SharedPreferences
                // TODO
            } else if (formatCounter % 3 == 2) {
                questionFormatTv.setText("Both");
                saveFormat(KEY_FORMAT, 2); // Save the format to SharedPreferences
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

    private void saveFormat(String formatKey, int format) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(formatKey, format);
        editor.apply();
    }

    private int getFormat(String formatKey) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(formatKey, 0); // Default value (0) if the key is not found
    }

    private List<ModelWritten> handleWrittenQuestion() {
        int format = getFormat(KEY_FORMAT);

        List<ModelFlashcard> flashcardList = new ArrayList<>();
        flashcardList.add(new ModelFlashcard("Dog", "A domesticated mammal", "Pies"));
        flashcardList.add(new ModelFlashcard("Cat", "A small domesticated carnivorous mammal", "Kot"));
        flashcardList.add(new ModelFlashcard("Elephant", "A large, herbivorous mammal with a trunk", "Słoń"));
        flashcardList.add(new ModelFlashcard("Lion", "A large wild cat known for its mane", "Lew"));
        flashcardList.add(new ModelFlashcard("Giraffe", "A tall, long-necked African mammal", "Żyrafa"));

        List<ModelWritten> questionList = new ArrayList();
        Random random = new Random();

        int questionPosition = random.nextInt(2);

        for (ModelFlashcard flashcard : flashcardList) {
            // Create a list of options with the correct answer at position 0
            List<String> options = new ArrayList<>();
            options.add(flashcard.getTerm());
            options.add(flashcard.getDefinition());
            options.add(flashcard.getTranslation());

            // Randomly shuffle the options to change the order
            Collections.shuffle(options);

            // Set the correct answer positions based on the chosen question position
            int correctAnswerPosition = 1, alternativeAnswerPosition = 2;

            switch (questionPosition) {
                case 0:
                    correctAnswerPosition = 1;
                    alternativeAnswerPosition = 2;
                    break;
                case 1:
                    correctAnswerPosition = 0;
                    alternativeAnswerPosition = 2;
                    break;
                case 2:
                    correctAnswerPosition = 1;
                    alternativeAnswerPosition = 0;
                    break;
            }

            // Create a ModelTrueFalse object with the question and the correct answer positions
            ModelWritten written = new ModelWritten(options.get(questionPosition), options.get(correctAnswerPosition), options.get(alternativeAnswerPosition));

            questionList.add(written);

            System.out.println(written);
        }

        return questionList;
    }
}
