package com.surveyapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.surveyapp.R;

/**
 * Created by abhey singh on 01-02-2016.
 */
public class FragmentMySurvey extends Fragment {

    private RecyclerView mySurveyList;
    private FloatingActionButton addNewSurveyButton;

    public FragmentMySurvey() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_surveys,container,false);

        mySurveyList = (RecyclerView) rootView.findViewById(R.id.mySurveysList);
        addNewSurveyButton = (FloatingActionButton) rootView.findViewById(R.id.addNewSurveyButton);

        return rootView;
    }
}
