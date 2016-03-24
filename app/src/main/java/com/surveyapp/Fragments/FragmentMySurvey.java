package com.surveyapp.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

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

/**
 * Created by abhey singh on 01-02-2016.
 */
public class FragmentMySurvey extends Fragment {

    private RecyclerView mySurveyList;
    private ProgressBar loadingBar;
    private MySurveyAdapter adapter;
    private SharedPrefUtil sharedPrefUtil;

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
        loadingBar = (ProgressBar) rootView.findViewById(R.id.mySurveyLoadingBar);
        adapter = new MySurveyAdapter(getActivity(),new ArrayList<Survey>());
        mySurveyList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mySurveyList.addItemDecoration(new DividerItemDecoration(getActivity().getDrawable(R.drawable.dividers)));
        mySurveyList.setAdapter(adapter);

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



            for (int i=0;i<=jsonArray.length();i++){
                try {
                    JSONObject oneSurvey = jsonArray.getJSONObject(i);
                    surveyList.add(convertItemDetails(oneSurvey));
                }
                catch (JSONException j){
                    j.printStackTrace();
                }
            }

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.changeList(surveyList);
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
}
