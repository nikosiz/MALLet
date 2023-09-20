package com.example.mallet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.mallet.utils.Utils;

import java.util.Arrays;
import java.util.Collections;

public class FragmentMatch extends Fragment {
    TextView tv_p1, tv_p2;
    ImageView iv_11, iv_12, iv_21, iv_22, iv_31, iv_32, iv_41, iv_42, iv_51, iv_52;
    Integer[] cardsArray = {101, 102, 103, 104, 105, 201, 202, 203, 204, 205};
    int image101, image102, image103, image104, image105, image201, image202, image203, image204, image205;
    int firstCard, secondCard;
    int clickedFirst, clickedSecond;
    int cardNumber = 1;
    int turn = 1;
    int playerPoints = 0, cpuPoints = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the fragment's layout
        View rootView = inflater.inflate(R.layout.fragment_match, container, false);

        tv_p1 = rootView.findViewById(R.id.tv_p1);
        tv_p2 = rootView.findViewById(R.id.tv_p2);

        iv_11 = rootView.findViewById(R.id.iv_11);
        iv_12 = rootView.findViewById(R.id.iv_12);
        iv_21 = rootView.findViewById(R.id.iv_21);
        iv_22 = rootView.findViewById(R.id.iv_22);
        iv_31 = rootView.findViewById(R.id.iv_31);
        iv_32 = rootView.findViewById(R.id.iv_32);
        iv_41 = rootView.findViewById(R.id.iv_41);
        iv_42 = rootView.findViewById(R.id.iv_42);
        iv_51 = rootView.findViewById(R.id.iv_51);
        iv_52 = rootView.findViewById(R.id.iv_52);

        iv_11.setTag("0");
        iv_12.setTag("1");
        iv_21.setTag("2");
        iv_22.setTag("3");
        iv_31.setTag("4");
        iv_32.setTag("5");
        iv_41.setTag("6");
        iv_42.setTag("7");
        iv_51.setTag("8");
        iv_52.setTag("9");

        frontOfCardResources();

        Collections.shuffle(Arrays.asList(cardsArray));

        tv_p2.setTextColor(Color.GRAY);

