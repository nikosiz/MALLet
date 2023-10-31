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
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example.mallet.R;

import org.w3c.dom.Text;

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
        String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
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
        String setName; // Name of the learning set
        String setCreator; // Creator of the learning set
        String setDescription; // Description of the learning set
        long setId = 0;
        String nextChunkUri = "";

        try {
            // Get the asset manager for reading files from the assets folder
            AssetManager assetManager = context.getAssets();

            // Open the file at the specified filePath for reading
            InputStreamReader inputStreamReader = new InputStreamReader(assetManager.open(filePath));
            BufferedReader reader = new BufferedReader(inputStreamReader);

            // Read the first three lines of the file (setName, setCreator, setDescription)
            setName = reader.readLine(); // Read the first line
            setCreator = reader.readLine(); // Read the second line
            setDescription = reader.readLine(); // Read the third line

            String line;

            // Read the remaining lines containing flashcard data
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";"); // Split the line into parts using ';' as the delimiter
                if (parts.length == 3) {
                    // Extract and trim the term, definition, and translation from the parts
                    String term = parts[0].trim();
                    String definition = parts[1].trim();
                    String translation = parts[2].trim();

                    // Create a new ModelFlashcard object and add it to the flashcards list
                    flashcards.add(new ModelFlashcard(term, definition, translation));
                }
            }

            // Close the reader
            reader.close();

            // Create a new ModelLearningSet object with the extracted data
            learningSet = new ModelLearningSet(setName, setCreator, setDescription, flashcards, setId, nextChunkUri);
        } catch (IOException e) {
            // Handle any IOException by printing the stack trace
            e.printStackTrace();
        }

        // Return the populated ModelLearningSet object
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

    public static void setTextColor(TextView textView, int color) {
        textView.setTextColor(color);
    }
}