package com.surveyapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.surveyapp.CustomObjects.TemplateSurveyObject;
import com.surveyapp.Adapters.TemplateSurveyListAdapter;
import com.surveyapp.R;

import java.util.ArrayList;

/**
 * Created by Rahul Yadav on 02-02-2016.
 */
public class TabsCustomerFeedback extends Fragment {

    private ListView templatesListView;
    private TemplateSurveyListAdapter adapter;

    public TabsCustomerFeedback() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_template_survey,container,false);

        templatesListView = (ListView) rootView.findViewById(R.id.templatesListView);
        adapter = new TemplateSurveyListAdapter(new ArrayList<TemplateSurveyObject>()); //The Parameter Is the List Of The Templates
        templatesListView.setAdapter(adapter);

        return rootView;
    }
}
