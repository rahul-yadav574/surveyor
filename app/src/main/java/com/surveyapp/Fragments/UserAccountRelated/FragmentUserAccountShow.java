package com.surveyapp.Fragments.UserAccountRelated;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.surveyapp.R;
import com.surveyapp.SharedPrefUtil;

/**
 * Created by Rahul Yadav on 06-02-2016.
 */
public class FragmentUserAccountShow extends Fragment {

    private ListView userCredentialsListView;
    private ListView userResponseListView;
    private Button logOutButton;
    private SharedPrefUtil sharedPrefUtil;

    public FragmentUserAccountShow() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.sharedPrefUtil = new SharedPrefUtil(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_account,container,false);

        userCredentialsListView = (ListView)  rootView.findViewById(R.id.userCredentialsListView);
        userResponseListView  = (ListView) rootView.findViewById(R.id.newResponsesListView);
        logOutButton = (Button) rootView.findViewById(R.id.logOutButton);

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*Here The User Will be log Out From The App And The First Page Will Be Show*/
            }
        });

        return rootView;
    }
}
