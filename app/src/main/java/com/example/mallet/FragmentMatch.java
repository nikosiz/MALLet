package com.example.mallet;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.mallet.databinding.DialogAreYouReadyBinding;
import com.example.mallet.databinding.DialogGameOverBinding;
import com.example.mallet.databinding.FragmentMatchBinding;
import com.example.mallet.utils.ModelFlashcard;
import com.example.mallet.utils.ModelLearningSet;
import com.example.mallet.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class FragmentMatch extends Fragment {
    private ActivityLearn activityLearn;
    private FragmentMatchBinding binding;
    private List<ModelFlashcard> flashcardList;
    private final Integer[] cardsArray = {101, 102, 103, 104, 105, 201, 202, 203, 204, 205};
    private Chronometer chronometer;
    private TextView tv_p1;
    private CardView cv_11, cv_12, cv_21, cv_22, cv_31, cv_32, cv_41, cv_42, cv_51, cv_52;
    private TextView cv_11Tv, cv_12Tv, cv_21Tv, cv_22Tv, cv_31Tv, cv_32Tv, cv_41Tv, cv_42Tv, cv_51Tv, cv_52Tv;
    private TextView pointsTv;
    private String text101, text102, text103, text104, text105, text201, text202, text203, text204, text205;
    private int firstCard, secondCard;
    private int clickedFirst, clickedSecond;
    private int cardNr = 1;
    private int playerPoints = 0;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof ActivityLearn) {
            activityLearn = (ActivityLearn) context;
        } else {
            // Handle the case where the activity does not implement the method
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMatchBinding.inflate(inflater, container, false);

        setupContents();

        generateTextVariables(flashcardList);

        View rootView = inflater.inflate(R.layout.fragment_match, container, false);

        chronometer = rootView.findViewById(R.id.matchF_chronometer);
        pointsTv = rootView.findViewById(R.id.matchF_pointsTv);

        areYouReadyDialog();

        Utils.openActivityWithFragment(getContext(), FragmentMatch.class, ActivityLearn.class);

        cv_11 = rootView.findViewById(R.id.cv_11);
        cv_11Tv = rootView.findViewById(R.id.cv_11Tv);
        cv_12 = rootView.findViewById(R.id.cv_12);
        cv_12Tv = rootView.findViewById(R.id.cv_12Tv);
        cv_21 = rootView.findViewById(R.id.cv_21);
        cv_21Tv = rootView.findViewById(R.id.cv_21Tv);
        cv_22 = rootView.findViewById(R.id.cv_22);
        cv_22Tv = rootView.findViewById(R.id.cv_22Tv);
        cv_31 = rootView.findViewById(R.id.cv_31);
        cv_31Tv = rootView.findViewById(R.id.cv_31Tv);
        cv_32 = rootView.findViewById(R.id.cv_32);
        cv_32Tv = rootView.findViewById(R.id.cv_32Tv);
        cv_41 = rootView.findViewById(R.id.cv_41);
        cv_41Tv = rootView.findViewById(R.id.cv_41Tv);
        cv_42 = rootView.findViewById(R.id.cv_42);
        cv_42Tv = rootView.findViewById(R.id.cv_42Tv);
        cv_51 = rootView.findViewById(R.id.cv_51);
        cv_51Tv = rootView.findViewById(R.id.cv_51Tv);
        cv_52 = rootView.findViewById(R.id.cv_52);
        cv_52Tv = rootView.findViewById(R.id.cv_52Tv);

        cv_11.setTag("0");
        cv_12.setTag("1");
        cv_21.setTag("2");
        cv_22.setTag("3");
        cv_31.setTag("4");
        cv_32.setTag("5");
        cv_41.setTag("6");
        cv_42.setTag("7");
        cv_51.setTag("8");
        cv_52.setTag("9");

        //Collections.shuffle(Arrays.asList(cardsArray));

        cv_11Tv.setText(text101);
        cv_12Tv.setText(text102);
        cv_21Tv.setText(text103);
        cv_22Tv.setText(text104);
        cv_31Tv.setText(text105);
        cv_32Tv.setText(text201);
        cv_41Tv.setText(text202);
        cv_42Tv.setText(text203);
        cv_51Tv.setText(text204);
        cv_52Tv.setText(text205);

        cv_11.setOnClickListener(view -> {
            int theCard = Integer.parseInt((String) view.getTag());
            handleMatching(cv_11, cv_11Tv, theCard);
        });
        cv_12.setOnClickListener(view -> {
            int theCard = Integer.parseInt((String) view.getTag());
            handleMatching(cv_12, cv_12Tv, theCard);
        });
        cv_21.setOnClickListener(view -> {
            int theCard = Integer.parseInt((String) view.getTag());
            handleMatching(cv_21, cv_21Tv, theCard);
        });
        cv_22.setOnClickListener(view -> {
            int theCard = Integer.parseInt((String) view.getTag());
            handleMatching(cv_22, cv_22Tv, theCard);
        });
        cv_31.setOnClickListener(view -> {
            int theCard = Integer.parseInt((String) view.getTag());
            handleMatching(cv_31, cv_31Tv, theCard);
        });
        cv_32.setOnClickListener(view -> {
            int theCard = Integer.parseInt((String) view.getTag());
            handleMatching(cv_32, cv_32Tv, theCard);
        });
        cv_41.setOnClickListener(view -> {
            int theCard = Integer.parseInt((String) view.getTag());
            handleMatching(cv_41, cv_41Tv, theCard);
        });
        cv_42.setOnClickListener(view -> {
            int theCard = Integer.parseInt((String) view.getTag());
            handleMatching(cv_42, cv_42Tv, theCard);
        });
        cv_51.setOnClickListener(view -> {
            int theCard = Integer.parseInt((String) view.getTag());
            handleMatching(cv_51, cv_51Tv, theCard);
        });
        cv_52.setOnClickListener(view -> {
            int theCard = Integer.parseInt((String) view.getTag());
            handleMatching(cv_52, cv_52Tv, theCard);
        });

        return rootView;
    }

    private void setupContents() {
        setupToolbar();

        flashcardList = getLearningSetData();
    }

    private void setupToolbar() {
        Toolbar toolbar = binding.matchFToolbar;
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("");

        ImageView backIv = binding.matchFToolbarBackIv;

        backIv.setOnClickListener(v -> activityLearn.confirmExitDialog());
    }

    private void areYouReadyDialog() {
        Dialog dialog = Utils.createDialog(requireContext(), R.layout.dialog_are_you_ready, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT), Gravity.BOTTOM);
        DialogAreYouReadyBinding dialogBinding = DialogAreYouReadyBinding.inflate(LayoutInflater.from(getContext()));
        Objects.requireNonNull(dialog).setContentView(dialogBinding.getRoot());
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        TextView readyStartTv = dialogBinding.readyStartTv;

        readyStartTv.setOnClickListener(v -> {
            dialog.dismiss();

            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
        });
    }


    private void handleMatching(CardView cv, TextView tv, int card) {

        if (cardNr == 1) {
            firstCard = cardsArray[card];
            if (firstCard > 200) {
                firstCard = firstCard - 100;
            }
            cardNr = 2;
            clickedFirst = card;

            cv.setEnabled(false);
        } else if (cardNr == 2) {
            secondCard = cardsArray[card];
            if (secondCard > 200) {
                secondCard = secondCard - 100;
            }
            cardNr = 1;
            clickedSecond = card;

            cv_11.setEnabled(false);
            cv_12.setEnabled(false);
            cv_21.setEnabled(false);
            cv_22.setEnabled(false);
            cv_31.setEnabled(false);
            cv_32.setEnabled(false);
            cv_41.setEnabled(false);
            cv_42.setEnabled(false);
            cv_51.setEnabled(false);
            cv_52.setEnabled(false);


            Handler handler = new Handler();
            handler.postDelayed(this::calculate, 200);
        }
    }

    private void calculate() {
        if (firstCard == secondCard) {
            if (clickedFirst == 0) {
                cv_11.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 1) {
                cv_12.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 2) {
                cv_21.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 3) {
                cv_22.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 4) {
                cv_31.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 5) {
                cv_32.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 6) {
                cv_41.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 7) {
                cv_42.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 8) {
                cv_51.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 9) {
                cv_52.setVisibility(View.INVISIBLE);
            }

            if (clickedSecond == 0) {
                cv_11.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 1) {
                cv_12.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 2) {
                cv_21.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 3) {
                cv_22.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 4) {
                cv_31.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 5) {
                cv_32.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 6) {
                cv_41.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 7) {
                cv_42.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 8) {
                cv_51.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 9) {
                cv_52.setVisibility(View.INVISIBLE);
            }

            playerPoints++;
            pointsTv.setText(Integer.toString(playerPoints));
            //tv_p1.setText("P1: " + playerPoints);

        } else {
            //playerPoints--;
            chronometer.setBase(chronometer.getBase() - 5 * 1000);

        }

        cv_11.setEnabled(true);
        cv_12.setEnabled(true);
        cv_21.setEnabled(true);
        cv_22.setEnabled(true);
        cv_31.setEnabled(true);
        cv_32.setEnabled(true);
        cv_41.setEnabled(true);
        cv_42.setEnabled(true);
        cv_51.setEnabled(true);
        cv_52.setEnabled(true);

        checkEnd();
    }

    private void checkEnd() {
        if (cv_11.getVisibility() == View.INVISIBLE &&
                cv_12.getVisibility() == View.INVISIBLE &&
                cv_21.getVisibility() == View.INVISIBLE &&
                cv_22.getVisibility() == View.INVISIBLE &&
                cv_31.getVisibility() == View.INVISIBLE &&
                cv_32.getVisibility() == View.INVISIBLE &&
                cv_41.getVisibility() == View.INVISIBLE &&
                cv_42.getVisibility() == View.INVISIBLE &&
                cv_51.getVisibility() == View.INVISIBLE &&
                cv_52.getVisibility() == View.INVISIBLE) {

            chronometer.stop();

            gameOverDialog();
        }
    }

    private void gameOverDialog() {
        Dialog dialog = Utils.createDialog(requireContext(), R.layout.dialog_game_over, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT), Gravity.BOTTOM);
        DialogGameOverBinding dialogBinding = DialogGameOverBinding.inflate(getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());
        dialog.show();

        /*TextView scoreTv = dialogBinding.gameOverScoreTv;
        scoreTv.setText("Your score: " + playerPoints);*/

        TextView timeTv = dialogBinding.gameOverTimeTv;
        timeTv.setText("Your time: " + chronometer.getText());

        TextView finish = dialogBinding.gameOverFinishTv;
        TextView restart = dialogBinding.gameOverRestartTv;

        restart.setOnClickListener(v -> {
            dialog.dismiss();
            requireActivity().finish();
        });

        finish.setOnClickListener(v -> requireActivity().finish());
    }

    /*private void restart() {
        generateTextVariables(flashcardList);
    }*/

    private ModelLearningSet learningSet;

    private List<ModelFlashcard> getLearningSetData() {
        Bundle args = getArguments();
        if (args != null) {
            learningSet = args.getParcelable("learningSet");
            if (learningSet != null) {
                flashcardList = learningSet.getTerms();
                return flashcardList;
            }
        }

        return new ArrayList<>();
    }

    private void generateTextVariables(List<ModelFlashcard> flashcards) {
        // Shuffle the flashcards
        List<ModelFlashcard> shuffledFlashcards = new ArrayList<>(flashcards);
        Collections.shuffle(shuffledFlashcards);

        // Initialize text variables for terms and definitions
        String[] termTexts = new String[5];
        String[] definitionTexts = new String[5];

        // Select five random terms and definitions
        for (int i = 0; i < 5; i++) {
            ModelFlashcard flashcard = shuffledFlashcards.get(i);
            termTexts[i] = flashcard.getTerm();
            definitionTexts[i] = flashcard.getDefinition();
        }

        // Assign the generated texts to variables
        text101 = termTexts[0];
        text102 = termTexts[1];
        text103 = termTexts[2];
        text104 = termTexts[3];
        text105 = termTexts[4];

        text201 = definitionTexts[0];
        text202 = definitionTexts[1];
        text203 = definitionTexts[2];
        text204 = definitionTexts[3];
    }

}