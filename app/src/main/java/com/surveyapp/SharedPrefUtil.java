package com.surveyapp;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by user on 31-01-2016.
 */
public class SharedPrefUtil {

    private final String PREFERENCES="com.surveyapp";
    private final String USER_NAME="username";
    private final String USER_PASSWORD="userPassword";
    private final String USER_EMAIL="userEmail";
    private final String LOGGED_IN_STATUS = "loggedInStatus";
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    public SharedPrefUtil(Context context) {
        this.context=context;
        sharedPreferences = context.getSharedPreferences(PREFERENCES, context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if (!sharedPreferences.contains(LOGGED_IN_STATUS)){
            editor.putBoolean(LOGGED_IN_STATUS,false).commit();
        }
    }

    public boolean getLoggedInStatus() {
        return sharedPreferences.getBoolean(LOGGED_IN_STATUS,false);
    }

    public void setUsername(String name) {
        editor.putString(USER_NAME,name).commit();
    }

    public void setUserEmail(String email) {
        editor.putString(USER_EMAIL,email).commit();
    }

    public void setUserPassword(String password) {
        editor.putString(USER_PASSWORD,password).commit();
    }

    public String getUsername() {
        return sharedPreferences.getString(USER_NAME,null);
    }

    public String getUserPassword() {
        return sharedPreferences.getString(USER_PASSWORD,null);
    }

    public void clearSharedPref(){
        editor.clear().commit();
    }
}

