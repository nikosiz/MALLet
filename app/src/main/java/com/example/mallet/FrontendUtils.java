package com.example.mallet;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

public class FrontendUtils {

    public static void showItem(View view) {
        if (view != null) {
            view.setVisibility(View.VISIBLE);
        }
    }

    public static void hideitem(View view) {
        if (view != null) {
            view.setVisibility(View.GONE);
        }
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

}
