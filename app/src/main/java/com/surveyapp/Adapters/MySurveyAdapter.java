package com.surveyapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.surveyapp.Activities.EditSurveyActivity;
import com.surveyapp.CustomObjects.Survey;
import com.surveyapp.R;

import java.util.List;

/**
 * Created by Rahul Yadav on 17-03-2016.
 */
public class MySurveyAdapter  extends RecyclerView.Adapter<MySurveyAdapter.MySurveyViewHolder>{


    private List<Survey> mySurveyList;
    private Context context;
    public MySurveyAdapter(Context context , List<Survey> mySurveyList) {
        this.mySurveyList = mySurveyList;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return mySurveyList.size();
    }

    @Override
    public void onBindViewHolder(MySurveyViewHolder holder, final int position) {

        holder.surveyTitle.setText(mySurveyList.get(position).getSurveyTitle());
        holder.dateCreated.setText(mySurveyList.get(position).getDateCreated());

     /*   holder.mainContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startEditSurveyProcess(position);
            }
        });*/

        holder.mainContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startShareIntentProcess(position);
                return true;
            }
        });

    }

    @Override
    public MySurveyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MySurveyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_my_survey_row,parent,false));
    }

    protected class MySurveyViewHolder extends RecyclerView.ViewHolder{

        private TextView surveyTitle;
        private TextView dateCreated;
        private LinearLayout mainContainer;

        public MySurveyViewHolder(View itemView) {
            super(itemView);

            surveyTitle = (TextView) itemView.findViewById(R.id.mySurveyTitle);
            dateCreated = (TextView) itemView.findViewById(R.id.mSurveyDateCreated);
            mainContainer = (LinearLayout) itemView.findViewById(R.id.mainContainer);

        }
    }

    public void changeList(List<Survey> mySurveyList ){
        this.mySurveyList = mySurveyList;
        notifyDataSetChanged();
    }

    public void startShareIntentProcess(int position){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT , "http://participateme.com/"+mySurveyList.get(position).getSurveyID());
        shareIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(shareIntent,"Share My Survey Via : "));
    }

   /* public void startEditSurveyProcess(int position){

        Bundle extraBundle = new Bundle();
        extraBundle.putString("surveyTitle",mySurveyList.get(position).getSurveyTitle());
        extraBundle.putString("surveyId",mySurveyList.get(position).getSurveyID());


        Intent editSurvey = new Intent(context, EditSurveyActivity.class);
        editSurvey.putExtras(extraBundle);
        context.startActivity(editSurvey);
    }*/
}
