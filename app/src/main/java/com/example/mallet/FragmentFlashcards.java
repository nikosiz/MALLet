package com.example.mallet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DiffUtil;

import com.example.mallet.databinding.FragmentFlashcardsBinding;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.Duration;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FragmentFlashcards extends Fragment {

    private FragmentFlashcardsBinding binding;
    private static final String TAG = "LearnFragment";
    private CardStackLayoutManager manager;
    private AdapterFlashcardStack adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFlashcardsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        setupCardStackView();
        setupSwipeButtons();
        setupToolbar();
        return view;
    }

    private void setupToolbar() {
        Toolbar toolbar = binding.flashcardsToolbar;
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setTitle(""); // Set the title to an empty string

        // Display back arrow on the toolbar
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void setupCardStackView() {
        manager = new CardStackLayoutManager(requireContext(), new CardStackListener() {
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

                if (manager.getTopPosition() == adapter.getItemCount() - 5) {
                    paginate();
                }
            }

            private void paginate() {
                List<ModelFlashcard> oldItem = adapter.getItems();
                List<ModelFlashcard> newItem = new ArrayList<>(addList());
                CallbackFlashcardStack callback = new CallbackFlashcardStack(oldItem, newItem);
                DiffUtil.DiffResult results = DiffUtil.calculateDiff(callback);
                adapter.setItems(newItem);
                results.dispatchUpdatesTo(adapter);
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
                TextView tv = view.findViewById(R.id.flashcard_word);
                // Log.d(TAG, "onCardAppeared: " + position + ", word: " + tv.getText());
            }

            @Override
            public void onCardDisappeared(View view, int position) {
                TextView tv = view.findViewById(R.id.flashcard_word);
                // Log.d(TAG, "onCardDisappeared: " + position + ", word: " + tv.getText());
            }
        });

        manager.setStackFrom(StackFrom.Bottom);
        manager.setVisibleCount(3);
        manager.setTranslationInterval(4.0f);
        manager.setScaleInterval(0.95f);
        manager.setSwipeThreshold(0.3f);
        manager.setMaxDegree(0.0f);
        manager.setDirections(Direction.HORIZONTAL);
        manager.setCanScrollHorizontal(true);
        manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual);
        manager.setOverlayInterpolator(new LinearInterpolator());
        manager.setCanScrollVertical(false);

        adapter = new AdapterFlashcardStack(addList());
        binding.cardStackView.setLayoutManager(manager);
        binding.cardStackView.setAdapter(adapter);
        binding.cardStackView.setItemAnimator(new DefaultItemAnimator());
    }

    private void setupSwipeButtons() {
        binding.learnSwipeLeftBtn.setOnClickListener(v -> performSwipe(Direction.Left));

        binding.learnSwipeRightBtn.setOnClickListener(v -> performSwipe(Direction.Right));

        binding.learnUndoBtn.setOnClickListener(v -> undoSwipe());
    }

    private void performSwipe(Direction direction) {
        int currentPosition = manager.getTopPosition();
        if (currentPosition >= 0 && currentPosition < adapter.getItemCount()) {
            if (direction == Direction.Left) {
                manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual);
                manager.setSwipeAnimationSetting(new SwipeAnimationSetting.Builder()
                        .setDirection(Direction.Left)
                        .setDuration(Duration.Normal.duration)
                        .build());
            } else if (direction == Direction.Right) {
                manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual);
                manager.setSwipeAnimationSetting(new SwipeAnimationSetting.Builder()
                        .setDirection(Direction.Right)
                        .setDuration(Duration.Normal.duration)
                        .build());
            }

            binding.cardStackView.swipe();
        } else {
            Toast.makeText(requireContext(), "No more cards to swipe", Toast.LENGTH_SHORT).show();
        }
    }

    private void undoSwipe() {
        int currentPosition = manager.getTopPosition();
        if (currentPosition > 0) { // Check if there are cards to rewind
            binding.cardStackView.rewind();
        } else {
            Toast.makeText(requireContext(), "No more cards to undo", Toast.LENGTH_SHORT).show();
        }
    }

    private List<ModelFlashcard> addList() {
        List<ModelFlashcard> items = new ArrayList<>();
        items.add(new ModelFlashcard("Apple", "A red fruit", "Jabłko"));
        items.add(new ModelFlashcard("Orange", "An orange fruit", "Pomarańcza"));
        items.add(new ModelFlashcard("Pear", "A round yellow fruit", "Gruszka"));
        items.add(new ModelFlashcard("Banana", "A long, curved yellow fruit", "Banan"));
        items.add(new ModelFlashcard("Strawberry", "A small red fruit", "Truskawka"));
        return items;
    }
}
