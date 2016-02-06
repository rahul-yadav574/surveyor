package com.surveyapp.Fragments.UserAccountRelated;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.surveyapp.R;

/**
 * Created by Rahul Yadav on 06-02-2016.
 */
public class FragmentUserAccountShow extends Fragment {

    public FragmentUserAccountShow() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_account,container,false);

        return rootView;
    }
}
