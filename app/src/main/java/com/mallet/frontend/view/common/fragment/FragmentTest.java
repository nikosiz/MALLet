package com.mallet.frontend.view.common.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.mallet.frontend.view.common.activity.ActivityLearn;
import com.example.mallet.R;
import com.example.mallet.databinding.DialogTestAreYouReadyBinding;
import com.example.mallet.databinding.DialogTestFinishedBinding;
import com.example.mallet.databinding.DialogTestNotEnoughBinding;
import com.example.mallet.databinding.FragmentTestBinding;
import com.mallet.frontend.model.question.ModelAnswer;
import com.mallet.frontend.model.flashcard.ModelFlashcard;
import com.mallet.frontend.model.question.ModelSingleChoice;
import com.mallet.frontend.model.question.ModelTrueFalse;
import com.mallet.frontend.model.question.ModelWritten;
import com.mallet.frontend.utils.ViewUtils;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.mallet.MALLet;

import org.apache.commons.lang3.time.StopWatch;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @noinspection deprecation
 */
public class FragmentTest extends Fragment {
    private ActivityLearn activityLearn;
    private FragmentTestBinding binding;
    private List<ModelFlashcard> flashcards;
    private LinearLayout questionsLl, answersLl;
    private View writtenQuestionView;
    private TextView writtenQuestionTv;
    private TextInputEditText writtenAnswerEt;
    private String writtenAnswer;
    private List<ModelWritten> writtenQuestions;
    private TextView correctAnswersTv;
    private View multipleChoiceQuestionView;
    private TextView multipleChoiceQuestionTv;
    private List<ModelSingleChoice> multipleChoiceQuestions;
    private List<ModelTrueFalse> trueFalseQuestions;

    private TextView prevTv, finishTv, errorTv;
    private ExtendedFloatingActionButton nextEfab;
    private int currentQuestionIndex = 0;
    private final StopWatch stopWatch = new StopWatch();
    private View truefalseQuestionView;


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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTestBinding.inflate(inflater, container, false);

        writtenQuestionView = inflater.inflate(R.layout.model_written, container, false);
        multipleChoiceQuestionView = inflater.inflate(R.layout.model_multiple_choice, container, false);
        truefalseQuestionView = inflater.inflate(R.layout.model_true_false, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireActivity());

        setupContents();