        iv_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int theCard = Integer.parseInt((String) view.getTag());
                doStuff(iv_11, theCard);
            }
        });

        iv_12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int theCard = Integer.parseInt((String) view.getTag());
                doStuff(iv_12, theCard);
            }
        });

        iv_21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int theCard = Integer.parseInt((String) view.getTag());
                doStuff(iv_21, theCard);
            }
        });

        iv_22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int theCard = Integer.parseInt((String) view.getTag());
                doStuff(iv_22, theCard);
            }
        });

        iv_31.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int theCard = Integer.parseInt((String) view.getTag());
                doStuff(iv_31, theCard);
            }
        });

        iv_32.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int theCard = Integer.parseInt((String) view.getTag());
                doStuff(iv_32, theCard);
            }
        });

        iv_41.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int theCard = Integer.parseInt((String) view.getTag());
                doStuff(iv_41, theCard);
            }
        });

        iv_42.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int theCard = Integer.parseInt((String) view.getTag());
                doStuff(iv_42, theCard);
            }
        });

        iv_51.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int theCard = Integer.parseInt((String) view.getTag());
                doStuff(iv_51, theCard);
            }
        });

        iv_52.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int theCard = Integer.parseInt((String) view.getTag());
                doStuff(iv_52, theCard);
            }
        });

        return rootView;
    }

    private void doStuff(ImageView iv, int card) {
        if (cardsArray[card] == 101) {
            iv.setImageResource(image101);
        } else if (cardsArray[card] == 102) {
            iv.setImageResource(image102);
        } else if (cardsArray[card] == 103) {
            iv.setImageResource(image103);
        } else if (cardsArray[card] == 104) {
            iv.setImageResource(image104);
        } else if (cardsArray[card] == 105) {
            iv.setImageResource(image105);
        } else if (cardsArray[card] == 201) {
            iv.setImageResource(image201);
        } else if (cardsArray[card] == 202) {
            iv.setImageResource(image202);
        } else if (cardsArray[card] == 203) {
            iv.setImageResource(image203);
        } else if (cardsArray[card] == 204) {
            iv.setImageResource(image204);
        } else if (cardsArray[card] == 205) {
            iv.setImageResource(image205);
        }

        if (cardNumber == 1) {
            firstCard = cardsArray[card];
            if (firstCard > 200) {
                firstCard = firstCard - 100;
            }
            cardNumber = 2;
            clickedFirst = card;

            iv.setEnabled(false);
        } else if (cardNumber == 2) {
            secondCard = cardsArray[card];
            if (secondCard > 200) {
                secondCard = secondCard - 100;
            }
            cardNumber = 1;
            clickedSecond = card;

            iv_11.setEnabled(false);
            iv_12.setEnabled(false);
            iv_21.setEnabled(false);
            iv_22.setEnabled(false);
            iv_31.setEnabled(false);
            iv_32.setEnabled(false);
            iv_41.setEnabled(false);
            iv_42.setEnabled(false);
            iv_51.setEnabled(false);
            iv_52.setEnabled(false);


            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    calculate();
                }
            }, 1000); // Specify the delay in milliseconds
        }
    }

    private void calculate() {
        if (firstCard == secondCard) {
            if (clickedFirst == 0) {
                iv_11.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 1) {
                iv_12.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 2) {
                iv_21.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 3) {
                iv_22.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 4) {
                iv_31.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 5) {
                iv_32.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 6) {
                iv_41.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 7) {
                iv_42.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 8) {
                iv_51.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 9) {
                iv_52.setVisibility(View.INVISIBLE);
            }

            if (clickedSecond == 0) {
                iv_11.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 1) {
                iv_12.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 2) {
                iv_21.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 3) {
                iv_22.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 4) {
                iv_31.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 5) {
                iv_32.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 6) {
                iv_41.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 7) {
                iv_42.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 8) {
                iv_51.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 9) {
                iv_52.setVisibility(View.INVISIBLE);
            }

            if (turn == 1) {
                playerPoints++;
                tv_p1.setText("P1: " + playerPoints);
            } else if (turn == 2) {
                cpuPoints++;
                tv_p2.setText("P2: " + cpuPoints);
            }
        } else {
            iv_11.setImageResource(R.drawable.img_test);
            iv_12.setImageResource(R.drawable.img_test);
            iv_21.setImageResource(R.drawable.img_test);
            iv_22.setImageResource(R.drawable.img_test);
            iv_31.setImageResource(R.drawable.img_test);
            iv_32.setImageResource(R.drawable.img_test);
            iv_41.setImageResource(R.drawable.img_test);
            iv_42.setImageResource(R.drawable.img_test);
            iv_51.setImageResource(R.drawable.img_test);
            iv_52.setImageResource(R.drawable.img_test);

            if (turn == 1) {
                turn = 2;
                tv_p1.setTextColor(Color.GRAY);
                tv_p2.setTextColor(Color.BLACK);
            } else if (turn == 2) {
                turn = 1;
                tv_p1.setTextColor(Color.BLACK);
                tv_p2.setTextColor(Color.GRAY);
            }
        }

        iv_11.setEnabled(true);
        iv_12.setEnabled(true);
        iv_21.setEnabled(true);
        iv_22.setEnabled(true);
        iv_31.setEnabled(true);
        iv_32.setEnabled(true);
        iv_41.setEnabled(true);
        iv_42.setEnabled(true);
        iv_51.setEnabled(true);
        iv_52.setEnabled(true);

        checkEnd();
    }

    private void checkEnd() {
        if (iv_11.getVisibility() == View.INVISIBLE &&
                iv_12.getVisibility() == View.INVISIBLE &&
                iv_21.getVisibility() == View.INVISIBLE &&
                iv_22.getVisibility() == View.INVISIBLE &&
                iv_31.getVisibility() == View.INVISIBLE &&
                iv_32.getVisibility() == View.INVISIBLE &&
                iv_41.getVisibility() == View.INVISIBLE &&
                iv_42.getVisibility() == View.INVISIBLE &&
                iv_51.getVisibility() == View.INVISIBLE &&
                iv_52.getVisibility() == View.INVISIBLE) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
            alertDialogBuilder
                    .setMessage("GAME OVER!\nP1: " + playerPoints + "\nP2: " + cpuPoints)
                    .setCancelable(false)
                    .setPositiveButton("NEW", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Utils.openActivityWithFragment(getContext(), FragmentMatch.class, ActivityLearn.class);
                            requireActivity().finish();
                        }
                    })
                    .setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            requireActivity().finish();
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    private void frontOfCardResources() {
        image101 = R.drawable.image_1;
        image102 = R.drawable.image_2;
        image103 = R.drawable.image_3;
        image104 = R.drawable.image_4;
        image105 = R.drawable.image_5;
        image201 = R.drawable.image_1;
        image202 = R.drawable.image_2;
        image203 = R.drawable.image_3;
        image204 = R.drawable.image_4;
        image205 = R.drawable.image_5;
    }
}
