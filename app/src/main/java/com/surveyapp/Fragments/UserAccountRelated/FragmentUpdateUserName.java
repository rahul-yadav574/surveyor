package com.surveyapp.Fragments.UserAccountRelated;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.surveyapp.R;

/**
 * Created by Rahul Yadav on 06-02-2016.
 */
public class FragmentUpdateUserName extends Fragment {

    private AutoCompleteTextView newUsernameInput;
    private Button updateUserNameButton;


    public FragmentUpdateUserName() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_update_username,container,false);

        newUsernameInput = (AutoCompleteTextView) rootView.findViewById(R.id.updateUserNameInput);
        updateUserNameButton = (Button) rootView.findViewById(R.id.updateUserNameButton);

        updateUserNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUpdateUserNameProcess();
            }
        });


        return rootView;
    }

    private void startUpdateUserNameProcess(){

        /*Do the Stuff here*/

    }

}
