package com.example.mallet.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.mallet.R;

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

    public static Dialog createDialog(Context context, int layoutResourceId) {
        if (layoutResourceId == 0) {
            return null;
        }

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layoutResourceId);

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        return dialog;
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


    // This stays, what is up, needs to be reviewed
    public static void setupAnimation(Context context, TextView logo) {
        Animation pulseAnimation = AnimationUtils.loadAnimation(context, R.anim.pulse_anim);
        logo.startAnimation(pulseAnimation);
    }

    public static void setupPasswordTextWatcher(EditText et, TextView err) {
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = s.toString().trim();
                if (input.isEmpty()) {
                    showItems(err);
                    err.setText("This field cannot be empty");
                } else if (input.contains(" ")) {
                    showItems(err);
                    err.setText("Check your input for spaces");
                } else if (checkPasswordPattern(input)) {
                    showItems(err);
                    err.setText("Invalid password");
                } else {
                    hideItems(err);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public static void setupEmailTextWatcher(EditText et, TextView err) {
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = s.toString().trim();
                if (input.isEmpty()) {
                    showItems(err);
                    err.setText("This field cannot be empty");
                } else if (input.contains(" ")) {
                    showItems(err);
                    err.setText("Check your input for spaces");
                } else if (checkEmailPattern(input)) {
                    showItems(err);
                    err.setText("Invalid email");
                } else {
                    hideItems(err);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public static void setupUniversalTextWatcher(EditText et, TextView err) {
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = s.toString().trim();
                if (input.isEmpty()) {
                    showItems(err);
                    err.setText("This field cannot be empty");
                } else {
                    hideItems(err);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    public static void setupConfirmPasswordTextWatcher(EditText newPassEt, EditText confirmNewPassEt, TextView confirmErrTv, Pattern p, String errMsg) {
        confirmNewPassEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newPassword = newPassEt.getText().toString();
                String confirmPassword = confirmNewPassEt.getText().toString();
                if (confirmPassword.isEmpty()) {
                    showItems(confirmErrTv);
                    confirmErrTv.setText("This field cannot be empty");
                } else if (confirmPassword.contains(" ")) {
                    showItems(confirmErrTv);
                    confirmErrTv.setText("Check your input for spaces");
                } else if (!p.matcher(confirmPassword).matches()) {
                    showItems(confirmErrTv);
                    confirmErrTv.setText(errMsg);
                } else if (!confirmPassword.equals(newPassword)) { // Use .equals() to compare string content
                    showItems(confirmErrTv);
                    confirmErrTv.setText("Passwords do not match");
                } else {
                    hideItems(confirmErrTv);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
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

    public static void makeItemsClickable(View... items) {
        for (View item : items) {
            item.setClickable(true);
        }
    }

    public static void makeItemsUnclickable(View... items) {
        for (View item : items) {
            item.setClickable(false);
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
        Toast.makeText(activity, "Press back again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> backClickCounter = false, 2000); // Reset the flag after 2 seconds
    }

    public static void resetEditText(EditText et, TextView err) {
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
        String PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*[@_.]).*$";
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

}