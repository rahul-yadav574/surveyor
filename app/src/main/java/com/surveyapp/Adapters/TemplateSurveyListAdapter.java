package com.surveyapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.surveyapp.CustomObjects.TemplateSurveyObject;
import com.surveyapp.R;

import java.util.List;

/**
 * Created by Rahul Yadav on 02-02-2016.
 */
public class TemplateSurveyListAdapter extends BaseAdapter{

    private List<TemplateSurveyObject> templateSurveyList;


    public TemplateSurveyListAdapter(List<TemplateSurveyObject> templateSurveyList) {
        this.templateSurveyList = templateSurveyList;
    }

    @Override
    public int getCount() {
        return templateSurveyList.size();
    }

    @Override
    public Object getItem(int position) {
        return templateSurveyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View customView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_item_template_survey,parent,false);

        TextView templateSurveyTitle = (TextView) customView.findViewById(R.id.templateSurveyTitle);
        TextView templateSurveyContent = (TextView) customView.findViewById(R.id.templateSurveyContent);
        TextView templateSurveyInfo = (TextView) customView.findViewById(R.id.templateSurveyDetails);

        templateSurveyTitle.setText(templateSurveyList.get(position).getTitle());
        templateSurveyContent.setText(templateSurveyList.get(position).getContent());
        templateSurveyInfo.setText(templateSurveyList.get(position).getInfo());

        return customView;
    }
}