        return binding.getRoot();
    }

    private void setupContents() {
        setupToolbar();

        questionsLl = binding.testQuestionsLl;

        nextEfab = binding.testNextEfab;
        nextEfab.setOnClickListener(v -> nextQuestion());
        ViewUtils.showItems(nextEfab);

        writtenQuestionTv = writtenQuestionView.findViewById(R.id.written_questionTv);
        writtenAnswerEt = writtenQuestionView.findViewById(R.id.written_answerEt);

        if (ActivityLearn.flashcardList.size() < MALLet.MIN_FLASHCARDS_FOR_TEST) {
            ViewUtils.hideItems(toolbarOptionsIv, nextEfab);
            ViewUtils.disableItems(toolbarOptionsIv, nextEfab);
            notEnoughDialog();
        } else {
            startTestDialog();
        }

        multipleChoiceMainLl = multipleChoiceQuestionView.findViewById(R.id.multipleChoice_mainLl);

        bgPrimaryStroke = ContextCompat.getDrawable(getActivity(), R.drawable.bg_white_primary_stroke);
        bgDarkStroke = ContextCompat.getDrawable(getActivity(), R.drawable.bg_white_dark_stroke);
    }

    private LinearLayout multipleChoiceMainLl;

    private ImageView toolbarBackIv, toolbarOptionsIv;

    private void setupToolbar() {
        Toolbar toolbar = binding.testToolbar;
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("");

        toolbarBackIv = binding.testToolbarBackIv;
        toolbarBackIv.setOnClickListener(v -> getActivity().finish());

        toolbarOptionsIv = binding.testOptionsIv;
        toolbarOptionsIv.setOnClickListener(v -> startTestDialog());
    }

    private void startTestDialog() {
        Dialog dialog = ViewUtils.createDialog(requireContext(), R.layout.dialog_test_are_you_ready, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT), Gravity.BOTTOM);
        DialogTestAreYouReadyBinding dialogBinding = DialogTestAreYouReadyBinding.inflate(getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());
        dialog.show();

        TextView startTv = dialogBinding.testReadyStartTv;
        TextView messageTv = dialogBinding.testReadyMessageTv;


        startTv.setOnClickListener(v -> {
            writtenQuestions = activityLearn.generateWrittenQuestions();
            multipleChoiceQuestions = ActivityLearn.generateMultipleChoiceQuestions();
            trueFalseQuestions = activityLearn.generateTrueFalseQuestions();

            allQuestions = Math.min(30, writtenQuestions.size() + multipleChoiceQuestions.size() + trueFalseQuestions.size());

            displayWrittenQuestion(writtenQuestions, questionsLl, getLayoutInflater());

            currentQuestionIndex = 0;
            dialog.dismiss();
            stopWatch.start();
        });

        messageTv.setText(getActivity().getResources().getString(R.string.you_are_about_to_take_a_test));
    }

    private void notEnoughDialog() {
        Dialog dialog = ViewUtils.createDialog(requireContext(), R.layout.dialog_test_not_enough, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT), Gravity.BOTTOM);
        DialogTestNotEnoughBinding dialogBinding = DialogTestNotEnoughBinding.inflate(getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());
        dialog.show();

        TextView leaveTv = dialogBinding.notEnoughLeaveTv;

        leaveTv.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.setOnDismissListener(dialog1 -> {
            getActivity().finish();
        });
    }

    private int allQuestions;

    private void nextQuestion() {
        if (currentQuestionIndex < 29) {
            if (currentQuestionIndex < 9) {
                writtenAnswer = writtenAnswerEt.getText().toString().toLowerCase().trim();
                ModelWritten writtenQuestion = writtenQuestions.get(currentQuestionIndex);
                String writtenCorrectAnswer = writtenQuestion.getCorrectAnswer();

                boolean isCorrect = checkWrittenAnswer(writtenAnswer, writtenCorrectAnswer);

                if (isCorrect) {
                    points++;

                    writtenAnswerEt.setText("");
                    currentQuestionIndex++;

                    if (currentQuestionIndex < writtenQuestions.size()) {
                        displayWrittenQuestion(writtenQuestions, this.questionsLl, getLayoutInflater());
                    } else {
                        testFinishedDialog();
                    }
                } else {
                    writtenAnswerEt.setText("");
                    currentQuestionIndex++;

                    if (currentQuestionIndex < writtenQuestions.size()) {
                        displayWrittenQuestion(writtenQuestions, this.questionsLl, getLayoutInflater());
                    } else {
                        testFinishedDialog();
                    }
                }
            } else if (currentQuestionIndex < 19) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                questionsLl.setGravity(Gravity.NO_GRAVITY);

                layoutParams.gravity = Gravity.NO_GRAVITY;

                multipleChoiceMainLl.setLayoutParams(layoutParams);

                boolean isCorrect = checkMultipleChoiceAnswer(multipleChoiceClickedPosition, correctMultipleChoiceAnswerPosition);

                if (isCorrect) {
                    points++;
                    currentQuestionIndex++;

                    if (currentQuestionIndex - 10 < multipleChoiceQuestions.size()) {
                        displayMultipleChoiceQuestion(multipleChoiceQuestions, questionsLl, getLayoutInflater());
                    } else {
                        testFinishedDialog();
                    }
                } else {
                    currentQuestionIndex++;

                    if (currentQuestionIndex - 10 < multipleChoiceQuestions.size()) {
                        displayMultipleChoiceQuestion(multipleChoiceQuestions, questionsLl, getLayoutInflater());
                    }
                }
            } else if (currentQuestionIndex < 30) {

                boolean isTrueFalseCorrect = checkTrueFalseAnswer(trueFalseClicked, trueFalseCorrectAnswer);

                if (isTrueFalseCorrect) {
                    points++;
                    currentQuestionIndex++;

                    if (currentQuestionIndex - 20 < trueFalseQuestions.size()) {
                        displayTrueFalseQuestion(trueFalseQuestions, questionsLl, getLayoutInflater());
                    }
                } else {
                    currentQuestionIndex++;

                    if (currentQuestionIndex - 20 < trueFalseQuestions.size()) {
                        displayTrueFalseQuestion(trueFalseQuestions, questionsLl, getLayoutInflater());
                    }
                }
            }
        } else {
            testFinishedDialog();
        }

    }

    private int points = 0;

    private boolean checkWrittenAnswer(String userAnswer, String correctAnswer) {
        String userInputLower = userAnswer.toLowerCase();
        String correctAnswerLower = correctAnswer.toLowerCase();
        return userInputLower.equals(correctAnswerLower);
    }

    private void displayWrittenQuestion(List<ModelWritten> wQuestions, LinearLayout
            ll, LayoutInflater inflater) {
        ll.removeAllViews();

        if (currentQuestionIndex < wQuestions.size()) {
            ModelWritten writtenQuestion = wQuestions.get(currentQuestionIndex);

            View writtenQuestionItem = inflater.inflate(R.layout.model_written, ll, false);
            writtenQuestionTv = writtenQuestionItem.findViewById(R.id.written_questionTv);
            writtenQuestionTv.setText(writtenQuestion.getQuestion());

            correctAnswersTv = writtenQuestionItem.findViewById(R.id.written_correctAnswersTv);
            correctAnswersTv.setText("\"" + writtenQuestion.getCorrectAnswer() + "\"");

            writtenAnswerEt = writtenQuestionItem.findViewById(R.id.written_answerEt);
            answersLl = writtenQuestionItem.findViewById(R.id.written_correctAnswersLl);
            ViewUtils.hideItems(answersLl);

            writtenAnswerEt.setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus) {
                    writtenAnswer = writtenAnswerEt.getText().toString();
                }
            });

            ll.addView(writtenQuestionItem);
        } else {
            // All questions have been shown
        }
    }

    private TextView trueTv, falseTv;

    private void displayTrueFalseQuestion(List<ModelTrueFalse> questions, LinearLayout ll, LayoutInflater inflater) {
        ll.removeAllViews();

        //if (currentQuestionIndex < questions.size()) {
        ModelTrueFalse trueFalseQuestion = questions.get(currentQuestionIndex - 20);

        View trueFalseQuestionItem = inflater.inflate(R.layout.model_true_false, ll, false);

        TextView trueFalseQuestionTv = trueFalseQuestionItem.findViewById(R.id.trueFalse_questionTv);
        trueFalseQuestionTv.setText(trueFalseQuestion.getQuestion());

        TextView trueFalseAnswerTv = trueFalseQuestionItem.findViewById(R.id.trueFalse_answerTv);
        trueFalseAnswerTv.setText(trueFalseQuestion.getDisplayedAnswer());

        trueFalseCorrectAnswer = trueFalseQuestion.getCorrectAnswer();

        trueTv = trueFalseQuestionItem.findViewById(R.id.trueFalse_trueTv);
        falseTv = trueFalseQuestionItem.findViewById(R.id.trueFalse_falseTv);

        Typeface remBold = Typeface.createFromAsset(requireContext().getAssets(), "rem_bold.ttf");
        Typeface remRegular = Typeface.createFromAsset(requireContext().getAssets(), "rem_regular.ttf");

        trueTv.setOnClickListener(v -> {
            trueFalseClicked = 1;
            //checkTrueFalseAnswer(trueFalseClicked, trueFalseCorrectAnswer);

            trueTv.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
            trueTv.setTypeface(remBold);
            trueTv.getCompoundDrawables()[0].setTint(getActivity().getResources().getColor(R.color.colorPrimaryDark));
            trueTv.setBackground(getActivity().getDrawable(R.drawable.bg_white_dark_stroke));

            falseTv.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            falseTv.setTypeface(remRegular);
            falseTv.getCompoundDrawables()[0].setTint(getActivity().getResources().getColor(R.color.colorPrimary));
            falseTv.setBackground(getActivity().getDrawable(R.drawable.bg_white_primary_stroke));
        });

        falseTv.setOnClickListener(v -> {
            trueFalseClicked = 2;
           // checkTrueFalseAnswer(trueFalseClicked, trueFalseCorrectAnswer);

            trueTv.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            trueTv.setTypeface(remRegular);
            trueTv.getCompoundDrawables()[0].setTint(getActivity().getResources().getColor(R.color.colorPrimary));
            trueTv.setBackground(getActivity().getDrawable(R.drawable.bg_white_primary_stroke));

            falseTv.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
            falseTv.setTypeface(remBold);
            falseTv.getCompoundDrawables()[0].setTint(getActivity().getResources().getColor(R.color.colorPrimaryDark));
            falseTv.setBackground(getActivity().getDrawable(R.drawable.bg_white_dark_stroke));
        });

        ll.addView(trueFalseQuestionItem);
        //} else {
        // All questions have been shown
        //}
    }

    private int trueFalseClicked;
    private boolean trueFalseCorrectAnswer;

    private boolean checkTrueFalseAnswer(int clickedAnswer, boolean correctAnswer) {
        if (correctAnswer && clickedAnswer == 1) {
            trueFalseClicked = 0;
            return true;
        } else if (!correctAnswer && clickedAnswer == 2) {
            trueFalseClicked = 0;
            return true;
        } else if (correctAnswer && clickedAnswer == 2) {
            trueFalseClicked = 0;
            return false;
        } else {
            trueFalseClicked = 0;
            return false;
        }
    }

    private TextView writtenAnswerTv;
    private int correctMultipleChoiceAnswerPosition;
    private TextView option1Tv, option2Tv, option3Tv, option4Tv;
    private Drawable bgPrimaryStroke;// = ContextCompat.getDrawable(getActivity(),R.drawable.bg_white_primary_stroke);
    private Drawable bgDarkStroke;// = ContextCompat.getDrawable(getActivity(),R.drawable.bg_white_dark_stroke);

    private void displayMultipleChoiceQuestion
            (List<ModelSingleChoice> questions, LinearLayout ll, LayoutInflater inflater) {
        ll.removeAllViews();

        //if (currentQuestionIndex < questions.size()) {
        ModelSingleChoice multipleChoiceQuestion = questions.get(currentQuestionIndex - 10);

        View multipleChoiceQuestionItem = inflater.inflate(R.layout.model_multiple_choice, ll, false);
        multipleChoiceQuestionTv = multipleChoiceQuestionItem.findViewById(R.id.multipleChoice_questionTv);

        option1Tv = multipleChoiceQuestionItem.findViewById(R.id.multipleChoice_option1Tv);
        option2Tv = multipleChoiceQuestionItem.findViewById(R.id.multipleChoice_option2Tv);
        option3Tv = multipleChoiceQuestionItem.findViewById(R.id.multipleChoice_option3Tv);
        option4Tv = multipleChoiceQuestionItem.findViewById(R.id.multipleChoice_option4Tv);

        multipleChoiceQuestionTv.setText(multipleChoiceQuestion.getQuestion());

        List<ModelAnswer> answers = multipleChoiceQuestion.getAnswers().stream()
                .collect(Collectors.toList());
        ModelAnswer correctAnswer = answers.stream()
                .filter(ModelAnswer::isCorrect)
                .findAny().get();

        correctMultipleChoiceAnswerPosition = answers.indexOf(correctAnswer);

        option1Tv.setText(answers.get(0).getAnswer());
        option2Tv.setText(answers.get(1).getAnswer());
        option3Tv.setText(answers.get(2).getAnswer());
        option4Tv.setText(answers.get(3).getAnswer());

        // System.out.println(correctMultipleChoiceAnswerPosition);

        Typeface remBold = Typeface.createFromAsset(requireContext().getAssets(), "rem_bold.ttf");
        Typeface remRegular = Typeface.createFromAsset(requireContext().getAssets(), "rem_regular.ttf");

        option1Tv.setOnClickListener(v -> {
            multipleChoiceClickedPosition = 0;

            option1Tv.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
            option1Tv.setTypeface(remBold);
            option1Tv.getCompoundDrawables()[0].setTint(getActivity().getResources().getColor(R.color.colorPrimaryDark));
            option1Tv.setBackground(bgDarkStroke);

            option2Tv.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            option2Tv.setTypeface(remRegular);
            option2Tv.getCompoundDrawables()[0].setTint(getActivity().getResources().getColor(R.color.colorPrimary));
            option2Tv.setBackground(bgPrimaryStroke);

            option3Tv.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            option3Tv.setTypeface(remRegular);
            option3Tv.getCompoundDrawables()[0].setTint(getActivity().getResources().getColor(R.color.colorPrimary));
            option3Tv.setBackground(bgPrimaryStroke);

            option4Tv.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            option4Tv.setTypeface(remRegular);
            option4Tv.getCompoundDrawables()[0].setTint(getActivity().getResources().getColor(R.color.colorPrimary));
            option4Tv.setBackground(bgPrimaryStroke);
        });

        option2Tv.setOnClickListener(v -> {
            multipleChoiceClickedPosition = 1;

            option1Tv.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            option1Tv.setTypeface(remRegular);
            option1Tv.getCompoundDrawables()[0].setTint(getActivity().getResources().getColor(R.color.colorPrimary));
            option1Tv.setBackground(bgPrimaryStroke);

            option2Tv.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
            option2Tv.setTypeface(remBold);
            option2Tv.getCompoundDrawables()[0].setTint(getActivity().getResources().getColor(R.color.colorPrimaryDark));
            option2Tv.setBackground(bgDarkStroke);

            option3Tv.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            option3Tv.setTypeface(remRegular);
            option3Tv.getCompoundDrawables()[0].setTint(getActivity().getResources().getColor(R.color.colorPrimary));
            option3Tv.setBackground(bgPrimaryStroke);

            option4Tv.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            option4Tv.setTypeface(remRegular);
            option4Tv.getCompoundDrawables()[0].setTint(getActivity().getResources().getColor(R.color.colorPrimary));
            option4Tv.setBackground(bgPrimaryStroke);
        });

        option3Tv.setOnClickListener(v -> {
            multipleChoiceClickedPosition = 2;

            option1Tv.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            option1Tv.setTypeface(remRegular);
            option1Tv.getCompoundDrawables()[0].setTint(getActivity().getResources().getColor(R.color.colorPrimary));
            option1Tv.setBackground(bgPrimaryStroke);

            option2Tv.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            option2Tv.setTypeface(remRegular);
            option2Tv.getCompoundDrawables()[0].setTint(getActivity().getResources().getColor(R.color.colorPrimary));
            option2Tv.setBackground(bgPrimaryStroke);

            option3Tv.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
            option3Tv.setTypeface(remBold);
            option3Tv.getCompoundDrawables()[0].setTint(getActivity().getResources().getColor(R.color.colorPrimaryDark));
            option3Tv.setBackground(bgDarkStroke);

            option4Tv.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            option4Tv.setTypeface(remRegular);
            option4Tv.getCompoundDrawables()[0].setTint(getActivity().getResources().getColor(R.color.colorPrimary));
            option4Tv.setBackground(bgPrimaryStroke);
        });

        option4Tv.setOnClickListener(v -> {
            multipleChoiceClickedPosition = 3;

            option1Tv.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            option1Tv.setTypeface(remRegular);
            option1Tv.getCompoundDrawables()[0].setTint(getActivity().getResources().getColor(R.color.colorPrimary));
            option1Tv.setBackground(bgPrimaryStroke);

            option2Tv.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            option2Tv.setTypeface(remRegular);
            option2Tv.getCompoundDrawables()[0].setTint(getActivity().getResources().getColor(R.color.colorPrimary));
            option2Tv.setBackground(bgPrimaryStroke);

            option3Tv.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            option3Tv.setTypeface(remRegular);
            option3Tv.getCompoundDrawables()[0].setTint(getActivity().getResources().getColor(R.color.colorPrimary));
            option3Tv.setBackground(bgPrimaryStroke);

            option4Tv.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
            option4Tv.setTypeface(remBold);
            option4Tv.getCompoundDrawables()[0].setTint(getActivity().getResources().getColor(R.color.colorPrimaryDark));
            option4Tv.setBackground(bgDarkStroke);
        });


        ll.addView(multipleChoiceQuestionItem);
    }

    private int multipleChoiceClickedPosition=4;

    private boolean checkMultipleChoiceAnswer(int clickedOption, int correctAnswerPosition) {
        boolean isCorrect = clickedOption == correctAnswerPosition;

        multipleChoiceClickedPosition = 4;

        return isCorrect;
    }

    private static SharedPreferences sharedPreferences;
    private int lastTestScore;

    private void testFinishedDialog() {
        Dialog dialog = ViewUtils.createDialog(requireContext(), R.layout.dialog_test_finished, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT), Gravity.BOTTOM);
        DialogTestFinishedBinding dialogBinding = DialogTestFinishedBinding.inflate(getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());
        dialog.show();
        stopWatch.stop();

        TextView scoreTimeTv = dialogBinding.testFinishedScoreTimeTv;
        scoreTimeTv.setText(getActivity().getString(R.string.you_finished_and_scored, formatTime(stopWatch.getTime()), String.valueOf(points)));

        TextView motivationalMessageTv = dialogBinding.testFinishedMessageTv;
        motivationalMessageTv.setText(motivationalMessage());
        lastTestScore = points;
        sharedPreferences.edit().putInt("lastTestScore", lastTestScore).apply();

        TextView dialogFinishTv = dialogBinding.testFinishedFinishTv;
        TextView dialogRestartTv = dialogBinding.testFinishedRestartTv;

        dialogFinishTv.setOnClickListener(v -> {
            dialog.dismiss();
            requireActivity().finish();
        });

        dialogRestartTv.setOnClickListener(v -> {
            currentQuestionIndex = 0;
            points = 0;

            writtenQuestions = activityLearn.generateWrittenQuestions();
            multipleChoiceQuestions = ActivityLearn.generateMultipleChoiceQuestions();
            trueFalseQuestions = activityLearn.generateTrueFalseQuestions();

            dialog.dismiss();

            stopWatch.reset();

            startTestDialog();
        });
    }

    private String motivationalMessage() {
        String motivationalMessage;
        if (points >= 0 && points < 5) {
            motivationalMessage = getActivity().getResources().getString(R.string.motivational_message05);
        } else if (points >= 5 && points < 10) {
            motivationalMessage = getActivity().getResources().getString(R.string.motivational_message510);
        } else if (points >= 10 && points < 15) {
            motivationalMessage = getActivity().getResources().getString(R.string.motivational_message1015);
        } else if (points >= 15 && points < 20) {
            motivationalMessage = getActivity().getResources().getString(R.string.motivational_message1520);
        } else if (points >= 20 && points < 25) {
            motivationalMessage = getActivity().getResources().getString(R.string.motivational_message2025);
        } else if (points >= 25 && points < 30) {
            motivationalMessage = getActivity().getResources().getString(R.string.motivational_message2530);
        } else {
            motivationalMessage = getActivity().getResources().getString(R.string.motivational_message30);
        }

        return motivationalMessage;
    }

    private String formatTime(long elapsedTime) {
        long seconds = elapsedTime / 1000;
        long minutes = seconds / 60;
        seconds %= 60;

        if (minutes > 0) {
            return getActivity().getResources().getString(R.string.time_minutes_seconds, String.valueOf(minutes), String.valueOf(seconds));
        } else {
            return getActivity().getResources().getString(R.string.time_seconds, String.valueOf(seconds));
        }
    }

}