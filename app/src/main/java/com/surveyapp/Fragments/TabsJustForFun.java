package com.surveyapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.surveyapp.R;

/**
 * Created by Rahul Yadav on 02-02-2016.
 */
public class TabsJustForFun extends Fragment {

    private RecyclerView templatesRecyclerView;

    public TabsJustForFun() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_template_survey,container,false);

        templatesRecyclerView = (RecyclerView) rootView.findViewById(R.id.templatesRecyclerView);

        return rootView;
    }
}
