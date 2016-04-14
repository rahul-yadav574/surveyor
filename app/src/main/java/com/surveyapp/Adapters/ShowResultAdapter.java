package com.surveyapp.Adapters;

import android.content.ContentValues;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.surveyapp.CustomObjects.FilledChoiceQuestion;
import com.surveyapp.CustomObjects.Result;
import com.surveyapp.R;

import java.util.List;

/**
 * Created by Rahul Yadav on 26-03-2016.
 */
public class ShowResultAdapter extends RecyclerView.Adapter<ShowResultAdapter.ShowResultViewHolder> {


    private List<Result> resultsQuestionsList;
    private Context context;
    private int questionID;

    public ShowResultAdapter(Context context , List<Result> resultsQuestionsList) {
        this.resultsQuestionsList = resultsQuestionsList;
        this.context = context;
    }


    @Override
    public void onBindViewHolder(ShowResultViewHolder holder, int position) {

        if (resultsQuestionsList.size()>0) {

            holder.choiceStatement.setText(resultsQuestionsList.get(questionID - 1).getChoiceList().get(position).getChoiceStatement());
            holder.choicesResponseCount.setText(resultsQuestionsList.get(questionID - 1).getChoiceList().get(position).getChoiceResponseCount());

        }
    }

    @Override
    public ShowResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ShowResultViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_result_show,parent,false));
    }

    @Override
    public int getItemCount() {

        if(resultsQuestionsList.size()>0){
            return resultsQuestionsList.get(questionID-1).getChoiceList().size();}
        return resultsQuestionsList.size();
    }

    protected class ShowResultViewHolder extends RecyclerView.ViewHolder{

        private TextView choiceStatement;
        private TextView choicesResponseCount;
        private ProgressBar progressBar;

        public ShowResultViewHolder(View itemView) {
            super(itemView);

            choiceStatement = (TextView) itemView.findViewById(R.id.choiceStatementResult);
            choicesResponseCount = (TextView) itemView.findViewById(R.id.choicesResponsesCount);
        }
    }

    public void changeQuestionId(int questionID){
        this.questionID = questionID;
        notifyDataSetChanged();
    }
    public void changeList(List<Result> resultsQuestionsList){
        this.resultsQuestionsList = resultsQuestionsList;
        notifyDataSetChanged();
    }

}
