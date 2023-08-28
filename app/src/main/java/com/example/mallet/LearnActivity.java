package com.example.mallet;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DiffUtil;

import com.example.mallet.databinding.ActivityLearnBinding;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.Duration;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import java.util.ArrayList;
import java.util.List;

public class LearnActivity extends AppCompatActivity {

    private ActivityLearnBinding binding;
    private static final String TAG = "LearnActivity";
    private CardStackLayoutManager manager;
    private FlashcardStackAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLearnBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupCardStackView();
        setupSwipeButtons();
    }

    private void setupCardStackView() {
        manager = new CardStackLayoutManager(this, new CardStackListener() {
            @Override
            public void onCardDragging(Direction direction, float ratio) {
                Log.d(TAG, "onCardDragging: d = " + direction.name() + " ratio = " + ratio);
            }

            @Override
            public void onCardSwiped(Direction direction) {
                Log.d(TAG, "onCardSwiped: d = " + manager.getTopPosition() + " d = " + direction);

                if (direction == Direction.Left) {
                    Toast.makeText(LearnActivity.this, "Left", Toast.LENGTH_SHORT).show();
                } else if (direction == Direction.Right) {
                    Toast.makeText(LearnActivity.this, "Right", Toast.LENGTH_SHORT).show();
                }

                if (manager.getTopPosition() == adapter.getItemCount() - 5) {
                    paginate();
                }
            }

            private void paginate() {
                List<FlashcardModel> oldItem = adapter.getItems();
                List<FlashcardModel> newItem = new ArrayList<>(addList());
                FlashcardStackCallback callback = new FlashcardStackCallback(oldItem, newItem);
                DiffUtil.DiffResult results = DiffUtil.calculateDiff(callback);
                adapter.setItems(newItem);
                results.dispatchUpdatesTo(adapter);
            }

            @Override
            public void onCardRewound() {
                Log.d(TAG, "onCardRewound: p = " + manager.getTopPosition());
                Toast.makeText(LearnActivity.this, "REWIND", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCardCanceled() {
                Log.d(TAG, "onCardRewound: p = " + manager.getTopPosition());
            }

            @Override
            public void onCardAppeared(View view, int position) {
                TextView tv = view.findViewById(R.id.item_word);
                Log.d(TAG, "onCardAppeared: " + position + ", word: " + tv.getText());
            }

            @Override
            public void onCardDisappeared(View view, int position) {
                TextView tv = view.findViewById(R.id.item_word);
                Log.d(TAG, "onCardDisappeared: " + position + ", word: " + tv.getText());
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

        adapter = new FlashcardStackAdapter(addList());
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
            Toast.makeText(this, "No more cards to swipe", Toast.LENGTH_SHORT).show();
        }
    }

    private void undoSwipe() {
        int currentPosition = manager.getTopPosition();
        if (currentPosition > 0) { // Check if there are cards to rewind
            binding.cardStackView.rewind();
        } else {
            Toast.makeText(this, "No more cards to undo", Toast.LENGTH_SHORT).show();
        }
    }

    private List<FlashcardModel> addList() {
        List<FlashcardModel> items = new ArrayList<>();
        items.add(new FlashcardModel("Apple", "A red fruit", "Jabłko"));
        items.add(new FlashcardModel("Orange", "An orange fruit", "Pomarańcza"));
        items.add(new FlashcardModel("Pear", "A round yellow fruit", "Gruszka"));
        items.add(new FlashcardModel("Banana", "A long, curved yellow fruit", "Banan"));
        items.add(new FlashcardModel("Strawberry", "A small red fruit", "Truskawka"));
        return items;
    }
}
