package com.example.mallet.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.mallet.ModelFlashcard;
import com.example.mallet.ModelFolder;
import com.example.mallet.ModelLearningSet;
import com.example.mallet.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class Utils {

    private static boolean backClickCounter = false;

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

    public static Dialog createDialog(Context context, int layoutResourceId) {
        if (layoutResourceId == 0) {
            return null;
        }

        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        View dialogView = LayoutInflater.from(context).inflate(layoutResourceId, null);

        dialog.setContentView(dialogView);

        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        return dialog;
    }


    public static void showDialog(Dialog dialog) {
        if (dialog != null) {
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

    public static void openActivity(Context context, Class<?> activityClass) {
        Intent intent = new Intent(context, activityClass);
        context.startActivity(intent);
    }

    public static void openActivityWithFragment(Context context, Class<? extends Fragment> fragmentClass, Class<?> targetActivityClass) {
        Intent intent = new Intent(context, targetActivityClass);
        intent.putExtra("fragment_class", fragmentClass.getName());
        context.startActivity(intent);
    }


    public static List<ModelFlashcard> readFlashcardsFromFile(Context context, String filePath) {
        List<ModelFlashcard> flashcards = new ArrayList<>();

        try {
            AssetManager assetManager = context.getAssets();
            InputStreamReader inputStreamReader = new InputStreamReader(assetManager.open(filePath));
            BufferedReader reader = new BufferedReader(inputStreamReader);

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 3) {
                    String term = parts[0].trim();
                    String definition = parts[1].trim();
                    String translation = parts[2].trim();
                    flashcards.add(new ModelFlashcard(term, definition, translation));
                }
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return flashcards;
    }


    public static List<ModelFlashcard> createFlashcardList(ModelLearningSet learningSet) {
        if (learningSet != null) {
            return learningSet.getTerms(); // Return the flashcards from the learning set
        }
        return new ArrayList<>(); // Return an empty list if learningSet is null
    }

    public void createFolder(ModelFolder folder) {
        if (folder != null) {
            folder.getFolderName();
            folder.getFolderCreator();
        }
    }


    // This stays, what is up, needs to be reviewed

    public static void setupAnimation(Context context, TextView logo) {
        Animation pulseAnimation = AnimationUtils.loadAnimation(context, R.anim.pulse_anim);
        logo.startAnimation(pulseAnimation);
    }

    public static void setupTextWatcher(EditText et, TextView err, Pattern p, String errorMsg) {
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = s.toString().trim(); // Get the current input text
                if (input.isEmpty()) {
                    Utils.showItem(err);
                    err.setText("This field cannot be empty");
                } else if (input.contains(" ")) {
                    Utils.showItem(err);
                    err.setText("Check your input for spaces");
                } else if (!p.matcher(input).matches()) {
                    Utils.showItem(err);
                    err.setText(errorMsg);
                } else {
                    Utils.hideItem(err); // Hide the error when input is valid
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public static void validateInput(EditText et, TextView errorTv, Pattern pattern, String invalidInputMsg) {
        String input = et.getText().toString().trim();

        if (input.isEmpty()) {
            Utils.showItem(errorTv);
            errorTv.setText("This field cannot be empty");
        } else if (input.contains(" ")) {
            Utils.showItem(errorTv);
            errorTv.setText("Check your input for spaces");
        } else if (!pattern.matcher(input).matches()) {
            Utils.showItem(errorTv);
            errorTv.setText(invalidInputMsg);
        } else {
            Utils.hideItem(errorTv); // Hide the error when input is valid
        }
    }

    public static boolean isErrorVisible(TextView errorTv) {
        return errorTv.getVisibility() == View.VISIBLE;
    }

    public static void hideItems(View... elements) {
        for (View element : elements) {
            element.setVisibility(View.GONE);
        }
    }

    public static void showItems(View... elements) {
        for (View element : elements) {
            element.setVisibility(View.VISIBLE);
        }
    }

    public static void openEmailClient(Context context) {
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_APP_EMAIL);
            context.startActivity(intent);
        } catch (android.content.ActivityNotFoundException e) {
            showToast(context, "There is no email client installed.");
        }
    }

    public static void terminateApp(Activity activity) {
        if (backClickCounter) {
            activity.finishAffinity();
            System.exit(0);
            return;
        }

        backClickCounter = true;
        Toast.makeText(activity, "Press back again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> backClickCounter = false, 2000); // Reset the flag after 2 seconds
    }

    public static void resetEditText(EditText et, TextView err) {
        et.setText("");
        et.clearFocus();
        hideItem(err);
    }

}