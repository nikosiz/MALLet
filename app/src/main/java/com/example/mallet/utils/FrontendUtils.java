package com.example.mallet.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mallet.R;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static Dialog createDialog(Context context, String layoutName) {
        // Get the layout resource ID based on the layoutName
        int layoutResourceId = context.getResources().getIdentifier(layoutName, "layout", context.getPackageName());

        // Check if the layout resource ID is valid
        if (layoutResourceId == 0) {
            // Handle the case where the layout does not exist
            return null;
        }

        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Inflate the layout using the obtained layout resource ID
        View dialogView = LayoutInflater.from(context).inflate(layoutResourceId, null);

        // Set the content view of the dialog
        dialog.setContentView(dialogView);

        // Customize the dialog's appearance
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        return dialog;
    }

    public static void showDialog(Dialog dialog) {
        if (dialog != null) {
            // Show the dialog
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

    public static void showProgressBar(Activity activity) {
        // Inflate the layout containing the progress bar
        View progressBarView = LayoutInflater.from(activity).inflate(R.layout.progress_bar, null);

        // Add the progress bar to the activity's layout
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        );
        activity.addContentView(progressBarView, layoutParams);

        // Set the progress bar visibility to VISIBLE
        progressBarView.setVisibility(View.VISIBLE);
    }

    public static void hideProgressBar(Activity activity) {
        // Inflate the layout containing the progress bar
        View progressBarView = LayoutInflater.from(activity).inflate(R.layout.progress_bar, null);

        // Add the progress bar to the activity's layout
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        );
        activity.addContentView(progressBarView, layoutParams);

        // Set the progress bar visibility to VISIBLE
        progressBarView.setVisibility(View.GONE);
    }

    public static boolean emailPatternCheck(String email, TextView errorTV) {
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorTV.setVisibility(View.GONE);
            return true;
        } else {
            errorTV.setVisibility(View.VISIBLE);
            return false;
        }
    }

    public static boolean passwordPatternCheck(String password, TextView errorTV) {
        Pattern p = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$");

        if(p.matcher(password).matches()){
            errorTV.setVisibility(View.GONE);
            return true;
        } else {
            errorTV.setVisibility(View.VISIBLE);
            return false;
        }
    }

}
