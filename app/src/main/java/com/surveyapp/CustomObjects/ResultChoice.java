package com.surveyapp.CustomObjects;

/**
 * Created by Rahul Yadav on 26-03-2016.
 */
public class ResultChoice {

    private String choiceStatement;
    private String choiceResponseCount;

    public ResultChoice(String choiceStatement, String choiceResponseCount) {
        this.choiceStatement = choiceStatement;
        this.choiceResponseCount = choiceResponseCount;
    }

    public String getChoiceResponseCount() {
        return this.choiceResponseCount;
    }

    public void setChoiceResponseCount(String choiceResponseCount) {
        this.choiceResponseCount = choiceResponseCount;
    }

    public String getChoiceStatement() {
        return this.choiceStatement;
    }

    public void setChoiceStatement(String choiceStatement) {
        this.choiceStatement = choiceStatement;
    }
}
