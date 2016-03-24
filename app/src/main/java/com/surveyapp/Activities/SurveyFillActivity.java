package com.surveyapp.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.surveyapp.Adapters.FilledChoicesAdapter;
import com.surveyapp.Constants;
import com.surveyapp.CustomObjects.FilledChoiceQuestion;
import com.surveyapp.R;
import com.surveyapp.SharedPrefUtil;
import com.surveyapp.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SurveyFillActivity extends AppCompatActivity {


    private String surveyId;
    private TextView questionStatement;
    private RecyclerView choicesRecyclerView;
    private FilledChoicesAdapter adapter;
    private Button loadNextQuestion;
    private Button loadPrevQuestion;
    private List<FilledChoiceQuestion> questionList;
    private int currentQuestion = 1;
    private Toolbar toolbar;
    private TextView helperQuestionText;
    private SharedPrefUtil sharedPrefUtil;
    public  int RC_LOGIN = 574;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_fill);

        sharedPrefUtil = new SharedPrefUtil(SurveyFillActivity.this);




        Intent intent = getIntent();
        String data = intent.getDataString();

        if (Intent.ACTION_VIEW.equals(intent.getAction()) && data!=null){
            this.surveyId = data.substring(data.lastIndexOf("/")+1);
        }

        if (!sharedPrefUtil.getLoggedInStatus()){

            //User Is Not Logged In We Have To Find The UserID To Succesfully Submit the Survey

            Intent loginUser = new Intent(SurveyFillActivity.this , ActivityLoginSignUp.class);
            loginUser.setAction(Constants.LOGIN_ACTION_FOR_FILL_SURVEY);
            startActivityForResult(loginUser, RC_LOGIN);
        }

        questionList = new ArrayList<>();

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        setTitle("Fill Survey");
        questionStatement = (TextView) findViewById(R.id.fillQuestionStatement);
        helperQuestionText = (TextView) findViewById(R.id.questionNumberHelper);
        choicesRecyclerView = (RecyclerView) findViewById(R.id.fillQuestionChoicesList);
        adapter = new FilledChoicesAdapter(SurveyFillActivity.this,questionList);
        choicesRecyclerView.setLayoutManager(new LinearLayoutManager(SurveyFillActivity.this));
        choicesRecyclerView.setAdapter(adapter);
        loadNextQuestion = (Button) findViewById(R.id.nextQuestion);
        loadPrevQuestion = (Button) findViewById(R.id.prevQuestion);

        loadPrevQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLoadingPrevQuestion();
            }
        });
        loadNextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLoadingNextQuestion();
            }
        });

        new GetSurveyDetailsById().execute();

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

        if (id==android.R.id.home){
            this.finish();
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.submitSurvey) {

            new SubmitFilledSurvey().execute();

            return true;
        }



        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void startLoadingPrevQuestion(){
        if (currentQuestion>1){
            currentQuestion -= 1;
            questionStatement.setText(questionList.get(currentQuestion - 1).getQuestionStatement());
            adapter.changeQuestionId(currentQuestion);
            helperQuestionText.setText(currentQuestion + " / " + questionList.size());
        }
    }

    private void startLoadingNextQuestion(){
        if (currentQuestion<questionList.size()){
            currentQuestion += 1;
            questionStatement.setText(questionList.get(currentQuestion-1).getQuestionStatement());
            adapter.changeQuestionId(currentQuestion);
            helperQuestionText.setText(currentQuestion + " / " + questionList.size());
        }

    }


    protected class GetSurveyDetailsById extends AsyncTask<String,Void,Void> {

        MaterialDialog progressDialog;

        List<FilledChoiceQuestion> newQuestionList = new ArrayList<>();
        JSONArray jsonArray;

        boolean isSuccess;
        boolean isNoQuestion;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new MaterialDialog.Builder(SurveyFillActivity.this)
                    .content("Getting Survey Info..")
                    .progress(true,100)
                    .build();

            progressDialog.show();


        }

        @Override
        protected Void doInBackground(String... params) {

            String requestUrl = "http://contactsyncer.com/getquestioninfo.php?surveyID="+surveyId;

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
                        Utils.toastL(SurveyFillActivity.this, "Check Internet Connection");
                    }
                });

                SurveyFillActivity.this.finish();         //As We Dont have Any Info Related to the survey
                return null;


            }

            isNoQuestion = !(jsonArray.length()>0);



            for (int i=0;i<jsonArray.length();i++){

                try{
                    newQuestionList.add(convertDetailsIntoQuestion(jsonArray.getJSONObject(i)));
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
                new GetChoiceListExecutor(newQuestionList).execute();

            }
        }
    }
    private FilledChoiceQuestion convertDetailsIntoQuestion(JSONObject jsonObject) throws JSONException{

        String questionStatement = jsonObject.getString("QText");
        List<String> choicesList = new ArrayList<>();

        return new FilledChoiceQuestion(false,questionStatement,choicesList,-3);

    }


    protected class GetChoiceListExecutor extends AsyncTask<String,Void,Void>{

        MaterialDialog progressDialog;
        List<FilledChoiceQuestion> newQuestionList;

        public GetChoiceListExecutor(List<FilledChoiceQuestion> newQuestionList) {
            this.newQuestionList = newQuestionList;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new MaterialDialog.Builder(SurveyFillActivity.this)
                    .content("Getting Survey Info..")
                    .progress(true,100)
                    .build();

            progressDialog.show();



        }

        @Override
        protected Void doInBackground(String... params) {

            for (int i=0;i<newQuestionList.size();i++){
                String requestUrl ="http://contactsyncer.com/optioninfo.php?surveyID="+surveyId+"&qID="+(i+1);
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
                            Utils.toastL(SurveyFillActivity.this, "Unable To Connect To Host");
                        }
                    });
                }

                try{
                    newQuestionList.get(i).setChoicesList(convertItemDetailsIntoList(jsonArray.getJSONObject(0)));}
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


            questionList = newQuestionList;
            questionStatement.setText(questionList.get(currentQuestion - 1).getQuestionStatement());
            adapter.changeList(questionList);
            adapter.changeQuestionId(currentQuestion);

            helperQuestionText.setText(currentQuestion + " / " + questionList.size());


        }
    }

    private List<Integer> getSendQuesArray(){

        List<FilledChoiceQuestion> arr = adapter.getTheList();
        List<Integer> ansList = new ArrayList<>();

        for (int i=0;i<arr.size();i++){
            if (arr.get(i).isFilled()){
                ansList.add(i+1);
            }
        }

        return ansList;

    }

    private List<Integer> getSendChoicesArray(){

        List<FilledChoiceQuestion> arr = adapter.getTheList();
        List<Integer> ansList = new ArrayList<>();

        for (int i=0;i<arr.size();i++){
            if (questionList.get(i).isFilled()){
                ansList.add(questionList.get(i).getChoiceNo()+1);
            }
        }

        return ansList;

    }

    private List<String> convertItemDetailsIntoList(JSONObject jsonObject) throws  JSONException{
        String arrayChoice = jsonObject.getString("Option");
        String newString = arrayChoice.substring(1,arrayChoice.lastIndexOf("]"));
        Log.e("the length is-",""+Arrays.asList(newString.split(",")).size());
        return Arrays.asList(newString.split(","));

        //Log.e("length of th list is",""+list.size());
    }

    protected class SubmitFilledSurvey extends AsyncTask<String,Void,Void>{

        MaterialDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new MaterialDialog.Builder(SurveyFillActivity.this)
                    .content("Submitting Your Filled Choices")
                    .progress(true,100)
                    .build();

            progressDialog.show();
        }


        @Override
        protected Void doInBackground(String... params) {

            String requestUrl = "";

            JSONObject jsonObject = new JSONObject();

            try{
                jsonObject.put("userID",sharedPrefUtil.getUserInfo().getId());
                jsonObject.put("surveyID",surveyId);
                jsonObject.put("qID",getSendQuesArray());
                jsonObject.put("option",getSendChoicesArray());}
            catch (JSONException j){
                j.printStackTrace();
            }

            String response = Utils.postObject(requestUrl,jsonObject);

            if (response == null){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Utils.toastS(SurveyFillActivity.this,"Unable to Submit Survey ...Check Network and Try Again");
                    }
                });

                return null;
            }



            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            progressDialog.cancel();
        }
    }



}
