package com.surveyapp.Fragments.UserAccountRelated;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.surveyapp.Activities.ActivityLoginSignUp;
import com.surveyapp.Adapters.UserAccountListAdapter;
import com.surveyapp.AppContext;
import com.surveyapp.Constants;
import com.surveyapp.R;
import com.surveyapp.SharedPrefUtil;
import com.surveyapp.Utilities.GoogleUtility;

import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rahul Yadav on 06-02-2016.
 */
public class FragmentUserAccountShow extends Fragment {

    private ListView userCredentialsListView;
    private ListView userResponseListView;
    private Button logOutButton;
    private SharedPrefUtil sharedPrefUtil;
    private UserAccountListAdapter adapter;

    public FragmentUserAccountShow() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Account");
        this.sharedPrefUtil = new SharedPrefUtil(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_account,container,false);

        userCredentialsListView = (ListView)  rootView.findViewById(R.id.userCredentialsListView);
        userResponseListView  = (ListView) rootView.findViewById(R.id.newResponsesListView);
        logOutButton = (Button) rootView.findViewById(R.id.logOutButton);

        List<UserAccountListAdapter.UserAccountObject> accountObjects = new ArrayList<>(4);
        accountObjects.add(new UserAccountListAdapter.UserAccountObject("Username",sharedPrefUtil.getUserInfo().getName()));
        accountObjects.add(new UserAccountListAdapter.UserAccountObject("Email",sharedPrefUtil.getUserInfo().getEmail()));
        accountObjects.add(new UserAccountListAdapter.UserAccountObject("Password",sharedPrefUtil.getUserInfo().getPassword()));
        accountObjects.add(new UserAccountListAdapter.UserAccountObject("Plan", sharedPrefUtil.getUserInfo().getPlan()));
        adapter = new UserAccountListAdapter(getActivity(),accountObjects);

        userCredentialsListView.setAdapter(adapter);

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sharedPrefUtil.clearUserSession();
                Intent redirectToLogin = new Intent(getActivity(), ActivityLoginSignUp.class);
                redirectToLogin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(redirectToLogin);

                /*Here The User Will be log Out From The App And The First Page Will Be Show*/
            }
        });

        return rootView;
    }
}
