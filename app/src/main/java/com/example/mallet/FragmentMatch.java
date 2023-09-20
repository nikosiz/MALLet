package com.example.mallet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.mallet.utils.Utils;

import java.util.Arrays;
import java.util.Collections;

public class FragmentMatch extends Fragment {
    private CardView card1, card2, card3, card4, card5, card6, card7, card8, card9, card10;

    Integer[] cardsArray = {101, 102, 201, 202, 301, 302, 401, 402, 501, 502};
    int text101, text102, text201, text202, text301, text302, text401, text402, text501, text502;
    int firstCard, secondCard;
    int clickedFirst, clickedSecond;
    int cardNumber = 1;

    int turn = 1;
    int playerPts = 0, cpuPts = 0;
    TextView pointsTv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the fragment's layout
        View rootView = inflater.inflate(R.layout.fragment_match, container, false);

        pointsTv = rootView.findViewById(R.id.match_pointsTv);

        // Find the CardView within the inflated layout
        card1 = rootView.findViewById(R.id.matchM_card1);
        TextView cardTv1 = rootView.findViewById(R.id.matchM_cardTv1);
        card2 = rootView.findViewById(R.id.matchM_card2);
        TextView cardTv2 = rootView.findViewById(R.id.matchM_cardTv2);
        card3 = rootView.findViewById(R.id.matchM_card3);
        TextView cardTv3 = rootView.findViewById(R.id.matchM_cardTv3);
        card4 = rootView.findViewById(R.id.matchM_card4);
        TextView cardTv4 = rootView.findViewById(R.id.matchM_cardTv4);
        card5 = rootView.findViewById(R.id.matchM_card5);
        TextView cardTv5 = rootView.findViewById(R.id.matchM_cardTv5);
        card6 = rootView.findViewById(R.id.matchM_card6);
        TextView cardTv6 = rootView.findViewById(R.id.matchM_cardTv6);
        card7 = rootView.findViewById(R.id.matchM_card7);
        TextView cardTv7 = rootView.findViewById(R.id.matchM_cardTv7);
        card8 = rootView.findViewById(R.id.matchM_card8);
        TextView cardTv8 = rootView.findViewById(R.id.matchM_cardTv8);
        card9 = rootView.findViewById(R.id.matchM_card9);
        TextView cardTv9 = rootView.findViewById(R.id.matchM_cardTv9);
        card10 = rootView.findViewById(R.id.matchM_card10);
        TextView cardTv10 = rootView.findViewById(R.id.matchM_cardTv10);

        cardTv1.setTag("0");
        cardTv2.setTag("1");
        cardTv3.setTag("2");
        cardTv4.setTag("3");
        cardTv5.setTag("4");
        cardTv6.setTag("5");
        cardTv7.setTag("6");
        cardTv8.setTag("7");
        cardTv9.setTag("8");
        cardTv10.setTag("9");

        cardsFront();

        Collections.shuffle(Arrays.asList(cardsArray));

        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int theCard = Integer.parseInt((String) view.getTag());

