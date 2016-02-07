package com.surveyapp;

import android.content.Context;
import android.content.SharedPreferences;

import com.surveyapp.CustomObjects.User;

/**
 * Created by user on 04-02-2016.
 */
public class SharedPrefUtil {


    private final String USER_NAME = "userName";
    private final String PREFERENCES = "preferences";
    private final String USER_PASSWORD = "userPassword";
    private final String USER_EMAIL = "userEmail";
    private final String LOGGED_IN_STATUS = "loggedInStatus";
    private final String USER_LOGIN_TYPE = "userLogintype";
    private final String USER_PLAN = "userPlan";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public SharedPrefUtil(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREFERENCES,context.MODE_PRIVATE);
        editor=sharedPreferences.edit();

        if (!sharedPreferences.contains(LOGGED_IN_STATUS)){
            editor.putBoolean(LOGGED_IN_STATUS,false).commit();
        }
    }

    public void createSession(String userName,String password,String userEmail,int type, String plan){
        editor.putBoolean(LOGGED_IN_STATUS,true);
        editor.putString(USER_NAME,userName);
        editor.putString(USER_PASSWORD,password);
        editor.putString(USER_EMAIL, userEmail);
        editor.putInt(USER_LOGIN_TYPE , type);
        editor.putString(USER_PLAN,plan);
        editor.commit();
    }

    public boolean getLoggedInStatus(){
        return sharedPreferences.getBoolean(LOGGED_IN_STATUS,false);
    }

    public User getUserInfo(){
        return new User(sharedPreferences.getString(USER_NAME,null)
                ,sharedPreferences.getString(USER_EMAIL,null)
                ,sharedPreferences.getString(USER_PASSWORD,null)
                ,sharedPreferences.getString(USER_PLAN,null)
                ,sharedPreferences.getInt(USER_LOGIN_TYPE,0));
    }

}
