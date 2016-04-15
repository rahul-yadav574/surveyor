package com.surveyapp.Activities;

import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.surveyapp.Adapters.ShowResultAdapter;
import com.surveyapp.CustomObjects.FilledChoiceQuestion;
import com.surveyapp.CustomObjects.Result;
import com.surveyapp.CustomObjects.ResultChoice;
import com.surveyapp.R;
import com.surveyapp.Utilities.DividerItemDecoration;
import com.surveyapp.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShowResultsActivity extends AppCompatActivity {


    private String surveyTitle;
    private String surveyID;
    private TextView questionStatement;
    private RecyclerView resultChoiceRecyclerView;
    private Button nextQues;
    private Button prevQues;
    private ShowResultAdapter adapter;
    private List<Result> resultList;
    private int currentQuestion = 1;
    private Toolbar toolbar;
    private TextView helperQuestionText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_results);

        Bundle extras = getIntent().getExtras();

        this.surveyID = extras.getString("surveyID");
        this.surveyTitle = extras.getString("surveyTitle");

        resultList = new ArrayList<>();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Survey Results");
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        questionStatement = (TextView) findViewById(R.id.showResultQuestionStatement);
        resultChoiceRecyclerView = (RecyclerView) findViewById(R.id.showResultQuestionList);
        nextQues = (Button) findViewById(R.id.nextQuestion);
        prevQues = (Button) findViewById(R.id.prevQuestion);
        helperQuestionText = (TextView) findViewById(R.id.questionNumberHelper);
        adapter = new ShowResultAdapter(ShowResultsActivity.this,resultList);
        resultChoiceRecyclerView.setAdapter(adapter);
        resultChoiceRecyclerView.setLayoutManager(new LinearLayoutManager(ShowResultsActivity.this));
        resultChoiceRecyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.dividers)));

        nextQues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadNextQuestion();
            }
        });

        prevQues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadPrevQuestion();
            }
        });


        new GetSurveyDetailsById().execute();
    }

    private void loadPrevQuestion(){
        if (currentQuestion>1){
            currentQuestion -= 1;
            questionStatement.setText(resultList.get(currentQuestion - 1).getQuestionStatement());
            adapter.changeQuestionId(currentQuestion);
            helperQuestionText.setText(currentQuestion + " / " + resultList.size());
        }
    }

    private void loadNextQuestion(){
        if (currentQuestion<resultList.size()){
            currentQuestion += 1;
            questionStatement.setText(resultList.get(currentQuestion - 1).getQuestionStatement());
            adapter.changeQuestionId(currentQuestion);
            helperQuestionText.setText(currentQuestion + " / " + resultList.size());
        }

    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);

    }



    protected class GetSurveyDetailsById extends AsyncTask<String,Void,Void> {

        MaterialDialog progressDialog;

        List<Result> newResultList = new ArrayList<>();
        JSONArray jsonArray;

        boolean isSuccess;
        boolean isNoQuestion;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new MaterialDialog.Builder(ShowResultsActivity.this)
                    .content("Getting Survey Info..")
                    .progress(true,100)
                    .build();

            progressDialog.show();


        }

        @Override
        protected Void doInBackground(String... params) {

            String requestUrl = "http://contactsyncer.com/getquestioninfo.php?surveyID="+ShowResultsActivity.this.surveyID;

            String response = Utils.getStringFromUrl(requestUrl);

            try{
                jsonArray = new JSONArray(response);
            }
            catch (JSONException j){
                j.printStackTrace();
            }

            isSuccess = true;


            if(jsonArray==null){

                isSuccess = false;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Utils.toastL(ShowResultsActivity.this, "Check Internet Connection");
                    }
                });

                ShowResultsActivity.this.finish();         //As We Dont have Any Info Related to the survey
                return null;


            }

            isNoQuestion = !(jsonArray.length()>0);



            for (int i=0;i<jsonArray.length();i++){

                try{
                    newResultList.add(convertDetailsIntoQuestion(jsonArray.getJSONObject(i)));
                }
                catch (JSONException j){
                    j.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            progressDialog.cancel();

            if (isSuccess && !isNoQuestion){

                new GetChoiceListExecutor(newResultList).execute();
                //Here We Need All The Choices And There Related Counts

            }
        }
    }
    private Result convertDetailsIntoQuestion(JSONObject jsonObject) throws JSONException{

        String questionStatement = jsonObject.getString("QText");
        List<ResultChoice> choicesList = new ArrayList<>();

        return new Result(this.surveyID,questionStatement,choicesList); ///-3 is just a random number ....

    }


    protected class GetChoiceListExecutor extends AsyncTask<String,Void,Void>{

        MaterialDialog progressDialog;
        List<Result> newResultList;

        public GetChoiceListExecutor(List<Result> newResultList) {
            this.newResultList = newResultList;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new MaterialDialog.Builder(ShowResultsActivity.this)
                    .content("Getting Survey Results Info...")
                    .progress(true,100)
                    .build();

            progressDialog.show();



        }

        @Override
        protected Void doInBackground(String... params) {

            for (int i=0;i<newResultList.size();i++){
                String requestUrl ="http://contactsyncer.com/surveyresult.php?surveyID="+surveyID;
                String response = Utils.getStringFromUrl(requestUrl);


                JSONArray jsonArray = null ;
                try{
                    jsonArray = new JSONArray(response);
                }
                catch (JSONException j){
                    j.printStackTrace();
                }

                if (jsonArray==null){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Utils.toastL(ShowResultsActivity.this, "Unable To Connect To Host");
                        }
                    });
                }

                try {
                    newResultList.get(i).setChoiceList(convertItemDetails(jsonArray,i+1));

                }catch (JSONException j){
                    j.printStackTrace();
                }



            }


            return null;
        }



        private List<ResultChoice> convertItemDetails(JSONArray jsonArray , int questionIndex)  throws JSONException{

            List<ResultChoice> resultChoices = new ArrayList<>();



            for (int i=0;i<jsonArray.length();i++){


                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Log.e("Thr ", jsonObject.toString());

                int index = Integer.valueOf(jsonObject.getString("QID"));

                if (index==questionIndex){

                    String choiceStatement = jsonObject.getString("Option");
                    String choiceResponseCount = jsonObject.getString("SelectedCount");

                    resultChoices.add(new ResultChoice(choiceStatement,choiceResponseCount));
                }


            }



            return resultChoices;
        }



      /*  private List<String> convertItemDetailsIntoList(JSONArray jsonArray) throws  JSONException{


            List<String> theChoiceList = new ArrayList<>();

            for (int i=0;i<jsonArray.length();i++){
                theChoiceList.add(jsonArray.getJSONObject(i).getString("Option"));
            }

            return theChoiceList;

            //Log.e("length of th list is",""+list.size());
        }*/


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.cancel();


            resultList = newResultList;
            questionStatement.setText(resultList.get(currentQuestion - 1).getQuestionStatement());
            adapter.changeList(newResultList);
            adapter.changeQuestionId(currentQuestion);

            helperQuestionText.setText(currentQuestion + " / " + newResultList.size());


        }
    }



}
