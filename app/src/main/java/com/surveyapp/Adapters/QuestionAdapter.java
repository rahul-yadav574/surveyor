package com.surveyapp.Adapters;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.surveyapp.Activities.EditSurveyActivity;
import com.surveyapp.CustomObjects.Question;
import com.surveyapp.R;
import com.surveyapp.Utilities.DividerItemDecoration;
import com.surveyapp.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rahul Yadav on 05-03-2016.
 */
public class QuestionAdapter extends RecyclerView.Adapter <QuestionAdapter.QuestionViewHolder>{

    private List<Question> questionList;
    private Context context;

    public QuestionAdapter(Context context,List<Question> questionList) {
        this.context = context;
        this.questionList = questionList;
    }

    @Override
    public void onBindViewHolder(final QuestionViewHolder holder, final int position) {
        holder.questionStatement.setText(questionList.get(position).getQuestionStatement());
        holder.totalChoices.setText(questionList.get(position).getChoicesList().size()+" choices");
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startEditingQuestionProcess(questionList.get(position),position);
            }
        });
    }

    @Override
    public QuestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new QuestionViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_question_row,parent,false));
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public void addNewQuestion(Question newQuestion){
        this.questionList.add(newQuestion);
        this.notifyDataSetChanged();
    }
    protected class QuestionViewHolder extends RecyclerView.ViewHolder{

        private TextView questionStatement;
        private TextView totalChoices;
        private LinearLayout mainLayout;

        public QuestionViewHolder(View itemView) {
            super(itemView);

            questionStatement = (TextView) itemView.findViewById(R.id.questionStatement);
            totalChoices = (TextView) itemView.findViewById(R.id.totalChoices);
            mainLayout = (LinearLayout) itemView.findViewById(R.id.mainLayout);
        }
    }

    public List<Question> getQuestionList(){
        return this.questionList;
    }

    private void startEditingQuestionProcess(final Question question, final int position){

        View dialogView = View.inflate(context, R.layout.dialog_edit_survey, new RelativeLayout(context));


         final MaterialDialog dialog = new MaterialDialog.Builder(context)
                 .customView(dialogView,false)
                 .build();

        final TextView questionStatement = (TextView) dialogView.findViewById(R.id.questionStatementInChoiceDialog);
        ImageButton editQuestionStatement = (ImageButton) dialogView.findViewById(R.id.changeQuestionStatement);

        Button addNewChoice = (Button) dialogView.findViewById(R.id.editAddNewChoice);
        Button doneEditing = (Button) dialogView.findViewById(R.id.editDoneEnteringChoices);

        RecyclerView editChoicesRecyclerView = (RecyclerView) dialogView.findViewById(R.id.choicesEditRecyclerview);
        editChoicesRecyclerView.addItemDecoration(new DividerItemDecoration(context.getDrawable(R.drawable.dividers)));
        final EditChoiceAdapter adapter = new EditChoiceAdapter(context,question.getChoicesList());

        editChoicesRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        editChoicesRecyclerView.setAdapter(adapter);

        questionStatement.setText(question.getQuestionStatement());
        editQuestionStatement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(context)
                        .content("Enter Updated Question")
                        .input(null, null, false, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                questionStatement.setText(input.toString());
                                questionList.get(position).setQuestionStatement(input.toString());
                                notifyDataSetChanged();
                            }
                        })
                        .show();
            }
        });

        addNewChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(context)
                        .content("Enter The Choice Text")
                        .input(null, null, false, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                String choiceStatement = input.toString();
                                dialog.cancel();
                                adapter.addChoiceToList(choiceStatement);

                            }
                        })
                        .show();
            }
        });

        doneEditing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionList.get(position).setChoicesList(adapter.getChoiceList());
                notifyDataSetChanged();
                dialog.cancel();
            }
        });



        dialog.show();


    }
}

