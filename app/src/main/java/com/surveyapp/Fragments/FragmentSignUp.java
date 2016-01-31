package com.surveyapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.surveyapp.Activities.ActivityLoginSignUp;
import com.surveyapp.R;

/**
 * Created by on 31-01-2016.
 */
public class FragmentSignUp extends Fragment {

    public FragmentSignUp() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.btn_sign_up);
        ActivityLoginSignUp.toolbar.setVisibility(View.VISIBLE);
        ActivityLoginSignUp.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        this.setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_signup,container,false);

        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            getActivity().getSupportFragmentManager().popBackStack();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
