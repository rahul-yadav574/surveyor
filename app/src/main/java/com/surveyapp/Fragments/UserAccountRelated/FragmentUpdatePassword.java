package com.surveyapp.Fragments.UserAccountRelated;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.surveyapp.Activities.LandingActivity;
import com.surveyapp.R;

/**
 * Created by Rahul Yadav on 06-02-2016.
 */
public class FragmentUpdatePassword extends Fragment {

    private TextInputLayout oldPasswordInputLayout;
    private TextInputLayout newPasswordInputLayout;
    private TextInputLayout confirmNewPasswordInputLayout;
    private AutoCompleteTextView oldPasswordInput;
    private AutoCompleteTextView newPasswordInput;
    private AutoCompleteTextView confirmNewPasswordInput;
    private Button updatePasswordButton;

    public FragmentUpdatePassword() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_update_password,container,false);

        oldPasswordInputLayout = (TextInputLayout) rootView.findViewById(R.id.updatePasswordOldInputLayout);
        newPasswordInputLayout = (TextInputLayout) rootView.findViewById(R.id.updatePasswordNewInputLayout);
        confirmNewPasswordInputLayout = (TextInputLayout) rootView.findViewById(R.id.updatePasswordConfirmInputLayout);
        oldPasswordInput = (AutoCompleteTextView) rootView.findViewById(R.id.updatePasswordOldInput);
        newPasswordInput = (AutoCompleteTextView) rootView.findViewById(R.id.updatePasswordNewInput);
        confirmNewPasswordInput = (AutoCompleteTextView) rootView.findViewById(R.id.updatePasswordConfirmInput);
        updatePasswordButton = (Button) rootView.findViewById(R.id.updatePasswordButton);

        oldPasswordInput.addTextChangedListener(new MyTextWatcher(oldPasswordInput));
        newPasswordInput.addTextChangedListener(new MyTextWatcher(newPasswordInput));
        confirmNewPasswordInput.addTextChangedListener(new MyTextWatcher(confirmNewPasswordInput));

        updatePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUpdatePasswordProcess();
            }
        });
        return rootView;
    }

    private void startUpdatePasswordProcess(){

        if (!validateOldPassword()) {
            return;
        }

        if (!validateNewPassword()) {
            return;
        }

        if (!validateConfirmPassword()){
            return;
        }

        //Do The Stuff Here

    }

    private class MyTextWatcher implements TextWatcher {

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
                case R.id.updatePasswordOldInput:
                   validateOldPassword();
                    break;
                case R.id.updatePasswordNewInput:
                    validateNewPassword();
                    break;
                case R.id.updatePasswordConfirmInput:
                    validateConfirmPassword();
                    break;
            }
        }
    }

    private boolean validateOldPassword(){

        if (!(oldPasswordInput.getText().toString().trim().length()>0)){
            oldPasswordInputLayout.setError(getString(R.string.err_update_password_old));
            requestFocus(oldPasswordInput);
            return false;}
        else {
            oldPasswordInputLayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateNewPassword(){
        String oldPassword = oldPasswordInput.getText().toString();
        String newPassword = newPasswordInput.getText().toString();
        if (newPassword.matches(oldPassword) || !(newPassword.trim().length()>0)){
            newPasswordInputLayout.setError(getString(R.string.err_update_password_new));
            requestFocus(newPasswordInput);
            return false;
        }

        else {
            newPasswordInputLayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateConfirmPassword(){

        String newPassword = newPasswordInput.getText().toString();
        String confirmPassword = confirmNewPasswordInput.getText().toString();

        if (!confirmPassword.matches(newPassword)){
            confirmNewPasswordInputLayout.setError(getString(R.string.err_update_password_confirm));
            requestFocus(confirmNewPasswordInput);
            return false;
        }

        else {
            confirmNewPasswordInputLayout.setErrorEnabled(false);
        }

        return true;

    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

}
