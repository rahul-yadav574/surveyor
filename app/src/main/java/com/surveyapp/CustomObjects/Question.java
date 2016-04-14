package com.surveyapp.CustomObjects;

import java.util.List;

/**
 * Created by Rahul Yadav on 05-03-2016.
 */
public class Question {

    private String questionStatement;
    private List<String> choicesList;
    private String questionImage;
    private String questionImageName;

    public Question() {
    }

    public Question(String questionStatement, List<String> choicesList, String questionImage, String questionImageName) {
        this.questionStatement = questionStatement;
        this.choicesList = choicesList;
        this.questionImage = questionImage;
        this.questionImageName = questionImageName;
    }

    public Question(String questionStatement, List<String> choicesList) {
        this.questionStatement = questionStatement;
        this.choicesList = choicesList;
    }

    public String getQuestionImage() {
        return this.questionImage;
    }

    public void setQuestionImage(String questionImage) {
        this.questionImage = questionImage;
    }

    public String getQuestionImageName() {
        return this.questionImageName;
    }

    public void setQuestionImageName(String questionImageName) {
        this.questionImageName = questionImageName;
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
