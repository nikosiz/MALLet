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

import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import java.util.ArrayList;
import java.util.List;

public class LearnActivity extends AppCompatActivity {

    private static final String TAG = "LearnActivity";
    private CardStackLayoutManager manager;
    private CardStackAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);

        CardStackView cardStackView = findViewById(R.id.card_stack_view);
        manager = new CardStackLayoutManager(this, new CardStackListener() {
            @Override
            public void onCardDragging(Direction direction, float ratio) {
                Log.d(TAG, "onCardDragging: d = " + direction.name() + " ratio = " + ratio);
            }

            @Override
            public void onCardSwiped(Direction direction) {
                Log.d(TAG, "onCardSwiped: d = " + manager.getTopPosition() + " d = " + direction);

                /*if (direction == Direction.Top) {
                    Toast.makeText(LearnActivity.this, "Top", Toast.LENGTH_SHORT).show();
                }
                if (direction == Direction.Bottom) {
                    Toast.makeText(LearnActivity.this, "Bottom", Toast.LENGTH_SHORT).show();
                }*/
                if (direction == Direction.Left) {
                    Toast.makeText(LearnActivity.this, "Left", Toast.LENGTH_SHORT).show();
                }
                if (direction == Direction.Right) {
                    Toast.makeText(LearnActivity.this, "Right", Toast.LENGTH_SHORT).show();
                }

                // Paginating
                if (manager.getTopPosition() == adapter.getItemCount() - 5) {
                    paginate();
                }
            }

            private void paginate() {
                List<ItemModel> oldItem = adapter.getItems();
                List<ItemModel> newItem = new ArrayList<>(addList());
                CardStackCallback callback = new CardStackCallback(oldItem, newItem);
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
                Log.d(TAG, "onCardAppeared: " + position + ", word: " + tv.getText());
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
        manager.setSwipeableMethod(SwipeableMethod.Manual);
        manager.setOverlayInterpolator(new LinearInterpolator());
        manager.setCanScrollVertical(false);
        adapter = new CardStackAdapter(addList());
        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(adapter);
        cardStackView.setItemAnimator(new DefaultItemAnimator());

    }

    private List<ItemModel> addList() {
        List<ItemModel> items = new ArrayList<>();
        items.add(new ItemModel("Apple", "A red fruit", "Jabłko"));
        items.add(new ItemModel("Orange", "An orange fruit", "Pomarańcza"));
        items.add(new ItemModel("Pear", "A round yellow fruit", "Gruszka"));
        items.add(new ItemModel("Banana", "A long, curved yellow fruit", "Banan"));
        items.add(new ItemModel("Strawberry", "A small red fruit", "Truskawka"));


        return items;
    }
}
