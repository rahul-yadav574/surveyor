package com.surveyapp.CustomObjects;

/**
 * Created by Rahul Yadav on 05-02-2016.
 */
public class User {

    private String name;
    private String email;
    private String password;
    private String plan;

    public User(String name, String email, String password, String plan) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.plan = plan;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPlan() {
        return this.plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }
}
