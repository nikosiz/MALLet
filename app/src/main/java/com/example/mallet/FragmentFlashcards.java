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
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DiffUtil;

import com.example.mallet.databinding.DialogConfirmExitBinding;
import com.example.mallet.databinding.DialogFlashcardOptionsBinding;
import com.example.mallet.databinding.FragmentFlashcardsBinding;
import com.example.mallet.utils.AdapterFlashcardStack;
import com.example.mallet.utils.CallbackFlashcardStack;
import com.example.mallet.utils.ModelFlashcard;
import com.example.mallet.utils.ModelLearningSet;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.Duration;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class FragmentFlashcards extends Fragment {
    private static final String TAG = "LearnFragment";
    private FragmentFlashcardsBinding binding;
    private CardStackLayoutManager cardStackManager;
    private AdapterFlashcardStack adapterFlashcardStack;
    private ImageView swipeLeftIv, undoIv, swipeRightIv;
    private List<ModelFlashcard> flashcards;
    private List<ModelFlashcard> originalFlashcards;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFlashcardsBinding.inflate(inflater, container, false);

        setupContents();

        setupCardStackView();

        return binding.getRoot();
    }

    private void setupContents() {
        originalFlashcards = getLearningSetData();

        setupToolbar();

        swipeLeftIv = binding.flashcardsSwipeLeftIv;
        swipeLeftIv.setOnClickListener(v -> performSwipe(Direction.Left));

        undoIv = binding.flashcardsUndoIv;
        undoIv.setOnClickListener(v -> undoSwipe());

        swipeRightIv = binding.flashcardsSwipeRightIv;
        swipeRightIv.setOnClickListener(v -> performSwipe(Direction.Right));
    }

    private void setupToolbar() {
        Toolbar toolbar = binding.flashcardsToolbar;
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("");

        ImageView toolbarBackIv = binding.flashcardsToolbarBackIv;
        toolbarBackIv.setOnClickListener(v -> confirmExitDialog());

        ImageView toolbarOptionsIv = binding.flashcardsToolbarOptionsIv;
        toolbarOptionsIv.setOnClickListener(v -> flashcardsOptionsDialog());
    }

    private void flashcardsOptionsDialog() {
        final Dialog dialog = createDialog(R.layout.dialog_flashcard_options);
        DialogFlashcardOptionsBinding dialogBinding = DialogFlashcardOptionsBinding.inflate(LayoutInflater.from(getContext()));
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        Objects.requireNonNull(dialog).setContentView(dialogBinding.getRoot());
        dialog.show();

        ImageView optionsBackIv = dialogBinding.flashcardsOptionsBackIv;
        optionsBackIv.setOnClickListener(v -> dialog.dismiss());

        TextView shuffleTv = dialogBinding.flashcardsOptionsShuffleTv;
        shuffleTv.setOnClickListener(v -> {
            shuffleFlashcards();
            dialog.dismiss();
        });

        TextView restartTv = dialogBinding.flashcardsOptionsRestartTv;
        restartTv.setOnClickListener(v -> {
            restartFlashcards();
            dialog.dismiss();
        });

        TextView optionsCancelTv = dialogBinding.flashcardsOptionsCancelTv;
        optionsCancelTv.setOnClickListener(v -> dialog.dismiss());
    }

    private void shuffleFlashcards() {
        Collections.shuffle(originalFlashcards);

        adapterFlashcardStack.setItems(originalFlashcards);
        adapterFlashcardStack.notifyDataSetChanged();
    }

    private void setupCardStackView() {
        cardStackManager = new CardStackLayoutManager(requireContext(), new CardStackListener() {
            @Override
            public void onCardDragging(Direction direction, float ratio) {
                // Handle card dragging
                // Log.d(TAG, "onCardDragging: d = " + direction.name() + " ratio = " + ratio);
            }

            @Override
            public void onCardSwiped(Direction direction) {
                // Handle card swiped
                // Log.d(TAG, "onCardSwiped: d = " + manager.getTopPosition() + " d = " + direction);

                if (direction == Direction.Left) {
                    Toast.makeText(requireContext(), "Left", Toast.LENGTH_SHORT).show();
                } else if (direction == Direction.Right) {
                    Toast.makeText(requireContext(), "Right", Toast.LENGTH_SHORT).show();
                }

                if (cardStackManager.getTopPosition() == adapterFlashcardStack.getItemCount() - 5) {
                    paginate();
                }
            }

            private void paginate() {
                List<ModelFlashcard> oldItem = adapterFlashcardStack.getItems();
                List<ModelFlashcard> newItem = new ArrayList<>(flashcards);
                CallbackFlashcardStack callback = new CallbackFlashcardStack(oldItem, newItem);
                DiffUtil.DiffResult results = DiffUtil.calculateDiff(callback);
                adapterFlashcardStack.setItems(newItem);
                results.dispatchUpdatesTo(adapterFlashcardStack);
            }

            @Override
            public void onCardRewound() {
                // Handle card rewound
                // Log.d(TAG, "onCardRewound: p = " + manager.getTopPosition());
                Toast.makeText(requireContext(), "REWIND", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCardCanceled() {
                // Handle card canceled
                // Log.d(TAG, "onCardRewound: p = " + manager.getTopPosition());
            }

            @Override
            public void onCardAppeared(View view, int position) {
                TextView termTv = view.findViewById(R.id.flashcard_termTv);
                // Log.d(TAG, "onCardAppeared: " + position + ", word: " + termTv.getText());
            }

            @Override
            public void onCardDisappeared(View view, int position) {
                TextView termTv = view.findViewById(R.id.flashcard_termTv);
                // Log.d(TAG, "onCardDisappeared: " + position + ", word: " + termTv.getText());
            }
        });

        cardStackManager.setStackFrom(StackFrom.Bottom);
        cardStackManager.setVisibleCount(3);
        cardStackManager.setTranslationInterval(4.0f);
        cardStackManager.setScaleInterval(0.95f);
        cardStackManager.setSwipeThreshold(0.3f);
        cardStackManager.setMaxDegree(0.0f);
        cardStackManager.setDirections(Direction.HORIZONTAL);
        cardStackManager.setCanScrollHorizontal(true);
        cardStackManager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual);
        cardStackManager.setOverlayInterpolator(new LinearInterpolator());
        cardStackManager.setCanScrollVertical(false);

        adapterFlashcardStack = new AdapterFlashcardStack(flashcards);
        binding.flashcardsCardStackCsv.setLayoutManager(cardStackManager);
        binding.flashcardsCardStackCsv.setAdapter(adapterFlashcardStack);
        binding.flashcardsCardStackCsv.setItemAnimator(new DefaultItemAnimator());
    }


    private void confirmExitDialog() {
        final Dialog dialog = createDialog(R.layout.dialog_confirm_exit);
        DialogConfirmExitBinding dialogBinding = DialogConfirmExitBinding.inflate(LayoutInflater.from(getContext()));
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Objects.requireNonNull(dialog).setContentView(dialogBinding.getRoot());
        dialog.show();

        dialogBinding.confirmExitCancelTv.setOnClickListener(v -> dialog.dismiss());
        dialogBinding.confirmExitConfirmTv.setOnClickListener(v -> {
            requireActivity().finish();
            dialog.dismiss();
        });
    }

    private Dialog createDialog(int layoutResId) {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layoutResId);

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        return dialog;
    }

    private void performSwipe(Direction direction) {
        int currentPosition = cardStackManager.getTopPosition();
        if (currentPosition >= 0 && currentPosition < adapterFlashcardStack.getItemCount()) {
            if (direction == Direction.Left) {
                cardStackManager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual);
                cardStackManager.setSwipeAnimationSetting(new SwipeAnimationSetting.Builder()
                        .setDirection(Direction.Left)
                        .setDuration(Duration.Normal.duration)
                        .build());
            } else if (direction == Direction.Right) {
                cardStackManager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual);
                cardStackManager.setSwipeAnimationSetting(new SwipeAnimationSetting.Builder()
                        .setDirection(Direction.Right)
                        .setDuration(Duration.Normal.duration)
                        .build());
            }

            binding.flashcardsCardStackCsv.swipe();
        } else {
            Toast.makeText(requireContext(), "No more cards to swipe", Toast.LENGTH_SHORT).show();
        }
    }

    private void undoSwipe() {
        int currentPosition = cardStackManager.getTopPosition();
        if (currentPosition > 0) { // Check if there are cards to rewind
            binding.flashcardsCardStackCsv.rewind();
        } else {
            Toast.makeText(requireContext(), "No more cards to undo", Toast.LENGTH_SHORT).show();
        }
    }

    private void restartFlashcards() {
        adapterFlashcardStack = new AdapterFlashcardStack(originalFlashcards);
        binding.flashcardsCardStackCsv.setAdapter(adapterFlashcardStack);
        cardStackManager.scrollToPosition(0); // Scroll to the first card
    }

    private List<ModelFlashcard> getLearningSetData() {
        Bundle args = getArguments();
        if (args != null) {
            ModelLearningSet learningSet = args.getParcelable("learningSet");
            if (learningSet != null) {
                String setName = learningSet.getName();
                String nrOfTerms = String.valueOf(learningSet.getNrOfTerms());
                flashcards = learningSet.getTerms();
                return flashcards;
            }
        }
        return new ArrayList<>(); // Return an empty list if data retrieval fails
    }
}