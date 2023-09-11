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

    FragmentLearnBinding binding;
    private Dialog resultDialog; // Declare the Dialog as a class member
    private TextView questionTv, questionNumberTV;
    private Button answerABtn, answerBBtn, answerCBtn, answerDBtn;
    private ArrayList<ModelLearnFragment> modelLearnArrayList;
    Random random;
    int currentScore = 0, questionAttempted = 1, currentPos;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLearnBinding.inflate(inflater, container, false);

        questionTv = binding.idTVQuestion;
        questionNumberTV = binding.idTVQuestionAttempted;
        answerABtn = binding.idBtnOption1;
        answerBBtn = binding.idBtnOption2;
        answerCBtn = binding.idBtnOption3;
        answerDBtn = binding.idBtnOption4;
        modelLearnArrayList = new ArrayList<>();
        random = new Random();
        getQuizQuestion(modelLearnArrayList);

        currentPos = random.nextInt(modelLearnArrayList.size());
        setDataToViews(currentPos);

        answerABtn.setOnClickListener(v -> {
            if (modelLearnArrayList.get(currentPos).getRightAnswer().trim().equalsIgnoreCase(answerABtn.getText().toString().trim())) {
                currentScore++;
            }
            questionAttempted++;
            currentPos = random.nextInt(modelLearnArrayList.size());
            setDataToViews(currentPos);
        });

        answerBBtn.setOnClickListener(v -> {
            if (modelLearnArrayList.get(currentPos).getRightAnswer().trim().equalsIgnoreCase(answerBBtn.getText().toString().trim())) {
                currentScore++;
            }
            questionAttempted++;
            currentPos = random.nextInt(modelLearnArrayList.size());
            setDataToViews(currentPos);
        });

        answerCBtn.setOnClickListener(v -> {
            if (modelLearnArrayList.get(currentPos).getRightAnswer().trim().equalsIgnoreCase(answerCBtn.getText().toString().trim())) {
                currentScore++;
            }
            questionAttempted++;
            currentPos = random.nextInt(modelLearnArrayList.size());
            setDataToViews(currentPos);
        });

        answerDBtn.setOnClickListener(v -> {
            if (modelLearnArrayList.get(currentPos).getRightAnswer().trim().equalsIgnoreCase(answerDBtn.getText().toString().trim())) {
                currentScore++;
            }
            questionAttempted++;
            currentPos = random.nextInt(modelLearnArrayList.size());
            setDataToViews(currentPos);
        });

        return binding.getRoot();
    }

    private void setDataToViews(int currentPos) {
        questionNumberTV.setText("Questions Attempted: " + questionAttempted + "/10");
        if (questionAttempted > 10) {
            questionNumberTV.setText("Questions Attempted: 10/10");
            resultDialog();


        } else {
            questionTv.setText(modelLearnArrayList.get(currentPos).getQuestion());
            answerABtn.setText(modelLearnArrayList.get(currentPos).getAnswerA());
            answerBBtn.setText(modelLearnArrayList.get(currentPos).getAnswerB());
            answerCBtn.setText(modelLearnArrayList.get(currentPos).getAnswerC());
            answerDBtn.setText(modelLearnArrayList.get(currentPos).getAnswerD());
        }
    }

    private void getQuizQuestion(ArrayList<ModelLearnFragment> modelLearnArrayList) {
        modelLearnArrayList.add(new ModelLearnFragment("Dog", "Pies", "Kot", "Sikiratka", "Gil", "Pies"));
        modelLearnArrayList.add(new ModelLearnFragment("Cat", "Rosomak", "Kot", "Małpa", "Żyrafa", "Kot"));
        modelLearnArrayList.add(new ModelLearnFragment("Butterfly", "Karp", "Ślimak", "Rak", "Motyl", "Motyl"));
        modelLearnArrayList.add(new ModelLearnFragment("Boar", "Gąsienica", "Świnia", "Dzik", "Krab", "Dzik"));
    }

    private void resultDialog() {
        if (resultDialog == null) { // Create the dialog only once
            resultDialog = createDialog(R.layout.dialog_learn_result);
            DialogLearnResultBinding dialogBinding = DialogLearnResultBinding.inflate(LayoutInflater.from(requireContext()));

            TextView scoreTv = dialogBinding.idScore;
            Button restartQuizBtn = dialogBinding.idBtnRestart;

            scoreTv.setText(currentScore + "/10");

            resultDialog.setContentView(dialogBinding.getRoot());

            restartQuizBtn.setOnClickListener(v -> {
                currentPos = random.nextInt(modelLearnArrayList.size());
                setDataToViews(currentPos);
                questionAttempted = 1;
                resultDialog.dismiss();
                currentScore = 0;
            });
        }

        resultDialog.show(); // Show the dialog when needed
    }


    private Dialog createDialog(int layoutResId) {
        final Dialog dialog = new Dialog(Objects.requireNonNull(getContext()));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layoutResId);
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        return dialog;
    }
}
