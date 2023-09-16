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
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mallet.databinding.DialogLearnResultBinding;
import com.example.mallet.databinding.FragmentLearnBinding;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;


public class FragmentLearn extends Fragment {

    private Dialog resultDialog; // Declare the Dialog as a class member
    private TextView questionTv, questionNumberTV;
    private Button answerOneBtn, answerTwoBtn, answerThreeBtn, answerFourBtn;
    private ArrayList<ModelQuizABCD> modelLearnArrayList;
    private Random random;
    int currentScore = 0, questionAttempted = 1, currentPos;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        com.example.mallet.databinding.FragmentLearnBinding binding = FragmentLearnBinding.inflate(inflater, container, false);

        questionTv = binding.learnQuestionTv;

        answerOneBtn = binding.learnAnswerOneBtn;
        answerTwoBtn = binding.learnAnswerTwoBtn;
        answerThreeBtn = binding.learnAnswerThreeBtn;
        answerFourBtn = binding.learnAnswerFourBtn;

        modelLearnArrayList = new ArrayList<>();
        random = new Random();
        questionNumberTV = binding.learnQuestionCounterTv;

        setupContents();

        currentPos = random.nextInt(modelLearnArrayList.size());
        setDataToViews(currentPos);

        return binding.getRoot();
    }

    private void setupContents() {
        getQuizQuestion(modelLearnArrayList);
        setQuizButtonListeners();
    }

    // Separate method to set data to views
    private void setDataToViews(int currentPos) {
        questionNumberTV.setText(questionAttempted + "/10");
        if (questionAttempted > 10) {
            questionNumberTV.setText("10/10");
            resultDialog();
        } else {
            questionTv.setText(modelLearnArrayList.get(currentPos).getQuestion());
            answerOneBtn.setText(modelLearnArrayList.get(currentPos).getAnswerA());
            answerTwoBtn.setText(modelLearnArrayList.get(currentPos).getAnswerB());
            answerThreeBtn.setText(modelLearnArrayList.get(currentPos).getAnswerC());
            answerFourBtn.setText(modelLearnArrayList.get(currentPos).getAnswerD());
        }
    }

    private void getQuizQuestion(ArrayList<ModelQuizABCD> modelLearnArrayList) {
        modelLearnArrayList.add(new ModelQuizABCD("Dog", "Pies", "Kot", "Sikiratka", "Gil", "Pies"));
        modelLearnArrayList.add(new ModelQuizABCD("Cat", "Rosomak", "Kot", "Małpa", "Żyrafa", "Kot"));
        modelLearnArrayList.add(new ModelQuizABCD("Butterfly", "Karp", "Ślimak", "Rak", "Motyl", "Motyl"));
        modelLearnArrayList.add(new ModelQuizABCD("Boar", "Gąsienica", "Świnia", "Dzik", "Krab", "Dzik"));
    }

    private void setQuizButtonListeners() {
        answerOneBtn.setOnClickListener(v -> handleAnswerClick(answerOneBtn));
        answerTwoBtn.setOnClickListener(v -> handleAnswerClick(answerTwoBtn));
        answerThreeBtn.setOnClickListener(v -> handleAnswerClick(answerThreeBtn));
        answerFourBtn.setOnClickListener(v -> handleAnswerClick(answerFourBtn));
    }

    // Handle button click logic

    private void handleAnswerClick(Button button) {
        if (modelLearnArrayList.get(currentPos).getCorrectAnswer().trim().equalsIgnoreCase(button.getText().toString().trim())) {
            currentScore++;
        }
        questionAttempted++;
        currentPos = random.nextInt(modelLearnArrayList.size());
        setDataToViews(currentPos);
    }

    private void resultDialog() {
        resultDialog = createDialog(R.layout.dialog_learn_result);
        DialogLearnResultBinding dialogBinding = DialogLearnResultBinding.inflate(LayoutInflater.from(requireContext()));
        assert resultDialog != null;
        resultDialog.setContentView(dialogBinding.getRoot());
        resultDialog.show();

        TextView scoreTv = dialogBinding.learnScoreScoreTv;
        Button restartQuizBtn = dialogBinding.learnScoreRestartBtn;

        scoreTv.setText(currentScore + "/10");

        restartQuizBtn.setOnClickListener(v -> {
            resultDialog.dismiss();
            questionAttempted = 1;
            currentScore = 0;
            currentPos = random.nextInt(modelLearnArrayList.size());
            setDataToViews(currentPos);
        });
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
