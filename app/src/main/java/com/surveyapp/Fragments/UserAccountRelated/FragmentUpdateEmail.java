package com.surveyapp.Fragments.UserAccountRelated;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
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
public class FragmentUpdateEmail extends Fragment {

    private AutoCompleteTextView newEmailInput;
    private Button updateEmailButton;

    public FragmentUpdateEmail() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_update_email,container,false);

        newEmailInput = (AutoCompleteTextView)rootView.findViewById(R.id.updateEmailInput);
        updateEmailButton = (Button)rootView.findViewById(R.id.updateEmailIdButton);

        updateEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUpdateEmailProcess();
            }
        });

        return rootView;
    }

    private void startUpdateEmailProcess(){

        String newEmail = newEmailInput.getText().toString();


    }
}
