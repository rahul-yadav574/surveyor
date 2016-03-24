package com.surveyapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.surveyapp.CustomObjects.FilledChoiceQuestion;
import com.surveyapp.CustomObjects.Question;
import com.surveyapp.R;
import com.surveyapp.Utils;

import java.util.List;

/**
 * Created by Rahul Yadav on 23-03-2016.
 */
public class FilledChoicesAdapter extends RecyclerView.Adapter<FilledChoicesAdapter.FilledChoiceViewHolder> {


    private List<FilledChoiceQuestion> choicesList;
    private int questionID;
    private Context context;


    public FilledChoicesAdapter(Context context , List<FilledChoiceQuestion> choicesList) {
        this.choicesList = choicesList;
        this.context = context;
    }

    @Override
    public FilledChoiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FilledChoiceViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_fill_choices,parent,false));
    }

    @Override
    public void onBindViewHolder(final FilledChoiceViewHolder holder, final int position) {

        if (choicesList.size() > 0) {
            holder.choiceStatement.setText(choicesList.get(questionID - 1).getChoicesList().get(position));

            holder.selectChoiceButton.setChecked(false);

            if (choicesList.get(questionID-1).getChoiceNo()==position){
                holder.selectChoiceButton.setChecked(true);
            }

            holder.selectChoiceButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    choicesList.get(questionID-1).setIsFilled(true);
                    choicesList.get(questionID-1).setChoiceNo(position);
                    notifyDataSetChanged();
                }
            });
        }

        else {
            //show a toast here
        }
    }

    @Override
    public int getItemCount() {
        if(choicesList.size()>0){
            return choicesList.get(questionID-1).getChoicesList().size();}
        return choicesList.size();
    }

    protected class FilledChoiceViewHolder extends RecyclerView.ViewHolder{

        private RadioButton selectChoiceButton;
        private TextView choiceStatement;

        public FilledChoiceViewHolder(View itemView) {
            super(itemView);

            selectChoiceButton = (RadioButton) itemView.findViewById(R.id.selectChoiceRadioButton);
            choiceStatement = (TextView) itemView.findViewById(R.id.choiceStatementFilled);
        }
    }

    public void changeQuestionId(int questionID){
        this.questionID = questionID;
        notifyDataSetChanged();
    }
    public void changeList(List<FilledChoiceQuestion> choicesList){
        this.choicesList = choicesList;
        notifyDataSetChanged();
    }

    public List<FilledChoiceQuestion> getTheList(){
        return this.choicesList;
    }


}
