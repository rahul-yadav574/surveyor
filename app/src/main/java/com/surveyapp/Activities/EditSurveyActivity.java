package com.surveyapp.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import com.surveyapp.Adapters.ChoicesAdapter;
import com.surveyapp.Adapters.QuestionAdapter;
import com.surveyapp.Constants;
import com.surveyapp.CustomObjects.Question;
import com.surveyapp.CustomObjects.Survey;
import com.surveyapp.R;
import com.surveyapp.Utilities.DividerItemDecoration;
import com.surveyapp.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class EditSurveyActivity extends AppCompatActivity {

    private RecyclerView newSurveyQuestionList;
    private Toolbar toolbar;
    private QuestionAdapter questionAdapter;
    private FloatingActionButton addNewQuestionToSurvey;
    private String surveyTitle;
    private String surveyId;
    private MaterialDialog shareDialog;
    private Bitmap bitmap;
    private int PICK_IMAGE_REQUEST = 1;
    private int questionIndex;  //Only For Selecting Image
    private MenuItem submitButton;       //making global just to hide it while taking screenshot
    private TextView customToolBarTitle;
    private ImageView dialogQuestionImage;


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
        setTitle(this.surveyTitle);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);

        customToolBarTitle = (TextView) findViewById(R.id.toolbar_custom_title);
        customToolBarTitle.setText(this.surveyTitle);

        //customToolBarTitle.setText("the title");



        newSurveyQuestionList = (RecyclerView) findViewById(R.id.newSurveyQuestionList);
        questionAdapter = new QuestionAdapter(EditSurveyActivity.this,new ArrayList<Question>());
        addNewQuestionToSurvey = (FloatingActionButton) findViewById(R.id.addQuestionToNewSurvey);


        newSurveyQuestionList.setLayoutManager(new LinearLayoutManager(EditSurveyActivity.this));
        newSurveyQuestionList.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.dividers)));
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

        getMenuInflater().inflate(R.menu.menu_edit_survey, menu);

        MenuItem item = menu.findItem(R.id.showImageOfQuestion);
        item.setVisible(false);


        submitButton = menu.findItem(R.id.submitSurvey);


        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();



        if (id==R.id.submitSurvey){
            //Do the Network Calling Here For Submitting the Survey

            if (questionAdapter.getQuestionList().size()==0){
                Utils.toastS(EditSurveyActivity.this,"Add Some Questions To The Survey");
                return true;
            }

            takeScreenshot();

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data!=null && data.getData()!=null){

            Uri imagePath = data.getData();

            try {
                //Getting the Bitmap from Gallery
                Bitmap  bitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);

                //Scaling the bitmap as it might cause issues OPENGL RENDERING
                Bitmap bitmap1= new BitmapDrawable(getResources() , bitmap2).getBitmap();
                int nh = (int) ( bitmap1.getHeight() * (512.0 / bitmap1.getWidth()) );
                bitmap = Bitmap.createScaledBitmap(bitmap1, 512, nh, true);
                // Log.d(TAG, String.valueOf(bitmap));

                Utils.toastS(EditSurveyActivity.this,"Image Added");
                dialogQuestionImage.setVisibility(View.VISIBLE);
                dialogQuestionImage.setImageBitmap(bitmap);

                //setImageToQuestionList(bitmap);
                //Setting the Bitmap to ImageView
                // imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void showDialogView(final String questionStatement){

        questionIndex = questionAdapter.getQuestionList().size();

        final View dialogView = View.inflate(EditSurveyActivity.this,R.layout.dialog_add_choice_to_questions,new RelativeLayout(EditSurveyActivity.this));


        final MaterialDialog dialog = new MaterialDialog.Builder(EditSurveyActivity.this)
                .customView(dialogView,false)
                .build();

        TextView questionTextView = (TextView) dialogView.findViewById(R.id.questionStatementInChoiceDialog);
        TextView questionImageHint = (TextView) dialogView.findViewById(R.id.questionImageTextInChoiceDialog);
        dialogQuestionImage = (ImageView) dialogView.findViewById(R.id.questionImageView);
        questionTextView.setText(questionStatement);
       // questionImageHint.setText("No Image Added");


        final RecyclerView choicesView = (RecyclerView) dialogView.findViewById(R.id.choicesRecyclerview);

        final ChoicesAdapter choicesAdapter = new ChoicesAdapter(new ArrayList<String>());

        choicesView.setLayoutManager(new LinearLayoutManager(EditSurveyActivity.this));
        choicesView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.dividers)));
        choicesView.setAdapter(choicesAdapter);

        Button addNewChoiceButton = (Button) dialogView.findViewById(R.id.addNewChoice);
        Button doneEnteringChoices = (Button) dialogView.findViewById(R.id.doneEnteringChoices);
        Button addImageToQuestion = (Button) dialogView.findViewById(R.id.addImageToQuestion);

        addImageToQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showFileChooser();


            }
        });

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
                if (choicesAdapter.getItemCount() < 2) {
                    Utils.toastL(EditSurveyActivity.this, "Add At Least Two Choices");
                    return;
                }

                Question question = new Question();
                question.setChoicesList(choicesAdapter.getChoicesList());
                question.setQuestionStatement(questionStatement);
                question.setQuestionImageName(surveyId + "QUES" + (questionAdapter.getQuestionList().size() + 1));
                question.setQuestionImage(getStringImage(bitmap));
                questionAdapter.addNewQuestion(question);
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
              final boolean status = addQuestionToSurvey(item, questionList.get(item - 1), surveyId,questionList.get(item-1).getQuestionImage(),questionList.get(item-1).getQuestionImageName());

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

                shareDialog = new  MaterialDialog.Builder(EditSurveyActivity.this)
                        .customView(getDialogView(),true)
                        .build();

                showShareDialog();
            }


        }
    }

    private boolean addQuestionToSurvey(int index,Question question,String surveyId,String imageUri , String imageName){


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

       // Log.e("response on add survey", "" + response);

        if (question.getQuestionImage()!=null) {


            String requestUrl = Constants.IMAGE_UPLOAD_URL;

            JSONObject jsonObject1 = new JSONObject();

            try {
                jsonObject1.put("image", imageUri);
                jsonObject1.put("name", imageName);
            } catch (JSONException j) {
                j.printStackTrace();
            }

          //  Log.e("imageUri", imageUri);
          //  Log.e("imageName", imageName);

            String response1 = Utils.postObject(requestUrl, jsonObject1);

            Log.e("image uploadresponse", "" + response1);


        }
        return response!=null ;
    }

    private View getDialogView(){

        View dialogView = View.inflate(EditSurveyActivity.this,R.layout.dialog_custom_share_after_survey_submit,new LinearLayout(EditSurveyActivity.this));

        Button shareSurvey = (Button) dialogView.findViewById(R.id.shareSurveyButton);
        Button shareLater = (Button) dialogView.findViewById(R.id.shareSurveyLaterButton);

        shareLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideShareDialog();

                startActivity(new Intent(EditSurveyActivity.this,LandingActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));

            }
        });

        shareSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditSurveyActivity.this.finish();
                startShareIntentProcess();
            }
        });


        return dialogView;
    }

    private void showShareDialog(){
        if (shareDialog!=null && !shareDialog.isShowing()){
            shareDialog.show();
        }
    }

    private void hideShareDialog(){

        if (shareDialog!=null && shareDialog.isShowing()){
            shareDialog.cancel();
        }
    }

    public void startShareIntentProcess(){

        File imgFile = new  File( Environment.getExternalStorageDirectory().toString() + "/" + this.surveyId + ".jpg");

        if(imgFile.exists()){

            Log.e("image exists","1");

            Uri uri = Uri.parse(imgFile.toString());

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, "http://participateme.com/r/" + this.surveyId+"      (Because Your Opinion Matters)");
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.setType("image/*");
            startActivity(Intent.createChooser(shareIntent, "Share My Survey Via : "));



        }

        else{

            Log.e("image exists","0");

        Uri path = Uri.parse("android.resource://com.surveyapp/" + R.drawable.share_image);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "http://participateme.com/r/" + this.surveyId+"      (Because Your Opinion Matters)");
        shareIntent.putExtra(Intent.EXTRA_STREAM, path);
        shareIntent.setType("image/*");

        startActivity(Intent.createChooser(shareIntent, "Share My Survey Via : "));}


    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public String getStringImage(Bitmap bmp){
        if (bmp!=null){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            return Base64.encodeToString(imageBytes, Base64.DEFAULT);}

        return null;
    }


    private void takeScreenshot() {

        String now = this.surveyId;


        submitButton.setVisible(false);
        toolbar.setNavigationIcon(null);
        addNewQuestionToSurvey.setVisibility(View.GONE);



        try {
            // image naming and path  to include sd card  appending name you choose for file
            //Utils.toastL(getApplicationContext(),"ScreenShot Taken");
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

           // openScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or OOM
            e.printStackTrace();
        }

        submitButton.setVisible(true);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        addNewQuestionToSurvey.setVisibility(View.VISIBLE);


    }

    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }

}





