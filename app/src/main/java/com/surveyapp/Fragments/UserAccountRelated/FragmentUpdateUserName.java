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
import com.surveyapp.SharedPrefUtil;
import com.surveyapp.Utils;

/**
 * Created by Rahul Yadav on 06-02-2016.
 */
public class FragmentUpdateUserName extends Fragment {

    private AutoCompleteTextView newUsernameInput;
    private Button updateUserNameButton;
    private SharedPrefUtil sharedPrefUtil;


    public FragmentUpdateUserName() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPrefUtil = new SharedPrefUtil(getActivity());
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

        String newUserName = newUsernameInput.getText().toString();

        if (newUserName.length()==0){
            if (newUserName.matches(sharedPrefUtil.getUserInfo().getName())){
                Utils.toastS(getActivity(),"New Name Should Be Different");
                return;
            }
            Utils.toastS(getActivity(),"Name Is Not Valid");
            return;
        }


         /*Do the Stuff here*/

    }

}
