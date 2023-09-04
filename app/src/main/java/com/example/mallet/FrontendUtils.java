package com.example.mallet;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Map;
import java.util.Objects;

public class FrontendUtils {

    public static void showItem(View view) {
        if (view != null) {
            view.setVisibility(View.VISIBLE);
        }
    }

    public static void hideItem(View view) {
        if (view != null) {
            view.setVisibility(View.GONE);
        }
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showDialog(Dialog dialog) {
        if (dialog != null) {
            Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setGravity(Gravity.BOTTOM);
            dialog.show();
        }
    }

    public static void applySwipeTransformer(View page, float position) {
        float absPosition = Math.abs(position);
        float MIN_Y_SCALE = 0.9f;
        float MIN_X_SCALE = 0.9f;
        float MIN_ALPHA = 0.9f;

        // Center element (scale it fully)
        if (absPosition < 1) {
            page.setScaleY(Math.max(MIN_Y_SCALE, 1 - absPosition));
            page.setScaleX(Math.max(MIN_X_SCALE, 1 - absPosition));
            page.setAlpha(Math.max(MIN_ALPHA, 1 - absPosition));
        }
        // Elements to the left
        else if (position < 0) {
            page.setScaleY(MIN_Y_SCALE);
            page.setScaleX(MIN_X_SCALE);
            page.setAlpha(MIN_ALPHA);
        }
        // Elements to the right
        else {
            page.setScaleY(MIN_Y_SCALE);
            page.setScaleX(MIN_X_SCALE);
            page.setAlpha(MIN_ALPHA);
        }
    }

    public static void passDataToActivity(Intent intent, Map<String, Object> dataMap) {
        for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof String) {
                intent.putExtra(key, (String) value);
            } else if (value instanceof Integer) {
                intent.putExtra(key, (int) value);
            } else if (value instanceof Boolean) {
                intent.putExtra(key, (boolean) value);
            } else if (value instanceof Float) {
                intent.putExtra(key, (float) value);
            } // Add more data types as needed
        }
    }

}
