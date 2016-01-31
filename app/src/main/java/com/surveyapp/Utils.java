package com.surveyapp;

import android.content.Context;
import android.text.TextUtils;

/**
 * Created by arcks on 30/1/16.
 */
//This class will contain methods to be used through out the project


public class Utils {


    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}
