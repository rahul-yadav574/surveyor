package com.surveyapp.CustomObjects;

/**
 * Created by Rahul Yadav on 05-02-2016.
 */
public class User {

    private String name;
    private String email;
    private String password;
    private String plan;
    private int loginType;
    private int id;

    public User() {
    }

    public User(int id,String name, String email, String password, String plan,int loginType) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.plan = plan;
        this.loginType =loginType;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
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

    public int getLoginType() {
        return this.loginType;
    }

    public void setLoginType(int loginType) {
        this.loginType = loginType;
    }
}
