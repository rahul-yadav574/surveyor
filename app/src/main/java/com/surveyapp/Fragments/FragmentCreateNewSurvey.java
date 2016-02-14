package com.surveyapp.Fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.surveyapp.Activities.EditSurveyActivity;
import com.surveyapp.Activities.TemplatesActivity;
import com.surveyapp.Adapters.TemplateSurveyListAdapter;
import com.surveyapp.CustomObjects.TemplateSurveyObject;
import com.surveyapp.R;
import com.surveyapp.SharedPrefUtil;
import com.surveyapp.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Rahul yadav on 31-01-2016.
 */
public class FragmentCreateNewSurvey extends Fragment {

    private Button createNewSurveyButton;
    private MaterialDialog dialog;
    private SharedPrefUtil sharedPrefUtil;


    public FragmentCreateNewSurvey() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPrefUtil = new SharedPrefUtil(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_create_survey,container,false);

        createNewSurveyButton = (Button) rootView.findViewById(R.id.createNewSurveyButton);

        dialog = new MaterialDialog.Builder(getActivity())
                .customView(getDialogView(), false)
                .build();

        createNewSurveyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();        //it will show a dialog allow user to choose options to crete new survey or choose a template
            }
        });

        return rootView;
    }

    private void showDialog(){
        if (dialog!=null && !dialog.isShowing()){
            dialog.show();
        }
    }

    private View getDialogView(){

        //layout of this dialog defined in xml

        View dialogView  = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_create_survey,new LinearLayout(getActivity()),false);

        Button createNewSurvey = (Button) dialogView.findViewById(R.id.dialogCreateNewSurveyButton);
        Button createNewFromTemplate = (Button) dialogView.findViewById(R.id.dialogCreateNewSurveyFromTemplateButton);
        Button cancelDialog = (Button) dialogView.findViewById(R.id.dialogCancelButton);

        createNewSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog!=null && dialog.isShowing()){
                    dialog.cancel();
                }

                new MaterialDialog.Builder(getActivity())
                        .content("Choose A Title")
                        .input(null, null, false, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {

                                String surveyTitle = input.toString();

                                new CreateNewSurveyExecutor().execute(surveyTitle);
                            }
                        })
                .show();
            }
        });

        createNewFromTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog!=null && dialog.isShowing()){
                    dialog.cancel();
                }
                startTemplateActivity();
            }
        });

        cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog!=null && dialog.isShowing()){
                    dialog.cancel();
                }
            }
        });
        return dialogView;
    }

    private void startTemplateActivity(){
        startActivity(new Intent(getActivity(), TemplatesActivity.class));
    }

    protected class CreateNewSurveyExecutor extends AsyncTask<String,Void,Void>{

        MaterialDialog dialog;
        String surveyId = null;
        String surveyTitle = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new MaterialDialog.Builder(getActivity())
                    .content("Creating Survey")
                    .progress(true,100)
                    .build();

            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {

            String userId = String.valueOf(sharedPrefUtil.getUserInfo().getId());

            Calendar calendar = Calendar.getInstance();

            int dd = calendar.get(Calendar.DATE);
            int mm = calendar.get(Calendar.MONTH);
            int yyyy = calendar.get(Calendar.YEAR);

            String date = dd + "-" + mm + "-" + "-" + yyyy;

            String requestUrl = "http://contactsyncer.com/surveyinfo.php?title="+params[0]+"&userID="+userId+"&category="+"null"+"&date="+date;

            JSONObject response = Utils.getJSONFromUrl(requestUrl);

            try{
                surveyId = response.getString("surveyID");}
            catch (JSONException j){
                j.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            dialog.cancel();

            if (surveyId!=null){
                Bundle extraBundle = new Bundle();
                extraBundle.putString("surveyTitle",surveyTitle);
                extraBundle.putString("surveyId",surveyId);

                Intent startNewSurveyEditing = new Intent(getActivity(), EditSurveyActivity.class);
                startNewSurveyEditing.putExtras(extraBundle);
                startActivity(startNewSurveyEditing);
            }

            else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       Utils.toastL(getActivity(),"Problem Creating Survey");
                    }
                });
            }


        }


    }

}
