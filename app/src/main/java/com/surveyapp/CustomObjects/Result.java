package com.surveyapp.CustomObjects;

import java.util.List;

/**
 * Created by Rahul Yadav on 26-03-2016.
 */
public class Result {

    private String surveyID;
    private String questionStatement;
    private List<ResultChoice> choiceList;

    public Result(String surveyID, String questionStatement, List<ResultChoice> choiceList) {
        this.surveyID = surveyID;
        this.questionStatement = questionStatement;
        this.choiceList = choiceList;
    }


    public String getSurveyID() {
        return this.surveyID;
    }

    public void setSurveyID(String surveyID) {
        this.surveyID = surveyID;
    }

    public String getQuestionStatement() {
        return this.questionStatement;
    }

    public void setQuestionStatement(String questionStatement) {
        this.questionStatement = questionStatement;
    }

    public List<ResultChoice> getChoiceList() {
        return this.choiceList;
    }

    public void setChoiceList(List<ResultChoice> choiceList) {
        this.choiceList = choiceList;
    }



}
