package com.example.mallet.frontend.security;


import android.content.Context;
import android.content.SharedPreferences;

import org.apache.commons.lang3.StringUtils;

import okhttp3.Credentials;

public class CredentialsHandler {

    public static final String AUTHENTICATION = "Authentication";
    public static final String CREDENTIAL_PARAM = "credential";

    public static void save(Context context, String email, String password){
        SharedPreferences sharedPreferences = context.getSharedPreferences(AUTHENTICATION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CREDENTIAL_PARAM, Credentials.basic(email, password));
        editor.apply();
    }

    public static String get(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(AUTHENTICATION, Context.MODE_PRIVATE);
        return sharedPreferences.getString(CREDENTIAL_PARAM, StringUtils.EMPTY);
    }

}
