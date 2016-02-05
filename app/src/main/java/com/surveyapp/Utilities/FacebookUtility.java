package com.surveyapp.Utilities;

import android.support.v4.app.Fragment;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by user on 05-02-2016.
 */
public class FacebookUtility {
    private CallbackManager callbackManager;
    Fragment fragment ;

    public FacebookUtility(Fragment fragment) {
        this.fragment = fragment;
    }

    public CallbackManager getCallbackManager() {
        return callbackManager;
    }

    public JSONObject getJsonFromLoginResult(LoginResult loginResult){
        final JSONObject[] resultJson = new JSONObject[1];
        GraphRequest.newMeRequest(loginResult.getAccessToken(),new GraphRequest.GraphJSONObjectCallback(){
            @Override
            public void onCompleted(JSONObject jsonObject, GraphResponse response) {
                if(response.getError()!=null){
                    //TODO:Handle error
                }
                else{
                    resultJson[0] = jsonObject ;

                }
            }
        }).executeAsync();
        return resultJson[0];
    }
    public void onFbLogin(){
        callbackManager= CallbackManager.Factory.create();
        LoginManager.getInstance().logInWithReadPermissions(fragment, Arrays.asList("email"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                /* get access token and graph the login result to json object store the
                result in shared preferences and use getJsonFromURL to connect to the server and send details to them*/
            }

            @Override
            public void onCancel() {
                Log.d("error", "Fb Login Cancelled in FragmentLogin ");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("error", "Fb Login ERROR in FragmentLogin ");
            }
        });

    }
}
