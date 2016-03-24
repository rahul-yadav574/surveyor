package com.surveyapp.CustomObjects;

import java.util.List;

/**
 * Created by Rahul Yadav on 07-03-2016.
 */
public class Survey {

    private String surveyTitle;
    private String surveyID;
    private List<Question> questionList;
    private String dateCreated;



    public Survey(String surveyTitle, List<Question> questionList) {
        this.surveyTitle = surveyTitle;
        this.questionList = questionList;
    }

    public Survey(String surveyTitle, String dateCreated , String surveyID) {
        this.surveyTitle = surveyTitle;
        this.dateCreated = dateCreated;
        this.surveyID = surveyID;
    }

    public String getDateCreated() {
        return this.dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getSurveyTitle() {
        return this.surveyTitle;
    }

    public List<Question> getQuestionList() {
        return this.questionList;
    }

    public String getSurveyID(){
        return this.surveyID;
    }
}
