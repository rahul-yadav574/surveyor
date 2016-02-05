package com.surveyapp.Utilities;

import android.util.Log;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user on 05-02-2016.
 */
public class FacebookUtility {

    public void getJsonFromLoginResult(LoginResult loginResult){
        GraphRequest.newMeRequest(loginResult.getAccessToken(),new GraphRequest.GraphJSONObjectCallback(){
            @Override
            public void onCompleted(JSONObject jsonObject, GraphResponse response) {
                if(response.getError()!=null){
                    //TODO:Handle error
                }
                else{
                    try {
                        String jsonresult = String.valueOf(jsonObject);
                        String emailstring = jsonObject.getString("email");
                    }catch(JSONException exception){
                        Log.d("error", "JSONException in FacebookUtility");
                    }
                }
            }
        }).executeAsync();
    }
}
