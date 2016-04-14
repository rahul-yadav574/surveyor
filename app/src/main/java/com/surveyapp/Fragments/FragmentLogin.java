package com.surveyapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.surveyapp.Activities.ActivityLoginSignUp;
import com.surveyapp.Activities.LandingActivity;
import com.surveyapp.Constants;
import com.surveyapp.R;
import com.surveyapp.Utilities.FacebookUtility;
import com.surveyapp.Utilities.GoogleUtility;
import com.surveyapp.Utils;

/**
 * Created by Rahul Yadav on 31-01-2016.
 */
public class FragmentLogin extends Fragment  {


   // private AutoCompleteTextView loginIdInput;
   // private AutoCompleteTextView loginPasswordInput;
   // private TextInputLayout loginIdInputLayout;
   // private TextInputLayout loginPasswordInputLayout;
   // private Button loginButton;
  //  private Button troubleLoginButton;
    private Button googleLoginButton;
    private Button fbLoginButton;
    FacebookUtility facebookUtility ;
    private GoogleUtility googleUtility;
    private final int RC_SIGN_IN = 9001;
    public static boolean  FILL_SURVEY_LOGIN;

    public FragmentLogin() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        FacebookSdk.sdkInitialize(getActivity());
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.btn_login);
        ActivityLoginSignUp.toolbar.setVisibility(View.VISIBLE);
        facebookUtility = new FacebookUtility(this,getActivity());
        googleUtility = new GoogleUtility(getActivity(),getActivity());
        ActivityLoginSignUp.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        this.setHasOptionsMenu(true);

        FILL_SURVEY_LOGIN = false;

        String action = getActivity().getIntent().getAction();
        if(action!=null && action.equals(Constants.LOGIN_ACTION_FOR_FILL_SURVEY)){
            FILL_SURVEY_LOGIN = true;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login,container,false);

    /*    loginIdInput = (AutoCompleteTextView) rootView.findViewById(R.id.loginId);
        loginPasswordInput = (AutoCompleteTextView)  rootView.findViewById(R.id.loginPassword);
        loginIdInputLayout = (TextInputLayout) rootView.findViewById(R.id.loginIdInputLayout);
        loginPasswordInputLayout = (TextInputLayout) rootView.findViewById(R.id.loginPasswordInputLayout);
        loginButton = (Button) rootView.findViewById(R.id.loginButton);*/
        fbLoginButton = (Button) rootView.findViewById(R.id.facebookLoginButton);
        googleLoginButton = (Button) rootView.findViewById(R.id.googleLoginButton);


       /* loginIdInput.addTextChangedListener(new MyTextWatcher(loginIdInput));
        loginPasswordInput.addTextChangedListener(new MyTextWatcher(loginPasswordInput));

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUserLoginRequest();
            }
        });*/

        fbLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facebookUtility.onFbLogin();
            }
        });

        googleLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGoogleLogin();

            }
        });

        return rootView;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        facebookUtility.getCallbackManager().onActivityResult(requestCode, resultCode, data);


        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){

            View view = getActivity().getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            getActivity().getSupportFragmentManager().popBackStack();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



 /*   private void startUserLoginRequest(){

        if (!validateEmail(loginIdInput.getText().toString())) {
            return;
        }

        if (!validatePassword(loginPasswordInput.getText().toString())) {
            return;
        }

        //Do The Login Stuff Here

        Intent intent = new Intent(getActivity(), LandingActivity.class);
        startActivity(intent);

        Toast.makeText(getActivity(), "Thank You!", Toast.LENGTH_SHORT).show();
    }*/



   /* private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.loginId:
                    validateEmail(loginIdInput.getText().toString());
                    break;
                case R.id.loginPassword:
                    validatePassword(loginPasswordInput.getText().toString());
                    break;
            }
        }
    }*/

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

  /*  private boolean validateEmail(String email) {

        if (email.isEmpty() || !Utils.isValidEmail(email)) {
            loginIdInputLayout.setError(getString(R.string.err_email_input));
            requestFocus(loginIdInput);
            return false;
        } else {
            loginIdInputLayout.setErrorEnabled(false);
        }

        return true;
    }*/

   /* private boolean validatePassword(String password) {
        if (password.trim().isEmpty()) {
            loginPasswordInputLayout.setError(getString(R.string.err_password_input));
            requestFocus(loginPasswordInput);
            return false;
        } else {
            loginPasswordInputLayout.setErrorEnabled(false);
        }

        return true;
    }
*/
    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleUtility.getGoogleApiClient());
        if (opr.isDone()) {
            /* If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            and the GoogleSignInResult will be available instantly.*/
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            /*If the user has not previously signed in on this device or the sign-in has expired,
            this asynchronous branch will attempt to sign in the user silently.  Cross-device
            single sign-on will occur in this branch.*/
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        stopAutoManage();        //otherwise when we start the activity again it will give error that g+ api client already built
    }

    @Override
    public void onPause() {
        super.onPause();
    }



    public void startGoogleLogin(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleUtility.getGoogleApiClient());
        startActivityForResult(signInIntent, RC_SIGN_IN);           //As we cant call the startActivityForResult from Non Ui Classes So We Need A Activity Referene
    }


    public void handleSignInResult(GoogleSignInResult result){
        if (result.isSuccess()) {
            googleUtility.checkUserOnServer(result.getSignInAccount());

           // GoogleUtility(getActivity()).CheckUserExistence().execute(acct.getDisplayName(), acct.getEmail());
        }
    }

    public void stopAutoManage(){
        googleUtility.getGoogleApiClient().stopAutoManage(getActivity());
    }

}
