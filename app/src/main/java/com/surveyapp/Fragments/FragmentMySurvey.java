package com.surveyapp.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.surveyapp.Adapters.MySurveyAdapter;
import com.surveyapp.CustomObjects.Survey;
import com.surveyapp.R;
import com.surveyapp.SharedPrefUtil;
import com.surveyapp.Utilities.DividerItemDecoration;
import com.surveyapp.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by abhey singh on 01-02-2016.
 */
public class FragmentMySurvey extends Fragment {

    private RecyclerView mySurveyList;
    private ProgressBar loadingBar;
    private MySurveyAdapter adapter;
    private SharedPrefUtil sharedPrefUtil;
    private TextView noDataText;
    private FloatingActionButton addnewSurvey;
    private RecyclerView myTakenSurveysList;
    private MySurveyAdapter takenSurveysAdapter;

    public FragmentMySurvey() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPrefUtil = new SharedPrefUtil(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_surveys,container,false);

        getActivity().setTitle("My Surveys");

        mySurveyList = (RecyclerView) rootView.findViewById(R.id.mySurveysList);
        myTakenSurveysList = (RecyclerView) rootView.findViewById(R.id.myTakenSurveysList);
        loadingBar = (ProgressBar) rootView.findViewById(R.id.mySurveyLoadingBar);
        noDataText = (TextView) rootView.findViewById(R.id.noDataText);
        addnewSurvey = (FloatingActionButton) rootView.findViewById(R.id.createNewSurveyFromMySurvey);
        adapter = new MySurveyAdapter(getActivity(),new ArrayList<Survey>());
        takenSurveysAdapter = new MySurveyAdapter(getActivity(),new ArrayList<Survey>());
        myTakenSurveysList.setLayoutManager(new LinearLayoutManager(getActivity()));
        myTakenSurveysList.addItemDecoration(new DividerItemDecoration(getActivity().getResources().getDrawable(R.drawable.dividers)));
        mySurveyList.setLayoutManager(new LinearLayoutManager(getActivity()));
        myTakenSurveysList.setAdapter(takenSurveysAdapter);
        mySurveyList.addItemDecoration(new DividerItemDecoration(getActivity().getResources().getDrawable(R.drawable.dividers)));
        mySurveyList.setAdapter(adapter);

        addnewSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().setTitle("Home");
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        new MySurveyLoader().execute();


        return rootView;
    }

    protected class MySurveyLoader extends AsyncTask<String,Void,Void>{


        public MySurveyLoader() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            loadingBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(String... params) {

            String requestUrl = "http://contactsyncer.com/mysurvey.php?userID="+sharedPrefUtil.getUserInfo().getId();
            String response = Utils.getStringFromUrl(requestUrl);

            JSONArray jsonArray = null;
            final List<Survey> surveyList = new ArrayList<>();

            try{
                jsonArray = new JSONArray(response);}
            catch (JSONException j){
                j.printStackTrace();
            }


            if(jsonArray==null){

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Utils.toastL(getActivity(),"Check Internet Connection");
                    }
                });
                return null;
            }

            String requestUrl1 = "http://www.contactsyncer.com/usertakensurveys.php?userID="+sharedPrefUtil.getUserInfo().getId();
            String response1 = Utils.getStringFromUrl(requestUrl1);

            //Log.e("mytak",response1);

            //Log.e("url to call",""+requestUrl1);
            JSONArray jsonArray1 = null;
            final List<Survey> surveyList1 = new ArrayList<>();

            try{
                jsonArray1 = new JSONArray(response1);}
            catch (JSONException j){
                j.printStackTrace();
            }


            if(jsonArray1==null){

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Utils.toastL(getActivity(),"Check Internet Connection");
                    }
                });
                return null;
            }


            for (int i=0;i<=jsonArray.length();i++){
                try {
                    JSONObject oneSurvey = jsonArray.getJSONObject(i);
                    surveyList.add(convertItemDetails(oneSurvey));
                }
                catch (JSONException j){
                    j.printStackTrace();
                }
            }


            for (int i=0;i<=jsonArray1.length();i++){
                try {
                    JSONObject oneSurvey = jsonArray1.getJSONObject(i);
                    surveyList1.add(convertMyTakenItemDetails(oneSurvey));
                }
                catch (JSONException j){
                    j.printStackTrace();
                }
            }

            Log.e("sizes are","sur"+surveyList.size()+"   myta"+surveyList1.size());

            if(!(surveyList.size()>0 || surveyList1.size()>0)){

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        noDataText.setVisibility(View.VISIBLE);
                        addnewSurvey.setVisibility(View.VISIBLE);


                    }
                });

            }

            else {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        noDataText.setVisibility(View.GONE);
                    }
                });

            }

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    addnewSurvey.setVisibility(View.VISIBLE);
                    adapter.changeList(surveyList);
                    adapter.addItems(surveyList1);
                    takenSurveysAdapter.changeList(surveyList1);
                }
            });


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            loadingBar.setVisibility(View.GONE);
        }


    }

    private Survey convertItemDetails(JSONObject jsonObject) throws JSONException{


        String surveyTitle = jsonObject.getString("SurveyInfo");
        String dateCreated = jsonObject.getString("DateCreated");
        String surveyID = jsonObject.getString("SurveyID");

        return new Survey(surveyTitle,dateCreated,surveyID);

    }

    private Survey convertMyTakenItemDetails(JSONObject jsonObject) throws JSONException{

        String title = jsonObject.getString("SurveyTitle");
        String surveyID = jsonObject.getString("SurveyID");

        return new Survey(title,getActivity().getResources().getString(R.string.survey_taken_by_you_hint),surveyID);

    }
}
