package com.example.mallet;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mallet.databinding.ActivitySignUpBinding;
import com.example.mallet.databinding.DialogChooseUsernameBinding;
import com.example.mallet.utils.FrontendUtils;

public class ActivitySignUp extends AppCompatActivity {

    ActivitySignUpBinding binding;
    DialogChooseUsernameBinding dialogBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupAnimation();
        setupListeners();
        setupLogInField();
    }

    private void setupListeners() {
        binding.signUpContinue.setOnClickListener(v -> chooseUsernameDialog());
    }

    private void chooseUsernameDialog() {
        String email = binding.signUpEmailEt.getText().toString();
        TextView emailErrorTV = binding.signUpEmailErrorTv;

        String password = binding.signUpPasswordEt.getText().toString();
        TextView passwordErrorTV = binding.signUpPasswordErrorTv;

        if (FrontendUtils.emailPatternCheck(email, emailErrorTV) == true
                && FrontendUtils.passwordPatternCheck(password, passwordErrorTV) == true) {
            Dialog dialog = FrontendUtils.createDialog(this, "dialog_choose_username");
            DialogChooseUsernameBinding binding = DialogChooseUsernameBinding.inflate(getLayoutInflater());
            FrontendUtils.showDialog(dialog);
            FrontendUtils.showToast(this, "\"Continue\" in sign up activity was clicked");
        } else {
            FrontendUtils.showToast(this, "NOPE");
        }
    }

    private void setupAnimation() {
        TextView pulsatingLogoTV = binding.signUpLogo;
        Animation pulseAnimation = AnimationUtils.loadAnimation(this, R.anim.pulse_anim);
        pulsatingLogoTV.startAnimation(pulseAnimation);
    }

    private void setupLogInField() {
        TextView signUp = binding.logInBtn;

        signUp.setOnClickListener(v -> {
            Intent intent = new Intent(ActivitySignUp.this, ActivityLogIn.class);
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
        // Start ChooseLogInSignUpActivity and clear the back stack
        Intent intent = new Intent(ActivitySignUp.this, ActivityOpening.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish(); // Finish the SignUpActivity
    }
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
