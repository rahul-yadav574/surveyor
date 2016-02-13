package com.surveyapp.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import com.surveyapp.Fragments.LauncherSplash;
import com.surveyapp.R;
import com.surveyapp.SharedPrefUtil;

public class ActivityLoginSignUp extends AppCompatActivity {

    public static Toolbar toolbar;
    private SharedPrefUtil sharedPrefUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_sign_up);

        sharedPrefUtil = new SharedPrefUtil(ActivityLoginSignUp.this);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (sharedPrefUtil.getLoggedInStatus()){
            Intent intent = new Intent(ActivityLoginSignUp.this,LandingActivity.class);
            startActivity(intent);

            finish();
        }
        else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container_login_sign_up, new LauncherSplash())
                    .commit();
        }
    }

}
