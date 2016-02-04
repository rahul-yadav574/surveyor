package com.surveyapp;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by user on 31-01-2016.
 */
public class SharedPrefUtil {

    private static final String PREFERENCES="com.surveyapp";
    public static final String USER_NAME="username";
    public static final String USER_PASSWORD="userPassword";
    public static final String USER_EMAIL="userEmail";

    public static void setUsername(Context context,String name)
    {
        SharedPreferences sharedPreferences=context.getSharedPreferences(PREFERENCES,context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(USER_NAME,name);
        editor.commit();
    }

    public static void setUserEmail(Context context,String email)
    {
        SharedPreferences sharedPreferences=context.getSharedPreferences(PREFERENCES,context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(USER_EMAIL,email);
        editor.commit();
    }

    public static void setUserPassword(Context context,String password)
    {
        SharedPreferences sharedPreferences=context.getSharedPreferences(PREFERENCES,context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(USER_PASSWORD,password);
        editor.commit();
    }

    public static String getUsername(Context context)
    {
        SharedPreferences sharedPreferences=context.getSharedPreferences(PREFERENCES,context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_NAME,null);
    }

    public static String getUserPassword(Context context)
    {
        SharedPreferences sharedPreferences=context.getSharedPreferences(PREFERENCES,context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_PASSWORD,null);
    }
}

