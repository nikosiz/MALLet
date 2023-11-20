package com.example.mallet.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example.mallet.R;
import com.google.android.material.textfield.TextInputEditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    private static boolean backClickCounter = false;

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
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
            return learningSet.getTerms();
        }
        return new ArrayList<>();
    }


    // This stays, what is up, needs to be reviewed
    public static void setupAnimation(Context context, TextView logo) {
        Animation pulseAnimation = AnimationUtils.loadAnimation(context, R.anim.pulse_anim);
        logo.startAnimation(pulseAnimation);
    }

    public static void setupSignupPasswordTextWatcher(TextInputEditText et, TextView errTv) {
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                hideItems(errTv);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = s.toString().trim();
                if (input.isEmpty()) {
                    showItems(errTv);
                    errTv.setText("This field cannot be empty.");
                } else if (input.contains(" ")) {
                    showItems(errTv);
                    errTv.setText("Check your input for spaces.");
                } else if (checkPasswordPattern(input)) {
                    showItems(errTv);
                    errTv.setText("Password must consist of a minimum of 8 characters: a combination of lowercase and uppercase letters, numbers, and special symbols.");
                } else {
                    hideItems(errTv);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                hideItems(errTv);
            }
        });
    }

    public static void setupLoginPasswordTextWatcher(TextInputEditText et, TextView errTv) {
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                hideItems(errTv);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = s.toString().trim();
                if (input.isEmpty()) {
                    showItems(errTv);
                    errTv.setText("This field cannot be empty.");
                } else if (input.contains(" ")) {
                    showItems(errTv);
                    errTv.setText("Check your input for spaces.");
                } else {
                    hideItems(errTv);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                hideItems(errTv);
            }
        });
    }

    public static void setupEmailTextWatcher(TextInputEditText et, TextView errTv) {
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                hideItems(errTv);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = s.toString().trim();
                if (input.isEmpty()) {
                    showItems(errTv);
                    errTv.setText("This field cannot be empty.");
                } else if (input.contains(" ")) {
                    showItems(errTv);
                    errTv.setText("Check your input for spaces.");
                } else if (checkEmailPattern(input)) {
                    showItems(errTv);
                    errTv.setText("Provided email is incorrect.");
                } else {
                    hideItems(errTv);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                hideItems(errTv);
            }
        });
    }

    public static void setupUniversalTextWatcher(TextInputEditText et, TextView errTv) {
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                hideItems(errTv);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = s.toString().trim();
                if (input.isEmpty()) {
                    showItems(errTv);
                    errTv.setText("This field cannot be empty.");
                } else {
                    hideItems(errTv);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                hideItems(errTv);
            }
        });
    }

    public static void setupAddFlashcardTextWatcher(TextInputEditText et, TextView errTv) {

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                hideItems(errTv);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = s.toString().trim();
                if (input.isEmpty()) {
                    showItems(errTv);
                    errTv.setText("This field cannot be empty.");
                } else {
                    hideItems(errTv);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                hideItems(errTv);
            }
        });
    }

    public static boolean isErrVisible(TextView errorTv) {
        return errorTv.getVisibility() == View.VISIBLE;
    }

    public static void showItems(View... elements) {
        for (View element : elements) {
            element.setVisibility(View.VISIBLE);
        }
    }

    public static void hideItems(View... elements) {
        for (View element : elements) {
            element.setVisibility(View.GONE);
        }
    }

    public static void enableItems(View... items) {
        for (View item : items) {
            item.setEnabled(true);
        }
    }

    public static void disableItems(View... items) {
        for (View item : items) {
            item.setEnabled(false);
        }
    }

    public static void openEmailClient(Context context) {
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_APP_EMAIL);
            context.startActivity(intent);
        } catch (android.content.ActivityNotFoundException e) {
            showToast(context, "There is no email client installed");
        }
    }

    public static void terminateApp(Activity activity) {
        if (backClickCounter) {
            activity.finishAffinity();
            System.exit(0);
            return;
        }

        backClickCounter = true;
        Toast.makeText(activity, "Press back again to exit MALLet", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> backClickCounter = false, 2000); // Reset the flag after 2 seconds
    }

    public static void closeActivity(Activity a) {
        a.finish();
    }

    public static void resetEditText(TextInputEditText et, TextView err) {
        et.setText("");
        et.clearFocus();
        hideItems(err);
    }

    public static void saveSwitchState(Context context, String prefsName, String switchKey, boolean isChecked) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(switchKey, isChecked);
        editor.apply();
    }

    public static boolean getSwitchState(Context context, String prefsName, String switchKey) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(switchKey, false);
    }

    public static boolean checkPasswordPattern(String password) {
        String PASSWORD_PATTERN = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return !password.matches(".*\\d.*") || !matcher.matches();
    }

    public static boolean checkEmailPattern(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        Matcher matcher = pattern.matcher(email);
        // Check if the input matches the email pattern
        return !matcher.matches();
    }

    public static ModelLearningSet readFlashcards(Context context, String filePath) {
        ModelLearningSet learningSet = null;

        List<ModelFlashcard> flashcards = new ArrayList<>();

        String setName;
        String setCreator;
        String setDescription;
        long setId = 0;
        String nextChunkUri = "";

        try {
            AssetManager assetManager = context.getAssets();

            InputStreamReader inputStreamReader = new InputStreamReader(assetManager.open(filePath));
            BufferedReader reader = new BufferedReader(inputStreamReader);

            setName = reader.readLine();
            setCreator = reader.readLine();
            setDescription = reader.readLine();

            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";"); // Split the line into parts using ';' as the delimiter
                if (parts.length == 3) {
                    String term = parts[0].trim();
                    String definition = parts[1].trim();
                    String translation = parts[2].trim();

                    flashcards.add(new ModelFlashcard(term, definition, translation));

                    /*// Display the flashcard
                   // System.out.println("Term: " + term);
                   // System.out.println("Definition: " + definition);
                   // System.out.println("Translation: " + translation);
                   // System.out.println();*/
                }
            }

            reader.close();

            learningSet = new ModelLearningSet(setName, setCreator, setDescription, flashcards, setId, nextChunkUri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return learningSet;
    }

    public static void makeTextPhat(Context c, TextView tv) {
        Typeface phatFont = ResourcesCompat.getFont(c, R.font.rem_bold);
        tv.setTypeface(phatFont);
    }

    public static void setViewLayoutParams(View view, int width, int height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();

        if (layoutParams != null) {
            layoutParams.width = width;
            layoutParams.height = height;
            view.setLayoutParams(layoutParams);
        }
    }

    public static void setTextColor(Context c, TextView tv, int colorId) {
        int color = ContextCompat.getColor(c, colorId);
        tv.setTextColor(color);
    }

    public static void setMargins(Context c, View view, int leftDp, int topDp, int rightDp, int bottomDp) {
        int leftPx = dpToPx(leftDp, c);
        int topPx = dpToPx(topDp, c);
        int rightPx = dpToPx(rightDp, c);
        int bottomPx = dpToPx(bottomDp, c);

        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams layoutParams) {
            layoutParams.setMargins(leftPx, topPx, rightPx, bottomPx);
            view.requestLayout();
        }
    }

    private static int dpToPx(int dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static Dialog createDialog(Context context, int layoutResId, ViewGroup.LayoutParams layoutParams, int gravity) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layoutResId);
        dialog.setCanceledOnTouchOutside(false);

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(gravity);

        if (layoutParams != null) {
            Window window = dialog.getWindow();
            if (window != null) {
                window.setLayout(layoutParams.width, layoutParams.height);
            }
        }

        return dialog;
    }

    public static void visuallyDisableTvs(Context context, TextView... tvs) {
        for (TextView tv : tvs) {
            tv.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        }

    }

    public static void visuallyDisableIvs(Context context, ImageView... ivs) {
        for (ImageView iv : ivs) {
            iv.setColorFilter(context.getResources().getColor(R.color.colorPrimary));
        }
    }

}