package com.surveyapp.CustomObjects;

import java.util.List;

/**
 * Created by Rahul Yadav on 05-03-2016.
 */
public class Question {

    private String questionStatement;
    private List<String> choicesList;


    public Question(String questionStatement, List<String> choicesList) {
        this.questionStatement = questionStatement;
        this.choicesList = choicesList;
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