                doStuff(cardTv1, theCard);
                Utils.showToast(

                        getContext(), "card1");
            }
        });
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int theCard = Integer.parseInt((String) view.getTag());

                doStuff(cardTv1, theCard);
                Utils.showToast(

                        getContext(), "card2");
            }
        });
        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int theCard = Integer.parseInt((String) view.getTag());

                doStuff(cardTv1, theCard);
                Utils.showToast(

                        getContext(), "card3");
            }
        });
        card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int theCard = Integer.parseInt((String) view.getTag());

                doStuff(cardTv1, theCard);
                Utils.showToast(

                        getContext(), "card4");
            }
        });
        card5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int theCard = Integer.parseInt((String) view.getTag());

                doStuff(cardTv1, theCard);
                Utils.showToast(

                        getContext(), "card5");
            }
        });
        card6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int theCard = Integer.parseInt((String) view.getTag());

                doStuff(cardTv1, theCard);
                Utils.showToast(

                        getContext(), "card6");
            }
        });
        card7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int theCard = Integer.parseInt((String) view.getTag());

                doStuff(cardTv1, theCard);
                Utils.showToast(

                        getContext(), "card7");
            }
        });
        card8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int theCard = Integer.parseInt((String) view.getTag());

                doStuff(cardTv1, theCard);
                Utils.showToast(

                        getContext(), "card8");
            }
        });
        card9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int theCard = Integer.parseInt((String) view.getTag());

                doStuff(cardTv1, theCard);
                Utils.showToast(

                        getContext(), "card9");
            }
        });
        card10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int theCard = Integer.parseInt((String) view.getTag());

                doStuff(cardTv1, theCard);
                Utils.showToast(

                        getContext(), "card10");
            }
        });
        return rootView;
    }

    private void doStuff(TextView cvTv, int card) {
        if (cardsArray[card] == 101) {
            cvTv.setText(text101);
        } else if (cardsArray[card] == 102) {
            cvTv.setText(text102);
        } else if (cardsArray[card] == 201) {
            cvTv.setText(text201);
        } else if (cardsArray[card] == 202) {
            cvTv.setText(text202);
        } else if (cardsArray[card] == 301) {
            cvTv.setText(text301);
        } else if (cardsArray[card] == 302) {
            cvTv.setText(text302);
        } else if (cardsArray[card] == 401) {
            cvTv.setText(text401);
        } else if (cardsArray[card] == 402) {
            cvTv.setText(text402);
        } else if (cardsArray[card] == 501) {
            cvTv.setText(text501);
        } else if (cardsArray[card] == 502) {
            cvTv.setText(text502);
        }

        if (cardNumber == 1) {
            firstCard = cardsArray[card];
            if (firstCard > 200) {
                firstCard = secondCard - 100;
            }
            cardNumber = 2;
            clickedFirst = card;

            cvTv.setEnabled(false);
        } else if (cardNumber == 2) {
            secondCard = cardsArray[card];
            if (secondCard > 200) {
                secondCard = secondCard - 100;
            }
            cardNumber = 1;
            clickedSecond = card;

            cvTv.setEnabled(false);
        }

        card1.setEnabled(false);
        card2.setEnabled(false);
        card3.setEnabled(false);
        card4.setEnabled(false);
        card5.setEnabled(false);
        card6.setEnabled(false);
        card7.setEnabled(false);
        card8.setEnabled(false);
        card9.setEnabled(false);
        card10.setEnabled(false);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Your code to be executed after the delay
                calculate();
            }
        }, 2000); // Specify the delay in milliseconds
    }

    private void calculate() {
        if (firstCard == secondCard) {
            if (clickedFirst == 1) {
                card1.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 2) {
                card2.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 3) {
                card3.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 4) {
                card4.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 5) {
                card5.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 6) {
                card6.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 7) {
                card7.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 8) {
                card8.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 9) {
                card9.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 10) {
                card10.setVisibility(View.INVISIBLE);
            }

            if (clickedSecond == 1) {
                card1.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 2) {
                card2.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 3) {
                card3.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 4) {
                card4.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 5) {
                card5.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 6) {
                card6.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 7) {
                card7.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 8) {
                card8.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 9) {
                card9.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 10) {
                card10.setVisibility(View.INVISIBLE);
            }

            if (turn == 1) {
                playerPts++;
                pointsTv.setText("Points: " + playerPts);
            }
        } else {
            card1.setVisibility(View.GONE);
            card2.setVisibility(View.GONE);
            card3.setVisibility(View.GONE);
            card4.setVisibility(View.GONE);
            card5.setVisibility(View.GONE);
            card6.setVisibility(View.GONE);
            card7.setVisibility(View.GONE);
            card8.setVisibility(View.GONE);
            card9.setVisibility(View.GONE);
            card10.setVisibility(View.GONE);

            if (turn == 1) {
                turn = 2;
                Utils.showToast(getContext(), "Turn 2");
            } else if (turn == 2) {
                turn = 1;
                Utils.showToast(getContext(), "Turn 2");
            }
        }

        card1.setEnabled(true);
        card2.setEnabled(true);
        card3.setEnabled(true);
        card4.setEnabled(true);
        card5.setEnabled(true);
        card6.setEnabled(true);
        card7.setEnabled(true);
        card8.setEnabled(true);
        card9.setEnabled(true);
        card10.setEnabled(true);

        checkEnd();
    }

    private void checkEnd() {
        if (card1.getVisibility() == View.INVISIBLE && card2.getVisibility() == View.INVISIBLE &&
                card3.getVisibility() == View.INVISIBLE && card4.getVisibility() == View.INVISIBLE &&
                card5.getVisibility() == View.INVISIBLE && card6.getVisibility() == View.INVISIBLE &&
                card7.getVisibility() == View.INVISIBLE && card8.getVisibility() == View.INVISIBLE &&
                card9.getVisibility() == View.INVISIBLE && card10.getVisibility() == View.INVISIBLE) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
            alertDialogBuilder.setMessage("GAME OVER\nPOINTS: " + playerPts + "\nCPU:" + cpuPts).setCancelable(false).setPositiveButton("NEW", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(getContext(), ActivityLearn.class);
                    startActivity(intent);
                    requireActivity().finish();
                }
            }).setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    requireActivity().finish();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    private void cardsFront() {
        text101 = R.string.about;
        text102 = R.string.back_arrow;
        text201 = R.string.cancel;
        text202 = R.string.dark_mode;
        text301 = R.string.email;
        text302 = R.string.facebook;
        text401 = R.string.google;
        text402 = R.string.home;
        text501 = R.string.input_new_password_error;
        text502 = R.string.library;
    }
}
