package com.surveyapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.surveyapp.Activities.ActivityLoginSignUp;
import com.surveyapp.R;

/**
 * Created by Rahul Yadav on 31-01-2016.
 */
public class LauncherSplash extends Fragment {

    private Button startLoginFragment;
   // private Button startSignUpFragment;

    public LauncherSplash() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLoginSignUp.toolbar.setVisibility(View.GONE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_launcher,container,false);

        startLoginFragment = (Button) rootView.findViewById(R.id.startLoginFragment);
       // startSignUpFragment = (Button) rootView.findViewById(R.id.startSignUpFragment);

        startLoginFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFragmentProcess(new FragmentLogin());
            }
        });

      /*  startSignUpFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFragmentProcess(new FragmentSignUp());
            }
        });*/

        return rootView;
    }

    private void startFragmentProcess(Fragment fragment){
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container_login_sign_up, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        ActivityLoginSignUp.toolbar.setVisibility(View.GONE);

    }
}
