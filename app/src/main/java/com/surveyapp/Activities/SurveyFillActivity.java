package com.surveyapp.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.surveyapp.Adapters.FilledChoicesAdapter;
import com.surveyapp.AppContext;
import com.surveyapp.Constants;
import com.surveyapp.CustomObjects.FilledChoiceQuestion;
import com.surveyapp.R;
import com.surveyapp.SharedPrefUtil;
import com.surveyapp.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SurveyFillActivity extends AppCompatActivity {


    private String surveyId;
    private TextView questionStatement;
    private FilledChoicesAdapter adapter;
    private List<FilledChoiceQuestion> questionList;
    private int currentQuestion = 1;
    private TextView helperQuestionText;
    private SharedPrefUtil sharedPrefUtil;
    public  int RC_LOGIN = 574;
    private MaterialDialog imageShowDialog;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_fill);

        sharedPrefUtil = new SharedPrefUtil(SurveyFillActivity.this);

        imageLoader = AppContext.imageLoader;

        AppContext.getInstance().initImageLoader(SurveyFillActivity.this);  //Initialising The ImageLoader

        //just a check we found on stack overflow for adapter problem we were facing...

        this.options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.abc_ic_ab_back_mtrl_am_alpha)
                .showImageOnFail(R.drawable.abc_textfield_activated_mtrl_alpha)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();




        Intent intent = getIntent();
        String data = intent.getDataString();

        if (Intent.ACTION_VIEW.equals(intent.getAction()) && data!=null){
            this.surveyId = data.substring(data.lastIndexOf("/")+1);
        }

      /*  if (!sharedPrefUtil.getLoggedInStatus()){

            //User Is Not Logged In We Have To Find The UserID To Successfully Submit the Survey

            Intent loginUser = new Intent(SurveyFillActivity.this , ActivityLoginSignUp.class);
            loginUser.setAction(Constants.LOGIN_ACTION_FOR_FILL_SURVEY);
            startActivityForResult(loginUser, RC_LOGIN);
        }*/

        questionList = new ArrayList<>();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        setTitle("Fill Survey");
        questionStatement = (TextView) findViewById(R.id.fillQuestionStatement);
        helperQuestionText = (TextView) findViewById(R.id.questionNumberHelper);
        RecyclerView choicesRecyclerView = (RecyclerView) findViewById(R.id.fillQuestionChoicesList);
        adapter = new FilledChoicesAdapter(SurveyFillActivity.this,questionList);
        choicesRecyclerView.setLayoutManager(new LinearLayoutManager(SurveyFillActivity.this));
        choicesRecyclerView.setAdapter(adapter);
        Button loadNextQuestion = (Button) findViewById(R.id.nextQuestion);
        Button loadPrevQuestion = (Button) findViewById(R.id.prevQuestion);

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

        getMenuInflater().inflate(R.menu.menu_edit_survey, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id==android.R.id.home){
            this.finish();
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.submitSurvey) {

            new SubmitFilledSurvey().execute();

            return true;
        }

        else if (id==R.id.showImageOfQuestion){

            imageShowDialog = new MaterialDialog.Builder(SurveyFillActivity.this)
                    .customView(getCustomImageShowView(),false)
                    .build();

            showImageDialog();
            return true;
        }



        return super.onOptionsItemSelected(item);
    }

    private void showImageDialog(){

        if (imageShowDialog!=null && !(imageShowDialog.isShowing())){
            imageShowDialog.show();
        }
    }

    private void hideImageDialog(){

        if (imageShowDialog!=null && imageShowDialog.isShowing()){
            imageShowDialog.hide();
        }

    }
    private View getCustomImageShowView(){

        View dialogView = View.inflate(SurveyFillActivity.this,R.layout.dialog_show_image,new LinearLayout(SurveyFillActivity.this));

        final ProgressBar progressBar = (ProgressBar) dialogView.findViewById(R.id.imageLoadingProgressBar);
        ImageView imageOfQuestion = (ImageView) dialogView.findViewById(R.id.imageOfQuestion);

        imageLoader.displayImage(Constants.IMAGE_ACCESS_URL + surveyId + "QUES" + currentQuestion+".png", imageOfQuestion, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                hideImageDialog();
                Utils.toastS(SurveyFillActivity.this,"No Image Available");
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });


        return dialogView;
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

        return new FilledChoiceQuestion(false,questionStatement,choicesList,-3); ///-3 is just a random number ....

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
                    newQuestionList.get(i).setChoicesList(convertItemDetailsIntoList(jsonArray));}
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


    private List<String> convertItemDetailsIntoList(JSONArray jsonArray) throws  JSONException{


        List<String> theChoiceList = new ArrayList<>();

        for (int i=0;i<jsonArray.length();i++){
            theChoiceList.add(jsonArray.getJSONObject(i).getString("Option"));
        }

        return theChoiceList;

        //Log.e("length of th list is",""+list.size());
    }

    protected class SubmitFilledSurvey extends AsyncTask<String,Void,String>{

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
        protected String doInBackground(String... params) {

            String requestUrl = "http://contactsyncer.com/submitsurvey.php";

            JSONObject jsonObject = new JSONObject();

            int userId ;

            try
            {
                userId = sharedPrefUtil.getUserInfo().getId();
            }

            catch (NullPointerException e){

                userId = -1;
                e.printStackTrace();
            }


            try{

                jsonObject.put("userID",userId);
                jsonObject.put("surveyID",surveyId);
                jsonObject.put("qID",getSendQuesArray());
                jsonObject.put("optionselected",getSendChoicesArray());}
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

            Log.e("submitted", "" + response);

            return response;
        }


        @Override
        protected void onPostExecute(final String s) {
            super.onPostExecute(s);

            progressDialog.cancel();

            if (s!=null){

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Utils.toastS(SurveyFillActivity.this, "Your Responses Are Succesfully Stored");

                    }
                });


                SurveyFillActivity.this.finish();
            }

            else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Utils.toastS(SurveyFillActivity.this, "Error in Submitting Responses .....Try Again");

                    }
                });
            }


        }
    }



}
