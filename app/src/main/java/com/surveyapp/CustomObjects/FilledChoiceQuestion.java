package com.surveyapp.CustomObjects;

import java.util.List;

/**
 * Created by Rahul Yadav on 23-03-2016.
 */
public class FilledChoiceQuestion {

    private boolean isFilled;
    private int choiceNo;
    private String questionStatement;
    private List<String> choicesList;

    public FilledChoiceQuestion(boolean isFilled,String questionStatement, List<String> choicesList, int choiceNo ) {
        this.isFilled = isFilled;
        this.choicesList = choicesList;
        this.choiceNo = choiceNo;
        this.questionStatement = questionStatement;
    }

    public boolean isFilled() {
        return this.isFilled;
    }

    public void setIsFilled(boolean isFilled) {
        this.isFilled = isFilled;
    }

    public int getChoiceNo() {
        return this.choiceNo;
    }

    public void setChoiceNo(int choiceNo) {
        this.choiceNo = choiceNo;
    }

    public String getQuestionStatement() {
        return this.questionStatement;
    }

    public void setQuestionStatement(String questionStatement) {
        this.questionStatement = questionStatement;
    }

    public List<String> getChoicesList() {
        return this.choicesList;
    }

    public void setChoicesList(List<String> choicesList) {
        this.choicesList = choicesList;
    }
}
