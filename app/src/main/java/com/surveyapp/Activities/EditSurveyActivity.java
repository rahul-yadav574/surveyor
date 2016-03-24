package com.surveyapp.Activities;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.surveyapp.Adapters.ChoicesAdapter;
import com.surveyapp.Adapters.QuestionAdapter;
import com.surveyapp.CustomObjects.Question;
import com.surveyapp.CustomObjects.Survey;
import com.surveyapp.R;
import com.surveyapp.Utilities.DividerItemDecoration;
import com.surveyapp.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EditSurveyActivity extends AppCompatActivity {

    private RecyclerView newSurveyQuestionList;
    private Toolbar toolbar;
    private QuestionAdapter questionAdapter;
    private FloatingActionButton addNewQuestionToSurvey;
    private String surveyTitle;
    private String surveyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_survey);


        surveyId =  getIntent().getExtras().getString("surveyId");
        surveyTitle = getIntent().getExtras().getString("surveyTitle");





      /*  else if(data.getQueryParameter("surveyID")!=null){

            Utils.toastL(EditSurveyActivity.this,"survey "+data.getQueryParameter("surveyID"));
            surveyId = data.getQuery();
            isNewSurvey = false;       //Otherwise there will be a nullPointer Exception below
        }*/


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);



        newSurveyQuestionList = (RecyclerView) findViewById(R.id.newSurveyQuestionList);
        questionAdapter = new QuestionAdapter(EditSurveyActivity.this,new ArrayList<Question>());
        addNewQuestionToSurvey = (FloatingActionButton) findViewById(R.id.addQuestionToNewSurvey);


        newSurveyQuestionList.setLayoutManager(new LinearLayoutManager(EditSurveyActivity.this));
        newSurveyQuestionList.addItemDecoration(new DividerItemDecoration(getDrawable(R.drawable.dividers)));
        newSurveyQuestionList.setAdapter(questionAdapter);

        addNewQuestionToSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(EditSurveyActivity.this)
                        .content("Enter Question Title")
                        .input(null, null, false, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                String questionStatement = input.toString();
                                dialog.cancel();
                                showDialogView(questionStatement);
                            }
                        })
                        .show();
            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_survey, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        if (id==R.id.submitSurvey){
            //Do the Network Calling Here For Submitting the Survey

            if (questionAdapter.getQuestionList().size()==0){
                Utils.toastS(EditSurveyActivity.this,"Add Some Questions To The Survey");
                return true;
            }

            new SubmitSurveyExecutor(surveyTitle,surveyId,questionAdapter.getQuestionList()).execute();

            return true;
        }

        else if (id==android.R.id.home){
            this.finish();
        }
       

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        super.onStart();



    }


    public void showDialogView(final String questionStatement){

        final View dialogView = View.inflate(EditSurveyActivity.this,R.layout.dialog_add_choice_to_questions,new RelativeLayout(EditSurveyActivity.this));


        final MaterialDialog dialog = new MaterialDialog.Builder(EditSurveyActivity.this)
                .customView(dialogView,false)
                .build();

        TextView questionTextView = (TextView) dialogView.findViewById(R.id.questionStatementInChoiceDialog);
        questionTextView.setText(questionStatement);

        final RecyclerView choicesView = (RecyclerView) dialogView.findViewById(R.id.choicesRecyclerview);

        final ChoicesAdapter choicesAdapter = new ChoicesAdapter(new ArrayList<String>());

        choicesView.setLayoutManager(new LinearLayoutManager(EditSurveyActivity.this));
        choicesView.addItemDecoration(new DividerItemDecoration(getDrawable(R.drawable.dividers)));
        choicesView.setAdapter(choicesAdapter);

        Button addNewChoiceButton = (Button) dialogView.findViewById(R.id.addNewChoice);
        Button doneEnteringChoices = (Button) dialogView.findViewById(R.id.doneEnteringChoices);

        addNewChoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(EditSurveyActivity.this)
                        .content("Enter The Choice Text")
                        .input(null, null, false, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                String choiceStatement = input.toString();
                                dialog.cancel();
                                choicesAdapter.addItemToList(choiceStatement);

                            }
                        })
                        .show();
            }
        });


        doneEnteringChoices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (choicesAdapter.getItemCount()<2){
                    Utils.toastL(EditSurveyActivity.this,"Add At Least Two Choices");
                    return;
                }
                questionAdapter.addNewQuestion(new Question(questionStatement,choicesAdapter.getChoicesList()));
                dialog.cancel();
                }
        });


        dialog.show();
    }


    protected class SubmitSurveyExecutor extends AsyncTask<String,Void,Void>{

        MaterialDialog progressDialog;
        String surveyTitle;
        String surveyId;
        List<Question> questionList;
        boolean isSuccess;

        int UPLOADING = 1;
        int total = 0;

        public SubmitSurveyExecutor(String surveyTitle,String surveyId,List<Question> questionList) {
            this.surveyTitle = surveyTitle;
            this.questionList = questionList;
            this.surveyId = surveyId;
            this.total = questionList.size();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new MaterialDialog.Builder(EditSurveyActivity.this)
                    .progress(true, 100)
                    .content("Adding Question " + UPLOADING + " / " + total)
                    .build();

            progressDialog.show();
        }


        @Override
        protected Void doInBackground(String... params) {

          for (int item = 1 ;item<=total;item++) {
              final boolean status = addQuestionToSurvey(item, questionList.get(item - 1), surveyId);

              if (status) {
                  UPLOADING+=1;
                  runOnUiThread(new Runnable() {
                      @Override
                      public void run() {
                          progressDialog.setContent("Adding Question " + UPLOADING + " / " + total);
                      }
                  });

              }
              else{
                  runOnUiThread(new Runnable() {
                      @Override
                      public void run() {
                          Utils.toastL(EditSurveyActivity.this,"Problem Submitting questions ..Try Again");
                      }
                  });

                  isSuccess = false;
                  progressDialog.cancel();
                  return null;
              }

          }

            isSuccess = true;

            progressDialog.cancel();

            /*String requestUrl = "";   //Here The Api Url Will Be Provided For Submiiting the Survey*/
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (isSuccess){

                new MaterialDialog.Builder(EditSurveyActivity.this)
                        .content("Your Survey Is SuccessFully Submitted")
                        .positiveText("OK")
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                super.onPositive(dialog);

                                dialog.cancel();

                                startActivity(new Intent(EditSurveyActivity.this,LandingActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
                            }
                        })
                .show();
            }


        }
    }

    private boolean addQuestionToSurvey(int index,Question question,String surveyId){


        JSONObject jsonObject = new JSONObject();

        try{
            jsonObject.put("surveyID",surveyId);
            jsonObject.put("qID",index);
            jsonObject.put("qTitle",question.getQuestionStatement());
            jsonObject.put("maxOptions",question.getChoicesList().size());
            jsonObject.put("options",question.getChoicesList());
        }
        catch (JSONException j){
            j.printStackTrace();
        }


        //String completeUrl = "http://contactsyncer.com/questioninfo.php?surveyId="+surveyId+"&qID="+index+"&maxOptions="+question.getChoicesList().size()+"&options="+question.getChoicesList().toString();

        String completeUrl = "http://contactsyncer.com/questioninfo.php";
        JSONObject response = Utils.postJSONObject(completeUrl, jsonObject);

        return response!=null;
    }

}
