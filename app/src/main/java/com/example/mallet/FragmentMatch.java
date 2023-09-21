package com.example.mallet;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.mallet.databinding.DialogAreYouReadyBinding;
import com.example.mallet.utils.Utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

public class FragmentMatch extends Fragment {
    private Chronometer chronometer;
    TextView tv_p1;
    CardView cv_11, cv_12, cv_21, cv_22, cv_31, cv_32, cv_41, cv_42, cv_51, cv_52;
    TextView cv_11Tv, cv_12Tv, cv_21Tv, cv_22Tv, cv_31Tv, cv_32Tv, cv_41Tv, cv_42Tv, cv_51Tv, cv_52Tv;
    Integer[] cardsArray = {101, 102, 103, 104, 105, 201, 202, 203, 204, 205};
    int text101, text102, text103, text104, text105, text201, text202, text203, text204, text205;
    int firstCard, secondCard;
    int clickedFirst, clickedSecond;
    int cardNumber = 1;
    int turn = 1;
    int playerPoints = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_match, container, false);

        chronometer = rootView.findViewById(R.id.matchChronometer);

        areYouReadyDialog();

        tv_p1 = rootView.findViewById(R.id.tv_p1);

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

        cardResources();

        Collections.shuffle(Arrays.asList(cardsArray));

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

    private void areYouReadyDialog() {
        final Dialog dialog = Utils.createDialog(getContext(), R.layout.dialog_are_you_ready);
        DialogAreYouReadyBinding dialogBinding = DialogAreYouReadyBinding.inflate(LayoutInflater.from(getContext()));
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Objects.requireNonNull(dialog).setContentView(dialogBinding.getRoot());
        dialog.show();

        TextView readyStartTv = dialogBinding.readyStartTv;

        readyStartTv.setOnClickListener(v -> {
            dialog.dismiss();
            // Start the chronometer here
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
        });
    }


    private void handleMatching(CardView cv, TextView tv, int card) {

        if (cardsArray[card] == 101) {
            tv.setText(text101);
        } else if (cardsArray[card] == 102) {
            tv.setText(text102);
        } else if (cardsArray[card] == 103) {
            tv.setText(text103);
        } else if (cardsArray[card] == 104) {
            tv.setText(text104);
        } else if (cardsArray[card] == 105) {
            tv.setText(text105);
        } else if (cardsArray[card] == 201) {
            tv.setText(text201);
        } else if (cardsArray[card] == 202) {
            tv.setText(text202);
        } else if (cardsArray[card] == 203) {
            tv.setText(text203);
        } else if (cardsArray[card] == 204) {
            tv.setText(text204);
        } else if (cardsArray[card] == 205) {
            tv.setText(text205);
        }

        if (cardNumber == 1) {
            firstCard = cardsArray[card];
            if (firstCard > 200) {
                firstCard = firstCard - 100;
            }
            cardNumber = 2;
            clickedFirst = card;

            cv.setEnabled(false);
        } else if (cardNumber == 2) {
            secondCard = cardsArray[card];
            if (secondCard > 200) {
                secondCard = secondCard - 100;
            }
            cardNumber = 1;
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
            handler.postDelayed(this::calculate, 1000);
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
            tv_p1.setText("P1: " + playerPoints);

        } else {
            /*cv_11Tv.setText("");
            cv_12Tv.setText("");
            cv_21Tv.setText("");
            cv_22Tv.setText("");
            cv_31Tv.setText("");
            cv_32Tv.setText("");
            cv_41Tv.setText("");
            cv_42Tv.setText("");
            cv_51Tv.setText("");
            cv_52Tv.setText("");*/

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

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
            alertDialogBuilder
                    .setMessage("GAME OVER!\nP1: " + playerPoints)
                    .setCancelable(false)
                    .setPositiveButton("NEW", (dialogInterface, i) -> {
                        Utils.openActivityWithFragment(getContext(), FragmentMatch.class, ActivityLearn.class);
                        requireActivity().finish();
                    })
                    .setNegativeButton("EXIT", (dialogInterface, i) -> requireActivity().finish());

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    private void cardResources() {
        text101 = R.string.about;
        text102 = R.string.back_arrow;
        text103 = R.string.cancel;
        text104 = R.string.dark_mode;
        text105 = R.string.email;
        text201 = R.string.about;
        text202 = R.string.back_arrow;
        text203 = R.string.cancel;
        text204 = R.string.dark_mode;
        text205 = R.string.email;
    }
}
