package com.example.super_app.db.entity;

public class User {

    //constants for Database
    public static final String TABLE_NAME = "Users";
    public static final String COLUMN_ID = "user_id";
    public static final String COLUMN_NAME = "user_name";
    public static final String COLUMN_EMAIL ="user_email";
    public static final String COLUMN_PASSWORD ="user_password";
    public static final String COLUMN_ADDRESS ="user_address";

    // var
    private String userName;
    private String userEmail;
    private String userPassword;
    private String userAdd;
    private String birthdate;

    public User() {

    }


    public User(String userName, String userEmail, String userPassword, String userAdd, String birthdate) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userAdd = userAdd;
        this.birthdate = birthdate;
    }
    public User(String userName,String userAdd, String birthdate) {
        this.userName = userName;
        this.userAdd = userAdd;
        this.birthdate = birthdate;
    }

    public User(String userName, String userEmail, String userAdd, String birthdate) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userAdd = userAdd;
        this.birthdate = birthdate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserAdd() {
        return userAdd;
    }

    public void setUserAdd(String userAdd) {
        this.userAdd = userAdd;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }
}
