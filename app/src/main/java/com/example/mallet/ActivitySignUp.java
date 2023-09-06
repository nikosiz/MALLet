package com.example.mallet;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mallet.databinding.ActivitySignUpBinding;
import com.example.mallet.databinding.DialogChooseUsernameBinding;
import com.example.mallet.utils.Utils;

import java.util.Objects;
import java.util.regex.Pattern;

public class ActivitySignUp extends AppCompatActivity {

    // Binding for the activity layout
    ActivitySignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the binding layout and set it as the content view
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize animation, listeners, and views
        setupAnimation();
        setupListeners();
        initializeViews();
    }

    // Set up animation for the pulsating logo
    private void setupAnimation() {
        TextView pulsatingLogoTV = binding.signUpLogo;
        Animation pulseAnimation = AnimationUtils.loadAnimation(this, R.anim.pulse_anim);
        pulsatingLogoTV.startAnimation(pulseAnimation);
    }

    // Set up click listeners for buttons
    private void setupListeners() {
        binding.signUpContinue.setOnClickListener(v -> chooseUsernameDialog());
        binding.logInBtn.setOnClickListener(v -> Utils.openActivity(this, ActivityLogIn.class));
    }

    // Validate signup data including email and password
    private boolean validateSignupData(String email, String password) {
        TextView emailErrorTV = binding.signUpEmailErrorTv;
        TextView emailEmptyTV = binding.signUpEmailEmptyEmailTv;
        TextView passwordErrorTV = binding.signUpPasswordErrorTv;
        TextView passwordEmptyTV = binding.signUpEmailEmptyPasswordTv;
        TextView passwordSpaceErrorTV = binding.signUpPasswordSpaceErrorTv;

        Pattern passwordPattern = Pattern.compile("^" +
                "(?=.*[0-9])" +         // at least 1 digit
                "(?=.*[a-z])" +         // at least 1 lowercase letter
                "(?=.*[A-Z])" +         // at least 1 uppercase letter
                "(?=.*[@#$%^&+=])" +    // at least 1 special character
                "(?=\\S+$)" +           // no white spaces
                ".{8,}" +               // at least 8 characters
                "$");

        // Reset error messages initially
        Utils.hideItem(emailErrorTV);
        Utils.hideItem(emailEmptyTV);
        Utils.hideItem(passwordErrorTV);
        Utils.hideItem(passwordEmptyTV);
        Utils.hideItem(passwordSpaceErrorTV);

        boolean isValid = true;

        if (email.isEmpty()) {
            Utils.showItem(emailEmptyTV);
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Utils.showItem(emailErrorTV);
            isValid = false;
        }

        if (password.isEmpty()) {
            Utils.showItem(passwordEmptyTV);
            isValid = false;
        } else if (!passwordPattern.matcher(password).matches()) {
            Utils.showItem(passwordErrorTV);
            isValid = false;
        } else if (password.contains(" ")) {
            Utils.showItem(passwordSpaceErrorTV);
            isValid = false;
        }

        return isValid;
    }

    // Initialize views and attach text change listeners
    private void initializeViews() {
        EditText emailEditText = binding.signUpEmailEt;
        EditText passwordEditText = binding.signUpPasswordEt;
        TextView emailErrorTV = binding.signUpEmailErrorTv;
        TextView emailEmptyTV = binding.signUpEmailEmptyEmailTv;
        TextView passwordErrorTV = binding.signUpPasswordErrorTv;
        TextView passwordEmptyTV = binding.signUpEmailEmptyPasswordTv;
        TextView passwordSpaceErrorTV = binding.signUpPasswordSpaceErrorTv;

        Pattern passwordPattern = Pattern.compile("^" +
                "(?=.*[0-9])" +         // at least 1 digit
                "(?=.*[a-z])" +         // at least 1 lowercase letter
                "(?=.*[A-Z])" +         // at least 1 uppercase letter
                "(?=.*[@#$%^&+=])" +    // at least 1 special character
                "(?=\\S+$)" +           // no white spaces
                ".{8,}" +               // at least 8 characters
                "$");

        // Initialize text change listeners for email and password fields
        initializeTextWatchers(emailEditText, passwordEditText, emailErrorTV, emailEmptyTV, passwordErrorTV, passwordEmptyTV, passwordSpaceErrorTV, passwordPattern);
    }

    // Initialize text change listeners for email and password fields
    private void initializeTextWatchers(
            EditText emailEditText,
            EditText passwordEditText,
            TextView emailErrorTV,
            TextView emailEmptyTV,
            TextView passwordErrorTV,
            TextView passwordEmptyTV,
            TextView passwordSpaceErrorTV,
            Pattern passwordPattern) {

        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // This method is not used here, but you can implement it if needed.
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String email = s.toString().trim();
                Utils.hideItem(emailErrorTV);
                Utils.hideItem(emailEmptyTV);

                if (email.isEmpty()) {
                    Utils.showItem(emailEmptyTV);
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Utils.showItem(emailErrorTV);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // This method is not used here, but you can implement it if needed.
            }
        });

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // This method is not used here, but you can implement it if needed.
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = s.toString().trim();
                Utils.hideItem(passwordErrorTV);
                Utils.hideItem(passwordEmptyTV);
                Utils.hideItem(passwordSpaceErrorTV);

                if (password.isEmpty()) {
                    Utils.showItem(passwordEmptyTV);
                } else if (!passwordPattern.matcher(password).matches()) {
                    Utils.showItem(passwordErrorTV);
                } else if (password.contains(" ")) {
                    Utils.showItem(passwordSpaceErrorTV);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // This method is not used here, but you can implement it if needed.
            }
        });
    }

    // Handle the "Continue" button click and show the username dialog if data is valid
    private void chooseUsernameDialog() {
        String email = Objects.requireNonNull(binding.signUpEmailEt.getText()).toString();
        String password = Objects.requireNonNull(binding.signUpPasswordEt.getText()).toString();

        boolean isValid = validateSignupData(email, password);

        if (!isValid) {
            Utils.showToast(this, "Invalid email or password");
        } else {
            // Valid email and password, proceed to show the dialog

            Dialog dialog = Utils.createDialog(this, R.layout.dialog_choose_username);
            DialogChooseUsernameBinding dialogBinding = DialogChooseUsernameBinding.inflate(getLayoutInflater());
            assert dialog != null;
            dialog.setContentView(dialogBinding.getRoot());
            Utils.showDialog(dialog);
            Utils.showToast(this, "\"Continue\" in sign-up activity was clicked");
        }
    }

    @Override
    public void onBackPressed() {
        // Start ActivityOpening and clear the back stack
        Intent intent = new Intent(ActivitySignUp.this, ActivityOpening.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish(); // Finish the SignUpActivity
    }


// TODO
/*
    public void handleRegisterBtn() {

        dialogBinding = DialogChooseUsernameBinding.inflate(LayoutInflater.from(this));

        disableElements();
        String email = binding.signUpEmailEt.getText().toString();
        String password = binding.signUpPasswordEt.getText().toString();
        String username = dialogBinding.chooseUsernameNameEt.getText().toString();

        boolean validateAllData = validateAllData(email, password, username);

        // TODO
        if (validateAllData) {
            FrontendUtils.showProgressBar(this);
            FrontendUtils.showToast(this, "Sign up successful");
            signUpRequest(email, password, username, new VolleyRequestCallback() {
                @Override
                public void onSuccess() {
                    Dialog dialog = FrontendUtils.createDialog(this, "dialog_open_email");
                    dialog.setContentView(binding.getRoot());

                }
            });
        } else {
            FrontendUtils.showToast(this, "Sign up failed");
            enableElements();
        }
    }

    // TODO update to fit to MALLet and MAKE SANGUAGE GREAT AGAIN
    public void signUpRequest(String username, String email, String password, String secondLanguage, final VolleyRequestCallback callback) {

        String URL = "https://sanguage.herokuapp.com/registration";
        try {
            JSONObject signUpJSON = createSignUpJSON(username, email, password, secondLanguage);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, signUpJSON, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String userID = response.getString("messages");
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SignUpActivity.this);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putLong("userID", Long.valueOf(userID));
                        editor.apply();
                        callback.onSuccess();
                    } catch (JSONException jsonException) {
                        jsonException.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        String message = RequestErrorParser.parseError(error);
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    } catch (JSONException jsonException) {
                        jsonException.printStackTrace();
                    }
                    Log.e("signUpRequest - onErrorResponse()", String.valueOf(error));
                    hideProgressBar();
                    enableAllActions();
                }
            }
            );
            queue.add(jsonObjectRequest);
        } catch (JSONException j) {
            Log.e("SignUp - signUpRequest()", j.getMessage());
        }
    }

    public JSONObject createSignUpJSON(String username, String email, String password, String) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("email", email);
        jsonObject.put("password", password);
        jsonObject.put("username", username);
        return jsonObject;

    }


    public void disableElements(View... elements) {
        for (View element : elements) {
            element.setEnabled(false);
        }
    }

    public void enableElements(View... elements) {
        for (View element : elements) {
            element.setEnabled(true);
        }
    }
*/

}
