package com.android.paskahlis.anchat.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;


import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

/**
 * Created by fajar on 23/02/18.
 */

public class UserPrefs {

    private static final String KEY_EMAIL = "email";
    private static final String KEY_TOKEN = "token";
//    private static final String KEY_STATUS = "status";
    private static final String KEY_PROFILE_USER = "user";



    private static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static String getKeyEmail(Context context) {
        return getSharedPreferences(context).getString(KEY_EMAIL, null);
    }

    public static String getKeyToken(Context context) {
        return getSharedPreferences(context).getString(KEY_TOKEN, null);
    }


    public static void setKeyEmail(Context context,String email){
        getSharedPreferences(context).edit().putString(KEY_EMAIL, email).apply();
    }

    public static void setKeyToken(Context context, String token){
        getSharedPreferences(context).edit().putString(KEY_TOKEN, token).apply();
    }

    public static FirebaseUser getUser(Context context) {
        String json = getSharedPreferences(context).getString(KEY_PROFILE_USER, null);
        if (TextUtils.isEmpty(json)) return null;
        return new Gson().fromJson(json, FirebaseUser.class);
    }

    public static void saveUser(Context context, FirebaseUser user) {
        Gson gson = new Gson();
        String userJson = gson.toJson(user);
        getSharedPreferences(context).edit().putString(KEY_PROFILE_USER, userJson).apply();
    }


}
