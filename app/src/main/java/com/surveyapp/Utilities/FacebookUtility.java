package com.surveyapp.Utilities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telecom.Call;
import android.util.Log;

import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.surveyapp.Activities.LandingActivity;
import com.surveyapp.Constants;
import com.surveyapp.CustomObjects.User;
import com.surveyapp.SharedPrefUtil;
import com.surveyapp.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by user on 05-02-2016.
 */
public class FacebookUtility {
    private CallbackManager callbackManager;
    private Fragment fragment ;
    private MaterialDialog progressDialog;
    private SharedPrefUtil sharedPrefUtil;
    private Context context;


    public FacebookUtility(Fragment fragment,Context context) {
        this.context = context;
        this.callbackManager = CallbackManager.Factory.create();
        this.fragment = fragment;
        this.progressDialog = new MaterialDialog.Builder(context).content("Logging You In").progress(true,100).build();
        this.sharedPrefUtil = new SharedPrefUtil(context);
    }

    public CallbackManager getCallbackManager() {
        return this.callbackManager;
    }

    private void makeGraphApiRequest(LoginResult loginResult){
        GraphRequest graphRequest;
        graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {


               /* this will check whether user exists on our website*/

                try {
                    checkInfoOnServer(jsonObject);
                }
                catch(JSONException e){
                    e.printStackTrace();
                }

            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender,birthday");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();
    }

    public void onFbLogin(){

        LoginManager.getInstance().logInWithReadPermissions(fragment, Arrays.asList("email", "user_photos", "public_profile"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                makeGraphApiRequest(loginResult);
            }

            @Override
            public void onCancel() {
                Log.d("error", "Fb Login Cancelled in FragmentLogin ");
            }

            @Override
            public void onError(FacebookException error) {
                Utils.toastL(context,"Error Connecting to facebook ...try later");
            }
        });
    }

    private void checkInfoOnServer(JSONObject jsonObject) throws JSONException{

        String facebookId = jsonObject.getString("id");
        String facebookUserName = jsonObject.getString("name");
        String facebookUserEmail = jsonObject.getString("email");

        new CheckUserExistence().execute(facebookId,facebookUserEmail,facebookUserName);
    }

    private void showDialog(){
        if (progressDialog!=null && !progressDialog.isShowing()){
            progressDialog.show();
        }
    }

    private void hideDialog(){
        if (progressDialog!=null && progressDialog.isShowing()){
            progressDialog.cancel();
        }
    }


    protected class CheckUserExistence extends AsyncTask<String,Void,User> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog();

        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);

            hideDialog();
            sharedPrefUtil.createSession(user.getId(),user.getName(),user.getPassword(), user.getEmail(),user.getLoginType(),user.getPlan());
            Intent intent = new Intent(context, LandingActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
        }

        @Override
        protected User doInBackground(String... params) {
            String completeUrl = "http://contactsyncer.com/signin.php?type=fb&name="+params[2]+"&fbID="+params[0]; //will be changed when web service is created!
            completeUrl = completeUrl.replaceAll(" ", "%20");

            JSONObject jsonObject = Utils.getJSONFromUrl(completeUrl);

            User user = new User();

            user.setEmail(params[1]);
            user.setLoginType(Constants.TYPE_FACEBOOK_LOGIN);
            user.setPassword(null);
            user.setPlan(null);
            user.setName(params[2]);

            try{
                int userId = Integer.valueOf(jsonObject.getString("userID"));
                user.setId(userId);

            }
            catch (JSONException e){
                e.printStackTrace();
            }

            return user;

        }
    }

}
