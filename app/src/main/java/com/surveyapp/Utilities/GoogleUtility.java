package com.surveyapp.Utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.surveyapp.Activities.LandingActivity;
import com.surveyapp.Constants;
import com.surveyapp.CustomObjects.User;
import com.surveyapp.Fragments.FragmentLogin;
import com.surveyapp.R;
import com.surveyapp.SharedPrefUtil;
import com.surveyapp.Utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Rahul Yadav on 13-02-2016.
 */
public class GoogleUtility implements GoogleApiClient.OnConnectionFailedListener{

    private Context context;
    private SharedPrefUtil sharedPrefUtil;
    private MaterialDialog  progressDialog;
    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions googleSignInOptions;
    private FragmentActivity fragmentActivity;

    public GoogleUtility(Context context,FragmentActivity fragmentActivity) {
        this.context = context;
        this.sharedPrefUtil = new SharedPrefUtil(context);
        this.fragmentActivity = fragmentActivity;
        this.buildGoogleSignInOptions();

        if (googleApiClient!=null){
            buildGoogleApiClient();}
        this.progressDialog = new MaterialDialog.Builder(context).progress(true,100).content("Logging You In").build();
    }

    public void buildGoogleApiClient(){
        googleApiClient = new GoogleApiClient.Builder(context)
                .enableAutoManage(fragmentActivity, R.string.google_client_id, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();
    }


    private void buildGoogleSignInOptions(){
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();
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

    public GoogleApiClient getGoogleApiClient()
    {   if (googleApiClient==null){
            buildGoogleApiClient();}

        return googleApiClient;
    }

    public void checkUserOnServer(GoogleSignInAccount userAccount){
        new CheckUserExistence().execute(userAccount.getDisplayName(),userAccount.getEmail());
    }

    public class CheckUserExistence extends AsyncTask<String,Void,User> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog();

        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);

            hideDialog();

            sharedPrefUtil.createSession(user.getId(), user.getName(), user.getPassword(), user.getEmail(), user.getLoginType(), user.getPlan());

            if (FragmentLogin.FILL_SURVEY_LOGIN){
                Intent result = new Intent("com.surevyapp.Surveyour.RESULT_ACTION", Uri.parse(""));
                Activity fragmentActivity = (Activity) context;
                fragmentActivity.setResult(Activity.RESULT_OK,result);
                fragmentActivity.finish();

                return;
            }


            Intent intent = new Intent(context, LandingActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
        }

        @Override
        protected User doInBackground(String... params) {
            String completeUrl = "http://contactsyncer.com/signin.php?name="+params[0]+"&email="+params[1]+"&type=google";//will be changed when web service is created!
            completeUrl = completeUrl.replaceAll(" ", "%20");

            JSONObject jsonObject = Utils.getJSONFromUrl(completeUrl);

            User user = new User();

            user.setEmail(params[1]);
            user.setLoginType(Constants.TYPE_GOOGLE_LOGIN);
            user.setPassword(null);
            user.setPlan(null);
            user.setName(params[0]);


            try{
                int userId = Integer.valueOf(jsonObject.getString("userID"));
                user.setId(userId);

                //Do the Stuff Here
            }
            catch (JSONException e){
                e.printStackTrace();
            }

            return user;

        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //not connected to google api
    }
}
